COMP3260 - Assignment 2
by
Andrew Thursby (CXXXXXXX)
Felix Behrendt (C3215090)

Classes:

Application:
Main class that starts the program. Call main function to see usage.

AESType:
Enum for the different AES Variations

AddRoundKey:
Class for adding the round key to a byte matrix. Also precalculating keys for decryption

Decrypter:
Class the handles decryption of cipher texts.

Encrypter:
Class the handles encryption of cipher texts and calculates avalanche effect.

MixColumns:
Class for mixing columns during encryption / decryption as part of AES.

ShiftRows:
Class for shifting rows during encryption / decryption as part of AES.

SubstituteBytes:
Class for substituting bytes during encryption / decryption as part of AES.

Helper:
Provides some static methods to convert arrays to matrix, parse files etc.

HelperTests:
JUnit Tests for some java methods

OutputFileWriter:
Collects all data during the encryption / decryption progress and creates the required output file.