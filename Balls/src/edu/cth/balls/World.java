package edu.cth.balls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaku on 2016-04-24.
 */
public class World {

    private final List<Ball> balls = new ArrayList();

    public World(){

    }

    public void addBall(Ball ball){
        balls.add(ball);
    }

    public void tick(double deltaT){
        for (Ball b : balls) {
            b.tickForWalls(deltaT);
        }
        for (Ball b : balls) {
            b.tickPos(deltaT);
        }
        for(int i = 0; i < balls.size(); i++){
            Ball balli = balls.get(i);
            double xposi = balli.getX();
            double yposi = balli.getY();
            double radiusi = balli.getRadius();
                for(int j = (i+1); j < balls.size(); j++){
                    Ball ballj = balls.get(j);
                    if (Math.pow((xposi - ballj.getX()),2) + Math.pow((yposi - ballj.getY()),2)
                            < Math.pow((radiusi + ballj.getRadius()),2)){
                        double vbefore = getAbsVelocity(balli) - getAbsVelocity(ballj);
                        double mbefore = balli.getMass()*getAbsVelocity(balli) +
                                ballj.getMass()*getAbsVelocity(ballj);
                        Pair<Pair<Double>> newSpeeds = calcCollision(balli, ballj);
                        balli.setVx(newSpeeds.x.x);
                        balli.setVy(newSpeeds.x.y);
                        ballj.setVx(newSpeeds.y.x);
                        ballj.setVy(newSpeeds.y.y);
                        double vafter = getAbsVelocity(balli) - getAbsVelocity(ballj);
                        double mafter = balli.getMass()*getAbsVelocity(balli) +
                                ballj.getMass()*getAbsVelocity(ballj);
                        System.out.println("vdiff: " + (vafter - vbefore));
                        System.out.println("mdiff: " + (mafter - mbefore));
                    }
                }
        }

    }

    public List<Ball> getBalls(){
        return balls;
    }

    private Pair<Pair<Double>> calcCollision(Ball b1, Ball b2){
        double m1 = b1.getMass();
        double m2 = b2.getMass();

        double angle = getAngle(b1, b2);

        double u1 = getAbsVelocity(b1);
        double u2 = getAbsVelocity(b2);

        double v1 = (-m2*u1+m1*u1+2*m2*u2)/(m1+m2);
        double v2 = (-m1*u2+m2*u2+2*m1*u1)/(m1+m2);

        System.out.println();

        Pair<Double> vel1 = new Pair<>(-v1*Math.cos(angle), -v1*Math.sin(angle));
        Pair<Double> vel2 = new Pair<>(v2*Math.cos(angle), v2*Math.sin(angle));

        return new Pair<>(vel1,vel2);
    }

    private double getAbsVelocity(Ball ball){
        return Math.sqrt(Math.pow(ball.getVx(),2) + Math.pow(ball.getVy(),2));
    }

    private Double getAngle(Ball b1, Ball b2){
        double xdiff = b1.getX() - b2.getX();
        double ydiff = b1.getY() - b2.getY();
        return ydiff/xdiff;
    }
}
