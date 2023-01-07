import java.util.*;

public class AF {

    class PairNode{
        private String state;
        private String label;

        public PairNode(String state, String label) {
            this.state = state;
            this.label = label;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PairNode pairNode = (PairNode) o;
            return Objects.equals(state, pairNode.state) && Objects.equals(label, pairNode.label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, label);
        }
    }

    private List<String> inputAlphabet;
    private String initialState;
    private List<String> finalStates;
    private List<String> nodes; // the set of states
    private List<Transition> edges;

    public AF(String initialState, List<String> finalStates, List<String> nodes, List<Transition> edges, List<String> inputAlphabet) {
        this.initialState = initialState;
        this.finalStates = finalStates;
        this.nodes = nodes;
        this.edges = edges;
        this.inputAlphabet = inputAlphabet;
    }

    public List<String> getInputAlphabet() {
        return inputAlphabet;
    }

    public void setInputAlphabet(List<String> inputAlphabet) {
        this.inputAlphabet = inputAlphabet;
    }

    public String getInitialState() {
        return initialState;
    }

    public List<String> getFinalStates() {
        return finalStates;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public List<Transition> getEdges() {
        return edges;
    }

    public Boolean isDeterminism(){
        Map< PairNode, Boolean> map = new HashMap<>();
        for(Transition transition: edges){
            PairNode pairNode = new PairNode(transition.getSource(), transition.getWeight());
            if(map.containsKey(pairNode))
                return Boolean.FALSE;
            map.put(pairNode, true);
        }

        return Boolean.TRUE;
    }

    public String findTheLongestPrefix(String sequence){
        Integer i = 0;
        String currentState = initialState;
        String prefix = "";
        while(i < sequence.length()){
            String character = sequence.substring(i, i + 1);
            Optional<Transition> transition = checkThereIsTransition(currentState, character);
            if(transition.isEmpty())
                return prefix;
            currentState = transition.get().getDestination();
            i = i + 1;
            if(checkFinalState(currentState))
                prefix = sequence.substring(0, i);
        }
        return prefix;
    }

    public Boolean checkSequence(String sequence){
        Integer i = 0;
        String currentState = initialState;
        while(i < sequence.length()){
            String character = sequence.substring(i, i + 1);
            Optional<Transition> transition = checkThereIsTransition(currentState, character);
            if(transition.isEmpty())
                return Boolean.FALSE;
            currentState = transition.get().getDestination();
            i = i + 1;
        }

        if(checkFinalState(currentState))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Boolean checkFinalState(String currentState){
        for(String finalState: finalStates)
            if(finalState.equals(currentState))
                return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Optional<Transition> checkThereIsTransition(String currentState, String character){

        for(Transition transition: edges){
            if(transition.getSource().equals(currentState) && transition.getWeight().equals(character))
                return Optional.of(transition);
        }
        return Optional.empty();
    }
}
