(ns client.jquery)

(def jq (js* "$"))

(defn jq-id [id]
  (jq (str "#" id)))

