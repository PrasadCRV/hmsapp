package com.hmsapp.service;


import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SMSService {

    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;

    public String sendSMS(String to, String messageBody) {
        Message message = Message.creator(
                new PhoneNumber(to),      // To number
                new PhoneNumber(twilioPhoneNumber), // From Twilio number
                messageBody               // SMS body
        ).create();

        return message.getSid(); // Return the unique SID of the message
    }
}
