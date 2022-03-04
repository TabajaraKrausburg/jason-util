package jason.fuzzer;

import jason.JasonException;

public class Main {
    public static void main(String[] args) {
		
		System.out.println("~~~~~~~ Calling Fuzzer");
		try {
			RunMASCustomized.main(new String[] {"blocksworld.mas2j"});
		} catch (JasonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("~~~~~~~ Fuzzer returned, preparing next instance");
    }
}
