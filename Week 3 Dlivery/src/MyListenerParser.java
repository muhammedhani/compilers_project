import org.antlr.v4.runtime.TokenStream;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/***************************************************************************//**
 * This class extends JavaParserBaseListener, which is a java parser
 * listener which walks on all nodes of the parsing table,
 * It's used for injecting a java code in the parsed code to detect
 * the executed branches.
 ******************************************************************************/
public class MyListenerParser extends JavaParserBaseListener {

    public boolean ishtml = false; /*!< to know if the tree is being walked in the first time or the second time  */
    private String code = "";      /*!< The generated Java code is concatenated using this string */
    private String html =          /*!< The generated HTML code is concatenated using this string*/
           """   
           <!DOCTYPE html>
           <html>
               <head>
                   <title>Compilers Project</title>
                   <meta charset = "UTF-8">
                      <meta name = "description" content = "Compilers Project: Which Blocks and visited?!">
                      <style>
                       .green {
                           background-color: #90ee90;
                       }
                       .red {
                           background-color: #ffa09e;
                       }
                   </style>
                      <script></script>
               </head>
               <body>
                   <pre>
               """;
    private JavaParser parser;  /*!< opject used to call getTokenStream method from JavaParser class */
    private int counter = 0;    /*!< Used to make the indentation of the generated code */
    private int blockNum = 0;   /*!< Used to count the branches while walking the parse tree */
    private int index = 0;      /*!< Used to iterate over the stored visited branches */
    private boolean breakNotSwitch = false;
    private List<Integer> ints ;/*!< Used to get the visited branches from visited_blocks file */

    /***************************************************************************//**
     * fn MyListenerParser(JavaParser parser)
     * The Consturtor of the class
     *
     * This initializes the parser while making a JavaParser instance
     * @param parser
     ******************************************************************************/
    public MyListenerParser(JavaParser parser) {this.parser = parser;}

    /***************************************************************************//**
     * Used to write the generated java code to Output.java file
     * @param string writes it to the output file
     ******************************************************************************/
    public void write(String string) throws Exception {
        String outputFileName = "Output.java";
        FileOutputStream outputFile = new FileOutputStream(outputFileName, false);
        BufferedOutputStream buffer = new BufferedOutputStream(outputFile);
        // buffer can only write with characters
        byte[] bytes = string.getBytes();
        buffer.write(bytes);
        buffer.close();
    }

