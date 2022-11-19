(ns do.lib.todo-test
  (:require [do.lib.todo :as sut]
            [mount.core]
            [clojure.test :refer [deftest is use-fixtures testing]]
            [malli.generator :as mg]))

(defn reset-db-fixture
  [f]
  (reset! sut/db {})
  (f))

(use-fixtures :each reset-db-fixture)

(deftest todo-crud-test
  (let [todo
        (sut/create-todo! {:todo/title "Buy milk"
                           :todo/done? false})]
    (is (= [todo] (sut/list-todos)))
    (is (true? (sut/delete-todo! (:todo/id todo))))
    (is (= [] (sut/list-todos)))))

(deftest create-todo!-test
  (testing "create todos"
    (let [todo-data (mg/generate sut/create-todo-schema)]
      (is (uuid? (:todo/id (sut/create-todo! todo-data))))))

  (testing "creating invalid todos doesn't work"
    (is (thrown? Exception (sut/create-todo! {:name "Bob"})))
    (is (thrown? Exception (sut/create-todo!
                             {:todo/title "Buy milk"
                              :todo/done? false
                              :todo/id    (random-uuid)})
                 "Allowed setting the :todo/id"))))
