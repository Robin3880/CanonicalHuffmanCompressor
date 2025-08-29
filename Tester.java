import java.io.*;
import java.util.HashMap;
class Tester {
	public static void main(String[] args) {
		if (args.length == 1) {
			String file = args[0];
			try {
				BufferedReader br = new BufferedReader(new FileReader(args[0]));
				/*read in ascii characters,  get frequency of each  */
			
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
			
				/*use huffman encoding to get binary number for each  */
				System.out.println(frequencies);
				
			} catch (IOException e) {
				System.out.println(e);
			}
        } else if (args.length == 0){
            System.out.println("Error: No file to encode provided in arguments");
        } else {
			System.out.println("Error: too many arguments given. (arguments expected: 1)");
		}
	}
	
}