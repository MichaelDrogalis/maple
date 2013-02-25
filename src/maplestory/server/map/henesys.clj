(ns maplestory.server.map.henesys
  (:require [maplestory.server.npc.sera :as sera]
            [maplestory.server.monster.stump :as stump]
            [maplestory.server.map.binders :as b]))

(def specs {:width  1241
            :height 613})

(def connections (ref #{}))
(def entities (atom #{}))

(let [map-name :henesys]
  (b/register-map! map-name)
  (b/spawn map-name (sera/birth) sera/actions)
  (b/spawn map-name (stump/birth :origin {:x 600 :y 496}) stump/actions))

