(ns maplestory.server.map.henesys
  (:require [maplestory.server.npc.sera :as sera]
            [maplestory.server.monster.slime :as slime]
            [maplestory.server.movement :refer [scheduler]]))

(def specs {:width  1241
            :height 613})

(def entities (atom []))

(defn register-client [connection]
  (doseq [entity @entities]
    (.send connection (pr-str {:type :init :message {(:type @entity) @entity}}))
    (add-watch entity
               connection
               (fn [_ _ _ state]
                 (.send connection (pr-str {:type :update :message {:who (:type @entity) :event @entity}}))))))

(defn unregister-client [connection]
  (doseq [entity @entities]
    (remove-watch entity connection)))

(defn spawn! []
  (let [sera-npc (agent (merge sera/spawn-state {:x 750 :x-origin 750}))
        slime-monster (agent (merge slime/spawn-state {:x 800 :x-origin 800}))]
    (swap! entities conj sera-npc)
    (future (scheduler sera-npc sera/actions))
    (swap! entities conj slime-monster)
    (future (scheduler slime-monster slime/actions))))

(spawn!)

