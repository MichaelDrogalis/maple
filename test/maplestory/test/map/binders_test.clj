(ns maplestory.test.map.binders-test
  (:require [midje.sweet :refer [fact =>]]
            [maplestory.server.map.binders :refer [compress birth]]))

(fact (compress {:hp 100}) => {:hp 100})
(fact (compress {:map :specs}) => {})

(fact (:id @(birth {})) =not=> nil)

(defn without-id [agent]
  (dissoc @agent :id))

(fact (without-id (birth {} :origin {:x 0 :y 0})) =>
      {:position {:x 0 :y 0}
       :origin   {:x 0 :y 0}})

(fact (without-id (birth {:speed 20} :origin {:x 5 :y 3})) =>
      {:position {:x 5 :y 3}
       :origin   {:x 5 :y 3}
       :speed    20})

(fact (without-id (birth {:speed 20} :speed 40 :origin {:x 1 :y 1})) =>
      {:position {:x 1 :y 1}
       :origin   {:x 1 :y 1}
       :speed    40})

