package mariusz.scheduledSMS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mariusz on 21.09.15.
 */
public class MessageHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "sendsms.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "Message_Table"; //nazwa tabeli

    MessageHelper(final Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sb = new StringBuilder();
        /*CREATE TABLE Message_Table (ID INTEGER PRIMARY KEY, Date_Time DATETIME,
         Phone_Number TEXT, Sms TEXT);*/
        sb.append("CREATE TABLE " + TABLE_NAME + " (");
        sb.append(MessageTable.MessageColumns.ID + " INTEGER PRIMARY KEY, ");
        sb.append(MessageTable.MessageColumns.DateTime + " TEXT NOT NULL, ");
        sb.append(MessageTable.MessageColumns.PhoneNumber + " TEXT NOT NULL, ");
        sb.append(MessageTable.MessageColumns.Sms + " TEXT, ");
        sb.append(MessageTable.MessageColumns.Sended + " INTEGER NOT NULL);");
        sqLiteDatabase.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
