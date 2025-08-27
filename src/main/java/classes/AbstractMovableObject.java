package classes;

import components.MoveComponent;
import components.RotateComponent;
import interfaces.IAbstractObject;
import interfaces.IMovable;
import interfaces.IRotatable;

public class AbstractMovableObject implements IMovable, IRotatable {
    private MoveComponent movementComponent;
    private RotateComponent rotationComponent;
    private IAbstractObject abstractObject;

    public AbstractMovableObject(IAbstractObject abstractObject) {
        // Предполагается, что getField возвращает Object, и нужно привести к нужному типу
        Vector location = (Vector) abstractObject.getField("Location");
        Vector velocity = (Vector) abstractObject.getField("Velocity");
        Number rotationNumber = (Number) abstractObject.getField("Direction");
        double rotation = rotationNumber.doubleValue();

        this.movementComponent = new MoveComponent(location, velocity);
        this.rotationComponent = new RotateComponent(rotation);
        this.abstractObject = abstractObject;
    }

    // IMovable
    @Override
    public Vector getLocation() {
        return (Vector) abstractObject.getField("Location");
    }

    @Override
    public void setLocation(Vector vector) {
        abstractObject.setField("Location", vector);
        this.movementComponent.setLocation(vector);
    }

    @Override
    public Vector getVelocity() {
        return this.movementComponent.getVelocity();
    }

    // IRotatable
    @Override
    public double getDirection() {
        Number dirNumber = (Number) abstractObject.getField("Direction");
        return dirNumber.doubleValue();
    }

    @Override
    public void setDirection(double angle) {
        this.rotationComponent.setDirection(angle);
        abstractObject.setField("Direction", angle);
    }

    @Override
    public int getDirectionsNumber() {
        Number num = (Number) abstractObject.getField("DirectionsNumber");
        return num.intValue();
    }

    @Override
    public double getAngularVelocity() {
        Number num = (Number) abstractObject.getField("AngularVelocity");
        return num.doubleValue();
    }

    public Vector clarificateDirection() {
        Vector initialVelocity = movementComponent.getVelocity();
        double direction = rotationComponent.getDirection(); // в градусах

        double angleRad = Math.toRadians(direction); // преобразование в радианы

        int newX = (int) Math.round(initialVelocity.getX() * Math.cos(angleRad) - initialVelocity.getY() * Math.sin(angleRad));
        int newY = (int) Math.round(initialVelocity.getX() * Math.sin(angleRad) + initialVelocity.getY() * Math.cos(angleRad));

        return new Vector(newX, newY);
    }

    public void setVelocity(Vector velocity) {
        this.movementComponent.setVelocity(velocity);
        this.abstractObject.setField("Velocity", velocity);
    }
}