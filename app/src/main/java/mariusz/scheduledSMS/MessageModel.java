package mariusz.scheduledSMS;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mariusz on 14.09.15.
 */
public class MessageModel implements Parcelable {
    private int hour;
    private int minute;
    private int year;
    private int month;
    private int day;
    private long id;
    private int sended;

    private String sms;
    private String phone;

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        public MessageModel createFromParcel(Parcel in) {
            return new MessageModel(in);
        }

        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };
    MessageModel(Parcel in){
        id=in.readLong();
        phone=in.readString();
        sms=in.readString();
        year=in.readInt();
        month=in.readInt();
        day=in.readInt();
        hour=in.readInt();
        minute=in.readInt();
        sended=in.readInt();
    }
    MessageModel(long id, String phone, String sms, int year, int month, int day, int hour, int minute, int sended){
        this.id=id;
        this.phone=phone;
        this.sms=sms;
        this.year=year;
        this.month=month;
        this.day=day;
        this.hour=hour;
        this.minute=minute;
        this.sended=sended;
    }

    public int getSended(){ return sended; }

    public long getId(){ return id; }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getSms() {
        return sms;
    }

    public String getPhone() {
        return phone;
    }

    public void setSended(int sended) { this.sended = sended; }

    public void setId(long id){ this.id=id; }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(phone);
        parcel.writeString(sms);
        parcel.writeInt(year);
        parcel.writeInt(month);
        parcel.writeInt(day);
        parcel.writeInt(hour);
        parcel.writeInt(minute);
        parcel.writeInt(sended);
    }
}
