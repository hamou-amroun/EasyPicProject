package easypic.master.com.easypicproject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothConnexion {
    static BluetoothConnexion INSTANCE = null;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    InputStream inStream = null;
    String data;
    Boolean donneeRecu = false;
    ReceptionThread receptionThread;

    private static final UUID SPP_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static String address = "20:15:11:23:74:95";
    BluetoothConnexion(){
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    void Connexion(){
        BluetoothDevice robot = btAdapter.getRemoteDevice(address);
        try {
            btSocket = robot.createRfcommSocketToServiceRecord(SPP_UUID);
        } catch (IOException e) {
            Log.e("Socket", " NOK ");
        }
        btAdapter.cancelDiscovery();
        try {
            btSocket.connect();
            Log.i("Connexion", " OK ");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Log.e("Connexion", " NOK ");
            }
        }

        //activation de la reception si la connexion a marché
        if(btSocket.isConnected()){
            receptionThread = new ReceptionThread();
            receptionThread.start();
        }
    }

    void EnvoiMessage(String trame){
        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            Log.e("stream", " NOK ");
        }

        try {
            outStream.write(trame.getBytes());

            outStream.flush();
            //soutStream.flush();
            Log.v("Envoi", trame);
        } catch (IOException e) {
            //Log.e("Envoi", " NOK ");
        }
    }

    void Deconnexion(){
        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                Log.i("RAZ Buffer", " NOK ");
            }
        }

        try{
            btSocket.close();
        } catch (IOException e2) {
            Log.e("Deconnexion", " NOK ");
        }
    }

    private class ReceptionThread extends Thread{
        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(500);
                    inStream = btSocket.getInputStream();
                    if(inStream.available() > 0)
                    {
                        byte buffer[] = new byte[100];
                        int nbroctet = inStream.read(buffer, 0, 100);
                        if(nbroctet > 0) {
                            byte rawdata[] = new byte[nbroctet];
                            for(int i=0;i<nbroctet;i++)
                                rawdata[i] = buffer[i];

                            data = new String(rawdata);

                            //to remove
//                            data = "15";

                            donneeRecu = true;
                            //Log.e("=>",data);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("erreur de reception", ""+e);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    String LectureData(){
        if(donneeRecu){
            donneeRecu = false;
            return data;
        }
        return null;
    }

    public static BluetoothConnexion getINSTANCE(){
        if(INSTANCE == null){
            INSTANCE = new BluetoothConnexion();
        }
        return INSTANCE;
    }
}
