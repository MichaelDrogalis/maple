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

(defn scheduler [agent actions]
  (doseq [action (shuffle actions)]
    (if (sequential? action)
      (doseq [subaction action]
        (subaction agent))
      (action agent)))
  (recur agent actions))

(defn platform [& {:keys [from to on]}]
  (reduce #(conj %1 {:x %2 :y on}) #{} (range from (inc to))))

(defn entity-scheduler [entity f p]
  (send entity f)
  (Thread/sleep (:sleep-ms (:transient @entity)))
  ;;; todo: Strip transient map here.
  (deliver p))

(defn ms-for-pixels [length rate]
  (/  (* 140 length) rate))

(defn total-pixels-moved [old-x old-y new-x new-y]
  (int (Math/sqrt (+ (Math/pow (Math/abs (- new-x old-x)) 2)
                     (Math/pow (Math/abs (- new-y old-y)) 2)))))

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

