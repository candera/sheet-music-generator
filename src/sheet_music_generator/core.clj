(ns sheet-music-generator.core
  (:require [clojure.java.io :as io])
  (:import [javax.sound.midi
            MetaMessage
            MidiEvent
            MidiMessage
            MidiSystem
            Sequence
            ShortMessage
            SysexMessage
            Track]))

;; Useful snippets
(comment
  (MidiSystem/write sequence 1 (io/file "/Users/candera/tmp/test-out.mid")))

(def sequencer (doto (MidiSystem/getSequencer)
                 (.open)))

(def short-message-command-name
  {ShortMessage/ACTIVE_SENSING        :active-sensing
   ShortMessage/CHANNEL_PRESSURE      :channel-pressure
   ShortMessage/CONTINUE              :continue
   ShortMessage/CONTROL_CHANGE        :control-change
   ShortMessage/END_OF_EXCLUSIVE      :end-of-exclusive
   ShortMessage/MIDI_TIME_CODE        :midi-time-code
   ShortMessage/NOTE_OFF              :note-off
   ShortMessage/NOTE_ON               :note-on
   ShortMessage/PITCH_BEND            :pitch-bend
   ShortMessage/POLY_PRESSURE         :poly-pressure
   ShortMessage/PROGRAM_CHANGE        :program-change
   ShortMessage/SONG_POSITION_POINTER :song-position-pointer
   ShortMessage/SONG_SELECT           :song-select
   ShortMessage/START                 :start
   ShortMessage/STOP                  :stop
   ShortMessage/SYSTEM_RESET          :system-reset
   ShortMessage/TIMING_CLOCK          :timing-clock
   ShortMessage/TUNE_REQUEST          :tune-request})

(def meta-type
  {0x00 :sequence-number
   0x01 :text-event
   0x02 :copyright-notice
   0x03 :track-name
   0x04 :instrument-name
   0x05 :lyric-text
   0x06 :marker-text
   0x07 :cue-point
   0x20 :midi-channel-prefix-assignment
   0x2F :end-of-track
   0x51 :tempo-setting
   0x54 :smtpe-offset
   0x58 :time-signature
   0x59 :key-signature})

(defprotocol Datafy
  (datafy* [this] "Turn something into data"))

(declare datafy)

(extend-protocol Datafy
  MidiEvent
  (datafy* [event]
    (merge {:tick (.getTick event)}
           (datafy (.getMessage event))))

  Sequence
  (datafy* [midiseq]
    (into []
          (for [track (.getTracks midiseq)]
            (datafy track))))

  Track
  (datafy* [track]
    (mapv #(-> track (.get %) datafy)
          (range (.size track))))

  MetaMessage
  (datafy* [message]
    {:message-type :meta
     :data (.getData message)
     :meta-type (meta-type (.getType message) :other)})

  ShortMessage
  (datafy* [message]
    {:message-type :short
     :command (short-message-command-name (.getCommand message) :other)
     :data [(.getData1 message)
            (.getData2 message)]})

  SysexMessage
  (datafy* [message]
    {:message-type :sysex}))

;; Wrapper function, to give us one place to put things like logic,
;; special cases, etc., rather than having to duplicate it through
;; each type-specific protocol implementation
(defn datafy
  "Turns something into data"
  [x]
  (datafy* x))

(defn load-midi-file
  "Given something that can be passed to clojure.java.io/input-stream,
  return a data structure representing the MIDI data encoded therein."
  [source]
  (with-open [source-stream (io/input-stream source)]
    (datafy (MidiSystem/getSequence source-stream))))
