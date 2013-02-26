(ns maplestory.server.movement)

(defn flip-direction [direction]
  (let [directions {:left :right
                    :right :left}]
    (get directions direction)))

(defn can-move-left? [origin current span direction]
  (and (< (- origin current) span)
       (= direction :left)))

(defn can-move-right? [origin current span direction]
  (and (< (- current origin) span)
       (= direction :right)))

(defn stand! [state]
  (send state (fn [monster] (assoc monster :action :stand)))
  (Thread/sleep 2000))

(defn flip [{:keys [direction] :as monster}]
  (assoc monster :action :flip :direction (flip-direction direction)))

(defn flip! [state]
  (send state flip))

(defn advance-horizontally [{:keys [origin x-step footing]}])

#_(defn move! [state]
  (let [x-motion (advance-horizontally @state)]
    (send state walk)
    (when (< x-motion x-step)
      (send state fall))))

#_(defn move [state]
  (let [{:keys [max-left max-right origin direction x-step]} @state]
    (cond (should-turn-around?) (flip! state)
          (move! state)))
  (send state
        (fn [{:keys [x x-span x-step origin direction] :as monster}]
          (cond (can-move-left? (:x origin) x x-span direction) (assoc monster :action :walk :x (- x x-step))
                (can-move-right? (:x origin) x x-span direction) (assoc monster :action :walk :x (+ x x-step))
                :else (flip monster))))
  (Thread/sleep 1000))

(defn scheduler [agent actions]
  (doseq [action (shuffle actions)]
    (if (sequential? action)
      (doseq [subaction action]
        (subaction agent))
      (action agent)))
  (recur agent actions))

(defn platform [& {:keys [from to on]}]
  (reduce #(conj %1 {:x %2 :y on}) #{} (range from (inc to))))

