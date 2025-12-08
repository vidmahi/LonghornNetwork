// import javax.swing.*;
// import javax.swing.table.DefaultTableModel;
// import java.awt.*;
// import java.awt.event.*;
// import java.awt.geom.*;
// import java.util.*;
// import java.util.List;

// /**
//  * Main UI class for the Longhorn Network application.
//  * Provides a graphical interface to visualize student connections, roommate assignments,
//  * referral paths, and social interactions.
//  * 
//  * @author Vidmahi Sistla
//  * @version 1.0
//  */
// public class LonghornNetworkUI extends JFrame {
//     // Data
//     private List<UniversityStudent> students;
//     private StudentGraph graph;
//     private List<UniversityStudent> currentPath;
//     private int currentTestCase = 1;
    
//     // UI Components
//     private GraphPanel graphPanel;
//     private JTextArea infoArea;
//     private JComboBox<String> testCaseCombo;
//     private JTextField companyField;
//     private JLabel statusLabel;
    
//     // Colors
//     private static final Color BACKGROUND_COLOR = new Color(18, 18, 18);
//     private static final Color PANEL_COLOR = new Color(30, 30, 30);
//     private static final Color ACCENT_COLOR = new Color(191, 87, 0);
//     private static final Color TEXT_COLOR = new Color(230, 230, 230);
//     private static final Color NODE_COLOR = new Color(66, 135, 245);
//     private static final Color ROOMMATE_COLOR = new Color(76, 175, 80);
//     private static final Color PATH_COLOR = new Color(255, 193, 7);
    
//     /**
//      * Constructs the main UI window.
//      */
//     public LonghornNetworkUI() {
//         setTitle("Longhorn Network - Student Social Platform");
//         setSize(1400, 900);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setLayout(new BorderLayout(10, 10));
//         getContentPane().setBackground(BACKGROUND_COLOR);
        
//         initializeComponents();
//         loadTestCase(1);
        
//         setLocationRelativeTo(null);
//         setVisible(true);
//     }
    
//     /**
//      * Initializes all UI components.
//      */
//     private void initializeComponents() {
//         // Top Control Panel
//         JPanel controlPanel = createControlPanel();
//         add(controlPanel, BorderLayout.NORTH);
        
//         // Center Panel (Graph + Info)
//         JSplitPane centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//         centerSplit.setDividerLocation(900);
//         centerSplit.setBackground(BACKGROUND_COLOR);
        
//         // Graph Panel
//         graphPanel = new GraphPanel();
//         JScrollPane graphScroll = new JScrollPane(graphPanel);
//         graphScroll.setBorder(BorderFactory.createTitledBorder(
//             BorderFactory.createLineBorder(ACCENT_COLOR, 2), 
//             "Student Network Graph",
//             0, 0, new Font("Arial", Font.BOLD, 14), TEXT_COLOR));
//         graphScroll.setBackground(PANEL_COLOR);
//         centerSplit.setLeftComponent(graphScroll);
        
//         // Info Panel
//         JPanel infoPanel = createInfoPanel();
//         centerSplit.setRightComponent(infoPanel);
        
//         add(centerSplit, BorderLayout.CENTER);
        
//         // Status Panel
//         JPanel statusPanel = createStatusPanel();
//         add(statusPanel, BorderLayout.SOUTH);
//     }
    
//     /**
//      * Creates the control panel with buttons and options.
//      */
//     private JPanel createControlPanel() {
//         JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
//         panel.setBackground(PANEL_COLOR);
//         panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
//         // Test Case Selection
//         JLabel testLabel = new JLabel("Test Case:");
//         testLabel.setForeground(TEXT_COLOR);
//         testLabel.setFont(new Font("Arial", Font.BOLD, 12));
//         panel.add(testLabel);
        
//         testCaseCombo = new JComboBox<>(new String[]{"Test Case 1", "Test Case 2", "Test Case 3"});
//         testCaseCombo.setFont(new Font("Arial", Font.PLAIN, 12));
//         panel.add(testCaseCombo);
        
//         // Load Button
//         JButton loadBtn = createStyledButton("Load Data");
//         loadBtn.addActionListener(e -> {
//             int selected = testCaseCombo.getSelectedIndex() + 1;
//             loadTestCase(selected);
//         });
//         panel.add(loadBtn);
        
//         panel.add(new JSeparator(SwingConstants.VERTICAL));
        
//         // Roommate Matching Button
//         JButton roommateBtn = createStyledButton("Assign Roommates");
//         roommateBtn.addActionListener(e -> runRoommateMatching());
//         panel.add(roommateBtn);
        
//         // Referral Path Button
//         JButton referralBtn = createStyledButton("Find Referral Path");
//         referralBtn.addActionListener(e -> runReferralPath());
//         panel.add(referralBtn);
        
