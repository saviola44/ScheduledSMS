package mariusz.scheduledSMS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mariusz.scheduledSMS.R;

/**
 * Created by mariusz on 24.09.15.
 */
public class ListSMSAdapter extends ArrayAdapter {
    List list = new ArrayList();
    public ListSMSAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DataHandler handler;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.sms_row_layout, parent, false);
            handler = new DataHandler();
            handler.image = (ImageView) row.findViewById(R.id.sms_image);
            handler.phone = (TextView) row.findViewById(R.id.phone_number);
            handler.sms = (TextView) row.findViewById(R.id.sms);
            handler.date = (TextView) row.findViewById(R.id.date);
            row.setTag(handler);
        }
        else{
            handler = (DataHandler) row.getTag();
        }
        MessageModel m = (MessageModel) getItem(position);
        handler.image.setImageResource(R.drawable.sms_list);
        handler.phone.setText(m.getPhone());
        handler.sms.setText(m.getSms());
        Calendar cal = Calendar.getInstance();
        cal.set(m.getYear(), m.getMonth(), m.getDay(), m.getHour(), m.getMinute());
        handler.date.setText(cal.getTime().toString());
        return row;
    }

    @Override
    public int getCount() {
        return list.size();
    }
    static class DataHandler{
        ImageView image;
        TextView phone;
        TextView sms;
        TextView date;
    }
}
