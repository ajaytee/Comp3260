package au.com.uon.comp3260;

public class MixColumns {

	// Assuming byteString input
	// Process one column at a time
	// Each column * the mixColumn matrix
	
	String byteStrings = "";
	int[][] mixColMatrix = { { 2,3,1,1 },		// used for encryption
							 { 1,2,3,1 },
							 { 1,1,2,3 },
							 { 3,1,1,2 }
						   };
	int[][] invMixColMatrix = { { 14,11,13,9 },	// used for decryption
								{ 9,14,11,13 },
								{ 13,9,14,11 },
								{ 11,13,9,14 }	
							  };
	
	public String mix (String inputString, String option) {
		// usage: option is either "encryption" or "decryption"
		int[][] mixColumnsMatrix = new int[4][4];
		if (option.equals("encryption")) {
			mixColumnsMatrix = mixColMatrix;
		} else {
			if (option.equals("decryption")) {
		}
			mixColumnsMatrix = invMixColMatrix;
		}
		byteStrings = inputString;
		String[][] hexMatrix = new String[4][4];
		// ADD: put byteStrings into hex matrix
		
		
		// Process each column, multiply by the mixColumnsMatrix
		// Steps:	1. Each cell in column 1 is multiplied by 2,3,1,1 respectively, sum is value of cell [0][0]
		//			2. Each cell in column 1 is multiplied by 1,2,3,1 respectively, sum is value of cell [1][0]
		//			3. Each cell in column 1 is multiplied by 1,1,2,3 respectively, sum is value of cell [2][0]
		//			4. Each cell in column 1 is multiplied by 3,1,1,2 respectively, sum is value of cell [3][0]
		
		for (int i=0; i<4; i++) {		// i = rows
			for (int j=0; j<4; j++) {	// j = columns
				
				// hexMatrix[j][i] = sum of: 	hexMatrix[0][i] * mixColumnsMatrix[j][0];
				//					 			hexMatrix[1][i] * mixColumnsMatrix[j][1];
				//								hexMatrix[2][i] * mixColumnsMatrix[j][2];
				//								hexMatrix[3][i] * mixColumnsMatrix[j][3];
				//
				//
				
			}
			
		}
		
		
		String mixedString = "";
		
		return mixedString;
	}
	
}
