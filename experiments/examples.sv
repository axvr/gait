// SystemVerilog examples.


// Structural design.
// Describes the circuit.
// Difficult to understand.

module mux (
    input wire a, b, s,
    output wire o
  );

  wire ns;
  wire sa;
  wire nsb;

  assign ns = !s;
  assign sa = a & s;
  assign nsb = b & ns;
  assign o = sa | nsb;

endmodule



// Behavioural design.
// Hard to visualise circuit.
// Easy to understand, more like a PL.

module mux (
    input wire a, b, s,
    output wire o
  );

  always @(a or b or s) begin
    if (s == 1'b1) begin
      o = a;
    end else begin
      o = b;
    end
  end

endmodule



;; Machina (currently)

(mod mux
  {:i [a b s]
   :o [o]}
  (o (or (and s a)
         (and (not s) b))))

(mod mux
  {:i [a b s]
   :o [o]}
  (o (if s a b)))

;; Machina will be able to tell that this is just "if" and will provide a
;; relevant refactor option.  In fact, it will encourage just removing this "mux" module.

;; Definition of `if`
(mod if
  {:i [cond then else]
   :o [result]}
  (result (or (and cond then)
              (and (not cond) else))))
