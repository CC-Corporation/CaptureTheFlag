package de.flag.utils;

import java.util.Arrays;

public class ArrayUtils {

	public static int[] arrayDeleteValues(int[] array, int valuesToDelete) {
		
		int a = array.length - valuesToDelete;
		int[] array2 = new int[a]; 
		
		for(int position = 0; position < array.length - valuesToDelete; position++) {
			
			array2[position] = array[position];
		
		}
		
		return array2;
		
	}
	
	public static String[] arrayDeleteValues(String[] array, int valuesToDelete) {
		
		int a = array.length - valuesToDelete;
		String[] array2 = new String[a]; 
		
		for(int position = 0; position < array.length - valuesToDelete; position++) {
			
			array2[position] = array[position];
		
		}
		
		return array2;
		
	}
	
	public static double[] arrayDeleteValues(double[] array, int valuesToDelete) {
		
		int a = array.length - valuesToDelete;
		double[] array2 = new double[a]; 
		
		for(int position = 0; position < array.length - valuesToDelete; position++) {
			
			array2[position] = array[position];
		
		}
		
		return array2;
		
	}
	
	public static int arrayFindValue(int[] arr) {
		if(arr != null) {
			int stelle = 0;
	        int anzahl = 1;
	        int anzahl_final = 0;
	        Arrays.sort(arr);
	 
	        for (int i = 0; i < arr.length - 1; ++i) {
	 
	            if (arr[i] == arr[i + 1]) {
	 
	                ++anzahl;
	            }
	 
	            if (anzahl_final < anzahl) {
	                anzahl_final = anzahl;
	                stelle = i;
	 
	            }
	            if (arr[i] != arr[i + 1]) {
	                anzahl = 1;
	            }
	 
	        }
	 

	        return arr[stelle];
		}else {
			
			return 0;
		}
 
    
	}
}
