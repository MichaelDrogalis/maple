(ns maplestory.server.monster.stump
  (:require [maplestory.server.movement :refer [can-move-right? can-move-left? stand! flip! flip]]))

(def spawn-state {:type :stump
                  :direction :left
                  :step 10
                  :action :walk})

(defn should-turn-around? [{:keys [x]} direction boundaries]
  (let [{:keys [left right]} (:x boundaries)]
    (if (= direction :right)
      (>= x right)
      (<= x left))))

(defn units-in-direction [{:keys [x]} step-length direction boundaries]
  (if (= direction :right)
    (if (<= step-length (- (:right (:x boundaries)) x))
      step-length
      (- (:right (:x boundaries)) x))
    (if (<= step-length (- x (:left (:x boundaries))))
      (* -1 step-length)
      (* -1 (- x (:left (:x boundaries)))))))

(defn drop-elevation [target-x current-y footing]
  (if (get footing {:x target-x :y current-y})
    current-y
    (recur target-x (inc current-y) footing)))

(defn move! [state]
  (send state
        (fn [{:keys [position step direction boundaries map] :as monster}]
          (let [target-x (+ (:x position) (units-in-direction position step direction boundaries))]
            (assoc
                (assoc-in
                 (assoc-in monster [:position :x] target-x)
                 [:position :y] (drop-elevation target-x (get-in monster [:position :y]) (:footing map)))
              :action :walk))))
  (Thread/sleep 1000))

(defn move [state]
  (let [{:keys [position direction boundaries]} @state]
    (if (should-turn-around? position direction boundaries)
      (flip! state)
      (move! state))))

(def actions [move])