//         // Company Input
//         JLabel companyLabel = new JLabel("Company:");
//         companyLabel.setForeground(TEXT_COLOR);
//         companyLabel.setFont(new Font("Arial", Font.BOLD, 12));
//         panel.add(companyLabel);
        
//         companyField = new JTextField("DummyCompany", 12);
//         companyField.setFont(new Font("Arial", Font.PLAIN, 12));
//         panel.add(companyField);
        
//         panel.add(new JSeparator(SwingConstants.VERTICAL));
        
//         // Run All Button
//         JButton runAllBtn = createStyledButton("Run All Tests");
//         runAllBtn.setBackground(ACCENT_COLOR);
//         runAllBtn.addActionListener(e -> runAllTests());
//         panel.add(runAllBtn);
        
//         return panel;
//     }
    
//     /**
//      * Creates the information panel showing student details.
//      */
//     private JPanel createInfoPanel() {
//         JPanel panel = new JPanel(new BorderLayout(5, 5));
//         panel.setBackground(PANEL_COLOR);
//         panel.setBorder(BorderFactory.createTitledBorder(
//             BorderFactory.createLineBorder(ACCENT_COLOR, 2), 
//             "Information Panel",
//             0, 0, new Font("Arial", Font.BOLD, 14), TEXT_COLOR));
        
//         infoArea = new JTextArea();
//         infoArea.setEditable(false);
//         infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
//         infoArea.setBackground(BACKGROUND_COLOR);
//         infoArea.setForeground(TEXT_COLOR);
//         infoArea.setMargin(new Insets(10, 10, 10, 10));
        
//         JScrollPane scroll = new JScrollPane(infoArea);
//         scroll.setBorder(null);
//         panel.add(scroll, BorderLayout.CENTER);
        
//         return panel;
//     }
    
//     /**
//      * Creates the status panel at the bottom.
//      */
//     private JPanel createStatusPanel() {
//         JPanel panel = new JPanel(new BorderLayout());
//         panel.setBackground(PANEL_COLOR);
//         panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
//         statusLabel = new JLabel("Ready. Select a test case and load data.");
//         statusLabel.setForeground(TEXT_COLOR);
//         statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
//         panel.add(statusLabel, BorderLayout.WEST);
        
//         return panel;
//     }
    
//     /**
//      * Creates a styled button with consistent appearance.
//      */
//     private JButton createStyledButton(String text) {
//         JButton btn = new JButton(text);
//         btn.setFont(new Font("Arial", Font.BOLD, 12));
//         btn.setBackground(NODE_COLOR);
//         btn.setForeground(Color.WHITE);
//         btn.setFocusPainted(false);
//         btn.setBorderPainted(false);
//         btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
//         return btn;
//     }
    
//     /**
//      * Loads a specific test case.
//      */
//     private void loadTestCase(int testNumber) {
//         currentTestCase = testNumber;
        
//         switch (testNumber) {
//             case 1:
//                 students = Main.generateTestCase1();
//                 break;
//             case 2:
//                 students = Main.generateTestCase2();
//                 break;
//             case 3:
//                 students = Main.generateTestCase3();
//                 break;
//         }
        
//         graph = new StudentGraph(students);
//         currentPath = null;
        
//         updateInfoPanel();
//         graphPanel.repaint();
//         statusLabel.setText("Loaded Test Case " + testNumber + " with " + students.size() + " students.");
//     }
    
//     /**
//      * Runs roommate matching algorithm.
//      */
//     private void runRoommateMatching() {
//         if (students == null) {
//             JOptionPane.showMessageDialog(this, "Please load data first!");
//             return;
//         }
        
//         GaleShapley.assignRoommates(students);
//         updateInfoPanel();
//         graphPanel.repaint();
//         statusLabel.setText("Roommate matching completed using Gale-Shapley algorithm.");
//     }
    
//     /**
//      * Runs referral path finder.
//      */
//     private void runReferralPath() {
//         if (students == null || students.isEmpty()) {
//             JOptionPane.showMessageDialog(this, "Please load data first!");
//             return;
//         }
        
//         String company = companyField.getText().trim();
//         if (company.isEmpty()) {
//             JOptionPane.showMessageDialog(this, "Please enter a company name!");
//             return;
//         }
        
//         ReferralPathFinder finder = new ReferralPathFinder(graph);
//         currentPath = finder.findReferralPath(students.get(0), company);
        
//         updateInfoPanel();
//         graphPanel.repaint();
        
//         if (currentPath.isEmpty()) {
//             statusLabel.setText("No referral path found for company: " + company);
//         } else {
//             statusLabel.setText("Referral path found! " + currentPath.size() + " students in path.");
//         }
//     }
    
//     /**
//      * Runs all tests (matching + referral path).
//      */
//     private void runAllTests() {
//         runRoommateMatching();
//         try {
//             Thread.sleep(500);
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }
//         runReferralPath();
//     }
    
