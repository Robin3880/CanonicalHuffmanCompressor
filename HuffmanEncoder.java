import java.io.*;
import java.util.*;
class HuffmanEncoder  {
	public HashMap<Character, String> encode(String file) {
		//read in ascii characters,  get frequency of each  
		HashMap<Character, Integer> frequencies = getFrequencies(file);
		
		// error handling for empty files or files with one character 
		if (frequencies.isEmpty()) {
			return new HashMap<>();
		}
		if (frequencies.size() == 1) {
			HashMap<Character, String> codes = new HashMap<>();
			Character c = frequencies.keySet().iterator().next();
			codes.put(c, "0");
			return codes;
		}
		
		// create huffman tree with the characters and their frequencies 
		ArrayList<Node> HufmannTree = generateHuffmanTree(frequencies);
		
		// trace path of each nodes to get binary code 
		HashMap<Character, String> codes = new HashMap<>();
		for (Node n: HufmannTree) {
			if (n.character != null) {
				String code = "";
				Node currentNode = n;
				while (currentNode.num != null) {
					code = currentNode.num + code;
					currentNode = currentNode.nextNode;
				}
				codes.put(n.character, code);
			}
		}
		return codes;
	}
	
	private HashMap<Character, Integer> getFrequencies(String file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))){
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
	
	private ArrayList<Node> generateHuffmanTree(HashMap<Character, Integer> frequencies) {
		ArrayList<Node> nodes = new ArrayList<>();
		HashSet<Node> activeNodes = new HashSet<>();
		for (Character c: frequencies.keySet()) {
			Node n = new Node(frequencies.get(c), c);
			nodes.add(n);
			activeNodes.add(n);
		}
		nodes.sort(Comparator.comparing(Node::getCharacter));
		
		while (true) { 
			//find two activeNodes with lowest frequencies
			Node node1 = null; 
			Node node2 = null;
			for (Node n: activeNodes) {
				 if (node1 == null || n.frequency < node1.frequency) {
					node2 = node1;  
					node1 = n;    
				} else if (node2 == null || n.frequency < node2.frequency) {
					node2 = n;      
				}
			}
			//create new node with combined frequency
			Node n = new Node(node1.frequency + node2.frequency, null);
			activeNodes.add(n);
			nodes.add(n);
			//update two previous nodes and remove from activeNodes 
			if (node1.frequency == node2.frequency) {
				if (nodes.indexOf(node1) < nodes.indexOf(node2)) {
					node1.num = "0";
					node2.num = "1";
				} else {
					node2.num = "0";
					node1.num = "1";
				}
			} else {
				node1.num = "0";
				node2.num = "1";
			}
			node1.nextNode = n;
			node2.nextNode = n;
			activeNodes.remove(node1);
			activeNodes.remove(node2);
			
			if (activeNodes.size() == 1) {
				break;
			}
		}
		return nodes;
	}
	
	public HashMap<Character, String> generateCanonicalCodes(HashMap<Character, String> codes) {
		ArrayList<CharBitLength> characters = new ArrayList<CharBitLength>();
		for (HashMap.Entry<Character, String> entry : codes.entrySet()) {
			characters.add(new CharBitLength(entry.getKey(), entry.getValue().length()));
		}
		//sort it first so that its sorted by length and then alphabetical
		characters.sort(Comparator.comparingInt((CharBitLength c) -> c.length).thenComparing(c -> c.character));
		
		HashMap<Character, String> canonicalCodes = new HashMap<Character, String>();
		
		//iterate through characters and calculate canonical code
		CharBitLength firstChar = characters.get(0);
		String currentCode = "0".repeat(firstChar.length);
		canonicalCodes.put(firstChar.character, currentCode);
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
			canonicalCodes.put(cbl.character, currentCode);
		}
		return canonicalCodes;
		
	}
}