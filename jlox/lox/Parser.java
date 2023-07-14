package lox;

import java.util.ArrayList;
import java.util.List;

import static lox.TokenType.*;

class Parser {
    // parser consumes an input sequence of tokens

    private static class ParseError extends RuntimeException {
    }

    private final List<Token> tokens;
    private int current = 0; // points to next token

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }

        return statements;
    }

    /*
     * -------------
     * NOTES
     * -------------
     * Declarations can be variable delarations or statments since statments go
     * wherever declarations go. So when defining functions, for example, we
     * might say that they are a series of declarations which are really
     * variable declarations or normal statements.
     *
     * -------------
     * GRAMMAR RULES
     * -------------
     * 
     * program → declaration* EOF ;
     * -------------
     * STATEMENTS
     * -------------
     * declaration → varDecl | statement ;
     * 
     * statement → exprStmt | printStmt ;
     * 
     * exprStmt → expression ";" ;
     * 
     * printStmt → "print" expression ";" ;
     * 
     * varDecl → "var" IDENTIFIER ("=" expression)? ";" ;
     * 
     * -------------
     * EXPRESSIONS
     * -------------
     * expression → comma ;
     * 
     * comma → ternary ( "," ternary )* ;
     * 
     * assignment → IDENTIFIER "=" assignment | ternary
     * 
     * ternary → equality "?" ternary ":" ternary | equality;
     * 
     * equality → comparison ( ( "!=" | "==" ) comparison )* ;
     * 
     * comparison → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
     * 
     * term → factor ( ( "-" | "+" ) factor )* ;
     * 
     * factor → unary ( ( "/" | "*" ) unary )* ;
     * 
     * unary → ( "!" | "-" ) unary | primary ;
     * 
     * primary → NUMBER | STRING | "true" | "false" | "nil"
     * | "(" expression ")" | IDENTIFIER ;
     */

    private Expr expression() {
        return comma();
    }

    private Stmt declaration() {
        try {
            if (match(VAR))
                return varDeclaration();
            return statement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    private Stmt statement() {
        if (match(PRINT))
            return printStatement();

        return expressionStatement();
    }

    private Stmt varDeclaration() {
        Token name = consume(IDENTIFIER, "Expect variable name.");

        Expr initializer = null;
        if (match(EQUAL)) {
            initializer = expression();
        }

        consume(SEMICOLON, "Expect ';' after variable declaration");
        return new Stmt.Var(name, initializer);
    }

    private Stmt printStatement() {
        Expr value = expression();
        consume(SEMICOLON, "Expect ';' after value.");
        return new Stmt.Print(value);
    }

    private Stmt expressionStatement() {
        Expr value = expression();
        consume(SEMICOLON, "Expect ';' after value.");
        return new Stmt.Expression(value);
    }

    private Expr comma() {
        Expr expr = assignment();

        while (match(COMMA)) {
            Token operator = previous();
            Expr right = assignment();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr assignment() {
        Expr expr = ternary();

        if (match(EQUAL)) {
            Token equals = previous();
            Expr value = assignment();

            /*
             * we parse expr as if it is an r-value and then convert it to an
             * l value if it has an assignment operator after it.
             */
            if (expr instanceof Expr.Variable) {
                Token name = ((Expr.Variable) expr).name;
                return new Expr.Assign(name, value);
            }

            error(equals, "Invalid assignment target");
        }

        return expr;
    }

    private Expr ternary() {
        Expr expr = equality();

        if (match(QUESTION)) {
            Token op1 = previous();
            Expr mid = ternary();

            if (match(COLON)) {
                Token op2 = previous();
                Expr left = ternary();
                expr = new Expr.Ternary(expr, op1, mid, op2, left);
            } else {
                throw error(peek(), "Expect ':'");
            }
        }

        return expr;
    }

    private Expr equality() {
        // will be the first 'comparison' nonterminal
        Expr expr = comparison();

        // (...)* expression is the while loop
        while (match(BANG_EQUAL, EQUAL_EQUAL)) { //
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr comparison() {
        Expr expr = term();

        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr term() {
        Expr expr = factor();

        while (match(MINUS, PLUS)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr factor() {
        Expr expr = unary();

        while (match(SLASH, STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr unary() {
        if (match(BANG, MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }

        return primary();
    }

    private Expr primary() {
        if (match(FALSE))
            return new Expr.Literal(false);
        if (match(TRUE))
            return new Expr.Literal(true);
        if (match(NIL))
            return new Expr.Literal(null);

        if (match(NUMBER, STRING)) {
            return new Expr.Literal(previous().literal);
        }

        if (match(IDENTIFIER)) {
            return new Expr.Variable(previous());
        }

        if (match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }

        /*
         * We also could have done this individually in each rule by checking
         * to see if the rule's particular operators are before anything else.
         * 
         * But instead we are letting everything fall through to primary and
         * then addressing the error here.
         */
        Token token = peek();
        boolean errorProduction = true;

        // error productions for binary operator without left operands

        if (match(COMMA))
            comma();
        else if (match(EQUAL_EQUAL, BANG_EQUAL))
            equality();
        else if (match(LESS, LESS_EQUAL, GREATER, GREATER_EQUAL))
            comparison();
        else if (match(PLUS, MINUS))
            term();
        else if (match(STAR, SLASH))
            factor();
        else if (match(EQUAL))
            assignment();
        else
            errorProduction = false;

        if (errorProduction) {
            error(token, "Expect expression before '" + token.lexeme + "'");
            return null;
        }

        // TODO error productions for ternary operator

        throw error(token, "Expect expression");
    }

    private boolean match(TokenType... types) {
        /*
         * This is to see if the token that current points too has
         * any of the given types. The token will be consumed if
         * it has one of the specified types, and will return true.
         * Otherwise the function will return false and not consume
         * the token.
         */
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type))
            return advance();

        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        // returns if the current token is of a particular type and only peeks
        if (isAtEnd())
            return false;
        return peek().type == type;
    }

    private Token advance() {
        /*
         * advance is the only operation which increases current, so once EOF is reached
         * current is fixed.
         */
        if (!isAtEnd())
            current++;
        return previous();
    }

    // the last token will be EOF
    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private ParseError error(Token token, String message) {
        Lox.error(token, message);
        return new ParseError();
    }

    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().type == SEMICOLON)
                return;

            switch (peek().type) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
                default:
                    break;
            }

            advance();
        }
    }

}