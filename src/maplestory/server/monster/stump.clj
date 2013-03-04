(ns maplestory.server.monster.stump
  (:require [maplestory.server.physics :refer [should-turn-around? flip units-in-direction drop-elevation
                                               total-pixels-moved ms-for-pixels ms-for-gravity]]))

(def spawn-state {:type :stump
                  :direction :left
                  :speed 10
                  :action :walk})

(defn change-position [{:keys [position speed direction boundaries map] :as monster}]
  (let [dst-x (+ (:x position) (units-in-direction position speed direction boundaries))
        dst-y (drop-elevation dst-x (get-in monster [:position :y]) (:footing map))
        cooldown (+ (ms-for-pixels (total-pixels-moved (:x position) dst-x) speed)
                    (ms-for-gravity (Math/abs (- (:y position) dst-y))))]
    (-> monster
        (assoc-in [:position :x] dst-x)
        (assoc-in [:position :y] dst-y)
        (assoc-in [:transient :sleep-ms] cooldown)
        (assoc :action :move))))

(defn move [{:keys [position direction boundaries] :as monster}]
  (if (should-turn-around? position direction boundaries)
    (flip monster)
    (change-position monster)))

(defn scheduler [agent]
  (fn [state]
    (send agent move)))

