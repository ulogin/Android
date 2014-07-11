package com.example.uloginsdkdemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ulogin.sdk.UloginAuthActivity;



public class MainActivity extends ActionBarActivity {

	
/*
 * 	Label for uLogin authorization intent. Use any you want 
 */
	public final int REQUEST_ULOGIN = 1;

	
/*
 * 	Set main activity layout
 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	
/*
 * 	Button onClick - here we start uLogin authorization activity
 */
	public void button_click(View v) {
		
/*
 * 		Creating new intent
 */
		Intent intent = new Intent(getApplicationContext(),
				UloginAuthActivity.class);
		
		/*
		 *  Set the list of shown providers.
		 *  String array R.array.ulogin_providers is the list of all known providers.
		 *  You can also providers list manually:
		 *  String[] providers = { "vkontakte", "google", "mailru","odnoklassniki"};
		 */
		String[] providers = getResources().getStringArray(R.array.ulogin_providers);
		
		
		/*
		 * Set the required and optional field lists.
		 * String array R.array.ulogin_fields is the list of all available fields.
		 * Do NOT set mandatory all of them! 
		 */
		String[] mandatory_fields = new String[] {"first_name", "last_name" };
		String[] optional_fields  = new String[] {"email", "nickname", "bdate", "sex", "phone", "photo", "photo_big", "city", "country"};


		/*
		 * Set the parameters to intent. Any parameter can be omitted. Default values are:
		 * 
		 * providers		- all providers
		 * mandatory fields - first_name, last_name
		 * optional fields 	- empty
		 *  
		 */
		intent.putExtra(UloginAuthActivity.PROVIDERS,	new ArrayList<String>(Arrays.asList(providers)));
		intent.putExtra(UloginAuthActivity.FIELDS,		new ArrayList<String>(Arrays.asList(mandatory_fields)));
		intent.putExtra(UloginAuthActivity.OPTIONAL,	new ArrayList<String>(Arrays.asList(optional_fields)));

		/*
		 * Start uLogin activity and wait for authorization
		 */
		startActivityForResult(intent, REQUEST_ULOGIN);
	}
	
	
	/*
	 * 	Button onClick - here we start uLogin authorization activity
	 */
		public void button_vk_click(View v) {
			
	/*
	 * 		Creating new intent
	 */
			Intent intent = new Intent(getApplicationContext(),
					UloginAuthActivity.class);
			
			/*
			 *  Set the list of shown providers.
			 *  This is custom button, so the provider is only VK
			 */
			String[] providers = { "vkontakte" };
			
			
			/*
			 * Set the required and optional field lists.
			 * String array R.array.ulogin_fields is the list of all available fields.
			 * Do NOT set mandatory all of them! 
			 */
			String[] mandatory_fields = new String[] {"first_name", "last_name" };
			String[] optional_fields  = new String[] {"email", "nickname", "bdate", "sex", "phone", "photo", "photo_big", "city", "country"};


			/*
			 * Set the parameters to intent. Any parameter can be omitted. Default values are:
			 * 
			 * providers		- all providers
			 * mandatory fields - first_name, last_name
			 * optional fields 	- empty
			 *  
			 */
			intent.putExtra(UloginAuthActivity.PROVIDERS,	new ArrayList<String>(Arrays.asList(providers)));
			intent.putExtra(UloginAuthActivity.FIELDS,		new ArrayList<String>(Arrays.asList(mandatory_fields)));
			intent.putExtra(UloginAuthActivity.OPTIONAL,	new ArrayList<String>(Arrays.asList(optional_fields)));

			/*
			 * Start uLogin activity and wait for authorization
			 */
			startActivityForResult(intent, REQUEST_ULOGIN);
		}

	
	
	
	
	/*
	 * Get the result from activities
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		
		/*
		 *  If the result returned from uLogin authorization activity
		 */
		if (requestCode == REQUEST_ULOGIN) {

			
			/*
			 * If the authorization was successfully passed, the fields of
			 * HashMap are the fields, passed to the calling intent. In the
			 * other case, HashMap has the key "error" with the description of
			 * the cancellation reason
			 */
			HashMap<String, String> userdata = (HashMap<String, String>) intent.getSerializableExtra(UloginAuthActivity.USERDATA);
			
			
			/*
			 * Check the authorization result code
			 */
			switch (resultCode) {
			
			case RESULT_OK:
				
				/*
				 * Now you can do with the userdata anything you want.
				 * For example, you can show the greeting text.
				 * 
				 * There is no check of first_name and last_name presence here
				 * because these fields where set mandatory.
				 */
				Toast.makeText(
						this,
						"Hello, " + userdata.get("first_name") + " "
								+ userdata.get("last_name") + "!",
						Toast.LENGTH_SHORT).show();

				
				
				
				/*
				 * Parse all userdata fields
				 */
				String userdata_text = "";
				Iterator userdata_iterator = userdata.entrySet().iterator();
				while (userdata_iterator.hasNext()) {
					Map.Entry pairs = (Map.Entry) userdata_iterator.next();
					userdata_text += (pairs.getKey() + " = " + pairs.getValue()) + "\n";
					userdata_iterator.remove();
				}
				
				
				/* 
				 * Show user data
				 */
				((TextView) findViewById(R.id.text)).setText(userdata_text);
				break;

				
			case RESULT_CANCELED:
				/*
				 * Authorization was cancelled by user. Checking the "error" key
				 */
				if (userdata.get("error").equals("canceled")) {
					Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(this, "Error: " + userdata.get("error"),
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
