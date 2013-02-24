(ns client.npc.sera
  (:require-macros [hiccups.core :refer [defhtml]])
  (:require [hiccups.runtime :as hrt]
            [client.jquery :refer [jq jq-id]]
            [client.animation :refer [swap-image!]]))

(defhtml sera-container [id]
  [:div {:type :sera :id id :class "stump-stand0"}])

(defn init [{:keys [id x y direction]}]
  (.append (jq "body") (sera-container id))
  (.css (jq-id id) "left" x)
  (.css (jq-id id) "top" y)
  (if (= direction :right)
    (.addClass (jq-id id) "mirrored")))

(defmulti update :action)

(defmethod update :blink [{:keys [id]}]
  (swap-image! id 100 ["sera-blink0" "sera-blink1" "sera-blink2" "sera-blink3"
                    "sera-blink4" "sera-blink5" "sera-blink6" "sera-blink7"
                    "sera-blink8"])
  (swap-image! id 0 ["sera-stand0"]))

(defmethod update :hair [{:keys [id]}]
  (swap-image! id 300 ["sera-hair0" "sera-hair1" "sera-hair2"]))

(defmethod update :smile [{:keys [id]}]
  (swap-image! id 200 ["sera-smile0"])
  (swap-image! id 1500 ["sera-smile1"]))

(defmethod update :alert [{:keys [id]}]
  (swap-image! id 500 ["sera-alert0" "sera-alert1"]))

(defmethod update :angry [{:keys [id]}]
  (swap-image! id 500 ["sera-angry0"])
  (swap-image! id 1500 ["sera-angry0"]))

(defmethod update :walk [{:keys [id x y]}]
  (swap-image! id 300 ["sera-move0" "sera-move1" "sera-move2" "sera-move3"])
  (.animate (jq-id id) (clj->js {:left x :top y}) 1000))

(defmethod update :flip [{:keys [id direction]}]
  (if (= direction :left)
    (.removeClass (jq-id id) "mirrored")
    (.addClass (jq-id id) "mirrored")))

(defmethod update :default [])

