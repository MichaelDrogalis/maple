(ns client.monster.slime
  (:require [client.animation :refer [swap-image!]]))

(def jquery (js* "$"))
(def slime "#slime")

(def swap-slime! (partial swap-image! slime "slime-queue"))

(defn init [{:keys [x direction]}]
  (.css (jquery slime) "left" x)
  (if (= direction :right)
    (.addClass (jquery slime) "mirrored")))

(defmulti update :action)

(defmethod update :flip [{:keys [direction]}]
  (if (= direction :left)
    (.removeClass (jquery slime) "mirrored")
    (.addClass (jquery slime) "mirrored")))

(defmethod update :stand [_]
  (swap-slime! 1500 ["slime-stand0" "slime-stand1" "slime-stand2"]))

(defmethod update :walk [{:keys [x]}]
  (swap-slime! 300 ["slime-move0" "slime-move1" "slime-move2" "slime-move3" "slime-move4" "slime-move5" "slime-move6"])
  (.animate (jquery slime) (clj->js {:left x}) 1000))

(defmethod update :default [])

