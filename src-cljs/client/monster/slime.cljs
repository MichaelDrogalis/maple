(ns client.monster.slime
  (:require [client.jquery :refer [jq jq-id]]
            [client.animation :refer [swap-image!]]))

(defn init [{:keys [id x direction]}]
  (.append (jq "body") (str "<div type=\"slime\" id=\"" id "\"></div>"))
  (.css (jq-id id) "left" x)
  (if (= direction :right)
    (.addClass (jq-id id) "mirrored")))

(defmulti update :action)

(defmethod update :flip [{:keys [id direction]}]
  (if (= direction :left)
    (.removeClass (jq-id id) "mirrored")
    (.addClass (jq-id id) "mirrored")))

(defmethod update :stand [{:keys [id]}]
  (swap-image! id id 1500 ["slime-stand0" "slime-stand1" "slime-stand2"]))

(defmethod update :walk [{:keys [id x]}]
  (swap-image! id id 300 ["slime-move0" "slime-move1" "slime-move2" "slime-move3"
                          "slime-move4" "slime-move5" "slime-move6"])
  (.animate (jq-id id) (clj->js {:left x}) 1000))

(defmethod update :default [])

