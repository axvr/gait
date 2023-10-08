(ns uk.axvr.gait
  (:refer-clojure :exclude [eval]))

;; Thunks, Modules, Instances, Networks.

;; Should input evaluation order matter?  Probably not.  Assume the simulator
;; will and can perform evaluation however it likes.

;; == Thunks ==
;;
;; Thunk evaluations should be cached!  Use delay?
;;
;; Implement clojure.lang.IFn on thunks?
;;   - probably not because I want the simulator to have more
;;     control.

;; NOTE: Temporary thunk evaluation.
(defn thunk-eval [thunk]
  (deref thunk))

;; == Primitive modules ==

;;  A  B | O
;; ------|---
;;  T  T | F
;;  T  F | T
;;  F  F | T
;;  F  T | T
(def !nand
  "Core primitive NAND module."
  {:in   '[a & bs]
   :out  '[o]
   :eval (fn [ins]
           (loop [[in & ins] (vals ins)]
             (cond
               (false? (thunk-eval in)) {:o true}
               (empty? ins)             {:o false}
               :else                    (recur ins))))})

(comment
  (let [a (delay true)
        b (delay true)]
    ((:eval !nand)
     {:a a, :b b}))
  )

;; (defn bare-net []
;;   {:modules {'nand !nand}})

;; TODO: module instances are only required when they are impure.
;;   Instances can be implemented purely by extending a module with hidden IO.

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
