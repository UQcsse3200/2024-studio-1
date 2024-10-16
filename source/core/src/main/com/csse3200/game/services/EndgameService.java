package com.csse3200.game.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.GdxGame;


/**
 * Service for loading resources, e.g. textures, texture atlases, sounds, music, etc. Add new load
 * methods when new types of resources are added to the game.
 */
public class EndgameService {
  private GdxGame game;
  private boolean shouldEnd = false; 


  public EndgameService(GdxGame game) {
    this.game = game;
  }
  public void flagEnd(){
        this.shouldEnd = true;
  } 
  public boolean shouldEnd(){
    return this.shouldEnd;}
  public void winGame(){
    if(shouldEnd){
            game.setScreen(GdxGame.ScreenType.WIN); 
    }
  }
}
