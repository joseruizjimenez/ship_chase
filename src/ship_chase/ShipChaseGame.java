package ship_chase;

// -Djava.library.path=data/lwjgl-2.8.5/native/linux:/usr/lib/swipl-jpl
// -Djava.library.path=C:\lwjgl-2.8.5\native\windows;C:\ ... (buscar swipl-jpl)

import java.util.ArrayList;
import java.util.Timer;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.tiled.TiledMap;

/**
 * Juego de barcos para un jugador enfrentado a la maquina que implementa una
 * IA. Cada jugador dispone de 5 vidas. Los movimientos son A y D para virar. Se
 * dispara por babor con J y por estribor con K.
 *
 * @author Pablo Calleja - Jose Ruiz
 */
public class ShipChaseGame extends BasicGame {

    Ship playerShip;
    Ship aIShip;
    ArrayList<Cannonball> cannonballs;
    ArrayList<Cannonball> cannonballsToRemove;
    int gunLimiter = 0;
    int aIGunLimiter = 0;
    Image land = null;
    private TiledMap seaMap;
    Timer frames = new Timer();

    /**
     * Instanciamos un juego del framework
     */
    public ShipChaseGame() {
        super("Ship Chase Game");
    }

    public static void main(String[] arguments) {
        try {
            AppGameContainer app = new AppGameContainer(new ShipChaseGame());
            app.setTargetFrameRate(60);
            app.setDisplayMode(800, 600, false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo de inicializacion
     * @param container
     * @throws SlickException 
     */
    @Override
    public void init(GameContainer container) throws SlickException {
        playerShip = new Ship(1);
        aIShip = new Ship(2);
        cannonballs = new ArrayList<>();
        cannonballsToRemove = new ArrayList<>();
        land = new Image("data/sea2.png");
        seaMap = new TiledMap("data/sea.tmx");
    }

    /**
     * Actualizacion de la logica del juego y sus componentes
     * @param container
     * @param delta
     * @throws SlickException 
     */
    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        Input input = container.getInput();

        if (input.isKeyDown(Input.KEY_A)) {
            playerShip.rotate(-0.1f * delta);
        }

        if (input.isKeyDown(Input.KEY_D)) {
            playerShip.rotate(0.1f * delta);
        }

        if (input.isKeyDown(Input.KEY_2)) {
            playerShip.scale += (playerShip.scale >= 5.0f) ? 0 : 0.1f;
            playerShip.scale(playerShip.scale);
        }
        if (input.isKeyDown(Input.KEY_1)) {
            playerShip.scale -= (playerShip.scale <= 1.0f) ? 0 : 0.1f;
            playerShip.scale(playerShip.scale);
        }

        if (input.isKeyDown(Input.KEY_J)) {
            if (gunLimiter > 50) {
                gunLimiter = 0;
                cannonballs.add(new Cannonball(1,
                        playerShip.cX,
                        playerShip.cY,
                        playerShip.texture.getRotation() - 60));
            }
        }

        if (input.isKeyDown(Input.KEY_K)) {
            if (gunLimiter > 50) {
                gunLimiter = 0;
                cannonballs.add(new Cannonball(1,
                        playerShip.cX,
                        playerShip.cY,
                        playerShip.texture.getRotation() + 60));
            }
        }

        // Disparos de babor de la maquina
        int shoot = aIShip.shouldShoot(playerShip);
        if (shoot == 1) {
            if (aIGunLimiter > 100) {
                System.out.println("AI SHOOT BABOR");
                aIGunLimiter = 0;
                cannonballs.add(new Cannonball(2,
                        aIShip.cX,
                        aIShip.cY,
                        aIShip.texture.getRotation() - 80));
            }
        }

        // Disparos de estribor de la maquina
        if (shoot == 2) {
            if (aIGunLimiter > 100) {
                System.out.println("AI SHOOT ESTRIBOR");
                aIGunLimiter = 0;
                cannonballs.add(new Cannonball(2,
                        aIShip.cX,
                        aIShip.cY,
                        aIShip.texture.getRotation() + 80));
            }
        }

        playerShip.navigate(delta, true);
        aIShip.aINavigate(delta, playerShip);

        for (Cannonball c : cannonballs) {
            if (c.player == 2 && playerShip.gotHitted(c)) {
                cannonballsToRemove.add(c);
                playerShip.life--;
                System.out.println("YOU GOT HITTED!");
            } else if (c.player == 1 && aIShip.gotHitted(c)) {
                cannonballsToRemove.add(c);
                aIShip.life--;
                System.out.println("THE ENEMY GOT HITTED!");
            } else {
                if (c.x > 800 || c.x < 0 || c.y > 600 || c.y < 0) {
                    cannonballsToRemove.add(c);
                } else {
                    c.move(delta);
                }
            }
        }
        for (Cannonball c : cannonballsToRemove) {
            cannonballs.remove(c);
        }

        cannonballsToRemove.clear();
        aIGunLimiter++;
        gunLimiter++;

    }

    /**
     * Metodo para renderizar los componentes del juego por pantalla
     * @param container
     * @param g
     * @throws SlickException 
     */
    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        land.draw(0, 0);
        seaMap.render(0, 0);
        g.setAntiAlias(true);
        g.setColor(Color.pink);
        g.setLineWidth(1.5f);
        if (aIShip.life <= 0) {

            g.drawString("YOU WIN!", 350f, 270f);
        } else if (playerShip.life <= 0) {
            g.drawString("YOU LOOSE!", 350f, 270f);
        } else {
            playerShip.draw();
            aIShip.draw();
            g.drawString("YOUR LIFE: " + playerShip.life, 650f, 10f);
            g.drawString("ENEMY'S LIFE: " + aIShip.life, 650f, 25f);

            for (Cannonball c : cannonballs) {
                c.draw();
            }

            if (aIShip.fireLineA != null && aIShip.fireLineB != null) {
                //g.draw(aIShip.fireLineA);
                //g.draw(aIShip.fireLineB);
            }
        }
    }
}