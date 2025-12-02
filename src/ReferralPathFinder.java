import java.util.*;

/**
 * Finds the shortest referral path to students who have interned at a target company
 * using Dijkstra's algorithm. The path represents the strongest chain of connections
 * between the starting student and someone with the desired internship experience.
 * 
 * <p>The algorithm prioritizes stronger connections as shorter paths by inverting
 * the edge weights. A connection strength of 10 becomes a path cost of 0 (strongest),
 * while a connection strength of 1 becomes a path cost of 9 (weakest).</p>
 * 
 * <p>Edge cases handled:
 * <ul>
 *   <li>No student with the target internship (returns empty path)</li>
 *   <li>Disconnected graphs with no path to target (returns empty path)</li>
 *   <li>Starting student already has the internship (returns single-node path)</li>
 *   <li>Multiple students with the same target internship (returns shortest path to any)</li>
 * </ul>
 * 
 * @author Vidmahi Sistla
 * @version 2.0
 */
public class ReferralPathFinder {
    /** The student graph to search for referral paths */
    private StudentGraph graph;
    
    /** Maximum possible connection strength (used for weight inversion) */
    private static final int MAX_CONNECTION_STRENGTH = 10;

    /**
     * Constructs a ReferralPathFinder with the specified student graph.
     * 
     * @param graph the StudentGraph containing all students and their connections
     * @throws IllegalArgumentException if graph is null
     */
    public ReferralPathFinder(StudentGraph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }
        this.graph = graph;
    }

    /**
     * Finds the shortest (strongest connection) path from a starting student to any student
     * who has previously interned at the specified company using Dijkstra's algorithm.
     * 
     * <p>Algorithm details:
     * <ul>
     *   <li>Edge weights are inverted (MAX_CONNECTION_STRENGTH - weight) to treat
     *       stronger connections as shorter paths</li>
     *   <li>Uses a priority queue to always expand the most promising path first</li>
     *   <li>Tracks the shortest distance and path to each student</li>
     *   <li>Stops when a student with the target internship is found</li>
     * </ul>
     * 
     * <p>Path representation:
     * The returned list represents the path from start to target student:
     * [StartStudent, IntermediateStudent1, IntermediateStudent2, ..., TargetStudent]
     * 
     * @param start the starting UniversityStudent to begin the search from
     * @param targetCompany the name of the company to find a referral path to
     * @return a list of UniversityStudent objects representing the path from start
     *         to a student with the target internship, or an empty list if no path exists
     * @throws IllegalArgumentException if start is null or targetCompany is null/empty
     */
    public List<UniversityStudent> findReferralPath(UniversityStudent start, String targetCompany) {
        if (start == null) {
            throw new IllegalArgumentException("Start student cannot be null");
        }
        if (targetCompany == null || targetCompany.isEmpty()) {
            throw new IllegalArgumentException("Target company cannot be null or empty");
        }
        
        // Check if start student already has the internship
        if (start.previousInternships.contains(targetCompany)) {
            List<UniversityStudent> path = new ArrayList<>();
            path.add(start);
            return path;
        }
        
        // Initialize data structures for Dijkstra's algorithm
        Map<UniversityStudent, Integer> distances = new HashMap<>();
        Map<UniversityStudent, UniversityStudent> previous = new HashMap<>();
        Set<UniversityStudent> visited = new HashSet<>();
        
        // Priority queue: (distance, student)
        PriorityQueue<StudentDistance> pq = new PriorityQueue<>();
        
        // Initialize distances
        for (UniversityStudent student : graph.getAllNodes()) {
            distances.put(student, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        pq.offer(new StudentDistance(start, 0));
        
        UniversityStudent targetStudent = null;
        
        // Dijkstra's algorithm main loop
        while (!pq.isEmpty()) {
            StudentDistance current = pq.poll();
            UniversityStudent currentStudent = current.student;
            
            // Skip if already visited
            if (visited.contains(currentStudent)) {
                continue;
            }
            visited.add(currentStudent);
            
            // Check if current student has the target internship
            if (currentStudent.previousInternships.contains(targetCompany)) {
                targetStudent = currentStudent;
                break;
            }
            
            // Explore neighbors
            List<StudentGraph.Edge> neighbors = graph.getNeighbors(currentStudent);
            for (StudentGraph.Edge edge : neighbors) {
                UniversityStudent neighbor = edge.neighbor;
                
                if (visited.contains(neighbor)) {
                    continue;
                }
                
                // Invert weight: stronger connections = shorter paths
                int invertedWeight = MAX_CONNECTION_STRENGTH - edge.weight;
                int newDistance = distances.get(currentStudent) + invertedWeight;
                
                // Update if we found a shorter path
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, currentStudent);
                    pq.offer(new StudentDistance(neighbor, newDistance));
                }
            }
        }
        
        // Reconstruct path if target was found
        if (targetStudent == null) {
            return new ArrayList<>(); // No path found
        }
        
        return reconstructPath(previous, start, targetStudent);
    }
    
    /**
     * Reconstructs the path from start to target using the previous map from Dijkstra's algorithm.
     * 
     * @param previous map of each student to their predecessor in the shortest path
     * @param start the starting student
     * @param target the target student
     * @return a list representing the path from start to target
     */
    private List<UniversityStudent> reconstructPath(Map<UniversityStudent, UniversityStudent> previous,
                                                    UniversityStudent start, 
                                                    UniversityStudent target) {
        LinkedList<UniversityStudent> path = new LinkedList<>();
        UniversityStudent current = target;
        
        // Build path backwards from target to start
        while (current != null) {
            path.addFirst(current);
            current = previous.get(current);
        }
        
        // Verify the path starts at the correct student
        if (!path.isEmpty() && path.getFirst().equals(start)) {
            return path;
        }
        
        return new ArrayList<>(); // Invalid path
    }
    
    /**
     * Helper class to represent a student and their distance in the priority queue.
     * Implements Comparable to allow priority queue ordering by distance.
     */
    private static class StudentDistance implements Comparable<StudentDistance> {
        /** The student */
        UniversityStudent student;
        
        /** The current shortest distance to this student */
        int distance;
        
        /**
         * Constructs a StudentDistance pair.
         * 
         * @param student the student
         * @param distance the distance to this student
         */
        StudentDistance(UniversityStudent student, int distance) {
            this.student = student;
            this.distance = distance;
        }
        
        /**
         * Compares this StudentDistance to another based on distance.
         * 
         * @param other the other StudentDistance to compare to
         * @return negative if this distance is less, positive if greater, 0 if equal
         */
        @Override
        public int compareTo(StudentDistance other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
}