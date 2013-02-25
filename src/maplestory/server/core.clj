(ns maplestory.server.core
  (:require [maplestory.server.map.henesys :as henesys]
            [maplestory.server.map.mushmom :as mushmom]
            [maplestory.server.map.binders :as b]
            [maplestory.client.views :refer [henesys-view mushmom-view]])
  (:import [org.webbitserver WebServer WebServers WebSocketHandler HttpHandler]
           [org.webbitserver.handler StaticFileHandler]))

(def server (WebServers/createWebServer 42800))

(.add server (StaticFileHandler. "resources/public"))

(defn add-map-handler [map-name view]
  (.add server (str "/maps/" map-name)
        (proxy [HttpHandler] []
          (handleHttpRequest [_ response _]
            (doto response
              (.header "Content-Type", "text/html")
              (.content (view))
              (.end))))))

(defn add-socket-handler [map-name connections entities]
  (.add server (str "/maps/" map-name "/socket")
        (proxy [WebSocketHandler] []
          (onOpen [c] (b/register-client c connections entities))
          (onMessage [c m] (println c ": " m))
          (onClose [c] (b/unregister-client c connections entities)))))

(defn add-map! [map-name view connections entities]
  (add-map-handler map-name view)
  (add-socket-handler map-name connections entities))

(add-map! "henesys" henesys-view henesys/connections henesys/entities)
(add-map! "mushmom" mushmom-view mushmom/connections mushmom/entities)

(.start server)

