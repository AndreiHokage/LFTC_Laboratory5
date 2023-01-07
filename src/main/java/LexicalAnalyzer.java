import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LexicalAnalyzer {

    private static Integer LENGTH_WORD = 8;

    private static LexicalAtomTable lexicalAtomTable = new LexicalAtomTable();
    private static FIP fip = new FIP();
    private static SymbolTable identifiersSymbolTable = new SymbolTable();
    private static SymbolTable constantsSymbolTable = new SymbolTable();


    private List<String> keyWords = Arrays.asList("#include", "int", "double", "vector<int>", "if", "else",
            "for", "while", "cin", "cout", "main()", "using", "namespace", "std", "return", "<iostream>");
    AF identifierStateMachine;
    AF literalStateMachine;
    AF relationalOperatorStateMachine;
    AF arithmeticOperatorStateMachine;
    AF separatorsStateMachine;

    public LexicalAnalyzer(AF identifierStateMachine, AF literalStateMachine,
                           AF relationalOperatorStateMachine, AF arithmeticOperatorStateMachine, AF separatorsStateMachine) {
        this.identifierStateMachine = identifierStateMachine;
        this.literalStateMachine = literalStateMachine;
        this.relationalOperatorStateMachine = relationalOperatorStateMachine;
        this.arithmeticOperatorStateMachine = arithmeticOperatorStateMachine;
        this.separatorsStateMachine = separatorsStateMachine;
    }

    public LexicalAtomTable getLexicalAtomTable() {
        return lexicalAtomTable;
    }

    public void analyze(File file) throws Exception {

        Scanner sc = new Scanner(file);
        Integer indexLine = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            line = line.trim();
            indexLine = indexLine + 1;

            if (line.equals("") || line.charAt(0) == line.charAt(line.length() - 1) && line.charAt(0) == ' ')
                continue;

            Integer indexColumn = 0;
            Boolean flag = false;
            while (line.length() > 0) {
                Integer j = 0;
                while (j < line.length() && line.charAt(j) == ' ') j++;
                line = line.substring(j);
                indexColumn += j;

                for (String keyWord : keyWords) {
                    if (line.startsWith(keyWord)) {
                        lexicalAtomTable.addLexicalAtom(keyWord);
                        fip.addEntry(lexicalAtomTable.getCodeAtom(keyWord), 0);
                        line = line.substring(keyWord.length());
                        indexColumn = indexColumn + keyWord.length();
                        flag = true;
                        break;
                    }
                }

                String sequence = "";
                if (!flag) {
                    // Identifiers identification
                    sequence = identifierStateMachine.findTheLongestPrefix(line);
                    if (sequence.length() > 0) {
                        if (sequence.length() > LENGTH_WORD)
                            throw new Exception("The length of word is greater than " + LENGTH_WORD + " characters");
                        if (identifiersSymbolTable.getCodeFromSymbolTable(sequence) == 0)
                            identifiersSymbolTable.addIdentifierToSymbolTable(sequence);
                        fip.addEntry(lexicalAtomTable.getIdentifierAtomCode(), identifiersSymbolTable.getCodeFromSymbolTable(sequence));
                        flag = true;
                    }
                }

                // Literals identification
                if (!flag) {
                    sequence = literalStateMachine.findTheLongestPrefix(line);
                    if (sequence.length() > 0) {
                        if (constantsSymbolTable.getCodeFromSymbolTable(sequence) == 0)
                            constantsSymbolTable.addIdentifierToSymbolTable(sequence);
                        fip.addEntry(lexicalAtomTable.getLiteralAtomCode(), constantsSymbolTable.getCodeFromSymbolTable(sequence));
                        flag = true;
                    }
                }

                // arithmetic operator identification
                if (!flag) {
                    sequence = arithmeticOperatorStateMachine.findTheLongestPrefix(line);
                    if (sequence.length() > 0) {
                        if (lexicalAtomTable.getCodeAtom(sequence) == null)
                            lexicalAtomTable.addLexicalAtom(sequence);
                        fip.addEntry(lexicalAtomTable.getCodeAtom(sequence), constantsSymbolTable.getCodeFromSymbolTable(sequence));
                        flag = true;
                    }
                }

                // relation operator identification
                if (!flag) {
                    sequence = relationalOperatorStateMachine.findTheLongestPrefix(line);
                    if (sequence.length() > 0) {
                        if (lexicalAtomTable.getCodeAtom(sequence) == null)
                            lexicalAtomTable.addLexicalAtom(sequence);
                        fip.addEntry(lexicalAtomTable.getCodeAtom(sequence), constantsSymbolTable.getCodeFromSymbolTable(sequence));
                        flag = true;
                    }
                }

                // delimitators identification
                if (!flag) {
                    sequence = separatorsStateMachine.findTheLongestPrefix(line);
                    if (lexicalAtomTable.getCodeAtom(sequence) == null)
                        lexicalAtomTable.addLexicalAtom(sequence);
                    if (sequence.length() > 0) {
                        fip.addEntry(lexicalAtomTable.getCodeAtom(sequence), constantsSymbolTable.getCodeFromSymbolTable(sequence));
                        flag = true;
                    }
                }

                // error
                if (flag == false)
                    throw new Exception("Lexical error on (line, column): ( " + indexLine + " , " + indexColumn + " )");

                line = line.substring(sequence.length());
                indexColumn = indexColumn + sequence.length();
                flag = false;
            }
        }

        writeToFiles();
    }

    public List<Integer> getInputSequence() {
        return fip.getAtomCodeSequence();
    }

    private void writeToFiles() throws IOException {
        String crt = System.getProperty("user.dir") + "\\src\\main\\resources\\";
        FileWriter outputName = new FileWriter(crt + "atomLexicalTable.txt");
        PrintWriter fileWriter = new PrintWriter(outputName);
        fileWriter.println(lexicalAtomTable);
        fileWriter.close();

        outputName = new FileWriter(crt + "fip.txt");
        fileWriter = new PrintWriter(outputName);
        fileWriter.println(fip);
        fileWriter.close();

        outputName = new FileWriter(crt + "identifiersSymbolTable.txt");
        fileWriter = new PrintWriter(outputName);
        fileWriter.println(identifiersSymbolTable);
        fileWriter.close();

        outputName = new FileWriter(crt + "constantsSymbolTable.txt");
        fileWriter = new PrintWriter(outputName);
        fileWriter.println(constantsSymbolTable);
        fileWriter.close();
    }
}
