import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class TestJava {
    public static void main(String[] args) throws Exception {
        ANTLRInputStream input =new ANTLRInputStream(System.in);

        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);

        ParseTree tree = parser.prog();

        MyJava myJava = new MyJava();
        myJava.visit(tree);
    }
}
