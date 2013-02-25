(ns maplestory.server.map.henesys
  (:require [maplestory.server.npc.sera :as sera]
            [maplestory.server.monster.stump :as stump]
            [maplestory.server.map.binders :as b]))

(def specs {:width  1241
            :height 613})

(def connections (ref #{}))
(def entities (atom #{}))

(def spawn-in-henesys
  (fn [npc actions]
    (b/spawn-entity connections entities npc actions)))

(defn spawn! []
  (let [sera-npc (sera/birth)
        stump-monster (stump/birth :origin {:x 600 :y 496})]
    (spawn-in-henesys sera-npc sera/actions)
    (spawn-in-henesys stump-monster stump/actions)))

(spawn!)

