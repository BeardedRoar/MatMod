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
    private final List<Integer> timers = new ArrayList<>();

    public World(){
        collided.add(false);
        collided.add(false);
        timers.add(500);
        timers.add(500);

    }

    public void addBall(Ball ball){
        balls.add(ball);
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
                    if (Math.pow((xposi - ballj.getX()),2) + Math.pow((yposi - ballj.getY()),2)
                            < Math.pow((radiusi + ballj.getRadius()),2)&& !collided.get(0) && !collided.get(1)){
                        collided.set(i, true);
                        collided.set(j, true);
                        double vbefore = getAbsVelocity(balli) + getAbsVelocity(ballj);
                        double mbefore = balli.getMass()*getAbsVelocity(balli) +
                                ballj.getMass()*getAbsVelocity(ballj);
                        Pair<Pair<Double>> newSpeeds = calcCollision(balli, ballj);
                        balli.setVx(newSpeeds.x.x);
                        balli.setVy(newSpeeds.x.y);
                        ballj.setVx(newSpeeds.y.x);
                        ballj.setVy(newSpeeds.y.y);
                        double vafter = getAbsVelocity(balli) + getAbsVelocity(ballj);
                        double mafter = balli.getMass()*getAbsVelocity(balli) +
                                ballj.getMass()*getAbsVelocity(ballj);
                        //System.out.println("vdiff: " + (vafter - vbefore));
                        //System.out.println("mdiff: " + (mafter - mbefore));
                    }
                }
        }        //int i = 0;
        for (Ball b: balls) {
            b.tickPos(deltaT);
            if(collided.get(0)) {
                timers.set(0, timers.get(0) - 1);
                timers.set(1, timers.get(1) - 1);
                if (timers.get(1) <= 0&& timers.get(0)<=0) {
                    collided.set(0, false);
                    collided.set(1,false);
                    timers.set(0,50);
                    timers.set(1,50);
                }
            }
            //i++;
            //System.out.println(timers.get(0) + " " + timers.get(1));

        }

    }

    public List<Ball> getBalls(){
        return balls;
    }

    private Pair<Pair<Double>> calcCollision(Ball b1, Ball b2){
        double m1 = b1.getMass();
        double m2 = b2.getMass();
        //System.out.println(b1.getMass());
        //System.out.println(b2.getMass());
        double colAngle1 = getAngle(b1, b2);
        double colAngle2 = getAngle(b1, b2)+Math.PI;
        Pair<Double> polarb1 = rectToPolar(b1.getVx(), b1.getVy());
        Pair<Double> polarb2 = rectToPolar(b2.getVx(), b2.getVy());

        double u1 = polarb1.x*Math.cos(polarb1.y-colAngle1);
        boolean dir1 = u1>0;
        double u2 = polarb2.x*Math.cos(polarb2.y-colAngle2);
        boolean dir2 = u2>0;
        //System.out.println(polarb1.x);
        //System.out.println(polarb2.x);
        System.out.println("u1 = " + u1);
        System.out.println("u2 = " + u2);
        double v1 = (m1*u1 -m2*u1+2*m2*u2)/(m1+m2);
        double v2 = (2*m1*u1-m1*u2+m2*u2)/(m1+m2);
        boolean dir3 = v1>0;
        boolean dir4 = v1>0;
        System.out.println("v1 = " + v1);
        System.out.println("v2 = " + v2);
        if(dir1==dir3){
            v1=-v1;
        }else{
            colAngle1= colAngle1+Math.PI;
        }if(dir2==dir4){
            v2 =-v2;
        }else{
            colAngle2 = colAngle2+Math.PI;
        }

        Pair<Double> vel1 = polarToRect(colAngle1,v1);
        Pair<Double> vel2 = polarToRect(colAngle2,v2);

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
