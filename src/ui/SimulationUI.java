package ui;

import engine.Engine;
import engine.Scene;
import engine.Vector;
import engine.bodies.Body;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;

public class SimulationUI extends JFrame {

    private Scene scene;
    private SimulationPanel panel;

    public SimulationUI(Scene scene) {
        super("Simulation");
        this.scene = scene;
        panel = new SimulationPanel();
        // Remove window decorations and make full screen
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    class SimulationPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
        // Camera transformation variables
        private double offsetX = 0, offsetY = 0; // pan offset
        private double zoom = 1.0;              // zoom factor

        private int lastMouseX, lastMouseY;

        public SimulationPanel() {
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
            setBackground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Save the original transform for later resetting
            AffineTransform originalTransform = g2d.getTransform();

            // Center screen then apply zoom and pan
            g2d.translate(getWidth() / 2, getHeight() / 2);
            g2d.scale(zoom, zoom);
            g2d.translate(offsetX, offsetY);

            // Draw the grid (representing space-time)
            drawGrid(g2d);

            // Draw each body as a circle
            for (Body body : scene.bodies) {
                Vector pos = body.getPos(); // assuming pos.data[0] = x, pos.data[1] = y, ignore z for 2D
                double r = body.getRadius();
                Color col = body.getColor();
                g2d.setColor(col);
                int diameter = (int)(2 * r);
                int x = (int)(pos.data[0] - r);
                int y = (int)(pos.data[1] - r);
                g2d.fillOval(x, y, diameter, diameter);
            }

            // Reset transform for UI overlay text (no scaling/panning)
            g2d.setTransform(originalTransform);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Monospaced", Font.PLAIN, 14));
            // Draw simulation info text
            g2d.drawString("Uptime: " + Engine.uptime, 20, 20);
            g2d.drawString("Time Step: " + scene.timeStep, 20, 40);
            // You can add more info here as desired.
        }

        private void drawGrid(Graphics2D g2d) {
            g2d.setColor(Color.GRAY);
            // Set grid spacing in simulation units
            int gridSpacing = 50;
            int halfGridCount = 100; // how many grid lines in each direction

            // Draw vertical grid lines
            for (int i = -halfGridCount; i <= halfGridCount; i++) {
                int x = i * gridSpacing;
                g2d.drawLine(x, -halfGridCount * gridSpacing, x, halfGridCount * gridSpacing);
            }
            // Draw horizontal grid lines
            for (int i = -halfGridCount; i <= halfGridCount; i++) {
                int y = i * gridSpacing;
                g2d.drawLine(-halfGridCount * gridSpacing, y, halfGridCount * gridSpacing, y);
            }
        }

        // Mouse events for panning and zooming:
        @Override
        public void mousePressed(MouseEvent e) {
            lastMouseX = e.getX();
            lastMouseY = e.getY();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int dx = e.getX() - lastMouseX;
            int dy = e.getY() - lastMouseY;
            // Adjust the offset (accounting for the current zoom)
            offsetX += dx / zoom;
            offsetY += dy / zoom;
            lastMouseX = e.getX();
            lastMouseY = e.getY();
            repaint();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int notches = e.getWheelRotation();
            double scaleFactor = 1.1;
            if (notches < 0) {
                zoom *= scaleFactor;
            } else {
                zoom /= scaleFactor;
            }
            repaint();
        }

        // Unused mouse events
        @Override public void mouseReleased(MouseEvent e) {}
        @Override public void mouseClicked(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {}
        @Override public void mouseExited(MouseEvent e) {}
        @Override public void mouseMoved(MouseEvent e) {}
    }
}
