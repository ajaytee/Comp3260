package au.com.uon.comp3260;

/**
 * This class can shift rows used in AES Encryption / Decryption
 * 
 * @author Andrew Thursby
 * 
 */
public class ShiftRows {

	public byte[][] shiftRows(byte[][] input, boolean decrypt) {

		byte[][] output = new byte[4][4];

		if (!decrypt) {
			// shift left - encryption
			for (int i = 1; i < 4; i++) { // initialise i to 1 as Row 0 remains
											// unchanged
				for (int j = 0; j < 4; j++) { // i = rows, j = columns
					int newColumn = j - i;
					if (newColumn < 0) {
						newColumn = newColumn + 4; // circular shift
					}
					output[i][newColumn] = input[i][j];
				}
			}

		} else {
			// shift right - decryption
			for (int i = 1; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					int newColumn = j + i;
					if (newColumn > 3) {
						newColumn = newColumn - 4;
					}
					output[i][newColumn] = input[i][j];
				}
			}
		}
		return output;
	}
}
