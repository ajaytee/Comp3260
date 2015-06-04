/**
 * 
 */
package au.com.uon.comp3260.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import au.com.uon.comp3260.AESType;
import au.com.uon.comp3260.AddRoundKey;
import au.com.uon.comp3260.Decrypter;
import au.com.uon.comp3260.Encrypter;
import au.com.uon.comp3260.MixColumns;
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
        int round = 0;
        int errors = 0;
        String testData[] = testData();
        for (String testKey : testData) {
            for (String testPlainTest : testData) {
                String cipherText = enc.encrypt(testPlainTest, testKey, null,
                        AESType.AES0);
                String decryptedCipherText = dec.decrypt(cipherText, testKey,
                        null);
                String output = "Round " + round + "\n      Plain: "
                        + Helper.byteStringToHexString(testPlainTest)
                        + "\n    Chipher: "
                        + Helper.byteStringToHexString(cipherText)
                        + "\n Deciphered: "
                        + Helper.byteStringToHexString(decryptedCipherText);
                if (testPlainTest.equals(decryptedCipherText)) {
                    System.out.println("Success: " + output);
                } else {
                    System.err.println("Fail:    " + output);
                    errors++;
                }
                round++;
            }
        }
        assertEquals(0, errors);
    }

    @Test
    public void testEncrypt() {
        Encrypter enc = new Encrypter();
        String key = testData()[0];
        String plainText = key;
        String cipherText = enc.encrypt(plainText, key, null, AESType.AES0);
        String hexStringOfCipher = Helper.matrixToHexString(Helper
                .arrayToMatrix(Helper.stringToByteArray(cipherText)));
        System.out.println("Encrypted Cipher Text: " + hexStringOfCipher);
        assertEquals("66E94BD4EF8A2C3B884CFA59CA342B2E", hexStringOfCipher);
    }

    @Test
    public void testEncryptExample2() {
        Encrypter enc = new Encrypter();
        String key = "00000000000000010000001000000011000001000000010100000110000001110000100000001001000010100000101100001100000011010000111000001111";
        String plainText = "00000000000100010010001000110011010001000101010101100110011101111000100010011001101010101011101111001100110111011110111011111111";
        String cipherText = "01101001110001001110000011011000011010100111101100000100001100001101100011001101101101111000000001110000101101001100010101011010";
        String encryptedText = enc.encrypt(plainText, key, null, AESType.AES0);
        String hexStringOfEncryptedText = Helper.matrixToHexString(Helper
                .arrayToMatrix(Helper.stringToByteArray(encryptedText)));
        String hexStringOfCipher = Helper.matrixToHexString(Helper
                .arrayToMatrix(Helper.stringToByteArray(cipherText)));
        System.out
                .println("Encrypted Cipher Text: " + hexStringOfEncryptedText);
        assertEquals(hexStringOfCipher, hexStringOfEncryptedText);
    }

    @Test
    public void testDecrypt() {
        Decrypter dec = new Decrypter();
        String key = testData()[0];
        String plainText = key;
        String cipherText = Helper.matrixToString(
                Helper.hexStringToMatrix("66E94BD4EF8A2C3B884CFA59CA342B2E"),
                false);
        String decryptedText = dec.decrypt(cipherText, key, null);
        String hexStringOfDecryptedCipherText = Helper.matrixToHexString(Helper
                .arrayToMatrix(Helper.stringToByteArray(decryptedText)));
        System.out.println("Decrypted Cipher Text: "
                + hexStringOfDecryptedCipherText);
        assertEquals(plainText, decryptedText);
    }

    @Test
    public void testDecryptExample2() {
        Decrypter dec = new Decrypter();
        String key = "00000000000000010000001000000011000001000000010100000110000001110000100000001001000010100000101100001100000011010000111000001111";
        String plain = "00000000000100010010001000110011010001000101010101100110011101111000100010011001101010101011101111001100110111011110111011111111";
        String cipherText = "01101001110001001110000011011000011010100111101100000100001100001101100011001101101101111000000001110000101101001100010101011010";

        String decryptedText = dec.decrypt(cipherText, key, null);
        String hexStringOfDecryptedText = Helper.matrixToHexString(Helper
                .arrayToMatrix(Helper.stringToByteArray(decryptedText)));
        String hexStringOfPlainText = Helper.matrixToHexString(Helper
                .arrayToMatrix(Helper.stringToByteArray(plain)));
        assertEquals(hexStringOfPlainText, hexStringOfDecryptedText);
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
    public void testAddRoundKeyOutput() {
        System.out.println("TestAdd Round Key Output");
        byte[][] keyMatrix = Helper
                .hexStringToMatrix("00000000000000000000000000000000");
        byte[] key = Helper.matrixToArray(keyMatrix);
        AddRoundKey roundKeyAdder = new AddRoundKey(key, false);
        byte[][] input = Helper
                .hexStringToMatrix("00000000000000000000000000000000");
        byte[][] output = roundKeyAdder.addRoundKey(input, 0);
        System.out.println("Round 0: " + Helper.matrixToHexString(output));
        for (int round = 1; round < 11; round++) {
            output = roundKeyAdder.addRoundKey(input, round);
            System.out.println("Round " + round + ": "
                    + Helper.matrixToHexString(output));
        }
    }

    @Test
    public void testAddRoundKeyEncryption() {
        byte[][] keyMatrix = Helper
                .hexStringToMatrix("00000000000000000000000000000000");
        byte[] key = Helper.matrixToArray(keyMatrix);
        AddRoundKey roundKeyAdder = new AddRoundKey(key, false);

        byte[][] input = Helper
                .hexStringToMatrix("00000000000000000000000000000000");
        byte[][] output = roundKeyAdder.addRoundKey(input, 0);
        assertEquals("00000000000000000000000000000000",
                Helper.matrixToHexString(output));

        input = Helper.hexStringToMatrix("63636363636363636363636363636363");
        output = roundKeyAdder.addRoundKey(input, 1);
        assertEquals("01000000010000000100000001000000",
                Helper.matrixToHexString(output));

        input = Helper.hexStringToMatrix("5D7C7C425D7C7C425D7C7C425D7C7C42");
        output = roundKeyAdder.addRoundKey(input, 2);
        assertEquals("C6E4E48BA48787E8C6E4E48BA48787E8",
                Helper.matrixToHexString(output));

        input = Helper.hexStringToMatrix("B8BAC794039F49DFB8BAC794039F49DF");
        output = roundKeyAdder.addRoundKey(input, 3);
        assertEquals("282DF3C46AF386254A4E90A70890E546",
                Helper.matrixToHexString(output));

        input = Helper.hexStringToMatrix("45D41785B030A0C8253EED720B0B8474");
        output = roundKeyAdder.addRoundKey(input, 4);
        assertEquals("ABD2CDFE375AB54950A0AFC0759A6A5F",
                Helper.matrixToHexString(output));

        input = Helper.hexStringToMatrix("AB4164E4ADFCA83AF3DFC7868A324CB3");
        output = roundKeyAdder.addRoundKey(input, 5);
        assertEquals("D46F4F6C55B896337E05BB3D7979DE23",
                Helper.matrixToHexString(output));

        input = Helper.hexStringToMatrix("E8938112135D5DC97BD008A123714CB7");
        output = roundKeyAdder.addRoundKey(input, 6);
        assertEquals("04F2CA9707782845E22F019649C5D710",
                Helper.matrixToHexString(output));

        input = Helper.hexStringToMatrix("96DFF34228754F44C03D64BD52FE71CB");
        output = roundKeyAdder.addRoundKey(input, 7);
        assertEquals("B7AAE4C51D252D4F6C920F8194E58150",
                Helper.matrixToHexString(output));

        input = Helper.hexStringToMatrix("2D1E8F0F288802E33DC6CC537F1E310A");
        output = roundKeyAdder.addRoundKey(input, 8);
        assertEquals("23E78C3C132163DBAAC0C6572E03CB95",
                Helper.matrixToHexString(output));

        input = Helper.hexStringToMatrix("CE2AD677DBD8DFEF134FCF99654FA58A");
        output = roundKeyAdder.addRoundKey(input, 9);
        assertEquals("7FFE0E9551A566350E347C472929ECCB",
                Helper.matrixToHexString(output));

        input = Helper.hexStringToMatrix("D206101FD118CE2AABA5AB96A5BB33A0");
        output = roundKeyAdder.addRoundKey(input, 10);
        assertEquals("66E94BD4EF8A2C3B884CFA59CA342B2E",
                Helper.matrixToHexString(output));
    }

    @Test
    public void testAddRoundKeyDecryption() {
        byte[][] keyMatrix = Helper
                .hexStringToMatrix("00000000000000000000000000000000");
        byte[] key = Helper.matrixToArray(keyMatrix);
        AddRoundKey roundKeyAdder = new AddRoundKey(key, true);

        byte[][] input = Helper
                .hexStringToMatrix("66E94BD4EF8A2C3B884CFA59CA342B2E");
        byte[][] output = roundKeyAdder.addRoundKey(input, 0);
        assertEquals("D206101FD118CE2AABA5AB96A5BB33A0",
                Helper.matrixToHexString(output));

        input = Helper.hexStringToMatrix("7FFE0E9551A566350E347C472929ECCB");
        output = roundKeyAdder.addRoundKey(input, 1);
        assertEquals("CE2AD677DBD8DFEF134FCF99654FA58A",
                Helper.matrixToHexString(output));
    }

    @Test
    public void testShiftRows2() {
        ShiftRows rowShifter = new ShiftRows();

        byte[][] input = Helper
                .hexStringToMatrix("63636363636363636363636363636363");
        byte[][] output = rowShifter.shiftRows(input, false);
        assertEquals("63636363636363636363636363636363",
                Helper.matrixToHexString(output));

        input = Helper.hexStringToMatrix("7C6363637C6363637C6363637C636363");
        output = rowShifter.shiftRows(input, false);
        assertEquals("7C6363637C6363637C6363637C636363",
                Helper.matrixToHexString(output));

        input = Helper.hexStringToMatrix("34D80D1C020D443FD62F605C3060D95A");
        output = rowShifter.shiftRows(input, false);
        assertEquals("340D605A022FD91CD6600D3F30D8445C",
                Helper.matrixToHexString(output));

        // Decrypion
        input = Helper.hexStringToMatrix("340D605A022FD91CD6600D3F30D8445C");
        output = rowShifter.shiftRows(input, true);
        assertEquals("34D80D1C020D443FD62F605C3060D95A",
                Helper.matrixToHexString(output));

        // Decryption
        input = Helper.hexStringToMatrix("7C6363637C6363637C6363637C636363");
        output = rowShifter.shiftRows(input, true);
        assertEquals("7C6363637C6363637C6363637C636363",
                Helper.matrixToHexString(output));

    }

    @Test
    public void testMixColumns() {
        MixColumns columnMixerEnc = new MixColumns(false);
        MixColumns columnMixerDec = new MixColumns(true);

        byte[][] input = Helper
                .hexStringToMatrix("63636363636363636363636363636363");
        byte[][] output = columnMixerEnc.mixColumns(input);
        assertEquals("63636363636363636363636363636363",
                Helper.matrixToHexString(output));

        input = Helper.hexStringToMatrix("7C6363637C6363637C6363637C636363");
        output = columnMixerEnc.mixColumns(input);
        assertEquals("5D7C7C425D7C7C425D7C7C425D7C7C42",
                Helper.matrixToHexString(output));

        input = Helper.hexStringToMatrix("B417699B4969173DB417699B4969173D");
        output = columnMixerEnc.mixColumns(input);
        assertEquals("B8BAC794039F49DFB8BAC794039F49DF",
                Helper.matrixToHexString(output));

        input = Helper.hexStringToMatrix("340D605A022FD91CD6600D3F30D8445C");
        output = columnMixerEnc.mixColumns(input);
        assertEquals("45D41785B030A0C8253EED720B0B8474",
                Helper.matrixToHexString(output));

        // Decryption
        input = Helper.hexStringToMatrix("45D41785B030A0C8253EED720B0B8474");
        output = columnMixerDec.mixColumns(input);
        assertEquals("340D605A022FD91CD6600D3F30D8445C",
                Helper.matrixToHexString(output));

        // Decryption
        input = Helper.hexStringToMatrix("5D7C7C425D7C7C425D7C7C425D7C7C42");
        output = columnMixerDec.mixColumns(input);
        assertEquals("7C6363637C6363637C6363637C636363",
                Helper.matrixToHexString(output));
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