//     /**
//      * Updates the information panel with current data.
//      */
//     private void updateInfoPanel() {
//         StringBuilder sb = new StringBuilder();
//         sb.append("═══════════════════════════════════════\n");
//         sb.append("  TEST CASE ").append(currentTestCase).append(" - STUDENT INFORMATION\n");
//         sb.append("═══════════════════════════════════════\n\n");
        
//         // Student Details
//         sb.append("STUDENTS:\n");
//         sb.append("─────────────────────────────────────\n");
//         for (UniversityStudent s : students) {
//             sb.append(String.format("• %s (%d, %s)\n", s.name, s.age, s.major));
//             sb.append(String.format("  GPA: %.2f | Year: %d\n", s.gpa, s.year));
//             sb.append(String.format("  Internships: %s\n", 
//                 s.previousInternships.isEmpty() ? "None" : s.previousInternships));
//             sb.append("\n");
//         }
        
//         // Roommate Assignments
//         sb.append("\n═══════════════════════════════════════\n");
//         sb.append("  ROOMMATE ASSIGNMENTS\n");
//         sb.append("═══════════════════════════════════════\n\n");
//         Set<UniversityStudent> paired = new HashSet<>();
//         for (UniversityStudent s : students) {
//             if (s.getRoommate() != null && !paired.contains(s)) {
//                 sb.append(String.format("✓ %s ↔ %s\n", s.name, s.getRoommate().name));
//                 paired.add(s);
//                 paired.add(s.getRoommate());
//             }
//         }
//         for (UniversityStudent s : students) {
//             if (s.getRoommate() == null && !s.roommatePreferences.isEmpty()) {
//                 sb.append(String.format("✗ %s (unpaired)\n", s.name));
//             }
//         }
        
//         // Referral Path
//         if (currentPath != null && !currentPath.isEmpty()) {
//             sb.append("\n═══════════════════════════════════════\n");
//             sb.append("  REFERRAL PATH TO ").append(companyField.getText()).append("\n");
//             sb.append("═══════════════════════════════════════\n\n");
//             for (int i = 0; i < currentPath.size(); i++) {
//                 sb.append(currentPath.get(i).name);
//                 if (i < currentPath.size() - 1) {
//                     sb.append(" → ");
//                 }
//             }
//             sb.append("\n");
//         }
        
//         // Friend Requests & Chat
//         sb.append("\n═══════════════════════════════════════\n");
//         sb.append("  SOCIAL INTERACTIONS\n");
//         sb.append("═══════════════════════════════════════\n\n");
//         for (UniversityStudent s : students) {
//             List<UniversityStudent> friends = s.getFriends();
//             if (!friends.isEmpty()) {
//                 sb.append(String.format("%s's friends: ", s.name));
//                 for (int i = 0; i < friends.size(); i++) {
//                     sb.append(friends.get(i).name);
//                     if (i < friends.size() - 1) sb.append(", ");
//                 }
//                 sb.append("\n");
//             }
//         }
        
//         infoArea.setText(sb.toString());
//         infoArea.setCaretPosition(0);
//     }
    
//     /**
//      * Custom panel for drawing the student graph.
//      */
//     private class GraphPanel extends JPanel {
//         private Map<UniversityStudent, Point> positions;
        
//         public GraphPanel() {
//             setPreferredSize(new Dimension(850, 700));
//             setBackground(BACKGROUND_COLOR);
//             positions = new HashMap<>();
//         }
        
//         @Override
//         protected void paintComponent(Graphics g) {
//             super.paintComponent(g);
//             if (students == null || students.isEmpty()) {
//                 drawEmptyState(g);
//                 return;
//             }
            
//             Graphics2D g2 = (Graphics2D) g;
//             g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
//             // Calculate positions if not done
//             if (positions.isEmpty()) {
//                 calculatePositions();
//             }
            
//             // Draw edges first
//             drawEdges(g2);
            
//             // Draw nodes
//             drawNodes(g2);
            
//             // Draw legend
//             drawLegend(g2);
//         }
        
//         /**
//          * Draws empty state message.
//          */
//         private void drawEmptyState(Graphics g) {
//             g.setColor(TEXT_COLOR);
//             g.setFont(new Font("Arial", Font.BOLD, 18));
//             String msg = "No data loaded. Select a test case and click 'Load Data'.";
//             FontMetrics fm = g.getFontMetrics();
//             int x = (getWidth() - fm.stringWidth(msg)) / 2;
//             int y = getHeight() / 2;
//             g.drawString(msg, x, y);
//         }
        
//         /**
//          * Calculates node positions using circular layout.
//          */
//         private void calculatePositions() {
//             positions.clear();
//             int centerX = getWidth() / 2;
//             int centerY = getHeight() / 2;
//             int radius = Math.min(getWidth(), getHeight()) / 3;
            
