import java.util.*;

/**
 * Represents a weighted undirected graph of university students and their connections.
 * This graph structure is used for various algorithms, such as:
 * <ul>
 *   <li>Prim's algorithm for pod formation (forming groups of students)</li>
 *   <li>Dijkstra's algorithm for finding referral paths to internship opportunities</li>
 * </ul>
 * 
 * <p>The graph is implemented using an adjacency list where each student (node) maps
 * to a list of weighted edges representing their connections to other students.
 * Edge weights represent connection strength calculated based on shared attributes.</p>
 * 
 * <p>Students with a connection strength of 0 are not connected in the graph,
 * which may result in disconnected components.</p>
 * 
 * @author Vidmahi Sistla
 * @version 1.0
 */
public class StudentGraph {
    /**
     * Adjacency list representation of the graph.
     * Maps each student to their list of outgoing edges.
     */
    private Map<UniversityStudent, List<Edge>> adjacencyList;

    /**
     * Represents a weighted edge in the student graph.
     * An edge connects two students with a weight representing their connection strength.
     */
    public static class Edge {
        /** The neighboring student this edge connects to */
        public UniversityStudent neighbor;
        
        /** The weight of this edge (connection strength between students) */
        public int weight;

        /**
         * Constructs an edge with the specified neighbor and weight.
         * 
         * @param neighbor the student this edge connects to
         * @param weight the connection strength (edge weight)
         */
        public Edge(UniversityStudent neighbor, int weight) {
            this.neighbor = neighbor;
            this.weight = weight;
        }

        /**
         * Returns a string representation of this edge.
         * 
         * @return a formatted string showing the neighbor's name and edge weight
         */
        @Override
        public String toString() {
            return String.format("(%s, weight=%d)", neighbor.name, weight);
        }
    }

    /**
     * Constructs a StudentGraph from a list of university students.
     * Creates nodes for all students and adds weighted edges between students
     * based on their calculated connection strengths.
     * 
     * <p>Edges are only added if the connection strength is greater than 0.
     * The graph is undirected, so edges are added in both directions.</p>
     * 
     * @param students the list of UniversityStudent objects to include in the graph
     */
    public StudentGraph(List<UniversityStudent> students) {

    }

    /**
     * Adds a bidirectional weighted edge between two students.
     * This method ensures the graph remains undirected by adding the edge
     * in both directions.
     * 
     * @param student1 the first student
     * @param student2 the second student
     * @param weight the connection strength between the students
     */
    public void addEdge(UniversityStudent student1, UniversityStudent student2, int weight) {
        adjacencyList.get(student1).add(new Edge(student2, weight));
        adjacencyList.get(student2).add(new Edge(student1, weight));
    }

    /**
     * Gets all neighboring edges for a specific student.
     * 
     * @param student the student whose neighbors to retrieve
     * @return a list of Edge objects representing connections to other students,
     *         or an empty list if the student has no connections
     */
    public List<Edge> getNeighbors(UniversityStudent student) {
        return adjacencyList.getOrDefault(student, new ArrayList<>());
    }

    /**
     * Gets all students (nodes) in the graph.
     * 
     * @return a set of all UniversityStudent objects in the graph
     */
    public Set<UniversityStudent> getAllNodes() {
        return adjacencyList.keySet();
    }

    /**
     * Gets the number of nodes (students) in the graph.
     * 
     * @return the total number of students in the graph
     */
    public int size() {
        return adjacencyList.size();
    }

    /**
     * Checks if the graph contains a specific student.
     * 
     * @param student the student to check for
     * @return true if the student exists in the graph, false otherwise
     */
    public boolean containsStudent(UniversityStudent student) {
        return adjacencyList.containsKey(student);
    }
}