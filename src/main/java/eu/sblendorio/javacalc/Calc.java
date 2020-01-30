package eu.sblendorio.javacalc;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toMap;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Calc {
    private String gsFormula;
    private Map<String, BigDecimal> varspace;

    public static BigDecimal eval(String expr) {
        return new Calc(expr, emptyMap()).expSum();
    }

    public static BigDecimal eval(String expr, Map<String, ? extends Number> varspace) {
        return new Calc(expr, varspace).expSum();
    }

    private Calc(String formula, Map<String, ? extends Number> vars) {
        Objects.nonNull(vars);
        Objects.nonNull(formula);

        varspace = vars.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, item ->new BigDecimal(item.getValue().toString())));

        this.gsFormula = formula.replace(" ", "");

    }

    private BigDecimal expSum() {
        String op;
        BigDecimal result;

        result = expProduct();
        while (true) {
            op = head(gsFormula);
            if ("+".equals(op)) {
                gsFormula = tail(gsFormula);
                result = result.add(expProduct());
            } else if ("-".equals(op)) {
                gsFormula = tail(gsFormula);
                result = result.subtract(expProduct());
            } else {
                break;
            }
        }
        return result;
    }

    private BigDecimal expNumber() {
        BigDecimal result = BigDecimal.ZERO;
        String number = "";
        String identifier = "";
        BigDecimal functionArgument = BigDecimal.ZERO;

        String c = head(gsFormula);
        if (c.matches("[0-9\\.]")) {
            while (true) {
                c = head(gsFormula);
                if (c.matches("[0-9\\.]")) {
                    number += head(gsFormula);
                    gsFormula = tail(gsFormula);
                } else {
                    result = new BigDecimal(number);
                    break;
                }
            }
        } else if (c.equals("(")) {
            gsFormula = tail(gsFormula);
            result = expSum();
            if (!")".equals(head(gsFormula))) {
                throw new IllegalArgumentException("Syntax error");
            } else {
                gsFormula = tail(gsFormula);
            }
        } else if (c.matches("[a-zA-Z]")) {
            while (true) {
                c = head(gsFormula);
                if (c.matches("[a-zA-Z]")) {
                    identifier += head(gsFormula);
                    gsFormula = tail(gsFormula);
                } else if (c.equals("(")) {
                    gsFormula = tail(gsFormula);
                    functionArgument = expSum();
                    if (!")".equals(head(gsFormula))) {
                        throw new IllegalArgumentException("Syntax error");
                    } else {
                        gsFormula = tail(gsFormula);
                        result = evaluateFunction(identifier, functionArgument);
                        break;
                    }
                } else {
                    result = varspace.get(identifier);
                    if (result == null) {
                        throw new IllegalArgumentException("Undefined variable '" + identifier + "'");
                    } else {
                        break;
                    }
                }
            }
        }
        return result;
    }

    private BigDecimal evaluateFunction(String functionName, BigDecimal functionArgument) {
        if ("sqrt".equalsIgnoreCase(functionName)) {
            return new BigDecimal(Math.sqrt(functionArgument.doubleValue()));
        } else if ("sin".equalsIgnoreCase(functionName)) {
            return new BigDecimal(Math.sin(functionArgument.doubleValue()));
        } else if ("cos".equalsIgnoreCase(functionName)) {
            return new BigDecimal(Math.cos(functionArgument.doubleValue()));
        } else if ("tan".equalsIgnoreCase(functionName)) {
            return new BigDecimal(Math.tan(functionArgument.doubleValue()));
        } else if ("asin".equalsIgnoreCase(functionName)) {
            return new BigDecimal(Math.asin(functionArgument.doubleValue()));
        } else if ("acos".equalsIgnoreCase(functionName)) {
            return new BigDecimal(Math.acos(functionArgument.doubleValue()));
        } else if ("atan".equalsIgnoreCase(functionName)) {
            return new BigDecimal(Math.atan(functionArgument.doubleValue()));
        } else if ("sgn".equalsIgnoreCase(functionName)) {
            return new BigDecimal(Math.signum(functionArgument.doubleValue()));
        } else if ("abs".equalsIgnoreCase(functionName)) {
            return functionArgument.abs();
        } else {
            throw new IllegalArgumentException("Undefined function '" + functionName + "'");
        }
    }

    private BigDecimal expProduct() {
        String op;
        BigDecimal result;
        BigDecimal num = BigDecimal.ZERO;

        result = expNumber();
        while (true) {
            op = head(gsFormula);
            if ("*".equals(op)) {
                gsFormula = tail(gsFormula);
                result = result.multiply(expNumber());
            } else if ("/".equals(op)) {
                gsFormula = tail(gsFormula);
                try {
                    num = expNumber();
                    result = result.divide(num);
                } catch (ArithmeticException e) {
                    if (e.getMessage().startsWith("Non-terminating")) {
                        result = result.divide(num, MathContext.DECIMAL128);
                    } else {
                        throw e;
                    }
                }
            } else {
                break;
            }
        }
        return result;
    }

    private String head(String s) {
        return (s != null && s.length() > 0) ? s.substring(0, 1) : "";
    }

    private String tail(String s) {
        return (s != null && s.length() > 1) ? s.substring(1) : "";
    }
}
