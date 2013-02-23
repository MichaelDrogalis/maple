(ns maplestory.server.map.henesys
  (:require [maplestory.server.npc.sera :as sera]
            [maplestory.server.monster.slime :as slime]
            [maplestory.server.movement :refer [scheduler]]))

(def specs {:width  1241
            :height 613})

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
  (let [sera-npc (agent (merge sera/spawn-state {:x 750 :x-origin 750 :id (name (gensym))}))
        slime-monster (agent (merge slime/spawn-state {:x 800 :x-origin 800 :id (name (gensym))}))]
    (spawn-entity sera-npc sera/actions)
    (spawn-entity slime-monster (shuffle slime/actions))))

(spawn!)

