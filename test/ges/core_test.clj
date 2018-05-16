(ns ges.core-test
  (:require [clojure.test :refer :all]
            [ges.core :as core]))

(deftest commands-test
  (testing "commands"
    (is (= (core/cmd [] "I 7 3")
           [["O" "O" "O" "O" "O" "O" "O"]
            ["O" "O" "O" "O" "O" "O" "O"]
            ["O" "O" "O" "O" "O" "O" "O"]]))))

(deftest client-examples-test
  (testing "examples provided by client"
    (is (= (with-out-str
             (-> (core/cmd [] "I 5 6")
                 (core/cmd "L 2 3 A")
                 (core/cmd "S")))
           "=>\nOOOOO\nOOOOO\nOAOOO\nOOOOO\nOOOOO\nOOOOO\n")
        "example 1")
    (is (= (with-out-str
             (-> (core/cmd [] "I 5 6")
                 (core/cmd "L 2 3 A")
                 (core/cmd "F 3 3 J")
                 (core/cmd "V 2 3 4 W")
                 (core/cmd "H 3 4 2 Z")
                 (core/cmd "S")))
           "=>\nJJJJJ\nJJZZJ\nJWJJJ\nJWJJJ\nJJJJJ\nJJJJJ\n")
        "example 2")))
