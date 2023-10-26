(ns uk.axvr.gait
  (:refer-clojure :exclude [eval]))

;; Thunks, Modules, Instances, Networks.

;; Should input evaluation order matter?  Probably not.  Assume the simulator
;; will and can perform evaluation however it likes even concurrently.

;; == Thunks ==
;;
;; Thunk evaluations should be cached!  Use delay?

;; NOTE: Temporary thunk evaluation.
(defn thunk-eval [thunk]
  (deref thunk))

;; == Primitive modules ==

(def !nand
  "Core primitive NAND module."
  {:name 'nand
   :in   '[a & bs]
   :out  '[o]
   :eval (fn [ins]
           (loop [[in & ins] (vals ins)]
             (cond
               (false? (thunk-eval in)) {'o true}
               (empty? ins)             {'o false}
               :else                    (recur ins))))})

(comment
  (let [a (delay true)
        b (delay true)]
    @(delay ((:eval !nand)
             {'a a, 'b b})))
  )

;; (defn bare-net []
;;   {:modules {'nand !nand}})

;; TODO: module instances are only required when they are impure.
;;   Instances can be implemented purely by extending a module with hidden IO.
;;
;;     (def !stateful-mod
;;       {:in       '[a & bs]
;;        :state-in '[s#]
;;        :out      '[o so#]})

;; Plan:
;;   - Compile into a network/graph.
;;   - Simulator should navigate this graph and perform reactions.
;;
;; Simulator should be entirely in control, to enable concurrent and parallel
;; reactions, snapshotting, time travel, optimisations and refactoring
;; suggestions.

;; Collections
;;   Coll references/imports
;;   Modules
;;     Inputs/outputs
;;     Instances
;;
;; Collections can be added later.
