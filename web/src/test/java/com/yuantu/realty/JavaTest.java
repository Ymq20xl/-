package com.yuantu.realty;

import com.yuantu.dev.sys.utils.DateUtils;
import org.junit.Test;

import java.util.List;

public class JavaTest {
    @Test
    public void testDate() {
        try {
            List<String> dateList = DateUtils.getDateBetween("2018-12-03", "2019-05-01");
            for (String dateS : dateList) {
                System.out.println(dateS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
