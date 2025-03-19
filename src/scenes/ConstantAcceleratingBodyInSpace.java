package scenes;

import engine.Engine;
import engine.Scene;
import engine.Vector2;
import engine.bodies.Photon;
import engine.bodies.Rigidbody;
import engine.VectorDynamic;
import javax.swing.SwingUtilities;  // Make sure the package path is correct
import ui.SimulationUI;

import java.awt.*;

public class ConstantAcceleratingBodyInSpace {
    public static void main(String[] args) {
        Scene scene = new Scene();

        // Create and configure a simulation body
        Rigidbody body = new Rigidbody("1");
        body.setConstantForce(new Vector2(1,0));
        // Optionally adjust rendering properties:
        body.setRadius(8.0);
        body.setColor(Color.PINK);
        scene.add(body);

        // Launch a photon
        Photon photon = new Photon(new Vector2(0,0), new Vector2(1,0), 440);
        photon.setRadius(0.0000000000001);
        photon.setColor(java.awt.Color.YELLOW);
        scene.add(photon);

        // Set simulation timestep
        scene.timeStep = 1/60d;

        // Launch the full-screen canvas UI on the EDT.
        SwingUtilities.invokeLater(() -> {
            SimulationUI ui = new SimulationUI(scene);
        });

        // Start the simulation engine
        Engine.init(scene);
    }
}
