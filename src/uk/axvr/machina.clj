(ns uk.axvr.machina
  "Machina v0.1 (prototype)  A Clojure DSL.")

(defn module
  ([name io body]
   (module name nil io body))
  ([name doc io body]
   {:name name
    :doc  doc
    :in   (:in io [])
    :out  (:out io [])
    :body body}))

(defn instantiate
  "Create an instance of a module."
  [{:keys [in out] :as module}]
  ;; TODO: initial reaction from nil inputs?
  (let [->io #(into {} (map (fn [n] {n nil})) %)
        outs (->io out)
        ins  (->io in)]
    ;; TODO: is all of this needed?
    (assoc module
           :out  outs
           :prev {:in  ins
                  :out outs})))

(comment
  (instantiate
   (module 'reg "A register module" '{:in [a b c] :out [o]} '((o i))))
  )

(defn- normalise-inputs
  "Normalise module inputs.

     (mod foo {:in [a b] :out [c]} ...)

     (foo true false)        ; => {:a true, :b false}
     (foo :b false :a true)  ; => {:a true, :b false}"
  [module inputs]
  ;; TODO: add validation.
  (into {}
        (if (keyword? (first inputs))
          (partition 2 inputs)
          (map vector (:in module) inputs))))

(comment
  (normalise-inputs
   (module 'reg "A register module" '{:in [a b c] :out [o]} '((o i)))
   '(:a true :c false :b true))

  (normalise-inputs
   (module 'reg "A register module" '{:in [a b c] :out [o]} '((o i)))
   '(true false true))

  (normalise-inputs
   (module 'reg "A register module" '{:in [a b c] :out [o]} '((o i)))
   '(true false))
  )

(defn react
  "Refresh a module from new inputs.

   Module + inputs => next module state."
  [module inputs]
  (let [prev-inputs (get-in module [:prev :in])]
    (if (= prev-inputs inputs)
      module
      (let [{:keys [body in]} module]
        ))))

(comment
  (let [module (module 'reg "A register module" '{:in [a b c] :out [o]} '((o i)))]
    (react
     module
     (normalise-inputs
      module
      '(:a true :c false :b true))))
  )

;; (send my-module react)

(defn eval-mach [env mach-code]
  (let [[op & params] mach-code]
    (when-let [prim (get-in env [:prims op])]
      (apply prim params))))

(comment
  (eval-mach
   {}
   '(log "something happened" 1 2 3))

  (eval-mach
   {}
   '(err "something unexpected" 1 2 3))

  (eval-mach
   {}
   '(nand false true))

  ;; TODO: get nested forms to evaluate.
  ;; Need modules first as this will happen during module react.
  (eval-mach
   {}
   '(nand true (nand true true)))

  (eval-mach
   {}
   '(mod reg
      "A register module"
      {:in [i] :out [o]}
      (o i)))

  (module 'reg "A register module" '{:in [i] :out [o]} '((o i)))
  )


;; {:colls {'coll {'sym ...}}
;;  :prims {'mod  ...
;;          'nand ...
;;          'log  ...
;;          'err  ...}}

;; (defn react
;;   "Given a module instance, react to new inputs and return the new
;;   state.  Recursive and lazily evaluates module inputs."
;;   [mod-inst inputs]
;;   (let [prev-inputs ...]
;;     (if (= prev-inputs inputs)
;;       mod-inst
;;       ())))


;; (defprotocol React
;;   (react [this]))
