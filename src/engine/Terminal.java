package engine;

import java.util.Scanner;

public class Terminal {

	public Terminal() {

	}

	public static void println(Object s) {
		printText(s.toString());
		System.out.println();
		
	}

	public static void print(Object s) {
		printText(s.toString());
	}

	public static String readln() {
		return new Scanner(System.in).nextLine();
	}
	public static void printText(String s) {
		s = s.replace("(", "∆").replace(")", "∆");
		String[] strs = s.split("∆");
		System.out.print(strs[0]);
		for(int i = 1; i < strs.length; i += 2) {
			int n = Integer.parseInt(strs[i]);
			try {
			Thread.sleep(n);
			} catch(InterruptedException e) {}
			
			try {
				System.out.print(strs[i + 1]);
			} catch(Exception e) {}
		}
	}
}
