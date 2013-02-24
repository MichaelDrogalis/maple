(ns client.monster.slime
  (:require-macros [hiccups.core :refer [defhtml]])
  (:require [hiccups.runtime :as hrt]
            [client.jquery :refer [jq jq-id]]
            [client.animation :refer [swap-image!]]))

(defhtml slime-container [id]
  [:div {:type :slime :id id}])

(defn init [{:keys [id x y direction]}]
  (.append (jq "body") (slime-container id))
  (.css (jq-id id) "left" x)
  (.css (jq-id id) "right" y)
  (if (= direction :right)
    (.addClass (jq-id id) "mirrored")))

(defmulti update :action)

(defmethod update :flip [{:keys [id direction]}]
  (if (= direction :left)
    (.removeClass (jq-id id) "mirrored")
    (.addClass (jq-id id) "mirrored")))

(defmethod update :stand [{:keys [id]}]
  (swap-image! id 1500 ["slime-stand0" "slime-stand1" "slime-stand2"]))

(defmethod update :walk [{:keys [id x y subaction]}]
  (let [delay-time 142]
    (swap-image! id delay-time [(str "slime-move" subaction)])
    (.animate (jq-id id) (clj->js {:left x :top y}) delay-time)))

(defmethod update :default [])

