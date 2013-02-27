(ns maplestory.test.movement-test
  (:require [midje.sweet :refer [fact =>]]
            [maplestory.server.movement :refer :all]))

(fact (flip-direction :left) => :right)
(fact (flip-direction :right) => :left)


