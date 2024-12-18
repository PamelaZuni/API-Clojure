(defproject api_testing "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [clj-http "3.12.3"]
                 [cheshire "5.10.0"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [compojure "1.6.2"]
                 [metosin/muuntaja "0.6.10"]
                 [com.fasterxml.jackson.core/jackson-core "2.10.1"]
                 [com.fasterxml.jackson.core/jackson-databind "2.10.1"]
                 [com.fasterxml.jackson.dataformat/jackson-dataformat-cbor "2.10.1"]
                 [com.fasterxml.jackson.dataformat/jackson-dataformat-smile "2.10.1"]
                 [org.clojure/data.json "0.2.6"]
                 [ring/ring-json "0.5.0"]]
  ;:repl-options {:init-ns api-server.core}
  :main api-testing.core)
