import java.util.HashMap;
import java.io.File;
import java.nio.ByteBuffer;
import java.io.IOException;
class Decompress {
	public static void main(String[] args) throws IOException {
		if (args.length == 1) {
			//check for correct file format
			if (!args[0].endsWith(".huf")) {
				throw new IOException("invalid file: must be .huf format");
			}
			//check if file that will be created already exists
			File newFile = new File(args[0].substring(0,args[0].length()-4));
			if (newFile.exists()) {
				throw new IOException("file: " +newFile.getName() + " already exists");
			}
			
			//decompress file
			HuffmanDecoder hd = new HuffmanDecoder();
			hd.decompress(args[0]);
				
			
        } else if (args.length == 0){
            System.out.println("Error: No file to compress provided in arguments");
        } else {
			System.out.println("Error: too many arguments given. (arguments expected: 1)");
		}
	}

}