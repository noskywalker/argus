/**
 *
 */
package com.monitor.argus.service;

import com.monitor.argus.dao.system.ISystemAuthDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * All rights Reserved, Designed By alex zhang
 *
 * @version V1.0
 * @Title: LogTest.java
 * @Package com.monitor.argus.service
 * @Description: TODO
 * @author: alex zhang
 * @date: 2016年7月6日 下午1:07:29
 */
public class LogTest extends AppTest {
    public static String mockJSON = "{\n"
            + "    \"message\": \"2016-07-05 16:27:40.403 | 10.130.82.79 | 115.193.104.26, 115.193.104.26 | android | 4.4.2 | HM NOTE 1TD | 2.9.0 | 0 | 28e31f763748 | 25.041308=102.739233 | /init/getBannerList | UNiVCSknqHmqTpsNYhfw0zGyOs8D4tkjtNHwMQfxIag2htj6lom0WAGwJpYl KAfoFVnIKUP+7opAOYS0rS9w3I/CDfoLKxyUlrE6PHwefSgnuL57sY55g8/N qfoEFXWiqVU5wVrF3aOzLZjdHb6jGmD557LzYNYEoQazOAD7WcISoT1J+XBm SJxLk1oi5+aKT8qHQDo+Xfg2uJF4XoktxXqzWIcu/gFiU2GVwlpyJsYXoV/3 svzOSpW+MQ/au4N502PMJXHAoEq2T6uCe6T1OyTgpmglZiUo/nErpVELg9B6 Zo8GNmbt/9yrnmMK43TFJABTtrxHNmeFTPYy9Ans04g5N6QYMCezvmKzT2ul RslsWFv/FxA6LDPoYi3YoYeKpmOkGXJ8ugo7ryvPCiA/M+Fr5Kb8MQjRotpW eSi0mBbRmpdqhMeO870dtVs20ZUclSSazfV+Tfkw5hVHXI5//kKpvfmlThAj goTxrvpLtlFvMYiqR/rHdVcbSco2+xkc | resCode:0000,resMsg:请求成功 \",\n"
            + "    \"@version\": \"1\",\n" + "    \"@timestamp\": \"2016-07-05T08:27:40.876Z\",\n"
            + "    \"count\": 1,\n" + "    \"log_ip\": \"10.130.82.79\",\n" + "    \"log_project\": \"laas\",\n"
            + "    \"log_topic\": \"mobile\",\n" + "    \"offset\": 162789885,\n"
            + "    \"source\": \"/opttestlogs/tomcat_8080_laas/apiinfo/laas-apiinfo.log\",\n"
            + "    \"type\": \"mobile\",\n" + "    \"host\": \"ovz-loan-laas-01testhost.online.com\",\n"
            + "    \"log_filename\": \"laas-apiinfo.log\",\n" + "    \"log_timestamp\": \"2016-07-05 16:27:40.403\",\n"
            + "    \"log_date\": \"2016-07-05\",\n" + "    \"kafka\": {\n" + "        \"msg_size\": 1354,\n"
            + "        \"topic\": \"mobile\",\n" + "        \"consumer_group\": \"mobile\",\n"
            + "        \"partition\": 2,\n" + "        \"key\": null\n" + "    },\n" + "    \"tags\": [\n"
            + "        \"_grokparsefailure\"\n" + "    ]\n" + "}";
    //	@Autowired
//	LogMessageListener logMessageListenerImpl;
    @Autowired
    ISystemAuthDao systemAuthDao;

    @Test
    public void testLogParser() {
        // ApplicationContext app = new
        // ClassPathXmlApplicationContext("/spring/spring-dataSource.xml",
        // "/spring/spring-service.xml", "/spring/spring-redis.xml");
        // LogMessageListenerImpl l = (LogMessageListenerImpl)
        // app.getBean("logMessageListenerImpl");
        // l.onMessage(LogTest.mockJSON);
//		logMessageListenerImpl.onMessage(mockJSON);
    }
}
