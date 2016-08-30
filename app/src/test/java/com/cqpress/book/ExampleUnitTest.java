package com.cqpress.book;

import com.cqpress.book.bean.BaseRequestVo;
import com.cqpress.book.uhf.DataTransfer;
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

        String uuid = "3400978730705381a0500009c000";
        System.out.println(uuid.length());
        String substring = uuid.substring(4, uuid.length());
        System.out.println(substring);

        //15位是校验位
        String str = "97 87 30 70 53 81 a0 50 00 09 c0 00";
        System.out.println(DataTransfer.getBytesByHexString(str).length);

        System.out.println(1 ^ 1 ^ 1 ^ 1 ^ 2 ^ 2 ^ 2 ^ 2 ^ 3 ^ 3 ^ 3 ^ 3 ^ 1 ^ 0 ^ 0 ^ 0 ^ 0 ^ 0 ^ 3 ^ 11 ^ 0 ^ 0 ^ 0);
        System.out.println(1 ^ 1 ^ 1 ^ 1 ^ 2 ^ 2 ^ 2 ^ 2 ^ 3 ^ 3 ^ 3 ^ 3 ^ 1 ^ 0 ^ 0 ^ 0 ^ 0 ^ 0 ^ 1 ^ 6 ^ 0 ^ 4 ^ 15);

        //11112222333310 9 00003B000 校验码为9
        //1111222233331000003B000
        String uiiStr = "9787111495737070000010AF";
        if (uiiStr.length() > 24) {//判断扫描24位和28位
            String strUii = uiiStr.substring(4, uiiStr.length());
            if (strUii.indexOf("f") != -1) {
                System.out.println(Integer.parseInt(strUii.substring(strUii.length() - 2, strUii.length() - 1),16));
            }
        } else {
            System.out.println("===="+ Integer.parseInt(uiiStr.substring(uiiStr.length() - 2, uiiStr.length() - 1),16));
        }

        String startStr = uiiStr.substring(0, 14);
        String verifyCode = uiiStr.substring(14, 15);
        String endStr = uiiStr.substring(15, uiiStr.length());
        int lost = accumulation((startStr + endStr).toCharArray());
        System.out.println(lost == Integer.parseInt(verifyCode));

//        System.out.println(Integer.parseInt("10", 16));
//        System.out.println(Integer.parseInt("A", 16));
//        System.out.println(Integer.parseInt("B", 16));
//        System.out.println(Integer.parseInt("C", 16));
//        System.out.println(Integer.parseInt("D", 16));
//        System.out.println(Integer.parseInt("E", 16));
//        System.out.println(Integer.parseInt("F", 16));
    }

    public static int accumulation(char[] chars) {
        int result = 0;
        for (int i = 0; i < chars.length; i++) {
            result ^= Integer.parseInt("" + chars[i], 16);
        }
        return result;
    }
}