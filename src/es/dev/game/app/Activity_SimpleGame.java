package es.dev.game.app;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Activity_SimpleGame extends Activity {
	
	protected CCGLSurfaceView _glSurfaceView;
    private SensorManager mSensorManager = null;
    private Sensor mAccelerometer = null;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * Set up sensors
         */
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        /*
         * This sets up the OpenGL surface for Cocos2D to utilise. 
         * We set some flags to ensure we always have a fullscreen view, then display the view to the user.
         */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        _glSurfaceView = new CCGLSurfaceView(this);
        
        setContentView(_glSurfaceView);
    }
    
    /** Called when the activity is going to be started */
    @Override
    protected void onStart() {
    	super.onStart();
    	/*
    	 * This is the initial setup for Cocos2D. 
    	 * First we tell Cocos2D which surface to render to (the OpenGL surface we set up earlier). 
    	 * We then ask Cocos2D to display the FPS and to run at 60fps. Note that the 60fps is our animation interval, not the framerate of the application itself which is often limited by the device.
    	 */
    	CCDirector.sharedDirector().attachInView(_glSurfaceView);
    	CCDirector.sharedDirector().setDisplayFPS(true);
    	CCDirector.sharedDirector().setAnimationInterval(1.0f / 60.0f);
    	
    	/*
    	 * creates the scene with the objects
    	 */
    	CCScene scene = GameLayer.scene(mAccelerometer);
    	CCDirector.sharedDirector().runWithScene(scene);
    }
    
    /** Called when the activity goes to second plane */
    @Override
    protected void onPause() {
    	super.onPause();
    	CCDirector.sharedDirector().pause();
    }
    
    /** Called before onStart*/
    @Override
    protected void onResume() {
    	super.onResume();    	
    	CCDirector.sharedDirector().resume();
    }
    /** Called when the activity is stopped*/
    @Override
    protected void onStop() {
    	super.onStop();
    	CCDirector.sharedDirector().end();
    }
}