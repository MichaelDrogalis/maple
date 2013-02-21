(ns maplestory.server.core
  (:require [maplestory.server.npc.sera :refer [sera]]
            [maplestory.server.monster.slime :refer [slime]]
            [maplestory.client.views :refer [sera-view]])
  (:import [org.webbitserver WebServer WebServers WebSocketHandler HttpHandler]
           [org.webbitserver.handler StaticFileHandler]))

(defn init-client [connection]
  (.send connection (pr-str {:type :init :message {:sera @sera :slime @slime}}))
  (add-watch sera connection
             (fn [_ _ _ state]
               (.send connection (pr-str {:type :update :message {:who :sera :event @sera}}))))
  (add-watch slime connection
             (fn [_ _ _ state]
               (.send connection (pr-str {:type :update :message {:who :slime :event @slime}})))))

(def server (WebServers/createWebServer 42800))

(.add server (StaticFileHandler. "resources/public"))

(.add server "/sera"
      (proxy [WebSocketHandler] []
        (onOpen [c] (future (init-client c)))
        (onMessage [c m] (println c ": " m))
        (onClose [c] (remove-watch sera c))))

(.add server "/sera-client"
      (proxy [HttpHandler] []
        (handleHttpRequest [_ response _]
          (doto response
            (.header "Content-Type", "text/html")
            (.content (sera-view))
            (.end)))))

(.start server)

