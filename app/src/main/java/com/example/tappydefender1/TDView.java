package com.example.tappydefender1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class TDView extends SurfaceView implements Runnable {
    private PlayerShip player;
    public EnemyShip enemy1;
    public EnemyShip enemy2;
    public EnemyShip enemy3;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    volatile boolean playing;
    public ArrayList<SpaceDust> dustList = new
            ArrayList<SpaceDust>();
    Thread gameThread = null;
    public TDView(Context context,int x,int y) {
        super(context);
        ourHolder = getHolder();
        paint = new Paint();
        player = new PlayerShip(context,x,y);
        player = new PlayerShip(context, x, y);
        enemy1 = new EnemyShip(context, x, y);
        enemy2 = new EnemyShip(context, x, y);
        enemy3 = new EnemyShip(context, x, y);
       // gameView = new TDView(this, size.x, size.y);
        int numSpecs = 40;
        for (int i = 0; i < numSpecs; i++) {
            // Where will the dust spawn?
            SpaceDust spec = new SpaceDust(x, y);
            dustList.add(spec);
        }
    }
    public void pause(){
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {
        while (playing){
            update();
                    draw();
                            control();
        }

    }

    private void update(){
        player.update();
        // Update the enemies
       // player = new PlayerShip(context, x, y);
        enemy1.update(player.getSpeed());
        enemy2.update(player.getSpeed());
        enemy3.update(player.getSpeed());
        for (SpaceDust sd : dustList) {
            sd.update(player.getSpeed());
        }
    }
    private void draw(){ if (ourHolder.getSurface().isValid()) {
        //First we lock the area of memory we will be drawing to
        canvas = ourHolder.lockCanvas();
        // Rub out the last frame
        canvas.drawColor(Color.argb(255, 0, 0, 0));

        paint.setColor(Color.argb(255, 255, 255, 255));
        for (SpaceDust sd:dustList){
            canvas.drawPoint(sd.getX(),sd.getY(),paint);
        }
        // Draw the player
        canvas.drawBitmap
                (player.getBitmap(), player.getX(), player.getY(), paint);
        canvas.drawBitmap
                (enemy1.getBitmap(),
                        enemy1.getX(),
                        enemy1.getY(), paint);
        canvas.drawBitmap
                (enemy2.getBitmap(),
                        enemy2.getX(),
                        enemy2.getY(), paint);
        canvas.drawBitmap
                (enemy3.getBitmap(),
                        enemy3.getX(),
                        enemy3.getY(), paint);

        canvas.drawBitmap(
                player.getBitmap(),
                player.getX(),
                player.getY(),
                paint);
        // Unlock and draw the scene
        ourHolder.unlockCanvasAndPost(canvas);

    }

    }
    private void control(){
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        // There are many different events in MotionEvent
        // We care about just 2 - for now.


        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Has the player lifted their finger up?
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            // Has the player touched the screen?
            case MotionEvent.ACTION_DOWN:
              player.setBoosting();
                break; }
        return true;
    }
}
