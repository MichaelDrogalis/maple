(ns maplestory.server.monster.stump
  (:require [maplestory.server.physics :refer [should-turn-around? stand! flip! flip units-in-direction drop-elevation]]))

(def spawn-state {:type :stump
                  :direction :left
                  :step 10
                  :action :walk})

(defn move! [state]
  (send state
        (fn [{:keys [position step direction boundaries map] :as monster}]
          (let [target-x (+ (:x position) (units-in-direction position step direction boundaries))]
            (assoc
                (assoc-in
                 (assoc-in monster [:position :x] target-x)
                 [:position :y] (drop-elevation target-x (get-in monster [:position :y]) (:footing map)))
              :action :walk))))
  (Thread/sleep 450))

(defn move [state]
  (let [{:keys [position direction boundaries]} @state]
    (if (should-turn-around? position direction boundaries)
      (flip! state)
      (move! state))))

(def actions [move])

