package mariusz.scheduledSMS;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import mariusz.scheduledSMS.R;

public class MainActivity extends Activity {
    //akcje dla menu
    public static final int SMS_SCHEDULED_FOR_SENDING = 0;
    public static final int SMS_SENDED = 1;
    public static final int SMS_FAILED_TO_SEND = 2;
    //przekazywany do intencji do pobierania sms'ow z ksiazki telefonicznej
    public static final int PICK_CONTACT_REQUEST = 1;


    private EditText phoneNumber;
    private EditText smsBody;
    private Button smsManagerBtn;//zatwierdz
    private Button mPickDate;
    private Button mPickTime;
    private TextView dateTextView;
    private TextView timeTextView;

    MessageModel smsModel;
    private Messenger messenger=null;
    private boolean isBind=false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messenger = new Messenger(iBinder);
            isBind=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messenger=null;
            isBind=false;
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPickDate = (Button) findViewById(R.id.pickDate);
        mPickTime = (Button) findViewById(R.id.pickTime);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        smsBody = (EditText) findViewById(R.id.smsBody);
        smsManagerBtn = (Button) findViewById(R.id.smsManager);
        dateTextView = (TextView) findViewById(R.id.date_text);
        timeTextView = (TextView) findViewById(R.id.time_text);

        //Przygotowanie obiektu MessageModel do pracy
        smsModel = new MessageModel(0,"","",0,0,0,0,0,0);

        smsManagerBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                sendSmsByManager();
            }
        });

        mPickDate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //showDialog(DATE_DIALOG_ID);
                DialogFragment newFragment = new DatePickerFragment();
                ((DatePickerFragment) newFragment).setM(smsModel);
                ((DatePickerFragment) newFragment).setTextView(dateTextView);
                ((DatePickerFragment) newFragment).setContext(MainActivity.this);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        mPickTime.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //showDialog(TIME_DIALOG_ID);
                DialogFragment newFragment = new TimePickerFragment();
                ((TimePickerFragment) newFragment).setM(smsModel);
                ((TimePickerFragment) newFragment).setTextView(timeTextView);
                ((TimePickerFragment) newFragment).setContext(MainActivity.this);
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });

        if (savedInstanceState != null) {
            DatePickerFragment dpf = (DatePickerFragment) getFragmentManager()
                    .findFragmentByTag("datePicker");
            if (dpf != null) {
                dpf.setContext(MainActivity.this);
                dpf.setM(smsModel);
                dpf.setTextView(dateTextView);
            }
            TimePickerFragment tpf = (TimePickerFragment) getFragmentManager()
                    .findFragmentByTag("timePicker");
            if (tpf != null) {
                tpf.setContext(MainActivity.this);
                tpf.setM(smsModel);
                tpf.setTextView(timeTextView);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SMSService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onStop(){
        unbindService(serviceConnection);
        isBind=false;
        messenger=null;
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("phone", phoneNumber.getText().toString());
        outState.putString("smsBody", smsBody.getText().toString());
        outState.putString("time", timeTextView.getText().toString());
        outState.putString("date", dateTextView.getText().toString());
        outState.putParcelable("smsModel", smsModel);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String p = savedInstanceState.getString("phone");
        phoneNumber.setText(p);
        String s = savedInstanceState.getString("smsBody");
        smsBody.setText(s);
        String t = savedInstanceState.getString("time");
        timeTextView.setText(t);
        String d = savedInstanceState.getString("date");
        dateTextView.setText(d);
        smsModel = savedInstanceState.getParcelable("smsModel");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuItem m1 = menu.add(Menu.NONE, SMS_SCHEDULED_FOR_SENDING, Menu.NONE, R.string.sms_scheduled);
        m1.setIcon(resizeImage(R.drawable.scheduled1, 108, 108));

        MenuItem m2 = menu.add(0, SMS_SENDED, 0, R.string.sms_sended);
        m2.setIcon(resizeImage(R.drawable.sended1, 108, 108));
        MenuItem m3=menu.add(0, SMS_FAILED_TO_SEND, 0, R.string.sms_failed);
        m3.setIcon(resizeImage(R.drawable.failed1, 108, 108));
        return super.onCreateOptionsMenu(menu);
    }
    private Drawable resizeImage(int resId, int w, int h)
    {
        // load the origial Bitmap
        Bitmap BitmapOrg = BitmapFactory.decodeResource(getResources(), resId);
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;
        // calculate the scale
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0,width, height, matrix, true);
        return new BitmapDrawable(resizedBitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(getApplicationContext(), SMSListActivity.class);
        switch (item.getItemId()){
            case SMS_SCHEDULED_FOR_SENDING:
                i.putExtra("type", SMS_SCHEDULED_FOR_SENDING);
                startActivity(i);
                return true;
            case SMS_SENDED:
                i.putExtra("type", SMS_SENDED);
                startActivity(i);
                return true;
            case SMS_FAILED_TO_SEND:
                i.putExtra("type", SMS_FAILED_TO_SEND);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //obsługa przycisku zatwierdz
    public void sendSmsByManager() {
        try {

            smsModel.setPhone(phoneNumber.getText().toString());
            smsModel.setSms(smsBody.getText().toString() + " ");
            dateTextView.setText("");
            timeTextView.setText("");
            phoneNumber.setText("");
            smsBody.setText("");
            if(Patterns.PHONE.matcher(smsModel.getPhone()).matches()){
                Message msg = Message.obtain(null, SMSService.SEND_SMS, 0, 0);
                Bundle bundle = new Bundle();
                bundle.putParcelable("sms", smsModel);
                msg.setData(bundle);
                try {
                    messenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), R.string.invalidPhone, Toast.LENGTH_LONG).show();
            }

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), R.string.failedToSendSms,
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void getPhoneNumberFromBook(View view){
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Sprawdzenie czy requectCode jest nasza odpowiedzia
        if (requestCode == PICK_CONTACT_REQUEST) {
            //upewnienie sie czy zakonczylo sie sukcesem
            if (resultCode == RESULT_OK) {
                // Pobranie Uri kontaktu
                Uri contactUri = data.getData();
                //potrzebujemy tylko kolumny z numerem telefonu
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                //wykonujemy zapytanie aby otrzymac nasza kolumne
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                //wydobycie ne klumny z numerem telefonu
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);
                phoneNumber.setText(number);
            }
        }
    }
}
