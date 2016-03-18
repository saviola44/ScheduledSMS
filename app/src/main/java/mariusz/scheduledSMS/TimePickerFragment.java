package mariusz.scheduledSMS;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by mariusz on 14.09.15.
 */
public class TimePickerFragment extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener {
    private MessageModel m;
    private TextView textView;
    Activity activity;
    public void setContext(Activity c){
        this.activity = c;
    }
    public void setTextView(TextView textView){
        this.textView = textView;
    }
    public void setM(MessageModel m){
        this.m = m;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceSateate) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(activity, this, mHour,mMinute,true);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        m.setHour(i);
        m.setMinute(i1);
        String text="";
        if(i<10) text+=0;
        text+=i+":";
        if(i1<10) text+=0;
        text+=i1;
        textView.setText(text);
    }
}
