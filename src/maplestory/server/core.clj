(ns maplestory.server.core
  (:require [maplestory.server.map.henesys :as henesys]
            [maplestory.server.map.mushmom :as mushmom]
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

(defn add-socket-handler [map-name register-fn unregister-fn]
  (.add server (str "/maps/" map-name "/socket")
        (proxy [WebSocketHandler] []
          (onOpen [c] (register-fn c))
          (onMessage [c m] (println c ": " m))
          (onClose [c] (unregister-fn c)))))

(defn add-map! [map-name view register-fn unregister-fn]
  (add-map-handler map-name view)
  (add-socket-handler map-name register-fn unregister-fn))

(add-map! "henesys" henesys-view henesys/register-client henesys/unregister-client)
(add-map! "mushmom" mushmom-view mushmom/register-client mushmom/unregister-client)

(.start server)

