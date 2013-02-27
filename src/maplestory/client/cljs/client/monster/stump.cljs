(ns client.monster.stump
  (:require-macros [hiccups.core :refer [defhtml]])
  (:require [hiccups.runtime :as hrt]
            [client.jquery :refer [jq jq-id]]
            [client.animation :as animate]))

(defhtml stump-container [id]
  [:div {:type :stump :id id}])

(defn init [monster]
  (animate/init monster stump-container 40))

(defmulti update :action)

(defmethod update :flip [monster]
  (animate/flip monster))

(defmethod update :stand [{:keys [id]}]
  (animate/swap-image! id 2000 ["stump-stand0"]))

(defmethod update :walk [{:keys [id position]}]
  (animate/swap-image! id 250 ["stump-move0" "stump-move1" "stump-move2" "stump-move3"])
  (.animate (jq-id id) (clj->js {:left (:x position) :top (- (:y position) 40)}) 1000))

(defmethod update :default [])

