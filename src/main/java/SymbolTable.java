import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SymbolTable {

    private Integer NO_FOUND = 0;
    private LexicographicalList lexicographicalList = new LexicographicalList();

    public SymbolTable(){

    }

    public Integer getCodeFromSymbolTable(String symbol){
        Optional<ItemSymbolTable> itemSymbolTable = lexicographicalList.findElement(symbol);
        if(itemSymbolTable.isEmpty())
            return NO_FOUND;
        return itemSymbolTable.get().getIndexSymbolTable();
    }

    public void addIdentifierToSymbolTable(String symbol){
        ItemSymbolTable item = new ItemSymbolTable(symbol);
        lexicographicalList.addItem(item);
    }

    @Override
    public String toString() {
        Map<Integer, ItemSymbolTable> hashMap = new HashMap<Integer, ItemSymbolTable>();
        ItemSymbolTable current = lexicographicalList.getHead();

        while(current != null){
            hashMap.put(current.getIndexSymbolTable(), current);
            current = current.getNextElement();
        }

        Integer noElements = lexicographicalList.getSize();
        String text = "";
        for(int i = 1; i <= noElements; i++){
            ItemSymbolTable element = hashMap.get(i);
            Integer indexNeigh = -1;
            if(element.getNextElement() != null)
                indexNeigh = element.getNextElement().getIndexSymbolTable();
            String line = "" + i + " " + element.getSymbol() + " " + indexNeigh + "\n";
            text = text + line;
        }

        return text;
    }
}
