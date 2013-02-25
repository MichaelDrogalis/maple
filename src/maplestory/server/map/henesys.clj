(ns maplestory.server.map.henesys
  (:require [maplestory.server.npc.sera :as sera]
            [maplestory.server.monster.stump :as stump]
            [maplestory.server.map.binders :as b]))

(def specs {:width  1241
            :height 613})

(def connections (ref #{}))
(def entities (atom #{}))

(defn spawn! []
  (let [sera-npc (sera/birth)
        stump-monster (stump/birth :origin {:x 600 :y 496})]
    (b/spawn-entity connections sera-npc entities sera/actions)
    (b/spawn-entity connections stump-monster entities stump/actions)))

(spawn!)

