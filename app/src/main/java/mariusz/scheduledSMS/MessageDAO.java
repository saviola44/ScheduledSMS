package mariusz.scheduledSMS;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mariusz on 21.09.15.
 */
/*klasa ta wykonuje operacje wstawie pobiera i usuwa dane z bazy danych */

public class MessageDAO {
    private SQLiteDatabase db;
    private SQLiteStatement insertStatement;

    private static final String INSERT =
            "INSERT INTO " + MessageHelper.TABLE_NAME + "(" +
                    MessageTable.MessageColumns.DateTime + ", " + MessageTable.MessageColumns.PhoneNumber +
                    ", " + MessageTable.MessageColumns.Sms + ", " + MessageTable.MessageColumns.Sended +
                    ") VALUES (?, ?, ?, ?)";
    public MessageDAO(SQLiteDatabase db){
        this.db=db;
        //przyspiesza to dzialanie kodu bo framework moze wstepnie przetworzyc i
        // ponownie wykorzystac plan instrukcji
        insertStatement=db.compileStatement(INSERT);
    }

    public long saveSMSInDatabase(MessageModel m){
        //najpierw nalezy usunac wczesniejsze wiazania
        insertStatement.clearBindings();
        //teraz trzeba powiazac kazdy symbol zastepczy z instrukcji z odpowiednia wartoscia obiektu modelu
        Calendar now = Calendar.getInstance();
        now.set(m.getYear(), m.getMonth(), m.getDay(), m.getHour(), m.getMinute());
        //trzeba przerobic date na String
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = simpleFormat.format(now.getTime());
        insertStatement.bindString(1, strDate);
        insertStatement.bindString(2, m.getPhone());
        insertStatement.bindString(3,m.getSms());
        insertStatement.bindLong(4,(long)m.getSended());
        return insertStatement.executeInsert();
    }

    public boolean deleteSMSFromDataBase(MessageModel m){
            return db.delete(MessageHelper.TABLE_NAME,
                    MessageTable.MessageColumns.ID + "="+
                    String.valueOf(m.getId()), null)>0;

    }
    public List<MessageModel> getMessagesFromDataBase(){
        List<MessageModel> list = new ArrayList<MessageModel>();
        String[] columns = {MessageTable.MessageColumns.ID, MessageTable.MessageColumns.DateTime,
                MessageTable.MessageColumns.PhoneNumber, MessageTable.MessageColumns.Sms,
                MessageTable.MessageColumns.Sended };

        Cursor c = db.query(MessageHelper.TABLE_NAME, columns, null, null,null,null,MessageTable.MessageColumns.DateTime,null);
        if(c.moveToFirst()){
            do{
                MessageModel m = buildMessageFromCursor(c);
                if(m!=null){
                    list.add(m);
                }
            }while(c.moveToNext());
        }
        if(!c.isClosed()){
            c.close();
        }
        return list;
    }

    MessageModel buildMessageFromCursor(Cursor c){
        MessageModel m = null;
        if(c!=null){
            m = new MessageModel(0,"","",0,0,0,0,0,0);
            m.setId(c.getLong(0));
            String time = c.getString(1);
            Date parsedDate = null;
            try {
                parsedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
                Calendar cal = Calendar.getInstance();
                cal.setTime(parsedDate);
                m.setYear( cal.get(Calendar.YEAR));
                m.setMonth( cal.get(Calendar.MONTH));
                m.setDay( cal.get(Calendar.DAY_OF_MONTH));
                m.setHour(cal.get(Calendar.HOUR_OF_DAY));
                m.setMinute(cal.get(Calendar.MINUTE));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            m.setPhone(c.getString(2));
            m.setSms(c.getString(3));
            m.setSended((int)c.getLong(4));
        }
        return m;
    }
}
