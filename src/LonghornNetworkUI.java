import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

/**
 * Main UI class for the Longhorn Network application.
 * Provides a graphical interface to visualize student connections, roommate assignments,
 * referral paths, and social interactions.
 * 
 * @author Vidmahi Sistla
 * @version 1.0
 */
public class LonghornNetworkUI extends JFrame {
    // Data
    private List<UniversityStudent> students;
    private StudentGraph graph;
    private List<UniversityStudent> currentPath;
    private int currentTestCase = 1;
    
    // UI Components
    private GraphPanel graphPanel;
    private JTextArea infoArea;
    private JComboBox<String> testCaseCombo;
    private JTextField companyField;
    private JLabel statusLabel;
    
    // Colors
    private static final Color BACKGROUND_COLOR = new Color(18, 18, 18);
    private static final Color PANEL_COLOR = new Color(30, 30, 30);
    private static final Color ACCENT_COLOR = new Color(191, 87, 0);
    private static final Color TEXT_COLOR = new Color(230, 230, 230);
    private static final Color NODE_COLOR = new Color(66, 135, 245);
    private static final Color ROOMMATE_COLOR = new Color(76, 175, 80);
    private static final Color PATH_COLOR = new Color(255, 193, 7);
    
    /**
     * Constructs the main UI window.
     */
    public LonghornNetworkUI() {
        setTitle("Longhorn Network - Student Social Platform");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        initializeComponents();
        loadTestCase(1);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Initializes all UI components.
     */
    private void initializeComponents() {
        // Top Control Panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);
        
        // Center Panel (Graph + Info)
        JSplitPane centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        centerSplit.setDividerLocation(900);
        centerSplit.setBackground(BACKGROUND_COLOR);
        
        // Graph Panel
        graphPanel = new GraphPanel();
        JScrollPane graphScroll = new JScrollPane(graphPanel);
        graphScroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 2), 
            "Student Network Graph",
            0, 0, new Font("Arial", Font.BOLD, 14), TEXT_COLOR));
        graphScroll.setBackground(PANEL_COLOR);
        centerSplit.setLeftComponent(graphScroll);
        
        // Info Panel
        JPanel infoPanel = createInfoPanel();
        centerSplit.setRightComponent(infoPanel);
        
        add(centerSplit, BorderLayout.CENTER);
        
        // Status Panel
        JPanel statusPanel = createStatusPanel();
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates the control panel with buttons and options.
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Test Case Selection
        JLabel testLabel = new JLabel("Test Case:");
        testLabel.setForeground(TEXT_COLOR);
        testLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(testLabel);
        
        testCaseCombo = new JComboBox<>(new String[]{"Test Case 1", "Test Case 2", "Test Case 3"});
        testCaseCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(testCaseCombo);
        
        // Load Button
        JButton loadBtn = createStyledButton("Load Data");
        loadBtn.addActionListener(e -> {
            int selected = testCaseCombo.getSelectedIndex() + 1;
            loadTestCase(selected);
        });
        panel.add(loadBtn);
        
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        
        // Roommate Matching Button
        JButton roommateBtn = createStyledButton("Assign Roommates");
        roommateBtn.addActionListener(e -> runRoommateMatching());
        panel.add(roommateBtn);
        
        // Referral Path Button
        JButton referralBtn = createStyledButton("Find Referral Path");
        referralBtn.addActionListener(e -> runReferralPath());
        panel.add(referralBtn);
        
        // Company Input
        JLabel companyLabel = new JLabel("Company:");
        companyLabel.setForeground(TEXT_COLOR);
        companyLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(companyLabel);
        
        companyField = new JTextField("DummyCompany", 12);
        companyField.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(companyField);
        
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        
        // Run All Button
        JButton runAllBtn = createStyledButton("Run All Tests");
        runAllBtn.setBackground(ACCENT_COLOR);
        runAllBtn.addActionListener(e -> runAllTests());
        panel.add(runAllBtn);
        
        return panel;
    }
    
    /**
     * Creates the information panel showing student details.
     */
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 2), 
            "Information Panel",
            0, 0, new Font("Arial", Font.BOLD, 14), TEXT_COLOR));
        
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        infoArea.setBackground(BACKGROUND_COLOR);
        infoArea.setForeground(TEXT_COLOR);
        infoArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scroll = new JScrollPane(infoArea);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the status panel at the bottom.
     */
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        statusLabel = new JLabel("Ready. Select a test case and load data.");
        statusLabel.setForeground(TEXT_COLOR);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(statusLabel, BorderLayout.WEST);
        
        return panel;
    }
    
    /**
     * Creates a styled button with consistent appearance.
     */
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(NODE_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    /**
     * Loads a specific test case.
     */
    private void loadTestCase(int testNumber) {
        currentTestCase = testNumber;
        
        switch (testNumber) {
            case 1:
                students = Main.generateTestCase1();
                break;
            case 2:
                students = Main.generateTestCase2();
                break;
            case 3:
                students = Main.generateTestCase3();
                break;
        }
        
        graph = new StudentGraph(students);
        currentPath = null;
        
        updateInfoPanel();
        graphPanel.repaint();
        statusLabel.setText("Loaded Test Case " + testNumber + " with " + students.size() + " students.");
    }
    
    /**
     * Runs roommate matching algorithm.
     */
    private void runRoommateMatching() {
        if (students == null) {
            JOptionPane.showMessageDialog(this, "Please load data first!");
            return;
        }
        
        GaleShapley.assignRoommates(students);
        updateInfoPanel();
        graphPanel.repaint();
        statusLabel.setText("Roommate matching completed using Gale-Shapley algorithm.");
    }
    
    /**
     * Runs referral path finder.
     */
    private void runReferralPath() {
        if (students == null || students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please load data first!");
            return;
        }
        
        String company = companyField.getText().trim();
        if (company.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a company name!");
            return;
        }
        
        ReferralPathFinder finder = new ReferralPathFinder(graph);
        currentPath = finder.findReferralPath(students.get(0), company);
        
        updateInfoPanel();
        graphPanel.repaint();
        
        if (currentPath.isEmpty()) {
            statusLabel.setText("No referral path found for company: " + company);
        } else {
            statusLabel.setText("Referral path found! " + currentPath.size() + " students in path.");
        }
    }
    
    /**
     * Runs all tests (matching + referral path).
     */
    private void runAllTests() {
        runRoommateMatching();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        runReferralPath();
    }
    
    /**
     * Updates the information panel with current data.
     */
    private void updateInfoPanel() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════\n");
        sb.append("  TEST CASE ").append(currentTestCase).append(" - STUDENT INFORMATION\n");
        sb.append("═══════════════════════════════════════\n\n");
        
        // Student Details
        sb.append("STUDENTS:\n");
        sb.append("─────────────────────────────────────\n");
        for (UniversityStudent s : students) {
            sb.append(String.format("• %s (%d, %s)\n", s.name, s.age, s.major));
            sb.append(String.format("  GPA: %.2f | Year: %d\n", s.gpa, s.year));
            sb.append(String.format("  Internships: %s\n", 
                s.previousInternships.isEmpty() ? "None" : s.previousInternships));
            sb.append("\n");
        }
        
        // Roommate Assignments
        sb.append("\n═══════════════════════════════════════\n");
        sb.append("  ROOMMATE ASSIGNMENTS\n");
        sb.append("═══════════════════════════════════════\n\n");
        Set<UniversityStudent> paired = new HashSet<>();
        for (UniversityStudent s : students) {
            if (s.getRoommate() != null && !paired.contains(s)) {
                sb.append(String.format("✓ %s ↔ %s\n", s.name, s.getRoommate().name));
                paired.add(s);
                paired.add(s.getRoommate());
            }
        }
        for (UniversityStudent s : students) {
            if (s.getRoommate() == null && !s.roommatePreferences.isEmpty()) {
                sb.append(String.format("✗ %s (unpaired)\n", s.name));
            }
        }
        
        // Referral Path
        if (currentPath != null && !currentPath.isEmpty()) {
            sb.append("\n═══════════════════════════════════════\n");
            sb.append("  REFERRAL PATH TO ").append(companyField.getText()).append("\n");
            sb.append("═══════════════════════════════════════\n\n");
            for (int i = 0; i < currentPath.size(); i++) {
                sb.append(currentPath.get(i).name);
                if (i < currentPath.size() - 1) {
                    sb.append(" → ");
                }
            }
            sb.append("\n");
        }
        
        // Friend Requests & Chat
        sb.append("\n═══════════════════════════════════════\n");
        sb.append("  SOCIAL INTERACTIONS\n");
        sb.append("═══════════════════════════════════════\n\n");
        for (UniversityStudent s : students) {
            List<UniversityStudent> friends = s.getFriends();
            if (!friends.isEmpty()) {
                sb.append(String.format("%s's friends: ", s.name));
                for (int i = 0; i < friends.size(); i++) {
                    sb.append(friends.get(i).name);
                    if (i < friends.size() - 1) sb.append(", ");
                }
                sb.append("\n");
            }
        }
        
        infoArea.setText(sb.toString());
        infoArea.setCaretPosition(0);
    }
    
    /**
     * Custom panel for drawing the student graph.
     */
    private class GraphPanel extends JPanel {
        private Map<UniversityStudent, Point> positions;
        
        public GraphPanel() {
            setPreferredSize(new Dimension(850, 700));
            setBackground(BACKGROUND_COLOR);
            positions = new HashMap<>();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (students == null || students.isEmpty()) {
                drawEmptyState(g);
                return;
            }
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Calculate positions if not done
            if (positions.isEmpty()) {
                calculatePositions();
            }
            
            // Draw edges first
            drawEdges(g2);
            
            // Draw nodes
            drawNodes(g2);
            
            // Draw legend
            drawLegend(g2);
        }
        
        /**
         * Draws empty state message.
         */
        private void drawEmptyState(Graphics g) {
            g.setColor(TEXT_COLOR);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            String msg = "No data loaded. Select a test case and click 'Load Data'.";
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(msg)) / 2;
            int y = getHeight() / 2;
            g.drawString(msg, x, y);
        }
        
        /**
         * Calculates node positions using circular layout.
         */
        private void calculatePositions() {
            positions.clear();
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = Math.min(getWidth(), getHeight()) / 3;
            
            int n = students.size();
            for (int i = 0; i < n; i++) {
                double angle = 2 * Math.PI * i / n - Math.PI / 2;
                int x = centerX + (int) (radius * Math.cos(angle));
                int y = centerY + (int) (radius * Math.sin(angle));
                positions.put(students.get(i), new Point(x, y));
            }
        }
        
        /**
         * Draws edges between connected students.
         */
        private void drawEdges(Graphics2D g2) {
            g2.setStroke(new BasicStroke(2));
            
            Set<String> drawnEdges = new HashSet<>();
            
            for (UniversityStudent s : students) {
                Point p1 = positions.get(s);
                List<StudentGraph.Edge> edges = graph.getNeighbors(s);
                
                for (StudentGraph.Edge edge : edges) {
                    UniversityStudent neighbor = edge.neighbor;
                    String edgeKey = getEdgeKey(s, neighbor);
                    
                    if (drawnEdges.contains(edgeKey)) continue;
                    drawnEdges.add(edgeKey);
                    
                    Point p2 = positions.get(neighbor);
                    
                    // Determine edge color
                    Color edgeColor = Color.GRAY;
                    if (currentPath != null && isInPath(s, neighbor)) {
                        edgeColor = PATH_COLOR;
                        g2.setStroke(new BasicStroke(4));
                    } else if (s.getRoommate() == neighbor) {
                        edgeColor = ROOMMATE_COLOR;
                        g2.setStroke(new BasicStroke(3));
                    } else {
                        g2.setStroke(new BasicStroke(2));
                    }
                    
                    g2.setColor(edgeColor);
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                    
                    // Draw weight label
                    int midX = (p1.x + p2.x) / 2;
                    int midY = (p1.y + p2.y) / 2;
                    g2.setColor(TEXT_COLOR);
                    g2.setFont(new Font("Arial", Font.BOLD, 11));
                    g2.drawString(String.valueOf(edge.weight), midX + 5, midY - 5);
                }
            }
        }
        
        /**
         * Draws student nodes.
         */
        private void drawNodes(Graphics2D g2) {
            int nodeSize = 60;
            
            for (UniversityStudent s : students) {
                Point p = positions.get(s);
                
                // Determine node color
                Color nodeColor = NODE_COLOR;
                if (currentPath != null && currentPath.contains(s)) {
                    nodeColor = PATH_COLOR;
                } else if (s.getRoommate() != null) {
                    nodeColor = ROOMMATE_COLOR;
                }
                
                // Draw node circle
                g2.setColor(nodeColor);
                g2.fillOval(p.x - nodeSize/2, p.y - nodeSize/2, nodeSize, nodeSize);
                
                // Draw border
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(p.x - nodeSize/2, p.y - nodeSize/2, nodeSize, nodeSize);
                
                // Draw name
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(s.name);
                g2.drawString(s.name, p.x - textWidth/2, p.y + 5);
            }
        }
        
        /**
         * Draws legend explaining colors.
         */
        private void drawLegend(Graphics2D g2) {
            int x = 20;
            int y = getHeight() - 100;
            
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRoundRect(x - 10, y - 25, 200, 90, 10, 10);
            
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.setColor(TEXT_COLOR);
            g2.drawString("Legend:", x, y);
            
            y += 20;
            drawLegendItem(g2, x, y, NODE_COLOR, "Regular Connection");
            y += 20;
            drawLegendItem(g2, x, y, ROOMMATE_COLOR, "Roommates");
            y += 20;
            drawLegendItem(g2, x, y, PATH_COLOR, "Referral Path");
        }
        
        /**
         * Draws a single legend item.
         */
        private void drawLegendItem(Graphics2D g2, int x, int y, Color color, String label) {
            g2.setColor(color);
            g2.fillOval(x, y - 8, 12, 12);
            g2.setColor(TEXT_COLOR);
            g2.drawString(label, x + 20, y);
        }
        
        /**
         * Checks if an edge is in the current path.
         */
        private boolean isInPath(UniversityStudent s1, UniversityStudent s2) {
            if (currentPath == null || currentPath.size() < 2) return false;
            
            for (int i = 0; i < currentPath.size() - 1; i++) {
                UniversityStudent a = currentPath.get(i);
                UniversityStudent b = currentPath.get(i + 1);
                if ((a == s1 && b == s2) || (a == s2 && b == s1)) {
                    return true;
                }
            }
            return false;
        }
        
        /**
         * Creates a unique edge key for tracking drawn edges.
         */
        private String getEdgeKey(UniversityStudent s1, UniversityStudent s2) {
            String name1 = s1.name;
            String name2 = s2.name;
            return name1.compareTo(name2) < 0 ? name1 + "-" + name2 : name2 + "-" + name1;
        }
    }
    
    /**
     * Main method to launch the UI.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LonghornNetworkUI());
    }
}