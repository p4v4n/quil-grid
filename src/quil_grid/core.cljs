(ns quil-grid.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [quil-grid.conway :as conway]))

(def m 50)          ;;no: of rows
(def n 50)          ;;no: of columns
(def width 600)
(def height 600)
(def default-color 100)

;;---------------------------
(defn generate-single-rect [c x y i j]
  {:color c
   :loc {:x x :y y}
   :index {:i i :j j}})

(defn generate-single-row [y n i]
  (mapv #(generate-single-rect (conway/choose-color)
                               (* (/  width n) %) y
                               i  %)
        (range n)))

(defn generate-initial-state [m n]
  (mapv #(generate-single-row (* (/ height m)  %) n % ) (range m)))
;;------
(defn setup []
  (q/frame-rate 5)
  (q/no-stroke)
  ;;(q/color-mode :hsb)
  (generate-initial-state m n))
;;-----------------------------------
;; (defn update-color [color]
;;   color)

;; (defn update-single-rect [s-state]
;;   (update s-state :color update-color))

;; (defn update-single-row [s-row]
;;   (mapv update-single-rect s-row))

;; (defn update-whole-grid [state]
;;   (mapv update-single-row state))
;;-------------------
(defn update-state [state]
  (conway/conway-transform state))
;;--------------------------------------------
(defn draw-single-rect [s-state]
  (apply q/fill (:color s-state))
  (q/rect (get-in s-state [:loc :x]) (get-in s-state [:loc :y]) (/ width n) (/ height m)))

(defn draw-single-row [s-row]
  (mapv draw-single-rect s-row))

(defn draw-whole-grid [state]
  (mapv draw-single-row state))

;;----------
(defn draw-state [state]
  (q/background 240)
  (draw-whole-grid state))
;;-----------------------------------------
(q/defsketch quil-grid
  :host "quil-grid"
  :size [width height]
  :setup setup                ; setup function called only once, during sketch initialization.
  :update update-state ; update-state is called on each iteration before draw-state.
  :draw draw-state
  :middleware [m/fun-mode])
