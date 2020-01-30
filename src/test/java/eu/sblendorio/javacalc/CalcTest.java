package eu.sblendorio.javacalc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalcTest {

    @Test
    public void shouldComputeCalculation() {
        assertEquals(
                new BigDecimal(6),
                Calc.eval("(2 + 2) * sqrt(9) /2")
        );
    }

    @Test
    public void shouldComputeCalculationWithVariables() {
        Map<String, BigDecimal> vars = new HashMap<>();

        vars.put("a", new BigDecimal(2));
        vars.put("b", new BigDecimal(3));

        assertEquals(
                new BigDecimal(5),
                Calc.eval("a+b", vars)
        );
    }
}
