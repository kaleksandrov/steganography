package org.university.steganography.servlet.response;

/**
 * Abstract class representing the HTTP response from any servlet. All servlet
 * responses should extends this class.
 *
 * @author Kiril Aleksandrov
 */
public abstract class ServletResponse {
    /**
     * The status of the performed operation
     */
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
