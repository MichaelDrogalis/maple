(ns maplestory.test.physics-test
  (:require [midje.sweet :refer [fact =>]]
            [maplestory.server.physics :refer :all]))

(fact (flip-direction :left) => :right)
(fact (flip-direction :right) => :left)

(fact (flip {:direction :left}) => {:direction :right :action :flip})
(fact (flip {:direction :right}) => {:direction :left :action :flip})

(fact (platform :from 3 :to 4 :on 5) => #{{:x 3 :y 5} {:x 4 :y 5}})
(fact (platform :from 0 :to 2 :on 1) => #{{:x 0 :y 1} {:x 1 :y 1} {:x 2 :y 1}})

(fact (ms-for-pixels 45 15) => 420)
(fact (total-pixels-moved 0 3) => 3)
(fact (total-pixels-moved 3 0) => 3)

(fact (should-turn-around? {:x 1} :left {:x {:left 0}}) => false)
(fact (should-turn-around? {:x 1} :left {:x {:left 1}}) => true)
(fact (should-turn-around? {:x 3} :right {:x {:right 4}}) => false)
(fact (should-turn-around? {:x 3} :right {:x {:right 3}}) => true)

(fact (units-in-direction {:x 0} 2 :right {:x {:right 4}}) => 2)
(fact (units-in-direction {:x 0} 2 :right {:x {:right 3}}) => 2)
(fact (units-in-direction {:x 0} 2 :right {:x {:right 2}}) => 2)
(fact (units-in-direction {:x 0} 2 :right {:x {:right 1}}) => 1)

(fact (units-in-direction {:x 4} 2 :left {:x {:left 0}}) => -2)
(fact (units-in-direction {:x 4} 2 :left {:x {:left 1}}) => -2)
(fact (units-in-direction {:x 4} 2 :left {:x {:left 2}}) => -2)
(fact (units-in-direction {:x 4} 2 :left {:x {:left 3}}) => -1)

(fact (drop-elevation 1 0 #{{:x 0 :y 0} {:x 1 :y 0}}) => 0)
(fact (drop-elevation 1 1 #{{:x 0 :y 1} {:x 1 :y 2}}) => 2)

