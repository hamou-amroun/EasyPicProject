package easypic.master.com.easypicproject;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public abstract class Base {
    // Nous sommes à la première version de la base
    // Si je décide de la mettre à jour, il faudra changer cet attribut
    protected final static int VERSION = 1;
    // Le nom du fichier qui représente ma base
    protected final static String NOM = "database.db";

    protected DatabaseHandler mHandler = null;

    public Base(Context pContext) {
        this.mHandler = new DatabaseHandler(pContext, NOM, null, VERSION);
    }

    public SQLiteDatabase open() {
        SQLiteDatabase mDb;
        // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }

    public void close(SQLiteDatabase mDb) {
        mDb.close();
    }

    /*public SQLiteDatabase getDb(SQLiteDatabase mDb) {
        return mDb;
    }*/
}


