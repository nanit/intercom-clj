(ns user
  (:require [clojure.tools.namespace.repl :as tools]
            [intercom-clj.core :refer [set-access-token!]]
            [intercom-clj.users :as users]
            [intercom-clj.events :as events]))

(defn refresh []
  (tools/refresh)
  (use 'clojure.repl))
