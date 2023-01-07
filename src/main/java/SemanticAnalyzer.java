import java.io.IOException;
import java.lang.module.Configuration;
import java.util.*;

public class SemanticAnalyzer {

    private class WorkConfiguration{
        private String stateMachine;
        private Integer positionInput;

        public WorkConfiguration(String stateMachine, Integer positionInput) {
            this.stateMachine = stateMachine;
            this.positionInput = positionInput;
        }

        public String getStateMachine() {
            return stateMachine;
        }

        public Integer getPositionInput() {
            return positionInput;
        }

        public void setStateMachine(String stateMachine) {
            this.stateMachine = stateMachine;
        }

        public void setPositionInput(Integer positionInput) {
            this.positionInput = positionInput;
        }
    }

    private class Pair{
        private String leftMember;
        private Integer indexOrder;

        public Pair(String leftMember, Integer indexOrder) {
            this.leftMember = leftMember;
            this.indexOrder = indexOrder;
        }

        public String getLeftMember() {
            return leftMember;
        }

        public void setLeftMember(String leftMember) {
            this.leftMember = leftMember;
        }

        public Integer getIndexOrder() {
            return indexOrder;
        }

        public void setIndexOrder(Integer indexOrder) {
            this.indexOrder = indexOrder;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return Objects.equals(leftMember, pair.leftMember) && Objects.equals(indexOrder, pair.indexOrder);
        }

        @Override
        public int hashCode() {
            return Objects.hash(leftMember, indexOrder);
        }
    }

    private String startSymbol;
    private List<Integer> inputSequence;
    private List<ProductionRule> productionRules;
    private HashMap<Pair, ProductionRule> hashMap = new HashMap<>();
    private LexicalAtomTable lexicalAtomTable;

    public SemanticAnalyzer(List<Integer> inputSequence, List<ProductionRule> productionRules, String startSymbol,
                            LexicalAtomTable lexicalAtomTable) {
        this.inputSequence = inputSequence;
        this.productionRules = productionRules;
        this.startSymbol = startSymbol;
        this.lexicalAtomTable = lexicalAtomTable;
        Integer index = 1;
        String prev = "";
        for(ProductionRule productionRule: productionRules){
            if(prev.equals(productionRule.getLeftHand())){
                index = index + 1;
            }else{
                prev = productionRule.getLeftHand();
                index = 1;
            }
            hashMap.put(new Pair(productionRule.getLeftHand(), index), productionRule);
        }

//        for(Map.Entry<Pair, ProductionRule> el : hashMap.entrySet())
//            System.out.println(el.getKey().leftMember + " " + el.getKey().indexOrder + " -> " + el.getValue().getRightHand());
    }

    private String extractSymbol(String expr){
        String ans = expr.substring(1,expr.length() - 1);
        return expr;
    }

    private Boolean checkIsUnfinishedSymbol(String expr){
        if(expr.charAt(0) == '`')
            return true;
        return false;
    }

