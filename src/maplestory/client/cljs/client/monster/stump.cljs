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

(defmethod update :stand [{:keys [id]}]
  (animate/swap-image! id 2000 ["stump-stand0"]))

(defmethod update :move [{:keys [id position transient]}]
  (let [sleep-time (:sleep-ms transient)]
    (.animate (jq-id id) (clj->js {:left (:x position) :top (- (:y position) offset)}) sleep-time)
    (let [frames (take (quot sleep-time (quot 140 4)) (cycle (range 0 4)))]
      (doseq [n frames]
        (.gx (jq-id id)
             (clj->js {})
             (:sleep-ms transient)
             "Linear"
             (clj->js {:start (fn [el] (animate/addClass el (str "stump-move" n)))}))))))

(defmethod update :default [])

