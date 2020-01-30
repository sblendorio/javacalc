# JavaCalc - an algebraic expression calculator

**JavaCalc** is a *recursive descendant parser* that evaluates math expression, including a bunch of math functions and with *variables* support.

## Type system
**JavaCalc** uses `java.math.BigDecimal` as main type, in order not to lose precision during calculation.

## Usage examples

### Plain math operation
    BigDecimal result = Calc.eval("(2 + 2) * 3 /2");

### Using math functions
    BigDecimal result = Calc.eval("sqrt(9)*sin(3/2)");

### Using variables
    Map<String, Number> vars = new HashMap<>();
    vars.put("x", 9);
    vars.put("y", 12.5);
    
    BigDecimal result = Calc.eval("x + y*2", vars);
 
