(ns maplestory.server.map.binders
  (:require [maplestory.server.movement :refer [scheduler]]))

(defn bind-client-to-monster [connection entity]
  (.send connection (pr-str {:type :init :message {(:type @entity) @entity}})))

(defn add-client-watch [connection entity]
  (add-watch entity
             connection
             (fn [_ _ _ state]
               (.send connection (pr-str {:type :update :message {:who (:type @entity) :event @entity}})))))

(defn register-client [connection connections entities]
  (dosync
   (commute connections conj connection)
   (doseq [entity @entities]
     (bind-client-to-monster connection entity)
     (add-client-watch connection entity))))

(defn unregister-client [connection connections entities]
  (dosync
   (commute connections disj connection)
   (doseq [entity @entities]
     (remove-watch entity connection))))

(defn spawn-entity [connections entities entity actions]
  (swap! entities conj entity)
  (future (scheduler entity actions))
  (doseq [connection @connections]
    (bind-client-to-monster connection entity)
    (add-client-watch connection entity)))

