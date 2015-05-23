package au.com.uon.comp3260;

/**
 * This class can mix columns used in AES Encryption / Decryption
 * 
 * @author Andrew Thursby
 * 
 */
public class MixColumns {
	
	int[][] mixColMatrix = { { 2, 3, 1, 1 }, 		// used for encryption
			{ 1, 2, 3, 1 }, { 1, 1, 2, 3 }, { 3, 1, 1, 2 } };
	int[][] invMixColMatrix = { { 14, 11, 13, 9 }, 	// used for decryption
			{ 9, 14, 11, 13 }, { 13, 9, 14, 11 }, { 11, 13, 9, 14 } };

	public byte[][] mixColumns(byte[][] input, boolean decrypt) {
		byte[][] output = new byte[4][4];
		String[] results = new String[4];
		
		int[][] matrix = mixColMatrix;
		if (decrypt) {
			matrix = invMixColMatrix;
		}
		
		for (int i = 0; i < 4; i++) { // i = rows
			for (int j = 0; j < 4; j++) { // j = columns
				
				String sum = "";
				for (int a = 0; a < 4; a++) {	// reset results
					results[a] = "";
				}
				
				// Calculate value for each cell in column [i]
				for (int k = 0; k < 4; k++) {
					String binaryInput = Integer.toBinaryString(input[k][i]);
					
					// Bug fix: Add missing zeros to binaryString
					int missingZeros = 8 - binaryInput.length();
			        for (int count = 0; count < missingZeros; count++) {
			            binaryInput = "0" + binaryInput;
			        }
					
					switch (matrix[j][k]) {		// check mix columns matrix value
						case 1: results[k] = binaryInput; // x1 result = no change
						case 2: results[k] = multiply2(binaryInput);
						case 3: results[k] = multiply3(binaryInput);
						case 9: results[k] = multiply9(binaryInput);
						case 11: results[k] = multiply11(binaryInput);
						case 13: results[k] = multiply13(binaryInput);
						case 14: results[k] = multiply14(binaryInput);
					}
				}
				
				// calculate sum of results
				sum = results[0];
				for (int resultNum = 1; resultNum < 4; resultNum++) {
					sum = binaryXOR(sum, results[resultNum]);
				}
				
				// Convert binary string back to byte and store
				output[j][i] = (byte) Integer.parseInt(sum, 2);		
			}
		}
		return output;
	}
	
	// multiply binary String by 2
	public String multiply2(String binaryString) {
		StringBuilder sb = new StringBuilder();
		
		// shift binary left by 1
		for (int i = 1; i < 8; i++) {		// ignore first digit
			sb.append(binaryString.charAt(i));
		}
		sb.append("0");						// add last digit
		String output = sb.toString();
		
		String oneChar = "1";
		// if 1st digit of original value is a 1, run binaryXOR
		if (binaryString.charAt(0) == oneChar.charAt(0)) {
			output = binaryXOR(output, "11011");
		}
		return output;
	}
	
	// multiply binary String by 3
	public String multiply3(String binaryString) {
		
		// multiply by 2, then XOR with original value
		String output = multiply2(binaryString);
		output = binaryXOR(output, binaryString);
		return output;
	}
	
	// multiply by 9
	public String multiply9(String binaryString) {
		
		// x9 = ((( x2) x2) x2) +x
		String output = multiply2(binaryString);	
		output = multiply2(output);					
		output = multiply2(output);
		output = binaryXOR(output, binaryString);
		return output;
	}
	
	// multiply by 11
	public String multiply11(String binaryString) {
		
		// x11 = (((( x2) x2) +x) x2) +x
		String output = multiply2(binaryString);
		output = multiply2(output);
		output = binaryXOR(output, binaryString);
		output = multiply2(output);
		output = binaryXOR(output, binaryString);
		return output;
	}
	
	// multiply by 13
	public String multiply13(String binaryString) {
		
		// x13 = (((( x2) +x) x2) x2) +x
		String output = multiply2(binaryString);
		output = binaryXOR(output, binaryString);
		output = multiply2(output);
		output = multiply2(output);
		output = binaryXOR(output, binaryString);
		return output;
	}
	
	// multiply by 14
	public String multiply14(String binaryString) {
	
		// x14 = (((( x2) +x) x2) +x) x2
		String output = multiply2(binaryString);
		output = binaryXOR(output, binaryString);
		output = multiply2(binaryString);
		output = binaryXOR(output, binaryString);
		output = multiply2(binaryString);
		return output;
	}
	
	// XOR two binary Strings
	public String binaryXOR(String bin1, String bin2) {
		StringBuilder sb = new StringBuilder();
		
		// add missing zeros if required
		int missingZeros1 = 8 - bin1.length();
		int missingZeros2 = 8 - bin2.length();
		for (int i = 0; i < missingZeros1; i++) {
			bin1 = "0" + bin1;
		}
		for (int i = 0; i < missingZeros2; i++) {
			bin2 = "0" + bin2;
		}
		
		// XOR both Strings
		for (int i = 0; i < 8; i++) {
			if (bin1.charAt(i) == bin2.charAt(i)) {
				sb.append("0");
			} else {
				sb.append("1");
			}
		}
		return sb.toString();
	}
	
}
