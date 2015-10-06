package mariusz.scheduledSMS;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;
import java.util.Calendar;

/**
 * Created by mariusz on 16.09.15.
 * klasa ta odbiera z widoku nasze dane o sms'ie (tresc, nr, data, czas)
 * nastepnie zapisuje ta wiadomosc w bazie danych
 * oraz za pomoca pendingIntent wywoluje w odpowiednim czasie obiekt klasy SMSSendService ktory juz
 * wysyla sms
 */
public class SMSService extends Service {
    static final int SEND_SMS = 1;
    //id dla dla pendingIntent zeby kazdy ten obiekt byl wywolywany z innym identyfikatorem
    public static int id=0;
    Messenger messenger=  new Messenger(new IncomingHandler());

    public class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case SEND_SMS:
                    Toast.makeText(getApplicationContext(), "Your sms will be send", Toast.LENGTH_LONG).show();
                    //pobranie wiadomosci z intencji

                    Bundle bundle = msg.getData();
                    bundle.setClassLoader(getClassLoader());
                    MessageModel m = (MessageModel) bundle.getParcelable("sms");

                    MySMSDBManager mgr = new MySMSDBManager(getApplicationContext());
                    mgr.addSMSToDatabase(m);
                    //uruchomienie i wyslanie sms w odpowiednim czasie
                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent i = new Intent(getApplicationContext(), SMSSendService.class);
                    i.putExtra("message", m);
                    PendingIntent sender = PendingIntent.getService(getApplicationContext(),id, i, PendingIntent.FLAG_ONE_SHOT);
                    Calendar now = Calendar.getInstance();
                    now.set(m.getYear(), m.getMonth(), m.getDay(), m.getHour(), m.getMinute());
                    manager.set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), sender);
                    id++;
                    break;
                default:
                    super.handleMessage(msg);

            }
        }
    }




    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }


}
