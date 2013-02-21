(ns maplestory.server.core
  (:require [maplestory.server.map.henesys :as henesys]
            [maplestory.client.views :refer [sera-view]])
  (:import [org.webbitserver WebServer WebServers WebSocketHandler HttpHandler]
           [org.webbitserver.handler StaticFileHandler]))

(def server (WebServers/createWebServer 42800))

(.add server (StaticFileHandler. "resources/public"))

(.add server "/sera"
      (proxy [WebSocketHandler] []
        (onOpen [c] (henesys/register-client c))
        (onMessage [c m] (println c ": " m))
        (onClose [c] (henesys/unregister-client c))))

(.add server "/sera-client"
      (proxy [HttpHandler] []
        (handleHttpRequest [_ response _]
          (doto response
            (.header "Content-Type", "text/html")
            (.content (sera-view))
            (.end)))))

(.start server)

