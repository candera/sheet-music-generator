(defproject sheet-music-generator "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-alpha4"]
                 ;;[org.clojars.jmeeks/jfugue-with-musicxml "4.0.3"]
                 ;; [org.clojure/data.xml "0.0.8"]
                 [hiccup "1.0.5"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.6"]]
                   :source-paths ["dev"]}})
