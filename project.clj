(defproject maplestory "0.1.0-SNAPSHOT"
  :description "A subset of MapleStory."
  :url "https://bitbucket.org/xpherior/maplestory"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"stuart" "http://stuartsierra.com/maven2"}
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [org.webbitserver/webbit "0.4.3"]
                 [com.stuartsierra/lazytest "1.2.3"]
                 [hiccup "1.0.2"]
                 [hiccups "0.2.0"]
                 [midje "1.4.0"]]
  :plugins [[lein-cljsbuild "0.3.0"]
            [lein-midje "2.0.3"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds [{:source-paths ["src/maplestory/client/cljs"]
                        :compiler {:output-to "resources/public/javascripts/main.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]})

