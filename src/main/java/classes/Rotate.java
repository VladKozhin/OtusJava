package classes;

import interfaces.ICommand;
import interfaces.IRotatable;

public class Rotate implements ICommand {
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
        System.out.println("Start direction: " + a + ", new Direction: " + result);
        target.setDirection(result);
    }
}
