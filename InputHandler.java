/**
 * The class that handles all of the input processing, and error checking for SRPN.
 * 
 * @author Manvir Ubhi
 * @version 1.0
 * @release 18/11/2017
 * @See top of SRPN.java for functionality which is processed here
 */

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.lang.Math;

public class InputHandler {
	SrpnStack stack = new SrpnStack();
	Operators o = new Operators();
	// Creates an array list that will hold the numbers used in the 'r' command
	ArrayList<Integer> randomNumberList = new ArrayList<Integer>();
	// Stores the current index for the 'r' command. See 'processNonNumerical' method
	private int ListIndex = 0;

	/**
	 * Constructor which adds all of the values to the array list needed for the 'r' command.
	 *
	 */
	public InputHandler(){
		showRandomNumber();
	}

	/**
	 * Method used to split up user input on one line into separate chars to allow multiple inputs on one line
	 * @param s the user input
	 */
	public void processCommand(String s) {  
		// Split user input by whitespace
		StringTokenizer st = new StringTokenizer(s, " ");
		boolean checkComment = false;
		// Loops through to the end of the inputted string, and while there are no comments
		while(st.hasMoreTokens() && !checkComment){
			// Holds the current token
			String nextElement = st.nextToken();
			checkComment = processInput(nextElement);
		}
	}

	/** 
	 * Method used to process the token passed from 'processCommand' method
	 * Checks for commenting
	 * @param nextElement
	 * 			the current part of the user input, which has been split by a space. See 'processCommand' method.
	 * 
	 * @return result of comment check
	 */
	private boolean processInput(String toProcess){
		boolean isComment = false;
		String currentOperand = "";
		int j = toProcess.length();
		// Loops through each character in the element
		for(int i = 0; i < j; i++){
			// Gets the current character in the token
			char currentChar = toProcess.charAt(i);
			char nextChar = ' ';

			// If not last character in token, get next character - this prevents uknown index errors
			if(i != j - 1){
				nextChar = toProcess.charAt(i + 1);
			}

			// If character is '#', disregard any input after it - move onto the next token
			if(currentChar == '#'){
				isComment = true;
				break;
			} 

			// If input is an operand, add it to the current string
			else if(isOperand(currentChar)){
				currentOperand += currentChar;
				
				/* If last character in string, or the next character is not an operand
				 * process the input, as this indicates end of operand.
				 */
				if(i == j - 1 || !(isOperand(nextChar))){
					processOperand(currentOperand);
					// Resets the string/operand for next iteration
					currentOperand = "";
				}
			}

			// Checks if the current character is an accepted non-numeric input
			else if(isNonNumerical(currentChar)){
				
				// Checks if negative numbers exist in user input, '-0' is not allowed

				if(isOperand(nextChar) && currentChar == '-' && nextChar != 0){
					currentOperand += currentChar;
				}
				else{
					processNonNumerical(currentChar);
				}
			}

			// If none of above criteria are met, input is an unknown character
			else{
				System.err.println("Unrecognised operator or operand \"" + currentChar + "\".");
			}

		}
		return isComment;
	}
	
	/** 
	 * Method which checks if input is an accepted non-numerical
	 * @param toCheck the character which is currently being processed. See 'processInput' method.
	 * @return whether or not the character is numeric or not
	 */
	private boolean isNonNumerical(char toCheck){
		if(toCheck == '+' || toCheck == '-' || toCheck == '*' || toCheck == '/' || toCheck == '%' || toCheck == '^' || toCheck == '=' || toCheck == 'd' || toCheck == 'r'){
			return true;
		}
		else{
			return false;
		}
	}
	
	/** 
	 * Checks if the data being passed is an octal, passes the saturation test and pushes it to the stack.
	 * @param operand the entire operand to be processed. See 'processToken' method.
	 */
	private void processOperand(String toCheck){
		// Checks if the operand is octal
		if(!checkOctal(toCheck)){
			// Checks if oprand passes saturation test
			if(saturationCheck(toCheck)){
				stack.push(toCheck);	
			}
		}
		// Calculates octal value of operand
		else{
			String octValue = getOctValue(toCheck);
			calculateOctal(octValue);
		}
	}
	
