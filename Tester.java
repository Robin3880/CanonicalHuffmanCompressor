import java.util.HashMap;
class Tester {
	public static void main(String[] args) {
		if (args.length == 1) {
			Encoder e = new Encoder();
			HashMap<Character, String> codes = e.encode(args[0]);
			System.out.println(codes);
        } else if (args.length == 0){
            System.out.println("Error: No file to encode provided in arguments");
        } else {
			System.out.println("Error: too many arguments given. (arguments expected: 1)");
		}
	}
	
}