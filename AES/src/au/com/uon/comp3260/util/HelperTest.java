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
