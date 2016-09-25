(ns riemann-http-proxy.api
  (:require [org.httpkit.server :refer [run-server]]
            [clojure.edn        :as edn]
            [byte-streams       :refer [convert]]
            [riemann.client     :refer [tcp-client send-event]]
            [clojure.pprint :as pprint]
            [riemann-http-proxy.totp :as totp]))

(defn mismatch-event [totp-token]
  {:service "sergio.my-amazing-service.otpmismatch"
  :state "ok"
  :metric 1
  :description (format "TOTP should be %s" totp-token)})

(defn handler
  [client req]
  (let [body-str (convert (:body req) String)
        event (edn/read-string body-str)
        local-token (totp/totp)
        client-token ((:headers req) "otp")]
        (if (= client-token local-token)
          (do
            (send-event client event)
            {:status 204})
          (do
            (send-event client (mismatch-event local-token))
            {:status 401}))))

(defn start-server
  "Returns a function for stopping the server."
  [port riemann-server]
  (let [client (tcp-client :host riemann-server)]
    (run-server (partial handler client)
                {:port port})))

;; development

(defn debug-handler
  [req]
  (handler (tcp-client :host "localhost") req))

(comment



  (def s (run-server #'debug-handler {:port 8080}))
(s)

  )
