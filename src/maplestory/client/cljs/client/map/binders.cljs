(ns client.map.binders
  (:require [client.npc.sera :as sera]
            [client.monster.slime :as slime]
            [client.monster.stump :as stump]
            [client.monster.orange-mushroom :as orange-mushroom]
            [cljs.reader :refer [read-string]]))

(def entities (atom #{}))

(def open-fn
  (fn [] (.log js/console "Connection established.")))

(defmulti init :entity)

(defmethod init :sera [{:keys [state]}] (sera/init state))

(defmethod init :slime [{:keys [state]}] (slime/init state))

(defmethod init :stump [{:keys [state]}] (stump/init state))

(defmethod init :orange-mushroom [{:keys [state]}] (orange-mushroom/init state))

(defn initialize [environment]
  (doseq [[entity state] environment]
    (swap! entities conj (:id state))
    (init {:entity entity :state state})))

(defmulti update :who)

(defmethod update :sera [{:keys [event]}] (sera/update event))

(defmethod update :slime [{:keys [event]}] (slime/update event))

(defmethod update :stump [{:keys [event]}] (stump/update event))

(defmethod update :orange-mushroom [{:keys [event]}] (orange-mushroom/update event))

(def message-fn
  (fn [response]
    (let [data (read-string (.-data response))
          message (:message data)]
      (cond (= (:type data) :init) (initialize message)
            (= (:type data) :update) (update message)))))

(defn map-for-url []
  (apply str (last (partition-by (partial = \/) (.-URL js/document)))))

(defn socket-for-map [map-name]
  (js/$.websocket. (str "ws://129.21.129.174:42800/maps/" map-name "/socket")))

(let [ws (socket-for-map (map-for-url))]
  (set! (.-onopen ws) open-fn)
  (set! (.-onmessage ws) message-fn))

