(ns maplestory.server.monster.stump
  (:require [maplestory.server.movement :refer [can-move-right? can-move-left? stand! flip! flip]]))

(def spawn-state {:type :stump
                  :direction :left
                  :step 20
                  :origin {:x 650 :y 500}
                  :boundaries {:x {:left 550 :right 750}}
                  :action :walk})

(defn should-turn-around? [{:keys [x]} direction boundaries]
  (let [{:keys [left right]} (:x boundaries)]
    (if (= direction :right)
      (>= x right)
      (<= x left))))

(defn units-in-direction [{:keys [x]} step-length direction boundaries]
  (if (= direction :right)
    (if (>= step-length (- (:right (:x boundaries)) x))
      step-length
      (- (:right (:x boundaries)) x))
    (if (>= step-length (- x (:left (:x boundaries))))
      (* -1 step-length)
      (* -1 (- x (:left (:x boundaries)))))))

(defn move! [state]
  (send state
        (fn [{:keys [position step direction boundaries] :as monster}]
          (assoc-in monster
                    [:action] :walk
                    [:position :x] (+ (:x position)
                                      (units-in-direction position step direction boundaries)))))
  (Thread/sleep 1000))

(defn move [state]
  (let [{:keys [position direction boundaries]} @state]
    (if (should-turn-around? position direction boundaries)
      (flip! state)
      (move! state))))

(def actions [move])

