import java.util.HashMap;
import java.util.Map;

public class MyJava extends JavaBaseVisitor<Integer> {
    Map<String, Integer> mp = new HashMap<>();

    // printStatement
    @Override
    public Integer visitPrintStatement(JavaParser.PrintStatementContext ctx) {
        int intResult = visit(ctx.expr());
        System.out.println("Visited my print statement...");
        System.out.println(intResult);
        return 0;
    }

    // intExpr
    @Override
    public Integer visitIntExpr(JavaParser.IntExprContext ctx) {
        int intValue = Integer.parseInt(ctx.INT().getText());
        return intValue;
    }

    // addSubExpr
    @Override
    public Integer visitAddSubExpr(JavaParser.AddSubExprContext ctx) {
        int firstExpr = visit(ctx.expr(0));
        int secondExpr = visit(ctx.expr(1));
        String operation = ctx.operator.getText();
        if(operation.equals("+")) {
            return firstExpr + secondExpr;
        }
        else {
            return firstExpr - secondExpr;
        }
    }

    // mulDivExpr
    @Override
    public Integer visitMulDivExpr(JavaParser.MulDivExprContext ctx) {
        int firstExpr = visit(ctx.expr(0));
        int secondExpr = visit(ctx.expr(1));
        String operation = ctx.operator.getText();
        if(operation.equals("*")) {
            return firstExpr * secondExpr;
        }
        else {
            return firstExpr / secondExpr;
        }
    }

    // assignStatement
    @Override
    public Integer visitAssignment(JavaParser.AssignmentContext ctx) {
        String var = ctx.VAR().getText();
        int exprIntVal = visit(ctx.expr());
        mp.put(var, exprIntVal);

        return exprIntVal;
    }

    // varExpr
    @Override
    public Integer visitVarExpr(JavaParser.VarExprContext ctx) {
        String var = ctx.VAR().getText();
        return mp.get(var);
    }
}
