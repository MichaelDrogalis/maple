(ns maplestory.server.npc.sera
  (:require [maplestory.server.movement :refer [flip-direction scheduler]]))

(def max-left 390)
(def max-right 800)
(def discrete-step 40)
(def sera (agent {:x max-right :direction :left :action :walk}))

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
        (fn [{:keys [x direction] :as npc}]
          (cond (and (= direction :left) (> x max-left)) (assoc npc :action :walk :x (- x discrete-step))
                (and (= direction :left) (<= x max-left)) (flip npc)
                (and (= direction :right) (< x max-right)) (assoc npc :action :walk :x (+ x discrete-step))
                (and (= direction :right) (>= x max-right)) (flip npc))))
  (Thread/sleep 1000))

(def runner (future (scheduler sera [walk walk blink walk walk smile walk walk blink walk walk hair walk alert angry walk blink pause])))

