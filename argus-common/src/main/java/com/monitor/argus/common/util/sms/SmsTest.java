package com.monitor.argus.common.util.sms;

import com.monitor.argus.common.util.UuidUtil;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SmsTest {

    public static void main(String args[]) throws Exception {

        // System.out.println(SmsSendUtil.sendSingleSms(UuidUtil.getUUID(),
        // "13810883645", "1234", "020303019"));
        System.out.println(SmsSendUtil.sendSingleSms_2(UuidUtil.getUUID(), "13522253816", "020303019"));
        // testOrderMsgHttpclient();
    }

    /**
     * 
     * restful方式单条发送测试.
     * 
     * @author yangdapeng
     * @version 2013-5-17
     * @----------------------------------------------------------------------------------------
     * @updated 修改描述.
     * @updated by yangdapeng
     * @updated at 2013-5-17
     */
    public static void testOrderMsgHttpclient() {
        try {
            MessageReqJaxb req = new MessageReqJaxb();
            DetailsJaxb detail = new DetailsJaxb();
            req.setBatchId("10001");
            req.setChannelCode("ydd_system_001");
            req.setOrgNo("06");
            req.setTypeNo("020303019");
            req.setVersion("1.0");
            detail.setMobile("13810883645");
            detail.setPriority("5");
            req.setDetailsJaxb(new DetailsJaxb[] { detail });
            ObjectMapper objectMapper = new ObjectMapper();
            String reqJson = objectMapper.writeValueAsString(req);
            String systime = "2015060514020000";
            String signature = SecurityUtil.sha1Digest("passport2013" + systime, reqJson.getBytes());
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("messageReqJson", reqJson));
            formparams.add(new BasicNameValuePair("systime", systime));
            formparams.add(new BasicNameValuePair("signature", signature));
            UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams, "UTF-8");

            // HttpPost post1 = new
            // HttpPost("http://10.106.1.37:8080/smsserver/restful/orderMsg/");
            HttpPost post1 = new HttpPost("http://10.141.4.51:8080/smsserver/restful/orderMsg/");
            post1.setEntity(entity1);
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(post1);
            InputStream instream = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
            System.out.println(reader.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}