package ch2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModernJavaTest {
    @Test
    @DisplayName("takeWhile, dropWhile : 이미 정렬된 경우 유용 (java 9)")
    public void test1() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);

        List<Integer> takeWhileExpected = List.of(1, 2);
        List<Integer> dropWhileExpected = List.of(3, 4, 5);

        assertEquals(takeWhileExpected, list.stream()
                .takeWhile(i -> i < 3)
                .collect(Collectors.toList()));

        assertEquals(dropWhileExpected, list.stream()
                .dropWhile(i -> i < 3)
                .collect(Collectors.toList()));
    }

    @Test
    @DisplayName("")
    public void test2() {
        
    }

}
