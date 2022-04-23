import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.File;
import java.io.FileInputStream;

public class Test {
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
    }
}
