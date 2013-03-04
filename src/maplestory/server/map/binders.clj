(ns maplestory.server.map.binders)

(defonce maps (atom {}))

(defn compress [entity]
  (dissoc entity :map))

(defn register-map! [map-name]
  (swap! maps assoc map-name {:connections (ref #{}) :entities (atom #{})}))

(defn bind-client-to-monster [connection entity]
  (.send connection (pr-str {:type :init :message {(:type @entity) (compress @entity)}})))

(defn add-client-watch [connection entity]
  (add-watch entity
             connection
             (fn [_ _ _ state]
               (.send connection (pr-str {:type :update :message {:who (:type @entity) :event (compress @entity)}})))))

(defn register-client [connection connections entities]
  (dosync
   (commute connections conj connection)
   (doseq [entity @entities]
     (bind-client-to-monster connection entity)
     (add-client-watch connection entity))))

(defn unregister-client [connection connections entities]
  (dosync
   (commute connections disj connection)
   (doseq [entity @entities]
     (remove-watch entity connection))))

(defn add-heartbeat [entity f]
  (add-watch entity :heartbeat
             (fn [_ _ _ state]
               (when-let [ms (:sleep-ms (:transient state))]
                 (Thread/sleep ms))
               (Thread/sleep 75)
               (f state))))

;(remove-watch (first @(get-in @maps [:mushmom :entities])) :heartbeat)

(defn spawn [map-name entity f]
  (let [connections (get-in @maps [map-name :connections])
        entities    (get-in @maps [map-name :entities])
        scheduler   (f entity)]
    (swap! entities conj entity)
    (add-heartbeat entity scheduler)
    (doseq [connection @connections]
      (bind-client-to-monster connection entity)
      (add-client-watch connection entity))
    (scheduler entity)))

(defn birth [spawn-state & {:as options}]
  (let [entity-data (merge spawn-state
                           options
                           {:position (:origin options)}
                           {:id (name (gensym))})]
    (agent entity-data)))

