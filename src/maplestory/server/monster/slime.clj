(ns maplestory.server.monster.slime
  (:require [maplestory.server.movement :refer [flip-direction can-move-right? can-move-left?]]))

(def x-span 205)
(def discrete-step 40)
(def spawn-state {:type :slime :direction :left :action :stand :y 500})

(defn stand [state]
  (send state (fn [monster] (assoc monster :action :stand)))
  (Thread/sleep 800))

(defn flip-action [state]
  (send state
        (fn [{:keys [direction] :as monster}]
          (assoc monster :action :flip :direction (flip-direction direction)))))

(defn move-subactions [state f]
  (let [xs [5 5 5 15 5 5 5]
        ys [-5 -5 -10 -30 0 30 20]
        ns  (range)]
    (doseq [[x-inc y-inc n] (map list xs ys ns)]
      (send state
            (fn [{:keys [action subaction x y] :as monster}]
              (assoc monster :action :walk :subaction n :x (f x x-inc) :y (+ y y-inc))))
      (Thread/sleep 142))))

(defn move-subactions-right [state]
  (move-subactions state +))

(defn move-subactions-left [state]
  (move-subactions state -))

(defn move [state]
  (let [{:keys [x origin direction] :as monster} @state]
    (cond (can-move-left? (:x origin) x x-span direction) (move-subactions-left state)
          (can-move-right? (:x origin) x x-span direction) (move-subactions-right state)
          :else (flip-action state))))

(def actions [move move move stand flip-action])

(defn birth [& {:as options}]
  (let [monster-data (merge spawn-state options (:origin options) {:id (name (gensym))})]
    (agent monster-data)))

