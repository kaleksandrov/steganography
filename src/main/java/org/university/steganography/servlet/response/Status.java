package org.university.steganography.servlet.response;

import org.university.steganography.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * The status of the performed operation. If something wrong occured the field
 * <b>success</b> should be false.
 *
 * @author Kiril Aleksandrov
 */
public class Status {
    /**
     * Flag indicating if the performed operation was successfully
     */
    private boolean success;

    /**
     * List of messages that are sent from the server to the client
     */
    private List<Message> messages;

    public Status() {
        this.success = true;
        this.messages = new ArrayList<Message>();
    }

    public boolean isSuccess() {
        return success;
    }

    /**
     * Add message to the list of messages. If the message type equals to
     * <b>ERROR</b> the <b>success</b> flag should be turned to false
     *
     * @param type The type of the message
     * @param text The message content
     */
    public void addMessage(String type, String text) {
        if (Constants.MESSAGE_TYPE_ERROR.equalsIgnoreCase(type) && this.success) {
            this.success = false;
        }

        Message message = new Message(type, text);
        this.messages.add(message);
    }

    public List<Message> getMessages() {
        return this.messages;
    }
}
