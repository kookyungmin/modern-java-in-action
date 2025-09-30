package ch2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Spliterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordCounterTest {
    @Test
    @DisplayName("단어 수 세기 :: 순차인 경우")
    public void test1() {
        String str = "Nel mezzo del cammin di nostra vita ";
        assertEquals(7, countWordsInteractively(str));
    }

    @Test
    @DisplayName("단어 수 세기 :: WordCounter 클래스 이용한 경우")
    public void test2() {
        String str = "Nel mezzo del cammin di nostra vita ";
        Stream<Character> stream = IntStream.range(0, str.length())
                        .mapToObj(str::charAt);
        assertEquals(7, countWords(stream));
    }

    @Test
    @DisplayName("단어 수 세기 :: WordCounter 클래스, Spliterator 이용한 경우 (병렬)")
    public void test3() {
        String str = "Nel mezzo del cammin di nostra vita ";
        Spliterator<Character> spliterator = new WordCounterSpliterator(str);
        Stream<Character> stream = StreamSupport.stream(spliterator, true);
        assertEquals(7, countWords(stream));
    }


    private int countWordsInteractively(String str) {
       int counter = 0;
       boolean lastSpace = true;

       for (char c : str.toCharArray()) {
           if (Character.isWhitespace(c)) {
               lastSpace = true;
           } else {
               if (lastSpace) counter++;
               lastSpace = false;
           }
       }

       return counter;
    }

    private int countWords(Stream<Character> stream) {
        WordCounter wordCounter = stream.reduce(new WordCounter(0, true),
                WordCounter::accumulate,
                WordCounter::combine);
        return wordCounter.getCounter();
    }
}
