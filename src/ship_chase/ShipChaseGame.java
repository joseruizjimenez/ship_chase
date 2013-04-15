package ship_chase;

// -Djava.library.path=data/lwjgl-2.8.5/native/linux
// -Djava.library.path=C:\lwjgl-2.8.5\native\windows

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Animation; 
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap; 

import jpl.Query;				// empirically, we need this, but I don't know why...
import jpl.*;


/**
 *
 * @author Pablo Calleja - Jose Ruiz
 */ 
public class ShipChaseGame extends BasicGame
{
    Ship playerShip;
    Ship aIShip;
    ArrayList<Cannonball> cannonballs;
    ArrayList<Cannonball> cannonballsToRemove;
    int gunLimiter = 0;
    int aIGunLimiter = 0;
    
    Image land = null;
    private TiledMap seaMap;

    Timer frames = new Timer();
    
     public ShipChaseGame()
     {
         super("Ship Chase Game");
     }

     public static void main(String [] arguments)
     {
         try
         {
             AppGameContainer app = new AppGameContainer(new ShipChaseGame());
             app.setDisplayMode(800, 600, false);
             app.start();
         }
         catch (SlickException e)
         {
             e.printStackTrace();
         }
     }

     @Override
     public void init(GameContainer container) throws SlickException
     {         
        playerShip = new Ship(1);
        aIShip = new Ship(2);
        cannonballs = new ArrayList<>();
        cannonballsToRemove = new ArrayList<>();
        land = new Image("data/sea2.png");
        seaMap = new TiledMap("data/sea.tmx");
     }

     @Override
     public void update(GameContainer container, int delta) throws SlickException
     {         
        Input input = container.getInput();
 
        if(input.isKeyDown(Input.KEY_A))
        {
            playerShip.rotate(-0.2f * delta);
        }
 
        if(input.isKeyDown(Input.KEY_D))
        {
            playerShip.rotate(0.2f * delta);
        }
        
        if(input.isKeyDown(Input.KEY_2))
        {
            playerShip.scale += (playerShip.scale >= 5.0f) ? 0 : 0.1f;
            playerShip.scale(playerShip.scale);
        }
        if(input.isKeyDown(Input.KEY_1))
        {
            playerShip.scale -= (playerShip.scale <= 1.0f) ? 0 : 0.1f;
            playerShip.scale(playerShip.scale);
        }
        
        if(input.isKeyDown(Input.KEY_J))
        {
            if(gunLimiter > 5) {
                gunLimiter = 0;
                cannonballs.add(new Cannonball(1,
                        playerShip.x + playerShip.texture.getWidth() / 2,
                        playerShip.y + playerShip.texture.getHeight() / 2,
                        playerShip.texture.getRotation() - 90));
            }
        }
        
        if(input.isKeyDown(Input.KEY_K))
        {
            if(gunLimiter > 5) {
                gunLimiter = 0;
                cannonballs.add(new Cannonball(1,
                        playerShip.x + playerShip.texture.getWidth() / 2,
                        playerShip.y + playerShip.texture.getHeight() / 2,
                        playerShip.texture.getRotation() + 90));
            }
        }
        
        // Disparos de babor
        if(false){ //aIShip.shouldShootLarboard(playerShip)) {
            if(aIGunLimiter > 100) {
                System.out.println("AI SHOOT");
                aIGunLimiter = 0;
                cannonballs.add(new Cannonball(2,
                        aIShip.x + aIShip.texture.getWidth() / 2,
                        aIShip.y + aIShip.texture.getHeight() / 2,
                        aIShip.texture.getRotation() - 90));
            }
        }
        
        // Disparos de estribor
        if(false){ //aIShip.shouldShootStarboard(playerShip)) {
            if(aIGunLimiter > 100) {
                System.out.println("AI SHOOT");
                aIGunLimiter = 0;
                cannonballs.add(new Cannonball(2,
                        aIShip.x + aIShip.texture.getWidth() / 2,
                        aIShip.y + aIShip.texture.getHeight() / 2,
                        aIShip.texture.getRotation() + 90));
            }
        }
        
        playerShip.navigate(delta);
        aIShip.aINavigate(delta, playerShip);
        
        for(Cannonball c : cannonballs) {
            if(c.player == 2 && playerShip.gotHitted(c)) {
                cannonballsToRemove.add(c);
                playerShip.life--;
                System.out.println("YOU GOT HITTED!");
            } else if (c.player == 1 && aIShip.gotHitted(c)) {
                cannonballsToRemove.add(c);
                aIShip.life--;
                System.out.println("THE ENEMY GOT HITTED!");
            } else {
                if(c.x > 800 || c.x < 0 || c.y > 600 || c.y < 0) {
                    cannonballsToRemove.add(c);
                } else {
                    c.move(delta);
                }
            }
        }
        for(Cannonball c : cannonballsToRemove) {
            cannonballs.remove(c);
        }
        
        if(aIShip.life <= 0) {
            System.out.println("YOU WIN!");
        } else if(playerShip.life <= 0) {
            System.out.println("YOU LOOSE!");
        }
        
        cannonballsToRemove.clear();
        aIGunLimiter++;
        gunLimiter++;
        //System.out.println("(" + playerShip.texture.getRotation() + ")" + " - " + playerShip.x+" "+playerShip.y);
        
     }

     @Override
     public void render(GameContainer container, Graphics g) throws SlickException
     {
         land.draw(0, 0);
         seaMap.render(0, 0);
         playerShip.draw();
         aIShip.draw();
         
         for(Cannonball c : cannonballs) {
            c.draw();
        }
         
     }

    
     /*
     private boolean isBlocked(float x, float y)
     {
         int xBlock = (int)x / SIZE;
         int yBlock = (int)y / SIZE;
         return blocked[xBlock][yBlock];
     }
     */
 }