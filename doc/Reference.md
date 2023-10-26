# Machina reference

This is an evolving reference of Machina.  It exists to document how Machina is intended to work, primarily to help me implement it correctly, however you may find it useful as a guide to Machina.

Being a [hardware description language (HDL)][HDL], Machina is not like your typical programming language.  Even compared to other HDLs, much of Machina is unique, particularly its modules (objects) and evalation model.  At a high level, Machina can be best described as an object-oriented, Lisp-based, HDL.  (Its object model is closer to Smalltalk than Java or C++.)

Initially, Machina is being implemented as a Clojure DSL for ease of prototyping, this will likely change in the future.  While it is implemented *on Clojure*, it will use Clojure's reader and printer.  This is a double-edged sword though, on the one hand, it makes prototyping easier, but on the other, it restricts Machina's syntax to a subset of Clojure's.  When Machina eventually gains its own reader, I expect the syntax to change.

[HDL]: https://en.wikipedia.org/wiki/Hardware_description_language

<!--
## Atoms

Atoms are Machina's foundational primitives, they cannot be decomposed further.


### Labels

Labels are like Lisp keywords, they are most commonly used for labeling the inputs and outputs of modules.  Labels begin with a colon character `:`.

Example labels:

```clojure
:in
:out
:a
:b!
:q
:q'
:hello-world?
```


### Symbols

Symbols are used to name things.  Most often they name modules.

```clojure
foo
nand
mod
def
bind
hello-world?
if
```


### Booleans

Machina booleans are Clojure booleans.

```clojure
true
false
```


### Nil


### Strings


### Comments


### Numbers


## Modules

The fundamental building block of Machina is the "module".  You can think of a module as hybrid of a class and a component you would see on a [circuit schematic](https://en.wikipedia.org/wiki/Circuit_diagram).

Modules have inputs and outputs.

The primitive module is `nand` which takes 2 inputs and has an output.

```clojure
(nand true true)   ; => false
(nand true false)  ; => true
```



The following example defines a module called "not", that has an input line: `a`, and an output line: `q`.  When `a` changes, `q` is changed to the result of `a NAND a` (i.e. [NOT](https://en.wikipedia.org/wiki/Negation) `a`).

```clojure
(mod not
  "Logical NOT - negation"
  {:in [a] :out [q]}
  (q (nand a a)))
```


## Evaluation model

As far as I am aware, Machina's evaluation model is unique.

_WIP_


### Module reaction

_WIP_


## Optimiser

_WIP_
-->
