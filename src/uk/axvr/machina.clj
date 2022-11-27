(ns uk.axvr.machina
  "Machina v0.1 (prototype)  A Clojure HDL DSL."
  (:require [clojure.string :as str])
  (:import [java.time Duration]))

;; Will likely need https://github.com/jarohen/chime to control the clock.

;; (defn ->out []
;;   (ref nil))

;; (defn !clock [dur]
;;   (Duration/parse dur))

(def ^:private level->tag
  (comp str/upper-case name))

(defn- log* [level msg & opts]
  (print (format "[%s] %s" (level->tag level) msg))
  (when opts
    (print " :: ")
    (prn opts))
  (newline)
  (flush)
  (when (= level :err)
    (throw (ex-info msg {:mach/level level
                         :opts       opts}))))

(def log (partial log* :log))
(def err (partial log* :err))

(defn module? [x]
  (map? x))

(def ^:dynamic *mach-ns*
  "Machina current namespace."
  nil)

(defn module [name doc io body]
  {:mach.mod/name name
   :mach.mod/doc  doc
   :mach.mod/in   (:in io)
   :mach.mod/out  (:out io)
   :mach.mod/con  (:con io)
   :mach.mod/src  (list 'mod name doc io body)
   :mach.mod/ns   *mach-ns*
   :mach.mod/deps ()})

(defn nand [a b]
  ;;(module? a)
  (not (and a b)))

(def init-env
  {:prims {'nand #'nand
           'log  #'log
           'err  #'err}
   :mods {}})

(defn eval-mach [env mach-code]
  (let [[op & params] mach-code]
    (when-let [prim (get-in env [:prims op])]
      (apply prim params))))

(comment
  (eval-mach
   init-env
   '(log "something happened" 1 2 3))

  (eval-mach
   init-env
   '(err "something unexpected" 1 2 3))

  (eval-mach
   init-env
   '(nand false true))

  (eval-mach
   init-env
   '(nand true (nand true true))))
