(ns maplestory.server.monster.stump
  (:require [maplestory.server.movement :refer [flip-direction can-move-right? can-move-left?]]))

(def x-span 300)
(def discrete-step 20)
(def spawn-state {:type :stump :origin {:x 650 :y 500} :direction :left :action :walk})

(defn stand [state]
  (send state (fn [npc] (assoc npc :action :stand)))
  (Thread/sleep 2000))

(defn flip-action [state]
  (send state
        (fn [{:keys [direction] :as monster}]
          (assoc monster :action :flip :direction (flip-direction direction)))))

(defn walk [state]
  (send state
        (fn [{:keys [x origin direction] :as monster}]
          (cond (can-move-left? (:x origin) x x-span direction) (assoc monster :action :walk :x (- x discrete-step))
                (can-move-right? (:x origin) x x-span direction) (assoc monster :action :walk :x (+ x discrete-step))
                :else (flip-action state))))
  (Thread/sleep 1000))

(def actions [[walk walk walk walk] stand walk flip-action])

(defn birth [& {:as options}]
  (let [monster-data (merge spawn-state (:origin spawn-state) options (:origin options) {:id (name (gensym))})]
    (agent monster-data)))

