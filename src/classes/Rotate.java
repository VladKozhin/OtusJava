package classes;

import interfaces.IRotatable;

public class Rotate {
    private IRotatable target;

    public Rotate(IRotatable target) {
        this.target = target;
    }

    public void execute() {
        double a = target.getDirection();
        double b = target.getAngularVelocity();

        double result = (a + b) % 360;
        if (result < 0) {
            result += 360; // чтобы результат был в диапазоне [0, 360)
        }

        target.setDirection(result);
    }
}
