(ns intercom-clj.users
  (:require [intercom-clj.core :refer [POST GET DELETE]])
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

(defn all
  "Receives all users by iterating all users pages"
  []
  (loop [acc []
         current-page 1]
    (let [res (:body (list {:page current-page}))
          new-acc (vec (concat acc (:users res)))]
      (if (-> res :pages :next)
        (recur new-acc
               (inc current-page))
        new-acc))))

(defn delete
  "Deletes a user from Intercom
   criteria examples: {:user_id 123} or {:email 'my@email.com'}" 
  [criteria]
  (DELETE "/users" criteria))

(defn delete-by-id
  "Deletes a user from Intercom by Intercom user ID"
  [id]
  (DELETE (format "/users/%s" id)))
