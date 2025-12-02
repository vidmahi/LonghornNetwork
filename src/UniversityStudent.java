import java.util.*;

/**
 * Represents a university student in the Longhorn Network social platform.
 * This class extends {@link Student} and provides concrete implementations
 * for connection strength calculation and manages social relationships including
 * roommates, friends, and chat history.
 * 
 * <p>Connection strength between students is calculated based on multiple factors:
 * <ul>
 *   <li>+4 points if students are roommates</li>
 *   <li>+3 points for each shared previous internship</li>
 *   <li>+2 points if students share the same major</li>
 *   <li>+1 point if students are the same age</li>
 * </ul>
 * 
 * <p>This class is thread-safe for concurrent friend requests and chat operations
 * through the use of {@link FriendRequestThread} and {@link ChatThread}.</p>
 * 
 * @author Vidmahi Sistla
 * @version 2.0
 */
public class UniversityStudent extends Student {
    /** The student's assigned roommate (null if no roommate assigned) */
    private UniversityStudent roommate;
    
    /** List of students who are friends with this student */
    private List<UniversityStudent> friends;
    
    /** Map storing chat history between this student and others (key: other student, value: list of messages) */
    private Map<UniversityStudent, List<String>> chatHistory;

    /**
     * Constructs a new UniversityStudent with the specified attributes.
     * Initializes empty collections for friends and chat history.
     * 
     * @param name the student's full name
     * @param age the student's age in years
     * @param gender the student's gender identity
     * @param year the student's current year in university (1-4)
     * @param major the student's major field of study
     * @param gpa the student's cumulative GPA on a 4.0 scale
     * @param roommatePreferences list of preferred roommate names in order of preference
     * @param previousInternships list of companies where the student has previously interned
     */
    public UniversityStudent(String name, int age, String gender, int year, 
                           String major, double gpa, 
                           List<String> roommatePreferences, 
                           List<String> previousInternships) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.year = year;
        this.major = major;
        this.gpa = gpa;
        this.roommatePreferences = roommatePreferences != null ? roommatePreferences : new ArrayList<>();
        this.previousInternships = previousInternships != null ? previousInternships : new ArrayList<>();
        this.friends = new ArrayList<>();
        this.chatHistory = new HashMap<>();
        this.roommate = null;
    }

    /**
     * Calculates the connection strength between this student and another student.
     * The calculation uses the following weighted scoring system:
     * <ul>
     *   <li>Roommate: +4 points</li>
     *   <li>Each shared internship: +3 points</li>
     *   <li>Same major: +2 points</li>
     *   <li>Same age: +1 point</li>
     * </ul>
     * 
     * @param other the other student to calculate connection strength with
     * @return the total connection strength as an integer (0 if no connections)
     */
    @Override
    public int calculateConnectionStrength(Student other) {
        if (!(other instanceof UniversityStudent)) {
            return 0;
        }
        
        UniversityStudent otherStudent = (UniversityStudent) other;
        int strength = 0;
        
        // Check if roommates (+4)
        if (this.roommate != null && this.roommate.equals(otherStudent)) {
            strength += 4;
        }
        
        // Count shared internships (+3 each)
        for (String internship : this.previousInternships) {
            if (otherStudent.previousInternships.contains(internship) && 
                !internship.equalsIgnoreCase("None")) {
                strength += 3;
            }
        }
        
        // Check same major (+2)
        if (this.major.equals(otherStudent.major)) {
            strength += 2;
        }
        
        // Check same age (+1)
        if (this.age == otherStudent.age) {
            strength += 1;
        }
        
        return strength;
    }

    /**
     * Gets the student's assigned roommate.
     * 
     * @return the roommate UniversityStudent object, or null if no roommate is assigned
     */
    public UniversityStudent getRoommate() {
        return roommate;
    }

    /**
     * Sets the student's roommate.
     * 
     * @param roommate the UniversityStudent to set as roommate
     */
    public void setRoommate(UniversityStudent roommate) {
        this.roommate = roommate;
    }

    /**
     * Gets the list of friends for this student.
     * 
     * @return an unmodifiable list of UniversityStudent friends
     */
    public List<UniversityStudent> getFriends() {
        return new ArrayList<>(friends);
    }

    /**
     * Adds a friend to this student's friend list in a thread-safe manner.
     * This method is synchronized to prevent concurrent modification issues.
     * 
     * @param friend the UniversityStudent to add as a friend
     */
    public synchronized void addFriend(UniversityStudent friend) {
        if (!friends.contains(friend)) {
            friends.add(friend);
        }
    }

    /**
     * Gets the chat history between this student and another student.
     * 
     * @param other the other UniversityStudent
     * @return a list of chat messages, or an empty list if no chat history exists
     */
    public List<String> getChatHistory(UniversityStudent other) {
        return chatHistory.getOrDefault(other, new ArrayList<>());
    }

    /**
     * Adds a message to the chat history with another student in a thread-safe manner.
     * This method is synchronized to prevent concurrent modification issues.
     * 
     * @param other the other UniversityStudent involved in the chat
     * @param message the message content to add to the chat history
     */
    public synchronized void addChatMessage(UniversityStudent other, String message) {
        chatHistory.computeIfAbsent(other, k -> new ArrayList<>()).add(message);
    }

    /**
     * Returns a string representation of this student including all attributes.
     * 
     * @return a formatted string containing student information
     */
    @Override
    public String toString() {
        return String.format("UniversityStudent{name='%s', age=%d, gender='%s', year=%d, major='%s', " +
                           "gpa=%.2f, roommatePreferences=%s, previousInternships=%s, roommate=%s}",
                           name, age, gender, year, major, gpa, roommatePreferences, 
                           previousInternships, (roommate != null ? roommate.name : "None"));
    }

    /**
     * Checks if this student equals another object.
     * Two students are equal if they have the same name.
     * 
     * @param obj the object to compare with
     * @return true if the students have the same name, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UniversityStudent)) return false;
        UniversityStudent other = (UniversityStudent) obj;
        return this.name.equals(other.name);
    }

    /**
     * Returns a hash code value for this student based on the name.
     * 
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}