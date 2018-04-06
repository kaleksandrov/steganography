package org.university.steganography.servlet;

import org.university.steganography.enumeration.ImageType;
import org.university.steganography.util.Constants;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The instances of this class are server(context) managed. Only HTTP GET
 * requests are allowed The servlet returns an image from user's session. The
 * image can be the original image that the user has uploded or the encoded
 * image thath has been encoded with the message. This is controlled by the HTTP
 * request parameter <b>version</b>.
 *
 * @author Kiril Aleksandrov
 */
@WebServlet(urlPatterns =
        {Constants.SERVLET_IMAGE})
public class ImageServlet extends HttpServlet {
    private static final long serialVersionUID = -7780986917524240245L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response content type and file properties
        response.setContentType("image/png");
        response.setHeader("Content-Disposition", "filename=image");

        String imageVersion = request.getParameter(Constants.PARAM_IMAGE_VERSION);

        HttpSession session = request.getSession();

        BufferedImage image = null;

        // Gets the image from the session according to the request parameter
        // 'version'
        synchronized (session) {
            if (Constants.PARAM_IMAGE_VERSION_ORIGINAL.equals(imageVersion)) {
                image = (BufferedImage) session.getAttribute(Constants.ATTR_ORIGINAL_IMAGE);
            } else if (Constants.PARAM_IMAGE_VERSION_ENCODED.equals(imageVersion)) {
                image = (BufferedImage) session.getAttribute(Constants.ATTR_ENCODED_IMAGE);
            }
        }

        // Add the image to the HTTP resposne stream so it can be loaded by the
        // browser
        if (null != image) {
            ImageType imageType = ImageType.PNG;
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, imageType.toString(), out);
            out.close();
            out.flush();
        }
    }
}
