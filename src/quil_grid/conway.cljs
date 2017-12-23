(ns quil-grid.conway)

;;---Initial State Helpers

(defn alive-or-dead []
  (nth [:alive :dead] (rand-int 2)))

(def color-choice {:alive [0 0 0] :dead [255 255 255]}) ;; alive - black , dead-white

(defn choose-color []
  (color-choice (alive-or-dead)))

;;---Transform State

(defn valid-index [m n [i j]]
  (and (>= i 0) (>= j 0) (< i m) (< j n)))

(defn neighbours [m n i j]
  (filter (partial valid-index m n)
          [[(dec i) (dec j)] [(dec i) (inc j)] [(inc i) (dec j)] [(inc i) (inc j)]
           [(dec i) j] [(inc i) j] [i (dec j)] [i (inc j)]]))

;;----------------------------------
(defn update-color [alive-neighbours color]
  (cond
    (and (= color (:dead color-choice)) (= alive-neighbours 3)) (:alive color-choice)
    (and (= color (:alive color-choice)) (<= 2 alive-neighbours 3)) (:alive color-choice)
    :else (:dead color-choice)))

(defn update-single-rect [s-state state]
  (let [{i :i j :j} (:index s-state)
        m (count state)
        n (count (first state))
        neighbours (neighbours m n i j)
        alive-neighbours (->> (map #(get-in state %) neighbours)
                              (map  :color)
                              (map (partial = [0 0 0]))
                              (filter true?)
                              count)]
    (update s-state :color (partial update-color  alive-neighbours))))

(defn update-single-row [s-row state]
  (mapv #(update-single-rect % state) s-row))

(defn conway-transform [state]
  (mapv #(update-single-row % state) state))
