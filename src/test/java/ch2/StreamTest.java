package ch2;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class StreamTest {
    private final List<Dish> dishList = List.of(
            Dish.builder().name("salmon").type("FISH").calories(100).build(),
            Dish.builder().name("pork").type("MEAT").calories(200).build(),
            Dish.builder().name("beef").type("MEAT").calories(250).build(),
            Dish.builder().name("chicken").type("MEAT").calories(300).build(),
            Dish.builder().name("pizza").type("OTHER").calories(500).build(),
            Dish.builder().name("rice").type("OTHER").calories(200).build(),
            Dish.builder().name("french fries").type("OTHER").calories(500).build()
    );

    @Test
    @DisplayName("takeWhile, dropWhile :: 이미 정렬된 경우 유용 (java 9)")
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
    @DisplayName("flatMap :: 스트림 평면화")
    public void test2() {
        String[] arrayOfWords = { "Happykoo", "GoodLuck" };
        List<String> expected = List.of("H", "a", "p", "p", "y", "k", "o", "o", "G", "o", "o", "d", "L", "u", "c", "k");
        assertEquals(expected, Arrays.stream(arrayOfWords)
                .map(str -> str.split(""))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList()));
    }

    @Test
    @DisplayName("IntStream, mapToObj :: 피타고라스 예제")
    public void test3() {
        String expected = "3 4 5\n" +
                "5 12 13\n" +
                "6 8 10\n" +
                "7 24 25\n" +
                "8 15 17\n" +
                "9 12 15\n" +
                "9 40 41\n" +
                "10 24 26\n" +
                "11 60 61\n" +
                "12 16 20\n" +
                "12 35 37\n" +
                "13 84 85\n" +
                "14 48 50\n" +
                "15 20 25\n" +
                "15 36 39\n" +
                "16 30 34\n" +
                "16 63 65\n" +
                "18 24 30\n" +
                "18 80 82\n" +
                "20 21 29\n" +
                "20 48 52\n" +
                "20 99 101\n" +
                "21 28 35\n" +
                "21 72 75\n" +
                "24 32 40\n" +
                "24 45 51\n" +
                "24 70 74\n" +
                "25 60 65\n" +
                "27 36 45\n" +
                "28 45 53\n" +
                "28 96 100\n" +
                "30 40 50\n" +
                "30 72 78\n" +
                "32 60 68\n" +
                "33 44 55\n" +
                "33 56 65\n" +
                "35 84 91\n" +
                "36 48 60\n" +
                "36 77 85\n" +
                "39 52 65\n" +
                "39 80 89\n" +
                "40 42 58\n" +
                "40 75 85\n" +
                "40 96 104\n" +
                "42 56 70\n" +
                "45 60 75\n" +
                "48 55 73\n" +
                "48 64 80\n" +
                "48 90 102\n" +
                "51 68 85\n" +
                "54 72 90\n" +
                "56 90 106\n" +
                "57 76 95\n" +
                "60 63 87\n" +
                "60 80 100\n" +
                "60 91 109\n" +
                "63 84 105\n" +
                "65 72 97\n" +
                "66 88 110\n" +
                "69 92 115\n" +
                "72 96 120\n" +
                "75 100 125\n" +
                "80 84 116";
        String actual = IntStream.rangeClosed(1, 100)
                //IntStream 의 flatMap, map 연산은 IntStream 이기에 박싱을 해줘야 함
                .boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100)
                        .mapToObj(b -> new double[] { a, b, Math.sqrt(a * a + b * b) })
                        .filter(t -> t[2] % 1 == 0))
                .map(t -> Arrays.stream(t)
                        .mapToObj(n -> String.valueOf((int) n))
                        .collect(Collectors.joining(" ")))
                .collect(Collectors.joining("\n"));
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("generate, IntSupplier :: 피보나치 수열")
    public void test4() {
        IntSupplier fiboIntSupplier = new IntSupplier() {
            private int previous = 0;
            private int current = 1;
            @Override
            public int getAsInt() {
                int oldPrevious = previous;
                this.previous = current;
                this.current = oldPrevious + previous;

                return oldPrevious;
            }
        };
        String expected = "0 1 1 2 3";
        String actual = IntStream.generate(fiboIntSupplier)
                .limit(5)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Collectors.reducing")
    public void test5() {
       List<String> strList = List.of("1", "2", "3", "4", "5");
       assertEquals(15, strList.stream()
               .collect(Collectors.reducing(0,
                       Integer::parseInt,
                       Integer::sum)));
    }

    @Test
    @DisplayName("groupingBy :: filtering 예제")
    public void test6() {
        Map<String, List<Dish>> filteringMap = dishList.stream()
                .collect(Collectors.groupingBy(Dish::getType,
                        Collectors.filtering(dish -> dish.getCalories() >= 300, Collectors.toList())));

        log.info("filtering result >>> {}", filteringMap);
        assertEquals(3, filteringMap.keySet().size());
    }

    @Test
    @DisplayName("groupingBy :: mapping 예제")
    public void test7() {
        Map<String, List<String>> mappingMap = dishList.stream()
                .collect(Collectors.groupingBy(Dish::getType,
                        Collectors.mapping(Dish::getName, Collectors.toList())));

        log.info("mapping result >>> {}", mappingMap);
        assertEquals("salmon", mappingMap.get("FISH").get(0));
    }

    @Test
    @DisplayName("groupingBy :: n 수준 중첩 예제")
    public void test8() {
        Map<String, Map<CaloriesType, List<String>>> nGroupingMap = dishList.stream()
                .collect(Collectors.groupingBy(Dish::getType,
                        Collectors.groupingBy(dish -> {
                            if (dish.getCalories() <= 200) {
                                return CaloriesType.DIET;
                            } else if (dish.getCalories() <= 300) {
                                return CaloriesType.NORMAL;
                            } else {
                                return CaloriesType.FAT;
                            }
                        },
                        Collectors.mapping(Dish::getName, Collectors.toList()))));

        log.info("mapping result >>> {}", nGroupingMap);
        assertEquals("salmon", nGroupingMap.get("FISH").get(CaloriesType.DIET).get(0));
    }

    @Test
    @DisplayName("groupingBy :: collectingAndThen 예제")
    public void test9() {
        Map<String, Dish> maxMap = dishList.stream()
                .collect(Collectors.groupingBy(Dish::getType,
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)),
                        Optional::get)));
        log.info("maxMap result >>> {}", maxMap);
        assertEquals("chicken", maxMap.get("MEAT").getName());
    }

    @Test
    @DisplayName("partitioningBy :: 예제")
    public void test10() {
        Map<Boolean, List<Dish>> partitioningByMap = dishList.stream()
                .collect(Collectors.partitioningBy(dish -> dish.getType().equals("MEAT")));
        assertEquals(3, partitioningByMap.get(true).size());
    }

    @Test
    @DisplayName("custom Collector :: 예제")
    public void test11() {
        List<Dish> meatList = dishList.stream()
                        .filter(dish -> dish.getType().equals("MEAT"))
                        .collect(new ToListCollector<>());
        assertEquals(3, meatList.size());
    }
}
