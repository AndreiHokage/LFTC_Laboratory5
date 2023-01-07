import java.util.Optional;

public class LexicographicalList {

    ItemSymbolTable head;
    Integer size;

    public LexicographicalList(){
        this.head = null;
        this.size = 0;
    }

    public LexicographicalList(ItemSymbolTable head) {
        this.head = head;
        this.size = 1;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public ItemSymbolTable getHead() {
        return head;
    }

    public void setHead(ItemSymbolTable head) {
        this.head = head;
    }

    public Optional<ItemSymbolTable> findElement(String symbol){
        ItemSymbolTable copyHead = head;
        while(copyHead != null){
            if(copyHead.getSymbol().equals(symbol))
                return Optional.of(copyHead);
            copyHead = copyHead.getNextElement();
        }
        return Optional.empty();
    }

    public void addItem(ItemSymbolTable item){
        size = size + 1;
        item.setIndexSymbolTable(size);

        if(size.equals(1)){ // empty list
            head = item;
//            item.setIndexLista(0);
            item.setNextElement(null);
            return;
        }

        ItemSymbolTable current = head;
        ItemSymbolTable prev = null;
        while( current != null && item.getSymbol().compareTo( current.getSymbol() ) > 0 ){
            prev = current;
            current = current.getNextElement();
        }

        if(current != null && head.getSymbol().equals( current.getSymbol() )) { // the new element becomes the new head of the list
//            item.setIndexLista(0);
            item.setNextElement(head);
//            current.setIndexLista(1);
            head = item;
            return;
        }

        if(current == null) { // the tail of the list
//            item.setIndexLista(prev.getIndexLista() + 1);
            prev.setNextElement(item);
            item.setNextElement(null);
            return;
        }

        prev.setNextElement(item);
        item.setNextElement(current);
//        item.setIndexLista(prev.getIndexLista() + 1);
    }
}
