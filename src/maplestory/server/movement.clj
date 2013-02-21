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

(defn scheduler [agent actions]
  (doseq [action actions]
    (action agent))
  (recur agent actions))

