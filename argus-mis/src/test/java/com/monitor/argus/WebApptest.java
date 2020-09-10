package com.monitor.argus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-webtest.xml")
public class WebApptest {

	@Test
	public void testA() throws Exception{
		MockHttpServletRequest mck=new MockHttpServletRequest();
				mck.addParameter("value", "value is not empty");
				mck.setContent("content".getBytes());
				System.out.println(Thread.currentThread().getContextClassLoader().getResource("spring-webtest.xml"));
//		controller.dealWithPushRequest(mck, new MockHttpServletResponse());
	}
}
