(ns client.monster.stump
  (:require-macros [hiccups.core :refer [defhtml]])
  (:require [hiccups.runtime :as hrt]
            [client.jquery :refer [jq jq-id]]
            [client.animation :as animate]))

(def offset 40)

(defhtml stump-container [id]
  [:div {:type :stump :id id}])

(defn init [monster]
  (animate/init monster stump-container offset))

(defmulti update :action)

(defmethod update :flip [monster]
  (animate/flip monster))

(defmethod update :stand [{:keys [id]}])

(defmethod update :move [monster]
  (animate/move monster "stump-move" offset))

(defmethod update :default [])

