import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The main method for SRPN. A Reverse Polish Notation calculator with the added features of:
 * Saturation checker: user input and calculations cannot exceed the Integer's maximum or minimum value 
 * Handling comments: it will not calculate any input after a '#' is entered
 * Random numbers: it will print out random numbers from a list
 * Octal calculator: checks if the first number of an input value is 0, then any succeeding values are treated as octal and converted to decimal
 * Error handling: it doesn't allow divisors to be zero, negative powers, unrecognised characters and stack overflow/underflow
 * 
 * @author Manvir Ubhi
 * @version 1.0
 * @release 17/11/2017
 * @See InputHandler.java
 */


public class SRPN {

	/**
	 * Main method which gets user input, and passes it to 'InputHandler' class.
	 */
	public static void main(String[] args) {
		InputHandler i = new InputHandler();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			//Keep on accepting input from the command-line
			while(true) {
				// Stores user input in 'command' variable
				String command = reader.readLine();

				//Close on an End-of-file (EOF) (Ctrl-D on the terminal)
				if(command == null) {
					//Exit code 0 for a graceful exit
					System.exit(0);
				}

				//Otherwise, (attempt to) process the character
				i.processCommand(command);          
			}
		} 
		catch(IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
}
