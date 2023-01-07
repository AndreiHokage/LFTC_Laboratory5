import java.util.ArrayList;
import java.util.List;

public class FIP {

    class ItemFIP {
        private Integer atomCode;
        private Integer indexTS;

        public ItemFIP(Integer atomCode, Integer indexTS) {
            this.atomCode = atomCode;
            this.indexTS = indexTS;
        }

        @Override
        public String toString() {
            return "ItemFIP{" +
                    "atomCode='" + atomCode + '\'' +
                    ", indexTS=" + indexTS +
                    '}';
        }
    }

    private List<ItemFIP> listFIP;

    public FIP(){
        listFIP = new ArrayList<ItemFIP>();
    }

    public void addEntry(Integer atomCode, Integer indexTS){
        listFIP.add(new ItemFIP(atomCode, indexTS));
    }

    public List<Integer> getAtomCodeSequence(){
        List<Integer> ans = new ArrayList<>();
        ans.add(-1);
        for(ItemFIP itemFIP: listFIP)
            ans.add(itemFIP.atomCode);
        return ans;
    }

    @Override
    public String toString() {
        String text = "";
        for(ItemFIP itemFIP: listFIP){
            String line = "" + itemFIP.atomCode + " " + itemFIP.indexTS + "\n";
            text = text + line;
        }
        return text;
    }
}
