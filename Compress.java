import java.util.HashMap;
import java.io.*;

class Compress {
	public static void main(String[] args) {
		if (args.length == 1) {
			// create huffman table 
			HuffmanTableBuilder htb = new HuffmanTableBuilder();
			HashMap<Character, String> codes = htb.encode(args[0]);
			System.out.println(codes);
			
			//create compressed file 
			try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(args[0] + ".huf"))) {
				// store total number of symbols in first byte
				dos.writeInt(codes.size());
				
				//store huffman table with symbols and their length
				for (HashMap.Entry<Character, String> entry : codes.entrySet()) {
					dos.writeChar(entry.getKey());       
					dos.writeByte(entry.getValue().length()); 
				}
				
				// Placeholder byte which will store how much of last byte is padded 
				dos.writeByte(0); 

				//write encoded data using huffman table values byte by byte
				BufferedReader br = new BufferedReader(new FileReader(args[0]));
				int c = br.read();
				String buffer = "";
				while (c != -1) {
					char character = (char) c;
					buffer += codes.get(character);
					if (buffer.length() > 7) {
						int value = Integer.parseInt(buffer.substring(0,8), 2);
						dos.write(value);
						buffer = buffer.substring(8);
					}
					c = br.read();
				}
				br.close();
				//deal with remaining buffer if its not 8 bits
				int bufferLength = buffer.length();
				for (int i=0;i<(8-bufferLength);i++) {
					buffer+="0";
				}
				dos.write(Integer.parseInt(buffer, 2));
				
				//go back and change plaholder byte to be bufferLength
				RandomAccessFile raf = new RandomAccessFile(args[0] + ".huf", "rw");
				raf.seek(4 + codes.size() * 3); //skip to bufferlenght byte position
				raf.writeByte(bufferLength); 
				
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