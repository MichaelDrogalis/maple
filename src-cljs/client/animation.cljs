(ns client.animation)

(def jquery (js* "$"))

(defn swap-image! [selector queue duration classes]
  (doseq [class classes]
    (.queue (.delay (jquery (str "#" selector)) duration queue)
            queue
            (fn [next-fn]
              (if (.hasClass (jquery (str "#" selector)) "mirrored")
                (.attr (jquery (str "#" selector)) "class" (str "mirrored" " " class))
                (.attr (jquery (str "#" selector)) "class" class))
              (next-fn))))
  (.dequeue (jquery (str "#" selector)) queue))

