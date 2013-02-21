(ns maplestory.server.movement)

(defn flip-direction [direction]
  (let [directions {:left :right
                    :right :left}]
    (get directions direction)))

(defn scheduler [agent actions]
  (doseq [action actions]
    (action agent))
  (recur agent actions))

