(ns maplestory.server.map.mushmom
  (:require [maplestory.server.monster.stump :as stump]
            [maplestory.server.movement :refer [scheduler]]
            [maplestory.server.map.binders :as b]))

(def specs {:width  1176
            :height 1154})

(def connections (ref #{}))
(def entities (atom #{}))

(def spawn-in-mushmom
  (fn [npc actions]
    (b/spawn-entity connections entities npc actions)))

(defn spawn! []
  (doseq [_ (range 5)]
    (let [monster (stump/birth :origin {:x 550 :y 560})]
      (spawn-in-mushmom monster stump/actions))))

(spawn!)

