(ns sheet-music-generator.music-xml
  (:require [hiccup.core :as h]
            [hiccup.page :as page]))

(defn generate-random-piano
  "Return a MusicXML string describing a randomly-generated piece as
  dictated by `options`, a map containing the following keys:

  - :length - the number of measures to produce"
  [options]
  (h/html {:mode :xml}
          (page/xml-declaration "UTF-8")
          " <!DOCTYPE score-partwise PUBLIC \"-//Recordare//DTD MusicXML 3.0 Partwise//EN\" \"http://www.musicxml.org/dtds/partwise.dtd\">"
          [:score-partwise
           [:identification
            [:creator {:type "software"}
             "sheet-music-generator"]]
           [:part-list
            [:score-part {:id "P1"}
             [:part-name "Piano"]]]
           [:part {:id "P1"}
            (for [n (->> options :length range (map inc))]
              [:measure {:number n}
               [:attributes
                [:divisions 1]
                [:key
                 [:fifths 0]]
                [:time
                 [:beats 4]
                 [:beat-type 4]]
                [:clef {:number 1}
                 [:sign "G"]
                 [:line 2]]
                [:clef {:number 2}
                 [:sign "F"]
                 [:line 4]]]
               [:note
                [:pitch
                 [:step "C"]
                 [:octave 4]]
                [:duration 1]
                [:staff 1]
                [:type "quarter"]]
               [:note
                [:pitch
                 [:step "C"]
                 [:octave 2]]
                [:duration 1]
                [:staff 2]
                [:type "quarter"]]])]]))
