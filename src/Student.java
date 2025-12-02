import java.util.*;

/**
 * Abstract base class representing a student in the Longhorn Network.
 * This class defines the common attributes and behaviors shared by all students,
 * including personal information, academic details, and social preferences.
 * 
 * <p>Subclasses must implement the {@code calculateConnectionStrength} method
 * to define how connection strength between students is computed based on
 * shared attributes such as major, internships, and roommate status.</p>
 * 
 * @author Vidmahi Sistla
 * @version 2.0
 */
public abstract class Student {
    /** The student's full name */
    protected String name;
    
    /** The student's age in years */
    protected int age;
    
    /** The student's gender identity */
    protected String gender;
    
    /** The student's current year in university (1-4) */
    protected int year;
    
    /** The student's major field of study */
    protected String major;
    
    /** The student's cumulative GPA on a 4.0 scale */
    protected double gpa;
    
    /** List of preferred roommate names in order of preference */
    protected List<String> roommatePreferences;
    
    /** List of companies where the student has previously interned */
    protected List<String> previousInternships;

    /**
     * Calculates the connection strength between this student and another student.
     * Connection strength is determined by shared attributes such as:
     * <ul>
     *   <li>Roommate status (if applicable)</li>
     *   <li>Shared previous internships</li>
     *   <li>Same major</li>
     *   <li>Same age</li>
     * </ul>
     * 
     * @param other the other student to calculate connection strength with
     * @return an integer representing the connection strength (higher values indicate stronger connections)
     */
    public abstract int calculateConnectionStrength(Student other);
}