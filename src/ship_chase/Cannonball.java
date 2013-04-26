package ship_chase;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Cannonball {
    Image texture;
    int player;
    float scale;
    float x;
    float y;
    
    /**
     * Genera una bala de cañon dado el jugador que disparon y sus datos de inicio
     * @param player el jugador que dispara
     * @param x posicion de inicio x
     * @param y posicion de inicio y
     * @param r direccion en grados
     */
    public Cannonball(int player, float x, float y, float r) {
        try {
            this.player = player;
            texture = new Image("data/bola"+ String.valueOf(player) +".png");
            texture.setRotation(r);
            scale = 0.05f;
            this.x = x;
            this.y = y;
            
        } catch (SlickException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Escala la textura de la bala
     * @param scale 
     */
    public void scale(Float scale) {
        this.scale = scale;
        texture.setCenterOfRotation(texture.getWidth()/2.0f*scale,
                texture.getHeight()/2.0f*scale);
    }
    
    /**
     * Se autodibuja la bala
     */
    public void draw() {
        texture.draw(x, y, scale);
    }
    
    /**
     * Movimiento de la bala siguiendo su curso establecido
     * @param delta lapso de tiempo de refresco del framework
     */
    public void move(int delta) {
        // movimiento automatico
        float hip = 0;
        if(player == 2) { // la maquina dispara un poquito mas rapido :)
            hip = 0.18f * delta;
        } else {
            hip = 0.10f * delta;
        }
        float rotation = texture.getRotation();
 
        x += hip * Math.sin(Math.toRadians(rotation));
        y -= hip * Math.cos(Math.toRadians(rotation));
    }
}
