import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * The class to carry out Plagiarism Detection
 */
public class PlagiarismDetector {

	static final int DEFAULT_TUPLE_SIZE = 3;

	static int tupleSize;
	static File synonymsFile, file1, file2;

	static HashMap<String, ArrayList<String>> synonymGroups = new HashMap<>();
	static HashMap<ArrayList<String>, Integer> file1tuples = new HashMap<>();
	static HashMap<ArrayList<String>, Integer> file2tuples = new HashMap<>();

	/**
	 * The main method of the Plagiarism Detector class
	 *
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		initializeInputs(args);

		loadSynonyms(synonymsFile);

		file1tuples = tokenize(parseFile(file1));
		
		substituteWithSynonyms(file1tuples);

		file2tuples = tokenize(parseFile(file2));
		
		substituteWithSynonyms(file2tuples);

		System.out.println(detectPlagiarism() + "%");
	}

	/**
	 * A helper method to load the synonyms from the synonyms file. The method
	 * groups synonyms using a word as a key , and the synonyms of the word as
	 * values
	 *
	 * @param synonymsFile
	 *            The file containing the synonyms
	 */
	public static void loadSynonyms(File synonymsFile) {
		ArrayList<String> lines = parseSynonyms(synonymsFile);

		ArrayList<String> synonymsSet;

		for (String line : lines) {
			String[] lineAsWords = line.split(" ");
			synonymsSet = new ArrayList<String>(Arrays.asList(lineAsWords));

			for (String word : lineAsWords) {
				if (!synonymGroups.containsKey(word))
					synonymGroups.put(word, synonymsSet);
			}
		}
	}

	/**
	 * A helper method to substitute words in a tuple with its synonym group
	 * 
	 * @param tuplesInFile:
	 *            A HashMap representing the tuples in a file
	 */
	public static void substituteWithSynonyms(HashMap<ArrayList<String>, Integer> tuplesInFile) {
		for (ArrayList<String> tuples : tuplesInFile.keySet()) {
			for (String key : tuples) {
				if (synonymGroups.containsKey(key)) {
					tuples.remove(key);
					tuples.add(synonymGroups.get(key).toString());
				}
			}
		}
	}

	/**
	 * A method to count the matching tuples
	 */
	public static double detectPlagiarism() {
		double matchingTuples = 0;

		HashMap<ArrayList<String>, Integer> largerTuples;
		HashMap<ArrayList<String>, Integer> smallerTuples;

		if (file1tuples.size() >= file2tuples.size()) {
			largerTuples = file1tuples;
			smallerTuples = file2tuples;
		} else {
			largerTuples = file2tuples;
			smallerTuples = file1tuples;
		}

		for (ArrayList<String> key : largerTuples.keySet()) {
			if (smallerTuples.toString().contains(key.toString())) {
				matchingTuples++;
			}
		}

		return calculateRatio(matchingTuples, largerTuples.size());
	}

	/**
	 * A helper method to calculate the plagiarism ratio given the number of
	 * matching tuples
	 * 
	 * @param matchingTuples:
	 *            The number of matching tuples
	 * @param largerTuples:
	 *            A hashmap representing the tuples of the larger file
	 */
	public static double calculateRatio(double matchingTuples, int sizeOflargerTuples) {
		return (matchingTuples / sizeOflargerTuples) * 100;
	}

	/**
	 * A helper method to create a HashMap of tuples from a given String of
	 * words
	 * 
	 * @param wordsList:
	 *            The string to convert to tuples
	 */
	public static HashMap<ArrayList<String>, Integer> tokenize(String wordsList) {
		HashMap<ArrayList<String>, Integer> tuplesInFile = new HashMap<>();
		int tuplesCount = 0;

		String[] wordsAsArray = wordsList.split(" ");
		ArrayList<String> tuples;
		int count;

		for (int i = 0; i <= (wordsAsArray.length - tupleSize); i++) {
			tuples = new ArrayList<>();
			count = 0;

			while (count < tupleSize) {
				tuples.add(wordsAsArray[count + i]);
				count++;
			}

			tuplesInFile.put(tuples, tuplesCount);
			tuplesCount++;
		}
		return tuplesInFile;
	}

	/**
	 * A helper method to parse a text file and return the lines in the file as
	 * a String
	 * 
	 * @param lines
	 *            The file to parse
	 */
	public static String parseFile(File fileName) {
		StringBuilder result = new StringBuilder();
		try {
			Scanner scanner = new Scanner(fileName);
			while (scanner.hasNext()) {
				result.append(scanner.next() + " ");
			}
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString().trim();
	}

	/**
	 * A helper method to parse the synonyms file and return the lines in the
	 * file as an ArrayList
	 * 
	 * @param fileName
	 *            The file to parse
	 */
	public static ArrayList<String> parseSynonyms(File fileName) {
		ArrayList<String> result = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(fileName);
			while (scanner.hasNextLine()) {
				result.add(scanner.nextLine());
			}
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * A helper method to validate the arguments passed by a user and initialize
	 * appropriate variables
	 * 
	 * @param lines
	 *            The file to parse
	 */
	public static void initializeInputs(String[] args) throws Exception {
		if (args.length < 3) {
			help();
		} else if (args.length == 3) {
			tupleSize = DEFAULT_TUPLE_SIZE;
		} else {
			tupleSize = Integer.valueOf(args[3]);
		}

		synonymsFile = new File(args[0]);
		file1 = new File(args[1]);
		file2 = new File(args[2]);

		if (!synonymsFile.exists() || !file1.exists() || !file2.exists()) {
			throw new Exception(
					"The files you provided could not be found, verify they exist and check the path entered!");
		}
	}

	/**
	 * A help method displayed to the user when invalid arguments are specified
	 */
	public static void help() {
		System.out.println();
		System.out.println("Welcome to the Plagiarism Detector");
		System.out.println("This program requires 3 mandatory arguments to run");
		System.out.println("This program also accepts one optional argument to specify the default tuple size");
		System.out.println();
		System.out.println("Usage Instructions : ");
		System.out.println("1. file name for a list of synonyms ");
		System.out.println("2. input file 1");
		System.out.println("3. input file 2");
		System.out.println("4. (optional) a number N, the tuple size.  If not supplied, the default will be N=3. ");
		System.out.println("Sample Run Command : `java PlagiarismDetector syns.txt file1.txt file2.txt 4` ");
		System.out.println("Sample Run Command : `java PlagiarismDetector syns.txt file1.txt file2.txt` ");
		System.exit(0);
	}
}
