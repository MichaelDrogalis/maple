(ns client.npc.sera
  (:require [client.animation :refer [swap-image!]]))

(def jquery (js* "$"))
(def sera "#sera")

(def swap-sera! (partial swap-image! sera "sera-queue"))

(defn init [{:keys [x direction]}]
  (.css (jquery sera) "left" x)
  (if (= direction :right)
    (.addClass (jquery sera) "mirrored")))

(defmulti update :action)

(defmethod update :blink [_]
  (swap-sera! 100 ["sera-blink0" "sera-blink1" "sera-blink2" "sera-blink3"
                    "sera-blink4" "sera-blink5" "sera-blink6" "sera-blink7"
                    "sera-blink8"])
  (swap-sera! 0 ["sera-stand0"]))

(defmethod update :hair [_]
  (swap-sera! 300 ["sera-hair0" "sera-hair1" "sera-hair2"]))

(defmethod update :smile [_]
  (swap-sera! 200 ["sera-smile0"])
  (swap-sera! 1500 ["sera-smile1"]))

(defmethod update :alert [_]
  (swap-sera! 500 ["sera-alert0" "sera-alert1"]))

(defmethod update :angry [_]
  (swap-sera! 500 ["sera-angry0"])
  (swap-sera! 1500 ["sera-angry0"]))

(defmethod update :walk [{:keys [x]}]
  (swap-sera! 300 ["sera-move0" "sera-move1" "sera-move2" "sera-move3"])
  (.animate (jquery sera) (clj->js {:left x}) 1000))

(defmethod update :flip [{:keys [direction]}]
  (if (= direction :left)
    (.removeClass (jquery sera) "mirrored")
    (.addClass (jquery sera) "mirrored")))

(defmethod update :default [])

