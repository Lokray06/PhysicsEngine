package engine.bodies;

import engine.Vector;

import static engine.Constants.*;

public class Bodies {
    public static Rigidbody electron = new Rigidbody(
            ELECTRON_MASS,
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            0, 0, 0,
            null, null, null
    );

    public static Rigidbody proton = new Rigidbody(
            PROTON_MASS,
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            0, 0, 0,
            null, null, null
    );

    public static Rigidbody moon = new Rigidbody(
            MOON_MASS,
            new Vector(0, 1022, 0), // Orbital velocity around Earth
            new Vector(0, 0, 0),
            new Vector(384400000, 0, 0), // Distance from Earth
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            0, 0, 0,
            null, null, null
    );

    public static Rigidbody earth = new Rigidbody(
            EARTH_MASS,
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            0, 0, 0,
            null, null, null
    );

    public static Rigidbody asteroid = new Rigidbody(
            1e12, // 1 trillion kg
            new Vector(0, 25000, 0), // Fast-moving asteroid
            new Vector(0, 0, 0),
            new Vector(1e9, 0, 0), // 1 billion meters from some reference point
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            0, 0, 0,
            null, null, null
    );

    public static Rigidbody sagittariusA = new Rigidbody(
            SAGITTARIUS_A_MASS,
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            0, 0, 0,
            null, null, null
    );

    public static Rigidbody photon = new Rigidbody(
            0, // Photons have no rest mass
            new Vector(SPEED_OF_LIGHT, 0, 0),
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            new Vector(0, 0, 0),
            0, 0, 0,
            null, null, null
    );
}