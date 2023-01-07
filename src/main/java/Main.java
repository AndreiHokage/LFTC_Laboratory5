import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private static AF readAFFromFile(String inputTXT) throws FileNotFoundException {
        inputTXT = System.getProperty("user.dir") + "\\src\\main\\resources\\" + inputTXT;
        InputStream inputFile = new FileInputStream(inputTXT);
        Scanner myReader = new Scanner(inputFile);

        String alphabet = myReader.nextLine();
        List<String> charactersAlphabet = Arrays.stream(alphabet.split(" ")).toList();

        String states = myReader.nextLine();
        List<String> elemStates = Arrays.stream(states.split(" ")).toList();

        String initialState = myReader.nextLine();

        String finalStates = myReader.nextLine();
        List<String> elemFinalStates = Arrays.stream(finalStates.split(" ")).toList();

        List<Transition> transitions = new ArrayList<>();
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            if (line.matches("^[ ]*$"))
                break;
            String[] words = line.split(" ");
            String source = words[0], destination = words[1];
            Integer index = 2;
            while (index < words.length) {
                String inputAlphabet = words[index];
                if (inputAlphabet.equals("space"))
                    inputAlphabet = " ";
                transitions.add(new Transition(source, destination, inputAlphabet));
//                if(inputTXT.equals("D:\\UBB-Didactic\\An 3\\LFTC\\Laborator 5\\src\\main\\resources\\AF_literal.txt"))
//                    System.out.println(source + " " + destination + " " + inputAlphabet);
                index++;
            }
        }

        AF af = new AF(initialState, elemFinalStates, elemStates, transitions, charactersAlphabet);
        return af;
    }

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        String currentDir = System.getProperty("user.dir") + "\\src\\main\\resources\\";

        System.out.print("Grammar file and input: ");
        String file = currentDir + "grammar.txt";

        List<String> dataFromFile = FileUtils.readFromFile(file);
        List<ProductionRule> productionRules = dataFromFile.stream()
                .filter(line -> line.contains("->"))
                .map(line -> new ProductionRule(line.split("->")[0], line.split("->")[1]))
                .collect(Collectors.toList());

//        System.out.println("\nProduction rules:");
//        productionRules.forEach(System.out::println);

        AF identifierStateMachine = readAFFromFile("AF_identifier.txt");
        AF literalStateMachineList = readAFFromFile("AF_literal.txt");
        AF relationalOperatorStateMachine = readAFFromFile("AF_relation.txt");
        AF arithmeticOperatorStateMachine = readAFFromFile("AF_arithmetic.txt");
        AF separatorsStateMachine = readAFFromFile("AF_delimitator.txt");

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(identifierStateMachine, literalStateMachineList,
                relationalOperatorStateMachine, arithmeticOperatorStateMachine, separatorsStateMachine);

        lexicalAnalyzer.analyze(new File(System.getProperty("user.dir") + "\\src\\main\\resources\\input.txt"));
        List<Integer> inputSequence = lexicalAnalyzer.getInputSequence();
        for (Integer x : inputSequence)
            System.out.print(x + " ");
        System.out.println();

        SemanticAnalyzer analyzer = new SemanticAnalyzer(inputSequence, productionRules,
                productionRules.get(0).getLeftHand(), lexicalAnalyzer.getLexicalAtomTable());

        List<String> result = analyzer.analyze();
        if (result.isEmpty())
            System.out.println(" - NOT ACCEPTED BY THE GRAMMAR");
        else {
            System.out.println(" - ACCEPTED BY THE GRAMMAR, USING THE RULES TO BUILT THE SEQUENCE " +
                    "IN THIS ORDER: ");
            result.forEach(System.out::println);
        }


    }
}
