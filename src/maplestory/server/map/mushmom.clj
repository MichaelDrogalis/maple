(ns maplestory.server.map.mushmom
  (:require [clojure.set :refer [union]]
            [maplestory.server.monster.stump :as stump]
            [maplestory.server.monster.axe-stump :as axe-stump]
            [maplestory.server.physics :refer [platform]]
            [maplestory.server.map.binders :refer [register-map! spawn birth maps]]))

(def specs {:width  1176
            :height 1154
            :footing (union (platform :from 249 :to 562  :on 240)
                            (platform :from 612 :to 742  :on 301)
                            (platform :from 165 :to 650  :on 423)
                            (platform :from 72  :to 746  :on 600)
                            (platform :from 72  :to 1108 :on 871))})

(let [map-name :mushmom]
  (register-map! map-name)
  (spawn map-name (birth axe-stump/spawn-state
                         :origin {:x 300 :y 600}
                         :boundaries {:x {:left 200 :right 500}}
                         :map specs
                         :direction :left)
         axe-stump/scheduler)
  (spawn map-name (birth stump/spawn-state
                         :origin {:x 500 :y 600}
                         :boundaries {:x {:left 300 :right 600}}
                         :map specs
                         :direction :right)
         stump/scheduler))

