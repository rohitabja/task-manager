package com.cognizant.taskmanager.util;

import java.sql.Date;
import java.time.LocalDate;

public class TaskManagerUtil {

    public static LocalDate getDate(final Date date) {
        return date == null ? null : date.toLocalDate();
    }

    public static boolean getBoolean(final String value) {
        return  "Y".equalsIgnoreCase(value) ? true : false;
    }

    public static String getStrFromBoolean(final boolean value) {
        return value ? "Y" : "N";
    }
}
