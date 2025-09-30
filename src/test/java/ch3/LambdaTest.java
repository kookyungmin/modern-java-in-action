package ch3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LambdaTest {
    @Test
    @DisplayName("함수체인 예제 :: UnaryOperator")
    public void test1() {
        //Function<T, T>
        UnaryOperator<String> p1 = (text) -> "Happykoo\n" + text;
        UnaryOperator<String> p2 = (text) -> text + "\nBy Happykoo";

        Function<String, String> pipeline = p1.andThen(p2);

        String expected = "Happykoo\n" +
                "Hello!\n" +
                "By Happykoo";
        String actual = pipeline.apply("Hello!");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("람다를 이용한 DSL 예제 :: TaxCalculator")
    public void test2() {
        double value = 1.4;
        double expected = Tax.regional(Tax.general(value));
        double actual = new TaxCalculator()
                .with(Tax::general)
                .with(Tax::regional)
                .calculate(value);

        assertEquals(expected, actual);
    }
}