    /***************************************************************************//**
     * Used to read the numbers of visited blocks from visited_blocks.txt file
     ******************************************************************************/
    public List read() throws IOException {
        List<Integer> list = Files.lines(Paths.get("visited_blocks.txt"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        return list;
    }

    /***************************************************************************//**
     * Used to write HTML code to index.html file
     * @param string writes it to the output file
     ******************************************************************************/
   public void write_html(String string) throws Exception {
        String outputFileName = "index.html";
        FileOutputStream outputFile = new FileOutputStream(outputFileName, false);
        BufferedOutputStream buffer = new BufferedOutputStream(outputFile);
        // buffer can only write with characters
        byte[] bytes = string.getBytes();
        buffer.write(bytes);
        buffer.close();
    }

    /***************************************************************************//**
     * It's the first function to be called when the parser begins to parse rules
     *
     * Used to handle "compilationUnit" parser rule
     *
     * It also adds some imports, which are later used, to the generated file
     * @param ctx CompilationUnitContext. The function uses it to get the text of
     *            it's children: packageDeclaration, importDeclaration
     ******************************************************************************/
    @Override
    public void enterCompilationUnit(JavaParser.CompilationUnitContext ctx) {
        if(ishtml){
            html += "<div class = \"green\">";
        }

        TokenStream tokens = parser.getTokenStream();
        if(ctx.packageDeclaration() != null) {
            System.out.println(tokens.getText(ctx.packageDeclaration()));
            if(!ishtml) {
                code += tokens.getText(ctx.packageDeclaration()) + "\n";
            } else {
                html += tokens.getText(ctx.packageDeclaration()) + "\n";
            }
        }
        if(ctx.importDeclaration().size() != 0) {
            for(JavaParser.ImportDeclarationContext i: ctx.importDeclaration()) {
                System.out.println(tokens.getText(i));
                if(!ishtml) {
                    code += tokens.getText(i) + "\n";
                } else {
                    html += tokens.getText(i) + "\n";
                }
            }
        }
        System.out.println("import java.util.*;");
        if(!ishtml) {
            code += "import java.util.*;\n" +
                    "import java.io.BufferedOutputStream;\n" +
                    "import java.io.FileOutputStream;\n";
        }
        // to read the generated file of visited-block numbers...
        try {
            ints = read();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /***************************************************************************//**
     * Used to finally write the total string, which contains all the code to
     * be written
     *
     * It also resets the blockNum to zero (for html string)
     * @param ctx CompilationUnitContext.
     ******************************************************************************/
    @Override
    public void exitCompilationUnit(JavaParser.CompilationUnitContext ctx) {
        try {
            if(!ishtml) {
                write(code);
            }
            else {
                write_html(html);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        blockNum = 0;
    }

    /***************************************************************************//**
     * Used to handle the "typeDeclaration" parser rule
     *
     * It appends it to both strings; code, html that are going to be written
     * @param ctx TypeDeclarationContext. The function uses it to get the text of
     *            its children: classOrInterfaceModifier
     ******************************************************************************/
    @Override
    public void enterTypeDeclaration(JavaParser.TypeDeclarationContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        if(ctx.classOrInterfaceModifier().size() != 0 ){
            for (JavaParser.ClassOrInterfaceModifierContext i:ctx.classOrInterfaceModifier()) {
                System.out.print(tokens.getText(i)+" ");
                if(!ishtml) {
                    code += tokens.getText(i)+" ";
                } else {
                    html += tokens.getText(i)+" ";
                }
            }
        }
        counter = 0;
    }

    /***************************************************************************//**
     * Used to handle the "classDeclaration" parser rule
     *
     * It also adds the function, that is used to generate the text file of
     * the visited blocks, before starting the main program
     * @param ctx ClassDeclarationContext. The function uses it to get the text of
     *            its children: typeParameters, typeType, typeList
     ******************************************************************************/
    @Override
    public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        String id = "";
        if(ctx.identifier() != null){
            id = tokens.getText(ctx.identifier());
        }
        System.out.print("class "+id);
        if(!ishtml) {
            code += "class "+id;
        } else {
            html += "class "+id;
        }
        if(ctx.typeParameters() != null) {
            System.out.print(" "+tokens.getText(ctx.typeParameters()));
            if(!ishtml) {
                code += " "+tokens.getText(ctx.typeParameters());
            } else {
                html += " "+tokens.getText(ctx.typeParameters());
            }
        }
        if(ctx.typeType() != null) {
            System.out.print(" extends"+tokens.getText(ctx.typeType()));
            if(!ishtml) {
                code += " extends"+tokens.getText(ctx.typeType());
            } else {
                html += " extends"+tokens.getText(ctx.typeType());
            }
        }
        if(ctx.typeList().size() != 0) {
            System.out.print(" implements");
            if(!ishtml) {
                code += " implements";
            } else {
                html += " implements";
            }
            for (JavaParser.TypeListContext i: ctx.typeList() ) {
                System.out.print(" "+tokens.getText(i));
                if(!ishtml) {
                    code += " "+tokens.getText(i);
                } else {
                    html += " "+tokens.getText(i);
                }
            }
        }
        System.out.println(" {");
        if(!ishtml) {
            code += " {\n";
        } else {
            html += " {";
        }
        for(int i = 0;i < counter+1;i++) {
            System.out.print('\t');
            code += "\t";
        }

        System.out.println("static Set<Integer> set = new TreeSet<>();");
        code += "static Set<Integer> set = new TreeSet<>();\n";
        code += """
                \tstatic void __write(String string) throws Exception {
                    \tString outputFileName = "visited_blocks.txt";
                    \tFileOutputStream outputFile = new FileOutputStream(outputFileName, false);
                    \tBufferedOutputStream buffer = new BufferedOutputStream(outputFile);
                    \t// buffer can only write with characters
                    \tbyte[] bytes = string.getBytes();
                    \tbuffer.write(bytes);
                    \tbuffer.close();
                \t}\n""";
        counter++;
    }

    /***************************************************************************//**
     * Used to handle what to do before exiting the "classDeclaration" parser rule
     *
     * It also appends some html tags for the html string to be written to
     * index.html
     * @param ctx ClassDeclarationContext.
     ******************************************************************************/
    @Override
    public void exitClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        counter--;
        System.out.println("}");
        if(!ishtml) {
            code += "}\n";
        } else {
            html += "}";
            html += """
                        </div>
                    </pre>
                </body>
            </html>""";
        }

    }

    /***************************************************************************//**
     * Used to handle the "classBodyDeclaration" parser rule
     *
     * @param ctx ClassBodyDeclarationContext. The function uses it to get the
     *            text of its children: STATIC, modifier
     ******************************************************************************/
    @Override
    public void enterClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        blockNum++;
        if(ishtml){
            if(index < ints.size() && ints.get(index) == blockNum){
                html += "<div class = \"green\">";
                index++;
            }
            else {
                html += "<div class = \"red\">";
            }
        }
        for(int i = 0;i < counter;i++) {
            System.out.print('\t');
            if(!ishtml) {
                code += "\t";
            } else {
                html += "\t";
            }
        }
        if(ctx.STATIC() != null) {
            System.out.print("static ");
            if(!ishtml) {
                code += "static ";
            } else {
                html += "static ";
            }
        }
        if(ctx.modifier().size() != 0){
            for(JavaParser.ModifierContext i: ctx.modifier()){
                System.out.print(tokens.getText(i)+" ");
                if(!ishtml) {
                    code += tokens.getText(i) + " ";
                } else {
                    html += tokens.getText(i) + " ";
                }
            }
        }
        counter++;
    }

    /***************************************************************************//**
     * Used to handle what to do before exiting the "classBodyDeclaration" parser rule
     *
     * @param ctx ClassBodyDeclarationContext.
     ******************************************************************************/
    @Override
    public void exitClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
        counter--;
        for(int i = 0;i < counter;i++) {
            System.out.print("\t");
            if(!ishtml) {
                code += "\t";
            } else {
                html += "\t";
            }
        }
        System.out.println("}");
        if(!ishtml) {
            code += "}\n";
        } else {
            html += "}</div>";
        }

    }

    /***************************************************************************//**
     * Used to handle the "methodDeclaration" parser rule
     *
     * It also appends some useful code for recognizing the visited and non-visited
     * blocks
     * @param ctx MethodDeclarationContext. The function uses it to get the text of
     *            its children: typeTypeOrVoid, identifier, formalParameters,
     *            THROWS, qualifiedNameList
     ******************************************************************************/
    @Override
    public void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        TokenStream tokens = parser.getTokenStream();

        if(ctx.typeTypeOrVoid() != null) {
            System.out.print(tokens.getText(ctx.typeTypeOrVoid())+" ");
            if(!ishtml) {
                code += tokens.getText(ctx.typeTypeOrVoid())+" ";
            } else {
                html += tokens.getText(ctx.typeTypeOrVoid())+" ";
            }
        }
        if(ctx.identifier() != null) {
            System.out.print(tokens.getText(ctx.identifier())+" ");
            if(!ishtml) {
                code += tokens.getText(ctx.identifier())+" ";
            } else {
                html += tokens.getText(ctx.identifier())+" ";
            }
        }
        if(ctx.formalParameters() != null) {
            System.out.print(tokens.getText(ctx.formalParameters())+" ");
            if(!ishtml) {
                code += tokens.getText(ctx.formalParameters())+" ";
            } else {
                html += tokens.getText(ctx.formalParameters())+" ";
            }
        }
        if(ctx.THROWS() != null) {
            System.out.print(ctx.THROWS()+" Exception");
            if(!ishtml) {
                code += ctx.THROWS()+" Exception";
            } else {
                html += ctx.THROWS()+" Exception";
            }
        }
        else {
            System.out.print("throws Exception");
            if(!ishtml) {
                code += "throws Exception";
            }
        }
        if(ctx.qualifiedNameList() != null) {
            System.out.print(tokens.getText(ctx.qualifiedNameList())+" ");
            if(!ishtml) {
                code += tokens.getText(ctx.qualifiedNameList())+" ";
            } else {
                html += tokens.getText(ctx.qualifiedNameList())+" ";
            }
        }
        System.out.println("{");
        if(!ishtml) {
            code += "{\n";
        } else {
            html += "{\n";
        }
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
    }

    /***************************************************************************//**
     * Used to handle what to do before exiting the "methodDeclaration" parser rule
     *
     * It also adds some useful code for appending the index of visited blocks to
     * the set "set" which we defined earlier (watch enterClassDeclaration)
     *
     * Also, there is a string called "visited" defined to be written to the text
     * file, it's being appended here
     * @param ctx MethodDeclarationContext.
     ******************************************************************************/
    @Override
    public void exitMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        if(ctx.identifier().getText().equals("main")) {
            for(int i = 0;i < counter;i++) {
                System.out.print('\t');
                code += "\t";
            }
            System.out.println("String visited = \"\";");
            code += "String visited = \"\";\n";
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
            System.out.println("System.out.println(\"Block#\"+i+\" is visited\");");
            code += "System.out.println(\"Block#\"+i+\" is visited\");\n";
            for(int i = 0;i < counter+1;i++) {
                System.out.print('\t');
                code += "\t";
            }
            System.out.println("visited += \"Block#\"+i+\" is visited\\n\";");
            code += "visited += i+\"\\n\";\n";
            for(int i = 0;i < counter;i++) {
                System.out.print('\t');
                code += "\t";
            }
            System.out.println("}");
            code += "}\n";
            for(int i = 0;i < counter;i++) {
                System.out.print('\t');
                code += "\t";
            }
            System.out.println("__write(visited);");
            code += "__write(visited);\n";
        }
    }

