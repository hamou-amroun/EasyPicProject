package easypic.master.com.easypicproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String CONTACT_KEY = "key";
    public static final String CONTACT_ID = "id";
    public static final String CONTACT_NOM = "Nom";
    public static final String CONTACT_PRENOM = "Prenom";
    public static final String CONTACT_TELEPHONE = "Telephone";
    public static final String CONTACT_MAIL = "AdresseMail";
    public static final String CONTACT_PHOTO = "Photo";

    public static final String CONTACT_TABLE_NAME = "Contact";
    public static final String CONTACT_TABLE_CREATE =
            "CREATE TABLE " + CONTACT_TABLE_NAME + " (" +
                    CONTACT_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CONTACT_ID + " INTEGER, " +
                    CONTACT_NOM + " TEXT, " +
                    CONTACT_PRENOM + " TEXT,"+
                    CONTACT_TELEPHONE + " TEXT,"+
                    CONTACT_MAIL + " TEXT,"+
                    CONTACT_PHOTO + " BLOB);";

    public static final String CONTACT_TABLE_DROP = "DROP TABLE IF EXISTS " + CONTACT_TABLE_NAME + ";";

    public DatabaseHandler(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CONTACT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CONTACT_TABLE_DROP);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CONTACT_TABLE_DROP);
        onCreate(db);
    }

}
