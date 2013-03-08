(ns client.monster.axe-stump
  (:require-macros [hiccups.core :refer [defhtml]])
  (:require [hiccups.runtime :as hrt]
            [client.jquery :refer [jq jq-id]]
            [client.animation :as animate]))

(def offset 63)

(defhtml axe-stump-container [id]
  [:div {:type :axe-stump :id id}])

(defn init [monster]
  (animate/init monster axe-stump-container offset))

(defmulti update :action)

(defmethod update :flip [monster]
  (animate/flip monster))

(defmethod update :move [monster]
  (animate/move monster "axe-stump-move" offset))

(defmethod update :default [])

