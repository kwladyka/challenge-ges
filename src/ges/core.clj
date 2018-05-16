(ns ges.core
  (:require [clojure.string :as str]
            [ges.editor :as editor]))

(defn show-image [image]
  (println "=>")
  (doseq [row image]
    (-> (apply str row)
        (println))))

(defn read-input []
  (print "> ")
  (flush)
  (read-line))


(defmulti cmd-editor
          (fn [cmd image & args]
            cmd))

(defmethod cmd-editor "I" [_ _ x-size y-size]
  (editor/create-image (Integer. x-size) (Integer. y-size)))

(defmethod cmd-editor "C" [_ image]
  (editor/clean-image image))

(defmethod cmd-editor "L" [_ image x y colour]
  (editor/paint-pixel image colour (Integer. x) (Integer. y)))

(defmethod cmd-editor "V" [_ image x y1 y2 colour]
  (editor/paint-vertical-line image colour (Integer. x) (Integer. y1) (Integer. y2)))

(defmethod cmd-editor "H" [_ image x1 x2 y colour]
  (editor/paint-horizontal-line image colour (Integer. x1) (Integer. x2) (Integer. y)))

(defmethod cmd-editor "F" [_ image x y colour]
  (editor/paint-bucket-fill image colour (Integer. x) (Integer. y)))

(defmethod cmd-editor "S" [_ image]
  (show-image image))

(defmethod cmd-editor :default [& _]
  (println "=> Command not found."))


(defn cmd [image input]
  (try
    (let [[cmd & args] (-> (str/trim input)
                           (str/split #" +"))]
      (apply cmd-editor cmd image args))
    (catch Throwable ex
      (println "=> Wrong command parameters. Probably you miss parameter or use char instead of integer.")
      ex)))

;; I use atom to demonstrate possibility of open
;; many images in editor at the same time.
;; Editor functions are independent from interface.
;; In this scenario console.

(defn start-session []
  (let [image (atom [])]
    (loop [input (read-input)]
      (when (not= "X" input)
        (when-let [new-image (cmd @image input)]
          (reset! image new-image))
        (recur (read-input))))))

(defn -main []
  (start-session))
