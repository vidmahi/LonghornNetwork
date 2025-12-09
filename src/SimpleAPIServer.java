// import com.sun.net.httpserver.*;
// import java.io.*;
// import java.net.InetSocketAddress;
// import java.util.*;

// /**
//  * Simple HTTP server to expose Longhorn Network data as JSON API.
//  * Serves test case data and algorithm results to React frontend.
//  * 
//  * @author Vidmahi Sistla
//  * @version 1.0
//  */
// public class SimpleAPIServer {
    
//     private static List<UniversityStudent> currentStudents;
//     private static StudentGraph currentGraph;
    
//     public static void main(String[] args) throws IOException {
//         // Create HTTP server on port 8080
//         HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
//         // Enable CORS for React
//         server.createContext("/api/testcase", new TestCaseHandler());
//         server.createContext("/api/roommates", new RoommateHandler());
//         server.createContext("/api/referral", new ReferralHandler());
        
//         server.setExecutor(null);
//         server.start();
        
//         System.out.println("🚀 API Server started on http://localhost:8080");
//         System.out.println("📡 React app should connect to this server");
//         System.out.println("Press Ctrl+C to stop");
//     }
    
//     /**
//      * Handles test case requests.
//      */
//     static class TestCaseHandler implements HttpHandler {
//         @Override
//         public void handle(HttpExchange exchange) throws IOException {
//             // Enable CORS
//             exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
//             exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
//             exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            
//             if ("OPTIONS".equals(exchange.getRequestMethod())) {
//                 exchange.sendResponseHeaders(204, -1);
//                 return;
//             }
            
//             // Get test case number from query
//             String query = exchange.getRequestURI().getQuery();
//             int testCase = 1;
//             if (query != null && query.startsWith("case=")) {
//                 testCase = Integer.parseInt(query.substring(5));
//             }
            
//             // Load test case
//             switch (testCase) {
//                 case 1: currentStudents = Main.generateTestCase1(); break;
//                 case 2: currentStudents = Main.generateTestCase2(); break;
//                 case 3: currentStudents = Main.generateTestCase3(); break;
//             }
            
//             currentGraph = new StudentGraph(currentStudents);
            
//             // Build JSON response
//             String json = studentsToJson(currentStudents, currentGraph);
            
//             exchange.getResponseHeaders().set("Content-Type", "application/json");
//             exchange.sendResponseHeaders(200, json.length());
//             OutputStream os = exchange.getResponseBody();
//             os.write(json.getBytes());
//             os.close();
//         }
//     }
    
//     /**
//      * Handles roommate assignment requests.
//      */
//     static class RoommateHandler implements HttpHandler {
//         @Override
//         public void handle(HttpExchange exchange) throws IOException {
//             // Enable CORS
//             exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
//             exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            
//             if ("OPTIONS".equals(exchange.getRequestMethod())) {
//                 exchange.sendResponseHeaders(204, -1);
//                 return;
//             }
            
//             if (currentStudents == null) {
//                 String error = "{\"error\": \"No test case loaded\"}";
//                 exchange.sendResponseHeaders(400, error.length());
//                 exchange.getResponseBody().write(error.getBytes());
//                 exchange.getResponseBody().close();
//                 return;
//             }
            
//             // Run Gale-Shapley
//             GaleShapley.assignRoommates(currentStudents);
            
//             String json = studentsToJson(currentStudents, currentGraph);
            
//             exchange.getResponseHeaders().set("Content-Type", "application/json");
//             exchange.sendResponseHeaders(200, json.length());
//             OutputStream os = exchange.getResponseBody();
//             os.write(json.getBytes());
//             os.close();
//         }
//     }
    
//     /**
//      * Handles referral path requests.
//      */
//     static class ReferralHandler implements HttpHandler {
//         @Override
//         public void handle(HttpExchange exchange) throws IOException {
//             // Enable CORS
//             exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
//             exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            
//             if ("OPTIONS".equals(exchange.getRequestMethod())) {
//                 exchange.sendResponseHeaders(204, -1);
//                 return;
//             }
            
//             if (currentStudents == null || currentGraph == null) {
//                 String error = "{\"error\": \"No test case loaded\"}";
//                 exchange.sendResponseHeaders(400, error.length());
//                 exchange.getResponseBody().write(error.getBytes());
//                 exchange.getResponseBody().close();
//                 return;
//             }
            