//             int n = students.size();
//             for (int i = 0; i < n; i++) {
//                 double angle = 2 * Math.PI * i / n - Math.PI / 2;
//                 int x = centerX + (int) (radius * Math.cos(angle));
//                 int y = centerY + (int) (radius * Math.sin(angle));
//                 positions.put(students.get(i), new Point(x, y));
//             }
//         }
        
//         /**
//          * Draws edges between connected students.
//          */
//         private void drawEdges(Graphics2D g2) {
//             g2.setStroke(new BasicStroke(2));
            
//             Set<String> drawnEdges = new HashSet<>();
            
//             for (UniversityStudent s : students) {
//                 Point p1 = positions.get(s);
//                 List<StudentGraph.Edge> edges = graph.getNeighbors(s);
                
//                 for (StudentGraph.Edge edge : edges) {
//                     UniversityStudent neighbor = edge.neighbor;
//                     String edgeKey = getEdgeKey(s, neighbor);
                    
//                     if (drawnEdges.contains(edgeKey)) continue;
//                     drawnEdges.add(edgeKey);
                    
//                     Point p2 = positions.get(neighbor);
                    
//                     // Determine edge color
//                     Color edgeColor = Color.GRAY;
//                     if (currentPath != null && isInPath(s, neighbor)) {
//                         edgeColor = PATH_COLOR;
//                         g2.setStroke(new BasicStroke(4));
//                     } else if (s.getRoommate() == neighbor) {
//                         edgeColor = ROOMMATE_COLOR;
//                         g2.setStroke(new BasicStroke(3));
//                     } else {
//                         g2.setStroke(new BasicStroke(2));
//                     }
                    
//                     g2.setColor(edgeColor);
//                     g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                    
//                     // Draw weight label
//                     int midX = (p1.x + p2.x) / 2;
//                     int midY = (p1.y + p2.y) / 2;
//                     g2.setColor(TEXT_COLOR);
//                     g2.setFont(new Font("Arial", Font.BOLD, 11));
//                     g2.drawString(String.valueOf(edge.weight), midX + 5, midY - 5);
//                 }
//             }
//         }
        
//         /**
//          * Draws student nodes.
//          */
//         private void drawNodes(Graphics2D g2) {
//             int nodeSize = 60;
            
//             for (UniversityStudent s : students) {
//                 Point p = positions.get(s);
                
//                 // Determine node color
//                 Color nodeColor = NODE_COLOR;
//                 if (currentPath != null && currentPath.contains(s)) {
//                     nodeColor = PATH_COLOR;
//                 } else if (s.getRoommate() != null) {
//                     nodeColor = ROOMMATE_COLOR;
//                 }
                
//                 // Draw node circle
//                 g2.setColor(nodeColor);
//                 g2.fillOval(p.x - nodeSize/2, p.y - nodeSize/2, nodeSize, nodeSize);
                
//                 // Draw border
//                 g2.setColor(Color.WHITE);
//                 g2.setStroke(new BasicStroke(2));
//                 g2.drawOval(p.x - nodeSize/2, p.y - nodeSize/2, nodeSize, nodeSize);
                
//                 // Draw name
//                 g2.setColor(Color.WHITE);
//                 g2.setFont(new Font("Arial", Font.BOLD, 12));
//                 FontMetrics fm = g2.getFontMetrics();
//                 int textWidth = fm.stringWidth(s.name);
//                 g2.drawString(s.name, p.x - textWidth/2, p.y + 5);
//             }
//         }
        
//         /**
//          * Draws legend explaining colors.
//          */
//         private void drawLegend(Graphics2D g2) {
//             int x = 20;
//             int y = getHeight() - 100;
            
//             g2.setColor(new Color(0, 0, 0, 180));
//             g2.fillRoundRect(x - 10, y - 25, 200, 90, 10, 10);
            
//             g2.setFont(new Font("Arial", Font.BOLD, 12));
//             g2.setColor(TEXT_COLOR);
//             g2.drawString("Legend:", x, y);
            
//             y += 20;
//             drawLegendItem(g2, x, y, NODE_COLOR, "Regular Connection");
//             y += 20;
//             drawLegendItem(g2, x, y, ROOMMATE_COLOR, "Roommates");
//             y += 20;
//             drawLegendItem(g2, x, y, PATH_COLOR, "Referral Path");
//         }
        
//         /**
//          * Draws a single legend item.
//          */
//         private void drawLegendItem(Graphics2D g2, int x, int y, Color color, String label) {
//             g2.setColor(color);
//             g2.fillOval(x, y - 8, 12, 12);
//             g2.setColor(TEXT_COLOR);
//             g2.drawString(label, x + 20, y);
//         }
        
//         /**
//          * Checks if an edge is in the current path.
//          */
//         private boolean isInPath(UniversityStudent s1, UniversityStudent s2) {
//             if (currentPath == null || currentPath.size() < 2) return false;
            
