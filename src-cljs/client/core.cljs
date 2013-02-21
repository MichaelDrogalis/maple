(ns client.core
  (:require [client.npc.sera :as sera]
            [client.monster.slime :as slime]
            [cljs.reader :refer [read-string]]))

(def ws (js/$.websocket. "ws://localhost:42800/sera"))

(def open-fn
  (fn [] (.log js/console "Connection established.")))

(defn init [environment]
  (doseq [[entity state] environment]
    (cond (= entity :sera) (sera/init state)
          (= entity :slime) (slime/init state))))

(defn update [{:keys [who event]}]
  (cond (= who :sera) (sera/update event)
        (= who :slime) (slime/update event)))

(def message-fn
  (fn [response]
    (let [data (read-string (.-data response))
          message (:message data)]
      (cond (= (:type data) :init) (init message)
            (= (:type data) :update) (update message)))))

(set! (.-onopen ws) open-fn)
(set! (.-onmessage ws) message-fn)

