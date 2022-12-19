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
    private String inputSequence;
    private List<ProductionRule> productionRules;
    private HashMap<Pair, ProductionRule> hashMap = new HashMap<>();

    public SemanticAnalyzer(String inputSequence, List<ProductionRule> productionRules, String startSymbol) {
        this.inputSequence = " " + inputSequence;
        this.productionRules = productionRules;
        this.startSymbol = startSymbol;
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

        while(true){
//            System.out.println("state= " + transitions.stateMachine);
//            System.out.println("index= " + transitions.positionInput);
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
            if(transitions.getStateMachine().equals("q")) {

                // success
                if (transitions.getPositionInput().equals(inputSequence.length())) {
                    List<String> ans = new ArrayList<>();
                    while(!workStack.isEmpty()){
                        String unfinishedSymbolPair = workStack.pop();
                        if(unfinishedSymbolPair.equals("eps"))
                            continue;
                        if(unfinishedSymbolPair.length() == 1)
                            continue;
                        String unfinishedSymbol = "" + unfinishedSymbolPair.charAt(0);
                        Integer numberRule = Integer.parseInt(unfinishedSymbolPair.substring(1));
                        String rule = unfinishedSymbol + " -> " + hashMap.get(new Pair(unfinishedSymbol, numberRule)).getRightHand();
                        ans.add(rule);
                    }
                    Collections.reverse(ans);
                    //for(String el: ans) System.out.println(el);
                    return ans;
                }

                // AFTER SUCCESS; It means that the input stack is empty and we still have input characters
                if(inputStack.isEmpty()){
                    transitions.setStateMachine("r");
                    continue;
                }


                String element_input_stack = inputStack.peek();
                if (element_input_stack.compareTo("A") >= 0 && element_input_stack.compareTo("Z") <= 0) {
                    transitions = new WorkConfiguration("q", transitions.getPositionInput());
                    workStack.push(element_input_stack + "1");
                    inputStack.pop();
                    String rightHandString = hashMap.get(new Pair(element_input_stack, 1)).getRightHand();
                    for(int i = rightHandString.length() - 1; i >= 0; i--){
                        inputStack.push("" + rightHandString.charAt(i));
                    }
//                    inputStack.push(hashMap.get(new Pair(element_input_stack, 1)).getRightHand());
                    continue;
                }

                // avans
                String inputCharacter = "" + inputSequence.charAt(transitions.getPositionInput());
                if (inputStack.peek().equals(inputCharacter)) {
                    transitions = new WorkConfiguration("q", transitions.getPositionInput() + 1);
                    workStack.push(inputCharacter);
                    inputStack.pop();
                    continue;
                }
                else {
                    // insucces de moment
                    transitions.setStateMachine("r");
                    continue;
                }
            }

            if(transitions.getStateMachine().equals("r")) {
                // revenire
                String finishedSymbol = workStack.peek();
                if (!(finishedSymbol.compareTo("A") >= 0 && finishedSymbol.compareTo("Z") <= 0)) {
                    transitions.setPositionInput(transitions.getPositionInput() - 1);
                    workStack.pop();
                    inputStack.push(finishedSymbol);
                    continue;
                }

                // another try
                String unfinishedSymbolPair = workStack.peek();
                String unfinishedSymbol = "" + unfinishedSymbolPair.charAt(0);
                Integer numberRule = Integer.parseInt(unfinishedSymbolPair.substring(1));
                Integer newNumberRule = numberRule + 1;
                if(hashMap.get( new Pair(unfinishedSymbol, newNumberRule) ) != null){
                    transitions.setStateMachine("q");
                    workStack.pop();
                    workStack.push(unfinishedSymbol + newNumberRule.toString());

                    String oldRightRule = hashMap.get( new Pair(unfinishedSymbol, numberRule) ).getRightHand();
                    Integer indexPassing = 0;
                    while(indexPassing < oldRightRule.length()) {
                        inputStack.pop();
                        indexPassing++;
                    }
                    String rightHandString = hashMap.get( new Pair(unfinishedSymbol, newNumberRule) ).getRightHand();
                    for(int i = rightHandString.length() - 1; i >= 0; i--){
                        inputStack.push("" + rightHandString.charAt(i));
                    }
//                    inputStack.push(hashMap.get( new Pair(unfinishedSymbol, newNumberRule) ).getRightHand());
                    continue;
                }

                if(transitions.getPositionInput().equals(1) && unfinishedSymbol.equals(startSymbol)){
                    return new ArrayList<String>();
                }

                // !!
                workStack.pop();
                inputStack.push(unfinishedSymbol);
            }

        }

    }
}
