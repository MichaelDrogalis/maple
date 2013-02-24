(ns client.map.mushmom
  (:require [client.monster.stump :as stump]
            [cljs.reader :refer [read-string]]))

(def entities (atom #{}))

(def open-fn
  (fn [] (.log js/console "Connection established.")))

(defn init [environment]
  (doseq [[entity state] environment]
    (swap! entities conj (:id state))
    (cond (= entity :stump) (stump/init state))))

(defn update [{:keys [who event]}]
  (cond (= who :stump) (stump/update event)))

(def message-fn
  (fn [response]
    (let [data (read-string (.-data response))
          message (:message data)]
      (cond (= (:type data) :init) (init message)
            (= (:type data) :update) (update message)))))

(if (= (apply str (last (partition-by (partial = \/) (.-URL js/document)))) "mushmom")
  (let [ws (js/$.websocket. "ws://localhost:42800/maps/mushmom/socket")]
    (set! (.-onopen ws) open-fn)
    (set! (.-onmessage ws) message-fn)))

