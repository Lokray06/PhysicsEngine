package engine.bodies;

import engine.Vector;
import java.math.BigDecimal;
import java.math.MathContext;
import static engine.Constants.*;

public class Rigidbody extends Body {
    // --- Physical properties ---
    public double mass;
    public Vector velocity;
    public Vector momentum;
    public Vector position;
    public Vector sumOfForces;
    public Vector sumOfAccelerations;

    // --- Energy values ---
    public double kineticEnergy;
    public double potentialEnergy;
    public double internalEnergy;

    // --- Additional computed fields ---
    public double speedPercentC;
    public double gamma;
    public double velocityMagnitude;
    public double momentumMagnitude;
    public double forceMagnitude;
    public double netAccelerationMagnitude;

    // --- Constant values (applied each update if set) ---
    public Vector constantVelocity;
    public Vector constantAcceleration;
    public Vector constantForce;

    // -----------------------------------------------------------------
    // Constructors, setters, and utility methods (omitted for brevity)
    // -----------------------------------------------------------------
    public Rigidbody(String massInput) {
        this.mass = parseMass(massInput);
        this.velocity = new Vector(0, 0, 0);
        this.momentum = new Vector(0, 0, 0);
        this.position = new Vector(0, 0, 0);
        this.sumOfForces = new Vector(0, 0, 0);
        this.sumOfAccelerations = new Vector(0, 0, 0);
        this.kineticEnergy = 0;
        this.potentialEnergy = 0;
        this.internalEnergy = 0;
        this.speedPercentC = 0;
        this.gamma = 1;
        this.velocityMagnitude = 0;
        this.momentumMagnitude = 0;
        this.forceMagnitude = 0;
        this.netAccelerationMagnitude = 0;
        this.constantVelocity = null;
        this.constantAcceleration = null;
        this.constantForce = null;
    }

    private double parseMass(String massInput) {
        BigDecimal massValue = new BigDecimal(massInput, new MathContext(20));
        double magnitude = massValue.doubleValue();
        double absMag = Math.abs(magnitude);
        if (absMag >= 1e27) {
            return magnitude / SOLAR_MASS;
        } else if (absMag >= 1e23) {
            return magnitude / EARTH_MASS;
        } else {
            return magnitude;
        }
    }

    public void setMass(String massInput) {
        this.mass = parseMass(massInput);
    }

    public void setInitialVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public void setInitialMomentum(Vector momentum) {
        this.momentum = momentum;
    }

    public void setInitialPosition(Vector position) {
        this.position = position;
    }

    public void setInitialSumOfForces(Vector sumOfForces) {
        this.sumOfForces = sumOfForces;
    }

    public void setInitialSumOfAccelerations(Vector sumOfAccelerations) {
        this.sumOfAccelerations = sumOfAccelerations;
    }

    public void setInitialKineticEnergy(double kineticEnergy) {
        this.kineticEnergy = kineticEnergy;
    }

    public void setInitialPotentialEnergy(double potentialEnergy) {
        this.potentialEnergy = potentialEnergy;
    }

    public void setInitialInternalEnergy(double internalEnergy) {
        this.internalEnergy = internalEnergy;
    }

    public void setConstantVelocity(Vector velocity) {
        this.constantVelocity = velocity;
    }

    public void setConstantAcceleration(Vector acceleration) {
        this.constantAcceleration = acceleration;
    }

    public void setConstantForce(Vector force) {
        this.constantForce = force;
    }

    // -----------------------------------------------------------------
    // Force and Acceleration Accumulation
    // -----------------------------------------------------------------
    public void addForce(Vector force) {
        sumOfForces = sumOfForces.add(force);
    }

    public void addAcceleration(Vector acceleration) {
        sumOfAccelerations = sumOfAccelerations.add(acceleration);
    }

    // -----------------------------------------------------------------
    // Update Velocity and Momentum (Relativistic)
    // -----------------------------------------------------------------
    public void updateVelocity(double dt) {
        if (mass == 0) {
            if (constantVelocity != null) {
                velocity = constantVelocity.copy();
            } else if (velocity.magnitude() > 0) {
                velocity = velocity.copy().normalize().mul(SPEED_OF_LIGHT);
            } else {
                velocity = new Vector(SPEED_OF_LIGHT, 0, 0);
            }
            velocityMagnitude = velocity.magnitude();
            speedPercentC = (velocityMagnitude / SPEED_OF_LIGHT) * 100;
            sumOfForces = new Vector(0, 0, 0);
            sumOfAccelerations = new Vector(0, 0, 0);
            gamma = Double.POSITIVE_INFINITY;
            return;
        }

        if (constantForce != null) {
            sumOfForces = sumOfForces.add(constantForce);
        }
        if (constantAcceleration != null) {
            sumOfAccelerations = sumOfAccelerations.add(constantAcceleration);
        }

        netAccelerationMagnitude = sumOfAccelerations.mul(mass).magnitude();
        sumOfForces = sumOfForces.add(sumOfAccelerations.mul(mass));
        sumOfAccelerations = new Vector(0, 0, 0);
        Vector forceContribution = sumOfForces.copy().mul(dt);
        momentum = momentum.add(forceContribution);
        forceMagnitude = sumOfForces.magnitude();
        double pMagnitude = momentum.magnitude();
        momentumMagnitude = pMagnitude;
        gamma = Math.sqrt(1 + Math.pow(pMagnitude / (mass * SPEED_OF_LIGHT), 2));
        velocity = momentum.copy().div(mass * gamma);
        velocityMagnitude = velocity.magnitude();
        speedPercentC = (velocityMagnitude / SPEED_OF_LIGHT) * 100;

        if (constantVelocity != null) {
            velocity = constantVelocity.copy();
            double vMag = velocity.magnitude();
            double gammaNew = (vMag == 0) ? 1.0 : 1.0 / Math.sqrt(1 - Math.pow(vMag / SPEED_OF_LIGHT, 2));
            gamma = gammaNew;
            momentum = velocity.copy().mul(mass * gamma);
            momentumMagnitude = momentum.magnitude();
            velocityMagnitude = vMag;
            speedPercentC = (vMag / SPEED_OF_LIGHT) * 100;
        }
        sumOfForces = new Vector(0, 0, 0);
    }

