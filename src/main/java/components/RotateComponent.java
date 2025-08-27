package components;

import interfaces.IRotatable;

public class RotateComponent implements IRotatable {
    private double direction; // угол в градусах
    private double angularVelocity; // скорость вращения
    private int directionsNumber; // число возможных направлений (например, 360)

    public RotateComponent(double direction) {
        this.direction = direction;
        // Можно установить значения по умолчанию для angularVelocity и directionsNumber
        this.angularVelocity = 0.0;
        this.directionsNumber = 360; // например, для полного круга
    }

    @Override
    public double getDirection() {
        return direction;
    }

    @Override
    public void setDirection(double angle) {
        this.direction = angle;
    }

    @Override
    public double getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    @Override
    public int getDirectionsNumber() {
        return directionsNumber;
    }

    public void setDirectionsNumber(int directionsNumber) {
        this.directionsNumber = directionsNumber;
    }
}