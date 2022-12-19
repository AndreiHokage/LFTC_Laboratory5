import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String currentDir = System.getProperty("user.dir") + "\\src\\main\\resources\\";

        System.out.print("Grammar file and input: ");
        String file = currentDir + scanner.next() + ".txt";

        List<String> dataFromFile = FileUtils.readFromFile(file);
        List<ProductionRule> productionRules = dataFromFile.stream()
                .filter(line -> line.contains("->"))
                .map(line -> new ProductionRule(line.split("->")[0], line.split("->")[1]))
                .collect(Collectors.toList());
        List<String> inputSequences = dataFromFile.stream()
                .filter(line -> !line.contains("->"))
                .collect(Collectors.toList());

        System.out.println("\nProduction rules:");
        productionRules.forEach(System.out::println);

        System.out.println("\nAnalysing sequences:");
        for (String sequence : inputSequences) {
            SemanticAnalyzer analyzer = new SemanticAnalyzer(sequence, productionRules, productionRules.get(0).getLeftHand());
            List<String> result = analyzer.analyze();
            if (result.isEmpty())
                System.out.println(sequence + " - NOT ACCEPTED BY THE GRAMMAR");
            else
                System.out.println(sequence + " - ACCEPTED BY THE GRAMMAR, USING THE RULES TO BUILT THE SEQUENCE " +
                        "IN THIS ORDER: " + result);
        }

    }
}
