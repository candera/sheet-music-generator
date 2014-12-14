;;;; Scratch file for random snippets of code

(import '[javax.sound.midi MidiSystem])
(require '[clojure.java.io :as io])

(let [sequencer (MidiSystem/getSequencer)
      _ (.open sequencer)
      sequence (MidiSystem/getSequence (io/file "/Users/candera/Documents/test.mid"))
      [track1 track2] (.getTracks sequence)]
  (-> track1 (.get ) .getMessage .getCommand)
  ;;(MidiSystem/write sequence 1 (io/file "/Users/candera/tmp/test-out.mid"))
  )

(load-midi-file "/Users/candera/Documents/test.mid")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(import '[org.jfugue
          MusicStringParser
          MusicXmlRenderer
          Pattern
          Parser
          Player])
(require '[clojure.java.io :as io])

(let [renderer (MusicXmlRenderer.)
      parser (MusicStringParser.)
      pattern (Pattern. "V0 A B C D C B A V1 C2 D2 E2 F2 G2 A2 B2 C2")]
  (.addParserListener parser renderer)
  (.parse parser pattern)
  (spit "/tmp/out.xml" (.getMusicXMLString renderer)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(require 'sheet-music-generator.music-xml)
(spit "/Users/candera/Dropbox/public/test.xml" (sheet-music-generator.music-xml/generate-random-piano {:length 1}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(import [javax.sound.midi MidiSystem Sequence MidiEvent ShortMessage])

(defn rand-between
  [low high]
  (+ low (rand-int (inc (- high low)))))

(let [note-offset {:C  0
                   :C# 1
                   :D  2
                   :D# 3
                   :E  4
                   :F  5
                   :F# 6
                   :G  7
                   :G# 8
                   :A  9
                   :A# 10
                   :B  11}]
  (defn note-number
    [[name octave]]
    (+ (note-offset name) (* (inc octave) 12))))

(defn populate-track
  [track notes n step]
  (loop [tick 1
         n (dec n)]
    (let [note (note-number (rand-nth notes))]
      (.add track
            (MidiEvent. (ShortMessage. ShortMessage/NOTE_ON
                                       note
                                       0x7F)
                        tick))
      (.add track
            (MidiEvent. (ShortMessage. ShortMessage/NOTE_ON
                                       note
                                       0x00)
                        (+ tick step))))
    (when (pos? n)
      (recur (+ tick step) (dec n)))))

(let [sequencer (MidiSystem/getSequencer)
      _ (.open sequencer)
      sequence (Sequence. Sequence/PPQ 120 2)
      [treble bass] (.getTracks sequence)
      bass-notes   [                            [:G 2] [:A 2] [:B 2]
                    [:C 3] [:D 3] [:E 3] [:F 3] [:G 3] [:A 3] [:B 3]
                    [:C 4]]
      treble-notes [[:C 4] [:D 4] [:E 4] [:F 4] [:G 4] [:A 4] [:B 4]
                    [:C 5] [:D 5] [:E 5] [:F 5] [:G 5]]

      n 571
      step 120]
  (populate-track treble treble-notes n step)
  (populate-track bass bass-notes n step)
  (MidiSystem/write sequence 1 (io/file "/Users/candera/tmp/test-out-2.mid"))

  )

(rand-between 10 11)
