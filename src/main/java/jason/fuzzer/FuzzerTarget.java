package jason.fuzzer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import jason.JasonException;

public class FuzzerTarget {
    public static void fuzzerTestOneInput(byte[] input) throws JasonException {
		if (input.length <= 8)
			return;
		System.out.println("length: "+input.length);
		String[] colours = {"green", "blue", "yellow", "red", "purple"};
		int rooms = 5;
		int maxBlocks = 5;
		int packaging = 30;
		int maxEnergy = 100;
		int startEnergy = 5;
		int rechargeEnergy = 10;
		int energyCost = 5;

		// java byte between −128 - 127
		JSONArray jsonColours =  new JSONArray();
		if (Integer.bitCount(input[0]&0b00011111) == 0)
			input[0] = 1;
		System.out.println("byte: "+Integer.toString(input[0],2));
		for (int i=0; i < colours.length; i++)
			if ((input[0] & (1 << i)) != 0)
				jsonColours.put(colours[i]);		
		rooms = Math.max((input[1]&0x7F), 1);
		maxBlocks = Math.max((input[2]&0x7F), 1);
		packaging = Math.max(((input[3]&0x7F)*100)/128,1);
		// maxEnergy = Math.max((input[4]&0x7F),100);
		// startEnergy = new Random((input[5]&0x7F)).nextInt(maxEnergy)+1; 
		// rechargeEnergy = new Random((input[6]&0x7F)).nextInt(maxEnergy)+1; 
		// energyCost = new Random((input[7]&0x7F)).nextInt((int) (maxEnergy*.2)); 
		
		JSONObject instance = new JSONObject();
		instance.put("robots", 1);
		instance.put("colours", jsonColours);
		instance.put("rooms", rooms);
		instance.put("maxBlocks", maxBlocks);
		instance.put("packaging", packaging);
		instance.put("maxEnergy", maxEnergy);
		instance.put("startEnergy", startEnergy);
		instance.put("rechargeEnergy", rechargeEnergy);
		instance.put("energyCost", energyCost);
		System.out.println("Instance: "+instance.toString());

		FileWriter file = null;
		try {
            file = new FileWriter("./envConf/jazzer.json");
            file.write(instance.toString()); 
			System.out.println("written to the file");
        } catch (IOException e) {
            e.printStackTrace();
 
        } finally { 
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }	
		
		System.out.println("~~~~~~~ Calling Fuzzer");
		RunMASCustomized.main(new String[] {"blocksworld.mas2j"});
		System.out.println("~~~~~~~ Fuzzer returned, preparing next instance");
    }
}
