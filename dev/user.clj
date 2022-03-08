(ns user
  (:require [clojure.tools.namespace.repl :as tools]
            [clojure.test :refer [run-all-tests run-tests]]
            [intercom-clj.core :refer [set-access-token!]]
            [intercom-clj.users :as users]
            [intercom-clj.events :as events]))

(defn refresh []
  (tools/refresh)
  (use 'clojure.repl))
