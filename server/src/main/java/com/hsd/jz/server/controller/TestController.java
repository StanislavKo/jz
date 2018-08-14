package com.hsd.jz.server.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hsd.jz.server.controller.pojo.Test;
import com.hsd.jz.server.controller.pojo.TestList;
import com.hsd.jz.server.security.TokenAuthenticationService;

@RestController
public class TestController {

	private static final Logger logger = LoggerFactory.getLogger(TestController.class);

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() {
		logger.info("test");
		
		logger.info("jwt=" + TokenAuthenticationService.getAuthenticationJwt("фыва{}\"", "ф{}\""));
		
		return "test01";
	}

//	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/testObj/{query}", method = RequestMethod.GET)
	public TestList testObj(@PathVariable("query") String query) {
		try {
			query = URLDecoder.decode(query, "UTF-8");
		} catch (UnsupportedEncodingException ignored) {
		}
		logger.info("testObj(), query={}", query);
		
		return new TestList(Arrays.asList(
				new Test[] { new Test(200, "title1", "2018-04-11", "textValue"), new Test(201, "title2 title2 title2 title2", "2018-04-12", "textValue2") }));
	}

}