(ns intercom-clj.users-test
  (:require [intercom-clj.users :as users]
            [clojure.test :refer [is testing deftest]]))

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
        (is (= ["user"] (users/all))))))
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
                                       :body "doesn't matter..."}})
        (is (thrown? Exception (users/all)))))))
