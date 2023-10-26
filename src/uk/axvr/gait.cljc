(ns uk.axvr.gait
  (:require [clj-cbor.core :as cbor]))

(defmacro with-gensyms
  "Bulk generate gensyms and bind the values to each `sym`."
  [syms & body]
  `(let ~(into []
               (mapcat (juxt identity
                             (comp (fn [s] `'~s) gensym name)))
               syms)
     ~@body))

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
  written by hand."
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

(get-in network [:colls :uk.axvr.gait :mods :not])

(comment

  ;; Initial version of the network?

  ;; (mod and {:in [a b] :out [q]}
  ;;   "Logical AND (short circuiting)"
  ;;   (q (not (nand a b))))

  ;; Adjacency list over matrix.

  ;; Directed graph.
  ;; How to represent busses?

  ;; Entrypoint.

  ;; Everything (module names, IO, etc.) should have IDs.  Only use the names
  ;; given as display.  Use gensyms as IDs?  (Maybe just initially.)

  ;; TODO: auto-wiring and variadic inputs?

  ;; Reader tags #gait/mod and #gait/coll for creating an instance of a module
  ;; and collection record.  I'm curious to see performance difference between
  ;; regular maps and records.

  ;; Graph diff algorithms will be required.  Should be a fun challenge.

  ;; WYSIWYG/rich documentation on modules and collections.  Links to websites,
  ;; files and other modules.  Attach docs to wires, modules and collections.

  ;; Handling of different collection versions?  Later challenge, or consider
  ;; from the start?  Collections depend on specific versions of other
  ;; collections.

  ;; Use CBOR + extensions as the serialisation format for Gait .gaitc files.
  ;; Would want custom read/write handlers to convert into record objects.

  (-> {:foo 1234} cbor/encode cbor/decode)

  )

;; Module vs. package vs. collection vs. component vs. assembly vs. system.

;; Thunks, Modules, Instances, Networks.

;; Should input evaluation order matter?  Probably not.  Assume the simulator
;; will and can perform evaluation however it likes even concurrently.

;; TODO: module instances are only required when they are impure.
;;   Instances can be implemented purely by extending a module with hidden IO.
;;
;;     (def !stateful-mod
;;       {:in       '[a & bs]
;;        :state-in '[s#]
;;        :out      '[o so#]})

;; Simulator should be entirely in control, to enable concurrent and parallel
;; reactions, snapshotting, time travel, optimisations and refactoring
;; suggestions.

;; Collections
;;   Coll references/imports
;;   Modules
;;     Inputs/outputs
;;     Instances
