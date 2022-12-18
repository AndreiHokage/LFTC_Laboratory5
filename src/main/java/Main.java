import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        /*

        Your job:
        1) build the production rules ;ist which you re reading from the input file
        2) build the Semnatic analyzwer object and display the information in a right way after you call the analyzer method
        3) some configurion input files

        Note: !!!
        Please send to the analyzer the start unfinished symbol as well!

         */

        List<ProductionRule> ls = new ArrayList<>();
        ls.add(new ProductionRule("S", "+SS"));
        ls.add(new ProductionRule("S", "--S"));
        ls.add(new ProductionRule("S", "a"));
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer("+a-aa", ls, "S");
        semanticAnalyzer.analyze();
    }
}
