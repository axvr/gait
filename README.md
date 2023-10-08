# Gait

An experimental, interactive, Lisp-based, object-oriented [hardware description language (HDL)][HDL].

This is a **very** work-in-progress prototype.  Currently it is just a Clojure DSL for quick prototyping, which will likely change in the future.

Gait is influenced by many languages including: [Clojure][], [Bel][], [Smalltalk][], [SystemVerilog][] and [Bass][].

[Clojure]: https://clojure.org
[Bel]: http://www.paulgraham.com/bel.html
[Smalltalk]: https://en.wikipedia.org/wiki/Smalltalk
[SystemVerilog]: https://en.wikipedia.org/wiki/SystemVerilog
[Bass]: https://github.com/vito/bass


## Example

> **Note**<br>
> Subject to change.

The file extension for Gait code is `.gt`.

```clojure
;; File can either be `gait.example.gt` or `gait/example.gt`.
(coll gait.example)

(mod not
  "Logical NOT - negation"
  {:in [a] :out [q]}
  (q (nand a a)))

(mod and
  "Logical AND (short circuiting)"
  {:in [a b] :out [q]}
  (q (not (nand a b))))

(mod or
  "Logical inclusive OR (short circuiting)"
  {:in [a b] :out [q]}
  (q (nand (not a) (not b))))

(mod xor
  "Logical exclusive OR - XOR (short circuiting)"
  {:in [a b] :out [q]}
  (bind [nand-ab (nand a b)]
    (q (nand (nand b nand-ab)
             (nand a nand-ab)))))

(mod half-adder
  {:in  [a b]
   :out [s c]}
  (s (xor a b))
  (c (and a b)))

(mod full-adder
  {:in  [a b c-in]
   :out [s c-out]
   :con {c-out c-in}}
  (bind [{:s s1 :c c1} (half-adder a b)
         {:s s2 :c c2} (half-adder s1 c-in)]
    (s s2)
    (c-out (or c1 c2))))

(full-adder :a true :b true :c-in false)
;; => {:s false, :c-out true}

(full-adder 0x3 0x1)
;; => {:s 0x4, :c-out false}

(full-adder 0x1 0x3)
;; => {:s 0x1, :c-out true}

(full-adder
  ;; con-max = 4, making it a 4-bit adder.
  ^{:gait/max 4}
  {:a 0x1, :b 0x3})
;; => {:s 0x4, :c-out false}
```

[HDL]: https://en.wikipedia.org/wiki/Hardware_description_language


## Road map

- [ ] Initial language reference
- [ ] Initial simulator
- [ ] Collections (namespaces)
- [ ] Standard library
- [ ] Libraries (+ package management?)
- [ ] Optimiser
- [ ] Netlist generation
- [ ] Load onto an FPGA
- [ ] IDE


### Out of scope

- Compile to Verilog, SystemVerilog or VHDL.  (Use Clash instead.)
- Backwards compatibility with other systems and tools.
- Support for a wide range of FPGAs.


## Legal

Copyright © [Alex Vear](https://www.alexvear.com).

Gait is available under the terms of the [Mozilla Public License v2.0](https://www.mozilla.org/en-US/MPL/2.0/).  A full copy of the MPL 2.0 can be found in the accompanying [`LICENCE`](/LICENCE) file.
