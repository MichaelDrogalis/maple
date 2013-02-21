(ns client.animation)

(def jquery (js* "$"))

(defn swap-image! [selector queue duration classes]
  (doseq [class classes]
    (.queue (.delay (jquery selector) duration queue)
            queue
            (fn [next-fn]
              (if (.hasClass (jquery selector) "mirrored")
                (.attr (jquery selector) "class" (str "mirrored" " " class))
                (.attr (jquery selector) "class" class))
              (next-fn))))
  (.dequeue (jquery selector) queue))

