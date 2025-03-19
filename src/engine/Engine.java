package engine;

import engine.bodies.Body;

public class Engine {
    // Accumulated time since the last fixed update (in seconds)
    public static double deltaTime = 0d;
    // Fixed timestep (set via scene.timeStep)
    public static double TIME_STEP = 1d / 60d;
    public static double SIMULATION_TIME_STEP = 1d / 60d;
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
    public static void update() {
        if (!isRunning) {
            // If paused, update the lastUpdateTime to avoid accumulating deltaTime.
            lastUpdateTime = System.nanoTime();
            return;
        }
        // Use the scene's time step as the simulation step.
        SIMULATION_TIME_STEP = scene.timeStep;

        long now = System.nanoTime();
        // Convert elapsed time from nanoseconds to seconds.
        double frameTime = (now - lastUpdateTime) / 1e9;
        lastUpdateTime = now;
        deltaTime += frameTime;

        // Clamp deltaTime to avoid spiral of death if a frame takes too long.
        if (deltaTime > 0.25) {
            deltaTime = 0.25;
        }

        // Update the simulation using fixed timesteps.
        while (deltaTime >= TIME_STEP) {
            for (Body body : scene.bodies) {
                body.update(SIMULATION_TIME_STEP);
                // Uncomment the next line if you wish to print debug info.
                Debugger.debugBody(body);
            }
            deltaTime -= TIME_STEP;
            uptime += TIME_STEP;
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
