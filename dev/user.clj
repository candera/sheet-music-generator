(ns user
  (:require [clojure.pprint :refer [pprint]]
            [clojure.repl :refer :all]
            [clojure.tools.namespace.repl]
            [sheet-music-generator.core :refer :all])
  (:import [javax.sound.midi
            MidiSystem
            Sequence]))

(defn reset
  []
  (clojure.tools.namespace.repl/refresh))
