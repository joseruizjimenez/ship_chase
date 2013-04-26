package ship_chase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import jpl.Atom;
import jpl.Query;
import jpl.Term;
import jpl.Variable;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Line;

public class Ship {
    Image texture;
    Query query;
    float rAngle;
    float scale;
    float x;
    float y;
    float cX;
    float cY;
    int life;
    int lastRivalDirection = 1;
    Line fireLineA;
    Line fireLineB;
    
    /**
     * Crea un barco indicando su numero de jugador, y le asigna su textura asociada
     * @param player 
     */
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
            cX = (x + texture.getWidth() / 2);
            cY = (y + texture.getHeight() / 2);
            
        } catch (SlickException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Rota el barco dado un angulo en grados
     * @param angle 
     */
    public void rotate(float angle) {
        this.rAngle = angle;
        texture.rotate(angle);
    }
    
    /**
     * Escala la textura del barco
     * @param scale 
     */
    public void scale(Float scale) {
        this.scale = scale;
        texture.setCenterOfRotation(texture.getWidth()/2.0f*scale,
                texture.getHeight()/2.0f*scale);
    }
    
    /**
     * Se autodibuja el barco
     */
    public void draw() {
        texture.draw(x, y, scale);
    }
    
    /**
     * Navegacion del barco siguiendo su rumbo actual
     * @param delta lapso de tiempo de refresco del framework
     * @param limit denota si se han de tener en cuenta los limites del mapa
     */
    public void navigate(int delta, boolean limit) {
        float hip = 0.06f * delta;
        float rotation = texture.getRotation();
        x += hip * Math.sin(Math.toRadians(rotation));
        y -= hip * Math.cos(Math.toRadians(rotation));
        this.setCenter();
        if(limit && (cX<0||cX>800||cY<0||cY>600)) {
            life = 0;
            x = 400;
            y = 300;
            this.setCenter();
        }
    }
    
    /**
     * Navegacion con inteligencia artificial
     * @param delta lapso de tiempo de refresco del framework
     * @param rival barco enemigo
     */
    public void aINavigate(int delta, Ship rival) {
        double mA = Math.tan(Math.toRadians(rival.texture.getRotation() - 10));
        double mB = Math.tan(Math.toRadians(rival.texture.getRotation() + 10));
        
        setFireLines(mA, mB, rival.cX, rival.cY);
        
        // evitamos los muros
        if(queryProlog("wall("+cX+", "+cY+")")) {
            this.rotate(0.3f * delta);
        }
        // evitamos que nos disparen
        else if(onShootingPath(mA, mB, rival.cX, rival.cY, cX, cY)) {
            this.texture.setRotation(rival.texture.getRotation()+180);
            this.rAngle = 180;
        } else { // intentamos apuntar al enemigo si no estamos cerca de un muro
            float dist = (float) Math.sqrt(Math.pow(rival.cX-cX, 2) + Math.pow(rival.cY-cY, 2));
            if(dist > 150 && !queryProlog("nearWall("+cX+", "+cY+")")) {
                this.faceEnemy(delta, rival);
            } // else: sigue recto para alejarse del rival
        }
        this.navigate(delta, false);
    }
    
    /**
     * Comprueba si ha recibido un disparo
     * @param c
     * @return 
     */
    public boolean gotHitted(Cannonball c) {
        float dist = (float) Math.sqrt(Math.pow(c.x-cX, 2)+Math.pow(c.y-cY, 2));
        return queryProlog("hit("+dist+")");
    }
   
    /**
     * Comprueba si se ha de disparar por estribor y babor
     * @param rival barco enemigo
     * @return 1 si babor, 2 si estribor, 0 si el enemigo no esta en rango
     */
    public int shouldShoot(Ship rival) {
        double mA = Math.tan(Math.toRadians(texture.getRotation() - 10));
        double mB = Math.tan(Math.toRadians(texture.getRotation() + 10));
        if(onShootingPath(mA, mB, cX, cY, rival.cX, rival.cY, true)) {
            return 1;
        } else {
            if(onShootingPath(mA, mB, cX, cY, rival.cX, rival.cY, false)) {
                return 2;
            }
        }
        return 0;
    }
    
    /**
     * Consulta la query en nuestra base de conocimientos prolog
     * @param q
     * @return 
     */
    public boolean queryProlog(String q) {
        query = new Query(q);
        return query.hasSolution();
    }
    
    /**
     * Calcula la funcion del coste heuristico empleando nuestra base de
     * conocimientos prolog.
     * @param a
     * @param b
     * @return h
     */
    public int h(int a, int b) {
        if(a > b) {
            int holder = a;
            a = b;
            b = holder;
        } else if(a == b){
            return 0;
        }
        query = new Query("h('"+String.valueOf(a)+"', '"+String.valueOf(b)+"', P)");
        Hashtable solution = query.oneSolution();
        int sol = Integer.parseInt(solution.get("P").toString());
        return sol;
    }
    
    
    /**
     * Calcula el sector equivalente a su rotacion, dividiendo los 360 grados
     * en 18 sectores de 20 grados cada uno
     * @param rotation
     * @return sector
     */
    static public int getDirectionSector(float rotation) {
        int r = (int)(rotation/20);
        if (r < 0) {
            return r + 19;
        } else {
            return r + 1;
        }
    }

    private void setCenter() {
        this.cX = (x + texture.getWidth() / 2);
        this.cY = (y + texture.getHeight() / 2);
    }

    /**
     * Implementacion del algoritmo A* para apuntar al enemigo.
     * Para ello se codifica un mapa a partir de los sectores en los que puede
     * ir rotando el barco, y va modificando el fin de forma dinamica cuando
     * en dicho sector de rotacion se alcanza al enemigo.
     * @param delta
     * @param rival 
     */
    private void faceEnemy(int delta, Ship rival) {
        double mA = Math.tan(Math.toRadians(texture.getRotation() - 10));
        double mB = Math.tan(Math.toRadians(texture.getRotation() + 10));
        
        int startNode = getDirectionSector(this.texture.getRotation());
        int goalNode = translateSector(startNode + 8);
        ArrayList<Integer> closedSet = new ArrayList<>();
        HashMap<Integer, Integer> fScore = new HashMap<>();
        ArrayList<Integer> openSet = new ArrayList<>();
        openSet.add(new Integer(startNode));
        
        int gScore = 99;
        //int hScore = Math.abs(goalNode - startNode);
        int hScore = h(goalNode, startNode);
        fScore.put(startNode, gScore + hScore);
        while(!openSet.isEmpty()) {
            int currentNode = startNode;
            int thisGScore = 0;
            for(Integer thisNode : openSet) {           
                if(onShootingPath(mA, mB, cX, cY, rival.cX, rival.cY)) {
                    thisGScore = 1;
                    goalNode = thisNode; // variacion de fin dinamico
                } else {
                    thisGScore = 5;
                }
                int thisFScore = h(goalNode, thisNode) + thisGScore;
                if(thisFScore < fScore.get(currentNode)) {
                    currentNode = thisNode;
                }
            }
            if(currentNode == goalNode) {
                int move = 18 - startNode;
                int starboardMove = (goalNode + move) % 18;
                int larboardMove = 18 - starboardMove;
                if(starboardMove < larboardMove && starboardMove != 0){
                    this.texture.setRotation((startNode + 1)*10);
                } else if(larboardMove != 18 && larboardMove != 0 && starboardMove != 0){
                    this.texture.setRotation((startNode - 1)*10);
                }
                break;
            } else {
                Integer c = new Integer(currentNode);
                openSet.remove(c);
                closedSet.add(c);
                ArrayList<Integer> neighborNodes = new ArrayList<>();
                neighborNodes.add(new Integer(translateSector(currentNode - 1)));
                neighborNodes.add(new Integer(translateSector(currentNode +1 )));
                for(Integer neighborNode : neighborNodes) {
                    double imA = Math.tan(Math.toRadians(neighborNode*20 - 10));
                    double imB = Math.tan(Math.toRadians(neighborNode*20 + 10));
                    
                    int tentativeGScore = thisGScore + h(currentNode, neighborNode);
                    int neighborGScore;
                    if(onShootingPath(imA, imB, cX, cY, rival.cX, rival.cY)) {
                        neighborGScore = 1;
                    } else {
                        neighborGScore = 5;
                    }
                    if(closedSet.contains(new Integer(neighborNode))) {
                        if(tentativeGScore >= neighborGScore) {
                            continue;
                        }
                    }
                    if(!openSet.contains(new Integer(neighborNode)) ||
                            tentativeGScore < neighborGScore) {
                        neighborGScore = tentativeGScore;
                        fScore.put(neighborNode,
                                neighborGScore + h(goalNode, neighborNode));
                        if(!openSet.contains(neighborNode)) {
                            openSet.add(new Integer(neighborNode));
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Calculamos la triangulacion de disparo
     * @param mA pendiente del limite A
     * @param mB pendiente del limite B
     * @return los vertices del triangulo
     */
    private ArrayList<Double> calculateTriangle(double mA, double mB, double cX, double cY) {
        // limite izq: x = 0
        double yALeft = cY - mA * cX;
        double yBLeft = cY - mB * cX;
        // limite derecho: x = 800
        double yARight = cY + mA * (800 - cX);
        double yBRight = cY + mB * (800 - cX);
        // limite superior: y = 0
        double xATop = cX + (-1 * cY / mA);
        double xBTop = cX + (-1 * cY / mB);
        // limite inferior: y = 600
        double xABottom = cX + ((600 - cY) / mA);
        double xBBottom = cX + ((600 - cY) / mB);
        
        ArrayList<Double> fireLimitAX = new ArrayList<>();
        ArrayList<Double> fireLimitAY = new ArrayList<>();
        ArrayList<Double> fireLimitBX = new ArrayList<>();
        ArrayList<Double> fireLimitBY = new ArrayList<>();
        
        if(yALeft > 0 && yALeft < 600) {
            fireLimitAX.add(0.0);
            fireLimitAY.add(yALeft);
        }
        if(yBLeft > 0 && yBLeft < 600) {
            fireLimitBX.add(0.0);
            fireLimitBY.add(yBLeft);
        }
        if(yARight > 0 && yARight < 600) {
            fireLimitAX.add(800.0);
            fireLimitAY.add(yARight);
        }
        if(yBRight > 0 && yBRight < 600) {
            fireLimitBX.add(800.0);
            fireLimitBY.add(yBRight);
        }
        if(xATop > 0 && xATop < 800) {
            fireLimitAX.add(xATop);
            fireLimitAY.add(0.0);
        }
        if(xBTop > 0 && xBTop < 800) {
            fireLimitBX.add(xBTop);
            fireLimitBY.add(0.0);
        }
        if(xABottom > 0 && xABottom < 800) {
            fireLimitAX.add(xABottom);
            fireLimitAY.add(600.0);
        }
        if(xBBottom > 0 && xBBottom < 800) {
            fireLimitBX.add(xBBottom);
            fireLimitBY.add(600.0);
        }
        // comprobamos la paridad de los puntos
        double diffZero = Math.sqrt(Math.pow(fireLimitAX.get(0)-fireLimitBX.get(0), 2)+
                Math.pow(fireLimitAY.get(0)-fireLimitBY.get(0), 2));
        double diffOne = Math.sqrt(Math.pow(fireLimitAX.get(0)-fireLimitBX.get(1), 2)+
                Math.pow(fireLimitAY.get(0)-fireLimitBY.get(1), 2));
        if(diffOne > diffZero) {
            double holderX = fireLimitAX.get(0);
            double holderY = fireLimitAY.get(0);
            fireLimitAX.set(0, fireLimitAX.get(1));
            fireLimitAY.set(0, fireLimitAY.get(1));
            fireLimitAX.set(1, holderX);
            fireLimitAY.set(1, holderY);
        }
        ArrayList<Double> vertexes = new ArrayList<>();
        vertexes.add(fireLimitAX.get(0));
        vertexes.add(fireLimitAY.get(0));
        vertexes.add(fireLimitBX.get(0));
        vertexes.add(fireLimitBY.get(0));
        vertexes.add(fireLimitAX.get(1));
        vertexes.add(fireLimitAY.get(1));
        vertexes.add(fireLimitBX.get(1));
        vertexes.add(fireLimitBY.get(1));
        
        return vertexes;
    }
    
    /**
     * Comprueba que el punto P se encuentra dentro del triangulo formado por
     * las pendientes mA y mB, con vertice en el punto C.
     * @param mA
     * @param mB
     * @param cX
     * @param cY
     * @param pX
     * @param pY
     * @param larboard indica si se comprueba por babor o estribor
     * @return si esta en la linea de fuego a babor o estribor
     */
    public boolean onShootingPath(double mA, double mB, double cX, double cY, 
            double pX, double pY, boolean larboard) {
        ArrayList<Double> vertexes = calculateTriangle(mA, mB, cX, cY);
        double vAX1 = vertexes.get(0);
        double vAY1 = vertexes.get(1);
        double vBX1 = vertexes.get(2);
        double vBY1 = vertexes.get(3);
        double vAX2 = vertexes.get(4);
        double vAY2 = vertexes.get(5);
        double vBX2 = vertexes.get(6);
        double vBY2 = vertexes.get(7);
        double diffABabor = Math.sqrt(Math.pow(vAX1-pX, 2)+ Math.pow(vAY1-pY, 2));
        double diffBBabor = Math.sqrt(Math.pow(vBX1-pX, 2)+ Math.pow(vBY1-pY, 2));
        double diffAEstribor = Math.sqrt(Math.pow(vAX2-pX, 2)+ Math.pow(vAY2-pY, 2));
        double diffBEstribor = Math.sqrt(Math.pow(vBX2-pX, 2)+ Math.pow(vBY2-pY, 2));
        if(larboard) {
            if((diffABabor + diffBBabor) < (diffAEstribor + diffBEstribor)) {
                return queryProlog("onShootingPath("+ cX +", "+ cY +", "+ vAX1 +", "+
                        vAY1 +", "+ vBX1 +", "+ vBY1 + ", "+ pX +", "+ pY +")");
            } else {
                return queryProlog("onShootingPath("+ cX +", "+ cY +", "+ vAX2 +", "+
                        vAY2 +", "+ vBX2 +", "+ vBY2 + ", "+ pX +", "+ pY +")");
            }
        } else {
            if((diffABabor + diffBBabor) > (diffAEstribor + diffBEstribor)) {
                return queryProlog("onShootingPath("+ cX +", "+ cY +", "+ vAX2 +", "+
                        vAY2 +", "+ vBX2 +", "+ vBY2 + ", "+ pX +", "+ pY +")");
            } else {
                return queryProlog("onShootingPath("+ cX +", "+ cY +", "+ vAX1 +", "+
                        vAY1 +", "+ vBX1 +", "+ vBY1 + ", "+ pX +", "+ pY +")");
            }
        }
    }
    
    /**
     * Comprueba que el punto P se encuentra dentro de los triangulos formados por
     * las pendientes mA y mB, con vertice en el punto C.
     * @param mA
     * @param mB
     * @param cX
     * @param cY
     * @param pX
     * @param pY
     * @return si esta en la linea de fuego
     */
    public boolean onShootingPath(double mA, double mB, double cX, double cY, 
            double pX, double pY) {
        return onShootingPath(mA, mB, cX, cY, pX, pY, true) ||
                onShootingPath(mA, mB, cX, cY, pX, pY, false);
    }
    
    /**
     * Pinta las lineas de fuego del barco
     * @param mA
     * @param mB 
     */
    public void setFireLines(double mA, double mB, double cX, double cY) {
        ArrayList<Double> vertexes = calculateTriangle(mA, mB, cX, cY);
        double vAX1 = vertexes.get(0);
        double vAY1 = vertexes.get(1);
        double vBX1 = vertexes.get(2);
        double vBY1 = vertexes.get(3);
        double vAX2 = vertexes.get(4);
        double vAY2 = vertexes.get(5);
        double vBX2 = vertexes.get(6);
        double vBY2 = vertexes.get(7);
        fireLineA = new Line((float) vAX1, (float) vAY1, (float) vAX2, (float) vAY2);
        fireLineB = new Line((float) vBX1, (float) vBY1, (float) vBX2, (float) vBY2);
    }

    private int translateSector(int sector) {
        if(sector == 18) return 18;
        if(sector == 0) return 18;
        return sector % 18;
    }
    
}
