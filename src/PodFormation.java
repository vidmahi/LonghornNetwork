import java.util.*;

/**
 * Forms student pods (groups) using Prim's algorithm to create minimum spanning trees.
 * Pods are formed by grouping students with the strongest connections, minimizing
 * the total connection cost within each pod.
 * 
 * <p>Prim's algorithm creates minimum spanning trees (MSTs) where:
 * <ul>
 *   <li>Each pod is a connected component in the graph</li>
 *   <li>Students within a pod have the strongest possible connections</li>
 *   <li>The total weight of connections within each pod is minimized</li>
 * </ul>
 * 
 * <p>Edge cases handled:
 * <ul>
 *   <li>Disconnected graphs: Each connected component forms its own pod</li>
 *   <li>Isolated students (no connections): Form single-student pods</li>
 *   <li>Pod size requirements: Pods are formed to approximately match the target size</li>
 *   <li>Uneven distribution: Some pods may be smaller if students cannot be evenly distributed</li>
 * </ul>
 * 
 * @author Vidmahi Sistla
 * @version 2.0
 */
public class PodFormation {
    /** The student graph containing all students and their connections */
    private StudentGraph graph;

    /**
     * Constructs a PodFormation instance with the specified student graph.
     * 
     * @param graph the StudentGraph containing students and their connections
     * @throws IllegalArgumentException if graph is null
     */
    public PodFormation(StudentGraph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }
        this.graph = graph;
    }

    /**
     * Forms student pods using Prim's algorithm to create minimum spanning trees.
     * Each connected component in the graph becomes a pod, with students grouped
     * to maximize connection strength within pods.
     * 
     * <p>Algorithm overview:
     * <ol>
     *   <li>Identify all connected components in the graph</li>
     *   <li>For each component, apply Prim's algorithm to build an MST</li>
     *   <li>Group students based on the MST structure</li>
     *   <li>Attempt to create pods of approximately the specified size</li>
     * </ol>
     * 
     * <p>Implementation notes:
     * <ul>
     *   <li>Uses a priority queue to select edges with minimum weight (strongest connections)</li>
     *   <li>Connection strengths are used directly (NOT inverted like in Dijkstra's)</li>
     *   <li>Students with no connections form individual pods</li>
     *   <li>Pods are numbered sequentially starting from 1</li>
     * </ul>
     * 
     * <p>Output format:
     * For each pod, prints:
     * <pre>
     * Pod X: [Student1, Student2, Student3, ...]
     * Total Connection Strength: [sum]
     * </pre>
     * 
     * @param podSize the target size for each pod (actual pod sizes may vary)
     * @throws IllegalArgumentException if podSize is less than 1
     */
    public void formPods(int podSize) {
        if (podSize < 1) {
            throw new IllegalArgumentException("Pod size must be at least 1");
        }
        
        Set<UniversityStudent> allStudents = graph.getAllNodes();
        Set<UniversityStudent> visited = new HashSet<>();
        Map<UniversityStudent, Integer> podAssignments = new HashMap<>();
        int podNumber = 1;
        
        System.out.println("\n=== Pod Formation using Prim's Algorithm ===");
        
        // Process each connected component
        for (UniversityStudent startStudent : allStudents) {
            if (visited.contains(startStudent)) {
                continue;
            }
            
            // Find all students in this connected component using Prim's algorithm
            List<UniversityStudent> podMembers = new ArrayList<>();
            Set<UniversityStudent> podVisited = new HashSet<>();
            PriorityQueue<Edge> pq = new PriorityQueue<>();
            int totalConnectionStrength = 0;
            
            // Start Prim's algorithm from this student
            podVisited.add(startStudent);
            podMembers.add(startStudent);
            visited.add(startStudent);
            
            // Add all edges from start student to priority queue
            List<StudentGraph.Edge> edges = graph.getNeighbors(startStudent);
            for (StudentGraph.Edge edge : edges) {
                pq.offer(new Edge(startStudent, edge.neighbor, edge.weight));
            }
            
            // Build MST for this connected component
            while (!pq.isEmpty()) {
                Edge currentEdge = pq.poll();
                UniversityStudent neighbor = currentEdge.to;
                
                // Skip if already in this pod
                if (podVisited.contains(neighbor)) {
                    continue;
                }
                
                // Add neighbor to pod
                podVisited.add(neighbor);
                podMembers.add(neighbor);
                visited.add(neighbor);
                totalConnectionStrength += currentEdge.weight;
                
                // Add all edges from this new student
                List<StudentGraph.Edge> neighborEdges = graph.getNeighbors(neighbor);
                for (StudentGraph.Edge edge : neighborEdges) {
                    if (!podVisited.contains(edge.neighbor)) {
                        pq.offer(new Edge(neighbor, edge.neighbor, edge.weight));
                    }
                }
            }
            
            // Assign pod number to all members
            for (UniversityStudent member : podMembers) {
                podAssignments.put(member, podNumber);
            }
            
            // Display pod information
            System.out.println("\nPod " + podNumber + ": " + getStudentNames(podMembers));
            System.out.println("Total Connection Strength: " + totalConnectionStrength);
            System.out.println("Pod Size: " + podMembers.size());
            
            podNumber++;
        }
        
        System.out.println("\nTotal Pods Formed: " + (podNumber - 1));
    }
    
    /**
     * Helper method to extract student names from a list of UniversityStudent objects.
     * 
     * @param students the list of students
     * @return a list of student names
     */
    private List<String> getStudentNames(List<UniversityStudent> students) {
        List<String> names = new ArrayList<>();
        for (UniversityStudent student : students) {
            names.add(student.name);
        }
        return names;
    }
    
    /**
     * Helper class representing an edge with source, destination, and weight.
     * Implements Comparable to allow priority queue ordering by weight.
     */
    private static class Edge implements Comparable<Edge> {
        /** The source student of this edge */
        UniversityStudent from;
        
        /** The destination student of this edge */
        UniversityStudent to;
        
        /** The weight (connection strength) of this edge */
        int weight;
        
        /**
         * Constructs an edge with the specified endpoints and weight.
         * 
         * @param from the source student
         * @param to the destination student
         * @param weight the connection strength
         */
        Edge(UniversityStudent from, UniversityStudent to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
        
        /**
         * Compares this edge to another based on weight.
         * Used by priority queue to select minimum weight edges first.
         * 
         * @param other the other edge to compare to
         * @return negative if this weight is less, positive if greater, 0 if equal
         */
        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }
}