package engine;

import engine.bodies.Body;
import scenes.ConstantAcceleratingBodyInSpace;

public class Engine {
    // Accumulated time since the last fixed update (in seconds)
    public static double deltaTime = 0d;
    
    public static float timeScale = 1f;
    public static double TIME_STEP = 1 / 60d;
    // Total simulation time
    public static double uptime = 0d;

    // The scene containing all rigid bodies to update
    public static Scene scene = null;

    // Used to compute deltaTime (in nanoseconds)
    private static long lastUpdateTime;

    // Simulation state flag
    private static boolean isRunning = true;

    // Initialize the engine, scene, and timing variables.
    public static void init(Scene sceneToUse) {
        scene = sceneToUse;
        if (scene == null) {
            System.err.println("No active scene");
            return;
        }
        if (scene.bodies.isEmpty()) {
            System.err.println("Empty scene, terminating");
            terminate();
            return;
        }
        deltaTime = 0d;
        uptime = 0d;
        lastUpdateTime = System.nanoTime();
        while(true)
        {
            update();
        }
    }

    // Call this method repeatedly (e.g., via a Swing Timer) to update the simulation.
    // Declare an accumulator variable at the class level (or as a static variable)
    private static double debugAccumulator = 0;
    
    public static void update() {
        if (!isRunning) {
            lastUpdateTime = System.nanoTime();
            return;
        }
        
        long now = System.nanoTime();
        double frameTime = (now - lastUpdateTime) / 1e9;
        lastUpdateTime = now;
        deltaTime += frameTime;
        
        if (deltaTime > 0.25) {
            deltaTime = 0.25;
        }
        
        // Add frame time to the debug accumulator
        debugAccumulator += frameTime;
        
        // If one second has passed, output debug info for each body and reset accumulator.
        if (debugAccumulator >= 1.0) {
            for (Body body : scene.bodies) {
                Debugger.debugBody(body);
            }
            debugAccumulator -= 1.0;  // Reset accumulator, or set to zero if no leftover time is needed.
        }
        
        while (deltaTime >= TIME_STEP) {
            double fixedDt = TIME_STEP;
            double scaledDt = fixedDt * timeScale;
            for (Body body : scene.bodies) {
                body.update(scaledDt);
            }
            deltaTime -= TIME_STEP;
            uptime += fixedDt * timeScale;
        }
    }
    
    
    // Start the simulation.
    public static void togglePlay() {
        isRunning = !isRunning;
        lastUpdateTime = System.nanoTime();
    }

    // Clean up resources and optionally print a summary.
    public static void terminate() {
        System.out.println("Engine terminated. Total uptime: " + uptime + " seconds.");
        System.exit(0);
    }
}
