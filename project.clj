(defproject intercom-clj "0.1.2"
  :description "A Clojure native library to use the Intercom REST API"
  :url "https://github.com/nanit/intercom-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [cheshire "5.6.3"]
                 [http-kit "2.2.0"]]
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.3"]]}})
