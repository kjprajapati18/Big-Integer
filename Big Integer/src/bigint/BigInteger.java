package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	public static boolean isNumeric(char a) {
		if(a == '0' ||
				a == '1' ||
				a == '2' ||
				a == '3' ||
				a == '4' ||
				a == '5' ||
				a == '6' ||
				a == '7' ||
				a == '8' ||
				a == '9') {
			return true;
		}
		return false;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		
		//Deal with the empty case. Deal with easy generic examples
		//Clean the trailing and leading spaces
		String clean = integer.trim();
		if(clean.equals("") || clean.equals("+") || clean.equals("-")) throw new IllegalArgumentException();
		
		char a = ' ';
		
		//Check that the input is acceptable to work with
		for(int i = 0; i < clean.length(); i++) {
			a = clean.charAt(i);
			if(!isNumeric(a)) {
				if(i ==0 && (a=='-' || a=='+')) {
					continue;
				} else {
					throw new IllegalArgumentException();
				}
			}
		}
		
		//Since we know at this point that the input is acceptable, we will start working with it
		//The input at this point is in the form ~########## where ~ is either a # or + or - (at minimum 1 #)
		BigInteger bi = new BigInteger();
		String doubleClean = clean;
		
		//Figure out the sign. Then clean up the sign and any leading zeroes
		for(int i = 0; i < clean.length()-1; i++) {
			a = clean.charAt(i);
			if(a == '-' || a == '+') {
				if(a == '-') {
					bi.negative = true;
				}
				doubleClean = clean.substring(i+1, clean.length());
				continue;
			}
			if(a != '0') {
				break;
			}
			doubleClean = clean.substring(i+1, clean.length());
			
		}
		
		//If the number was some form of 0, make sure negative is false and return empty list
		if(doubleClean.equals("0")) {
			bi.front = null;
			bi.negative = false;
			return bi;
		}
		
		//DoubleClean is now in the form ####... where the leading digit is non-zero
		//To convert number chars to ints, subtract 48
		DigitNode ptr = new DigitNode(doubleClean.charAt(doubleClean.length()-1)-48, null);
		bi.front = ptr;
		bi.numDigits++;
		for(int i = doubleClean.length()-2; i>= 0; i--) {
			ptr.next = new DigitNode(doubleClean.charAt(i)-48, null);
			bi.numDigits++;
			ptr = ptr.next;
		}
		//We've looped through the remaining numbers and made the nodes, so just return
		return bi;
	}
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		
		/* IMPLEMENT THIS METHOD */
		// Since we're given 2 BigInts, they are assumed to be in proper format
		BigInteger result = new BigInteger();
		
		//If any (or both) lists are empty, then the result is obvious
		if(first.front == null && second.front == null) {
			return result;
		}
		if(first.front == null) {
			return second;
		}
		if(second.front == null) {
			return first;
		}

		//If we reach this point, we know there is at least 1 digit in each list
		//Initialize pointers for the inputs and the output and storage variables
		DigitNode ptr1 = first.front;
		DigitNode ptr2 = second.front;
		DigitNode ptrR;
		int curData;
		int carry =0;
		
		//If both signs are the same, then its effectively normal addition. We can then attach the sign.
		if(first.negative == second.negative) {
			result.negative = first.negative;
			
			curData = ptr1.digit + ptr2.digit; //Add
			if(curData > 9) { //Fix the digit and the carry
				curData-=10;
				carry++;
			}
			result.front = new DigitNode(curData,null); //Initilize the front & set pointers
			result.numDigits++;
			ptrR = result.front;
			ptr1 = ptr1.next;
			ptr2 = ptr2.next;
			
			while(ptr1 != null && ptr2 != null) {//Do the same thing over until we reach the end of one list
				curData = ptr1.digit + ptr2.digit + carry;
				carry = 0; //Set carry to 0 after using it
				if(curData > 9) {
					curData -=10;
					carry++;
				}
				ptrR.next = new DigitNode(curData, null);
				result.numDigits++;
				ptrR = ptrR.next;
				ptr1 = ptr1.next;
				ptr2 = ptr2.next;
			}
			
			//If we reach the end of the previous loop, then we reached the end of at least one of the lists
			//At most one of the next two while loops will occur depending on what finished
			//Perform the same operations however, using only 1 list and a carry, in case the trailing digits are 9's
			while(ptr1 != null) {
				curData = ptr1.digit + carry;
				carry = 0;
				if(curData > 9) {
					curData -=10;
					carry++;
				}
				ptrR.next = new DigitNode(curData, null);
				result.numDigits++;
				ptrR = ptrR.next;
				ptr1 = ptr1.next;
			}
			while(ptr2 != null) {
				curData = ptr2.digit + carry;
				carry = 0;
				if(curData > 9) {
					curData -=10;
					carry++;
				}
				ptrR.next = new DigitNode(curData, null);
				result.numDigits++;
				ptrR = ptrR.next;
				ptr2 = ptr2.next;
			}
			
			//In the event that both linked lists end, but there is still a carry, make the carry a new node
			//Example: 50 + 50 = 100
			if(carry != 0) {
				ptrR.next = new DigitNode(carry, null);
				result.numDigits++;
			}

			return result;
			
		}
		
		//If we reach this portion of the code, then the signs do not match up
		//First find the number with the largest magnitude.
		BigInteger big = null, small = null;
		if(first.numDigits > second.numDigits) {
			big = first;
			small = second;
		} else if(second.numDigits > first.numDigits) {
			big = second;
			small = first;
		} else {
			while(ptr1 != null) {
				if(ptr1.digit > ptr2.digit) {
					big = first;
					small = second;
				} else if (ptr2.digit > ptr1.digit) {
					big = second;
					small = first;
				}
				
				ptr1 = ptr1.next;
				ptr2 = ptr2.next;
			}
		}
		
		//Since we don't change big/small if the digits are the same, this case only occurs when |first| = |second|
		//The sum must be 0, so return an empty list
		if(big == null) {
			return result;
		}
		
		//Otherwise, set up the pointers and perform the subtraction operations on each digit(subtracting big from small)
		//The sign of the result should be the sign of big
		ptr1 = big.front;
		ptr2 = small.front;
		result.negative = big.negative;
		
		curData = ptr1.digit - ptr2.digit;
		if(curData < 0) { //Use carry as a borrow
			curData+=10;
			carry++;
		}
		
		//Initialize front since we already know there's at least 1 digit
		result.front = new DigitNode(curData,null);
		result.numDigits++;
		ptrR = result.front;
		ptr1 = ptr1.next;
		ptr2 = ptr2.next;
		
		//Loop (same concept as adding same signs, but this time with subtraction)
		while(ptr1 != null && ptr2 != null) {
			curData = ptr1.digit - ptr2.digit - carry;
			carry = 0;
			if(curData < 0) {
				curData +=10;
				carry++;
			}
			ptrR.next = new DigitNode(curData, null);
			result.numDigits++;
			ptrR = ptrR.next;
			ptr1 = ptr1.next;
			ptr2 = ptr2.next;
		}
		
		while(ptr1 != null) {
			curData = ptr1.digit - carry;
			carry = 0;
			if(curData < 0) {
				curData +=10;
				carry++;
			}
			ptrR.next = new DigitNode(curData, null);
			result.numDigits++;
			ptrR = ptrR.next;
			ptr1 = ptr1.next;
		}
		while(ptr2 != null) {
			//This while loop is actually unreachable because ptr1 refers to the list of larger magnitude
			//Since we traverse both lists at the same rate, ptr2 will reach null before or at the same time as ptr1
			//For the sake of consistency with adding 2 same-signed lists, I have kept this code
			curData = ptr2.digit - carry;
			carry = 0;
			if(curData < 0) {
				curData +=10;
				carry++;
			}
			ptrR.next = new DigitNode(curData, null);
			result.numDigits++;
			ptrR = ptrR.next;
			ptr2 = ptr2.next;
		}
		
		//In the case where the resulting difference has less digits than both linked lists
		//Reparse the BigInteger to get rid of the leading 0's
		//Example: 1000 - 999 = 0001 at this point. However, we want it to be 1
		result = BigInteger.parse(result.toString());
		return result;
	}
	
	
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		
		/* IMPLEMENT THIS METHOD */
		//Like add, since we are taking 2 already made lists, the format is already assumed
		//We can initialize a new LinkedList
		BigInteger sum = new BigInteger();
		if(first.front == null || second.front == null) return sum; //If either list is 0, return an empty list
		
		//Create a BigInteger to hold the product at each step of the loop iterations
		BigInteger digitProduct = new BigInteger();
		
		DigitNode ptr1 = first.front, ptr2 = second.front; //Initialize pointers for the inputs
		DigitNode ptrDP = null; //Initialize pointer for the digitProduct list
		
		int product = 0, carry = 0, multiplier = 0; //Initialize the basic storages
		
		while(ptr1 != null) {
			for(int i = 0; i < multiplier; i++) {
				//add a 0 (which is multiplying by 10) for every iteration of the loop that has already been completed
				if(digitProduct.front == null) {
					digitProduct.front = new DigitNode(0,null);
					ptrDP = digitProduct.front;
				} else {
					ptrDP.next = new DigitNode(0,null);
					ptrDP = ptrDP.next;
				}
			}
			while(ptr2 != null) { //multiply 1 digit of first by all of second, add up all of the products
				
				product = ptr1.digit * ptr2.digit + carry;
				carry = product/10;
				product %=10;
				
				if(digitProduct.front == null) {
					digitProduct.front = new DigitNode(product, null);
					ptrDP = digitProduct.front;
				} else {
					ptrDP.next = new DigitNode(product, null);
					ptrDP = ptrDP.next;
				}
				ptr2 = ptr2.next;
			}
			if(carry != 0) {//If there's still a carry, we have to add it as the next digit
				ptrDP.next = new DigitNode(carry, null);
				carry = 0;
			}
			sum = BigInteger.add(sum, digitProduct); //Add digitProduct (the product for this iteration) to the cumulative sum
			digitProduct = new BigInteger(); //Reset digitProduct
			ptr1 = ptr1.next; //iterate with the next digit
			ptr2 = second.front; //reset the pointer for the second list, since we will go through the whole thing again
			multiplier++; //Increase the multiplier so we know how many 0's to add at the beginning
		}
		
		if(first.negative != second.negative) sum.negative = true; //The only time the product is negative is if the signs are unequal
		return sum;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
}
