package ui;

import engine.Engine;
import engine.Scene;
import engine.bodies.Body;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.LineBorder;

import static engine.Debugger.df;

public class SimulationUI extends JFrame
{
    
    private Scene scene;
    private SimulationPanel simPanel;
    private StatsPanel statsPanel;
    private BodiesListPanel listPanel;
    // The currently selected body.
    public Body selectedBody;
    // Follow mode flag.
    private boolean followMode = false;
    
    // Labels for uptime and time scale.
    private JLabel uptimeLabel;
    private JLabel timeScaleLabel;
    
    public SimulationUI(Scene scene)
    {
        super("Simulation");
        this.scene = scene;
        
        // Full screen settings.
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Use the frame's layered pane with a null layout.
        JLayeredPane layeredPane = getLayeredPane();
        layeredPane.setLayout(null);
        
        // --- Simulation Panel (Background) ---
        simPanel = new SimulationPanel();
        simPanel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(simPanel, JLayeredPane.DEFAULT_LAYER);
        
        // --- Control Panel with Follow Button and Uptime/Time Scale Texts (Overlay) ---
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBackground(new Color(0, 0, 0, 128)); // semi-transparent
        JButton followButton = new JButton("Follow: OFF");
        followButton.addActionListener(e ->
                                       {
                                           followMode = !followMode;
                                           followButton.setText("Follow: " + (followMode ? "ON" : "OFF"));
                                       });
        controlPanel.add(followButton);
        
        uptimeLabel = new JLabel("Uptime: 0.00 s");
        uptimeLabel.setForeground(Color.WHITE);
        controlPanel.add(uptimeLabel);
        
        timeScaleLabel = new JLabel("Time Scale: 1.00x");
        timeScaleLabel.setForeground(Color.WHITE);
        controlPanel.add(timeScaleLabel);
        
        // Position at top-left.
        controlPanel.setBounds(10, 10, 400, 40);
        layeredPane.add(controlPanel, JLayeredPane.PALETTE_LAYER);
        
        // --- Stats Panel (Overlay) ---
        statsPanel = new StatsPanel();
        statsPanel.setOpaque(false);
        // Position it so that it starts at mid-screen and takes the lower half.
        statsPanel.setBounds(10, getHeight() / 2, 300, getHeight() / 2 - 20);
        layeredPane.add(statsPanel, JLayeredPane.PALETTE_LAYER);
        
        // --- Bodies List Panel (Overlay) ---
        listPanel = new BodiesListPanel();
        listPanel.setOpaque(false);
        // Position on the right.
        listPanel.setBounds(getWidth() - 210, 10, 200, getHeight() - 20);
        layeredPane.add(listPanel, JLayeredPane.PALETTE_LAYER);
        
        // Add a red border to the bodies list panel (on its scroll pane).
        listPanel.setBorder(new LineBorder(Color.RED, 2));
        
        // Component listener to update bounds on resize.
        addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                Dimension size = getSize();
                simPanel.setBounds(0, 0, size.width, size.height);
                // Control panel stays at top-left.
                controlPanel.setBounds(10, 10, 400, 40);
                // Stats panel now starts halfway down.
                statsPanel.setBounds(10, size.height / 2, 300, size.height / 2 - 20);
                // Bodies list panel on the right.
                listPanel.setBounds(size.width - 210, 10, 200, size.height - 20);
            }
        });
        
        // Timer to update the stats panel.
        Timer statsTimer = new Timer(1000 / 30, e -> statsPanel.repaint());
        statsTimer.start();
        
        // Timer to update uptime and time scale labels (dummy values; replace with real simulation data).
        Timer infoTimer = new Timer(1000/100, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                uptimeLabel.setText(String.format("Uptime: %.8f s", Engine.uptime));
                // For demonstration, we keep time scale fixed.
                timeScaleLabel.setText(String.format("Time Scale: %.2fx", 1 * Engine.timeScale));
            }
        });
        infoTimer.start();
        
        setVisible(true);
        
        // Force an initial resize update.
        ComponentEvent ce = new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED);
        for(ComponentListener cl : getComponentListeners())
        {
            cl.componentResized(ce);
        }
    }
    
    // --- Simulation Panel ---
    class SimulationPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener
    {
        private double offsetX = 0, offsetY = 0;
        private double zoom = 1.0;
        private int lastMouseX, lastMouseY;
        
        // Camera rotation.
        private final double yaw = Math.toRadians(-10);
        private final double pitch = Math.toRadians(30);
        private final double cosYaw = Math.cos(yaw);
        private final double sinYaw = Math.sin(yaw);
        private final double sinPitch = Math.sin(pitch);
        private final double cosPitch = Math.cos(pitch);
        
        // Grid projection parameters.
        private final double gridA = cosYaw;
        private final double gridB = sinYaw;
        private final double gridC = sinPitch * sinYaw;
        private final double gridD = -sinPitch * cosYaw;
        
        private final DecimalFormat df = new DecimalFormat("0.00");
        
        public SimulationPanel()
        {
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
            setBackground(Color.BLACK);
            
            Timer timer = new Timer(1000 / 165, e ->
            {
                if(followMode && selectedBody != null)
                {
                    followSelectedBody();
                }
                repaint();
            });
            timer.start();
        }
        
        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Build UI transform: center, zoom, then pan.
            AffineTransform uiTransform = new AffineTransform();
            uiTransform.translate(getWidth() / 2, getHeight() / 2);
            uiTransform.scale(zoom, zoom);
            uiTransform.translate(offsetX, offsetY);
            
            // Draw dynamic grid.
            drawDynamicGrid(g2d, uiTransform);
            
            // Draw all bodies.
            for(Body body : scene.bodies)
            {
                double x = body.getPos().get(0);
                double y = body.getPos().get(1);
                double z = 0;
                double projX = cosYaw * x + sinYaw * z;
                double projY = sinPitch * sinYaw * x + cosPitch * y - sinPitch * cosYaw * z;
                Point2D centerScreen = uiTransform.transform(new Point2D.Double(projX, projY), null);
                double r = body.getRadius();
                int screenDiameter = (int) (2 * r * zoom);
                int screenX = (int) (centerScreen.getX() - screenDiameter / 2.0);
                int screenY = (int) (centerScreen.getY() - screenDiameter / 2.0);
                g2d.setColor(body.getColor());
                g2d.fillOval(screenX, screenY, screenDiameter, screenDiameter);
            }
            
            // Overlay elements.
            Point2D gridCenterScreen = projectGridPoint(0, 0, uiTransform);
            AffineTransform originalTransform = g2d.getTransform();
            g2d.setTransform(new AffineTransform());
            
            // Adjust the ruler's position so it isn't hidden by the bodies list.
            int margin = 20;
            // Bodies list panel width is 210 pixels.
            int rulerXEnd = getWidth() - margin - 210;
            int rulerY = getHeight() - margin;
            double factorX = Math.sqrt(gridA * gridA + gridC * gridC);
            double targetPixelSpacing = 50;
            double targetWorldSpacing = targetPixelSpacing / (zoom * factorX);
            double gridSpacing = getNiceGridSpacing(targetWorldSpacing);
            double effectivePixelWidth = gridSpacing * zoom * factorX;
            int rulerXStart = (int) (rulerXEnd - effectivePixelWidth);
            g2d.setColor(Color.WHITE);
            g2d.drawLine(rulerXStart, rulerY, rulerXEnd, rulerY);
            g2d.drawLine(rulerXStart, rulerY - 5, rulerXStart, rulerY + 5);
            g2d.drawLine(rulerXEnd, rulerY - 5, rulerXEnd, rulerY + 5);
            String label = formatGridSpacing(gridSpacing);
            FontMetrics fm = g2d.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            int labelX = rulerXStart + (int) effectivePixelWidth / 2 - labelWidth / 2;
            int labelY = rulerY - 10;
            g2d.drawString(label, labelX, labelY);
            
            g2d.setColor(Color.RED);
            int redPointRadius = 3;
            int redPointX = (int) gridCenterScreen.getX();
            int redPointY = (int) gridCenterScreen.getY();
            g2d.fillOval(redPointX - redPointRadius, redPointY - redPointRadius, redPointRadius * 2, redPointRadius * 2);
            
            // Draw gizmo for selected body.
            if(selectedBody != null)
            {
                double bx = selectedBody.getPos().get(0);
                double by = selectedBody.getPos().get(1);
                double bz = 0;
                double projBX = cosYaw * bx + sinYaw * bz;
                double projBY = sinPitch * sinYaw * bx + cosPitch * by - sinPitch * cosYaw * bz;
                Point2D bodyScreen = uiTransform.transform(new Point2D.Double(projBX, projBY), null);
                int gizmoLength = 20;
                Point2D dirX = computeDirection(bx, by, bz, 1, 0, 0, uiTransform, bodyScreen);
                Point2D dirY = computeDirection(bx, by, bz, 0, 1, 0, uiTransform, bodyScreen);
                Point2D dirZ = computeDirection(bx, by, bz, 0, 0, 1, uiTransform, bodyScreen);
                // Invert Y axis so blue points upward.
                dirY = new Point2D.Double(-dirY.getX(), -dirY.getY());
                
                g2d.setTransform(new AffineTransform());
                g2d.setStroke(new BasicStroke(2));
                // Draw X (red).
                g2d.setColor(Color.RED);
                g2d.drawLine((int) bodyScreen.getX(), (int) bodyScreen.getY(), (int) (bodyScreen.getX() + dirX.getX() * gizmoLength), (int) (bodyScreen.getY() + dirX.getY() * gizmoLength));
                // Draw Y (blue).
                g2d.setColor(Color.BLUE);
                g2d.drawLine((int) bodyScreen.getX(), (int) bodyScreen.getY(), (int) (bodyScreen.getX() + dirY.getX() * gizmoLength), (int) (bodyScreen.getY() + dirY.getY() * gizmoLength));
                // Draw Z (green).
                g2d.setColor(Color.GREEN);
                g2d.drawLine((int) bodyScreen.getX(), (int) bodyScreen.getY(), (int) (bodyScreen.getX() + dirZ.getX() * gizmoLength), (int) (bodyScreen.getY() + dirZ.getY() * gizmoLength));
            }
            
            g2d.setTransform(originalTransform);
        }
        
        // Helper: Compute a screen-space unit vector for a world axis.
        private Point2D computeDirection(double bodyX, double bodyY, double bodyZ, double dx, double dy, double dz, AffineTransform uiTransform, Point2D bodyScreen)
        {
            double projX = cosYaw * (bodyX + dx) + sinYaw * (bodyZ + dz);
            double projY = sinPitch * sinYaw * (bodyX + dx) + cosPitch * (bodyY + dy) - sinPitch * cosYaw * (bodyZ + dz);
            Point2D axisScreen = uiTransform.transform(new Point2D.Double(projX, projY), null);
            double dirX = axisScreen.getX() - bodyScreen.getX();
            double dirY = axisScreen.getY() - bodyScreen.getY();
            double length = Math.sqrt(dirX * dirX + dirY * dirY);
            if(length != 0)
            {
                dirX /= length;
                dirY /= length;
            }
            return new Point2D.Double(dirX, dirY);
        }
        
        private void drawDynamicGrid(Graphics2D g2d, AffineTransform uiTransform)
        {
            AffineTransform gridProjection = new AffineTransform(gridA, gridC, gridB, gridD, 0, 0);
            AffineTransform combinedTransform = new AffineTransform(uiTransform);
            combinedTransform.concatenate(gridProjection);
            AffineTransform invTransform;
            try
            {
                invTransform = combinedTransform.createInverse();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return;
            }
            Point2D[] screenCorners = new Point2D[]{new Point2D.Double(0, 0), new Point2D.Double(getWidth(), 0), new Point2D.Double(getWidth(), getHeight()), new Point2D.Double(0, getHeight())};
            double minX = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY;
            double minZ = Double.POSITIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;
            for(Point2D corner : screenCorners)
            {
                Point2D worldPt = invTransform.transform(corner, null);
                double wx = worldPt.getX();
                double wz = worldPt.getY();
                if(wx < minX)
                {
                    minX = wx;
                }
                if(wx > maxX)
                {
                    maxX = wx;
                }
                if(wz < minZ)
                {
                    minZ = wz;
                }
                if(wz > maxZ)
                {
                    maxZ = wz;
                }
            }
            double targetPixelSpacing = 50;
            double factorX = Math.sqrt(gridA * gridA + gridC * gridC);
            double targetWorldSpacing = targetPixelSpacing / (zoom * factorX);
            double gridSpacing = getNiceGridSpacing(targetWorldSpacing);
            double startX = Math.floor(minX / gridSpacing) * gridSpacing;
            double endX = Math.ceil(maxX / gridSpacing) * gridSpacing;
            double startZ = Math.floor(minZ / gridSpacing) * gridSpacing;
            double endZ = Math.ceil(maxZ / gridSpacing) * gridSpacing;
            for(double z = startZ; z <= endZ; z += gridSpacing)
            {
                Point2D p1 = projectGridPoint(startX, z, uiTransform);
                Point2D p2 = projectGridPoint(endX, z, uiTransform);
                g2d.setColor(Color.GRAY);
                g2d.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
            }
            for(double x = startX; x <= endX; x += gridSpacing)
            {
                Point2D p1 = projectGridPoint(x, startZ, uiTransform);
                Point2D p2 = projectGridPoint(x, endZ, uiTransform);
                g2d.setColor(Color.GRAY);
                g2d.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
            }
        }
        
        private Point2D projectGridPoint(double x, double z, AffineTransform uiTransform)
        {
            double projX = gridA * x + gridB * z;
            double projY = gridC * x + gridD * z;
            return uiTransform.transform(new Point2D.Double(projX, projY), null);
        }
        
        private double getNiceGridSpacing(double target)
        {
            double exponent = Math.floor(Math.log10(target));
            double base = Math.pow(10, exponent);
            double fraction = target / base;
            double niceFraction;
            if(fraction < 1.5)
            {
                niceFraction = 1;
            }
            else if(fraction < 3)
            {
                niceFraction = 2;
            }
            else if(fraction < 7)
            {
                niceFraction = 5;
            }
            else
            {
                niceFraction = 10;
            }
            return niceFraction * base;
        }
        
        private String formatGridSpacing(double spacing)
        {
            if(spacing < 1000)
            {
                return String.format("%.2f m", spacing);
            }
            else if(spacing < 1e6)
            {
                return String.format("%.2f km", spacing / 1000);
            }
            else if(spacing < 1e9)
            {
                return String.format("%.2f Mm", spacing / 1e6);
            }
            else if(spacing < 1e12)
            {
                return String.format("%.2f Gm", spacing / 1e9);
            }
            else if(spacing < 1e15)
            {
                return String.format("%.2f Tm", spacing / 1e12);
            }
            else
            {
                return String.format("%.2e m", spacing);
            }
        }
        
        // Centers the camera on a body.
        public void centerOnBody(Body body)
        {
            double x = body.getPos().get(0);
            double y = body.getPos().get(1);
            double z = 0;
            double projX = cosYaw * x + sinYaw * z;
            double projY = sinPitch * sinYaw * x + cosPitch * y - sinPitch * cosYaw * z;
            offsetX = -projX;
            offsetY = -projY;
            double desiredDiameter = 0.8 * Math.min(getWidth(), getHeight());
            double newZoom = desiredDiameter / (2 * body.getRadius());
            zoom = newZoom;
            repaint();
        }
        
        // Updates the camera to follow the selected body.
        public void followSelectedBody()
        {
            if(selectedBody != null)
            {
                double x = selectedBody.getPos().get(0);
                double y = selectedBody.getPos().get(1);
                double z = 0;
                double projX = cosYaw * x + sinYaw * z;
                double projY = sinPitch * sinYaw * x + cosPitch * y - sinPitch * cosYaw * z;
                offsetX = -projX;
                offsetY = -projY;
            }
        }
        
        @Override
        public void mousePressed(MouseEvent e)
        {
            lastMouseX = e.getX();
            lastMouseY = e.getY();
        }
        
        @Override
        public void mouseDragged(MouseEvent e)
        {
            int dx = e.getX() - lastMouseX;
            int dy = e.getY() - lastMouseY;
            offsetX += dx / zoom;
            offsetY += dy / zoom;
            lastMouseX = e.getX();
            lastMouseY = e.getY();
            repaint();
        }
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e)
        {
            int notches = e.getWheelRotation();
            double scaleFactor = 1.1;
            zoom = (notches < 0) ? zoom * scaleFactor : zoom / scaleFactor;
            repaint();
        }
        
        @Override
        public void mouseReleased(MouseEvent e)
        {
        }
        
        @Override
        public void mouseClicked(MouseEvent e)
        {
        }
        
        @Override
        public void mouseEntered(MouseEvent e)
        {
        }
        
        @Override
        public void mouseExited(MouseEvent e)
        {
        }
        
        @Override
        public void mouseMoved(MouseEvent e)
        {
        }
    }
    
    // --- Stats Panel ---
    class StatsPanel extends JPanel
    {
        private final Font statsFont = new Font("Consolas", Font.PLAIN, 14);
        
        public StatsPanel()
        {
            setPreferredSize(new Dimension(300, 0));
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g)
        {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setFont(statsFont);
            g2d.setColor(Color.WHITE);
            int margin = 10;
            // Start drawing from the top of this panel.
            int y = margin + g2d.getFontMetrics().getAscent();
            if(selectedBody != null)
            {
                String[] lines = {"===== RigidBody Debug Information =====", "Mass: " + selectedBody.getMass() + " (in appropriate units)", "Position: " + selectedBody.getPos(), "Velocity: " + selectedBody.getVel(), "Velocity Magnitude: " + selectedBody.getVelocityMagnitude() + " m/s", "Speed as % of c: " + df.format(selectedBody.getSpeedPercentC()) + " %", "Momentum: " + selectedBody.getMomentum(), "Momentum Magnitude: " + selectedBody.getMomentumMagnitude() + " kg·m/s", "Force Magnitude: " + selectedBody.getForceMagnitude() + " N", "Net Acceleration: " + selectedBody.getNetAccelerationMagnitude() + " m/s²", "Kinetic Energy: " + selectedBody.getKineticEnergy() + " J", "Potential Energy: " + selectedBody.getPotentialEnergy() + " J", "Internal Energy: " + selectedBody.getInternalEnergy() + " J", "Lorentz Factor: " + selectedBody.getGamma(), "========================================"};
                for(String line : lines)
                {
                    g2d.drawString(line, margin, y);
                    y += g2d.getFontMetrics().getHeight();
                }
            }
            else
            {
                g2d.drawString("No body selected.", margin, y);
            }
            g2d.dispose();
        }
    }
    
    // --- Bodies List Panel ---
    class BodiesListPanel extends JPanel
    {
        private JList<Body> bodiesList;
        private DefaultListModel<Body> listModel;
        
        public BodiesListPanel()
        {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(200, 0));
            setOpaque(false);
            listModel = new DefaultListModel<>();
            for(Body body : scene.bodies)
            {
                listModel.addElement(body);
            }
            bodiesList = new JList<>(listModel);
            bodiesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            bodiesList.setOpaque(false);
            bodiesList.setFont(new Font("Consolas", Font.PLAIN, 14));
            bodiesList.setCellRenderer(new BodyListCellRenderer());
            bodiesList.addListSelectionListener(e ->
                                                {
                                                    selectedBody = bodiesList.getSelectedValue();
                                                    statsPanel.repaint();
                                                });
            bodiesList.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    if(e.getClickCount() == 2)
                    {
                        Body body = bodiesList.getSelectedValue();
                        if(body != null)
                        {
                            simPanel.centerOnBody(body);
                        }
                    }
                }
            });
            JScrollPane scrollPane = new JScrollPane(bodiesList);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setOpaque(false);
            // Set a red border on the scroll pane.
            scrollPane.setBorder(new LineBorder(Color.RED, 2));
            add(scrollPane, BorderLayout.CENTER);
        }
        
        class BodyListCellRenderer extends DefaultListCellRenderer
        {
            private final Color evenColor = new Color(0, 0, 0, 0);
            private final Color oddColor = new Color(0, 0, 0, 0);
            private final Color selectedColor = new Color(100, 149, 237, 180);
            
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String name = (value instanceof Body) ? ((Body) value).getName() : value.toString();
                label.setText(name);
                label.setOpaque(true);
                label.setFont(new Font("Consolas", Font.PLAIN, 14));
                label.setBackground(isSelected ? selectedColor : ((index % 2 == 0) ? evenColor : oddColor));
                label.setForeground(Color.WHITE);
                return label;
            }
        }
    }
}
