package eu.sblendorio.javacalc;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalcTest {

    @Test
    public void shouldComputeCalculation() {
        Assertions.assertEquals(new BigDecimal(4), Calc.eval("2 + 2"));
    }
}
