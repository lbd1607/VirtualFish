package com.missouristate.davis916.virtualfish;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Laura Davis CIS 262-902
 * This app simulates the life of a fish in
 * a fish tank. When the fish is hungry, it
 * searches for food and eats. When the fish is
 * full, it swims around to burn off a few
 * calories before its next meal.
 */

public class MainActivity extends Activity {
    //Animation is split into two threads: calculating movement,
    //and fish tank updates (on the UI thread)
    private Thread calculateMovementThread;

    //Fish tank elements and properties
    private ImageView fishImageView;
    private Fish mFish;
    private int tankWidth, tankHeight;
    private FrameLayout fishTankLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Set layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create references to the frame layout container
        fishTankLayout = (FrameLayout) findViewById(R.id.container);

        //Get the dimensions of the screen to use for tank size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        tankWidth = size.x;
        tankHeight = size.y;

        //Instantiate a fish object
        int initialXPosition = 0;
        int initialYPosition = 0;
        mFish = new Fish(initialXPosition, initialYPosition,
                Fish.IsSwimming, tankWidth, tankHeight);

        //Build the tank elements
        buildTank();

        //Construct the thread to calculate movement and animate movement
        calculateMovementThread = new Thread(calculateMovement);

        //Start the thread
        calculateMovementThread.start();
    }//end onCreate()

    private void buildTank(){
        //Create layout inflater to add visual views to layout
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Add the foliage
        ImageView foliageImageView = (ImageView) layoutInflater.inflate(R.layout.foliage_layout, null);
        foliageImageView.setScaleX((float) 1.0);
        foliageImageView.setScaleY((float) 1.0);
        foliageImageView.setAlpha((float) 0.97);
        fishTankLayout.addView(foliageImageView, 0);

        //Add the virtual fish
        fishImageView = (ImageView) layoutInflater.inflate(R.layout.fish_image, null);
        fishImageView.setScaleX((float) 0.3);
        fishImageView.setScaleY((float) 0.3);
        fishImageView.setX(mFish.x);
        fishImageView.setY(mFish.y);
        fishTankLayout.addView(fishImageView, 0);
    }//end buildTank()

    /************************* Runnable **********************/
    private Runnable calculateMovement = new Runnable() {
        private static final int DELAY = 200;
        public void run() {
            try{
                while(true){
                    mFish.move();
                    Thread.sleep(DELAY);
                    updateTankHandler.sendEmptyMessage(0);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }//end run()
    };//end Runnable *********************************************

    /*************************** Handler *************************/
    public Handler updateTankHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            //Face the fish in the correct direction
            fishImageView.setScaleX((float) (0.3 * mFish.getFacingDirection()));

            //Set the fish at the correct x,y location
            fishImageView.setX((float) mFish.x);
            fishImageView.setY((float) mFish.y);
        }//end handleMessage()
    };//end Handler ************************************************

    //Menu stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }//end createOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle action bar item clicks here. The action bar will
        //automatically handle clicks on the Home/Up button,
        //as long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }//end onOptionsItemSelected

}//end MainActivity class
