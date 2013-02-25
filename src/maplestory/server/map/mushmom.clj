(ns maplestory.server.map.mushmom
  (:require [maplestory.server.monster.stump :as stump]
            [maplestory.server.movement :refer [scheduler]]
            [maplestory.server.map.binders :as b]))

(def specs {:width  1176
            :height 1154})

(let [map-name :mushmom]
  (b/register-map! map-name)
  (b/spawn map-name (stump/birth :origin {:x 300 :y 560} :x-span 100) stump/actions)
  (b/spawn map-name (stump/birth :origin {:x 500 :y 560} :x-span 100) stump/actions))

