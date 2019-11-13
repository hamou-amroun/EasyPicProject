package easypic.master.com.easypicproject;

/**
 * Created by amrounhamou on 10/12/2017.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class CourbeView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private Resources 	mRes;
    private Context 	mContext;
    private int nbrPoints = 50;

    private     boolean destroy = true;
    private     Thread  cv_thread;
    SurfaceHolder holder;

    Paint paint;
    List<Integer> capteursUSON = new ArrayList<>();

    public CourbeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder = getHolder();
        holder.addCallback(this);
        mContext	= context;
        mRes 		= mContext.getResources();

        initparameters();

        cv_thread   = new Thread(this);
        setFocusable(true); // make sure we get key events
    }

    public void initparameters() {
        paint = new Paint();
        paint.setColor(0xff0000);

        paint.setDither(true);
        paint.setColor(0xFFFFFF00);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        paint.setTextAlign(Paint.Align.LEFT);

        if ((cv_thread!=null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }
    }

    private  void paintCourbe(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        Paint paint2 = new Paint();
        paint2.setColor(Color.GRAY);
        paint.setTextSize(30);

        while(capteursUSON.size() > nbrPoints){//50 points sur l'ecran
            capteursUSON.remove(0);
        }

        Integer capteursNM1 = capteursUSON.get(0);
        int xstart = 0;
        int xstop = getWidth()/nbrPoints;
        for (Integer capteurs:capteursUSON){
            if(capteursNM1 == capteurs) { // continue;
            paint2.setColor(Color.BLACK);
            canvas.drawLine(xstart, getY(capteursNM1), xstop, getY(capteurs), paint2);
            capteursNM1 = capteurs;
            xstart+=getWidth()/nbrPoints;
            xstop+=getWidth()/nbrPoints;}
        }
        paint2.setColor(Color.MAGENTA);
        canvas.drawText("valeur_us: " + capteursUSON.get(capteursUSON.size() - 1), 10, getY(capteursUSON.get(capteursUSON.size() - 1)), paint2);
    }

    float getY(int capteur){
        float retour;
        retour = getHeight() - ((float)capteur / 500) * getHeight();
        return retour;
    }

    public void UpdateCapteurs(List<Integer> capteurUSON){
        this.capteursUSON = capteurUSON;
    }
    void DestroyThread(){
        destroy = false;
        try {
            cv_thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void nDraw(Canvas canvas) {
        canvas.drawRGB(255, 255, 255);
        paintCourbe(canvas);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("-> FCT <-", "surfaceChanged " + width + " - " + height);
        initparameters();
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        Log.i("-> FCT <-", "surfaceCreated");
    }


    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.i("-> FCT <-", "surfaceDestroyed");
    }

    /**
     * run
     */
    public void run() {
        Canvas c = null;
        while (destroy) {
            try {
                cv_thread.sleep(200);
                try {
                    c = holder.lockCanvas(null);
                    nDraw(c);
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }
            } catch(Exception e) {
                Log.e("-> RUN <-", "NOK "+ e);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.e("-> FCT <-", "onKeyUp: "+ keyCode);
        return true;
    }

    public boolean onTouchEvent (MotionEvent event) {
        Log.e("-> FCT <-", "onTouchEvent: "+ event.getX()+"-"+event.getY());
        double resultat = 0.0;
        float x = event.getX();
        float y = event.getY();


        return super.onTouchEvent(event);
    }
}
