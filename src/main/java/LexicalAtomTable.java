import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LexicalAtomTable {

    class ItemLexicalAtom{
        private String lexicalAtom;
        private Integer atomCode;

        public ItemLexicalAtom(String lexicalAtom, Integer atomCode) {
            this.lexicalAtom = lexicalAtom;
            this.atomCode = atomCode;
        }
    }

    private Map<String, Integer> lexicalTable;
    private Integer increment = 1;

    public LexicalAtomTable(){
        lexicalTable = new HashMap<String, Integer>();
        lexicalTable.put("ID", 0);
        lexicalTable.put("CONST", 1);
    }

    public void addLexicalAtom(String lexicalAtom){
        if(lexicalTable.get(lexicalAtom) == null) {
            increment++;
            lexicalTable.put(lexicalAtom, increment);
        }
    }

    public Optional<String> getLexicalAtom(Integer codeAtom) {
        for(Map.Entry<String, Integer> entry: lexicalTable.entrySet()){
            if(entry.getValue().equals(codeAtom))
                return Optional.of(entry.getKey());
        }
        return Optional.empty();
    }

    public Integer getCodeAtom(String lexicalCode){
        return lexicalTable.get(lexicalCode);
    }

    public Integer getIdentifierAtomCode(){
        return 0;
    }

    public Integer getLiteralAtomCode(){
        return 1;
    }

    @Override
    public String toString() {
        String text = "";
        Map<Integer, String> hashMap = new HashMap<Integer, String>();
        for(Map.Entry<String, Integer> entry: lexicalTable.entrySet()){
            hashMap.put(entry.getValue(), entry.getKey());
        }

        for (int i = 0; i < lexicalTable.size(); i++) {
            String line = " " + hashMap.get(i) + " " + i + "\n";
            text = text + line;
        }
        return text;
    }
}
