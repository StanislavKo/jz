package com.hsd.jz.api.notification;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

public class SESUtils {

	public static void send(String message) {
		try {
			AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_WEST_2).build();
			SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses("drdrj@mail.ru"))
					.withMessage(new Message().withBody(new Body().withText(new Content().withCharset("UTF-8").withData(message)))
							.withSubject(new Content().withCharset("UTF-8").withData("JitZdorovo")))
					.withSource("visitmail1@mailinator.com");
			client.sendEmail(request);
			System.out.println("Email sent!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
