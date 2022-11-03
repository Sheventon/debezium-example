package ru.itis.suhd.util;

public class WeekDayMapper {

    public static String fromIntToStringWeekDay(int day) {
        return switch (day) {
            case 1 -> "Sunday";
            case 2 -> "Monday";
            case 3 -> "Tuesday";
            case 4 -> "Wednesday";
            case 5 -> "Thursday";
            case 6 -> "Friday";
            case 7 -> "Saturday";
            default -> null;
        };
    }
}
