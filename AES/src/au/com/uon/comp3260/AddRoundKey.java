package au.com.uon.comp3260;

/**
 * 
 * This class can add a round key used in AES Encryption / Decryption
 * 
 * @author Felix Behrendt
 * 
 */
public class AddRoundKey {

	private final byte[][] subkeys;

	public AddRoundKey(byte[] key) {
		subkeys = generateSubkeys(key);
	}

	private static byte[][] generateSubkeys(byte[] key) {
		byte[][] subkeys = new byte[11][16];
		subkeys[0] = key;

		for (int round = 1; round < 11; round++) {
			byte subkey[] = new byte[16];
			subkeys[round] = subkey;
		}

		// Key expansion
		
		int round = 0;
		int rounds = 10;
		int i = 16;
		byte[] tempBytes = new byte[4];
		
		while (i < 176) { // need 176 bytes of key
		    
		    round++;
		    // set next set of temp bytes
		    for (int j = 0; j < 4; j++) {
		        tempBytes[j] = subkeys[round-1][j+12]; // tempBytes = last 4 bytes of previous subkey
		        // for Round 1:
		        // tempBytes[0] = subkeys[0][12]
		        // tempBytes[1] = subkeys[0][13]
		        // tempBytes[2] = subkeys[0][14]
		        // tempBytes[3] = subkeys[0][15]
		    }
		    
		    // if i / 16 gives no remainder = new Key
		    if (i % 16 == 0) {
		        // TODO: rotate temp, then apply s-box, then XOR with rcon(i)
		        
		    }
		    
		    // set next 4 bytes of subkey
		    for (int j = 0; j < 4; j++) {
		        
		        // TODO: setting new 4 values in subkey
		        //subkeys[round][(i % 16)] =   // i % 16 gives new key position
		        
		        // May have to do this in 'words' as opposed to bytes, words are just groups of 4 bytes
		        // subkey[1][0] = subkey[0][0] ^ tempBytes[0]
		        // subkey[
		    }
		}
		
		
		
		
		return subkeys;
	}

	public byte[][] addRoundKey(byte[][] matrix, int round, boolean decrypt) {
		if (decrypt)
			round = 10 - round;
		byte[] key = subkeys[round];
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				matrix[j][i] ^= key[i * 4 + j];
		return matrix;
	}

	private byte[] rotate(byte[] input) {
		byte[] output = new byte[input.length];
		byte a = input[0];
		for (int i = 0; i < 3; i++)
			output[i] = output[i + 1];
		output[3] = a;
		return output;
	}

}
