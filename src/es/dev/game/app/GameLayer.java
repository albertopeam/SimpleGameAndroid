package es.dev.game.app;

import java.util.Random;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class GameLayer extends CCColorLayer implements SensorEventListener{

    private Sensor mAccelerometer = null;
    private static CCSprite player;
    private static CGPoint point;
    private static CGSize winSize;
	
	protected GameLayer(ccColor4B color,Sensor mAccelerometer) {
		super(color);
		/*
		 * 
		 */
        this.mAccelerometer = mAccelerometer;
        this.registerWithAccelerometer();
        
		winSize = CCDirector.sharedDirector().displaySize();
		player = CCSprite.sprite("player.png");
		 
		point = CGPoint.ccp(winSize.width / 2.0f, winSize.height / 2.0f);
		player.setPosition(point);
		 
		addChild(player);
		
		this.schedule("gameLogic", 0.5f);
	}

	/**
	 * Creates a new scene 
	 * @return
	 */
	public static CCScene scene(Sensor mAccelerometer){
		CCScene scene = CCScene.node();
		CCColorLayer layer = new GameLayer(ccColor4B.ccc4(255, 255, 255, 255), mAccelerometer);

		scene.addChild(layer);
		
		return scene;
	}
	
	/**
	 * Adds a new object to the scene
	 */
	protected void addTarget(){
		Random rand = new Random();
		CCSprite target = CCSprite.sprite("target.png");
		
		//Determine where to spawn the target along the y axis
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		int minY = (int)(target.getContentSize().height /2.0f);
		int maxY = (int)(winSize.height - target.getContentSize().height / 2.0f);
	    int rangeY = maxY - minY;
	    int actualY = rand.nextInt(rangeY) + minY;
	 
	    // Create the target slightly off-screen along the right edge,
	    // and along a random position along the Y axis as calculated above
	    CGPoint pos = new CGPoint();
	    pos.x = winSize.width + (target.getContentSize().width / 2.0f);
	    pos.y = actualY;
	    target.setPosition(pos);
	    addChild(target);
	 
	    // Determine speed of the target
	    int minDuration = 2;
	    int maxDuration = 4;
	    int rangeDuration = maxDuration - minDuration;
	    int actualDuration = rand.nextInt(rangeDuration) + minDuration;
	 
	    // Create the actions
	    CCMoveTo actionMove = CCMoveTo.action(actualDuration, CGPoint.ccp(-target.getContentSize().width / 2.0f, actualY));
	    CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "spriteMoveFinished");
	    CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);
	 
	    target.runAction(actions);
	}
	
	/**
	 * 
	 * @param sender
	 */
	public void spriteMoveFinished(Object sender)
	{
	    CCSprite sprite = (CCSprite)sender;
	    this.removeChild(sprite, true);
	}
	
	/**
	 * 
	 * @param dt
	 */
	public void gameLogic(float dt)
	{
	    addTarget();
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		System.out.println("ON ACCURACY CHANGED");
    }

	@Override
    public void onSensorChanged(SensorEvent event) {
		System.out.println("ON SENSOR CHANGED");
		System.out.println("wi"+winSize.width);
		System.out.println("He"+winSize.height);
		System.out.println("x"+point.x);
		System.out.println("y"+point.y);

		
		if (point.x >= 30.0f && point.x <= (winSize.width - 30)){
			point.x += event.values[1] / 1.2f;
		}else{
			if ( point.x <= 30.0f )
				point.x = 30.0f;
			else
				point.x = winSize.width - 30;
		}
		if (point.y >= 30.0f && point.y <= (winSize.height - 30)){
			point.y -= event.values[0] / 1.2f;
		}else {
			if ( point.y <= 30.0f )
				point.y = 30.0f;
			else
				point.y = winSize.height - 30;
		}
			
		player.setPosition(point);
    }
	
	
	@Override
	public void onExit() {
		super.onExit();
		this.unregisterWithAccelerometer();
	}

}
