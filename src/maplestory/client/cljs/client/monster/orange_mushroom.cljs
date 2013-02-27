(ns client.monster.orange-mushroom
  (:require-macros [hiccups.core :refer [defhtml]])
  (:require [hiccups.runtime :as hrt]
            [client.jquery :refer [jq jq-id]]
            [client.animation :as animate]))

(def offset 40)

(defhtml orange-mushroom-container [id]
  [:div {:type :orange-mushroom :id id}])

(defn init [monster]
  (animate/init monster orange-mushroom-container offset))

(defmulti update :action)

(defmethod update :stand [{:keys [id]}]
  (doseq [_ (range 5)]
    (animate/swap-image! id 100 ["orange-mushroom-stand0" "orange-mushroom-stand1"])))

(defmethod update :default [])

