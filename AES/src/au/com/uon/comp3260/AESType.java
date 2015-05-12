package au.com.uon.comp3260;

/**
 * 
 * @author Felix Behrendt
 */
public enum AESType {
	AES0(false, false, false, false),
	/**/AES1(true, false, false, false),
	/**/AES2(false, true, false, false),
	/**/AES3(false, false, true, false),
	/**/AES4(false, false, false, true);

	private boolean skipSubBytes;
	private boolean skipShiftRows;
	private boolean skipMixColumns;
	private boolean skipAddRoundKey;

	private AESType(boolean skipSubBytes, boolean skipShiftRows,
			boolean skipMixColumns, boolean skipAddRoundKey) {
		this.skipSubBytes = skipSubBytes;
		this.skipShiftRows = skipShiftRows;
		this.skipMixColumns = skipMixColumns;
		this.skipAddRoundKey = skipAddRoundKey;
	}

	public boolean isSkipSubBytes() {
		return skipSubBytes;
	}

	public boolean isSkipShiftRows() {
		return skipShiftRows;
	}

	public boolean isSkipMixColumns() {
		return skipMixColumns;
	}

	public boolean isSkipAddRoundKey() {
		return skipAddRoundKey;
	}

}
