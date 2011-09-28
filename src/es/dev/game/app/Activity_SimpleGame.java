package es.dev.game.app;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.sound.*;

import es.dev.game.layer.GameLayer;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Activity_SimpleGame extends Activity {
	
	protected CCGLSurfaceView _glSurfaceView;
    private boolean _soundPlaying = false;
    private boolean _soundPaused = false;
    private boolean _resumeSound = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CCDirector.sharedDirector().getActivity();

        // Preload background music
        SoundEngine.sharedEngine().preloadSound(this, R.raw.opening_song);
        
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
    	CCScene scene = GameLayer.scene();
    	CCDirector.sharedDirector().runWithScene(scene);
    	
        SoundEngine.sharedEngine().playSound(this, R.raw.opening_song, true);
        _soundPlaying = true;
    }
    
    /** Called when the activity goes to second plane */
    @Override
    protected void onPause() {
    	super.onPause();
    	CCDirector.sharedDirector().pause();
    	

        // If the sound is loaded and not paused, pause it - but flag that we want it resumed
        if (_soundPlaying && !_soundPaused)
        {
            SoundEngine.sharedEngine().pauseSound();
            _soundPaused = true;
            _resumeSound = true;
        }
        else
            _resumeSound = false; // No sound playing, don't resume
    }
    
    /** Called before onStart*/
    @Override
    protected void onResume() {
    	super.onResume();    	
    	CCDirector.sharedDirector().resume();
    	
        // Resume playing sound only if it's loaded, paused and we want to resume it
        if (_soundPlaying && _soundPaused && _resumeSound)
        {
            SoundEngine.sharedEngine().resumeSound();
            _soundPaused = false;
        }
    }
    /** Called when the activity is stopped*/
    @Override
    protected void onStop() {
    	super.onStop();
    	CCDirector.sharedDirector().end();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();

        // Clean everything up
        SoundEngine.sharedEngine().realesAllSounds();
        SoundEngine.sharedEngine().realesAllEffects();

        // Completely shut down the sound system
        SoundEngine.purgeSharedEngine();
    }
}
