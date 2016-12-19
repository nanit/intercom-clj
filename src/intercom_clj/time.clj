(ns intercom-clj.time)

(defn now 
  "Returns the current time epoch in 10 digits" 
  []
  (quot (System/currentTimeMillis) 1000))
