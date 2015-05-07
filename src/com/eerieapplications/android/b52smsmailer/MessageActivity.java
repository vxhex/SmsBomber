package com.eerieapplications.android.b52smsmailer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;

public class MessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		String phoneNumber = extras.getString("number");
		String textMessage = extras.getString("message");
		String numberToSend = extras.getString("loop");
		
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);
		SmsManager manager = SmsManager.getDefault();
		List<String> phoneNumbers = Arrays.asList(phoneNumber.split(";")); 
		int loopMax = Integer.parseInt(numberToSend);
		for (int sendLoop = 0; sendLoop < loopMax; sendLoop++) {
		    for (String number : phoneNumbers) {
		    	number = number.trim();
		    	if ((number != null)&&(!"".equalsIgnoreCase(number))) { 
			    	if (textMessage.length() > 160) {
			    		ArrayList<String> textMessages = manager.divideMessage(textMessage);
	                    manager.sendMultipartTextMessage(number, null, textMessages, null, null);
			    	} else {
			    		manager.sendTextMessage(number, null, textMessage, sentPI, deliveredPI);
			    	}
		    	}
		    }
		}
		
		finish();
	}
	
	@Override
	public void finish() {
		Intent finishedIntent = new Intent();
		setResult(RESULT_OK, finishedIntent);
		super.finish();
	}
}
