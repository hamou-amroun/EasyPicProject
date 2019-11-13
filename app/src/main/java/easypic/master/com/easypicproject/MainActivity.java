package easypic.master.com.easypicproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CourbeView courbeView;
    ContactBase contactBase =new  ContactBase(getBaseContext());
    List<Integer> capteurUSON = new ArrayList<>();
    BluetoothConnexion bluetoothConnexion;

    public ListView listView;
    public TextView tex;
    private static final String TABLE_NAME = "capteur";
    public static final String KEY_VAL_CAPTEUR = "val_capteur";

    SQLiteDatabase db;
    String affiche = "";
    int num;
    String[] values=new String[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        courbeView = (CourbeView)findViewById(R.id.courbeView);
        courbeView.setVisibility(View.VISIBLE);
        bluetoothConnexion = BluetoothConnexion.getINSTANCE();
        //bluetoothConnexion = new BluetoothConnexion();
        bluetoothConnexion.Connexion();


        final Button buttonInsert =(Button) findViewById(R.id.affichergraphe);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread tacheLecture = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //On cherche a savoir si le robot nous a envoyé une trame
                            if (bluetoothConnexion.donneeRecu) {
                                String donneeRecu = bluetoothConnexion.LectureData();
                                bluetoothConnexion.EnvoiMessage("1");
                                //on valide la chaine
                                ValidationDonnee(donneeRecu);
                            }
                        }
                    }
                });
                tacheLecture.start();
            }
        });

        //creation de la base de données
        listView = (ListView) findViewById(R.id.list);


        db = this.openOrCreateDatabase(TABLE_NAME, MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS capteur");
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                " (" +
                " " + KEY_VAL_CAPTEUR + " VARCHAR" +
                ");");


        final Button buttonGraphe =(Button) findViewById(R.id.afficherbase);
        buttonGraphe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothConnexion.donneeRecu) {
                    String donneeRecu = bluetoothConnexion.LectureData();
                    fetchContacts(donneeRecu);

                }
            }
        });
    }

    //creation d'une base de données
    public void fetchContacts(String chaine) {
        Log.e("entree", chaine);
        String[] tableau= null;
        tableau=chaine.split(" ");

        // Loop for every contact in the phone

        if (tableau.length > 0) {
            for (int i=0;i<tableau.length; i++) {

                db.execSQL("INSERT INTO capteur VALUES('" + tableau[i] + "');");
            }

            Cursor select = db.rawQuery("SELECT * FROM capteur", null);
            num = 0;

            while (select.moveToNext() && (num < 20)) {

                affiche = "Valeur : " + select.getString(0);
                values[num] += affiche;
                System.out.println(values[num]);}

                   /* SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.activity_main, select, values, null);
                    ListView listView = (ListView) findViewById(R.id.list);

                    // Assign adapter to ListView
                    listView.setAdapter(dataAdapter);
                }num++;*/


        }
    }

    //validation d'une trame recu
    private void ValidationDonnee(String chaine){
        char valcapteur[] = new char[chaine.length()];
        Log.e("entree", chaine);
        System.out.println("c'est mon tableau nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"+chaine);
        String[] tableau= null;
        tableau=chaine.split(" ");

        for(int k=0;k<tableau.length;k++){
            System.out.println("element "+k+" est moiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii"+tableau[k]);

        }
        try {

            char mot = 0;
            for(int i=0; i<chaine.length(); i++){
                mot = chaine.charAt(i);
                valcapteur[i] = mot;

            }

            for(int i=0; i<tableau.length; i++){

                System.out.println("le " + i + " eme point est " + tableau[i]);

                // contactBase.ajouter(Integer.parseInt(tableau[i]));
                capteurUSON.add(Integer.parseInt(String.valueOf(tableau[i])));

                System.out.println(tableau[i]);
                courbeView.UpdateCapteurs(capteurUSON);
            }
            // contactBase.lireValSensorFromDB();
        }
        catch (Exception e){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
