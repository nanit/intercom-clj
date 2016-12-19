(ns intercom-clj.users
  (:require [intercom-clj.core :refer [POST GET]])
  (:refer-clojure :exclude [update list]))

(defn create 
  "Create or updates a user" 
  [criteria]
  (POST "/users" criteria))

(def update create)

(defn show 
  "View a specific user by id, user_id or email" 
  [criteria]
  (GET "/users" criteria))

(defn list 
  "lists all users
  For arguments see https://developers.intercom.com/reference#list-users" 
  ([] (list {}))
  ([args] (GET "/users" args)))
