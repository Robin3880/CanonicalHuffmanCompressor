import java.io.*;
import java.util.*;
import java.nio.ByteBuffer;
class HuffmanDecoder {
	public void decompress(String filename) {
		try (FileInputStream fis = new FileInputStream(filename)) {
			//find number of symbols in file and contstruct table
			int totalSymbols = getTotalSymbols(filename);
			HashMap<Character, Integer> huffmanTable =  buildHuffmanTable(filename, totalSymbols);
			
			//skip to bufferLength byte
			fis.skip(4 + totalSymbols * 3);
			int bufferLength = fis.read();
			
			//get contents in binarystring form
			int byteValue = fis.read();
			StringBuilder contents = new StringBuilder();
			while (byteValue != -1) {
				contents.append(String.format("%8s", Integer.toBinaryString(byteValue)).replace(' ', '0')); //adds padding back so 00000001 isnt converted to 1
				byteValue = fis.read();
			}
			contents.setLength(contents.length() - bufferLength);//remove buffer
			
			//build codes map using huffmanTable symbols and lenghts to find character codes
			HashMap<String, Character> codes = getCodes(huffmanTable);
			//convert back to ascii
			convert(contents, codes, filename);
			
			
			
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	private Integer getTotalSymbols(String filename) {
		try (FileInputStream fis = new FileInputStream(filename)) {
				byte[] bytes = new byte[4];
				
				int bytesRead = fis.read(bytes);//stores bytes in bytes variable and returns number of bytes to bytesRead
				if (bytesRead != 4) {
					throw new IOException("invalid file: missing data");
				}
				
				ByteBuffer buffer = ByteBuffer.wrap(bytes);
				return buffer.getInt();
		} catch (IOException e) {
				System.out.println(e);
				return null;
			}
	}
	
	private HashMap<Character, Integer> buildHuffmanTable(String filename, int totalSymbols) {
		try (FileInputStream fis = new FileInputStream(filename)) {
			fis.skip(4); //skip totalSymbols integer bytes
			
			//create the huffmanTable that contains every symbol and its bitlength
			HashMap<Character, Integer> huffmanTable = new HashMap<Character, Integer>();
			for (int i=0;i<totalSymbols;i++) {
				byte[] bytes = new byte[2];
				fis.read(bytes);
				ByteBuffer buffer = ByteBuffer.wrap(bytes);
				char c = buffer.getChar(); 
				int length = fis.read();
				huffmanTable.put(c, length);
			}
			return huffmanTable;
		} catch (IOException e) {
				System.out.println(e);
				return null;
			}
	}
	
	private HashMap<String, Character> getCodes(HashMap<Character, Integer> huffmanTable) {
		ArrayList<CharBitLength> characters = new ArrayList<CharBitLength>();
		for (HashMap.Entry<Character, Integer> entry : huffmanTable.entrySet()) {
			//add character to characters and create CharLength object for each
			CharBitLength cbl = new CharBitLength(entry.getKey(), entry.getValue());
			characters.add(cbl);
		}
		//sort it first so that its sorted by length and then alphabetical
		characters.sort(Comparator.comparingInt((CharBitLength c) -> c.length).thenComparing(c -> c.character));
		
		//iterate through sorted array of characters and use canonical huffman coding to find bitstring
		HashMap<String, Character> codes = new  HashMap<String, Character>();
		
		CharBitLength firstChar = characters.get(0);
		String currentCode = "0".repeat(firstChar.length);
		codes.put(currentCode, firstChar.character);
		characters.remove(firstChar);
		for (CharBitLength cbl: characters) {
			//increment the current code by 1 in binary
			int num = Integer.parseInt(currentCode, 2);
			num += 1;
			String binaryString = Integer.toBinaryString(num);
			//add back padded 0s at start of binarystring if removed to have original length
			String padded = String.format("%" + currentCode.length() + "s", binaryString).replace(' ', '0');
			
			//add 1 zero to end for each difference in length
			padded += "0".repeat(cbl.length - currentCode.length());
			currentCode = padded;
			codes.put(currentCode, cbl.character);
		}
		
		return codes;
	}
	
	private void convert(StringBuilder contents, HashMap<String, Character> codes, String filename) {
		File newFile = new File(filename.substring(0,filename.length()-4));
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(newFile))) {
			//iterate through contents and every time one of the bitstring codes is stored in sb write out char it represents and clear sb
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < contents.length(); i++) {
				sb.append(contents.charAt(i));
				if (codes.containsKey(sb.toString())) { 
					bw.write(codes.get(sb.toString()));
					sb.setLength(0); //clear stringbuilder
				}
			}
			
		} catch (IOException e) {
			System.out.println(e);
		}
		
	}

}