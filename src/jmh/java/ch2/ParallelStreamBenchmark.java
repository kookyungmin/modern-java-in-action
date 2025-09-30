package ch2;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;

//JMH 는 기본적으로 5(코드 최적화) + 5(최종 결과 계산) = 10회 프로그램을 반복 실행
//gradle jwh
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(value = 2, jvmArgs = {"-Xms8G", "-Xmx8G"})
public class ParallelStreamBenchmark {
    private static final long N = 10_000_000L;
    private long[] numbers;

    @Setup(Level.Iteration)
    public void setup() {
        this.numbers = LongStream.rangeClosed(1, N).toArray();
    }

    @Benchmark
    public long sequentialSum() {
        return Stream.iterate(1L, i -> i + 1).limit(N)
                .reduce(0L, Long::sum);
    }

    @Benchmark
    public long iterativeSum() {
        long result = 0;
        for(long i = 1; i <= N; i++) {
            result += i;
        }
        return result;
    }

    @Benchmark
    public long parallelSum() {
        //iterate 자체는 순차적이라 청크로 분할하기 어려움
        return Stream.iterate(1L, i -> i + 1)
                .parallel()
                .limit(N)
                .reduce(0L, Long::sum);
    }

    @Benchmark
    public long rangeSum() {
        return LongStream.rangeClosed(1, N)
                .reduce(0L, Long::sum);
    }

    @Benchmark
    public long rangeParallelSum() {
        return LongStream.rangeClosed(1, N)
                .parallel()
                .reduce(0L, Long::sum);
    }

    @Benchmark
    public long forkJoinSum() {
        ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
        //일반적으로 ForkJoinPool 은 싱글턴으로 생성
        return ForkJoinPool.commonPool().invoke(task);
    }

    //매번 밴치마크를 실행한 다음에는 가비지 컬렉터 동작 시도
    @TearDown(Level.Invocation)
    public void tearDown() {
        System.gc();
    }
}
