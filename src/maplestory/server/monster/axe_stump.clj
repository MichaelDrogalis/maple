(ns maplestory.server.monster.axe-stump
  (:require [maplestory.server.physics :refer [move]]))

(def spawn-state {:type :axe-stump
                  :direction :left
                  :speed 10
                  :action :walk})

(defn scheduler [agent]
  (fn [state]
    (send agent move)))

