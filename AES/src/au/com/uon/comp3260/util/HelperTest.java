/**
 * 
 */
package au.com.uon.comp3260.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import au.com.uon.comp3260.AESType;
import au.com.uon.comp3260.Decrypter;
import au.com.uon.comp3260.Encrypter;
import au.com.uon.comp3260.ShiftRows;
import au.com.uon.comp3260.SubstituteBytes;

/**
 * @author Felix Behrendt
 * 
 */
public class HelperTest {

	private static final String sample128String = "10000011100011111111110101101100011111110011111110111011011000010100111100011110101000011100110100101100110110101111010010110000";

	@Test
	public void testArrayToMatrix() {
		for (String testStr : testData()) {
			byte[] testArr = Helper.stringToByteArray(testStr);
			byte[][] matrix = Helper.arrayToMatrix(testArr);
			byte[] actual = Helper.matrixToArray(matrix);
			assertArrayEquals(testArr, actual);
		}
	}

	@Test
	public void testArrayToString() {
		for (String testStr : testData()) {
			byte[] testArr = Helper.stringToByteArray(testStr);
			String test1 = Helper.arrayToString(testArr, false);
			String test2Pretty = Helper.arrayToString(testArr, true);
			String test2 = test2Pretty.replaceAll(" ", "");
			assertEquals(128, testStr.length());
			assertEquals(128, test1.length());
			assertEquals(128, test2.length());
			assertEquals(128 + 16, test2Pretty.length());
			assertEquals("Converted string should be equal to original", test1,
					testStr);
			assertEquals(
					"Pretty version should be the same without whitespaces",
					test1, test2);
		}
	}

	@Test
	public void testByteArrayDiff() {
		String[] testData = testData();
		byte[] testArr0s = Helper.stringToByteArray(testData[0]);
		byte[] testArr1s = Helper.stringToByteArray(testData[1]);
		int diffs = Helper.numberOfDifferentBits(testArr0s, testArr1s);
		int diffsInverted = Helper.numberOfDifferentBits(testArr1s, testArr0s);
		assertEquals(128, diffs);
		assertEquals(128, diffsInverted);
	}

	@Test
	public void testDecryptEncrypt() {
		Encrypter enc = new Encrypter();
		Decrypter dec = new Decrypter();
		for (String testKey : testData()) {
			for (String testPlainTest : testData()) {
				String cipherText = enc.encrypt(testPlainTest, testKey, null,
						AESType.AES0);
				String decryptedCipherText = dec.decrypt(cipherText, testKey,
						null);
				assertEquals(testPlainTest, decryptedCipherText);
			}
		}
	}

	@Test
	public void testShiftRows() {
		ShiftRows rowShifter = new ShiftRows();
		byte[][] encrypted;
		byte[][] decrypted;
		for (String testPlainText : testData()) {

			// change test String to byte matrix
			byte[] byteArray = Helper.stringToByteArray(testPlainText);
			byte[][] testBytes = Helper.arrayToMatrix(byteArray);

			// shift rows encryption on testPlainText
			encrypted = rowShifter.shiftRows(testBytes, false);

			// shift rows decryption on encrypted
			decrypted = rowShifter.shiftRows(encrypted, true);

			// change decrypted to String
			String plainText = Helper.matrixToString(testBytes, false);
			String decryptedText = Helper.matrixToString(decrypted, false);

			// check decryptedText = testPlainText
			assertEquals(decryptedText, plainText);
		}
	}

	@Test
	public void testSubstituteBytes() {
		SubstituteBytes byteSubstituter = new SubstituteBytes();
		byte[][] input = Helper
				.hexStringToMatrix("00000000000000000000000000000000");
		byte[][] output = byteSubstituter.substituteBytes(input, false);
		assertEquals("63636363636363636363636363636363",
				Helper.matrixToHexString(output));

		input = Helper.hexStringToMatrix("01000000010000000100000001000000");
		output = byteSubstituter.substituteBytes(input, false);
		assertEquals("7C6363637C6363637C6363637C636363",
				Helper.matrixToHexString(output));

		input = Helper.hexStringToMatrix("D46F4F6C55B896337E05BB3D7979DE23");
		output = byteSubstituter.substituteBytes(input, false);
		assertEquals("48A88450FC6C90C3F36BEA27B6B61D26",
				Helper.matrixToHexString(output));

		input = Helper.hexStringToMatrix("04F2CA9707782845E22F019649C5D710");
		output = byteSubstituter.substituteBytes(input, false);
		assertEquals("F2897488C5BC346E98157C903BA60ECA",
				Helper.matrixToHexString(output));

		input = Helper.hexStringToMatrix("B7AAE4C51D252D4F6C920F8194E58150");
		output = byteSubstituter.substituteBytes(input, false);
		assertEquals("A9AC69A6A43FD884504F760C22D90C53",
				Helper.matrixToHexString(output));

		input = Helper.hexStringToMatrix("23E78C3C132163DBAAC0C6572E03CB95");
		output = byteSubstituter.substituteBytes(input, false);
		assertEquals("269464EB7DFDFBB9ACBAB45B317B1F2A",
				Helper.matrixToHexString(output));

		// Decrypt
		input = Helper.hexStringToMatrix("A9AC69A6A43FD884504F760C22D90C53");
		output = byteSubstituter.substituteBytes(input, true);
		assertEquals("B7AAE4C51D252D4F6C920F8194E58150",
				Helper.matrixToHexString(output));

		// Decrypt
		input = Helper.hexStringToMatrix("269464EB7DFDFBB9ACBAB45B317B1F2A");
		output = byteSubstituter.substituteBytes(input, true);
		assertEquals("23E78C3C132163DBAAC0C6572E03CB95",
				Helper.matrixToHexString(output));
	}

	/**
	 * Creates different sample data to test with e.g. all 0s, all 1s, 2 random
	 * strings
	 * 
	 * @return strings with a length of 128 characters.
	 */
	private static String[] testData() {
		StringBuilder sb0s = new StringBuilder();
		StringBuilder sb1s = new StringBuilder();
		StringBuilder sbRandom0 = new StringBuilder();
		StringBuilder sbRandom1 = new StringBuilder();
		for (int i = 0; i < 128; i++) {
			sb0s.append("0");
			sb1s.append("1");
			sbRandom0.append((Math.random() > 0.5) ? "0" : "1");
			sbRandom1.append((Math.random() > 0.5) ? "0" : "1");
		}

		String[] testData = new String[5];
		testData[0] = sb0s.toString();
		testData[1] = sb1s.toString();
		testData[2] = sbRandom0.toString();
		testData[3] = sbRandom1.toString();
		testData[4] = sample128String;
		return testData;
	}
}