	/** 
	 * Method which processes input which is not a numerical value.
	 * 
	 * @param nonNumericInput
	 * 		the character which is non numeric
	 */
	private void processNonNumerical(char nonNumericInput){
		//Calls the peek function from the stack, and checks the values above the = sign
		//If there are none, then call the emptystack function
		if(nonNumericInput == '='){
			System.out.println(stack.peek());
		}
		else if(nonNumericInput == 'd'){
			stack.printStack();
		}
		
		/*
		 *  If r is entered, prints the first item of the 'randomNumberList' array list
		 *  and increment 'ListIndex' so if 'r' is entered again
		 *  the second item is printed etc.
		 */
		else if(nonNumericInput == 'r'){
			stack.pushInt(randomNumberList.get(ListIndex));
			ListIndex++;
		}
		else{
			performOperation(nonNumericInput);
		}
	}
	
	/** 
	 * Method which checks if an operand is an octal.
	 * 
	 * @param operand
	 * 		the operand to be checked
	 * 
	 * @return whether or not the operand is an octal
	 */
	private boolean checkOctal(String octalToDecimal){
		// If string starts with '0' or starts with '-0' which would still be an octal value, but negative.
		if(((octalToDecimal.charAt(0) == '0') || (octalToDecimal.charAt(0) == '-' && octalToDecimal.charAt(1) == '0')) && octalToDecimal.length() > 1){
			return true;
		}
		else{
			return false;
		}
	}
	
	/** 
	 * Method which calculates the decimal value of an operand which is an octal, and pushes it to the stack.
	 * 
	 * @param octValue
	 * 		the operand in octal number e.g. '010'
	 */
	private void calculateOctal(String octal){
		boolean signed = false;
		int decimal = 0;
		int exponent = 0;
		// Loops through the octValue from least significant digit to most significant digit
		for(int i = octal.length() - 1; i >= 0; i--, exponent++){
			char currentOctValue = octal.charAt(i);
			if(currentOctValue == '-'){
				signed = true;
				continue;
			}
			// Multiplies the current digit by the value in that digit's 'column', and adds it to the decimal value
			decimal += Character.getNumericValue(currentOctValue) * Math.pow(8, exponent);
		}
		
		// Negates the decimal value if a minus sign was found
		if(signed){
			decimal *= -1;
		}
		if(saturationCheck(Integer.toString(decimal))){
			stack.pushInt(decimal);
		}

	}
	
	/** 
	 * Method which extracts the octal value from the raw operand. 
	 * This is needed due to the fact SRPN disregards any input after 
	 * an '8' or a '9' is encountered, and uses the preceeding numbers 
	 * 
	 * @param octOperand
	 * 		the operand in 'raw' form. See 'processOperand' method.
	 * 
	 * @return the octal value of the given operand
	 */
	private String getOctValue(String octOperand){
		String octValue = "";
		for(int i = 0; i < octOperand.length(); i++){
			char currentCharacter = octOperand.charAt(i);
			if(currentCharacter == '8' || currentCharacter == '9'){
				break;
			}
			else if(currentCharacter != '0' || octValue != ""){
				octValue += octOperand.charAt(i);

			}
			else{
				continue;
			}
		}
		return octValue;
	}

	/** 
	 * Method which checks if a given character is an operand or not.
	 * 
	 * @param currentChar
	 * 		the character to be tested
	 * 
	 * @return whether or not the given character is an operand.
	 */
	private boolean isOperand(char currentChar){
		try{
			Integer.parseInt(Character.toString(currentChar));
			return true;
		}
		catch(NumberFormatException e){
			return false;
		}
	}    
	
