package mariusz.scheduledSMS;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Messenger;
import java.util.Calendar;
import java.util.List;

public class SMSReceiver extends BroadcastReceiver {
    Messenger messenger = null;
    public SMSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        MySMSDBManager mgr = new MySMSDBManager(context);
        List<MessageModel> list;
        list = mgr.getAllSMSFromDatabase();
        //aktualny czas - 5 minut
        Calendar now = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        now.add(Calendar.MINUTE, -5);
        for(int i=0; i<list.size();i++){
            MessageModel m = list.get(i);
            cal.set(m.getYear(), m.getMonth(), m.getDay(), m.getHour(), m.getMinute());
            if(cal.after(now)){
                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent in = new Intent(context, SMSSendService.class);
                in.putExtra("message", m);
                PendingIntent sender = PendingIntent.getService(context, (int) Calendar.getInstance().getTimeInMillis(), in, PendingIntent.FLAG_ONE_SHOT);
                manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
            }
            else if(m.getSended()==MainActivity.SMS_SCHEDULED_FOR_SENDING){
                m.setSended(MainActivity.SMS_FAILED_TO_SEND);
                mgr.updateSMSInDatabase(m);
            }
        }
    }
}
