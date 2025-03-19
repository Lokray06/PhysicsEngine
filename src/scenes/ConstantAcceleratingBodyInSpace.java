package scenes;

import engine.Engine;
import engine.Scene;
import engine.bodies.Photon;
import engine.bodies.Rigidbody;
import engine.Vector;
import javax.swing.SwingUtilities;  // Make sure the package path is correct
import ui.SimulationUI;

public class ConstantAcceleratingBodyInSpace {
    public static void main(String[] args) {
        Scene scene = new Scene();

        // Create and configure a simulation body
        Rigidbody body = new Rigidbody("0.000001");
        body.setConstantForce(new Vector(100000, 0, 0));
        // Optionally adjust rendering properties:
        body.setRadius(8.0);
        body.setColor(java.awt.Color.CYAN);
        scene.add(body);

        // Launch a photon
        Photon photon = new Photon(new Vector(0, 0, 0), new Vector(1, 0, 0), 440);
        photon.setRadius(4.0);
        photon.setColor(java.awt.Color.YELLOW);
        scene.add(photon);

        // Set simulation timestep
        scene.timeStep = 0.00000000016d;

        // Launch the full-screen canvas UI on the EDT.
        SwingUtilities.invokeLater(() -> {
            SimulationUI ui = new SimulationUI(scene);
        });

        // Start the simulation engine
        Engine.init(scene);
    }
}
