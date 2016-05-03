package edu.cth.balls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Gaku on 2016-04-24.
 */
public class World {

    private final List<Ball> balls = new ArrayList();
    private final List<Boolean> collided = new ArrayList();

    public World(){
    }

    public void addBall(Ball ball){
        balls.add(ball);
        collided.add(false);
    }

    public void tick(double deltaT){
        for (Ball b : balls) {
            b.tickForWalls(deltaT);
        }
        for(int i = 0; i < balls.size(); i++){
            Ball balli = balls.get(i);
            double xposi = balli.getX();
            double yposi = balli.getY();
            double radiusi = balli.getRadius();
                for(int j = (i+1); j < balls.size(); j++){
                    Ball ballj = balls.get(j);
                    if (!collided.get(i) && !collided.get(j)) {
                        if (Math.pow((xposi - ballj.getX()), 2) + Math.pow((yposi - ballj.getY()), 2)
                                < Math.pow((radiusi + ballj.getRadius()), 2) && !collided.get(0) && !collided.get(1)) {
                            collided.set(i, true);
                            collided.set(j, true);
                            double vbefore = getAbsVelocity(balli) + getAbsVelocity(ballj);
                            double mbefore = balli.getMass() * getAbsVelocity(balli) +
                                    ballj.getMass() * getAbsVelocity(ballj);
                            Pair<Pair<Double>> newSpeeds = calcCollision(balli, ballj);
                            balli.setVx(newSpeeds.x.x);
                            balli.setVy(newSpeeds.x.y);
                            ballj.setVx(newSpeeds.y.x);
                            ballj.setVy(newSpeeds.y.y);
                            double vafter = getAbsVelocity(balli) + getAbsVelocity(ballj);
                            double mafter = balli.getMass() * getAbsVelocity(balli) +
                                    ballj.getMass() * getAbsVelocity(ballj);
                            System.out.println("vdiff: " + (vafter - vbefore));
                            System.out.println("mdiff: " + (mafter - mbefore));
                        }
                    } else {
                        collided.set(i,false);
                        collided.set(j,false);
                    }
                }
        }
        for (Ball b: balls) {
            b.tickPos(deltaT);
        }

    }

    public List<Ball> getBalls(){
        return balls;
    }

    private Pair<Pair<Double>> calcCollision(Ball b1, Ball b2){
        double m1 = b1.getMass();
        double m2 = b2.getMass();

        double alpha = getAngle(b1, b2);

        Pair<Double> polarb1 = rectToPolar(b1.getVx(), b1.getVy());
        Pair<Double> polarb2 = rectToPolar(b2.getVx(), b2.getVy());

        double beta1 = polarb1.y-alpha;
        double beta2 = polarb2.y-alpha;

        double u1 = polarb1.x*Math.cos(beta1);
        double rv1 = polarb1.x*Math.sin(beta1);

        double u2 = polarb2.x*Math.cos(beta2);
        double rv2 = polarb2.x*Math.sin(beta2);

        double v1 = (m1*u1 -m2*u1+2*m2*u2)/(m1+m2);
        double v2 = (2*m1*u1-m1*u2+m2*u2)/(m1+m2);
        System.out.println("v1 = " + v1);
        System.out.println("v2 = " + v2);

        Pair<Double>polarAfter1 = rectToPolar(v1,rv1);
        Pair<Double>polarAfter2 = rectToPolar(v2,rv2);

        Pair<Double> vel1 = polarToRect(polarAfter1.y+alpha, polarAfter1.x);
        Pair<Double> vel2 = polarToRect(polarAfter2.y+alpha, polarAfter2.x);

        return new Pair<>(vel1,vel2);
    }

    private double getAbsVelocity(Ball ball){
        return Math.sqrt(Math.pow(ball.getVx(),2) + Math.pow(ball.getVy(),2));
    }

    private Double getAngle(Ball b1, Ball b2){
        double xdiff = b1.getX() - b2.getX();
        double ydiff = b1.getY() - b2.getY();
        return Math.atan(ydiff/xdiff);
    }

    private Pair<Double> rectToPolar(double vx, double vy){
        double theta = Math.atan2(vy, vx);
        double r = Math.sqrt((Math.pow(vx,2) + Math.pow(vy,2)));
        return new Pair<>(r,theta);
    }

    private Pair<Double> polarToRect(double theta, double r){
        double vx = r*Math.cos(theta);
        double vy = r*Math.sin(theta);
        return new Pair<>(vx,vy);
    }
}
