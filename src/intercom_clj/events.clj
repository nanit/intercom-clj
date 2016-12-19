(ns intercom-clj.events
  (:require [intercom-clj.core :refer [POST GET]]
            [intercom-clj.time :refer [now]])
  (:refer-clojure :exclude [name list]))

(defn create 
  "Create an event for user
   user can be any of the following:
  {:user_id <user-id>}
  {:email <user-email>}
  {:id <intercom-id>}
  If created-at is not supplied the current epoch will be used
  See https://developers.intercom.com/reference#event-model" 
  ([user name] (create user name {} (now)))
  ([user name metadata] (create user name metadata (now)))
  ([user name metadata created-at] 
   (POST "/events" 
         (merge user 
                {:event_name name 
                 :created_at created-at 
                 :metadata metadata}))))

(defn list 
  "Lists event for users
   user can be any of the following:
  {:user_id <user-id>}
  {:email <user-email>}
  {:id <intercom-id>}
  For opts see https://developers.intercom.com/reference#list-user-events"
  ([user] (list user {}))
  ([user opts]
   (GET "/events" 
        (merge user 
               {:type "user"}
               opts))))
