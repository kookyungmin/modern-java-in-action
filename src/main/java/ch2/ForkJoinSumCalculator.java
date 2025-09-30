package ch2;

import java.util.concurrent.RecursiveTask;

public class ForkJoinSumCalculator extends RecursiveTask<Long> {
    //더할 숫자 배열
    private final long[] numbers;
    private final int start;
    private final int end;
    //이 값 이하의 서브태스크는 더 이상 분할할 수 없다.
    public static final long THRESHOLD = 10_000;

    public ForkJoinSumCalculator(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    public ForkJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            //서브 태스크로 더 이상 분할할 수 없으면, 순차적으로 계산
            return computeSequentially();
        }
        ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length / 2);
        //배열 첫번째 절반을 더하도록 서브태스크 생성하여 다른 스레드로 비동기 실행
        leftTask.fork();
        ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length / 2, end);
        //배열 두번째 절반을 더하도록 서브태스크 생성하여 동기 실행
        Long rightResult = rightTask.compute();
        //첫번째 서브태스크의 결과를 읽거나 아직 결과가 없으면 기다림(blocking)
        Long leftResult = leftTask.join();
        return leftResult + rightResult;
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers[i];
        }
        return sum;
    }
}
