(ns client.monster.stump
  (:require-macros [hiccups.core :refer [defhtml]])
  (:require [hiccups.runtime :as hrt]
            [client.jquery :refer [jq jq-id]]
            [client.animation :refer [swap-image!]]))

(defhtml stump-container [id]
  [:div {:type :stump :id id}])

(defn offset-height [y]
  (- y 40))

(defn init [{:keys [id position direction]}]
  (.append (jq "body") (stump-container id))
  (.css (jq-id id) "left" (:x position))
  (.css (jq-id id) "top" (offset-height (:y position)))
  (if (= direction :right)
    (.addClass (jq-id id) "mirrored"))
  (.css (jq-id id) "visibility" "visible"))

(defmulti update :action)

(defmethod update :flip [{:keys [id direction]}]
  (if (= direction :left)
    (.removeClass (jq-id id) "mirrored")
    (.addClass (jq-id id) "mirrored")))

(defmethod update :stand [{:keys [id]}]
  (swap-image! id 2000 ["stump-stand0"]))

(defmethod update :walk [{:keys [id position]}]
  (swap-image! id 250 ["stump-move0" "stump-move1" "stump-move2" "stump-move3"])
  (.animate (jq-id id) (clj->js {:left (:x position) :top (offset-height (:y position))}) 1000))

(defmethod update :default [])

