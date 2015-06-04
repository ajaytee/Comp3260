package au.com.uon.comp3260.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * This is a helper class for reading input files and converting arrays and
 * matrixes
 * 
 * @author Felix Behrendt & Andrew Thursby
 * 
 */
public class Helper {

    public static byte[] copyByteArray(byte[] toCopy) {
        byte[] output = new byte[toCopy.length];
        for (int i = 0; i < toCopy.length; i++) {
            output[i] = toCopy[i];
        }
        return output;
    }

    public static byte[][] copyByteMatrix(byte[][] toCopy) {
        byte[][] output = new byte[toCopy.length][toCopy[0].length];
        for (int i = 0; i < toCopy.length; i++) {
            for (int j = 0; j < toCopy[0].length; j++)
                output[i][j] = toCopy[i][j];
        }
        return output;
    }

    private static String readLine(Path inputFile, int lineNbr) {
        String line = null;
        try {
            List<String> lines = Files.readAllLines(inputFile);
            if (lines != null && lines.size() > lineNbr) {
                // Remove optional whitespaces that might be in the input file
                line = lines.get(lineNbr).replaceAll(" ", "");
                if (line.length() != 128) {
                    throw new Exception(
                            "The textfile must contain 128 0s and 1s!");
                }
            } else {
                throw new Exception("The file does not contain "
                        + (lineNbr + 1) + " lines");
            }
        } catch (Exception e) {
            line = null;
            System.err.println("Could not read text from file: "
                    + e.getMessage());
            e.printStackTrace();
        }
        return line;
    }

    /**
     * Reads a plain text from an input file. The cipher text must have a length
     * of 128 characters containing 0s and 1s and whitespaces.
     * 
     * @param inputFile
     *            path to a file
     * @return cipher text as a String
     */
    public static String readPlainText(Path inputFile) {
        return readLine(inputFile, 0);
    }

    /**
     * Reads a cipher text from an input file. The cipher text must have a
     * length of 128 characters containing 0s and 1s and whitespaces.
     * 
     * @param inputFile
     *            path to a file
     * @return cipher text as a String
     */
    public static String readCipherText(Path inputFile) {
        return readLine(inputFile, 0);
    }

    /**
     * Reads the key from a given file. The key must be in the second line in
     * the file.
     * 
     * @param inputFile
     *            path to a file
     * @return key as String
     */
    public static String readKey(Path inputFile) {
        return readLine(inputFile, 1);
    }

    /**
     * Parses a string to a byte array
     * 
     * @param binaryString
     *            containing 0s and 1s and it must have a length of 128
     *            characters
     * @return byte array containing the data parsed from the string
     */
    public static byte[] stringToByteArray(String binaryString) {
        byte[] output = new byte[16];
        for (int i = 0; i < 16; i++) {
            String part = binaryString.substring(i * 8, (i + 1) * 8);
            output[i] = (byte) Integer.parseInt(part, 2);
        }
        return output;
    }

    /**
     * Converts a byte array to a 4x4 matrix
     * 
     * @param array
     *            to be converted
     * @return byte matrix with data of the array
     */
    public static byte[][] arrayToMatrix(byte[] array) {
        byte[][] hexMatrix = new byte[4][4];
        int k = 0;
        for (int i = 0; i < 4; i++) { // i = row #
            for (int j = 0; j < 4; j++) { // j = column #
                hexMatrix[j][i] = array[k];
                k++;
            }
        }
        return hexMatrix;
    }

    /**
     * Converts a byte matrix to a byte array
     * 
     * @param matrix
     *            to be converted
     * @return byte array with the data of the matix
     */
    public static byte[] matrixToArray(byte[][] matrix) {
        byte[] array = new byte[16];
        int k = 0;
        for (int i = 0; i < 4; i++) { // i = row #
            for (int j = 0; j < 4; j++) { // j = column #
                array[k] = matrix[j][i];
                k++;
            }
        }
        return array;
    }

    /**
     * Formats a byte matrix to a string. Each row in the matrix will be
     * presented by a single line in the string.
     * 
     * @param matrix
     *            to be converted
     * @param pretty
     *            if true, a whitespace will be added after each 8 bits.
     * @return String representing the matix
     */
    public static String matrixToString(byte[][] matrix, boolean pretty) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) { // i = row #
            for (int j = 0; j < 4; j++) { // j = column #
                Byte b = matrix[j][i];
                sb.append(byteToString(b));
                if (pretty)
                    sb.append(" ");
            }
            if (pretty)
                sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Formats a byte array to a string. each byte will be presented by 8
     * characters. 2 Arrays of the same length will always have the same output
     * length
     * 
     * @param array
     *            input array
     * @param pretty
     *            if true, a whitespace will be inserted after each byte
     * @return String
     */
    public static String arrayToString(byte[] array, boolean pretty) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < 16; j++) {
            Byte b = array[j];
            sb.append(byteToString(b));
            if (pretty)
                sb.append(" ");
        }
        return sb.toString();
    }

    public static String matrixToHexString(byte[][] matrix) {
        byte[] array = matrixToArray(matrix);
        StringBuilder sb = new StringBuilder();
        for (byte b : array)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString().toUpperCase();
    }

    public static byte[][] hexStringToMatrix(String input) {
        byte[] output = new byte[16];
        for (int i = 0; i < 16; i++) {
            String part = input.substring(i * 2, (i + 1) * 2);
            output[i] = (byte) Integer.parseInt(part, 16);
        }
        return Helper.arrayToMatrix(output);
    }

    /**
     * Formats a byte to a string with a length of 8. If the byte is smaller
     * than 128 (=7 bytes) the string will be padded with leading 0s.
     * 
     * @param b
     *            byte to be formatted
     * @return output string with a length of 8
     */
    public static String byteToString(byte b) {
        String value = Integer.toBinaryString(Byte.toUnsignedInt(b));
        int missingZeros = 8 - value.length();
        for (int i = 0; i < missingZeros; i++) {
            value = "0" + value;
        }
        return value;
    }

    /**
     * Compares to byte arrays and returns the number of bits that are different
     * 
     * @param original
     * @param newText
     * @return int > 0
     */
    public static int numberOfDifferentBits(byte[] original, byte[] newText) {
        // It is very easy to compare to strings
        String org = arrayToString(original, false);
        String newTex = arrayToString(newText, false);
        int diff = 0;
        for (int i = 0; i < org.length(); i++) {
            if (org.charAt(i) != newTex.charAt(i)) {
                diff++;
            }
        }
        return diff;
    }

}
