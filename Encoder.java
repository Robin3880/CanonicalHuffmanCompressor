import java.io.*;
import java.util.HashMap;
class Encoder {
	public HashMap<Character, String> encode(String file) {
		/*read in ascii characters,  get frequency of each  */
		HashMap<Character, Integer> frequencies = getFrequencies(file);
		/*use huffman encoding to get binary number for each  */
		HashMap<Character, String> HufmannTable = generateHuffmanTable(frequencies);
		
		
		return "test";
	}
	
	
	private HashMap<Character, Integer> getFrequencies(String file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			HashMap<Character, Integer> frequencies = new HashMap<>();
			String line = br.readLine();
			while (line != null) {
				for (int i = 0; i < line.length(); i++) {
					char character = line.charAt(i);
					if (frequencies.containsKey(character)) {
						frequencies.put(character, frequencies.get(character)+1);
					} else {
					frequencies.put(character, 1);
					}
				}	
				line = br.readLine();
			}
			return frequencies;
		} catch (IOException e) {
			System.out.println(e);
			return new HashMap<>();
		}
	}
	
	private HashMap<Character, String> generateHuffmanTable(HashMap<Character, Integer> frequencies) {
		/*  
		create object huffman tree?
		make set hasNode
		make set noNodes (frequencies.getkeys())
		find lowest two frequencies(iterate through noNodes and node frequencies and keep track of two lowest?) 
		create node and add to noNodes set
		
		
		*/
	}
}