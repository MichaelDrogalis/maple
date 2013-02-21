(ns maplestory.client.views
  (:require [hiccup.core :refer [html]]
            [hiccup.page :refer [include-js include-css]]))

(defn sera-view []
  (html
   [:html
    (include-css "stylesheets/theme.css")
    (include-css "stylesheets/sera.css")
    (include-css "stylesheets/slime.css")
    (include-js "http://code.jquery.com/jquery-latest.min.js")
    (include-js "javascripts/websockets.js")
    (include-js "javascripts/main.js")
    [:body
     [:div#screen]
     [:div#sera]
     [:div#slime]]]))

