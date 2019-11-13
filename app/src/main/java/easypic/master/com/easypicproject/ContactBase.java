package easypic.master.com.easypicproject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class ContactBase extends Base {
    public static final String CONTACT_KEY = "key";
    public static final int NUMCOL_KEY = 0;
    public static final String CONTACT_DATE = "date";
    public static final int NUMCOL_DATE = 1;
    public static final String CONTACT_CAPTEUR = "capteur";
    public static final int NUMCOL_CAPTEUR = 2;

    public static final String CONTACT_TABLE_NAME = "Capteurs";

    public static final String CONTACT_TABLE_CREATE =
            "CREATE TABLE " + CONTACT_TABLE_NAME + " (" +
                    CONTACT_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CONTACT_DATE + " DATE, " +
                    CONTACT_CAPTEUR + " TEXT);";

    public static final String CONTACT_TABLE_DROP = "DROP TABLE IF EXISTS " + CONTACT_TABLE_NAME + ";";

    public ContactBase(Context pContext) {
        super(pContext);
    }



    /**
     * @param capteur le contact à ajouter à la base
     */
    public void ajouter(int capteur) {
        SQLiteDatabase mDb = open();

        ContentValues value = new ContentValues();
        Calendar c = Calendar.getInstance();
        value.put(ContactBase.CONTACT_DATE, DateFormat.getDateTimeInstance().format(new Date()));
        value.put(ContactBase.CONTACT_CAPTEUR, capteur);
        mDb.insert(ContactBase.CONTACT_TABLE_NAME, null, value);

        close(mDb);
    }

    /**
     * @param id l'identifiant du contact à supprimer
     */
    public void effacerContact(long id) {
        SQLiteDatabase mDb = open();
        mDb.delete(ContactBase.CONTACT_TABLE_NAME, CONTACT_KEY + " = " + id, null);
        close(mDb);
    }

    void lireValSensorFromDB() {
        SQLiteDatabase mDb = open();
        String affiche;
        String[] values=new String[20];
        Cursor cur = mDb.rawQuery("SELECT * FROM SensorVal", null);
        int num=0;
        while (cur.moveToNext() && (num < 20)) {

            affiche =  cur.getString(0) + "\n\n";
            values[num] += affiche;
            System.out.println("c'est moi je suis la samirrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr"+affiche);

            num++;
        }
    }



    //Cette méthode permet de convertir un cursor en un contact
    private int cursorToContact(Cursor c,SQLiteDatabase mDb){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0) {
            Log.e("cursor null","+");
            close(mDb);
            return 0;
        }

        //Sinon on se place sur le premier élément
        c.moveToFirst();

        return 0;
    }
}

