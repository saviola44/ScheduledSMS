package mariusz.scheduledSMS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import mariusz.scheduledSMS.R;

/**
 * Created by mariusz on 22.09.15.
 */
public class ShowSms extends Activity {
    MessageModel m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_sms_layout);
        TextView phoneTextView = (TextView)findViewById(R.id.phonev);
        TextView smsTextView = (TextView)findViewById(R.id.sms_content);
        TextView dateTextView = (TextView)findViewById(R.id.sended);
        Intent i = getIntent();
        m = (MessageModel)i.getParcelableExtra("sms");
        phoneTextView.setText(m.getPhone());
        smsTextView.setText(m.getSms());
        dateTextView.setText(m.getYear()+"/"+m.getMonth()+"/"+m.getDay()+ " " + m.getHour()+":"+m.getMinute());

    }
    public void deleteMessage(View view){
        MySMSDBManager mgr = new MySMSDBManager(getApplicationContext());
        mgr.deleteSMSFromDataBase(m);
        Toast.makeText(getApplicationContext(), "message was deleted", Toast.LENGTH_LONG).show();
        this.finish();
    }
}
