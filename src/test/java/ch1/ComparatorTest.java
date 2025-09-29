package ch1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComparatorTest {
    @Test
    @DisplayName("map function 예제")
    public void test1() {
        List<String> list = List.of("1", "2", "3") ;
        List<Integer> expected = List.of(1, 2, 3);

        assertEquals(expected, map(list, (s) -> Integer.parseInt(s)));
    }

    @Test
    @DisplayName("Comparator 예제")
    public void test2() {
        List<Apple> apples = new ArrayList<>(List.of(
                new Apple(30, 20),
                new Apple(40, 20),
                new Apple(30, 10)
        ));

        apples.sort(Comparator.comparing(Apple::getWeight)
                .reversed()
                .thenComparing(Apple::getPrice));

        assertEquals(apples.get(0).getWeight(), 40);
        assertEquals(apples.get(1).getPrice(), 10);
        assertEquals(apples.get(2).getPrice(), 20);
    }

    public <T, R> List<R> map(List<T> list, Function<T, R> f) {
        List<R> result = new ArrayList<>();
        for (T t : list) {
            result.add(f.apply(t));
        }

        return result;
    }
}