    /*
        Returns an empty list if the input sequence is not accepted by the grammat
        Otherwise, it returns a list of the production rules in the order they should be applied in order to
        get our input sequence
    */
    public List<String> analyze(){
        Stack<String> workStack = new Stack<>();
        workStack.push("eps");
        Stack<String> inputStack = new Stack<>();
        inputStack.push(startSymbol);
        WorkConfiguration transitions = new WorkConfiguration("q", 1);

        while(true) {
//            System.out.println("state= " + transitions.stateMachine);
//            System.out.println("index= " + lexicalAtomTable.getLexicalAtom( inputSequence.get(transitions.positionInput)) );
//            System.out.println("--------------------WORK---------------------------");
//            for(String el: workStack) System.out.println(el);
//            System.out.println("--------------------INPUT---------------------------");
//            for(String el: inputStack) System.out.println(el);
//            System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
//            try {
//                System.in.read();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            if (transitions.getStateMachine().equals("q")) {

                // success
                if (transitions.getPositionInput().equals(inputSequence.size())) {
                    List<String> ans = new ArrayList<>();
                    while (!workStack.isEmpty()) {
                        String unfinishedSymbolPair = workStack.pop();
                        if (unfinishedSymbolPair.equals("eps"))
                            continue;
                        if (("" + unfinishedSymbolPair.charAt(0)).equals("'"))
                            continue;
                        int findDel = 1;
                        while (findDel < unfinishedSymbolPair.length() && unfinishedSymbolPair.charAt(findDel) != '`')
                            findDel++;
                        findDel++;
                        assert findDel < unfinishedSymbolPair.length();
                        String unfinishedSymbol = unfinishedSymbolPair.substring(0, findDel);
                        Integer numberRule = Integer.parseInt(unfinishedSymbolPair.substring(findDel));
                        String rule = unfinishedSymbol + " -> " + hashMap.get(new Pair(unfinishedSymbol, numberRule)).getRightHand();
                        ans.add(rule);
                    }
                    Collections.reverse(ans);
                    return ans;
                }

                // AFTER SUCCESS; It means that the input stack is empty and we still have input characters
                if (inputStack.isEmpty()) {
                    transitions.setStateMachine("r");
                    continue;
                }


                String element_input_stack = inputStack.peek();
                if (checkIsUnfinishedSymbol(element_input_stack)) {
                    transitions = new WorkConfiguration("q", transitions.getPositionInput());
                    workStack.push(element_input_stack + "1");
                    inputStack.pop();
                    String rightHandString = hashMap.get(new Pair(element_input_stack, 1)).getRightHand();
                    int i = rightHandString.length() - 1;
                    while (i >= 0) {
                        int j = i - 1;
                        while (j >= 0 && rightHandString.charAt(j) != rightHandString.charAt(i)) {
                            j--;
                        }
                        String aux = rightHandString.substring(j, i + 1);
                        inputStack.push(aux);
                        i = j - 1;
                    }
                    continue;
                }

                // avans
                Integer inputDigit = inputSequence.get(transitions.getPositionInput());
                String inputString = "'" + lexicalAtomTable.getLexicalAtom(inputDigit).get() + "'";
                if (inputStack.peek().equals(inputString) || inputStack.peek().equals("'EPS'")) {
                    transitions = new WorkConfiguration("q", transitions.getPositionInput() + 1);
                    workStack.push(inputString);
                    inputStack.pop();
                    continue;
                } else {
                    // insucces de moment
                    transitions.setStateMachine("r");
                    continue;
                }
            }

            if (transitions.getStateMachine().equals("r")) {
                // revenire
                String finishedSymbol = workStack.peek();
                if (!checkIsUnfinishedSymbol(finishedSymbol)) {
                    transitions.setPositionInput(transitions.getPositionInput() - 1);
                    workStack.pop();
                    inputStack.push(finishedSymbol);
                    continue;
                }

                // another try
                String unfinishedSymbolPair = workStack.peek();
                int findDel = 1;
                while (findDel < unfinishedSymbolPair.length() && unfinishedSymbolPair.charAt(findDel) != '`')
                    findDel++;
                findDel++;
                assert findDel < unfinishedSymbolPair.length();
                String unfinishedSymbol = unfinishedSymbolPair.substring(0, findDel);
                Integer numberRule = Integer.parseInt(unfinishedSymbolPair.substring(findDel));

                Integer newNumberRule = numberRule + 1;
                if (hashMap.get(new Pair(unfinishedSymbol, newNumberRule)) != null) {
                    transitions.setStateMachine("q");
                    workStack.pop();
                    workStack.push(unfinishedSymbol + newNumberRule.toString());

                    String oldRightRule = hashMap.get(new Pair(unfinishedSymbol, numberRule)).getRightHand();

                    Integer indexPassing = 0;
                    while (indexPassing < oldRightRule.length()) {
                        int j = indexPassing + 1;
                        while (j < oldRightRule.length() && oldRightRule.charAt(j) != oldRightRule.charAt(indexPassing))
                            j++;

                        inputStack.pop();
                        indexPassing = j + 1;
                    }
                    String rightHandString = hashMap.get(new Pair(unfinishedSymbol, newNumberRule)).getRightHand();


                    int i = rightHandString.length() - 1;
                    while (i >= 0) {
                        int j = i - 1;
                        while (j >= 0 && rightHandString.charAt(j) != rightHandString.charAt(i)) {
                            j--;
                        }
                        String aux = rightHandString.substring(j, i + 1);
                        inputStack.push(aux);

                        i = j - 1;
                    }
                    continue;
                }

                if (transitions.getPositionInput().equals(1) && unfinishedSymbol.equals(startSymbol)) {
                    return new ArrayList<String>();
                }

                workStack.pop();
                String oldRightRule = hashMap.get(new Pair(unfinishedSymbol, numberRule)).getRightHand();
                Integer indexPassing = 0;
                while (indexPassing < oldRightRule.length()) {
                    int j = indexPassing + 1;
                    while (j < oldRightRule.length() && oldRightRule.charAt(j) != oldRightRule.charAt(indexPassing))
                        j++;
                    inputStack.pop();
                    indexPassing = j + 1;
                }
                inputStack.push(unfinishedSymbol);
            }
        }
    }

}
