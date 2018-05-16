(ns ges.editor-test
  (:require [clojure.test :refer :all]
            [ges.editor :as editor]))

(def image-example [["F" "O" "O" "O" "O"]
                    ["O" "C" "O" "O" "B"]
                    ["O" "O" "O" "F" "A"]
                    ["B" "A" "Z" "O" "R"]
                    ["O" "O" "F" "O" "O"]
                    ["O" "B" "A" "Z" "O"]
                    ["O" "A" "B" "A" "R"]
                    ["O" "R" "O" "O" "O"]])



(deftest create-test
  (testing "create image"
    (is (= (editor/create-image 5 8)
           [["O" "O" "O" "O" "O"]
            ["O" "O" "O" "O" "O"]
            ["O" "O" "O" "O" "O"]
            ["O" "O" "O" "O" "O"]
            ["O" "O" "O" "O" "O"]
            ["O" "O" "O" "O" "O"]
            ["O" "O" "O" "O" "O"]
            ["O" "O" "O" "O" "O"]])
        "valid image")
    (is (= (editor/create-image 0 6)
           nil)
        "not valid image size")
    (is (= (editor/create-image 2 251)
           nil)
        "size outside scope")))

(deftest clean-test
  (testing "clean image"
    (is (= (editor/clean-image [])
           nil)
        "clean empty image")
    (is (= (editor/clean-image image-example)
           [["O" "O" "O" "O" "O"]
            ["O" "O" "O" "O" "O"]
            ["O" "O" "O" "O" "O"]
            ["O" "O" "O" "O" "O"]
            ["O" "O" "O" "O" "O"]
            ["O" "O" "O" "O" "O"]
            ["O" "O" "O" "O" "O"]
            ["O" "O" "O" "O" "O"]])
        "clean painted image")))

(deftest paint-test
  (testing "paint pixel"
    (is (= (editor/paint-pixel image-example "!" 1 3)
           [["F" "O" "O" "O" "O"]
            ["O" "C" "O" "O" "B"]
            ["!" "O" "O" "F" "A"]
            ["B" "A" "Z" "O" "R"]
            ["O" "O" "F" "O" "O"]
            ["O" "B" "A" "Z" "O"]
            ["O" "A" "B" "A" "R"]
            ["O" "R" "O" "O" "O"]])
        "paint 1 pixel")
    (is (= (editor/paint-pixel image-example "!" 1 100)
           nil)
        "outside scope"))

  (testing "Paint vertical segment"
    (is (= (editor/paint-vertical-line image-example "!" 3 2 5)
           [["F" "O" "O" "O" "O"]
            ["O" "C" "!" "O" "B"]
            ["O" "O" "!" "F" "A"]
            ["B" "A" "!" "O" "R"]
            ["O" "O" "!" "O" "O"]
            ["O" "B" "A" "Z" "O"]
            ["O" "A" "B" "A" "R"]
            ["O" "R" "O" "O" "O"]])
        "Paint 1 vertical segment")
    (is (= (editor/paint-vertical-line image-example "!" 3 2 100)
           nil)
        "outside scope"))

  (testing "Paint horizontal segment"
    (is (= (editor/paint-horizontal-line image-example "!" 2 5 3)
           [["F" "O" "O" "O" "O"]
            ["O" "C" "O" "O" "B"]
            ["O" "!" "!" "!" "!"]
            ["B" "A" "Z" "O" "R"]
            ["O" "O" "F" "O" "O"]
            ["O" "B" "A" "Z" "O"]
            ["O" "A" "B" "A" "R"]
            ["O" "R" "O" "O" "O"]])
        "Paint 1 horizontal segment")
    (is (= (editor/paint-horizontal-line image-example "!" 3 100 5)
           nil)
        "outside scope"))

  (testing "Paint bucket fill"
    (is (= (editor/paint-bucket-fill image-example "!" 4 2)
           [["F" "!" "!" "!" "!"]
            ["!" "C" "!" "!" "B"]
            ["!" "!" "!" "F" "A"]
            ["B" "A" "Z" "O" "R"]
            ["O" "O" "F" "O" "O"]
            ["O" "B" "A" "Z" "O"]
            ["O" "A" "B" "A" "R"]
            ["O" "R" "O" "O" "O"]])
        "Paint 1 bucket fill")
    (is (-> (editor/create-image 500 250)
            (editor/paint-bucket-fill "!" 1 1))
        "stack overflow check")
    (is (= (editor/paint-bucket-fill image-example "!" 3 100)
           nil)
        "outside scope")))

(testing "All in one"
  (is (= (-> (editor/create-image 5 8)
             (editor/paint-pixel "P" 3 4)
             (editor/paint-vertical-line "V" 2 2 7)
             (editor/paint-horizontal-line "H" 3 5 2)
             (editor/paint-bucket-fill "B" 3 5))
         [["B" "B" "B" "B" "B"]
          ["B" "V" "H" "H" "H"]
          ["B" "V" "B" "B" "B"]
          ["B" "V" "P" "B" "B"]
          ["B" "V" "B" "B" "B"]
          ["B" "V" "B" "B" "B"]
          ["B" "V" "B" "B" "B"]
          ["B" "B" "B" "B" "B"]])
      "Chain all functions"))
