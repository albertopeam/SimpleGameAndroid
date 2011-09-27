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

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.SoundEffectConstants;

import static es.dev.game.config.Constants.*;

public class GameLayer extends CCColorLayer implements SensorEventListener{

	private static CCSprite background;
	
    private static CCSprite player;
    private static CGPoint point;
    private static CGSize winSize;
    private static float playerWidth;
    private static float playerHeigth;
	
	protected GameLayer(ccColor4B color) {
		super(color);

        this.registerWithAccelerometer();
        
		winSize = CCDirector.sharedDirector().displaySize();
		player = CCSprite.sprite("player.png");
		
		playerWidth = player.getContentSize().width;
		playerHeigth = player.getContentSize().height; 
		 
		point = CGPoint.ccp(winSize.width / 2.0f, winSize.height / 2.0f);
		player.setPosition(point);
		 
		addChild(player,1);
		
		/*Adds a background to the scene*/
		background = CCSprite.sprite("background.png");		//TODO: puede ser en 2D!!!
		background.setPosition(CGPoint.ccp(winSize.width / 2.0f, winSize.height / 2.0f));
		addChild(background, 0);
		/*
		 * Sound effects
		 */
		Context context = CCDirector.sharedDirector().getActivity();
		//SoundEngine.sharedEngine().playSound(context, R.raw.background_music_aac, true);
		
		
		/*
		 * adds a call to the gameLogic method every 1/2 second
		 */
		this.schedule("gameLogic", 0.5f);
	}

	/**
	 * Creates a new scene 
	 * @return
	 */
	public static CCScene scene(){
		CCScene scene = CCScene.node();
		CCColorLayer layer = new GameLayer(ccColor4B.ccc4(255, 255, 255, 255));

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
	 * Destroys a sprite when it comes over the screen
	 * @param sender
	 */
	public void spriteMoveFinished(Object sender)
	{
	    CCSprite sprite = (CCSprite)sender;
	    this.removeChild(sprite, true);
	}
	
	/**
	 * Adds the game logic to plays against
	 * @param dt
	 */
	public void gameLogic(float dt)
	{
	    addTarget();
	}
	
	/**
	 * Not implemented
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		if (DEBUG)
			System.out.println("GameLayer.Class,  OnAccuracyChanged");    }

	/**
	 * Manage Accelerometer to gameLogic
	 */
	@Override
    public void onSensorChanged(SensorEvent event) {
		if (DEBUG)
			System.out.println("GameLayer.Class,  OnSensorChanged");
		if (point.x >= playerWidth && point.x <= (winSize.width - playerWidth)){			//Dentro de las posiciones validas del eje X
			point.x += event.values[AxisX];
		}else{
			if ( point.x < playerWidth && point.x + event.values[AxisX] >= point.x){		//Retorno de minima pos eje X
				point.x += event.values[AxisX];
			}else if ( point.x > (winSize.width - playerWidth) && point.x + event.values[AxisX] <= point.x ){		//Retorno de max. pos eje y
				point.x += event.values[AxisX];
			}
		}
		
		if (point.y >= playerHeigth && point.y <= (winSize.height - playerHeigth)){			//Dentro de las posiciones validas del eje X
			point.y -= event.values[AxisY];
		}else {
			if ( point.y < playerHeigth && point.y - event.values[AxisY] >= point.y){		//Retorno de minima pos eje Y
				point.y -= event.values[AxisY];
			}else if ( point.y > (winSize.height - playerHeigth) && point.y - event.values[AxisY] <= point.y ){		//Retorno de max. pos eje y
				point.y -= event.values[AxisY];
			}
		}
		player.setPosition(point);
    }
	
	/**
	 * Updates the IU calculating the collisions
	 * @param dt
	 */
	public void update(float dt)
	{
		/*
	    ArrayList<CCSprite> projectilesToDelete = new ArrayList<CCSprite>();
	 
	    for (CCSprite projectile : _projectiles)
	    {
	        CGRect projectileRect = CGRect.make(projectile.getPosition().x - (projectile.getContentSize().width / 2.0f),
	                                            projectile.getPosition().y - (projectile.getContentSize().height / 2.0f),
	                                            projectile.getContentSize().width,
	                                            projectile.getContentSize().height);
	 
	        ArrayList<CCSprite> targetsToDelete = new ArrayList<CCSprite>();
	 
	        for (CCSprite target : _targets)
	        {
	            CGRect targetRect = CGRect.make(target.getPosition().x - (target.getContentSize().width),
	                                            target.getPosition().y - (target.getContentSize().height),
	                                            target.getContentSize().width,
	                                            target.getContentSize().height);
	 
	            if (CGRect.intersects(projectileRect, targetRect))
	                targetsToDelete.add(target);
	        }
	 
	        for (CCSprite target : targetsToDelete)
	        {
	            _targets.remove(target);
	            removeChild(target, true);
	        }
	 
	        if (targetsToDelete.size() > 0)
	            projectilesToDelete.add(projectile);
	    }
	 
	    for (CCSprite projectile : projectilesToDelete)
	    {
	        _projectiles.remove(projectile);
	        removeChild(projectile, true);
	    }
	    */
	}

	
	/**
	 * Called when go out from this layer
	 */
	@Override
	public void onExit() {
		super.onExit();
		this.unregisterWithAccelerometer();
	}

}
