(ns maplestory.client.views
  (:require [hiccup.core :refer [html]]
            [hiccup.page :refer [include-js include-css]]))

(defn maple-view [map-name]
  (html
   [:html
    (include-css (str "/stylesheets/" map-name ".css"))
    (include-css "/stylesheets/movement.css")
    (include-css "/stylesheets/sera.css")
    (include-css "/stylesheets/stump.css")
    (include-css "/stylesheets/orange-mushroom.css")
    (include-js "http://code.jquery.com/jquery-latest.min.js")
    (include-js "/javascripts/websockets.js")
    (include-js "/javascripts/main.js")
    [:body
     [:div#screen]]]))

(defn henesys-view []
  (maple-view "henesys"))

(defn mushmom-view []
  (maple-view "mushmom"))

