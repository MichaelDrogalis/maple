(ns maplestory.server.monster.slime
  (:require [maplestory.server.movement :refer [flip-direction can-move-right? can-move-left?]]))

(def x-span 205)
(def discrete-step 40)
(def spawn-state {:type :slime :direction :left :action :stand})

(defn stand [state]
  (send state (fn [monster] (assoc monster :action :stand)))
  (Thread/sleep 800))

(defn flip [{:keys [direction] :as monster}]
  (assoc monster :action :flip :direction (flip-direction direction)))

(defn flip-action [state]
  (send state (fn [monster] (flip monster))))

(defn move [state]
  (send state
        (fn [{:keys [x x-origin direction] :as monster}]
          (cond (can-move-left? x-origin x x-span direction) (assoc monster :action :walk :x (- x discrete-step))
                (can-move-right? x-origin x x-span direction) (assoc monster :action :walk :x (+ x discrete-step))
                :else (flip monster))))
  (Thread/sleep 1000))

(def actions [stand move move move stand move move flip-action])

