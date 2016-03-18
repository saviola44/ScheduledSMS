package mariusz.scheduledSMS;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import mariusz.scheduledSMS.R;

/**
 * Created by mariusz on 24.09.15.
 */
public class SMSListActivity extends Activity {
    ListView listView;
    int smsImage = R.drawable.sms_list;
    List<MessageModel> list;
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.sms_list_layout);
        listView=(ListView) findViewById(R.id.sms_list_view);
        //listView = (ListView) findViewById(R.id.sms_list_view);

        //pobranie smsow z bazy danych
        //pobranie sms'ow z bazy danych
        MySMSDBManager mgr = new MySMSDBManager(getApplicationContext());
        list = mgr.getAllSMSFromDatabase();

        ListSMSAdapter adapter = new ListSMSAdapter(getApplicationContext(), R.layout.sms_row_layout);
        listView.setAdapter(adapter);
        int filter=getIntent().getIntExtra("type",-1);

        for(MessageModel m : list){
            if(m.getSended()==filter){
                adapter.add(m);
            }
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ShowSms.class);
                MessageModel message = ((MessageModel)parent.getItemAtPosition(position));
                intent.putExtra("sms", message);
                startActivity(intent);
            }
        });

    }

    /*@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

    }*/
}