//             for (int i = 0; i < currentPath.size() - 1; i++) {
//                 UniversityStudent a = currentPath.get(i);
//                 UniversityStudent b = currentPath.get(i + 1);
//                 if ((a == s1 && b == s2) || (a == s2 && b == s1)) {
//                     return true;
//                 }
//             }
//             return false;
//         }
        
//         /**
//          * Creates a unique edge key for tracking drawn edges.
//          */
//         private String getEdgeKey(UniversityStudent s1, UniversityStudent s2) {
//             String name1 = s1.name;
//             String name2 = s2.name;
//             return name1.compareTo(name2) < 0 ? name1 + "-" + name2 : name2 + "-" + name1;
//         }
//     }
    
//     /**
//      * Main method to launch the UI.
//      */
//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(() -> new LonghornNetworkUI());
//     }
// }


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

/**
 * Enhanced UI class for the Longhorn Network application.
 * Provides a comprehensive graphical interface with better visualization of
 * roommates, chat history, and social interactions.
 * 
 * @author Vidmahi Sistla
 * @version 2.0
 */
public class LonghornNetworkUI extends JFrame {
    // Data
    private List<UniversityStudent> students;
    private StudentGraph graph;
    private List<UniversityStudent> currentPath;
    private int currentTestCase = 1;
    
    // UI Components
    private GraphPanel graphPanel;
    private JTextArea studentInfoArea;
    private JTextArea roommateInfoArea;
    private JTextArea chatHistoryArea;
    private JTextArea socialInfoArea;
    private JComboBox<String> testCaseCombo;
    private JTextField companyField;
    private JLabel statusLabel;
    private JTabbedPane infoPanelTabs;
    
    // Colors
    private static final Color BACKGROUND_COLOR = new Color(18, 18, 18);
    private static final Color PANEL_COLOR = new Color(30, 30, 30);
    private static final Color ACCENT_COLOR = new Color(191, 87, 0);
    private static final Color TEXT_COLOR = new Color(230, 230, 230);
    private static final Color NODE_COLOR = new Color(66, 135, 245);
    private static final Color ROOMMATE_COLOR = new Color(76, 175, 80);
    private static final Color PATH_COLOR = new Color(255, 193, 7);
    private static final Color ISOLATED_COLOR = new Color(156, 39, 176);
    
