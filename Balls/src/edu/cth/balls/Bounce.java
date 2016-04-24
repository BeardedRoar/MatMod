package edu.cth.balls;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Bounce extends Animation {

    protected double deltaT;
    private List<Ball> balls = new ArrayList();

    protected void initAnimator() {
        deltaT=0.005; // simulation time interval in seconds
        setDelay((int)(1000*deltaT)); // needed for Animation superclass
        Ball b1 = new Ball(d, 2, 2, 0, 0, 40, 25, Color.RED);
        Ball b2 = new Ball(d, 4, 2, 0, 0, 40, 45, Color.BLUE);
        balls.add(b1);
        balls.add(b2);
    }

    protected void paintAnimator(Graphics g) {
        for (Ball b : balls) {
            b.tick(deltaT);
        }
        g.setColor(Color.white);
        //if(firstTime==1) {g.fillRect(0,0,d.width,d.height); firstTime=0;}
        g.fillRect(0,0,d.width,d.height); // slower?
        for (Ball b : balls) {
            b.draw(g);
        }
    }

}

