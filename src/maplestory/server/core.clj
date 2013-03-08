(ns maplestory.server.core
  (:require [maplestory.server.map.mushmom :as mushmom]
            [maplestory.server.map.binders :as b]
            [maplestory.client.views :refer [henesys-view mushmom-view]])
  (:import [org.webbitserver WebServer WebServers WebSocketHandler HttpHandler]
           [org.webbitserver.handler StaticFileHandler]))

(defonce server (WebServers/createWebServer 42800))

(.add server (StaticFileHandler. "resources/public"))

(defn add-view-handler [map-name view]
  (.add server (str "/maps/" (name map-name))
        (proxy [HttpHandler] []
          (handleHttpRequest [_ response _]
            (doto response
              (.header "Content-Type", "text/html")
              (.content (view))
              (.end))))))

(defn add-socket-handler [map-name]
  (let [connections (get-in @b/maps [map-name :connections])
        entities    (get-in @b/maps [map-name :entities])]
    (.add server (str "/maps/" (name map-name) "/socket")
          (proxy [WebSocketHandler] []
            (onOpen [c] (b/register-client c connections entities))
            (onMessage [c m] (println c ": " m))
            (onClose [c] (b/unregister-client c connections entities))))))

(defn add-view! [map-name view]
  (add-view-handler map-name view)
  (add-socket-handler map-name))

(add-view! :mushmom mushmom-view)

(.start server)

