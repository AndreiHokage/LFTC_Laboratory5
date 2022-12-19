import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static List<String> readFromFile(String filename) {
        List<String> list = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] words = line.split(" ");
                for (String each : words) {
                    if (each.length() > 0)
                        list.add(each);
                }
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            System.out.println("FILE DOES NOT EXIST!");
        }
        return list;
    }
}
