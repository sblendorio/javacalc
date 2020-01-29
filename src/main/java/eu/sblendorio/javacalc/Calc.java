package eu.sblendorio.javacalc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.channels.IllegalSelectorException;

public class Calc {
    String gsFormula;

    public static BigDecimal eval(String expr) {
        return new Calc(expr.replace(" ", "")).expSum();
    }

    private Calc(String formula) {
        this.gsFormula = formula == null ? "" : formula;
    }

    private BigDecimal expSum() {
        String op;
        BigDecimal ris;

        ris = expProduct();
        while (true) {
            op = head(gsFormula);
            if ("+".equals(op)) {
                gsFormula = tail(gsFormula);
                ris = ris.add(expProduct());
            } else if ("-".equals(op)) {
                gsFormula = tail(gsFormula);
                ris = ris.subtract(expProduct());
            } else {
                break;
            }
        }
        return ris;
    }

    private BigDecimal expNumber() {
        BigDecimal ris = BigDecimal.ZERO;
        String number = "";
        String functionName = "";
        BigDecimal functionArgument = BigDecimal.ZERO;

        String c = head(gsFormula);
        if (c.matches("[0-9\\.]")) {
            while (true) {
                c = head(gsFormula);
                if (c.matches("[0-9\\.]")) {
                    number += head(gsFormula);
                    gsFormula = tail(gsFormula);
                } else {
                    ris = new BigDecimal(number);
                    break;
                }
            }
        } else if (c.equals("(")) {
            gsFormula = tail(gsFormula);
            ris = expSum();
            if (!")".equals(head(gsFormula))) {
                throw new IllegalArgumentException();
            } else {
                gsFormula = tail(gsFormula);
            }
        } else if (c.matches("[a-zA-Z]")) {
            while (true) {
                c = head(gsFormula);
                if (c.matches("[a-zA-Z]")) {
                    functionName += head(gsFormula);
                    gsFormula = tail(gsFormula);
                } else if (c.equals("(")) {
                    gsFormula = tail(gsFormula);
                    functionArgument = expSum();
                    if (!")".equals(head(gsFormula))) {
                        throw new IllegalArgumentException();
                    } else {
                        gsFormula = tail(gsFormula);
                        ris = evaluateFunction(functionName, functionArgument);
                        break;
                    }
                }
            }
        }
        return ris;
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
        } else if ("abs".equalsIgnoreCase(functionName)) {
            return functionArgument.abs();
        } else {
            throw new IllegalSelectorException();
        }
    }

    private BigDecimal expProduct() {
        String op;
        BigDecimal ris;
        BigDecimal num = BigDecimal.ZERO;

        ris = expNumber();
        while (true) {
            op = head(gsFormula);
            if ("*".equals(op)) {
                gsFormula = tail(gsFormula);
                ris = ris.multiply(expNumber());
            } else if ("/".equals(op)) {
                gsFormula = tail(gsFormula);
                try {
                    num = expNumber();
                    ris = ris.divide(num);
                } catch (ArithmeticException e) {
                    if (e.getMessage().startsWith("Non-terminating")) {
                        ris = ris.divide(num, MathContext.DECIMAL128);
                    } else throw e;
                }
            } else {
                break;
            }
        }
        return ris;
    }

    private String head(String s) {
        return (s != null && s.length() > 0) ? s.substring(0, 1) : "";
    }

    private String tail(String s) {
        return (s != null && s.length() > 1) ? s.substring(1) : "";
    }
}
