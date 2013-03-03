(ns maplestory.test.physics-test
  (:require [midje.sweet :refer [fact =>]]
            [maplestory.server.physics :refer [flip-direction flip platform]]))

(fact (flip-direction :left) => :right)
(fact (flip-direction :right) => :left)

(fact (flip {:direction :left}) => {:direction :right :action :flip})
(fact (flip {:direction :right}) => {:direction :left :action :flip})

(fact (platform :from 3 :to 4 :on 5) => #{{:x 3 :y 5} {:x 4 :y 5}})
(fact (platform :from 0 :to 2 :on 1) => #{{:x 0 :y 1} {:x 1 :y 1} {:x 2 :y 1}})