    // -----------------------------------------------------------------
    // Update Position using simple Euler integration
    // -----------------------------------------------------------------
    public void updatePosition(double dt) {
        Vector displacement = velocity.copy().mul(dt);
        position = position.add(displacement);
    }

    // -----------------------------------------------------------------
    // Update Energy: Kinetic (Relativistic), Potential, and Internal energies.
    // -----------------------------------------------------------------
    public void updateEnergy() {
        double vMag = velocityMagnitude;
        if (mass == 0) {
            kineticEnergy = momentum.magnitude() * SPEED_OF_LIGHT;
        } else {
            double gammaLocal = (vMag == 0) ? 1.0 : 1.0 / Math.sqrt(1 - Math.pow(vMag / SPEED_OF_LIGHT, 2));
            kineticEnergy = (gammaLocal - 1) * mass * SPEED_OF_LIGHT * SPEED_OF_LIGHT;
        }
        potentialEnergy = getPotentialEnergy();
        internalEnergy = getInternalEnergy();
    }

    // -----------------------------------------------------------------
    // Main Update Method
    // -----------------------------------------------------------------
    @Override
    public void update(double dt) {
        if (mass == 0) {
            if (constantVelocity != null) {
                velocity = constantVelocity.copy().normalize().mul(SPEED_OF_LIGHT);
            } else if (velocity.magnitude() > 0) {
                velocity = velocity.copy().normalize().mul(SPEED_OF_LIGHT);
            } else {
                velocity = new Vector(SPEED_OF_LIGHT, 0, 0);
            }
            velocityMagnitude = velocity.magnitude();
            speedPercentC = (velocityMagnitude / SPEED_OF_LIGHT) * 100;
            momentum = velocity.copy();
            updatePosition(dt);
            kineticEnergy = momentum.magnitude() * SPEED_OF_LIGHT;
            return;
        }
        updateVelocity(dt);
        updatePosition(dt);
        updateEnergy();
    }

    // -----------------------------------------------------------------
    // Implementation of abstract Body getters
    // -----------------------------------------------------------------
    @Override
    public double getMass() {
        return mass;
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
        return velocityMagnitude;
    }

    @Override
    public double getSpeedPercentC() {
        return speedPercentC;
    }

    @Override
    public Vector getMomentum() {
        return momentum;
    }

    @Override
    public double getMomentumMagnitude() {
        return momentumMagnitude;
    }

    @Override
    public double getForceMagnitude() {
        return forceMagnitude;
    }

    @Override
    public double getNetAccelerationMagnitude() {
        return netAccelerationMagnitude;
    }

    @Override
    public double getKineticEnergy() {
        return kineticEnergy;
    }

    @Override
    public double getPotentialEnergy() {
        return potentialEnergy;
    }

    @Override
    public double getInternalEnergy() {
        return internalEnergy;
    }

    @Override
    public double getGamma() {
        return gamma;
    }
    public Rigidbody(double mass, Vector velocity, Vector momentum, Vector position,
                     Vector sumOfForces, Vector sumOfAccelerations,
                     double kineticEnergy, double potentialEnergy, double internalEnergy,
                     Vector constantVelocity, Vector constantAcceleration, Vector constantForce) {
        this.mass = mass;
        this.velocity = velocity;
        this.momentum = momentum;
        this.position = position;
        this.sumOfForces = sumOfForces;
        this.sumOfAccelerations = sumOfAccelerations;
        this.kineticEnergy = kineticEnergy;
        this.potentialEnergy = potentialEnergy;
        this.internalEnergy = internalEnergy;

        // Initialize additional computed fields
        this.velocityMagnitude = velocity.magnitude();
        this.momentumMagnitude = momentum.magnitude();
        this.forceMagnitude = sumOfForces.magnitude();
        this.netAccelerationMagnitude = sumOfAccelerations.magnitude();

        this.speedPercentC = (this.velocityMagnitude / SPEED_OF_LIGHT) * 100;
        this.gamma = (this.velocityMagnitude == 0) ? 1.0 : 1.0 / Math.sqrt(1 - Math.pow(this.velocityMagnitude / SPEED_OF_LIGHT, 2));

        this.constantVelocity = constantVelocity;
        this.constantAcceleration = constantAcceleration;
        this.constantForce = constantForce;
    }
}
