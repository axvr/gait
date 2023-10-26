# Gait

An interactive, object-oriented [hardware description language (HDL)][HDL].

> **Warning**<br>
> This is a **very** work-in-progress prototype.

Influenced by many languages and systems including: [Clojure][], [Bel][], [Smalltalk][], [GRAIL][], [SystemVerilog][] and [Bass][].

[HDL]: https://en.wikipedia.org/wiki/Hardware_description_language
[Clojure]: https://clojure.org
[Bel]: http://www.paulgraham.com/bel.html
[Smalltalk]: https://en.wikipedia.org/wiki/Smalltalk
[GRAIL]: https://en.wikipedia.org/wiki/RAND_Tablet
[SystemVerilog]: https://en.wikipedia.org/wiki/SystemVerilog
[Bass]: https://github.com/vito/bass


## Road map

- [ ] Initial simulator (machine API)
- [ ] Module collections (namespaces)
- [ ] Simulator server (for IDEs, use RSocket over WebSockets + CBOR)
- [ ] Libraries (+ package management?)
- [ ] IDE (C# Godot?)
- [ ] Standard library
- [ ] Optimiser
- [ ] Netlist generation
- [ ] Load onto an FPGA


### Out of scope

- Compile to Verilog, SystemVerilog or VHDL.  (Use Clash instead.)
- Backwards compatibility with other systems and tools.
- Support for a wide range of FPGAs.


## Legal

Copyright © [Alex Vear](https://www.alexvear.com).

Gait is available under the terms of the [Mozilla Public License v2.0](https://www.mozilla.org/en-US/MPL/2.0/).  A full copy of the MPL 2.0 can be found in the accompanying [`LICENCE`](/LICENCE) file.
