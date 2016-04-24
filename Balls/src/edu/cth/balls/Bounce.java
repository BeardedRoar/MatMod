package edu.cth.balls;

import java.awt.*;

public class Bounce extends Animation {

    protected double X, Y, Vx, Vy, deltaT, pixelsPerMeter;
    protected int radius, firstTime=1, pixelX, pixelY;
    protected Color color = Color.red;
    private Ball ball;

    protected void initAnimator() {
        deltaT=0.005; // simulation time interval in seconds
        setDelay((int)(1000*deltaT)); // needed for Animation superclass
        this.ball = new Ball(d, 3, 3, 2, -1.3, 40, 25, Color.RED);
    }

    protected void paintAnimator(Graphics g) {
        ball.tick(deltaT);
        g.setColor(Color.white);
        //if(firstTime==1) {g.fillRect(0,0,d.width,d.height); firstTime=0;}
        g.fillRect(0,0,d.width,d.height); // slower?

        ball.draw(g);
    }

}

