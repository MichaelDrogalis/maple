(defproject maplestory "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.0-RC16"]
                 [org.webbitserver/webbit "0.4.3"]
                 [hiccup "1.0.2"]]
  :plugins [[lein-cljsbuild "0.3.0"]]
  :cljsbuild {:builds [{:source-paths ["src-cljs"]
                        :compiler {:output-to "resources/public/javascripts/main.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]}
  :main maplestory.server.core)