	/** 
	 * Method which performs the given operation on the first two operands on the stack. 
	 * Also checks if the operation result is over or under saturated.
	 * 
	 * @param operation
	 * 		the operation which is to be performed on the operands. See 'processToken' method.
	 */
	private void performOperation(char operator){
		// Sets the stack in Operator class
		o.setStack(stack);
		// Checks for stack underflow
		if(willUnderflow()){
			int integerOne = stack.popInt();
			int integerTwo = stack.popInt();
			long result = 0;
			
			// Checks the operation entered, and pushes result to stack if it passes saturation test
			switch(operator){
			case '+':
				result = ((long) integerTwo) + integerOne;
				if(operationSaturationCheck(result)){
					o.add(integerOne, integerTwo);
				}
				break;
			case '-':
				result = ((long) integerTwo) - integerOne;
				if(operationSaturationCheck(result)){
					o.subtract(integerOne, integerTwo);
				}
				break;
			case '*':
				result = ((long) integerTwo) * integerOne;
				if(operationSaturationCheck(result)){
					o.multiply(integerOne, integerTwo);
				}
				break;
			case '/':
				// Checks for divide by zero error
				if(isExponentZero(integerOne, integerTwo)){
					result = ((long) integerTwo) / integerOne;
					if(operationSaturationCheck(result)){
						o.divide(integerOne, integerTwo);
					}
				}
				break;
			case '^':
				// Checks for negative power
				if(isNegativePower(integerOne, integerTwo)){
					result += (long) Math.pow(integerTwo, integerOne);
					if(operationSaturationCheck(result)){
						o.exponent(integerOne, integerTwo);
					}
				}
				break;
			case '%':
				if(isExponentZero(integerOne, integerTwo)){
					result = ((long) integerTwo) % integerOne;
					if(operationSaturationCheck(result)){
						o.modulus(integerOne, integerTwo);
					}
				}
				break;
			}
		}
	}

	/** 
	 * This method checks if an element is over or under saturated. 
	 * @param toCheck the element to be pushed to the stack
	 * @return whether or not the element was over/under saturated + was in the correct range.
	 */
	private boolean saturationCheck(String toCheck){
		// If element is over 10 digits long, then it is certainly over/under saturated
		if(toCheck.length() > 10){
			if(toCheck.contains("-")){
				stack.push(Integer.toString(Integer.MIN_VALUE));
				return false;
			}
			else{
				stack.push(Integer.toString(Integer.MAX_VALUE));
				return false;
			}
		}
		
		/* If not, it could still be over/under saturated, but will be 
		 * short enough to be stored in a long in order to check.
		 */
		else{
			long isSaturated = Long.parseLong(toCheck);
			if(isSaturated > Integer.MAX_VALUE){
				stack.push(Integer.toString(Integer.MIN_VALUE));
				return false;
			}
			else if(isSaturated < Integer.MIN_VALUE){
				stack.push(Integer.toString(Integer.MAX_VALUE));
				return false;
			}
		}
		return true;
	}

	//Checks if a number (in this case, integerTwo) is being divided by zero.
	private boolean isExponentZero(int integerOne, int integerTwo){
		//As numbers can't divide by zero (no matter the magnitude of the numerator),
		//An error message is printed rather than terminating the program
		if(integerOne != 0){
			return true;
		}
		else{
			stack.pushInt(integerTwo);
			stack.pushInt(integerOne);
			System.err.println("Divide by 0.");
			return false;
		}
	}

	/**
	 * 
	 * @param integerOne The exponent to check
	 * @param integerTwo The base which is not checked
	 * @return Whether or not a negative power exists in the input
	 */
	
	private boolean isNegativePower(int integerOne, int integerTwo){
		if(integerOne >= 0){
			return true;
		}
		else{
			stack.pushInt(integerTwo);
			stack.pushInt(integerOne);
			System.err.println("Negative power.");
			return false;
		}
	}

	/**
	 * This method checks the size of the stack
	 * If it's less than 2, then it is a stack underflow
	 * @return whether or not the stack is too small
	 */
	private boolean willUnderflow(){
		if(stack.size() <= 1){
			System.err.println("Stack underflow.");
			return false;
		}
		else{
			return true;
		}
	}

	/** 
	 * Method which checks if the result of an operation is under/over saturated.
	 * @param operationResult
	 * 		the result of the operation. 'See processOperation' method.
	 * 
	 * @return whether or not the result was under/over saturated or not
	 */
	private boolean operationSaturationCheck(long toCheck){
		// If the operand exceeds the max integer value, push the max value to the stack to prevent rollover
		if(toCheck > Integer.MAX_VALUE){
			stack.pushInt(Integer.MAX_VALUE);
			return false;
		}
		// Same as above, but for minimum value
		else if(toCheck < Integer.MIN_VALUE){
			stack.pushInt(Integer.MIN_VALUE);
			return false;
		}
		else{
			return true;
		}
	}
	
