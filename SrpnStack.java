/**
 * The Stack class for SRPN, which handles all of the functionality of the stack.
 * 
 * @author Manvir Ubhi
 * @version 1.0
 * @Since 23/11/2017
 * @See InputHandler.java
 */

import java.util.Stack;

public class SrpnStack{
	Stack<String> stack;

	/**
	 * This constructor initialises a new String stack
	 */
	public SrpnStack(){
		stack = new Stack<String>();
	}

	/**
	 * This method pushes a converted value onto the stack
	 * It also sets the limit of the stack size at 23
	 * If size exceeds 23, then it is a stack overflow
	 * @param input the value added to the stack
	 * @return nothing
	 */
	public void push(String input){
		if(size() < 23){
			stack.push(input);
		}
		else{
			System.err.println("Stack overflow.");
		}
	}

	/**
	 * This method converts an integer value into a string 
	 * This is then passed through to push which supports String arguments
	 * @param push the integer being added to the stack
	 * @return Nothing  
	 */
	public void pushInt(int push){
		String result = Integer.toString(push);
		push(result);
	}

	/**
	 * Method pop an integer from the stack.
	 * 
	 * @return the popped integer.
	 */
	public int popInt(){
		int popped = Integer.parseInt(stack.pop());
		return popped;
	}

	/**
	 * Removes an object from the top of a stack and returns a respective value
	 * @return the popped value
	 */
	public String pop(){
		String popped = stack.pop();
		return popped;
	}


	/**
	 * Looks at an object from the top of the stack without changing its position
	 * @return the value at the top of the stack
	 */
	public String peek(){
		String peeked = stack.peek();
		return peeked;
	}

	/**
	 * Returns the size of the stack in integer form
	 * @return the stack size
	 */
	public int size(){
		int size = stack.size();
		return size;
	}

	/**
	 * This method prints the whole stack (needed only for "d" command), see InputHandler
	 * @return Nothing
	 */
	public void printStack(){
		for(int i = 0; i < size(); i++){
			System.out.println(stack.get(i));
		}	
	}
}
