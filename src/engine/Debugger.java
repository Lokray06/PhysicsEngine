package engine;

import engine.bodies.Body;
import engine.bodies.Rigidbody;

import java.text.DecimalFormat;

public class Debugger {

    public static DecimalFormat df = new DecimalFormat("0.#############################");

    // -----------------------------------------------------------------
    // Debug Method for Rigidbody: Prints all internal state details.
    // -----------------------------------------------------------------
    public static void debugBody(Body body) {
        System.out.println("Uptime: " + Engine.uptime);
        System.out.println("=====" +  body.getName()  + "=====");
        System.out.println("Mass: " + body.getMass() + " (in appropriate units)");
        System.out.println("Position: " + body.getPos());
        System.out.println("Velocity: " + body.getVel());
        System.out.println("Velocity Magnitude: " + body.getVelocityMagnitude() + " m/s");
        System.out.println("Speed as % of c: " + df.format(body.getSpeedPercentC()) + " %");
        System.out.println("Momentum: " + body.getMomentum());
        System.out.println("Momentum Magnitude: " + body.getMomentumMagnitude() + " kg·m/s");
        System.out.println("Sum of Forces: " + body.getForceMagnitude());
        System.out.println("Force Magnitude: " + body.getForceMagnitude() + " N");
        System.out.println("Sum of Accelerations: " + body.getColor());
        System.out.println("Net Acceleration Magnitude: " + body.getNetAccelerationMagnitude() + " m/s²");
        System.out.println("Kinetic Energy: " + body.getKineticEnergy() + " J");
        System.out.println("Potential Energy: " + body.getPotentialEnergy() + " J");
        System.out.println("Internal Energy: " + body.getInternalEnergy() + " J");
        System.out.println("Lorentz Factor (gamma): " + body.getGamma());
        System.out.println("========================================");
    }
}