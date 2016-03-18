package mariusz.scheduledSMS;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsManager;

import java.util.Calendar;

import mariusz.scheduledSMS.R;

public class SMSSendService extends Service {
    public SMSSendService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SmsManager smsManager = SmsManager.getDefault();
        MessageModel m = (MessageModel) intent.getParcelableExtra("message");
        String sms = m.getSms();
        smsManager.sendTextMessage(m.getPhone(),null,m.getSms(),null,null);
        MySMSDBManager mgr = new MySMSDBManager(getApplicationContext());
        m.setSended(MainActivity.SMS_SENDED);
        mgr.updateSMSInDatabase(m);
        notify(m);
        return Service.START_NOT_STICKY;
    }

    private void notify(MessageModel m){
        Intent i = new Intent(this, ShowSms.class);
        i.putExtra("sms", m);
        PendingIntent pi = PendingIntent.getActivity(this, (int) Calendar.getInstance().getTimeInMillis(), i, 0);
        NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int smsIcon = R.drawable.sms;
        long time = System.currentTimeMillis();
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification n = new Notification.Builder(getApplicationContext()).setContentTitle(getString(R.string.smsNotification))
                .setContentText(getString(R.string.smsSendedok)).setUsesChronometer(true).setSound(uri)
                .setSmallIcon(smsIcon).setContentIntent(pi).setAutoCancel(true).build();

        mgr.notify(0, n);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
