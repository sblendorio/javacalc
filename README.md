# JavaCalc - an algebraic expression calculator

**JavaCalc** is a *recursive descent parser* that evaluates math expression, including a bunch of math functions and with *variables* support.

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
 
## Available math functions
- `sqrt(x)`: square root of `x`
- `sin(x)`: sine of `x` radiants
- `cos(x)`: cosine of `x` radiants
- `tan(x)`: tangent of `x` radiants
- `asin(x)`: arcsine of `x`
- `acos(x)`: arccosine of `x`
- `atan(x)`: arctangent of `x`
- `sgn(x)`: signum of `x` (positive: 1, negative: -1, otherwise 0)
- `abs(x)`: absolute value of `x`
