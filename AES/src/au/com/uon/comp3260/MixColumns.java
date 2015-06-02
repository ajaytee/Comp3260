package au.com.uon.comp3260;

/**
 * This class can mix columns used in AES Encryption / Decryption
 * 
 * @author Andrew Thursby, Felix Behrendt
 * 
 */
public class MixColumns {

	static byte[][] matrix = { { 0x03, 0x0b }, { 0x01, 0x0d }, { 0x01, 0x09 },
			{ 0x02, 0x0e } };

	// Multiplies two bytes in garlois field 2^8
	private byte multiply(byte a, byte b) {
		byte returnValue = 0;
		byte temp = 0;
		while (a != 0) {
			if ((a & 1) != 0)
				returnValue = (byte) (returnValue ^ b);
			temp = (byte) (b & 0x80);
			b = (byte) (b << 1);
			if (temp != 0)
				b = (byte) (b ^ 0x1b);
			a = (byte) ((a & 0xff) >> 1);
		}
		return returnValue;
	}

	public byte[][] mixColumns(byte[][] input, boolean decrypt) {
		int[] temp = new int[4];

		int arrayIndex = decrypt ? 1 : 0;
		byte a = matrix[0][arrayIndex];
		byte b = matrix[1][arrayIndex];
		byte c = matrix[2][arrayIndex];
		byte d = matrix[3][arrayIndex];

		for (int i = 0; i < 4; i++) {
			temp[0] = multiply(d, input[0][i]) ^ multiply(a, input[1][i])
					^ multiply(b, input[2][i]) ^ multiply(c, input[3][i]);
			temp[1] = multiply(c, input[0][i]) ^ multiply(d, input[1][i])
					^ multiply(a, input[2][i]) ^ multiply(b, input[3][i]);
			temp[2] = multiply(b, input[0][i]) ^ multiply(c, input[1][i])
					^ multiply(d, input[2][i]) ^ multiply(a, input[3][i]);
			temp[3] = multiply(a, input[0][i]) ^ multiply(b, input[1][i])
					^ multiply(c, input[2][i]) ^ multiply(d, input[3][i]);
			for (int j = 0; j < 4; j++)
				input[j][i] = (byte) (temp[j]);
		}

		return input;
	}
}