package lox;

class AstRpnPrinter implements Expr.Visitor<String> {

    // Now all these visitor functions are accessed via printRpn
    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return rpn(expr.name.lexeme + " =", expr.value);
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return expr.name.lexeme;
    }

    @Override
    public String visitTernaryExpr(Expr.Ternary expr) {
        return rpn("?:", expr.left, expr.mid, expr.right);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return rpn(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return rpn("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null)
            return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return rpn(expr.operator.lexeme, expr.right);
    }

    private String rpn(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        for (Expr expr : exprs) {
            builder.append(expr.accept(this));
            builder.append(" ");
        }
        builder.append(name);

        return builder.toString();
    }
}
