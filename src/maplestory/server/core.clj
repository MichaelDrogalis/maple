(ns maplestory.server.core
  (:require [maplestory.server.map.henesys :as henesys]
            [maplestory.server.map.mushmom :as mushmom]
            [maplestory.client.views :refer [henesys-view mushmom-view]])
  (:import [org.webbitserver WebServer WebServers WebSocketHandler HttpHandler]
           [org.webbitserver.handler StaticFileHandler]))

(def server (WebServers/createWebServer 42800))

(.add server (StaticFileHandler. "resources/public"))

(.add server "/maps/henesys/socket"
      (proxy [WebSocketHandler] []
        (onOpen [c] (henesys/register-client c))
        (onMessage [c m] (println c ": " m))
        (onClose [c] (henesys/unregister-client c))))

(.add server "/maps/henesys"
      (proxy [HttpHandler] []
        (handleHttpRequest [_ response _]
          (doto response
            (.header "Content-Type", "text/html")
            (.content (henesys-view))
            (.end)))))

(.add server "/maps/mushmom"
      (proxy [HttpHandler] []
        (handleHttpRequest [_ response _]
          (doto response
            (.header "Content-Type", "text/html")
            (.content (mushmom-view))
            (.end)))))

(.add server "/maps/mushmom/socket"
      (proxy [WebSocketHandler] []
        (onOpen [c] (mushmom/register-client c))
        (onMessage [c m] (println c ": " m))
        (onClose [c] (mushmom/unregister-client c))))

(.start server)

