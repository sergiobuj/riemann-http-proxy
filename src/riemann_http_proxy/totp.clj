;; More info https://nakkaya.com/2012/08/13/google-hotp-totp-two-factor-authentication-for-clojure/
(ns riemann-http-proxy.totp)

(defn hotp-token [secret idx]
  (let [secret (-> (org.apache.commons.codec.binary.Base64.)
                   (.decode secret))
        idx (-> (java.nio.ByteBuffer/allocate 8)
                (.putLong idx)
                (.array))
        key-spec (javax.crypto.spec.SecretKeySpec. secret "HmacSHA1")
        mac (doto (javax.crypto.Mac/getInstance "HmacSHA1")
              (.init key-spec))
        hash (->> (.doFinal mac idx)
                  (into []))]

    (let [offset (bit-and (hash 19) 0xf)
          bin-code (bit-or (bit-shift-left (bit-and (hash offset) 0x7f) 24)
                           (bit-shift-left (bit-and (hash (+ offset 1)) 0xff) 16)
                           (bit-shift-left (bit-and (hash (+ offset 2)) 0xff) 8)
                           (bit-and (hash (+ offset 3)) 0xff))]
      (format "%06d" (mod bin-code 1000000)))))

(defn totp []
    "Return the TOTP for my hardcoded password."
    (hotp-token "my-secret-passwd0" (/ (System/currentTimeMillis) 1000 30)))
