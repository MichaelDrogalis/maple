(ns maplestory.test.stump-test
  (:require [midje.sweet :refer [fact =>]]
            [maplestory.server.physics :refer [platform]]
            [maplestory.server.monster.stump :refer [move change-position]]))

(let [state {:origin {:x 0 :y 0}
             :position {:x 0 :y 0}
             :boundaries {:x {:left 0}}
             :direction :left}]
  (fact (move state) => (assoc state :action :flip :direction :right)))

(let [state {:origin {:x 25 :y 0}
             :position {:x 25 :y 0}
             :boundaries {:x {:left 0}}
             :direction :left
             :speed 15
             :map {:footing (platform :from 0 :to 25 :on 0)}}]
  (fact (change-position state) => (assoc state :action :move :position {:x 10 :y 0} :transient {:sleep-ms 140})))

