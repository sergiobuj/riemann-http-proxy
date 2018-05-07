(defproject riemann-http-proxy "0.1.0-SNAPSHOT"
  :description "Riemann HTTP proxy"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[base64-clj                  "0.1.1"]
                 [byte-streams                "0.2.2"]
                 [clj-otp                     "0.1.3"]
                 [commons-codec/commons-codec   "1.4"]
                 [http-kit                    "2.2.0"]
                 [org.clojure/clojure         "1.8.0"]
                 [org.clojure/tools.cli       "0.3.5"]
                 [org.clojure/tools.logging   "0.3.1"]
                 [riemann-clojure-client      "0.4.2"]]
  :main riemann-http-proxy.main
  :aot [ riemann-http-proxy.main ])
