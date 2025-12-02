/**
 * Implements a thread for handling chat messages between university students.
 * This class simulates the concurrent action of sending messages in the
 * Longhorn Network social platform.
 * 
 * <p>Thread safety:
 * <ul>
 *   <li>Uses synchronized methods in UniversityStudent to prevent race conditions</li>
 *   <li>Ensures that chat history is updated atomically</li>
 *   <li>Can be run concurrently with other ChatThread and FriendRequestThread instances</li>
 * </ul>
 * 
 * <p>Behavior:
 * <ul>
 *   <li>Adds the message to the sender's chat history with the receiver</li>
 *   <li>Maintains separate chat histories for each pair of students</li>
 *   <li>Messages are stored in chronological order</li>
 *   <li>Logs the chat action for debugging and verification</li>
 * </ul>
 * 
 * @author Vidmahi Sistla
 * @version 2.0
 */
public class ChatThread implements Runnable {
    /** The student sending the message */
    private UniversityStudent sender;
    
    /** The student receiving the message */
    private UniversityStudent receiver;
    
    /** The message content to be sent */
    private String message;

    /**
     * Constructs a ChatThread to handle sending a message between two students.
     * 
     * @param sender the UniversityStudent sending the message
     * @param receiver the UniversityStudent receiving the message
     * @param message the message content to send
     * @throws IllegalArgumentException if sender, receiver, or message is null
     */
    public ChatThread(UniversityStudent sender, UniversityStudent receiver, String message) {
        if (sender == null || receiver == null) {
            throw new IllegalArgumentException("Sender and receiver cannot be null");
        }
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    /**
     * Executes the chat message operation in a separate thread.
     * Adds the message to the sender's chat history with the receiver.
     * 
     * <p>This method:
     * <ol>
     *   <li>Adds the message to the sender's chat history with the receiver</li>
     *   <li>Logs the chat action with sender, receiver, and message preview</li>
     *   <li>Handles any exceptions that occur during execution</li>
     * </ol>
     * 
     * <p>Thread safety is ensured through synchronized methods in UniversityStudent
     * that prevent concurrent modification of chat histories.
     * 
     * @see UniversityStudent#addChatMessage(UniversityStudent, String)
     */
    @Override
    public void run() {
        try {
            // Add message to sender's chat history with receiver
            sender.addChatMessage(receiver, message);
            
            // Log the chat action
            String preview = message.length() > 30 ? 
                           message.substring(0, 27) + "..." : message;
            System.out.println(sender.name + " sent message to " + 
                             receiver.name + ": \"" + preview + "\"");
            
        } catch (Exception e) {
            System.err.println("Error processing chat from " + 
                             sender.name + " to " + receiver.name + ": " + e.getMessage());
        }
    }
}