    /**
     * Constructs the main UI window.
     */
    public LonghornNetworkUI() {
        setTitle("🎓 Longhorn Network - Student Social Platform");
        setSize(1600, 950);
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
        
        // Center Panel (Graph + Info Tabs)
        JSplitPane centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        centerSplit.setDividerLocation(950);
        centerSplit.setBackground(BACKGROUND_COLOR);
        
        // Graph Panel
        graphPanel = new GraphPanel();
        JScrollPane graphScroll = new JScrollPane(graphPanel);
        graphScroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 2), 
            "Student Network Graph",
            0, 0, new Font("Arial", Font.BOLD, 16), TEXT_COLOR));
        graphScroll.setBackground(PANEL_COLOR);
        centerSplit.setLeftComponent(graphScroll);
        
        // Info Panel with Tabs
        JPanel infoPanel = createTabbedInfoPanel();
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
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // First row: Test case selection
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row1.setBackground(PANEL_COLOR);
        
        JLabel testLabel = new JLabel("Test Case:");
        testLabel.setForeground(TEXT_COLOR);
        testLabel.setFont(new Font("Arial", Font.BOLD, 14));
        row1.add(testLabel);
        
        testCaseCombo = new JComboBox<>(new String[]{
            "Test Case 1 (6 students)", 
            "Test Case 2 (3 students)", 
            "Test Case 3 (3 students)"
        });
        testCaseCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        testCaseCombo.setPreferredSize(new Dimension(200, 30));
        row1.add(testCaseCombo);
        
        JButton loadBtn = createStyledButton("Load Data", NODE_COLOR);
        loadBtn.addActionListener(e -> {
            int selected = testCaseCombo.getSelectedIndex() + 1;
            loadTestCase(selected);
        });
        row1.add(loadBtn);
        
        panel.add(row1);
        
        // Second row: Actions
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row2.setBackground(PANEL_COLOR);
        
        JButton roommateBtn = createStyledButton("Assign Roommates", ROOMMATE_COLOR);
        roommateBtn.addActionListener(e -> runRoommateMatching());
        row2.add(roommateBtn);
        
        JButton referralBtn = createStyledButton("Find Referral Path", PATH_COLOR);
        referralBtn.addActionListener(e -> runReferralPath());
        row2.add(referralBtn);
        
        JLabel companyLabel = new JLabel("Company:");
        companyLabel.setForeground(TEXT_COLOR);
        companyLabel.setFont(new Font("Arial", Font.BOLD, 13));
        row2.add(companyLabel);
        
        companyField = new JTextField("DummyCompany", 15);
        companyField.setFont(new Font("Arial", Font.PLAIN, 13));
        row2.add(companyField);
        
        JButton runAllBtn = createStyledButton("Run All Tests", ACCENT_COLOR);
        runAllBtn.addActionListener(e -> runAllTests());
        row2.add(runAllBtn);
        
        panel.add(row2);
        
        return panel;
    }
    
    /**
     * Creates tabbed info panel for better organization.
     */
    private JPanel createTabbedInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_COLOR);
        
        infoPanelTabs = new JTabbedPane();
        infoPanelTabs.setBackground(PANEL_COLOR);
        infoPanelTabs.setForeground(TEXT_COLOR);
        infoPanelTabs.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Tab 1: Student Info
        studentInfoArea = createStyledTextArea();
        JScrollPane studentScroll = new JScrollPane(studentInfoArea);
        infoPanelTabs.addTab("Students", studentScroll);
        
        // Tab 2: Roommate Assignments
        roommateInfoArea = createStyledTextArea();
        JScrollPane roommateScroll = new JScrollPane(roommateInfoArea);
        infoPanelTabs.addTab("Roommates", roommateScroll);
        
        // Tab 3: Chat History
        chatHistoryArea = createStyledTextArea();
        JScrollPane chatScroll = new JScrollPane(chatHistoryArea);
        infoPanelTabs.addTab("Chat History", chatScroll);
        
        // Tab 4: Social Info
        socialInfoArea = createStyledTextArea();
        JScrollPane socialScroll = new JScrollPane(socialInfoArea);
        infoPanelTabs.addTab("Friends", socialScroll);
        
        panel.add(infoPanelTabs, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates a styled text area.
     */
    private JTextArea createStyledTextArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setBackground(BACKGROUND_COLOR);
        area.setForeground(TEXT_COLOR);
        area.setMargin(new Insets(10, 10, 10, 10));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }
    
    /**
     * Creates the status panel at the bottom.
     */
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        statusLabel = new JLabel("✅ Ready. Select a test case and load data.");
        statusLabel.setForeground(TEXT_COLOR);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(statusLabel, BorderLayout.WEST);
        
        return panel;
    }
    
    /**
     * Creates a styled button with emoji and color.
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 35));
        
        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });
        
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
        
        updateAllInfoPanels();
        graphPanel.repaint();
        statusLabel.setText("✅ Loaded Test Case " + testNumber + " with " + students.size() + " students.");
    }
    
    /**
     * Runs roommate matching algorithm.
     */
    private void runRoommateMatching() {
        if (students == null) {
            JOptionPane.showMessageDialog(this, "Please load data first!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        GaleShapley.assignRoommates(students);
        updateAllInfoPanels();
        graphPanel.repaint();
        statusLabel.setText("✅ Roommate matching completed using Gale-Shapley algorithm.");
    }
    
    /**
     * Runs referral path finder.
     */
    private void runReferralPath() {
        if (students == null || students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please load data first!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String company = companyField.getText().trim();
        if (company.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a company name!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ReferralPathFinder finder = new ReferralPathFinder(graph);
        currentPath = finder.findReferralPath(students.get(0), company);
        
        updateAllInfoPanels();
        graphPanel.repaint();
        
        if (currentPath.isEmpty()) {
            statusLabel.setText("❌ No referral path found for company: " + company);
        } else {
            statusLabel.setText("✅ Referral path found! " + currentPath.size() + " students in path.");
        }
    }
    
    /**
     * Runs all tests and simulates threading.
     */
    private void runAllTests() {
        runRoommateMatching();
        
        // Simulate some friend requests and chats
        if (students.size() >= 2) {
            try {
                java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(4);
                executor.submit(new FriendRequestThread(students.get(0), students.get(1)));
                executor.submit(new ChatThread(students.get(0), students.get(1), "Hey! Want to study together?"));
                if (students.size() >= 3) {
                    executor.submit(new FriendRequestThread(students.get(1), students.get(2)));
                    executor.submit(new ChatThread(students.get(1), students.get(2), "Sure! What time works?"));
                }
                executor.shutdown();
                executor.awaitTermination(2, java.util.concurrent.TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        runReferralPath();
        updateAllInfoPanels();
    }
    
    /**
     * Updates all information panels.
     */
    private void updateAllInfoPanels() {
        updateStudentInfo();
        updateRoommateInfo();
        updateChatHistory();
        updateSocialInfo();
    }
    
    /**
     * Updates student information tab.
     */
    private void updateStudentInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════\n");
        sb.append("         TEST CASE ").append(currentTestCase).append(" - STUDENT INFORMATION\n");
        sb.append("═══════════════════════════════════════════════════\n\n");
        
        for (int i = 0; i < students.size(); i++) {
            UniversityStudent s = students.get(i);
            sb.append(String.format(" %d. %s\n", i+1, s.name));
            sb.append(String.format("   Age: %d | Gender: %s | Year: %d\n", s.age, s.gender, s.year));
            sb.append(String.format("   Major: %s\n", s.major));
            sb.append(String.format("   GPA: %.2f\n", s.gpa));
            sb.append(String.format("   Internships: %s\n", 
                s.previousInternships.isEmpty() ? "None" : String.join(", ", s.previousInternships)));
            sb.append(String.format("   Preferences: %s\n", 
                s.roommatePreferences.isEmpty() ? "None" : String.join(", ", s.roommatePreferences)));
            sb.append("\n");
        }
        
        studentInfoArea.setText(sb.toString());
        studentInfoArea.setCaretPosition(0);
    }
    
    /**
     * Updates roommate information tab.
     */
    private void updateRoommateInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════\n");
        sb.append("              ROOMMATE ASSIGNMENTS\n");
        sb.append("═══════════════════════════════════════════════════\n\n");
        
        Set<UniversityStudent> paired = new HashSet<>();
        int pairCount = 0;
        
        for (UniversityStudent s : students) {
            if (s.getRoommate() != null && !paired.contains(s)) {
                pairCount++;
                sb.append(String.format("Pair %d:\n", pairCount));
                sb.append(String.format("   • %s ↔ %s\n", s.name, s.getRoommate().name));
                sb.append(String.format("   Connection Strength: %d\n", 
                    s.calculateConnectionStrength(s.getRoommate())));
                sb.append("\n");
                paired.add(s);
                paired.add(s.getRoommate());
            }
        }
        
        sb.append("\n─────────────────────────────────────────────────\n");
        sb.append("UNPAIRED STUDENTS:\n");
        sb.append("─────────────────────────────────────────────────\n\n");
        
        boolean hasUnpaired = false;
        for (UniversityStudent s : students) {
            if (s.getRoommate() == null) {
                sb.append(String.format("❌ %s", s.name));
                if (s.roommatePreferences.isEmpty()) {
                    sb.append(" (no preferences)\n");
                } else {
                    sb.append(String.format(" (wanted: %s)\n", 
                        String.join(", ", s.roommatePreferences)));
                }
                hasUnpaired = true;
            }
        }
        
        if (!hasUnpaired) {
            sb.append("   None - all students paired!\n");
        }
        
        if (currentPath != null && !currentPath.isEmpty()) {
            sb.append("\n\n═══════════════════════════════════════════════════\n");
            sb.append("         REFERRAL PATH TO ").append(companyField.getText()).append("\n");
            sb.append("═══════════════════════════════════════════════════\n\n");
            sb.append("Path: ");
            for (int i = 0; i < currentPath.size(); i++) {
                sb.append(currentPath.get(i).name);
                if (i < currentPath.size() - 1) {
                    sb.append(" → ");
                }
            }
            sb.append(String.format("\n\nPath Length: %d student(s)\n", currentPath.size()));
        }
        
        roommateInfoArea.setText(sb.toString());
        roommateInfoArea.setCaretPosition(0);
    }
    
    /**
     * Updates chat history tab.
     */
    private void updateChatHistory() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════\n");
        sb.append("                 CHAT HISTORY\n");
        sb.append("═══════════════════════════════════════════════════\n\n");
        
        boolean hasChats = false;
        
        for (UniversityStudent s : students) {
            for (UniversityStudent other : students) {
                if (s == other) continue;
                
                List<String> chats = s.getChatHistory(other);
                if (!chats.isEmpty()) {
                    hasChats = true;
                    sb.append(String.format(" %s → %s:\n", s.name, other.name));
                    for (String msg : chats) {
                        sb.append(String.format("   \"%s\"\n", msg));
                    }
                    sb.append("\n");
                }
            }
        }
        
        if (!hasChats) {
            sb.append("No chat messages yet.\n\n");
            sb.append("Click 'Run All Tests' to simulate friend requests\n");
            sb.append("and chat conversations between students!\n");
        }
        
        chatHistoryArea.setText(sb.toString());
        chatHistoryArea.setCaretPosition(0);
    }
    
    /**
     * Updates social information tab.
     */
    private void updateSocialInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════\n");
        sb.append("              FRIEND CONNECTIONS\n");
        sb.append("═══════════════════════════════════════════════════\n\n");
        
        boolean hasFriends = false;
        
        for (UniversityStudent s : students) {
            List<UniversityStudent> friends = s.getFriends();
            if (!friends.isEmpty()) {
                hasFriends = true;
                sb.append(String.format(" %s's Friends (%d):\n", s.name, friends.size()));
                for (UniversityStudent friend : friends) {
                    sb.append(String.format("   • %s\n", friend.name));
                }
                sb.append("\n");
            }
        }
        
        if (!hasFriends) {
            sb.append("👥 No friendships established yet.\n\n");
            sb.append("Click 'Run All Tests' to simulate friend requests!\n");
        }
        
        socialInfoArea.setText(sb.toString());
        socialInfoArea.setCaretPosition(0);
    }
    
    /**
     * Custom panel for drawing the student graph.
     */
    private class GraphPanel extends JPanel {
        private Map<UniversityStudent, Point> positions;
        
        public GraphPanel() {
            setPreferredSize(new Dimension(900, 750));
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
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            // Calculate positions
            calculatePositions();
            
            // Draw edges first
            drawEdges(g2);
            
            // Draw nodes
            drawNodes(g2);
            
            // Draw legend
            drawLegend(g2);
        }
        
        private void drawEmptyState(Graphics g) {
            g.setColor(TEXT_COLOR);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            String msg = "No data loaded. Select a test case and click 'Load Data'.";
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(msg)) / 2;
            int y = getHeight() / 2;
            g.drawString(msg, x, y);
        }
        
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
        
        private void drawEdges(Graphics2D g2) {
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
                    
                    // Determine edge color and thickness
                    Color edgeColor;
                    int thickness;
                    
                    if (currentPath != null && isInPath(s, neighbor)) {
                        edgeColor = PATH_COLOR;
                        thickness = 5;
                    } else if (s.getRoommate() == neighbor) {
                        edgeColor = ROOMMATE_COLOR;
                        thickness = 4;
                    } else {
                        edgeColor = new Color(100, 100, 100);
                        thickness = 2;
                    }
                    
                    g2.setColor(edgeColor);
                    g2.setStroke(new BasicStroke(thickness));
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                    
                    // Draw weight label
                    int midX = (p1.x + p2.x) / 2;
                    int midY = (p1.y + p2.y) / 2;
                    
                    // Background for weight
                    g2.setColor(BACKGROUND_COLOR);
                    g2.fillOval(midX - 12, midY - 12, 24, 24);
                    
                    g2.setColor(TEXT_COLOR);
                    g2.setFont(new Font("Arial", Font.BOLD, 12));
                    String weight = String.valueOf(edge.weight);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(weight, midX - fm.stringWidth(weight)/2, midY + 4);
                }
            }
        }
        
        private void drawNodes(Graphics2D g2) {
            int nodeSize = 70;
            
            for (UniversityStudent s : students) {
                Point p = positions.get(s);
                
                // Determine node color
                Color nodeColor;
                String prefix = "";
                
                if (currentPath != null && currentPath.contains(s)) {
                    nodeColor = PATH_COLOR;
                    //prefix = "🔍 ";
                } else if (s.getRoommate() != null) {
                    nodeColor = ROOMMATE_COLOR;
                    //prefix = "🏠 ";
                } else if (graph.getNeighbors(s).isEmpty()) {
                    nodeColor = ISOLATED_COLOR;
                    prefix = "⭕ ";
                } else {
                    nodeColor = NODE_COLOR;
                }
                
                // Draw shadow
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillOval(p.x - nodeSize/2 + 3, p.y - nodeSize/2 + 3, nodeSize, nodeSize);
                
                // Draw node circle
                g2.setColor(nodeColor);
                g2.fillOval(p.x - nodeSize/2, p.y - nodeSize/2, nodeSize, nodeSize);
                
                // Draw border
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3));
                g2.drawOval(p.x - nodeSize/2, p.y - nodeSize/2, nodeSize, nodeSize);
                
                // Draw name
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 13));
                String displayName = prefix + s.name;
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(displayName);
                g2.drawString(displayName, p.x - textWidth/2, p.y + 5);
            }
        }
        
        private void drawLegend(Graphics2D g2) {
            int x = 20;
            int y = getHeight() - 130;
            
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRoundRect(x - 10, y - 25, 230, 120, 15, 15);
            
            g2.setFont(new Font("Arial", Font.BOLD, 13));
            g2.setColor(TEXT_COLOR);
            g2.drawString("Legend:", x, y);
            
            y += 22;
            drawLegendItem(g2, x, y, NODE_COLOR, "Regular Student");
            y += 22;
            drawLegendItem(g2, x, y, ROOMMATE_COLOR, "Has Roommate");
            y += 22;
            drawLegendItem(g2, x, y, PATH_COLOR, "In Referral Path");
            y += 22;
            drawLegendItem(g2, x, y, ISOLATED_COLOR, "⭕ Isolated (No Connections)");
        }
        
        private void drawLegendItem(Graphics2D g2, int x, int y, Color color, String label) {
            g2.setColor(color);
            g2.fillOval(x, y - 8, 14, 14);
            g2.setColor(Color.WHITE);
            g2.drawOval(x, y - 8, 14, 14);
            g2.setColor(TEXT_COLOR);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString(label, x + 22, y);
        }
        
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
        
        private String getEdgeKey(UniversityStudent s1, UniversityStudent s2) {
            return s1.name.compareTo(s2.name) < 0 ? 
                   s1.name + "-" + s2.name : s2.name + "-" + s1.name;
        }
    }
    
    /**
     * Main method to launch the UI.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LonghornNetworkUI());
    }
}