import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTreeListener;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.*;


public class MyListenerParser extends JavaParserBaseListener {

    String code = "";
    JavaParser parser;
    int counter = 0, blockNum = 0;
    boolean f = false,e = true;
    Stack<List<Boolean>> s = new Stack<>();
    Stack<String> elseStack = new Stack<>();
    Set<Integer> set = new HashSet<>();
    boolean Switch = false;
    public MyListenerParser(JavaParser parser) {this.parser = parser;}

    void write(String string) throws Exception {
        String outputFileName = "Output.java";
        FileOutputStream outputFile = new FileOutputStream(outputFileName, false);
        BufferedOutputStream buffer = new BufferedOutputStream(outputFile);
        // buffer can only write with characters
        byte[] bytes = string.getBytes();
        buffer.write(bytes);
        buffer.close();
    }

    @Override
    public void enterCompilationUnit(JavaParser.CompilationUnitContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        if(ctx.packageDeclaration() != null) {
            System.out.println(tokens.getText(ctx.packageDeclaration()));
            code += tokens.getText(ctx.packageDeclaration()) + "\n";
        }
        if(ctx.importDeclaration().size() != 0) {
            for(JavaParser.ImportDeclarationContext i: ctx.importDeclaration()) {
                System.out.println(tokens.getText(i));
                code += tokens.getText(i) + "\n";
            }
        }
        System.out.println("import java.util.*;");
        code += "import java.util.*;\n";
    }

    @Override
    public void exitCompilationUnit(JavaParser.CompilationUnitContext ctx) {
        try {
            write(code);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void enterTypeDeclaration(JavaParser.TypeDeclarationContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        if(ctx.classOrInterfaceModifier().size() != 0 ){
            for (JavaParser.ClassOrInterfaceModifierContext i:ctx.classOrInterfaceModifier()) {
                System.out.print(tokens.getText(i)+" ");
                code += tokens.getText(i)+" ";
            }
        }
        counter = 0;
    }

    @Override
    public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        String id = "";
        if(ctx.identifier() != null){
            id = tokens.getText(ctx.identifier());
        }
        System.out.print("class "+id);
        code += "class "+id;
        if(ctx.typeParameters() != null) {
            System.out.print(" "+tokens.getText(ctx.typeParameters()));
            code += " "+tokens.getText(ctx.typeParameters());
        }
        if(ctx.typeType() != null) {
            System.out.print(" extends"+tokens.getText(ctx.typeType()));
            code += " extends"+tokens.getText(ctx.typeType());
        }
        if(ctx.typeList().size() != 0) {
            System.out.print(" implements");
            code += " implements";
            for (JavaParser.TypeListContext i: ctx.typeList() ) {
                System.out.print(" "+tokens.getText(i));
                code += " "+tokens.getText(i);
            }
        }
        System.out.println("{");
        code += "{\n";
        counter++;
    }

    @Override
    public void exitClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        counter--;
        System.out.println("}");
        code += "}\n";
    }

