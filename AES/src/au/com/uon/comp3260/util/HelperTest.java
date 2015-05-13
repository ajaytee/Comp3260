/**
 * 
 */
package au.com.uon.comp3260.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Felix Behrendt
 * 
 */
public class HelperTest {

	private static final String sample128String = "10000011100011111111110101101100011111110011111110111011011000010100111100011110101000011100110100101100110110101111010010110000";

	@Test
	public void testArrayToMatrix() {
		byte[] testArr = Helper.stringToByteArray(sample128String);
		byte[][] matrix = Helper.arrayToMatrix(testArr);
		byte[] actual = Helper.matrixToArray(matrix);
		assertArrayEquals(testArr, actual);
	}

	@Test
	public void testArrayToString() {
		byte[] testArr = Helper.stringToByteArray(sample128String);
		String test1 = Helper.arrayToString(testArr, false);
		String test2 = Helper.arrayToString(testArr, true).replaceAll(" ", "");
		assertEquals(test1, test2);
		assertEquals(test1, sample128String);
	}

}
