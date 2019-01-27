package test;

import email.SendEmailHeadle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanbin on 2019/1/27.
 */
public class TestMail {
    public static void main(String[] args) {
        SendEmailHeadle send = new SendEmailHeadle();
        Map<String,Object> param = new HashMap<>();
        param.put("userName","大神");
        try {
            send.sendEmailHtml("测试","",null,param,"test.ftl");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
