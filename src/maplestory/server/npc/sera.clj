(ns maplestory.server.npc.sera
  (:require [maplestory.server.movement :refer [flip-direction can-move-right? can-move-left?]]))

(def x-span 200)
(def discrete-step 40)
(def spawn-state {:direction :left :action :walk})

(defn blink [state]
  (send state (fn [npc] (assoc npc :action :blink)))
  (Thread/sleep 900))

(defn smile [state]
  (send state (fn [npc] (assoc npc :action :smile)))
  (Thread/sleep 1600))

(defn hair [state]
  (send state (fn [npc] (assoc npc :action :hair)))
  (Thread/sleep 900))

(defn alert [state]
  (send state (fn [npc] (assoc npc :action :alert)))
  (Thread/sleep 1000))

(defn angry [state]
  (send state (fn [npc] (assoc npc :action :angry)))
  (Thread/sleep 2000))

(defn pause [state]
  (send state (fn [npc] (assoc npc :action :pause)))
  (Thread/sleep 3000))

(defn flip [{:keys [direction] :as npc}]
  (assoc npc :action :flip :direction (flip-direction direction)))

(defn walk [state]
  (send state
        (fn [{:keys [x x-origin direction] :as npc}]
          (cond (can-move-left? x-origin x x-span direction) (assoc npc :action :walk :x (- x discrete-step))
                (can-move-right? x-origin x x-span direction) (assoc npc :action :walk :x (+ x discrete-step))
                :else (flip npc))))
  (Thread/sleep 1000))

(def actions [walk walk blink walk walk smile walk walk blink walk walk hair walk alert angry walk blink pause])

