package mariusz.scheduledSMS;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {
    private MessageModel m;
    TextView textView;
    public void setTextView(TextView textView){
        this.textView=textView;
    }
    public void setM(MessageModel m){
        this.m = m;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceSateate) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        //potrzebna walidacja jeszcze
        m.setYear(year);
        m.setMonth(month);
        m.setDay(day);
        String text = year + ".";
        if(month<10) text+=0;
        text+=month+".";
        if(day<10)text+=0;
        text+=day;

        textView.setText(text);
    }
}