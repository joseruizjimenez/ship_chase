package ship_chase;

import java.util.Hashtable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import jpl.Atom;
import jpl.Query;
import jpl.Term;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Ship {
    Image texture;
    Query query;
    float rAngle;
    float scale;
    float x;
    float y;
    int life;
    
    public Ship(int player) {
        try {
            query = new Query("consult('ship_chase.pl')");
            System.out.println( (query.hasSolution() ? "Prolog file loaded" : "Prolog file failed") );
            Random randomGenerator = new Random();
            rAngle = randomGenerator.nextFloat()*100f;
            texture = new Image("data/barco"+ String.valueOf(player) +".png");
            texture.rotate(rAngle);
            scale = 1;
            life = 5;
            x = 300 + randomGenerator.nextInt(200);
            y = 200 + randomGenerator.nextInt(200);
            
        } catch (SlickException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void rotate(float angle) {
        this.rAngle = angle;
        texture.rotate(angle);
    }
    
    public void scale(Float scale) {
        this.scale = scale;
        texture.setCenterOfRotation(texture.getWidth()/2.0f*scale,
                texture.getHeight()/2.0f*scale);
    }
    
    public void draw() {
        texture.draw(x, y, scale);
    }
    
    public void navigate(int delta) {
        float hip = 0.06f * delta;
        float rotation = texture.getRotation();
 
        x += hip * Math.sin(Math.toRadians(rotation));
        y -= hip * Math.cos(Math.toRadians(rotation));
    }
    
    public void aINavigate(int delta, Ship rival) {
        float dist = (float) Math.sqrt(Math.pow(rival.x-x, 2) + Math.pow(rival.y-y, 2));
        if(queryProlog("danger("+getDirectionSector(texture.getRotation())+", "+
                getDirectionSector(rival.texture.getRotation())+", "+dist+")") ||
                queryProlog("wall("+x+", "+y+")")) { // in danger, need to evade
            this.rotate(0.2f * delta);
        } else { // try to face the player
            //this.rotate(0.2f * delta);
        }
        this.navigate(delta);
    }
    
    public boolean gotHitted(Cannonball c) {
        float dist = (float) Math.sqrt(Math.pow(c.x-(x + texture.getWidth() / 2), 2)+
                Math.pow(c.y-(y + texture.getHeight() / 2), 2));
        return queryProlog("hit("+dist+")");
    }
    
    public boolean shouldShootLarboard(Ship rival) {
        float dist = (float) Math.sqrt(Math.pow(rival.x-x, 2) + Math.pow(rival.y-y, 2));
        return queryProlog("shootLarboard("+getDirectionSector(texture.getRotation())+", "+
                getDirectionSector(rival.texture.getRotation())+", "+dist+")");
    }
    
    public boolean shouldShootStarboard(Ship rival) {
        float dist = (float) Math.sqrt(Math.pow(rival.x-x, 2) + Math.pow(rival.y-y, 2));
        return queryProlog("shootStarboard("+getDirectionSector(texture.getRotation())+", "+
                getDirectionSector(rival.texture.getRotation())+", "+dist+")");
    }
    
    public boolean queryProlog(String q) {
        query = new Query(q);
        return query.hasSolution();
        
    /*
        Term a = new Atom("a");
        Term b = new Atom("b");

        query = new Query(q);

        if (!query.hasSolution()) {
            return null;
        }

        Term[] x_target = new Term[]{a, a};
        Term[] y_target = new Term[]{a, b};

        return query.allSolutions();
        * /

        /*if ( solutions.length != 2 ){
         * System.out.println( "p(X, Y) failed:" );
         * System.out.println( "\tExpected: 2 solutions" );
         * System.out.println( "\tGot:      " + solutions.length );
         * System.exit( 1 );
         * }
         * 
         * for ( int i = 0;  i < solutions.length;  ++i ){
         * Object x_binding = solutions[i].get("X");
         * if ( ! x_binding.equals( x_target[i] ) ){
         * System.out.println( "p(X, Y) failed:" );
         * System.out.println( "\tExpected: " + x_target[i] );
         * System.out.println( "\tGot:      " + x_binding );
         * System.exit( 1 );
         * }
         * Object y_binding = solutions[i].get("Y");
         * if ( ! y_binding.equals( y_target[i] ) ){
         * System.out.println( "p( X, Y ) failed:" );
         * System.out.println( "\tExpected: " + y_target[i] );
         * System.out.println( "\tGot:      " + y_binding );
         * System.exit( 1 );
         * }
         * }*/
    }
    
    static public int getDirectionSector(float rotation) {
        int r = (int)(rotation/20);
        if (r < 0) {
            return r + 19;
        } else {
            return r + 1;
        }
    }
}
