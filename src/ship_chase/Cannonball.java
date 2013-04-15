package ship_chase;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Cannonball {
    Image texture;
    int player;
    float rAngle;
    float scale;
    float x;
    float y;
    
    public Cannonball(int player, float x, float y, float r) {
        try {
            this.player = player;
            rAngle = r;
            texture = new Image("data/bola"+ String.valueOf(player) +".png");
            texture.rotate(rAngle);
            scale = 0.05f;
            this.x = x;
            this.y = y;
            
        } catch (SlickException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void scale(Float scale) {
        this.scale = scale;
        texture.setCenterOfRotation(texture.getWidth()/2.0f*scale,
                texture.getHeight()/2.0f*scale);
    }
    
    public void draw() {
        texture.draw(x, y, scale);
    }
    
    public void move(int delta) {
        // movimiento automatico
        float hip = 0.30f * delta;
        float rotation = texture.getRotation();
 
        x += hip * Math.sin(Math.toRadians(rotation));
        y -= hip * Math.cos(Math.toRadians(rotation));
    }
}
