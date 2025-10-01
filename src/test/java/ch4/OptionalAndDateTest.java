package ch4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Properties;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptionalAndDateTest {
    @Test
    @DisplayName("Optional map, flatMap, filter 는 :: 값이 있을 때만 호출")
    public void test1() {
       Optional<String> str = Optional.of("Happykoo");

       assertEquals("Happykoo", str.filter(s -> s.equals("Happykoo")).orElse("Default"));
       assertEquals("Default", str.filter(s -> s.equals("Default")).orElse("Default"));
    }

    @Test
    @DisplayName("Optional readDuration Test")
    public void test2() {
        Properties props = new Properties();
        props.setProperty("a", "5");
        props.setProperty("b", "true");
        props.setProperty("c", "-3");

        assertEquals(5, readDuration(props, "a"));
        assertEquals(0, readDuration(props, "b"));
        assertEquals(0, readDuration(props, "c"));
        assertEquals(0, readDuration(props, "d"));
    }

    private int readDuration(Properties props, String name) {
        return Optional.ofNullable((String) props.get(name))
                .flatMap(this::stringToInt)
                .filter(i -> i > 0)
                .orElse(0);
    }

    private Optional<Integer> stringToInt(String str) {
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
