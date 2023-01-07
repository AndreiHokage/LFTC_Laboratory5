public class ItemSymbolTable {

    private String symbol; // each symbol is unique in lexical atom list. It behaves like an id.
    private Integer indexLista;
    private Integer indexSymbolTable;
    private ItemSymbolTable nextElement;

    public ItemSymbolTable(String symbol){
        this.symbol = symbol;
        this.indexLista = null;
        this.indexSymbolTable = null;
        this.nextElement = null;
    }

    public ItemSymbolTable(String symbol, Integer indexLista, Integer indexSymbolTable, ItemSymbolTable nextElement) {
        this.symbol = symbol;
        this.indexLista = indexLista;
        this.indexSymbolTable = indexSymbolTable;
        this.nextElement = nextElement;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getIndexLista() {
        return indexLista;
    }

    public void setIndexLista(Integer indexLista) {
        this.indexLista = indexLista;
    }

    public Integer getIndexSymbolTable() {
        return indexSymbolTable;
    }

    public void setIndexSymbolTable(Integer indexSymbolTable) {
        this.indexSymbolTable = indexSymbolTable;
    }

    public ItemSymbolTable getNextElement() {
        return nextElement;
    }

    public void setNextElement(ItemSymbolTable nextElement) {
        this.nextElement = nextElement;
    }
}
