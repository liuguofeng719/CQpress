package com.cqpress.book;

import com.cqpress.book.bean.BaseRequestVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        // 34 00 97 87 30 70 53 81 a0 50 00 09 c0 00
        assertEquals(4, 2 + 2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timespan = sdf.parse(sdf.format(new Date())).getTime() / 1000;
        System.out.println((sdf.parse(sdf.format(new Date()))).getTime() / 1000);

        Long timestamp = timespan * 1000;

        String date1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));

        System.out.println(date1);

        BaseRequestVo baseRequestVo = new BaseRequestVo();
        baseRequestVo.setReqMethod("AppScanResultUpLoad");
        Gson gson = new GsonBuilder().create();
        System.out.println(baseRequestVo.sign());
        System.out.println(gson.toJson(baseRequestVo));
        System.out.println(baseRequestVo.getTimestamp());
    }
}