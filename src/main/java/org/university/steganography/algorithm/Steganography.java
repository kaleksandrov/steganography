package org.university.steganography.algorithm;

import org.university.steganography.exception.SteganographyException;
import org.university.steganography.util.Constants;
import org.university.steganography.util.Log;
import org.university.steganography.util.MessageConstants;

import java.awt.image.BufferedImage;

/**
 * Class used to encode and decode messages in image objects. <b>LSB (Least
 * Significant Bit)</b> algorithm is used.
 *
 * @author Kiril Aleksandrov
 */
public class Steganography {

    /**
     * Encodes message into the given image
     *
     * @param bufferedImage The image that will carry the encoded message
     * @param message       The message to encoded
     * @param offset        The index of the starting byte
     * @return New image encoded with the message
     * @throws SteganographyException
     */
    public BufferedImage encode(final BufferedImage bufferedImage, final String message, final int offset)
            throws SteganographyException {
        final BufferedImage image = this.addText(bufferedImage, message, offset);
        return image;
    }

    /**
     * Decodes and gets a hidden message (if it exists) from the given image.
     * The used algorithm is <b>LSB (Least Significant Bit)</b>
     *
     * @param bufferedImage  The image to be docoded
     * @param startingOffset The index of the starting byte
     * @return The message extracted from the image
     * @throws SteganographyException
     */
    public String decode(final BufferedImage bufferedImage, final int startingOffset) throws SteganographyException {
        byte[] decode = null;
        try {
            decode = this.decodeText(bufferedImage, startingOffset);
            final String decodedMessage = new String(decode);
            return decodedMessage;
        } catch (Exception e) {
            throw new SteganographyException(MessageConstants.DECODING_ERROR_GENERAL);
        }
    }

    /**
     * Encodes text into the given image
     *
     * @param bufferedImage The image that will carry the encoded message
     * @param text          The text to encoded
     * @param offset        The index of the starting byte
     * @return New image encoded with the text
     * @throws SteganographyException
     */
    private BufferedImage addText(BufferedImage image, final String text, final int offset)
            throws SteganographyException {
        // Convert all items to byte arrays: image, message, message length
        final byte msg[] = text.getBytes();
        final byte len[] = bitConversion(msg.length);
        try {
            // Encode the message length
            image = this.encodeText(image, len, offset);
            // Encode the message
            image = this.encodeText(image, msg, offset + Constants.HIDDEN_MESSAGE_BIT_LENGTH);
        } catch (Exception e) {
            throw new SteganographyException(MessageConstants.ENCODING_ERROR_GENERAL, e);
        }

        return image;
    }

    /**
     * Converts the given number to array of bytes
     *
     * @param i The number to be converted
     * @return The byte array representing the number
     */
    private byte[] bitConversion(final int i) {
        final byte byte3 = (byte) ((i & 0xFF000000) >>> 24);
        final byte byte2 = (byte) ((i & 0x00FF0000) >>> 16);
        final byte byte1 = (byte) ((i & 0x0000FF00) >>> 8);
        final byte byte0 = (byte) ((i & 0x000000FF));
        return (new byte[]
                {byte3, byte2, byte1, byte0});
    }

    /**
     * Encodes text into the given image
     *
     * @param bufferedImage The image that will carry the encoded message
     * @param addition      The text to encoded
     * @param offset        The index of the starting byte
     * @return New image encoded with the text
     * @throws SteganographyException
     */
    private BufferedImage encodeText(final BufferedImage image, final byte[] addition, final int offset)
            throws SteganographyException {
        // Gets the image dimensions
        final int height = image.getHeight();
        final int width = image.getWidth();

        // Initialize variables for iteration
        int i = offset / height;
        int j = offset % height;

        Log.info("Width : " + width);
        Log.info("Height : " + height);

        if ((width * height) >= (addition.length * 8 + offset)) {
            // Iterates over all message's bytes
            for (final byte add : addition) {
                // Iterates over all of the bits in the current byte
                for (int bit = 7; bit >= 0; --bit) {
                    Log.info("[ " + i + " , " + j + " ]");
                    // Gets the original image byte value
                    final int imageValue = image.getRGB(i, j);

                    // Calculates the new image byte value
                    int b = (add >>> bit) & 1;
                    final int imageNewValue = ((imageValue & 0xFFFFFFFE) | b);

                    // Sets the new image byte value
                    image.setRGB(i, j, imageNewValue);

                    if (j < (height - 1)) {
                        ++j;
                    } else {
                        ++i;
                        j = 0;
                    }
                }

            }
        } else {
            throw new SteganographyException(MessageConstants.ENCODING_ERROR_BIG_MESSAGE);
        }

        return image;
    }

    /**
     * Decodes and gets a hidden message (if it exists) from the given image.
     * The used algorithm is <b>LSB (Least Significant Bit)</b>
     *
     * @param image          The image to be decoded
     * @param startingOffset The index of the starting byte
     * @return The message extracted from the image
     */
    private byte[] decodeText(final BufferedImage image, final int startingOffset) {
        // Initialize starting variables
        final int height = image.getHeight();
        final int offset = startingOffset + Constants.HIDDEN_MESSAGE_BIT_LENGTH;
        int length = 0;

        // Loop through 32 bytes of data to determine text length
        for (int i = startingOffset; i < offset; ++i) {
            final int h = i / height;
            final int w = i % height;

            final int imageValue = image.getRGB(h, w);
            length = (length << 1) | (imageValue & 1);
        }

        Log.info("LENGTH : " + length);

        byte[] result = new byte[length];

        // Initialize variables for iteration
        int i = offset / height;
        int j = offset % height;

        // Iterate from zero to message length
        for (int letter = 0; letter < length; ++letter) {
            // Iterates over each bit for the hidden message
            for (int bit = 7; bit >= 0; --bit) {
                // Gets the byte from the image
                final int imageValue = image.getRGB(i, j);

                // Calculates the bit for the message
                result[letter] = (byte) ((result[letter] << 1) | (imageValue & 1));

                if (j < (height - 1)) {
                    ++j;
                } else {
                    ++i;
                    j = 0;
                }
            }
        }

        return result;
    }

}
