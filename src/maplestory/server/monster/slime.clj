(ns maplestory.server.monster.slime
  (:require [maplestory.server.movement :refer [flip-direction scheduler]]))

(def max-left 390)
(def max-right 800)
(def discrete-step 40)
(def min-height 500)

(def slime (agent {:x max-right :direction :left :action :stand}))

(defn stand [state]
  (send state (fn [monster] (assoc monster :action :stand)))
  (Thread/sleep 800))

(defn flip [{:keys [direction] :as monster}]
  (assoc monster :action :flip :direction (flip-direction direction)))

(defn move [state]
  (send state
        (fn [{:keys [x direction] :as monster}]
          (cond (and (= direction :left) (> x max-left)) (assoc monster :action :walk :x (- x discrete-step))
                (and (= direction :left) (<= x max-left)) (flip monster)
                (and (= direction :right) (< x max-right)) (assoc monster :action :walk :x (+ x discrete-step))
                (and (= direction :right) (>= x max-right)) (flip monster))))
  (Thread/sleep 1000))

(def runner (future (scheduler slime [stand move move move stand move move])))

