package au.com.uon.comp3260;

/**
 * This class can shift rows used in AES Encryption / Decryption
 * 
 * @author Andrew Thursby
 * 
 */
public class ShiftRows {

	// Accepts String as input, converts to Matrix and shifts rows, returns
	// String as output
	// Call initialise with String first, then call shift("encryption") or
	// shift("decryption") to execute the shift
	String inputString = "";
	String[][] matrix = new String[4][4];

	public void initialise(String inputString) {
		matrix = convertToMatrix(inputString);
	}

	// Convert String to Hex Matrix
	public String[][] convertToMatrix(String binaryString) {

		// ADD: split binary into bytes
		String[] byteStrings = new String[16];

		// Convert byteStrings to hexStrings
		String[] hexStrings = new String[16];
		for (int i = 0; i < 16; i++) {
			hexStrings[i] = Integer.toHexString(Integer.parseInt(
					byteStrings[i], 2));
		}

		// Add Hex Strings to hexMatrix, columns first
		String[][] hexMatrix = new String[4][4];
		int k = 0;
		for (int i = 0; i < 4; i++) { // i = row #
			for (int j = 0; j < 4; j++) { // j = column #
				hexMatrix[j][i] = hexStrings[k];
				k++;
			}
		}
		return hexMatrix;
	}

	// Shift rows in matrix
	public String shift(String option) {
		// Usage: option sets encryption or decryption

		if (option.equals("encyption")) {
			// shift left
			for (int i = 1; i < 4; i++) { // initialise i at 1 as Row 0 remains
											// unchanged
				for (int j = 0; j < 4; j++) { // i = rows, j = columns

					// Row 2 - shift left by 1
					// Row 3 - shift left by 2
					// Row 4 - shift left by 3
					String tempValue = matrix[i][j];
					int newPos = j - i;
					if (newPos < 0) {
						newPos = newPos + 4; // if new column = -1, it actually
												// means 3 as its a circular
												// shift
					}
					matrix[i][newPos] = tempValue;
				}
			}
		} else { // option = "decryption"
					// shift right
			for (int i = 1; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					String tempValue = matrix[i][j];
					int newPos = j + i;
					if (newPos > 3) {
						newPos = newPos - 4;
					}
					matrix[i][newPos] = tempValue;
				}
			}
		}
		String shiftedString = convertMatrixToString();
		return shiftedString;
	}

	// Convert matrix to String
	public String convertMatrixToString() {
		String newString = "";

		// ADD: matrix to newString conversion

		return newString;
	}

}
