package ru.job4j.dreamjob.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

public class DateUtil {
    private static final Logger LOGGER = Logger.getLogger(DateUtil.class.getName());
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String createDate() {
        Calendar calendar = Calendar.getInstance();
        String date = dateFormat.format(calendar.getTime());
        LOGGER.info("createDate : " + date);
        return date;
    }
}
