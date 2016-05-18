package edu.cth.balls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Gaku on 2016-04-24.
 */
public class World {

    private final List<Ball> balls;
    private final List<List<Ball>> collided;
    private final List<Pair<Ball>> collisions;

    public World(){
        collisions = new ArrayList<Pair<Ball>>();
        collided = new ArrayList<List<Ball>>();
        balls = new ArrayList<Ball>();
    }

    public void addBall(Ball ball){
        balls.add(ball);
        collided.add(new ArrayList<Ball>(balls.size()*2));
    }

    public void tick(double deltaT){
        for (Ball b : balls) {
            b.tickForWalls(deltaT);
        }
        checkForCollision();
        resolveCollisions();
        for (Ball b: balls) {
            b.tickPos(deltaT);
        }
        checkForClear();

    }

    private void checkForCollision(){
        for(int i = 0; i < balls.size(); i++){
            Ball balli = balls.get(i);
            double xposi = balli.getX();
            double yposi = balli.getY();
            double radiusi = balli.getRadius();
            for(int j = (i+1); j < balls.size(); j++){
                Ball ballj = balls.get(j);
                if (Math.pow((xposi - ballj.getX()), 2) + Math.pow((yposi - ballj.getY()), 2)
                        < Math.pow((radiusi + ballj.getRadius()), 2) &&
                        !collided.get(i).contains(ballj) && !collided.get(j).contains(balli)) {
                    collided.get(i).add(ballj);
                    collided.get(i).add(balli);
                    collisions.add(new Pair<>(balli,ballj));
                }
            }
        }
    }

    private void resolveCollisions(){
        for (Pair<Ball> collidedBalls : collisions) {
            Ball balli = collidedBalls.x;
            Ball ballj = collidedBalls.y;
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
        }
        collisions.clear();
    }

    private void checkForClear(){
        for(int i = 0; i < balls.size(); i++){
            Ball balli = balls.get(i);
            double xposi = balli.getX();
            double yposi = balli.getY();
            double radiusi = balli.getRadius();
            for(int j = (i+1); j < balls.size(); j++){
                Ball ballj = balls.get(j);
                if ((collided.get(i).contains(ballj) || collided.get(j).contains(balli)) &&
                        Math.pow((xposi - ballj.getX()), 2) + Math.pow((yposi - ballj.getY()), 2)
                        > Math.pow((radiusi + ballj.getRadius()), 2)) {
                    collided.get(i).remove(ballj);
                    collided.get(j).remove(balli);
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
