package org.university.steganography.servlet.response;

/**
 * This class is used to represent the response from <b>DecodeImageServlet</b>
 *
 * @author Kiril Aleksandrov
 */
public class DecodeServletResponse extends ServletResponse {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
