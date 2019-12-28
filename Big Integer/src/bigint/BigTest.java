package bigint;

import java.io.IOException;
import java.util.Scanner;

public class BigTest {

	static Scanner sc;
	
	public static void parse() 
	throws IOException {
		System.out.print("\tEnter integer => ");
		String integer = sc.nextLine();
		try {
			BigInteger bigInteger = BigInteger.parse(integer);
			System.out.println("\t\tValue = " + bigInteger);
			System.out.println("\t\tNegative = " + bigInteger.negative);
			System.out.println("\t\tNumDigits = " + bigInteger.numDigits);
		} catch (IllegalArgumentException e) {
			System.out.println("\t\tIncorrect Format");
		}
	}
	
	public static void add() 
	throws IOException {
		/*BigInteger result = null;
		boolean fail = false;
		for(int i = -9999; i <= 9999; i++) {
			for(int j = -9999; j<=9999; j++) {
				
				String first = Integer.toString(i);
				String second = Integer.toString(j);
				BigInteger firstBigInteger = BigInteger.parse(first);
				BigInteger secondBigInteger = BigInteger.parse(second);
		
				result = BigInteger.add(firstBigInteger,secondBigInteger);
				int sum = i+j;
				if(Integer.parseInt(result.toString()) != sum) {
					System.out.println("First Integer: " + firstBigInteger);
					System.out.println("Second Integer: " + secondBigInteger);
					System.out.println("Result: " + result);
					System.out.println("Actual: " + sum);
					break;
				}
			}
			if(fail)break;
			System.out.println("i = " +i);
			if(i == 9999) System.out.println("PASS :D");
		}*/
		
		System.out.print("\tEnter first integer => ");
		String integer = sc.nextLine();
		BigInteger firstBigInteger = BigInteger.parse(integer);
		
		System.out.print("\tEnter second integer => ");
		integer = sc.nextLine();
		BigInteger secondBigInteger = BigInteger.parse(integer);
		BigInteger result = BigInteger.add(firstBigInteger,secondBigInteger);
		
		System.out.println("\t\tSum: " + result);
	}
	
	public static void multiply() 
	throws IOException {
		System.out.print("\tEnter first integer => ");
		String integer = sc.nextLine();
		BigInteger firstBigInteger = BigInteger.parse(integer);
		
		System.out.print("\tEnter second integer => ");
		integer = sc.nextLine();
		BigInteger secondBigInteger = BigInteger.parse(integer);
		
		BigInteger result = BigInteger.multiply(firstBigInteger,secondBigInteger);
		System.out.println("\t\tProduct: " + result);
		/*BigInteger result = null;
		boolean fail = false;
		for(int i = -9999; i <= 9999; i++) {
			for(int j = -9999; j<=9999; j++) {
				
				String first = Integer.toString(i);
				String second = Integer.toString(j);
				BigInteger firstBigInteger = BigInteger.parse(first);
				BigInteger secondBigInteger = BigInteger.parse(second);
		
				result = BigInteger.add(firstBigInteger,secondBigInteger);
				int sum = i+j;
				if(Integer.parseInt(result.toString()) != sum) {
					System.out.println("First Integer: " + firstBigInteger);
					System.out.println("Second Integer: " + secondBigInteger);
					System.out.println("Your Result: " + result);
					System.out.println("Actual: " + sum);
					fail = true;
					break;
				}
			}
			if(fail)break;
			System.out.println("i = " +i);
			if(i == 9999) System.out.println("PASS :D");
		}*/
		
	}
	
	public static void main(String[] args) 
	throws IOException {
		
		// TODO Auto-generated method stub
		sc = new Scanner(System.in);
		char choice;
		while ((choice = getChoice()) != 'q') {
			switch (choice) {
				case 'p' : parse(); break;
				case 'a' : add(); break;
				case 'm' : multiply(); break;
				default: System.out.println("Incorrect choice"); 
			}
		}
	}

	private static char getChoice() {
		System.out.print("\n(p)arse, (a)dd, (m)ultiply, or (q)uit? => ");
		String in = sc.nextLine();
		char choice;
		if (in == null || in.length() == 0) {
			choice = ' ';
		} else {
			choice = in.toLowerCase().charAt(0);
		}
		return choice;
	}

}
