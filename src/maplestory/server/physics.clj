(ns maplestory.server.physics)

(defn flip-direction [direction]
  (get {:left :right :right :left} direction))

(defn stand! [state]
  (send state (fn [monster] (assoc monster :action :stand)))
  (Thread/sleep 2000))

(defn flip [{:keys [direction] :as monster}]
  (assoc monster :action :flip :direction (flip-direction direction)))

(defn flip! [state]
  (send state flip))

(defn platform [& {:keys [from to on]}]
  (reduce #(conj %1 {:x %2 :y on}) #{} (range from (inc to))))

(defn ms-for-pixels [length rate]
  (/ (* 140 length) rate))

(defn ms-for-gravity [length]
  (/ (* 140 length) 50))

(defn total-pixels-moved [old-x new-x]
  (Math/abs (- new-x old-x)))

(defn should-turn-around? [{:keys [x]} direction boundaries]
  (let [{:keys [left right]} (:x boundaries)]
    (if (= direction :right)
      (>= x right)
      (<= x left))))

(defn directionally [k direction]
  (let [k (Math/abs k)]
    (if (= direction :left)
      (* k -1)
      k)))

(defn units-in-direction [{:keys [x]} step-length direction boundaries]
  (let [length-to-boundary (- (get (:x boundaries) direction) x)]
    (if (<= step-length (Math/abs length-to-boundary))
      (directionally step-length direction)
      (directionally length-to-boundary direction))))

(defn drop-elevation [target-x current-y footing]
  (if (contains? footing {:x target-x :y current-y})
    current-y
    (recur target-x (inc current-y) footing)))

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

