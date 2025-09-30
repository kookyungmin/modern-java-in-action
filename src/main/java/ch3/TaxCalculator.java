package ch3;

import java.util.function.DoubleUnaryOperator;

public class TaxCalculator {
    public DoubleUnaryOperator taxFunction = d -> d;

    public TaxCalculator with(DoubleUnaryOperator f) {
        taxFunction = taxFunction.andThen(f);
        return this;
    }

    public double calculate(double value) {
        return taxFunction.applyAsDouble(value);
    }
}
