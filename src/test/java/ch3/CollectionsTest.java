package ch3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionsTest {
    @Test
    @DisplayName("computeIfAbsent 테스트")
    public void test1() {
        Map<String, List<String>> map = new HashMap<>();
        map.computeIfAbsent("Hello", (key) -> new ArrayList<>()).add("HappyKoo");
        assertEquals("HappyKoo", map.get("Hello").get(0));
    }

    @Test
    @DisplayName("merge 테스트")
    public void test2() {
        Map<String, Long> countMap = new HashMap<>();
        //key 에 값이 있으면 BiFunction 호출
        countMap.merge("Happykoo", 1L, (k, v) -> v + 1);
        countMap.merge("Happykoo", 1L, (k, v) -> v + 1);

        assertEquals(2, countMap.get("Happykoo"));
    }
}
