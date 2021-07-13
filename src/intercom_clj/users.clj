(ns intercom-clj.users
  (:require [intercom-clj.core :refer [POST GET DELETE]]
            [taoensso.timbre :refer [info] :as log])
  (:refer-clojure :exclude [update list]))

(defn create
  "Create or updates a user"
  [criteria]
  (POST "/users" criteria))

(def update create)

(defn show
  "View a specific user by user_id or email fields
   criteria examples: {:user_id 123} or {:email 'my@email.com'}"
  [criteria]
  (GET "/users" criteria))

(defn show-by-id
  "View a specific user by Intercom user ID"
  [id]
  (GET (format "/users/%s" id)))

(defn list
  "lists users
  For arguments see https://developers.intercom.com/reference#list-users"
  ([] (list {}))
  ([args] (GET "/users" args)))

(defn scroll
  "scrolls users
  docs: https://developers.intercom.com/intercom-api-reference/v1.0/reference#iterating-over-all-users"
  ([] (scroll nil))
  ([scroll-param]
   (let [query-params (if scroll-param {:scroll_param scroll-param} {})]
     (GET "/users/scroll" query-params {:timeout 20000}))))

(defn all
  "Receives all users by iterating all users pages
  Use page-limit to limit the number of scroll iterations
  Uses the scroll API
  docs: https://developers.intercom.com/intercom-api-reference/v1.0/reference#iterating-over-all-users"
  ([] (all nil))
  ([page-limit & {:keys [retries]
                  :or   {retries 0}}]
   (loop [pages-left   page-limit
          acc          []
          retries-left retries
          scroll-param nil]
     (info "INTERCOM_SCROLL_FETCHED" (count acc) "users")
     (if (or (nil? pages-left) (> pages-left 0))
       (let [{:keys [body status]} (scroll scroll-param)]
         (condp = status
           200 (let [users (:users body)]
                 (if (empty? users)
                   acc
                   (recur (when pages-left (dec pages-left))
                          (vec (concat acc users))
                          retries-left
                          (:scroll_param body))))
           (do (log/error "INTERCOM_REQUEST_FAILED" status body)
               (if (pos? retries-left)
                 (do
                   (log/info "RETRYING" retries-left retries)
                   (Thread/sleep 100)
                   (recur pages-left
                          acc
                          (dec retries-left)
                          scroll-param))
                 (do
                   (log/error "NO_MORE_RETRIES_LEFT" retries-left retries)
                   (throw (Exception. "INTERCOM_RETRIES_EXCEEDED")))))))
       acc))))

(defn delete
  "Deletes a user from Intercom
   criteria examples: {:user_id 123} or {:email 'my@email.com'}"
  [criteria]
  (DELETE "/users" criteria))

(defn delete-by-id
  "Deletes a user from Intercom by Intercom user ID"
  [id]
  (DELETE (format "/users/%s" id)))
