(ns maplestory.server.map.mushmom
  (:require [maplestory.server.monster.stump :as stump]
            [maplestory.server.movement :refer [scheduler]]))

(def specs {:width  1176
            :height 1154})

(def connections (ref #{}))
(def entities (atom #{}))

(defn bind-client-to-monster [connection entity]
  (.send connection (pr-str {:type :init :message {(:type @entity) @entity}})))

(defn add-client-watch [connection entity]
  (add-watch entity
             connection
             (fn [_ _ _ state]
               (.send connection (pr-str {:type :update :message {:who (:type @entity) :event @entity}})))))

(defn register-client [connection]
  (dosync
   (commute connections conj connection)
   (doseq [entity @entities]
     (bind-client-to-monster connection entity)
     (add-client-watch connection entity))))

(defn unregister-client [connection]
  (dosync
   (commute connections disj connection)
   (doseq [entity @entities]
     (remove-watch entity connection))))

(defn spawn-entity [entity actions]
  (swap! entities conj entity)
  (future (scheduler entity actions))
  (doseq [connection @connections]
    (bind-client-to-monster connection entity)
    (add-client-watch connection entity)))

(defn spawn! []
  (doseq [_ (range 5)]
    (spawn-entity (stump/birth :origin {:x 550 :y 560}) stump/actions)))

(spawn!)

