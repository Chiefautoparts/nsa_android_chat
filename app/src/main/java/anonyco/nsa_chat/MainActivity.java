package anonyco.nsa_chat;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.net.Uri;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnAll;
    Button btnInbox;
    Button btnSent;
    Button btnDraft;
    TableLayout tblMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });



        btnAll = (Button) findViewById(R.id.btnAll);
        btnAll.setOnClickListener(this);

        btnSent = (Button) findViewById(R.id.btnInbox);
        btnSent.setOnClickListener(this);

        btnDraft = (Button) findViewById(R.id.btnDraft);
        btnDraft.setOnClickListener(this);

        tblMain = (TableLayout) findViewById(R.id.tblMain);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String strPhone;
        String strMessage;
        SmsManager sms;


        Uri smsUri = Uri.parse("content://sms/");

        switch (v.getId()) {

            case R.id.btnInbox:
                smsUri = Uri.parse("content://sms/inbox");
                break;
            case R.id.btnSent:
                smsUri = Uri.parse("content://sms/sent");
                break;
            case R.id.btnDraft:
                smsUri = Uri.parse("content://sms/draft");
                break;
        }
        Cursor cursor = getContentResolver().query(smsUri, null, null, null, null);

        Cursor2TableLayout(cursor, tblMain);
    }

    public void Cursor2TableLayout(Cursor cur, TableLayout tblLayout) {
        tblLayout.removeAllViews();

        if (!cur.moveToFirst()) {
            return;
        }

        TableRow headersRow = new TableRow(this);

        for (int j = 0; j < cur.getColumnCount(); j++) {
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setText(cur.getColumnName(j));
            textView.setPadding(0, 0, 5, 0);

            if (Build.VERSION.SDK_INT >= 11) {
                textView.setAlpha(0.8f);
            } else {
                AlphaAnimation animation = new AlphaAnimation(0.8f, 0.8f);
                animation.setDuration(0);
                animation.setFillAfter(true);
                textView.startAnimation(animation);
            }

            headersRow.addView(textView);
        }

        headersRow.setPadding(10, 10, 10, 10);

        tblLayout.addView(headersRow);

        for (int i = 0; i < cur.getCount(); i++) {
            TableRow tableRow = new TableRow(this);

            for (int j = 0; j < cur.getColumnCount(); j++) {
                TextView textView = new TextView(this);

                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setText(cur.getString(j));
                textView.setPadding(0, 0, 5, 0);
                tableRow.addView(textView);
            }
            tableRow.setPadding(10, 10, 10, 10);
            tblLayout.addView(tableRow);
            cur.moveToNext();
        }
        cur.close();
    }

}

