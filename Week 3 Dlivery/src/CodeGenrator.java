import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class CodeGenrator {
    public static void main(String[] args) throws Exception{

        String inputFileName = "Input.java";


        FileInputStream inputFile = new FileInputStream(new File(inputFileName));


        ANTLRInputStream input = new ANTLRInputStream(inputFile);

//        ANTLRInputStream input = new ANTLRInputStream(System.in);

        JavaLexer lexer = new JavaLexer(input);

        CommonTokenStream token = new CommonTokenStream(lexer);

        JavaParser parser = new JavaParser(token);

//        TokenStreamRewriter rewriter = new TokenStreamRewriter(token);

        ParseTree tree = parser.compilationUnit();


//        MyVisitorParser myparser = new MyVisitorParser();
//        myparser.visit(tree);

        ParseTreeWalker walker = new ParseTreeWalker();
        MyListenerParser listner = new MyListenerParser(parser);
        walker.walk(listner, tree);

        // Run the generated Java file automatically using Process
        execute("javac Output.java ;" +
                "java Output");

        // walk again for html generation
        listner.ishtml = true;
        walker.walk(listner, tree);
    }
    /***************************************************************************//**
     * This class is used to execute shell commands written for powershell,
     * the code is used to automate the execution of the generated code in the
     * output file
     ******************************************************************************/
    static void execute(String command) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("powershell.exe", "/c", command);
        try {

            Process process = builder.start();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
//            System.out.println("\nExited with error code : " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
