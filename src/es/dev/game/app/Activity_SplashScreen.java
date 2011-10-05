package es.dev.game.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Activity_SplashScreen extends Activity{
	
	protected Context context;
	protected Boolean screenPressed = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		
		context = this;
		
		Thread splashScreen = new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					while(screenPressed);//infinite while to touch to start game
				} catch (Exception e) {
					e.getMessage();
				} finally{
					finish();
					startActivity(new Intent(context, Activity_SimpleGame.class));
				}
			}
		};
		splashScreen.start();
	}

	/**
	 * Launched when the image in the screen is ckicked
	 * @param v
	 */
	public void processClick(View v){
		screenPressed = false;
	}

}
