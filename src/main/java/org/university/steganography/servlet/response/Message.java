package org.university.steganography.servlet.response;

/**
 * Message that is returned from the server to the client. Possible types are
 * <b>INFO</b>, <b>WARN</b> and <b>ERROR</b>.
 *
 * @author Kiril Aleksandrov
 */
public class Message {
    /**
     * The type of the message
     */
    private String type;

    /**
     * The text of the message
     */
    private String text;

    public Message() {
    }

    public Message(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
