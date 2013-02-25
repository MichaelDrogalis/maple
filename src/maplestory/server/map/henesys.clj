(ns maplestory.server.map.henesys
  (:require [maplestory.server.npc.sera :as sera]
            [maplestory.server.monster.stump :as stump]
            [maplestory.server.map.binders :refer [register-map! spawn birth]]))

(def specs {:width  1241
            :height 613})

(let [map-name :henesys]
  (register-map! map-name)
  (spawn map-name (birth sera/spawn-state) sera/actions)
  (spawn map-name (birth stump/spawn-state :origin {:x 600 :y 496}) stump/actions))

