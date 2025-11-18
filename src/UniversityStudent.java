import java.util.*;

/**
 * Represents a university student in the Longhorn Network social platform.
 * This class extends {@link Student} and provides implementations
 * for connection strength calculation and manages social relationships including
 * roommates, friends, and chat history.
 * 
 * <p>Connection strength between students is calculated based on multiple factors:
 * <ul>
 *   <li> if students are roommates</li>
 *   <li> for each shared previous internship</li>
 *   <li> if students share the same major</li>
 *   <li> if students are the same age</li>
 * </ul>
 * 
 * <p>This class is thread-safe for concurrent friend requests and chat operations
 * through the use of {@link FriendRequestThread} and {@link ChatThread}.</p>
 * 
 * @author Vidmahi Sistla
 * @version 1.0
 */

public class UniversityStudent extends Student {
    // TODO: Constructor and additional methods to be implemented
}

