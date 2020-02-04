(ns intercom-clj.core
  (:require [org.httpkit.client :as http]
            [taoensso.timbre :as log]
            [cheshire.core :refer [generate-string parse-string]]))

(def access-token (atom nil))
(def ^:private base-url "https://api.intercom.io")

(defn set-access-token! 
  "Sets the private access token for the Intercom API" 
  [token]
  (reset! access-token token))

(defn- fetch-access-token!
  "Fetches access token or sets it to the INTERCOM_ACCESS_TOKEN environment variable" 
  []
  (cond 
    @access-token @access-token
    (System/getenv "INTERCOM_ACCESS_TOKEN") (do (reset! access-token (System/getenv "INTERCOM_ACCESS_TOKEN"))
                                                @access-token)
    :else (throw (Exception. "Intercom API access token is missing. Please set it before making an API request."))))

(defn- options [opts]
  (merge 
    {:timeout 2000
     :headers {"Accept" "application/json"
               "Authorization" (format "Bearer %s" (fetch-access-token!))
               "Content-Type" "application/json"}}
    opts))

(defn- url 
  "Composes url parts on top of base-url" 
  [path]
  (str base-url path))

(defn- parse-response
  [req]
  (let [{:keys [status body error]} @req]
    (if error
      (log/error "INTERCOM_REQUEST_ERROR" error)
      {:status status :body (parse-string body true)})))

(defn- request 
  "Launches an HTTP request to Intercom's API" 
  [method path opts]
  (parse-response (method (url path) (options opts))))

(defn POST 
  "Launches a POST request to Intercom's API. Body is serialized as JSON." 
  [path body]
  (request http/post path {:body (generate-string body)}))

(defn GET 
  "Launches a GET request to Intercom's API" 
  ([path] (GET path {}))
  ([path query-params] (GET path query-params {}))
  ([path query-params opts] (request http/get path (assoc opts :query-params query-params))))

(defn DELETE 
  "Launches a DELETE request to Intercom's API" 
  ([path] (DELETE path {}))
  ([path query-params] (request http/delete path {:query-params query-params})))
