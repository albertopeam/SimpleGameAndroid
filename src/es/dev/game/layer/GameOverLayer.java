package es.dev.game.layer;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.view.MotionEvent;


public class GameOverLayer extends CCColorLayer
{
    protected CCLabel GameOverLabel;
    protected CCMenuItem endButton;
    protected CCMenuItem restartGameButton;
 
    public static CCScene scene(String message)
    {
        CCScene scene = CCScene.node();
        GameOverLayer layer = new GameOverLayer(ccColor4B.ccc4(0, 0, 0, 0));
 
        layer.getLabel().setString(message);
 
        scene.addChild(layer);
 
        return scene;
    }
 
    public CCLabel getLabel()
    {
        return GameOverLabel;
    }
 
    protected GameOverLayer(ccColor4B color)
    {
        super(color);
 
        this.setIsTouchEnabled(true);
 
        CGSize winSize = CCDirector.sharedDirector().displaySize();
 
        GameOverLabel = CCLabel.makeLabel("Won't See Me", "DroidSans", 32);
        GameOverLabel.setColor(ccColor3B.ccWHITE);
        GameOverLabel.setPosition(winSize.width / 2.0f, winSize.height / 2.0f);
        addChild(GameOverLabel);
        
        /*TODO:
        endButton = new CCMenuItem(rec, cb)
        GameOverLabel.setColor(ccColor3B.ccWHITE);
        GameOverLabel.setPosition(winSize.width / 2.0f, winSize.height / 2.0f);
        addChild(GameOverLabel);
        */
        
 
        //this.runAction(CCSequence.actions(CCDelayTime.action(3.0f), CCCallFunc.action(this, "gameOverDone")));
    }
 
    public void gameOverDone()
    {
        //CCDirector.sharedDirector().replaceScene(GameLayer.scene());
    }
 
    @Override
    public boolean ccTouchesEnded(MotionEvent event)
    {
        gameOverDone();
 
        return true;
    }
}