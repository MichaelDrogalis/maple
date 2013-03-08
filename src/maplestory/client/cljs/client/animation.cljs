(ns client.animation
  (:require [client.jquery :refer [jq jq-id]]))

(defn swap-image! [selector duration classes]
  (let [queue selector]
    (doseq [class classes]
      (.queue (.delay (jq-id selector) duration queue)
              queue
              (fn [next-fn]
                (if (.hasClass (jq-id selector) "mirrored")
                  (.attr (jq-id selector) "class" (str "mirrored" " " class))
                  (.attr (jq-id selector) "class" class))
                (next-fn))))
    (.dequeue (jq-id selector) queue)))

(defn addClass [element class]
  (if (.hasClass element "mirrored")
    (.attr element "class" (str "mirrored" " " class))
    (.attr element "class" class)))

(defn init [{:keys [id position direction]} container offset-height]
  (.append (jq "body") (container id))
  (.css (jq-id id) "left" (:x position))
  (.css (jq-id id) "top" (- (:y position) offset-height))
  (if (= direction :right)
    (.addClass (jq-id id) "mirrored"))
  (.css (jq-id id) "visibility" "visible"))

(defn flip [{:keys [id direction]}]
  (if (= direction :left)
    (.removeClass (jq-id id) "mirrored")
    (.addClass (jq-id id) "mirrored")))

(defn move [{:keys [id position transient]} selector offset]
  (let [sleep-time (:sleep-ms transient)]
    (.animate (jq-id id) (clj->js {:left (:x position) :top (- (:y position) offset)}) sleep-time)
    (let [frames (take (quot sleep-time (quot 140 4)) (cycle (range 0 4)))]
      (doseq [n frames]
        (.gx (jq-id id)
             (clj->js {})
             (:sleep-ms transient)
             "Linear"
             (clj->js {:start (fn [el] (addClass el (str selector n)))}))))))

