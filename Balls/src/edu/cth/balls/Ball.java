package edu.cth.balls;

import java.awt.*;

/**
 * Created by Gaku on 2016-04-24.
 */
public class Ball {

    private double x, y, vx, vy, pixelsPerMeter;
    private int radius, pixelX, pixelY;
    private Color color;
    private Dimension d;

    public Ball(Dimension d, double x, double y, double vx, double vy, double pixelsPerMeter, int radius, Color color){
        this.d = d;
        this.x = x; // in meters
        this.y = y; // y reference direction downwards!
        this.vx = vx; // in m/s
        this.vy = vy;
        this.pixelsPerMeter = pixelsPerMeter;
        this.color = color;
        this.radius = radius; // in pixels!
    }

    protected void tickForWalls(double deltaT){
        if (pixelX < radius || pixelX > d.width - radius) {
            vx = -vx;
        }
        if (pixelY < radius || pixelY > d.height - radius) {
            vy = -vy;
        } else {
            vy += 9.82 * deltaT;
        }
    }

    protected void tickPos(double deltaT){
        x += vx * deltaT;
        y += vy * deltaT;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getRadius(){
        return (radius / pixelsPerMeter);
    }

    public void draw(Graphics g){
        pixelX = (int) (pixelsPerMeter * x);
        pixelY = (int) (pixelsPerMeter * y);

        g.setColor(color);
        g.fillOval( pixelX - radius
                , pixelY - radius, radius * 2, radius * 2);
    }

    public double getMass(){
        return Math.PI*Math.pow(radius,2)/2;
    }

    protected double getVx(){
        return vx;
    }

    protected double getVy(){
        return vy;
    }

    protected void setVx(double vx){
        this.vx = vx;
    }

    protected void setVy(double vy){
        this.vy = vy;
    }
}
