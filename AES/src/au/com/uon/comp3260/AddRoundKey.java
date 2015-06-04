package au.com.uon.comp3260;

import au.com.uon.comp3260.util.Helper;

/**
 * 
 * This class can add a round key used in AES Encryption / Decryption
 * 
 * @author Felix Behrendt
 * 
 */
public class AddRoundKey {

    private final byte[][] subkeys;
    private final boolean decrypt;

    public AddRoundKey(byte[] key, boolean decrypt) {
        subkeys = Helper.copyByteMatrix(generateSubkeys(key));
        this.decrypt = decrypt;
    }

    private static byte[][] generateSubkeys(byte[] key) {
        final byte[][] _subkeys = new byte[11][16];
        _subkeys[0] = key;

        byte[] keys = new byte[176];

        // set first 16 bytes
        for (int i = 0; i < 16; i++) {
            keys[i] = key[i];
        }

        int round = 1;
        int i = 16; // first 16 bytes already set
        byte[] temp = new byte[4];

        while (i < 176) { // need 176 bytes of keys

            // set temp bytes
            for (int j = 0; j < 4; j++) {
                temp[j] = keys[j + i - 4];
            }

            // if new key, do main calculations
            if (i % 16 == 0) {

                // Rotate tempBytes
                temp = rotate(temp);

                // Apply S-box
                for (int j = 0; j < 4; j++) {
                    // We always want the encryption sbox. for decryption we use
                    // the same keys, but in inverse order.
                    temp[j] = applySbox(temp[j], false);
                }

                // XOR 1st bit of temp with round constant
                temp[0] ^= rcon(round);
                round++;
            }

            byte newValue = 0;

            // assign temp bytes to keys
            for (int j = 0; j < 4; j++) {

                // set new value
                newValue = (byte) (temp[j] ^ keys[i - 16]);

                // store new value
                keys[i] = newValue;

                i++;
            }
        }

        // convert keys into subkeys arrays
        int total = 16;

        for (int rnd = 1; rnd < 11; rnd++) {
            final byte[] newRoundKey = new byte[16];

            // set new key value
            for (int j = 0; j < 16; j++) {
                newRoundKey[j] = keys[total];
                total++;
            }

            // set subkeys to new key value

            _subkeys[rnd] = Helper.copyByteArray(newRoundKey);

            // byte[][] testMatrix = Helper.arrayToMatrix(newRoundKey);
            // String testString = Helper.matrixToHexString(testMatrix);
            // System.out.println("Subkey Round " + rnd + " - " + testString);

        }
        return _subkeys;
    }

    private static byte applySbox(byte input, boolean decrypt) {
        char[] matrix = SubstituteBytes.subByteMatrix;
        if (decrypt) {
            // Use the inverted matrix when decrypting
            matrix = SubstituteBytes.inversedSubByteMatrix;
        }
        return (byte) matrix[input & 0xFF];
    }

    private static int rcon(int input) {
        int x = 1;
        // if input is 0, return 0
        if (input == 0) {
            x = 0;
        } else {
            // until input = 1, multiply x by 2
            while (input != 1) {
                x = multiply((byte) x, (byte) 2);
                input--;
            }
        }
        return x;
    }

    // Multiplies two bytes in garlois field 2^8
    private static byte multiply(byte a, byte b) {
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

    public byte[][] addRoundKey(byte[][] matrix, int round) {
        // String action = "Encrypt";
        if (decrypt) {
            round = 10 - round;
            // action = "Decrypt";
        }
        byte[] key = subkeys[round];
        // System.out.println(action + " with key: "
        // + Helper.matrixToHexString(Helper.arrayToMatrix(key))
        // + " in round: " + round);
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                matrix[j][i] = (byte) (matrix[j][i] ^ key[i * 4 + j]);
        return matrix;
    }

    private static byte[] rotate(byte[] input) {
        byte[] output = new byte[input.length];
        byte a = input[0];
        for (int i = 0; i < 3; i++)
            output[i] = input[i + 1];
        output[3] = a;
        return output;
    }

}
