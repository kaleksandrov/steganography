package org.university.steganography.servlet;

import com.google.gson.Gson;
import org.university.steganography.algorithm.Steganography;
import org.university.steganography.exception.SteganographyException;
import org.university.steganography.servlet.response.EncodeServletResponse;
import org.university.steganography.servlet.response.Status;
import org.university.steganography.util.Constants;
import org.university.steganography.util.Log;
import org.university.steganography.util.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * The instances of this class are server(context) managed. Only HTTP POST
 * requests are allowed The servlet encodes the image with the message both sent
 * through the HTTP request. The response is serialized az JSON (JavaScript
 * Object Nation) object
 *
 * @author Kiril Aleksandrov
 */
@WebServlet(urlPatterns =
        {Constants.SERVLET_ENCODE_IMAGE})
@MultipartConfig(location = Constants.CONF_TEMP_DIRECTORY, maxFileSize = Constants.CONF_MAX_FILE_SIZE)
public class EncodeImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        // Initialize varaibles and gets HTTP request parameters
        Status status = new Status();

        String message = request.getParameter(Constants.PARAM_HIDDEN_MESSAGE);
        String password = request.getParameter(Constants.PARAM_ENCODE_PASSWORD);

        BufferedImage image = null;

        String sourceType = request.getParameter(Constants.PARAM_ENCODED_SOURCE);

        try {
            if ("local".equalsIgnoreCase(sourceType)) {
                // Loads the image from the HTTP request object
                Part filePart = request.getPart(Constants.PARAM_FILE_ENCODE_LOCAL);
                InputStream fileInputStream = filePart.getInputStream();
                image = Utils.streamToImage(fileInputStream);
                fileInputStream.close();
            } else if ("remote".equalsIgnoreCase(sourceType)) {
                // Loads the image from remote resource
                String remoteAddress = request.getParameter(Constants.PARAM_FILE_ENCODE_REMOTE);
                image = Utils.loadRemoteFile(remoteAddress);
            }
        } catch (IOException | ServletException e) {
            status.addMessage(Constants.MESSAGE_TYPE_ERROR, "Error uploading image");
        }

        // Encodes the message in the image
        Log.info("Image : " + (image != null));
        final int imageLength = image.getHeight() * image.getWidth();
        Log.info("Image length : " + imageLength);
        final int startingOffset = Utils.calculateStartingOffset(password, imageLength);
        Log.info("Statring byte index : " + startingOffset);

        Steganography steganography = new Steganography();
        BufferedImage encodedImage = null;
        try {
            encodedImage = steganography.encode(image, message, startingOffset);
        } catch (SteganographyException e) {
            status.addMessage(Constants.MESSAGE_TYPE_ERROR, "Error encoding image");
        }

        // Sets the original and the encoded image to the user session object.
        // Thus they are available for later use.
        HttpSession session = request.getSession();
        synchronized (session) {
            session.setAttribute(Constants.ATTR_ORIGINAL_IMAGE, image);
            session.setAttribute(Constants.ATTR_ENCODED_IMAGE, encodedImage);
        }

        EncodeServletResponse responseWrapper = new EncodeServletResponse();

        responseWrapper.setStatus(status);

        // Writes the response object to the HTTP response stream
        PrintWriter out = null;
        try {
            out = new PrintWriter(response.getOutputStream());
        } catch (IOException e) {
            status.addMessage(Constants.MESSAGE_TYPE_ERROR, "Error writing response");
        }

        if (null != out) {
            Gson gson = new Gson();
            Log.info(gson.toJson(responseWrapper));

            out.write(gson.toJson(responseWrapper));

            out.close();
            out.flush();
        }
    }
}