    @Override
    public void enterClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        blockNum++;
        for(int i = 0;i < counter;i++) {
            System.out.print('\t');
            code += "\t";
        }
        if(ctx.STATIC() != null) {
            System.out.print("static ");
            code += "static ";
        }
        if(ctx.modifier().size() != 0){
            for(JavaParser.ModifierContext i: ctx.modifier()){
                System.out.print(tokens.getText(i)+" ");
                code += tokens.getText(i) + " ";
            }
        }
        counter++;
    }

    @Override
    public void exitClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
        counter--;
        for(int i = 0;i < counter;i++) {
            System.out.print("\t");
            code += "\t";
        }
        System.out.println("}");
        code += "}\n";
    }

    @Override
    public void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        if(ctx.typeTypeOrVoid() != null) {
            System.out.print(tokens.getText(ctx.typeTypeOrVoid())+" ");
            code += tokens.getText(ctx.typeTypeOrVoid())+" ";
        }
        if(ctx.identifier() != null) {
            System.out.print(tokens.getText(ctx.identifier())+" ");
            code += tokens.getText(ctx.identifier())+" ";
        }
        if(ctx.formalParameters() != null) {
            System.out.print(tokens.getText(ctx.formalParameters())+" ");
            code += tokens.getText(ctx.formalParameters())+" ";
        }
        if(ctx.THROWS() != null) {
            System.out.print(ctx.THROWS()+" ");
            code += ctx.THROWS()+" ";
        }
        if(ctx.qualifiedNameList() != null) {
            System.out.print(tokens.getText(ctx.qualifiedNameList())+" ");
            code += tokens.getText(ctx.qualifiedNameList())+" ";
        }
        System.out.println("{");
        code += "{\n";
        for(int i = 0;i < counter;i++) {
            System.out.print('\t');
            code += "\t";
        }
        System.out.println("Set<Integer> set = new HashSet<>();");
        code += "Set<Integer> set = new HashSet<>();\n";
    }

    @Override
    public void exitMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        for(int i = 0;i < counter;i++) {
            System.out.print('\t');
            code += "\t";
        }
        System.out.println("for(int i: set){");
        code += "for(int i: set){\n";
        for(int i = 0;i < counter+1;i++) {
            System.out.print('\t');
            code += "\t";
        }
        System.out.println("if(i != 0)System.out.println(\"Block#\"+i+\" is visited\");");
        code += "if(i != 0)System.out.println(\"Block#\"+i+\" is visited\");\n";
        for(int i = 0;i < counter;i++) {
            System.out.print('\t');
            code += "\t";
        }
        System.out.println("}");
        code += "}\n";
    }

    @Override
    public void enterBlock(JavaParser.BlockContext ctx) {
//        if(ctx.blockStatement().size() != 0){
//            for(JavaParser.BlockStatementContext i:ctx.blockStatement()){
//                enterBlockStatement(i);
//            }
//        }
    }

    @Override
    public void enterBlockStatement(JavaParser.BlockStatementContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        if(ctx.localVariableDeclaration() != null) {
            System.out.println("\t\t"+tokens.getText(ctx.localVariableDeclaration())+";");
            code += "\t\t"+tokens.getText(ctx.localVariableDeclaration())+";\n";
        }
        if(ctx.localTypeDeclaration() != null) {
            System.out.println("\t\t"+tokens.getText(ctx.localTypeDeclaration()));
            code += "\t\t"+tokens.getText(ctx.localTypeDeclaration()) + "\n";
        }
    }

    @Override
    public void enterStatement(JavaParser.StatementContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        switch (ctx.getChild(0).getText()){
            case "for":
                for(int i = 0;i < counter;i++) {
                    System.out.print('\t');
                    code += "\t";
                }
                System.out.println("int block"+blockNum+" = 0;");
                code += "int block"+blockNum+" = 0;\n";
                for(int i = 0;i < counter;i++) {
                    System.out.print("\t");
                    code += "\t";
                }
                System.out.println("for("+tokens.getText(ctx.forControl())+"){");
                code += "for("+tokens.getText(ctx.forControl())+"){\n";
                counter++;
                for(int i = 0;i < counter;i++) {
                    System.out.print('\t');
                    code += "\t";
                }
                System.out.println("block"+blockNum+" = "+blockNum+";");
                code += "block"+blockNum+" = "+blockNum+";\n";
                for(int i = 0;i < counter;i++) {
                    System.out.print('\t');
                    code += "\t";
                }
                System.out.println("set.add(block"+blockNum+");");
                code += "set.add(block"+blockNum+");\n";
                blockNum++;
                break;
            case "while":
                for(int i = 0;i < counter;i++) {
                    System.out.print('\t');
                    code += "\t";
                }
                System.out.println("int block"+blockNum+" = 0;");
                code += "int block"+blockNum+" = 0;\n";
                for(int i = 0;i < counter;i++) {
                    System.out.print("\t");
                    code += "\t";
                }
                System.out.println(ctx.WHILE()+tokens.getText(ctx.parExpression())+"{");
                code += ctx.WHILE()+tokens.getText(ctx.parExpression())+"{\n";
                counter++;
                for(int i = 0;i < counter;i++) {
                    System.out.print('\t');
                    code += "\t";
                }
                System.out.println("block"+blockNum+" = "+blockNum+";");
                code += "block"+blockNum+" = "+blockNum+";\n";
                for(int i = 0;i < counter;i++) {
                    System.out.print('\t');
                    code += "\t";
                }
                System.out.println("set.add(block"+blockNum+");");
                code += "set.add(block"+blockNum+");\n";
                blockNum++;
                break;
            case "do":
                for(int i = 0;i < counter;i++) {
                    System.out.print('\t');
                    code += "\t";
                }
                System.out.println("int block"+blockNum+" = 0;");
                code += "int block"+blockNum+" = 0;\n";
                for(int i = 0;i < counter;i++) {
                    System.out.print("\t");
                    code += "\t";
                }
                System.out.println(ctx.DO()+"{");
                code += ctx.DO()+"{\n";
                counter++;
                for(int i = 0;i < counter;i++) {
                    System.out.print('\t');
                    code += "\t";
                }
                System.out.println("block"+blockNum+" = "+blockNum+";");
                code += "block"+blockNum+" = "+blockNum+";\n";
                for(int i = 0;i < counter;i++) {
                    System.out.print('\t');
                    code += "\t";
                }
                System.out.println("set.add(block"+blockNum+");");
                code += "set.add(block"+blockNum+");\n";
                blockNum++;
                break;
//            case "switch":
//                Switch = true;
//                for(int i = 0;i < counter;i++) System.out.print('\t');
//                System.out.println(ctx.SWITCH()+tokens.getText(ctx.parExpression())+"{");
//                counter++;
//                break;
            default:
//                System.out.println("Parent: "+ctx.getParent().getParent().getText()+" child:"+ctx.getChild(0).getText());
                if(ctx.expression().size() != 0){
                    for(int i = 0;i < counter;i++) {
                        System.out.print("\t");
                        code += "\t";
                    }
                    for(JavaParser.ExpressionContext i:ctx.expression()){
                        System.out.print(tokens.getText(i));
                        code += tokens.getText(i);
                    }
                    System.out.println(";");
                    code += ";\n";
                }
                break;
        }

    }

    @Override
    public void enterSwitchBlockStatementGroup(JavaParser.SwitchBlockStatementGroupContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        for(JavaParser.SwitchLabelContext i : ctx.switchLabel()){
            for(int j = 0;j < counter;j++) {
                System.out.print("\t");
                code += "\t";
            }
            System.out.println(tokens.getText(i)+" ");
            code += tokens.getText(i)+" \n";
        }

        for(JavaParser.BlockStatementContext i : ctx.blockStatement()){
            for(int j = 0;j < counter;j++) {
                System.out.print("\t");
                code += "\t";
            }
            System.out.println(tokens.getText(i));
            code += tokens.getText(i) + "\n";
        }
    }

    @Override
    public void exitStatement(JavaParser.StatementContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        switch (ctx.getChild(0).getText()) {
            case "for":
            case "while":
            case "switch":
                counter--;
                for (int i = 0; i < counter; i++) {
                    System.out.print("\t");
                    code += "\t";
                }
                System.out.println("}");
                code += "}\n";
                break;
            case "do":
                counter--;
                for (int i = 0; i < counter; i++) {
                    System.out.print("\t");
                    code += "\t";
                }
                System.out.println("}while" + tokens.getText(ctx.parExpression()) + ";");
                code += "}while" + tokens.getText(ctx.parExpression()) + ";\n";
                break;
        }

    }

    @Override
    public void enterIfbranch(JavaParser.IfbranchContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        for(int i = 0;i < counter;i++) {
            System.out.print('\t');
            code += "\t";
        }
        System.out.println("int block"+blockNum+" = 0;");
        code += "int block"+blockNum+" = 0;\n";
        for(int i = 0;i < counter;i++) {
            System.out.print("\t");
            code += "\t";
        }
        System.out.print(ctx.IF());
        code += ctx.IF();
        System.out.println(tokens.getText(ctx.parExpression())+"{");
        code += tokens.getText(ctx.parExpression())+"{\n";
        counter++;
        for(int i = 0;i < counter;i++) {
            System.out.print('\t');
            code += "\t";
        }
        System.out.println("block"+blockNum+" = "+blockNum+";");
        code += "block"+blockNum+" = "+blockNum+";\n";
        for(int i = 0;i < counter;i++) {
            System.out.print('\t');
            code += "\t";
        }
        System.out.println("set.add(block"+blockNum+");");
        code += "set.add(block"+blockNum+");\n";
        blockNum++;
    }

    @Override
    public void exitIfbranch(JavaParser.IfbranchContext ctx) {
        counter--;
        for(int i = 0;i < counter;i++) {
            System.out.print("\t");
            code += "\t";
        }
        System.out.println("}");
        code += "}\n";
    }

    @Override
    public void enterElseif(JavaParser.ElseifContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        for(int i = 0;i < counter;i++) {
            System.out.print("\t");
            code += "\t";
        }
        System.out.println(ctx.ELSE()+"{");
        code += ctx.ELSE()+"{\n";
        System.out.print("\t");
        code += "\t";
        for(int i = 0;i < counter;i++) {
            System.out.print('\t');
            code += "\t";
        }
        System.out.println("int block"+blockNum+" = 0;");
        code += "int block"+blockNum+" = 0;\n";
        for(int i = 0;i < counter+1;i++) {
            System.out.print('\t');
            code += "\t";
        }
        System.out.println("block"+blockNum+" = "+blockNum+";");
        code += "block"+blockNum+" = "+blockNum+";\n";
        for(int i = 0;i < counter+1;i++) {
            System.out.print('\t');
            code += "\t";
        }
        System.out.println("set.add(block"+blockNum+");");
        code += "set.add(block"+blockNum+");\n";
        blockNum++;
        counter++;
    }

    @Override
    public void exitElseif(JavaParser.ElseifContext ctx) {
        counter--;
        for(int i = 0;i < counter;i++) {
            System.out.print("\t");
            code += "\t";
        }
        System.out.println("}");
        code += "}\n";
    }

