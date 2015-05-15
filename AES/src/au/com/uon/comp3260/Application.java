package au.com.uon.comp3260;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This is the Main Class that starts the application
 * 
 * @author Felix Behrendt & Andrew Thursby
 * 
 */
public class Application {

	public static void main(String[] args) {
		if (args == null || args.length < 3) {
			usage();
			return;
		}
		Path inputFile = Paths.get(args[1]);
		System.out.println(inputFile);
		Path outputFile = Paths.get(args[2]);
		if (args[0].equalsIgnoreCase("encrypt")) {
			Encrypter enc = new Encrypter();
			enc.encrypt(inputFile, outputFile);
		} else if (args[0].equalsIgnoreCase("decrypt")) {
			Decrypter dec = new Decrypter();
			dec.decrypt(inputFile, outputFile);
		} else {
			usage();
		}

	}

	public static void usage() {
		System.out
				.println("Welcome to AES Project for COMP3260 by Felix Behrendt and Andrew Thursby");
		System.out.println("");
		System.out.println("Usage:");
		System.out.println("encrypt inputfile outputfile");
		System.out
				.println("       inputfile:   path to the input file containing a plaintext and a key");
		System.out
				.println("       outputfile:  path to the output file where the ciphertext and analysis should be stored");
		System.out.println("decrypt inputfile outputfile");
		System.out
				.println("       inputfile:   path to the input file containing a ciphertext and a key");
		System.out
				.println("       outputfile:  path to the output file where the plaintext should be stored");
	}

}
