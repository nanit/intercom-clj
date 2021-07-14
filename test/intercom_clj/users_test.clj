(ns intercom-clj.users-test
  (:require [intercom-clj.users :as users]
            [clojure.test :refer [is testing deftest]]))

(defn gen-responses [& responses]
  (let [resps (atom responses)]
    (fn [_]
      (let [resp (first @resps)]
        (swap! resps rest)
        resp))))

(deftest all-test
  (testing "success"
    (testing "no users should return empty collection of users"
      (with-redefs (users/scroll (constantly {:status 200
                                              :body   {:users []}}))
        (is (empty? (users/all)))))

    (testing "scrolling should stop on empty users and return list of users"
      (with-redefs (users/scroll {nil {:status 200
                                       :body   {:users        ["user"]
                                                :scroll_param "1"}}
                                  "1" {:status 200
                                       :body   {:users []}}})
        (is (= ["user"] (users/all)))))
    (testing "scrolling should return users on error during scrolling with retries more than failures"
      (with-redefs (users/scroll (gen-responses {:status 200
                                                 :body   {:users        ["user"]
                                                          :scroll_param "1"}}
                                                {:status 404
                                                 :body   "doesn't matter..."}
                                                {:status 404
                                                 :body   "doesn't matter..."}
                                                {:status 200
                                                 :body   {:users        []
                                                          :scroll_param "1"}}))
        (is (= ["user"] (users/all nil :retries 2))))))
  (testing "failure"
    (testing "failure"
      (testing "scrolling should stop and throw an exception on error with no retries"
        (with-redefs (users/scroll {nil {:status 404
                                         :body   {:users        ["user"]
                                                  :scroll_param "1"}}})
          (is (thrown? Exception (users/all))))))
    (testing "scrolling should stop and throw an exception on error during scrolling with no retries"
      (with-redefs (users/scroll {nil {:status 200
                                       :body   {:users        ["user"]
                                                :scroll_param "1"}}
                                  "1" {:status 404
                                       :body   "doesn't matter..."}})
        (is (thrown? Exception (users/all)))))
    (testing "scrolling should stop and throw an exception on error during scrolling with retries less than failures"
      (with-redefs (users/scroll (gen-responses {:status 200
                                                 :body   {:users        ["user"]
                                                          :scroll_param "1"}}
                                                {:status 404
                                                 :body   "doesn't matter..."}
                                                {:status 404
                                                 :body   "doesn't matter..."}
                                                {:status 404
                                                 :body   "doesn't matter..."}
                                                {:status 200
                                                 :body   {:users []}}))
        (is (thrown? Exception (users/all :retries 2)))))))
