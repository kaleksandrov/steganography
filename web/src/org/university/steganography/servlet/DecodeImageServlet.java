package org.university.steganography.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.steganography.algorithm.LSBAlgorithm;
import org.steganography.algorithm.SteganographyAlgorithm;
import org.steganography.exception.SteganographyException;
//import org.university.steganography.algorithm.Steganography;
//import org.university.steganography.exception.SteganographyException;
import org.university.steganography.servlet.response.DecodeServletResponse;
import org.university.steganography.servlet.response.Status;
import org.university.steganography.util.Constants;
import org.university.steganography.util.Log;
import org.university.steganography.util.Utils;

import com.google.gson.Gson;

/**
 * The instances of this class are server(context) managed. Only HTTP POST
 * requests are allowed The servlet decodes the image sent through the HTTP
 * request. Returns the decoded message in the response object along with the
 * status object containing server messages. The response is serialized az JSON
 * (JavaScript Object Nation) object
 * 
 * @author Kiril Aleksandrov
 * 
 */
@WebServlet(urlPatterns = { Constants.SERVLET_DECODE_IMAGE })
@MultipartConfig(location = Constants.CONF_TEMP_DIRECTORY, maxFileSize = Constants.CONF_MAX_FILE_SIZE)
public class DecodeImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		// Initialize variables and ger HTTP request parameters

		Status status = new Status();

		String sourceType = request
				.getParameter(Constants.PARAM_DECODED_SOURCE);
		String password = request.getParameter(Constants.PARAM_DECODE_PASSWORD);
		BufferedImage image = null;

		try {
			if (Constants.SOURCE_TYPE_LOCAL.equalsIgnoreCase(sourceType)) {
				// Read the image from the request object
				Part filePart = request
						.getPart(Constants.PARAM_FILE_DECODE_LOCAL);

				InputStream filecontent = filePart.getInputStream();
				image = Utils.streamToImage(filecontent);
				filecontent.close();
			} else if (Constants.SOURCE_TYPE_REMOTE
					.equalsIgnoreCase(sourceType)) {
				// Read the request from remote resource
				String remoteAddress = request
						.getParameter(Constants.PARAM_FILE_DECODE_REMOTE);
				image = Utils.loadRemoteFile(remoteAddress);
			}
		} catch (IOException | ServletException e) {
			status.addMessage(Constants.MESSAGE_TYPE_ERROR,
					"Error uploading image");
		}

		final int imageLength = image.getHeight() * image.getWidth();
		final int startingOffset = Utils.calculateStartingOffset(password,
				imageLength);
		Log.info("Starting byte index : " + startingOffset);

		// Decode the image
		// Steganography steganography = new Steganography();
		SteganographyAlgorithm steganography = new LSBAlgorithm();
		String message = null;
		try {
			message = steganography.decode(image, startingOffset);
		} catch (SteganographyException e1) {
			status.addMessage(Constants.MESSAGE_TYPE_ERROR,
					"Error decoding image");
		}

		request.setAttribute(Constants.ATTR_MESSAGE, message);

		DecodeServletResponse responseWrapper = new DecodeServletResponse();
		responseWrapper.setStatus(status);

		responseWrapper.setMessage(message);

		// Writes the response object to the HTTP response stream that is
		// returned to the client
		PrintWriter out = null;
		try {
			out = new PrintWriter(response.getOutputStream());
		} catch (IOException e) {
			status.addMessage(Constants.MESSAGE_TYPE_ERROR,
					"Error writing response");
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