package com.example.birthdaycountdown;

import android.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CountDown {

    private static final String datePattern = "MM/dd/yyyy";
    private LocalDate birthDate;

    public String getFormattedBirthDate() {
        return birthDate.format(DateTimeFormatter.ofPattern(datePattern));
    }

    public long getDaysUntilNextBirthday() {
        LocalDate currentDate = LocalDate.now();
        LocalDate nextBirthday = birthDate.withYear(currentDate.getYear());

        if (nextBirthday.isBefore(currentDate) || nextBirthday.isEqual(currentDate)) {
            nextBirthday = nextBirthday.plusYears(1);
        }

        Log.d("DATE_DEBUG", currentDate.toString());
        Log.d("DATE_DEBUG", nextBirthday.toString());

        return ChronoUnit.DAYS.between(currentDate, nextBirthday);
    }

    // Optional: JSON handling methods if needed for saving/restoring instance state
    public String getJSONStringFromThis() {
        return birthDate != null ? getFormattedBirthDate() : "";
    }

    public static CountDown fromJSONString(String jsonString) {
        CountDown countDown = new CountDown(jsonString);
        return countDown;
    }

    public CountDown(String dateInput) {
        setBirthDate(dateInput);
    }

    public final void setBirthDate(String dateInput) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDate localDate = LocalDate.parse(dateInput, formatter);
        this.birthDate = localDate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}