//             // Get company from query
//             String query = exchange.getRequestURI().getQuery();
//             String company = "DummyCompany";
//             if (query != null && query.startsWith("company=")) {
//                 company = java.net.URLDecoder.decode(query.substring(8), "UTF-8");
//             }
            
//             // Find referral path
//             ReferralPathFinder finder = new ReferralPathFinder(currentGraph);
//             List<UniversityStudent> path = finder.findReferralPath(currentStudents.get(0), company);
            
//             // Build JSON
//             StringBuilder json = new StringBuilder("{\"path\": [");
//             for (int i = 0; i < path.size(); i++) {
//                 json.append("\"").append(path.get(i).name).append("\"");
//                 if (i < path.size() - 1) json.append(",");
//             }
//             json.append("]}");
            
//             String response = json.toString();
//             exchange.getResponseHeaders().set("Content-Type", "application/json");
//             exchange.sendResponseHeaders(200, response.length());
//             OutputStream os = exchange.getResponseBody();
//             os.write(response.getBytes());
//             os.close();
//         }
//     }
    
//     /**
//      * Converts students and graph to JSON.
//      */
//     private static String studentsToJson(List<UniversityStudent> students, StudentGraph graph) {
//         StringBuilder json = new StringBuilder();
//         json.append("{\"students\": [");
        
//         for (int i = 0; i < students.size(); i++) {
//             UniversityStudent s = students.get(i);
//             json.append("{");
//             json.append("\"name\":\"").append(s.name).append("\",");
//             json.append("\"age\":").append(s.age).append(",");
//             json.append("\"gender\":\"").append(s.gender).append("\",");
//             json.append("\"year\":").append(s.year).append(",");
//             json.append("\"major\":\"").append(s.major).append("\",");
//             json.append("\"gpa\":").append(s.gpa).append(",");
            
//             // Roommate
//             json.append("\"roommate\":");
//             if (s.getRoommate() != null) {
//                 json.append("\"").append(s.getRoommate().name).append("\"");
//             } else {
//                 json.append("null");
//             }
//             json.append(",");
            
//             // Preferences
//             json.append("\"roommatePreferences\":[");
//             for (int j = 0; j < s.roommatePreferences.size(); j++) {
//                 json.append("\"").append(s.roommatePreferences.get(j)).append("\"");
//                 if (j < s.roommatePreferences.size() - 1) json.append(",");
//             }
//             json.append("],");
            
//             // Internships
//             json.append("\"previousInternships\":[");
//             for (int j = 0; j < s.previousInternships.size(); j++) {
//                 json.append("\"").append(s.previousInternships.get(j)).append("\"");
//                 if (j < s.previousInternships.size() - 1) json.append(",");
//             }
//             json.append("],");
            
//             // Friends
//             json.append("\"friends\":[");
//             List<UniversityStudent> friends = s.getFriends();
//             for (int j = 0; j < friends.size(); j++) {
//                 json.append("\"").append(friends.get(j).name).append("\"");
//                 if (j < friends.size() - 1) json.append(",");
//             }
//             json.append("]");
            
//             json.append("}");
//             if (i < students.size() - 1) json.append(",");
//         }
        
//         json.append("], \"graph\": {");
        
//         // Add graph edges
//         Set<UniversityStudent> nodes = graph.getAllNodes();
//         json.append("\"edges\": [");
//         Set<String> addedEdges = new HashSet<>();
//         boolean firstEdge = true;
        
//         for (UniversityStudent s : nodes) {
//             List<StudentGraph.Edge> edges = graph.getNeighbors(s);
//             for (StudentGraph.Edge edge : edges) {
//                 String edgeKey = s.name.compareTo(edge.neighbor.name) < 0 ? 
//                     s.name + "-" + edge.neighbor.name : edge.neighbor.name + "-" + s.name;
                
//                 if (!addedEdges.contains(edgeKey)) {
//                     if (!firstEdge) json.append(",");
//                     json.append("{");
//                     json.append("\"from\":\"").append(s.name).append("\",");
//                     json.append("\"to\":\"").append(edge.neighbor.name).append("\",");
//                     json.append("\"weight\":").append(edge.weight);
//                     json.append("}");
//                     addedEdges.add(edgeKey);
//                     firstEdge = false;
//                 }
//             }
//         }
        
//         json.append("]}}");
//         return json.toString();
//     }
// }