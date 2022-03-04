package jason.fuzzer;

import jason.JasonException;

public class FuzzerTarget {
    public static void fuzzerTestOneInput(byte[] input) throws JasonException {
		
		System.out.println("~~~~~~~ Calling Fuzzer");
		RunMASCustomized.main(new String[] {"blocksworld.mas2j"});
		System.out.println("~~~~~~~ Fuzzer returned, preparing next instance");
    }
}
