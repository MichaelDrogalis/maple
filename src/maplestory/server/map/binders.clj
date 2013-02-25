(ns maplestory.server.map.binders
  (:require [maplestory.server.movement :refer [scheduler]]))

(def maps (atom {}))

(defn register-map! [map-name]
  (swap! maps assoc map-name {:connections (ref #{}) :entities (atom #{})}))

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

(defn spawn [map-name entity actions]
  (let [connections (get-in @maps [map-name :connections])
        entities    (get-in @maps [map-name :entities])]
    (swap! entities conj entity)
    (future (scheduler entity actions))
    (doseq [connection @connections]
      (bind-client-to-monster connection entity)
      (add-client-watch connection entity))))

