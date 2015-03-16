package ru.ulogin.uloginsdkdemo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ru.ulogin.sdk.UloginAuthActivity;

public class MainActivity extends ActionBarActivity {

    public final int REQUEST_ULOGIN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView t = (TextView) findViewById(R.id.text);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runUlogin();
            }
        });

        runUlogin();
    }


    protected void runUlogin() {
        Intent intent = new Intent(getApplicationContext(),
                UloginAuthActivity.class);

//		String[] providers = { "vkontakte", "google", "mailru", "odnoklassniki"};
        String[] providers = getResources().getStringArray(ru.ulogin.sdk.R.array.ulogin_providers);
        intent.putExtra(UloginAuthActivity.PROVIDERS, new ArrayList<String>(Arrays.asList(providers)));


//		String[] fields = getResources().getStringArray(ru.ulogin.sdk.R.array.ulogin_fields);
//		intent.putExtra(UloginAuthActivity.OPTIONAL, new ArrayList<String>(Arrays.asList(fields)));
        intent.putExtra(UloginAuthActivity.FIELDS, new ArrayList<String>(Arrays.asList(new String[]{"first_name", "last_name", "email", "token"})));


        intent.putExtra(UloginAuthActivity.OPTIONAL, new ArrayList<String>(Arrays.asList(new String[]{"nickname", "bdate", "sex", "phone", "photo", "photo_big", "city", "country"})));


//		intent.putExtra(UloginAuthActivity.APPLICATION_ID,"abc_app_id");
//		intent.putExtra(UloginAuthActivity.SECRET_KEY,	"xxx");
//      intent.putExtra(UloginAuthActivity.CLEAN_COOKIES_AFTER_AUTH,true);


        startActivityForResult(intent, REQUEST_ULOGIN);
    }


    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_ULOGIN) {

            HashMap<String, String> userdata = (HashMap<String, String>) intent.getSerializableExtra(UloginAuthActivity.USERDATA);
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(this, "Здравствуйте, " + userdata.get("first_name") + " " + userdata.get("last_name") + "!", Toast.LENGTH_SHORT).show();

                    String s = "";
                    Iterator it = userdata.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pairs = (Map.Entry) it.next();
                        s += (pairs.getKey() + " = " + pairs.getValue()) + "\n";
                        it.remove();
                    }
                    ((TextView) findViewById(R.id.text)).setText(s);
                    break;
                case RESULT_CANCELED:
                    if (userdata.get("error").equals("canceled")) {
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error: " + userdata.get("error"), Toast.LENGTH_SHORT).show();
                    }

                    break;
                default:
                    Toast.makeText(this, "Unknown result", Toast.LENGTH_SHORT).show();
            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}