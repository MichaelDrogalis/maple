(ns maplestory.server.map.mushmom
  (:require [maplestory.server.monster.stump :as stump]
            [maplestory.server.movement :refer [scheduler]]
            [maplestory.server.map.binders :refer [register-map! spawn birth]]))

(def specs {:width  1176
            :height 1154})

(let [map-name :mushmom]
  (register-map! map-name)
  (spawn map-name (birth stump/spawn-state :origin {:x 300 :y 560} :x-span 100) stump/actions)
  (spawn map-name (birth stump/spawn-state :origin {:x 500 :y 560} :x-span 100) stump/actions))

