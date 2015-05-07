package com.eerieapplications.android.b52smsmailer;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

public class ContactActivity extends ListActivity {
	private ArrayList<String> numbers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			//String s = extras.getString("value");
			//TextView view = (TextView) findViewById(R.id.detailsText);
			//view.setText(s);
		}
		*/
		//init numbers
		numbers = new ArrayList<String>();
		ArrayList<String> contactsList = getContacts();
		String[] contacts = contactsList.toArray(new String[contactsList.size()]);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_checked, contacts);
		setListAdapter(adapter);
	}
	
	@Override
	public void finish() {
		Intent data = new Intent();
		String returnNumbers = "";
		for (String s : numbers) {
			returnNumbers += s + "; ";
		}
		data.putExtra("numbers", returnNumbers);
		setResult(RESULT_OK, data);
		super.finish();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = getListAdapter().getItem(position).toString();
		String[] splitItem = item.split(":");
		String number = splitItem[1].trim();
		if (numbers.contains(number)) {
			numbers.remove(number);
		} else {
			numbers.add(number);
		}
		//could also check out l.setItemChecked(position, value)
		CheckedTextView textview = (CheckedTextView)v;
	    textview.setChecked(!textview.isChecked());

	}
	
	private ArrayList<String> getContacts() {
		ArrayList<String> contacts = new ArrayList<String>();
		
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
        	while (cursor.moveToNext()) {
        		String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        		String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        		if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
        			Cursor pCursor = getContentResolver().query(
        				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, 
        		 		ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
        		 		new String[]{id}, null);
        		 	while (pCursor.moveToNext()) {
        		 		String number = pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        		 		name = name.replaceAll(":", "-");
        		 		number = number.replaceAll("-", "").replaceAll(" ", "");
        		 		contacts.add(name + " : " + number);
        		 	} 
        		 	pCursor.close();
        		}
            }
        }
		cursor.close();
		return contacts;
	}
}
