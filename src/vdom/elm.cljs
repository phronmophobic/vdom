(ns vdom.elm
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:require [cljs.core.async :refer [chan <! put!]]
            [vdom.core :refer [renderer]]))

(defn foldp [f init in]
  (let [out (chan)]
    (put! out init)
    (go-loop [m init
              v (<! in)]
      (let [m' (f m v)]
        (put! out m')
        (recur m' (<! in))))
    out))

(defn event [ch x]
  (fn [e]
    (if (.-preventDefault e)
      (.preventDefault e))
    (put! ch x)))

(defn render! [views elem]
  (let [render (renderer elem)]
    (go-loop []
      (render (<! views))
      (recur))))