	//All the random numbers that are printed via the "r" case
	/**
	 * This method adds all of the random numbers to the list, which is initialised above
	 * These numbers are
	 * 
	 */
	private void showRandomNumber(){
		randomNumberList.add(1804289383);
		randomNumberList.add(846930886);
		randomNumberList.add(1681692777);
		randomNumberList.add(1714636915);
		randomNumberList.add(1957747793);
		randomNumberList.add(424238335);
		randomNumberList.add(719885386);
		randomNumberList.add(1649760492);
		randomNumberList.add(596516649);
		randomNumberList.add(1189641421);
		randomNumberList.add(1025202362);
		randomNumberList.add(1350490027);
		randomNumberList.add(783368690);
		randomNumberList.add(1102520059);
		randomNumberList.add(2044897763);
		randomNumberList.add(1967513926);
		randomNumberList.add(1365180540);
		randomNumberList.add(1540383426);
		randomNumberList.add(304089172);
		randomNumberList.add(1303455736);
		randomNumberList.add(35005211);
		randomNumberList.add(521595368);
		randomNumberList.add(294702567);
		randomNumberList.add(1726956429);
		randomNumberList.add(336465782);
		randomNumberList.add(861021530);
		randomNumberList.add(278722862);
		randomNumberList.add(233665123);
		randomNumberList.add(2145174067);
		randomNumberList.add(468703135);
		randomNumberList.add(1101513929);
		randomNumberList.add(1801979802);
		randomNumberList.add(1315634022);
		randomNumberList.add(635723058);
		randomNumberList.add(1369133069);
		randomNumberList.add(1125898167);
		randomNumberList.add(1059961393);
		randomNumberList.add(2089018456);
		randomNumberList.add(628175011);
		randomNumberList.add(1656478042);
		randomNumberList.add(1131176229);
		randomNumberList.add(1653377373);
		randomNumberList.add(859484421);
		randomNumberList.add(1914544919);
		randomNumberList.add(608413784);
		randomNumberList.add(756898537);
		randomNumberList.add(1734575198);
		randomNumberList.add(1973594324);
		randomNumberList.add(149798315);
		randomNumberList.add(2038664370);
		randomNumberList.add(1129566413);
		randomNumberList.add(184803526);
		randomNumberList.add(412776091);
		randomNumberList.add(1424268980);
		randomNumberList.add(1911759956);
		randomNumberList.add(749241873);
		randomNumberList.add(137806862);
		randomNumberList.add(42999170);
		randomNumberList.add(982906996);
		randomNumberList.add(135497281);
		randomNumberList.add(511702305);
		randomNumberList.add(2084420925);
		randomNumberList.add(1937477084);
		randomNumberList.add(1827336327);
		randomNumberList.add(572660336);
		randomNumberList.add(1159126505);
		randomNumberList.add(805750846);
		randomNumberList.add(1632621729);
		randomNumberList.add(1100661313);
		randomNumberList.add(1433925857);
		randomNumberList.add(1141616124);
		randomNumberList.add(84353895);
		randomNumberList.add(939819582);
		randomNumberList.add(2001100545);
		randomNumberList.add(1998898814);
		randomNumberList.add(1548233367);
		randomNumberList.add(610515434);
		randomNumberList.add(1585990364);
		randomNumberList.add(1374344043);
		randomNumberList.add(760313750);
		randomNumberList.add(1477171087);
		randomNumberList.add(356426808);
		randomNumberList.add(945117276);
		randomNumberList.add(1889947178);
		randomNumberList.add(1780695788);
		randomNumberList.add(709393584);
		randomNumberList.add(491705403);
		randomNumberList.add(1918502651);
		randomNumberList.add(752392754);
		randomNumberList.add(1474612399);
		randomNumberList.add(2053999932);
		randomNumberList.add(1264095060);
		randomNumberList.add(1411549676);
		randomNumberList.add(1843993368);
		randomNumberList.add(943947739);
		randomNumberList.add(1984210012);
		randomNumberList.add(855636226);
		randomNumberList.add(1749698586);
		randomNumberList.add(1469348094);
		randomNumberList.add(1956297539);
	}
}
