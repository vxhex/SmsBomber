/**
 * B-52 SMS Mail Bomber
 * @author andy@eerieapplications.com
 */

package com.eerieapplications.android.b52smsmailer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void clickHandler(View view) {
    	switch (view.getId()) {
			case R.id.sendButton: 
				Toast.makeText(this, "Bombing run started!", Toast.LENGTH_SHORT).show();
				
				EditText text = (EditText) findViewById(R.id.phoneNumber);
				String phoneNumber = text.getText().toString();
				text = (EditText) findViewById(R.id.message);
				String textMessage = text.getText().toString();
				text = (EditText) findViewById(R.id.numberToSend);
				String numberToSend = text.getText().toString();
				
				Intent sendIntent = new Intent(MainActivity.this, MessageActivity.class);
				sendIntent.putExtra("number", phoneNumber);
				sendIntent.putExtra("message", textMessage);
				sendIntent.putExtra("loop", numberToSend);
				startActivityForResult(sendIntent, 2);
				break;
				
			case R.id.contactsButton:
				Intent intent = new Intent(MainActivity.this, ContactActivity.class);
				startActivityForResult(intent, 1);
				break;
    	}
    	
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_OK) {
    		switch (requestCode) {
    			//Contact activity
    			case 1:
    				if (data.hasExtra("numbers")) {
    					EditText text = (EditText) findViewById(R.id.phoneNumber);
    					final String pNumb = text.getText().toString() + data.getExtras().getString("numbers");
    					text.setText(pNumb);
        			}
    				break;
    			
    			//Message activity
    			case 2:
    				Toast.makeText(this, "Bombing complete!", Toast.LENGTH_SHORT).show();
    				break;
    		
    			//Unknown activity code
    			default:
    				Toast.makeText(this, "Unknown response code!", Toast.LENGTH_SHORT).show();
    		}
    	}
    }
    
}