    /***************************************************************************//**
     * Used to handle the "blockStatement" parser rule
     *
     * @param ctx BlockStatementContext. The function uses it to get the text of
     *            its children: localVariableDeclaration, localTypeDeclaration
     ******************************************************************************/
    @Override
    public void enterBlockStatement(JavaParser.BlockStatementContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        if(ctx.localVariableDeclaration() != null) {
            for(int i = 0;i < counter;i++) {
                System.out.print('\t');
                code += "\t";
                if(ishtml)html += "\t";
            }
            System.out.println(tokens.getText(ctx.localVariableDeclaration())+";");
            if(!ishtml) {
                code += tokens.getText(ctx.localVariableDeclaration())+";\n";
            } else {
                html += tokens.getText(ctx.localVariableDeclaration())+";\n";
            }
        }
        if(ctx.localTypeDeclaration() != null) {
            for(int i = 0;i < counter;i++) {
                System.out.print('\t');
                code += "\t";
                if(ishtml)html += "\t";
            }
            System.out.println(tokens.getText(ctx.localTypeDeclaration()));
            if(!ishtml) {
                code += tokens.getText(ctx.localTypeDeclaration()) + "\n";
            } else {
                html += tokens.getText(ctx.localTypeDeclaration()) + "\n";
            }
        }
    }

