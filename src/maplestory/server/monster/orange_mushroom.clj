(ns maplestory.server.monster.orange-mushroom)

(def spawn-state {:type :orange-mushroom
                  :direction :right
                  :step 20
                  :action :stand})

(defn stand! [state]
  (send state (fn [monster] (assoc monster :action :stand)))
  (Thread/sleep 500))

(def actions [stand!])

