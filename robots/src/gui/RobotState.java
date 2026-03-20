package gui;

import java.util.ArrayList;
import java.util.List;

public class RobotState {
    private volatile double positionX;
    private volatile double positionY;
    private volatile double direction;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    private final List<Runnable> observers = new ArrayList<>();

    public RobotState(double positionX, double positionY, double direction) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.direction = direction;
    }

    public void addObserver(Runnable observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (Runnable observer : observers) {
            observer.run();
        }
    }

    public double getPositionX() { return positionX; }
    public double getPositionY() { return positionY; }
    public double getDirection()  { return direction; }

    public void updateTowards(TargetState target) {
        double distance = distance(target.getPositionX(), target.getPositionY(), positionX, positionY);
        if (distance < 0.5) {
            return;
        }
        double angleToTarget = angleTo(positionX, positionY, target.getPositionX(), target.getPositionY());
        double angularVelocity = 0;
        if (angleToTarget > direction) {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < direction) {
            angularVelocity = -maxAngularVelocity;
        }
        move(maxVelocity, angularVelocity, 10);
        notifyObservers();
    }

    private void move(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);

        double newX = positionX + velocity / angularVelocity *
                (Math.sin(direction + angularVelocity * duration) - Math.sin(direction));
        if (!Double.isFinite(newX)) {
            newX = positionX + velocity * duration * Math.cos(direction);
        }

        double newY = positionY - velocity / angularVelocity *
                (Math.cos(direction + angularVelocity * duration) - Math.cos(direction));
        if (!Double.isFinite(newY)) {
            newY = positionY + velocity * duration * Math.sin(direction);
        }

        positionX = newX;
        positionY = newY;
        direction = asNormalizedRadians(direction + angularVelocity * duration);
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0)            { angle += 2 * Math.PI; }
        while (angle >= 2 * Math.PI) { angle -= 2 * Math.PI; }
        return angle;
    }
}