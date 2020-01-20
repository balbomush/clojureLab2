(ns main
  (:require [clojure.core.async :as a]))

(defn fill [n]
  (take n (repeatedly #(rand-int 100))))

(def c1 (a/chan 10))
(a/onto-chan c1 (fill 10))

(def c2 (a/chan 10))

(defn fun [c1 c2 n]
  (a/go-loop [prev 0]
    (when-some [value (a/<! c1)]
      ;(println "value " value " diff " (- value prev))
      (when (< n (- value prev))
        (println  "True")
        (a/>! c2 value))
      (recur value)))
  (println c2)
  (a/go-loop []
    (when-some [value (a/<! c2)]
      (println value)
      (recur)))
  )

(fun c1 c2 5)