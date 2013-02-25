(ns maplestory.server.monster.stump
  (:require [maplestory.server.movement :refer [can-move-right? can-move-left? stand! flip! flip]]))

(def spawn-state {:type :stump :origin {:x 650 :y 500} :x-step 20
                  :direction :left :x-span 100 :action :walk})

(defn walk [state]
  (send state
        (fn [{:keys [x x-span x-step origin direction] :as monster}]
          (cond (can-move-left? (:x origin) x x-span direction) (assoc monster :action :walk :x (- x x-step))
                (can-move-right? (:x origin) x x-span direction) (assoc monster :action :walk :x (+ x x-step))
                :else (flip monster))))
  (Thread/sleep 1000))

(def actions [[walk walk walk walk] stand! walk flip!])