    /***************************************************************************//**
     * Used to handle the "statement" parser rule
     *
     * It adds some html code; defines the class of the "div" to be
     * green (visited block) or red (non-visited block)
     * @param ctx StatementContext. The function uses it to get the text of
     *            its children: FOR, WHILE, DO, SWITCH, BREAK, "expression"
     ******************************************************************************/
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
                    if(ishtml)html += "\t";
                }
                System.out.println("for("+tokens.getText(ctx.forControl())+"){");
                if(!ishtml) {
                    code += "for("+tokens.getText(ctx.forControl())+"){\n";
                } else {
                    html += "for("+tokens.getText(ctx.forControl())+"){\n";
                }
                if(ishtml) {
                    if(index < ints.size() && ints.get(index) == blockNum){
                        html += "<div class = \"green\">";
                        index++;
                    }
                    else {
                        html += "<div class = \"red\">";
                    }
                }
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
                    if(ishtml)html += "\t";
                }
                System.out.println(ctx.WHILE()+tokens.getText(ctx.parExpression())+"{");
                if(!ishtml) {
                    code += ctx.WHILE()+tokens.getText(ctx.parExpression())+"{\n";
                } else {
                    html += ctx.WHILE()+tokens.getText(ctx.parExpression())+"{\n";
                }
                if(ishtml) {
                    if(index < ints.size() && ints.get(index) == blockNum){
                        html += "<div class = \"green\">";
                        index++;
                    }
                    else {
                        html += "<div class = \"red\">";
                    }
                }
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
                    if(ishtml)html += "\t";
                }
                System.out.println(ctx.DO()+"{");
                if(!ishtml) {
                    code += ctx.DO()+" {\n";
                } else {
                    html += ctx.DO()+" {\n";
                }
                if(ishtml){
                    if(index < ints.size() && ints.get(index) == blockNum){
                        html += "<div class = \"green\">";
                        index++;
                    }
                    else {
                        html += "<div class = \"red\">";
                    }
                }
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
            case "switch":
                for(int i = 0;i < counter;i++){
                    System.out.print('\t');
                    code += "\t";
                    if(ishtml)html += "\t";
                }
                System.out.println(ctx.SWITCH()+tokens.getText(ctx.parExpression())+"{");
                if(!ishtml) code += ctx.SWITCH()+tokens.getText(ctx.parExpression())+"{\n";
                else html += ctx.SWITCH()+tokens.getText(ctx.parExpression())+"{\n";
                if(ishtml){
                    if(index < ints.size() && ints.get(index) == blockNum){
                        html += "<div class = \"green\">";
                        index++;
                    }
                    else {
                        html += "<div class = \"red\">";
                    }
                }
                counter++;
                break;
            case "break":
                counter--;
                for(int i = 0;i < counter;i++){
                    System.out.print('\t');
                    code += "\t";
                    if(ishtml)html += "\t";
                }
                System.out.println(ctx.BREAK()+";");
                if(!ishtml)code += "break;\n";
                else {
                    html += "break;\n";
                    html += "</div>";
                }
                break;
            default:
                if(ctx.expression().size() != 0){
                    for(int i = 0;i < counter;i++) {
                        System.out.print("\t");
                        code += "\t";
                        if(ishtml)html += "\t";
                    }
                    for(JavaParser.ExpressionContext i:ctx.expression()){
                        System.out.print(tokens.getText(i));
                        if(!ishtml) {
                            code += tokens.getText(i);
                        } else {
                            html += tokens.getText(i);
                        }
                    }
                    System.out.println(";");
                    if(!ishtml) {
                        code += ";\n";
                    } else {
                        html += ";\n";
                    }
                }
                break;
        }

    }

    /***************************************************************************//**
     * Used to handle the "switchBlockStatementGroup" parser rule
     *
     * It appends some useful code for generating the text file of
     * the visited blocks
     * @param ctx SwitchBlockStatementGroupContext. The function uses it to get
     *            the text of its children: switchLabel
     ******************************************************************************/
    @Override
    public void enterSwitchBlockStatementGroup(JavaParser.SwitchBlockStatementGroupContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        if(ishtml){
            if(index < ints.size() && ints.get(index) == blockNum){
                html += "<div class = \"green\">";
                index++;
            }
            else {
                html += "<div class = \"red\">";
            }
        }
        for(JavaParser.SwitchLabelContext i : ctx.switchLabel()){
            for(int j = 0;j < counter;j++) {
                System.out.print("\t");
                if(!ishtml) {
                    code += "\t";
                } else {
                    html += "\t";
                }
            }
            System.out.println(tokens.getText(i));
            if(!ishtml) {
                code += tokens.getText(i)+" \n";
            } else {
                html += tokens.getText(i)+" \n";
            }
            for(int j = 0;j < counter;j++) {
                System.out.print('\t');
                code += "\t";
            }
            System.out.println("int block"+blockNum+" = 0;");
            code += "int block"+blockNum+" = 0;\n";
            for(int j = 0;j < counter;j++) {
                System.out.print('\t');
                code += "\t";
            }
            System.out.println("block"+blockNum+" = "+blockNum+";");
            code += "block"+blockNum+" = "+blockNum+";\n";
            for(int j = 0;j < counter;j++) {
                System.out.print('\t');
                code += "\t";
            }
            System.out.println("set.add(block"+blockNum+");");
            code += "set.add(block"+blockNum+");\n";
            blockNum++;
            counter++;
        }
    }

    /***************************************************************************//**
     * Used to handle what to do before exiting the "statement" parser rule
     *
     * @param ctx StatementContext.
     ******************************************************************************/
    @Override
    public void exitStatement(JavaParser.StatementContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        switch (ctx.getChild(0).getText()) {
            case "for":
            case "while":
                counter--;
                for (int i = 0; i < counter; i++) {
                    System.out.print("\t");
                    code += "\t";
                    if(ishtml)html += "\t";
                }
                System.out.println("}");
                if(!ishtml) {
                    code += "}\n";
                } else {
                    html += "}</div>";
                }
                break;
            case "do":
                counter--;
                for (int i = 0; i < counter; i++) {
                    System.out.print("\t");
                    code += "\t";
                    if(ishtml)html += "\t";
                }
                System.out.println("} while" + tokens.getText(ctx.parExpression()) + ";");
                if(!ishtml) {
                    code += "} while" + tokens.getText(ctx.parExpression()) + ";\n";
                } else {
                    html += "} while" + tokens.getText(ctx.parExpression()) + ";</div>";
                }
                break;
            case "switch":
                counter--;
                for (int i = 0;i < counter; i++){
                    System.out.print("\t");
                    code += "\t";
                    if(ishtml)html += "\t";
                }
                System.out.println("}");
                code += "}\n";
                if(ishtml)html += "}</div>";
                break;
        }

    }

    /***************************************************************************//**
     * Used to handle the "ifbranch" parser rule
     *
     * @param ctx IfbranchContext. The function uses it to get the text of
     *            its children: IF, "parExpression"
     ******************************************************************************/
    @Override
    public void enterIfbranch(JavaParser.IfbranchContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        for(int i = 0;i < counter;i++) {
            System.out.print('\t');
            code += "\t";
            if(ishtml)html += "\t";
        }
        System.out.println("int block"+blockNum+" = 0;");
        code += "int block"+blockNum+" = 0;\n";
        for(int i = 0;i < counter;i++) {
            System.out.print("\t");
            code += "\t";
        }
        System.out.print(ctx.IF());
        if(!ishtml) {
            code += ctx.IF();
        } else {
            html += ctx.IF();
        }
        System.out.println(tokens.getText(ctx.parExpression())+"{");
        if(!ishtml) {
            code += tokens.getText(ctx.parExpression())+"{\n";
        } else {
            html += tokens.getText(ctx.parExpression())+"{\n";
        }
        if(ishtml) {
            if(index < ints.size() && ints.get(index) == blockNum){
                html += "<div class = \"green\">";
                index++;
            }
            else {
                html += "<div class = \"red\">";
            }
        }
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

    /***************************************************************************//**
     * Used to handle what to do before exiting the "ifbranch" parser rule
     *
     * @param ctx IfbranchContext.
     ******************************************************************************/
    @Override
    public void exitIfbranch(JavaParser.IfbranchContext ctx) {
        counter--;
        for(int i = 0;i < counter;i++) {
            System.out.print("\t");
            code += "\t";
            if(ishtml)html += "\t";
        }
        System.out.println("}");
        if(!ishtml) {
            code += "}\n";
        } else {
            html += "}</div>";
        }
    }

    /***************************************************************************//**
     * Used to handle the "esleif" parser rule
     *
     * @param ctx ElseifContext. The function uses it to get the text of
     *            its children: ELSE
     ******************************************************************************/
    @Override
    public void enterElseif(JavaParser.ElseifContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        if(ishtml) {
            if(index < ints.size() && ints.get(index) == blockNum){
                html += "<div class = \"green\">";
                index++;
            }
            else {
                html += "<div class = \"red\">";
            }
        }
        for(int i = 0;i < counter;i++) {
            System.out.print("\t");
            code += "\t";
            if(ishtml)html += "\t";
        }
        System.out.println(ctx.ELSE()+"{");
        if(!ishtml) {
            code += ctx.ELSE()+" {\n";
        } else {
            html += ctx.ELSE()+" {\n";
        }
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

    /***************************************************************************//**
     * Used to handle what to do before exiting the "elseif" parser rule
     *
     * @param ctx ElseifContext.
     ******************************************************************************/
    @Override
    public void exitElseif(JavaParser.ElseifContext ctx) {
        counter--;
        for(int i = 0;i < counter;i++) {
            System.out.print("\t");
            if(!ishtml) {
                code += "\t";
            } else {
                html += "\t";
            }
        }
        System.out.println("}");
        if(!ishtml) {
            code += "}\n";
        } else {
            html += "}</div>";
        }
    }
}


