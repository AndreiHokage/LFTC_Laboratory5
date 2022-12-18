import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer {

    private String inputSequence;
    private List<ProductionRule> productionRules;

    public SemanticAnalyzer(String inputSequence, List<ProductionRule> productionRules) {
        this.inputSequence = inputSequence;
        this.productionRules = productionRules;
    }

    /*
        Returns an empty list if the input sequence is not accepted by the grammat
        Otherwise, it returns a list of the production rules in order they should be applied in order to
        get our input sequence
         */
    public List<String> analyze(){

        return new ArrayList<>();
    }
}
