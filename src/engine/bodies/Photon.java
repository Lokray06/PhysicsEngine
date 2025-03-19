package engine.bodies;

import engine.Vector;
import static engine.Constants.*;

public class Photon extends Body {
    // --- Physical properties ---
    public Vector position;   // Current position in space.
    public Vector velocity;   // Velocity vector (always of magnitude SPEED_OF_LIGHT)
    public Vector momentum;   // Photon momentum computed as p = h / λ
    public double wavelength; // Photon wavelength (in meters)

    // --- Energy ---
    public double energy;     // Energy computed via E = p * c

    // --- Constant: Planck's constant (in J·s) ---
    public static final double PLANCK_CONSTANT = 6.62607015e-34;

    /**
     * Creates a new Photon.
     * @param position  The starting position.
     * @param direction The propagation direction (will be normalized).
     * @param wavelength The wavelength in meters.
     */
    public Photon(Vector position, Vector direction, double wavelength) {
        this.position = position;
        this.velocity = direction.copy().normalize().mul(SPEED_OF_LIGHT);
        this.wavelength = wavelength;
        double momentumMagnitude = PLANCK_CONSTANT / wavelength;
        this.momentum = this.velocity.copy().normalize().mul(momentumMagnitude);
        this.energy = PLANCK_CONSTANT * SPEED_OF_LIGHT / wavelength;
    }

    /**
     * Update the photon's position based on its constant velocity.
     * @param dt Time step in seconds.
     */
    @Override
    public void update(double dt) {
        Vector displacement = velocity.copy().mul(dt);
        position = position.add(displacement);
    }

    // -----------------------------------------------------------------
    // Implementation of abstract Body getters for Photon
    // -----------------------------------------------------------------
    @Override
    public double getMass() {
        return 0.0;
    }

    @Override
    public Vector getPos() {
        return position;
    }

    @Override
    public Vector getVel() {
        return velocity;
    }

    @Override
    public double getVelocityMagnitude() {
        return velocity.magnitude();
    }

    @Override
    public double getSpeedPercentC() {
        return (velocity.magnitude() / SPEED_OF_LIGHT) * 100;
    }

    @Override
    public Vector getMomentum() {
        return momentum;
    }

    @Override
    public double getMomentumMagnitude() {
        return momentum.magnitude();
    }

    @Override
    public double getForceMagnitude() {
        return 0.0;
    }

    @Override
    public double getNetAccelerationMagnitude() {
        return 0.0;
    }

    @Override
    public double getKineticEnergy() {
        return energy;
    }

    @Override
    public double getPotentialEnergy() {
        return 0.0;
    }

    @Override
    public double getInternalEnergy() {
        return 0.0;
    }

    @Override
    public double getGamma() {
        return Double.POSITIVE_INFINITY;
    }
}
