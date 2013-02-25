(ns client.map.binders
  (:require [client.npc.sera :as sera]
            [client.monster.stump :as stump]
            [cljs.reader :refer [read-string]]))

(def entities (atom #{}))

(def open-fn
  (fn [] (.log js/console "Connection established.")))

(defn init [environment]
  (doseq [[entity state] environment]
    (swap! entities conj (:id state))
    (cond (= entity :sera) (sera/init state)
          (= entity :slime) (slime/init state)
          (= entity :stump) (stump/init state))))

(defn update [{:keys [who event]}]
  (cond (= who :sera) (sera/update event)
        (= who :slime) (slime/update event)
        (= who :stump) (stump/update event)))

(def message-fn
  (fn [response]
    (let [data (read-string (.-data response))
          message (:message data)]
      (cond (= (:type data) :init) (init message)
            (= (:type data) :update) (update message)))))

(defn map-for-url []
  (apply str (last (partition-by (partial = \/) (.-URL js/document)))))

(defn socket-for-map [map-name]
  (js/$.websocket. (str "ws://localhost:42800/maps/" map-name "/socket")))

(let [ws (socket-for-map (map-for-url))]
  (set! (.-onopen ws) open-fn)
  (set! (.-onmessage ws) message-fn))

