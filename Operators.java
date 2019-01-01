/**
 * The operator class for SRPN, which sets up and deals with calculations involving arithmetic operators.
 * 
 * @author Manvir Ubhi
 * @version 1.0
 * @release 21/11/2017
 * @See InputHandler.java
 */

public class Operators{
	SrpnStack srpnStack;

	/**
	 * This method retrieves the contents of the stack
	 * @param stack the stack that the operators work on
	 * @return Nothing
	 */
	public void setStack(SrpnStack stack){
		srpnStack = stack;
	}
	
	/**
	 * This method adds two integers together and pushes the sum onto the stack
	 * @param integerOne The first integer the method performs on
	 * @param integerTwo The second integer the method performs on
	 * @return Nothing
	 */
	public void add(int integerOne, int integerTwo){
		int sum = integerTwo + integerOne;
		srpnStack.pushInt(sum);
	}

	/**
	 * This method takes one integer away from the other and pushes the result onto the stack
	 * @param integerOne The first integer the method performs on
	 * @param integerTwo The second integer the method performs on
	 * @return Nothing
	 */
	public void subtract(int integerOne, int integerTwo){
		int subtract = integerTwo - integerOne;
		srpnStack.pushInt(subtract);	
	}

	/**
	 * Method used to multiply two integers together, and push the calculated product to the stack.
	 * @param integerOne The first integer the method performs on
	 * @param integerTwo The second integer the method performs on
	 * @return Nothing
	 */
	public void multiply(int integerOne, int integerTwo){
		int product = integerTwo * integerOne;
		srpnStack.pushInt(product);
	}

	/**
	 * Method used to raise one integer to the power of another, and push the result to the stack.
	 * @param integerOne the base
	 * @param integerTwo the exponent
	 * @return Nothing
	 */
	public void exponent(int integerOne, int integerTwo){
		int power = (int) Math.pow(integerTwo, integerOne);
		srpnStack.pushInt(power);
	}

	/**
	 * Method used to divide one integer by the other, and push the result to the stack.
	 * @param integerOne the divisor
	 * @param integerTwo the dividend
	 * @return Nothing
	 */
	public void divide(int integerOne, int integerTwo){
		int quotient = integerTwo / integerOne;
		srpnStack.pushInt(quotient);
	}	

	/**
	 * Method used to retrieve the remainder from one value when it divides another continuously
	 * The result is pushed onto the stack.
	 * @param integerOne the divisor
	 * @param integerTwo the dividend
	 * @return Nothing
	 */
	public void modulus(int integerOne, int integerTwo){
		int modulus = integerTwo % integerOne;
		srpnStack.pushInt(modulus);
	}
}
