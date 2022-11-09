package parser.services;

import java.io.FileNotFoundException;
import lexer.models.*;
import lexer.services.*;

public class ParserService {

    private final LexerService lexer;
    private Token token;
    private boolean isEOF;
    private ErrorService errorService;

    /**
     * @param filename
     * @throws FileNotFoundException
     */
    public ParserService(String filename) throws FileNotFoundException {
        lexer = new LexerService(filename);
        errorService = new ErrorService();
        this.isEOF = false;
    }

    private void advance() {
        try {
            // lê o arquivo
            token = lexer.scan();
            if (token.getTag() == Tag.EOF) {
                setEOF(true);
            }
            // se o int do tokenen do scan for igual ao int da Tag
            if (token.getTag() == Tag.COMMENT_LINE || token.getTag() == Tag.COMMENT_BLOCK || token.tag == Tag.ERROR) {
                advance();
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public boolean isEOF() {
        return isEOF;
    }

    public void setEOF(boolean isEOF) {
        this.isEOF = isEOF;
    }

    private boolean eat(int tag) {
        if (token.getTag() == tag) {
            advance();
            return true;
        }
        return false;
    }

    public void start() {
        System.out.println("Iniciando Análise Sintática ...");
        advance();
        program();
        System.out.println("Finalizando Análise Sintática ...");

    }

    // program ::= start [decl-list] stmt-list exit
    public boolean program() {
        if (!eat(Tag.START)) {
            errorService.showError(lexer.line, "program");
            return false;
        }

        if (token.getTag() == Tag.INT || token.getTag() == Tag.STRING || token.getTag() == Tag.FLOAT) {
            declList();
        }

        stmtList();

        return eat(Tag.EXIT);
    }

    // decl-list ::= decl {decl}
    public boolean declList() {
        if (token.getTag() == Tag.INT ||
                token.getTag() == Tag.STRING ||
                token.getTag() == Tag.FLOAT) {
            do {
                decl();
            } while (token.getTag() == Tag.INT ||
                    token.getTag() == Tag.STRING ||
                    token.getTag() == Tag.FLOAT);
        } else {
            errorService.showError(lexer.line, "decl-list");
        }

        return true;
    }

    // decl ::= type ident-list ";"
    public boolean decl() {
        if (token.getTag() == Tag.INT ||
                token.getTag() == Tag.STRING ||
                token.getTag() == Tag.FLOAT) {
            type();
            identList();
            return eat(Tag.DOT_COMMA);
        } else {
            errorService.showError(lexer.line, "decl");
        }

        return true;
    }

    // ident-list ::= identifier {"," identifier}
    public boolean identList() {
        if (token.getTag() == Tag.IDENTIFIER) {
            identifier();
            while (token.getTag() == Tag.COMMA) {
                if (!eat(Tag.COMMA)) {
                    return false;
                }
                identifier();
            }
        } else {
            errorService.showError(lexer.line, "ident-list");
        }

        return true;
    }

    // type ::= int | float | string
    public boolean type() {
        if (token.getTag() == Tag.INT) {
            return eat(Tag.INT);
        } else if (token.getTag() == Tag.STRING) {
            return eat(Tag.STRING);
        } else if (token.getTag() == Tag.FLOAT) {
            return eat(Tag.FLOAT);
        } else {
            errorService.showError(lexer.line, "type");
        }
        return true;
    }

    // stmt-list ::= stmt {stmt}
    public boolean stmtList() {
        if (token.getTag() == Tag.IDENTIFIER ||
                token.getTag() == Tag.IF ||
                token.getTag() == Tag.DO ||
                token.getTag() == Tag.SCAN ||
                token.getTag() == Tag.PRINT) {
            do {
                stmt();
            } while (token.getTag() == Tag.IDENTIFIER ||
                    token.getTag() == Tag.IF ||
                    token.getTag() == Tag.DO ||
                    token.getTag() == Tag.SCAN ||
                    token.getTag() == Tag.PRINT);
        } else {
            errorService.showError(lexer.line, "stmt-list");
        }
        return true;
    }

    // stmt ::= assign-stmt ";" | if-stmt | while-stmt | read-stmt ";" | write-stmt
    // ";"
    public boolean stmt() {
        if (token.getTag() == Tag.IDENTIFIER) {
            assignStmt();
            return eat(Tag.DOT_COMMA);
        } else if (token.getTag() == Tag.IF) {
            ifStmt();
        } else if (token.getTag() == Tag.WHILE) {
            whileStmt();
        } else if (token.getTag() == Tag.SCAN) {
            readStmt();
            return eat(Tag.DOT_COMMA);
        } else if (token.getTag() == Tag.PRINT) {
            writeStmt();
            return eat(Tag.DOT_COMMA);
        } else {
            errorService.showError(lexer.line, "stmt");
        }
        return true;
    }

    // assign-stmt ::= identifier "=" simple_expr
    public boolean assignStmt() {
        if (token.getTag() == Tag.IDENTIFIER) {
            identifier();
            if (!eat(Tag.ASSIGN)) {
                return false;
            }
            simpleExpr();
        } else {
            errorService.showError(lexer.line, "assign-stmt");
        }
        return true;
    }

    // if-stmt​ ​::=​ ​if​ ​condition​ ​then​ ​stmt-list​ ​if-stmt’
    public boolean ifStmt() {
        if (token.getTag() == Tag.IF) {
            if (!eat(Tag.IF)) {
                return false;
            }
            condition();
            if (!eat(Tag.THEN)) {
                return false;
            }
            stmtList();
            ifStmtPrime();
        } else {
            errorService.showError(lexer.line, "if-stmt");
        }
        return true;
    }

    // if-stmt’​ ​::=​ ​end​ ​|​ ​else​ ​stmt-list​ ​end
    public boolean ifStmtPrime() {
        if (token.getTag() == Tag.END) {
            if (!eat(Tag.END)) {
                return false;
            }
        } else if (token.getTag() == Tag.ELSE) {
            if (!eat(Tag.ELSE)) {
                return false;
            }
            stmtList();
            if (token.getTag() == Tag.END) {
                if (!eat(Tag.END)) {
                    return false;
                }
            }
        } else {
            errorService.showError(lexer.line, "if-stmt'");
        }

        return true;
    }

    // condition ::= expression
    public boolean condition() {
        if (token.getTag() == Tag.IDENTIFIER ||
                token.getTag() == Tag.OPEN_PARENTHESES ||
                token.getTag() == Tag.NOT ||
                token.getTag() == Tag.MINUS ||
                token.getTag() == Tag.PLUS ||
                token.getTag() == Tag.DIGIT ||
                token.getTag() == Tag.FLOAT_CONST ||
                token.getTag() == Tag.LITERAL ||
                token.getTag() == Tag.OR) {
            expression();
        } else {
            errorService.showError(lexer.line, "condition");
        }

        return true;
    }

    // while-stmt ::= do stmt-list stmt-sufix
    public boolean whileStmt() {
        if (token.getTag() == Tag.DO) {
            if (!eat(Tag.DO)) {
                return false;
            }
            stmtList();
            stmtSufix();
        } else {
            errorService.showError(lexer.line, "while-stmt");
        }
        return true;
    }

    // stmt-sufix ::= while condition end
    public boolean stmtSufix() {
        if (token.getTag() == Tag.WHILE) {
            if (!eat(Tag.WHILE)) {
                return false;
            }
            condition();
            return eat(Tag.END);
        } else {
            errorService.showError(lexer.line, "stmt-sufix");
        }
        return true;
    }

    // read-stmt ::= scan "(" identifier ")"
    public boolean readStmt() {
        if (token.getTag() == Tag.SCAN) {
            if (!eat(Tag.SCAN)) {
                return false;
            }
            if (!eat(Tag.OPEN_PARENTHESES)) {
                return false;
            }
            identifier();
            return eat(Tag.CLOSE_PARENTHESES);
        } else {
            errorService.showError(lexer.line, "read-stmt");
        }
        return true;
    }

    // write-stmt ::= print "(" writable ")"
    public boolean writeStmt() {
        if (token.getTag() == Tag.PRINT) {
            if (!eat(Tag.PRINT)) {
                return false;
            }
            if (!eat(Tag.OPEN_PARENTHESES)) {
                return false;
            }
            writable();
            return eat(Tag.CLOSE_PARENTHESES);
        } else {
            errorService.showError(lexer.line, "write-stmt");
        }
        return true;
    }

    // writable ::= simple-expr | literal
    public boolean writable() {
        if (token.getTag() == Tag.IDENTIFIER ||
                token.getTag() == Tag.DIGIT ||
                token.getTag() == Tag.FLOAT_CONST ||
                token.getTag() == Tag.OPEN_PARENTHESES ||
                token.getTag() == Tag.NOT ||
                token.getTag() == Tag.MINUS) {
            simpleExpr();
        } else if (token.getTag() == Tag.LITERAL) {
            literal();
        } else {
            errorService.showError(lexer.line, "writable");
        }
        return true;
    }

    // expression​ ​::=​ ​simple-expr​ ​expression’
    public boolean expression() {
        if (token.getTag() == Tag.IDENTIFIER ||
                token.getTag() == Tag.DIGIT ||
                token.getTag() == Tag.FLOAT_CONST ||
                token.getTag() == Tag.OPEN_PARENTHESES ||
                token.getTag() == Tag.NOT ||
                token.getTag() == Tag.OR ||
                token.getTag() == Tag.MINUS) {
            simpleExpr();
            expressionPrime();
        } else {
            errorService.showError(lexer.line, "expression​");
        }
        return true;
    }

    // expression’​ ​::=​ ​ ​relop​ ​simple-expr​ ​|​ ​𝛌
    public boolean expressionPrime() {
        if (token.getTag() == Tag.GT ||
                token.getTag() == Tag.EQ ||
                token.getTag() == Tag.GE ||
                token.getTag() == Tag.LT ||
                token.getTag() == Tag.LE ||
                token.getTag() == Tag.NOT) {
            relop();
            simpleExpr();
        } else if (token.getTag() == Tag.THEN ||
                token.getTag() == Tag.END ||
                token.getTag() == Tag.CLOSE_PARENTHESES) {

        } else {
            errorService.showError(lexer.line, "expression'");
        }
        return true;
    }

    // simple-expr​ ​::=​ ​term​ ​simple-expr’
    public boolean simpleExpr() {
        if (token.getTag() == Tag.IDENTIFIER ||
                token.getTag() == Tag.OPEN_PARENTHESES ||
                token.getTag() == Tag.NOT ||
                token.getTag() == Tag.DIGIT ||
                token.getTag() == Tag.FLOAT_CONST ||
                token.getTag() == Tag.MINUS) {
            term();
            simpleExprPrime();
        } else {
            errorService.showError(lexer.line, "simple-expr​");
        }
        return true;
    }

    // simple-expr’​ ​::=​ ​addop​ ​term​ ​simple-expr’​ ​|​ ​𝛌
    public boolean simpleExprPrime() {
        if (token.getTag() == Tag.PLUS ||
                token.getTag() == Tag.MINUS ||
                token.getTag() == Tag.OR) {
            addop();
            term();
            simpleExpr();
        } else if (token.getTag() == Tag.CLOSE_PARENTHESES ||
                token.getTag() == Tag.DOT_COMMA ||
                token.getTag() == Tag.THEN ||
                token.getTag() == Tag.END ||
                token.getTag() == Tag.GT ||
                token.getTag() == Tag.EQ ||
                token.getTag() == Tag.GE ||
                token.getTag() == Tag.LT ||
                token.getTag() == Tag.LE ||
                token.getTag() == Tag.DIGIT ||
                token.getTag() == Tag.FLOAT_CONST ||
                token.getTag() == Tag.NOT) {
        } else {
            errorService.showError(lexer.line, "simple-expr’​");
        }

        return true;
    }

    // VERIFICAR == ok
    // term​ ​::=​ ​factor-a​ ​term’
    public boolean term() {
        if (token.getTag() == Tag.IDENTIFIER ||
                token.getTag() == Tag.OPEN_PARENTHESES ||
                token.getTag() == Tag.NOT ||
                token.getTag() == Tag.DIGIT ||
                token.getTag() == Tag.FLOAT_CONST ||
                token.getTag() == Tag.MINUS) {
            factorA();
            termPrime();
        } else {
            errorService.showError(lexer.line, "term");
        }
        return true;
    }

    // term’​ ​::=​ ​mulop​ ​factor-a​ ​term’​ ​|​ ​𝛌
    public boolean termPrime() {
        if (token.getTag() == Tag.MULTIPLICATION ||
                token.getTag() == Tag.DIVISION ||
                token.getTag() == Tag.AND) {
            mulop();
            factorA();
            termPrime();
        } else if (token.getTag() == Tag.THEN ||
                token.getTag() == Tag.END ||
                token.getTag() == Tag.CLOSE_PARENTHESES ||
                token.getTag() == Tag.DOT_COMMA ||
                token.getTag() == Tag.MINUS ||
                token.getTag() == Tag.EQ ||
                token.getTag() == Tag.GT ||
                token.getTag() == Tag.GE ||
                token.getTag() == Tag.LT ||
                token.getTag() == Tag.LE ||
                token.getTag() == Tag.NE ||
                token.getTag() == Tag.PLUS ||
                token.getTag() == Tag.OR) {
        } else {
            errorService.showError(lexer.line, "term’");
        }
        return true;
    }

    // Verificar = ok
    // fator-a ::= factor | "!" factor | "-" factor
    public boolean factorA() {
        if (token.getTag() == Tag.IDENTIFIER ||
                token.getTag() == Tag.DIGIT ||
                token.getTag() == Tag.FLOAT_CONST ||
                token.getTag() == Tag.LITERAL ||
                token.getTag() == Tag.OPEN_PARENTHESES) {
            factor();
        } else if (token.getTag() == Tag.NOT) {
            if (!eat(Tag.NOT)) {
                return false;
            }
            factor();
        } else if (token.getTag() == Tag.MINUS) {
            if (!eat(Tag.MINUS)) {
                return false;
            }
            factor();
        } else {
            errorService.showError(lexer.line, "fator-a");
        }
        return true;
    }

    // verificar - ok
    // factor ::= identifier | constant | "(" expression ")"
    public boolean factor() {
        if (token.getTag() == Tag.IDENTIFIER) {
            identifier();
        } else if (token.getTag() == Tag.DIGIT ||
                token.getTag() == Tag.FLOAT_CONST ||
                token.getTag() == Tag.LITERAL) {
            constant();
        } else if (token.getTag() == Tag.OPEN_PARENTHESES) {
            if (!eat(Tag.OPEN_PARENTHESES)) {
                return false;
            }
            expression();
            return eat(Tag.CLOSE_PARENTHESES);
        } else {
            errorService.showError(lexer.line, "factor");
        }
        return true;
    }

    // relop ::= "==" | ">" | ">=" | "<" | "<=" | "<>"
    public boolean relop() {
        if (token.getTag() == Tag.EQ) {
            return eat(Tag.EQ);
        } else if (token.getTag() == Tag.GT) {
            return eat(Tag.GT);
        } else if (token.getTag() == Tag.GE) {
            return eat(Tag.GE);
        } else if (token.getTag() == Tag.LT) {
            return eat(Tag.LT);
        } else if (token.getTag() == Tag.LE) {
            return eat(Tag.LE);
        } else if (token.getTag() == Tag.NE) {
            return eat(Tag.NE);
        } else {
            errorService.showError(lexer.line, "relop");
        }
        return true;
    }

    // addop ::= "+" | "-" | "||"
    public boolean addop() {
        if (token.getTag() == Tag.PLUS) {
            return eat(Tag.PLUS);
        } else if (token.getTag() == Tag.MINUS) {
            return eat(Tag.MINUS);
        } else if (token.getTag() == Tag.OR) {
            return eat(Tag.OR);
        } else {
            errorService.showError(lexer.line, "addop");
        }
        return true;
    }

    // mulop ::= "*" | "/" | "&&"
    public boolean mulop() {
        if (token.getTag() == Tag.MULTIPLICATION) {
            return eat(Tag.MULTIPLICATION);
        } else if (token.getTag() == Tag.DIVISION) {
            return eat(Tag.DIVISION);
        } else if (token.getTag() == Tag.AND) {
            return eat(Tag.AND);
        } else {
            errorService.showError(lexer.line, "mulop");
        }
        return true;
    }

    // constant ::= integer_const | float_const | literal
    public boolean constant() {
        if (token.getTag() == Tag.DIGIT) {
            integerConst();
        } else if (token.getTag() == Tag.FLOAT_CONST) {
            floatConst();
        } else if (token.getTag() == Tag.LITERAL) {
            literal();
        } else {
            errorService.showError(lexer.line, "constant");
        }
        return true;
    }

    // verificar - ok
    // integer_const ::= digit+
    public boolean integerConst() {
        if (token.getTag() == Tag.DIGIT) {
            return eat(Tag.DIGIT);
        } else {
            errorService.showError(lexer.line, "integer_const");
        }
        return true;
    }

    // verificar - ok
    // float_const ::= digit+“.”digit+
    public boolean floatConst() {
        if (token.getTag() == Tag.FLOAT_CONST) {
            return eat(Tag.FLOAT_CONST);
        } else {
            errorService.showError(lexer.line, "float_const");
        }
        return true;
    }

    // literal ::= " { " {caractere} " } "
    public boolean literal() {
        if (token.getTag() == Tag.LITERAL) {
            return eat(Tag.LITERAL);
        } else {
            errorService.showError(lexer.line, "literal");
        }
        return true;
    }

    // identifier ::= (letter | _ ) (letter | digit )*
    public boolean identifier() {
        if (token.getTag() == Tag.IDENTIFIER) {
            return eat(Tag.IDENTIFIER);
        } else {
            errorService.showError(lexer.line, "identifier");
        }
        return true;
    }
}