/*
    @Override
    public void enterStatement(JavaParser.StatementContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        if(ctx.statement().size() == 2){f = false; e = true;}
        else if(ctx.statement().size() == 1){f = true; e = false;}
        List<Boolean> l = new ArrayList<Boolean>();
        l.add(f);
        l.add(e);
        s.push(l);
        if(ctx.IF() != null){
            counter++;
            for(int i = 0;i < counter+1;i++) System.out.print('\t');
            System.out.print("if");
            System.out.println(tokens.getText(ctx.parExpression())+"{");
        }

        if(ctx.expression().size() != 0 && !f && e){
            //System.out.println(tokens.getText(ctx.expression(0)));
            String str = "";
            if(!elseStack.empty()){
                for(int i = 0;i < counter+1;i++) System.out.print('\t');
                System.out.println(elseStack.pop());
            }
            for(JavaParser.ExpressionContext i:ctx.expression()){
                str += tokens.getText(i);
            }
            str += ";";
            elseStack.push(str);
        }
    }

    @Override
    public void exitStatement(JavaParser.StatementContext ctx){
        TokenStream tokens = parser.getTokenStream();
        List<Boolean> l = s.pop();
        f = l.get(0);
        e = l.get(1);

        if(ctx.expression().size() != 0 && f && !e){
            //System.out.println("if: "+f+", else: "+e);
            for(int i = 0;i < counter+2;i++) System.out.print('\t');
            for(JavaParser.ExpressionContext i:ctx.expression()){
                System.out.print(tokens.getText(i));
            }
            System.out.println(";");
        }

        if(ctx.IF() != null){
            for(int i = 0;i < counter+1;i++) System.out.print('\t');
            System.out.println('}');
            if(ctx.ELSE() != null){
                for(int i = 0;i < counter+1;i++) System.out.print('\t');
                System.out.println(ctx.ELSE()+"{");
                for(int i = 0;i < counter+2;i++) System.out.print('\t');
                System.out.println(elseStack.pop());
                for(int i = 0;i < counter+1;i++) System.out.print('\t');
                System.out.println('}');
            }
            counter--;
        }
    }
 */
}


