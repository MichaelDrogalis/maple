(ns maplestory.server.map.henesys
  (:require [maplestory.server.npc.sera :as sera]
            [maplestory.server.monster.stump :as stump]
            [maplestory.server.map.binders :refer [register-map! spawn birth]]))

(def specs {:width  1241
            :height 613})

