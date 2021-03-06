
package ship_chase;

import org.newdawn.slick.Animation; 
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap; 

/**
 *
 * @author Pablo Calleja - Jose Ruiz
 */ 
public class ShipChaseGame extends BasicGame
{
    /*
     private TiledMap grassMap;
     private Animation sprite, up, down, left, right;
     private float x = 34f, y = 34f;
     */

     /** The collision map indicating which tiles block movement - generated based on tile properties */
     /*
     private boolean[][] blocked;
     private static final int SIZE = 34;
     */
    
    Image plane = null;
    Image land = null;
    private TiledMap seaMap;
    float x = 400;
    float y = 300;
    float scale = 1;

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
         /*
         Image [] movementUp = {new Image("data/wmg1_bk1.png"), new Image("data/wmg1_bk2.png")};
         Image [] movementDown = {new Image("data/wmg1_fr1.png"), new Image("data/wmg1_fr2.png")};
         Image [] movementLeft = {new Image("data/wmg1_lf1.png"), new Image("data/wmg1_lf2.png")};
         Image [] movementRight = {new Image("data/wmg1_rt1.png"), new Image("data/wmg1_rt2.png")};
         int [] duration = {300, 300};
         grassMap = new TiledMap("data/grassmap.tmx");
         * */

          /*
          * false variable means do not auto update the animation.
          * By setting it to false animation will update only when
          * the user presses a key.
          */
         /*
         up = new Animation(movementUp, duration, false);
         down = new Animation(movementDown, duration, false);
         left = new Animation(movementLeft, duration, false);
         right = new Animation(movementRight, duration, false);
         

         // Original orientation of the sprite. It will look right.
         sprite = right;

         // build a collision map based on tile properties in the TileD map
         blocked = new boolean[grassMap.getWidth()][grassMap.getHeight()];

        for (int xAxis=0;xAxis<grassMap.getWidth(); xAxis++)
        {
             for (int yAxis=0;yAxis<grassMap.getHeight(); yAxis++)
             {
                 int tileID = grassMap.getTileId(xAxis, yAxis, 0);
                 String value = grassMap.getTileProperty(tileID, "blocked", "false");
                 if ("true".equals(value))
                 {
                     blocked[xAxis][yAxis] = true;
                 }
             }
         }
         */
         
        plane = new Image("data/barco2.png");
        land = new Image("data/sea2.png");
        seaMap = new TiledMap("data/sea.tmx");
     }

     @Override
     public void update(GameContainer container, int delta) throws SlickException
     {
         /*
         Input input = container.getInput();
         if (input.isKeyDown(Input.KEY_UP))
         {
             sprite = up;
             if (!isBlocked(x, y - delta * 0.1f))
             {
                 sprite.update(delta);
                 // The lower the delta the slowest the sprite will animate.
                 y -= delta * 0.1f;
             }
         }
         else if (input.isKeyDown(Input.KEY_DOWN))
         {
             sprite = down;
             if (!isBlocked(x, y + SIZE + delta * 0.1f))
             {
                 sprite.update(delta);
                 y += delta * 0.1f;
             }
         }
         else if (input.isKeyDown(Input.KEY_LEFT))
         {
             sprite = left;
             if (!isBlocked(x - delta * 0.1f, y))
             {
                 sprite.update(delta);
                 x -= delta * 0.1f;
             }
         }
         else if (input.isKeyDown(Input.KEY_RIGHT))
         {
             sprite = right;
             if (!isBlocked(x + SIZE + delta * 0.1f, y))
             {
                 sprite.update(delta);
                 x += delta * 0.1f;
             }
         }
         */
         
         Input input = container.getInput();
 
        if(input.isKeyDown(Input.KEY_A))
        {
            plane.rotate(-0.2f * delta);
        }
 
        if(input.isKeyDown(Input.KEY_D))
        {
            plane.rotate(0.2f * delta);
        }
 
        if(input.isKeyDown(Input.KEY_W))
        {
            float hip = 0.4f * delta;
 
            float rotation = plane.getRotation();
 
            x+= hip * Math.sin(Math.toRadians(rotation));
            y-= hip * Math.cos(Math.toRadians(rotation));
        }
 
        if(input.isKeyDown(Input.KEY_2))
        {
            scale += (scale >= 5.0f) ? 0 : 0.1f;
            plane.setCenterOfRotation(plane.getWidth()/2.0f*scale, plane.getHeight()/2.0f*scale);
        }
        if(input.isKeyDown(Input.KEY_1))
        {
            scale -= (scale <= 1.0f) ? 0 : 0.1f;
            plane.setCenterOfRotation(plane.getWidth()/2.0f*scale, plane.getHeight()/2.0f*scale);
        }
     }

     @Override
     public void render(GameContainer container, Graphics g) throws SlickException
     {
         /*
         grassMap.render(0, 0);
         sprite.draw((int)x, (int)y);
         */
         
         land.draw(0, 0);
         plane.draw(x, y, scale);
         seaMap.render(0, 0);
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