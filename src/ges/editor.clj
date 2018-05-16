(ns ges.editor)

(defn pixel-position [x y]
  "Convert x-axis and y-axis into image vector position."
  [(- y 1) (- x 1)])

(defn image-size [image]
  (let [x-size (count (first image))
        y-size (count image)]
    [x-size y-size]))

(defn in-scope? [image x y]
  "Check if position is not outside image."
  (let [[x-size y-size] (image-size image)]
    (and (<= x x-size)
         (<= y y-size))))

(defn pixel-neighbors [[x y]]
  "Horizontal and vertical neighbors of pixel.
  Positions can be outside image size."
  [[(- x 1) y]
   [(+ x 1) y]
   [x (- y 1)]
   [x (+ y 1)]])



(defn create-image [x-size y-size]
  (when (and (>= x-size 1)
             (<= y-size 250))
    (->> (repeat x-size "O")
         (vec)
         (repeat y-size)
         (vec))))

(defn clean-image [image]
  "Reset image to state after create-image."
  (let [[x y] (image-size image)]
    (create-image x y)))

(defn paint-pixel [image colour x y]
  (when (in-scope? image x y)
    (let [position (pixel-position x y)]
      (assoc-in image position colour))))

(defn paint-vertical-line [image colour x y1 y2]
  (when (and (in-scope? image x y1)
             (in-scope? image x y2))
    (loop [img image
           ys (range y1 (+ y2 1))]
      (if (empty? ys)
        img
        (recur (paint-pixel img colour x (first ys))
               (rest ys))))))

(defn paint-horizontal-line [image colour x1 x2 y]
  (when (and (in-scope? image x1 y)
             (in-scope? image x2 y))
    (loop [img image
           xs (range x1 (+ x2 1))]
      (if (empty? xs)
        img
        (recur (paint-pixel img colour (first xs) y)
               (rest xs))))))

(defn paint-bucket-fill [image new-colour x y]
  (when (in-scope? image x y)
    (let [position (pixel-position x y)
          old-colour (get-in image position)]
      (if (= new-colour old-colour)
        image
        (loop [image image
               queue [position]]
          (if (empty? queue)
            image
            (let [position (first queue)
                  colour (get-in image position)]
              (if (= old-colour colour)
                (recur (assoc-in image position new-colour)
                       (concat (rest queue) (pixel-neighbors position)))
                (recur image
                       (rest queue))))))))))
