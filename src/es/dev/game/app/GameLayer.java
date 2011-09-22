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

public class GameLayer extends CCColorLayer{

	
	protected GameLayer(ccColor4B color) {
		super(color);
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCSprite player = CCSprite.sprite("player.png");
		 
		player.setPosition(CGPoint.ccp(player.getContentSize().width / 2.0f, winSize.height / 2.0f));
		 
		addChild(player);
		
		this.schedule("gameLogic", 1.0f);
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

}
