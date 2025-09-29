package ch2;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

// 1. A accumulator = collector.supplier().get();
// 2. collector.accumulator().accept(accumulator, item)
// 3. R result = collector.finisher().apply(accumulator);
// 4. 병렬일 때는, R result = collector.combiner.apply(r1, r2);
public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {
    @Override
    public Supplier<List<T>> supplier() {
        //새로운 결과 컨테이너
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<T>, T> accumulator() {
        //결과 데이터에 요소 추가하기
        return List::add;
    }

    @Override
    public Function<List<T>, List<T>> finisher() {
        //최종 값을 결과 컨테이너에 적용
        return Function.identity();
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
        //스트림의 서로 다른 서브파트를 병렬로 처리 할 때, 누적자가 이 결과를 어떻게 처리할 지
        return (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        //병렬로 리듀싱할 때, 힌트 제공
        // UNORDERED : 방문 순서나 누적 순서에 영향을 받지 않음
        // CONCURRENT: 멀티 쓰레드에서 accumulator 함수를 동시에 호출 가능, UNORDERED 를 함께 설정하지 않으면 정렬되어 있지 않은 상황(예: 집합)에서만 병렬 리듀싱 사용 가능
        // IDENTITY_FINISH : 누적 결과가 최종 결과 형식임
        return Collections.unmodifiableSet(EnumSet.of(
                Characteristics.UNORDERED,
                Characteristics.CONCURRENT,
                Characteristics.IDENTITY_FINISH
        ));
    }
}
