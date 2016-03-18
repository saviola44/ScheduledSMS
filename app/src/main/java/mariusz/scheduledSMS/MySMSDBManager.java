package mariusz.scheduledSMS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.List;

/**
 * Created by mariusz on 24.09.15.
 */
public class MySMSDBManager {
    private Context context;
    MessageHelper messageHelper;
    SQLiteDatabase sqLiteDatabase;
    MessageDAO messageDB;
    public MySMSDBManager(Context context) {
        this.context=context;
        messageHelper=new MessageHelper(context);
        sqLiteDatabase = messageHelper.getWritableDatabase();
        messageDB=new MessageDAO(sqLiteDatabase);
    }
    public void addSMSToDatabase(MessageModel m){
        long id = messageDB.saveSMSInDatabase(m);
        m.setId(id);
    }
    public List<MessageModel> getAllSMSFromDatabase(){
        return messageDB.getMessagesFromDataBase();
    }
    public void updateSMSInDatabase(MessageModel m){
        messageDB.deleteSMSFromDataBase(m);

        messageDB.saveSMSInDatabase(m);
    }
    public void deleteSMSFromDataBase(MessageModel m){

        messageDB.deleteSMSFromDataBase(m);
    }
}
