package edu.cth.balls;

import java.awt.*;

public class Bounce extends Animation {

    protected double deltaT;
    private World world;

    protected void initAnimator() {
        deltaT=0.005; // simulation time interval in seconds
        setDelay((int)(1000*deltaT)); // needed for Animation superclass
        Ball b1 = new Ball(d, 1, 4, 2, 0, 40, 15, Color.RED);
        Ball b2 = new Ball(d, 4, 5, -5, 0, 40, 20, Color.BLUE);
        Ball b3 = new Ball(d, 6, 3, -2, 0, 40, 25, Color.GREEN);
        Ball b4 = new Ball(d, 2, 4, 2, 0, 40, 18, Color.ORANGE);
        Ball b5 = new Ball(d, 3, 3, -5, 0, 40, 23, Color.CYAN);
        Ball b6 = new Ball(d, 5, 2, -1, 0, 40, 28, Color.YELLOW);

        world = new World();
        world.addBall(b1);
        world.addBall(b2);
        world.addBall(b3);

        world.addBall(b4);
        world.addBall(b5);
        world.addBall(b6);
    }

    protected void paintAnimator(Graphics g) {
        world.tick(deltaT);
        g.setColor(Color.white);
        //if(firstTime==1) {g.fillRect(0,0,d.width,d.height); firstTime=0;}
        g.fillRect(0,0,d.width,d.height); // slower?
        for (Ball b : world.getBalls()) {
            b.draw(g);
        }
    }

}

