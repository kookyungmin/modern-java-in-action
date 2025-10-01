package ch4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JavaTimeTest {
    //Date, Calendar은 가변 객체이고, 시간대 정보를 가지고 있지 않고, 국가 정보도 미포함 /  게다가 DateFormat 은 쓰레드에 안정하지 않다.
    //java 8 부터 새로운 날짜, 시간 API 가 등장
    @Test
    @DisplayName("LocalDate :: 날짜, 불변객체 테스트")
    public void test1() {
        LocalDate date = LocalDate.of(1993, 9, 20);

        assertEquals(1993, date.getYear());
        assertEquals(9, date.getMonthValue());
        assertEquals(20, date.getDayOfMonth());
        assertEquals("SEPTEMBER", date.getMonth().name());
        assertEquals("MONDAY", date.getDayOfWeek().name());
        //윤년 여부
        assertEquals(false, date.isLeapYear());
    }

    @Test
    @DisplayName("LocalTime :: 시간, 불변객체 테스트")
    public void test2() {
        LocalTime time = LocalTime.of(13, 45, 20);

        assertEquals(13, time.getHour());
        assertEquals(45, time.getMinute());
        assertEquals(20, time.getSecond());
    }

    @Test
    @DisplayName("LocalDateTime :: 날짜/시간, 불변객체 테스트")
    public void test3() {
        LocalDateTime localDateTime1 = LocalDateTime.of(1993, 9, 20, 1, 54, 0);
        LocalDate localDate = LocalDate.of(1993, 9, 20);
        LocalDateTime localDateTime2 = localDate.atTime(1, 54);

        assertEquals(1993, localDateTime1.getYear());
        assertEquals(1, localDateTime1.getHour());
        assertEquals(localDateTime1.getMinute(), localDateTime2.getMinute());
    }

    @Test
    @DisplayName("Instant :: 기계의 날짜와 시간 테스트")
    public void test4() {
        //(Unix epoch time -> 1970년 1월 1일 0시 0분 0초 UTC)를 기준으로 특정 지점까지의 시간을 초로 표시
        Instant expected = Instant.ofEpochSecond(3);
        assertEquals(expected, Instant.ofEpochSecond(3, 0));
        assertEquals(expected, Instant.ofEpochSecond(2, 1_000_000_000));
        assertEquals(expected, Instant.ofEpochSecond(4, -1_000_000_000));
    }

    @Test
    @DisplayName("Duration, Period :: 날짜, 시간 차이")
    public void test5() {
        LocalDateTime localDateTime1 = LocalDateTime.of(1993, 9, 20, 1, 54, 0);
        LocalDateTime localDateTime2 = LocalDateTime.of(2025, 10, 1, 18, 29, 5);

        Duration duration = Duration.between(localDateTime1, localDateTime2);
        assertEquals(280792, duration.toHours());


        LocalDate localDate1 = LocalDate.of(1993, 9, 20);
        LocalDate localDate2 = LocalDate.of(2025, 10, 1);
        Period period = Period.between(localDate1, localDate2);
        assertEquals(32, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(11, period.getDays());
    }

    @Test
    @DisplayName("TemporalAdjusters :: 날짜 조정")
    public void test6() {
        LocalDate date1 = LocalDate.of(2025, 10, 1);
        //date1 날짜 다음 주 일요일
        LocalDate date2 = date1.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        assertEquals(2025, date2.getYear());
        assertEquals(10, date2.getMonthValue());
        assertEquals(5, date2.getDayOfMonth());

        //date2 포함한 달의 마지막 날짜
        LocalDate date3 = date2.with(TemporalAdjusters.lastDayOfMonth());
        assertEquals(2025, date3.getYear());
        assertEquals(10, date3.getMonthValue());
        assertEquals(31, date3.getDayOfMonth());
    }

    @Test
    @DisplayName("DateTimeFormatter :: 날짜 포맷팅")
    public void test7() {
        LocalDate date = LocalDate.parse("1993-09-20", DateTimeFormatter.ISO_LOCAL_DATE);

        assertEquals("1993-09-20", date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        assertEquals("19930920", date.format(DateTimeFormatter.BASIC_ISO_DATE));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        assertEquals("1993년 09월 20일", date.format(formatter));
    }

    @Test
    @DisplayName("ZoneDateTime :: TomeZone 적용")
    public void test8() {
        LocalDateTime date = LocalDateTime.parse("2025-10-01T18:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        ZoneId romeZone = ZoneId.of("Europe/Rome");
        ZonedDateTime zdt1 = date.atZone(romeZone);

        assertEquals("2025-10-01T18:00:00", zdt1.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertEquals("+02:00", zdt1.getOffset().toString());

        ZoneId seoulZone = ZoneId.of("Asia/Seoul");
        ZonedDateTime zdt2 = zdt1.withZoneSameInstant(seoulZone);
        assertEquals("2025-10-02T01:00:00", zdt2.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertEquals("+09:00", zdt2.getOffset().toString());
    }
}
