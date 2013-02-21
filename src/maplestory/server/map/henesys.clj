(ns maplestory.server.map.henesys
  (:require [maplestory.server.npc.sera :as sera]
            [maplestory.server.monster.slime :as slime]
            [maplestory.server.movement :refer [scheduler]]))

(def specs {:width  1241
            :height 613})

(def sera-npc (agent (merge sera/spawn-state {:x 750 :x-origin 750})))
(def sera-runner (future (scheduler sera-npc sera/actions)))

(def slime-monster (agent (merge slime/spawn-state {:x 800 :x-origin 800})))
(def slime-runner (future (scheduler slime-monster slime/actions)))

(defn register-client [connection]
  (.send connection (pr-str {:type :init :message {:sera @sera-npc :slime @slime-monster}}))
  (add-watch sera-npc connection
             (fn [_ _ _ state]
               (.send connection (pr-str {:type :update :message {:who :sera :event @sera-npc}}))))
  (add-watch slime-monster connection
             (fn [_ _ _ state]
               (.send connection (pr-str {:type :update :message {:who :slime :event @slime-monster}})))))

(defn unregister-client [connection]
  (remove-watch sera-npc connection)
  (remove-watch slime-monster connection))

