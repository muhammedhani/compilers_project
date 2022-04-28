import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyVisitorParser extends JavaParserBaseVisitor<String> {

    void write(String string) throws Exception {
        String outputFileName = "Output.java";
        FileOutputStream outputFile = new FileOutputStream(outputFileName, true);
        BufferedOutputStream buffer = new BufferedOutputStream(outputFile);
        byte[] bytes = string.getBytes();
        buffer.write(bytes);
        buffer.close();
    }

    @Override
    public String visitPackageDeclaration(JavaParser.PackageDeclarationContext ctx) {
        return super.visitPackageDeclaration(ctx);
    }

    @Override
    public String visitCompilationUnit(JavaParser.CompilationUnitContext ctx) {

        return super.visitCompilationUnit(ctx);
    }

    @Override
    public String visitBlock(JavaParser.BlockContext ctx) {
        try {
            this.write(ctx.getText()+'\n');
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.visitBlock(ctx);
    }

    @Override
    public String visitBlockStatement(JavaParser.BlockStatementContext ctx) {

        return super.visitBlockStatement(ctx);
    }

    @Override
    public String visitStatement(JavaParser.StatementContext ctx) {
        return super.visitStatement(ctx);
    }

}
