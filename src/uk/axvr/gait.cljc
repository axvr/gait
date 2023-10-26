(ns uk.axvr.gait
  (:require [clj-cbor.core :as cbor]))

(def genkey
  "`clojure.core/gensym` but returns a keyword."
  (comp keyword gensym #(str % "__")))

(defmacro with-genkeys
  "Bulk generate \"genkey\" values and bind them to each `sym`."
  [syms & body]
  `(let ~(into []
               (mapcat (juxt identity (comp genkey name)))
               syms)
     ~@body))

(defrecord Module [in out mods cons])

(def network
  "Initial example network.  This is complicated, but not intended to be
  written by hand.  Everything uses IDs to prevent renames breaking things."
  (with-genkeys [nand_a nand_b nand_q
                 not_a not_q
                 and_a and_b and_q
                 or_a or_b or_q]
    {:colls {:uk.axvr.gait {:mods {:nand (map->Module
                                          {:in  {nand_a "a", nand_b "b"}
                                           :out {nand_q "q"}})
                                   :not  (with-genkeys [nand_1]
                                           (map->Module
                                            {:in   {not_a "a"}
                                             :out  {not_q "q"}
                                             :mods {nand_1 [:uk.axvr.gait :nand]}
                                             :cons {not_q [[nand_1 nand_a]
                                                           [nand_1 nand_b]]
                                                    [nand_1 nand_q] [not_q]}}))
                                   :and  (with-genkeys [nand_1 not_1]
                                           (map->Module
                                            {:in   {and_a "a", and_b "b"}
                                             :out  {and_q "q"}
                                             :mods {nand_1 [:uk.axvr.gait :nand]
                                                    not_1  [:uk.axvr.gait :not]}
                                             :cons {and_a [[nand_1 nand_a]]
                                                    and_b [[nand_1 nand_b]]
                                                    [nand_1 nand_q] [[not_1 not_a]]
                                                    [not_1 not_q] [and_q]}}))
                                   :or   (with-genkeys [not_1 not_2 nand_1]
                                           (map->Module
                                            {:in   {or_a "a", or_b "b"}
                                             :out  {or_q "q"}
                                             :mods {not_1  [:uk.axvr.gait :not]
                                                    not_2  [:uk.axvr.gait :not]
                                                    nand_1 [:uk.axvr.gait :nand]}
                                             :cons {or_a [[not_1 not_a]]
                                                    or_b [[not_2 not_a]]
                                                    [not_1 not_q] [nand_1 nand_a]
                                                    [not_2 not_q] [nand_1 nand_b]
                                                    [nand_1 nand_q] or_q}}))}}}}))

;; Directed graph.
;; Adjacency list over matrix.
;; How to represent buses?

;; Entrypoint.

;; Everything (module names, IO, etc.) should have IDs and display names.
;; (Auto-generate display names if none given.)

;; Auto-wiring and variadic inputs?

;; Graph diff algorithms will be required.  Should be a fun challenge.

;; WYSIWYG/rich documentation on modules and collections.  Links to websites,
;; files and other modules.  Attach docs to collections, modules, module IO
;; and wires.

;; Handling of different collection versions?  Later challenge, or consider
;; from the start?  Collections depend on specific versions of other
;; collections.

;; Use CBOR + extensions as the serialisation format for Gait files and server messages.
;; Would want custom read/write handlers to convert into record objects.

;; How to handle cyclical collection dependencies?

;; Module vs. package vs. collection vs. component vs. assembly vs. system.
;; Thunks, Modules, Instances, Collections, Networks.

;; Should input evaluation order matter?  Probably not.  Assume the simulator
;; will and can perform evaluation however it likes even concurrently.

;; Module instances are only required when they are impure.
;; Instances can be implemented purely by extending a module with hidden IO.

;; Simulator should be entirely in control, to enable concurrent and parallel
;; reactions, snapshotting, time travel, optimisations and refactoring
;; suggestions.
