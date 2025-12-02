import java.io.*;
import java.util.*;

/**
 * Utility class for parsing student data from external files.
 * This class handles reading and validating student information from formatted text files
 * and creates {@link UniversityStudent} objects from the parsed data.
 * 
 * <p>Expected input file format (see input_sample.txt for examples):
 * <pre>
 * Name: [student name]
 * Age: [age]
 * Gender: [gender]
 * Year: [year]
 * Major: [major]
 * GPA: [gpa]
 * Roommate Preferences: [name1, name2, ...]
 * Previous Internships: [company1, company2, ...]
 * ---
 * </pre>
 * 
 * <p>This class handles various edge cases including:
 * <ul>
 *   <li>Missing or incomplete data fields</li>
 *   <li>Invalid formatting (missing colons, incorrect separators)</li>
 *   <li>Empty preference lists or internship lists</li>
 *   <li>"None" values for internships</li>
 * </ul>
 * 
 * @author Vidmahi Sistla
 * @version 2.0
 */
public class DataParser {
    
    /**
     * Parses student data from a file and returns a list of UniversityStudent objects.
     * Reads the specified file line by line, extracting student attributes and
     * creating UniversityStudent instances.
     * 
     * <p>The method validates each field and handles missing or malformed data gracefully.
     * Students with incomplete data may be skipped or created with default values
     * depending on which fields are missing.</p>
     * 
     * <p>File format requirements:
     * <ul>
     *   <li>Each student record is separated by "---"</li>
     *   <li>Each attribute line follows the format "FieldName: value"</li>
     *   <li>List values (preferences, internships) are comma-separated</li>
     *   <li>Whitespace is trimmed from all values</li>
     * </ul>
     * 
     * @param filename the path to the input file containing student data
     * @return a list of UniversityStudent objects parsed from the file
     * @throws IOException if the file cannot be read or does not exist
     * @throws IllegalArgumentException if the file format is invalid or contains unparseable data
     */
    public static List<UniversityStudent> parseStudents(String filename) throws IOException {
        List<UniversityStudent> students = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        
        String line;
        String name = null;
        int age = 0;
        String gender = null;
        int year = 0;
        String major = null;
        double gpa = 0.0;
        List<String> roommatePreferences = new ArrayList<>();
        List<String> previousInternships = new ArrayList<>();
        
        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }
                
                // Check for student separator
                if (line.equals("---")) {
                    // Create student object if all required fields are present
                    if (name != null && major != null) {
                        UniversityStudent student = new UniversityStudent(
                            name, age, gender, year, major, gpa,
                            new ArrayList<>(roommatePreferences),
                            new ArrayList<>(previousInternships)
                        );
                        students.add(student);
                    }
                    
                    // Reset for next student
                    name = null;
                    age = 0;
                    gender = null;
                    year = 0;
                    major = null;
                    gpa = 0.0;
                    roommatePreferences = new ArrayList<>();
                    previousInternships = new ArrayList<>();
                    continue;
                }
                
                // Parse field:value pairs
                if (!line.contains(":")) {
                    continue;
                }
                
                String[] parts = line.split(":", 2);
                if (parts.length < 2) {
                    continue;
                }
                
                String fieldName = parts[0].trim();
                String value = parts[1].trim();
                
                // Parse each field
                switch (fieldName) {
                    case "Name":
                        name = value;
                        break;
                    case "Age":
                        try {
                            age = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid age value: " + value);
                        }
                        break;
                    case "Gender":
                        gender = value;
                        break;
                    case "Year":
                        try {
                            year = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid year value: " + value);
                        }
                        break;
                    case "Major":
                        major = value;
                        break;
                    case "GPA":
                        try {
                            gpa = Double.parseDouble(value);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid GPA value: " + value);
                        }
                        break;
                    case "Roommate Preferences":
                        roommatePreferences = parseList(value);
                        break;
                    case "Previous Internships":
                        previousInternships = parseList(value);
                        break;
                }
            }
            
            // Add the last student if file doesn't end with "---"
            if (name != null && major != null) {
                UniversityStudent student = new UniversityStudent(
                    name, age, gender, year, major, gpa,
                    roommatePreferences, previousInternships
                );
                students.add(student);
            }
            
        } finally {
            reader.close();
        }
        
        return students;
    }
    
    /**
     * Helper method to parse comma-separated lists from the input file.
     * Trims whitespace from each item and filters out empty strings.
     * Handles "None" values by returning an empty list.
     * 
     * @param listString the comma-separated string to parse
     * @return a list of trimmed, non-empty strings
     */
    private static List<String> parseList(String listString) {
        if (listString == null || listString.trim().isEmpty() || 
            listString.trim().equalsIgnoreCase("None")) {
            return new ArrayList<>();
        }
        
        String[] items = listString.split(",");
        List<String> result = new ArrayList<>();
        
        for (String item : items) {
            String trimmed = item.trim();
            if (!trimmed.isEmpty() && !trimmed.equalsIgnoreCase("None")) {
                result.add(trimmed);
            }
        }
        
        return result;
    }
}