package es.deusto.prog3.extra.utils;

import java.util.Random;

public class Utils {
	
	public static int randomInt(int min, int max) {
		Random rand = new Random();
		
		return rand.nextInt((max - min) + 1) + min;
	}
	
	public static int randomInt(int max) {
		return randomInt(0, max);
	}
	
	public static int max(int a, int b) {
		if(a > b)
			return a;
		else
			return b;
	}

}
