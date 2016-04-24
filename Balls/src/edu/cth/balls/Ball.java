package edu.cth.balls;

import java.awt.*;

/**
 * Created by Gaku on 2016-04-24.
 */
public class Ball {

    private double X, Y, Vx, Vy, pixelsPerMeter;
    private int radius, pixelX, pixelY;
    private Color color = Color.red;
    private Dimension d;

    public Ball(Dimension d){
        this.d = d;
        X = 3; // in meters
        Y = 3; // Y reference direction downwards!
        Vx = 2; // in m/s
        Vy = -1.3;
        pixelsPerMeter = 40;

        radius = 25; // in pixels!
    }

    public void tick(double deltaT){
        if (pixelX < radius || pixelX > d.width - radius) {
            Vx = -Vx;
        }
        if (pixelY < radius || pixelY > d.height - radius) {
            Vy  =  -Vy;
        }

        X += Vx * deltaT;
        Y += Vy * deltaT;
    }

    public double getX(){
        return X;
    }

    public double getY(){
        return Y;
    }

    public int getRadius(){
        return radius;
    }

    public void draw(Graphics g){
        pixelX = (int) (pixelsPerMeter * X);
        pixelY = (int) (pixelsPerMeter * Y);

        g.setColor(color);
        g.fillOval( pixelX - radius
                , pixelY - radius, radius * 2, radius * 2);
    }
}
