package org.university.steganography.util;

/**
 * Public interface containing all of the constants needed for the application
 *
 * @author Kiril Aleksandrov
 */
public interface Constants {
    // Common

    String EMPTY_STRING = "";

    // Servlet mappings

    String SERVLET_ENCODE_IMAGE = "/encode";

    String SERVLET_DECODE_IMAGE = "/decode";

    String SERVLET_IMAGE = "/image";

    // Configuration

    String CONF_TEMP_DIRECTORY = "/tmp/steganography";

    long CONF_MAX_FILE_SIZE = 20971520L; // 20 MB

    // Test

    String TEST_MESSAGE = "This is a test";

    // Parameters

    String PARAM_FILE_ENCODE_LOCAL = "file-encode-local";

    String PARAM_FILE_ENCODE_REMOTE = "file-encode-remote";

    String PARAM_FILE_DECODE_LOCAL = "file-decode-local";

    String PARAM_FILE_DECODE_REMOTE = "file-decode-remote";

    String PARAM_IMG_TYPE = "image-type";

    String PARAM_IMG_TYPE_PNG = "png";

    String PARAM_IMG_TYPE_JPG = "jpg";

    String PARAM_HIDDEN_MESSAGE = "hidden-message";

    String PARAM_IMAGE_VERSION = "version";

    String PARAM_IMAGE_VERSION_ORIGINAL = "original";

    String PARAM_IMAGE_VERSION_ENCODED = "encoded";

    String PARAM_ENCODED_SOURCE = "encode-source";

    String PARAM_DECODED_SOURCE = "decode-source";

    String PARAM_ENCODE_PASSWORD = "input-encode-password";

    String PARAM_DECODE_PASSWORD = "input-decode-password";

    // Attributes

    String ATTR_ORIGINAL_IMAGE = "originalImage";

    String ATTR_ENCODED_IMAGE = "encodedImage";

    String ATTR_IMAGE_TYPE = "encodedImageType";

    String ATTR_MESSAGE = "decodedMessage";

    String ATTR_ENCODE_MESSAGE = "encodeMessage";

    String ATTR_ENCODE_FILE_TYPE = "encodeFileType";

    // Source types

    String SOURCE_TYPE_LOCAL = "local";

    String SOURCE_TYPE_REMOTE = "remote";

    // Message types

    String MESSAGE_TYPE_ERROR = "error";

    String MESSAGE_TYPE_INFO = "info";

    String MESSAGE_TYPE_WARNING = "warning";

    // Message byte length

    int HIDDEN_MESSAGE_BIT_LENGTH = 32;
}
