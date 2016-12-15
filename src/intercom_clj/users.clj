(ns intercom-clj.users
  (:require [intercom-clj.core :refer [POST GET]])
  (:refer-clojure :exclude [update]))

(defn create 
  "Create or updates a user" 
  [criteria]
  (POST "/users" criteria))

(def update create)

(defn show 
  "View a specific user by id, user_id or email" 
  [criteria]
  (GET "/users" criteria))
