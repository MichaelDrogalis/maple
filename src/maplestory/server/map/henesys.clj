(ns maplestory.server.map.henesys
  (:require [maplestory.server.npc.sera :as sera]
            [maplestory.server.monster.slime :as slime]
            [maplestory.server.movement :refer [scheduler]]))

(def specs {:width  1241
            :height 613})

(def connections (ref #{}))
(def entities (atom #{}))

(defn register-client [connection]
  (dosync
   (conj @connections connection)
   (doseq [entity @entities]
     (.send connection (pr-str {:type :init :message {(:type @entity) @entity}}))
     (add-watch entity
                connection
                (fn [_ _ _ state]
                  (.send connection (pr-str {:type :update :message {:who (:type @entity) :event @entity}})))))))

(defn unregister-client [connection]
  (dosync
   (disj @connections connection)
   (doseq [entity @entities]
     (remove-watch entity connection))))

(defn spawn! []
  (let [sera-npc (agent (merge sera/spawn-state {:x 750 :x-origin 750 :id (name (gensym))}))
        slime-monster (agent (merge slime/spawn-state {:x 800 :x-origin 800 :id (name (gensym))}))
 ;       slime-monster2 (agent (merge slime/spawn-state {:x 600 :x-origin 600 :id (gensym)}))
        ]
    (swap! entities conj sera-npc)
    (future (scheduler sera-npc sera/actions))
    (swap! entities conj slime-monster)
    (future (scheduler slime-monster slime/actions))
;    (swap! entities conj slime-monster2)
;    (future (scheduler slime-monster2 slime/actions))
    ))

(spawn!)

