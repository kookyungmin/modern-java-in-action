package ch2;

import java.util.Spliterator;
import java.util.function.Consumer;

public class WordCounterSpliterator implements Spliterator<Character> {
    private final String str;
    private int currentChar;

    public WordCounterSpliterator(String str) {
        this.str = str;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Character> action) {
        //Spliterator의 요소를 하나씩 순차적으로 소비하면서 탐색해야하는 요소가 남아있으면 참을 반환
        action.accept(str.charAt(currentChar++)); return currentChar < str.length();
    }

    @Override
    public Spliterator<Character> trySplit() {
        //Spliterator 의 일부 요소(자신이 반환한 요소) 를 분할해서 두번째 Spliterator를 생성함
        int currentSize = str.length() - currentChar;
        if (currentSize < 10) {
            //순차적으로 처리할 만큼 충분히 작아졌으면 null 반환
            return null;
        }

        //파싱할 문자의 중간을 분할 위치로 정한다.
        for (int splitPos = currentSize / 2 + currentChar; splitPos < str.length(); splitPos++) {
            if (Character.isWhitespace(str.charAt(splitPos))) {
                Spliterator<Character> spliterator = new WordCounterSpliterator(str.substring(currentChar, splitPos));
                //시작 위치를 spliterator 의 분할 위치로 지정(spliterator는 또 해당 문자열에서 분할 됨)
                currentChar = splitPos;
                return spliterator;
            }
        }
        return null;
    }

    @Override
    public long estimateSize() {
        //탐색해야 할 요소 수
        return str.length() - currentChar;
    }

    @Override
    public int characteristics() {
        //ORDERED: 문자열의 문자 등장 순서가 유의미함
        //SIZED: estimatedSize 메서드의 반환값이 정확함
        //SUBSIZED: trySplit으로 생성된 Spliterator도 정확한 크기를 가짐
        //NONNULL: 문자열에는 null 문자가 존재하지 않음
        //IMMUTABLE: 문자열 자체가 불변클래스이므로 문자열을 파싱하면서 속성이 추가되지 않음
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }
}
