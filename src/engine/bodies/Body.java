package engine.bodies;

import engine.Vector;
import java.awt.Color;

public abstract class Body {
    // New rendering properties
    public double radius = 5.0; // default radius (can be adjusted per body)
    public Color color = Color.WHITE; // default drawing color

    // If you want, you can also add getters and setters:
    public double getRadius() {
        return radius;
    }

    public void setRadius(double r) {
        this.radius = r;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    // -------------------------------
    // Your existing abstract methods:
    public abstract String getName();
    public abstract void update(double dt);
    public abstract double getMass();
    public abstract Vector getPos();
    public abstract Vector getVel();
    public abstract double getVelocityMagnitude();
    public abstract double getSpeedPercentC();
    public abstract Vector getMomentum();
    public abstract double getMomentumMagnitude();
    public abstract double getForceMagnitude();
    public abstract double getNetAccelerationMagnitude();
    public abstract double getKineticEnergy();
    public abstract double getPotentialEnergy();
    public abstract double getInternalEnergy();
    public abstract double getGamma();
}
