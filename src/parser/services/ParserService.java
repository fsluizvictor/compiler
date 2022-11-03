package parser.services;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import lexer.controllers.*;
import lexer.models.*;
import lexer.services.*;

public class ParserService {

    private final LexerService lexer;
    private Token token;
    private final List<Integer> tags;
    private boolean eof;

    /**
     * @param filename
     * @throws FileNotFoundException
     */
    public ParserService(String filename) throws FileNotFoundException {
        lexer = new LexerService(filename);
        tags = new ArrayList<Integer>();
        eof = false;
    }

    private void advance() {
        try {
            // lê o arquivo
            token = lexer.scan();
            // se o int do tokenen do scan for igual ao int da Tag
            if (token.getTag() == Tag.COMMENT_LINE || token.getTag() == Tag.COMMENT_BLOCK || token.tag == Tag.ERROR) {
                advance();
            }

            if (token.tag == Tag.EOF) {
                eof = true;
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private boolean eat(int tag) {
        if (token.getTag() != tag) {
            return false;
        }
        // tags.add(tag);
        advance();
        return true;

    }

    public void start() {
        System.out.println("Iniciando Análise Sintática ...");
        advance();
        program();
        System.out.println("Finalizando Análise Sintática ...");

        // implementar a execução do program, de onde começam as funções de acordo com a
        // tabela do Follow

        // fim

    }

    // program ::= start [decl-list] stmt-list exit
    public boolean program() {
        if (!eat(Tag.START)) {
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
        if (token.getTag() == Tag.INT || token.getTag() == Tag.STRING || token.getTag() == Tag.FLOAT) {
            do {
                decl();
            } while (token.getTag() == Tag.INT || token.getTag() == Tag.STRING || token.getTag() == Tag.FLOAT);
        }

        return true;
    }

    // decl ::= type ident-list ";"
    public boolean decl() {
        if (token.getTag() == Tag.INT || token.getTag() == Tag.STRING || token.getTag() == Tag.FLOAT) {
            type();
            identList();
            return eat(Tag.DOT_COMMA);
        }

        return true;
    }

    // ident-list ::= identifier {"," identifier}
    public boolean identList() {
        identifier();
        do {
            if (!eat(Tag.COMMA)) {
                return false;
            }
            identifier();
        } while (token.getTag() == Tag.COMMA);

        return true;
    }

    // type ::= int | float | string
    public boolean type() {
        if (token.getTag() == Tag.INT) {
            return eat(Tag.INT);
        }
        if (token.getTag() == Tag.STRING) {
            return eat(Tag.STRING);
        }
        if (token.getTag() == Tag.FLOAT) {
            return eat(Tag.FLOAT);
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
        }
        return true;
    }

    // stmt ::= assign-stmt ";" | if-stmt | while-stmt | read-stmt ";" | write-stmt
    // ";"
    public boolean stmt() {
        if (token.getTag() == Tag.IDENTIFIER) {
            assignStmt();
            return eat(Tag.DOT_COMMA);
        }
        if (token.getTag() == Tag.IF) {
            ifStmt();
        }
        if (token.getTag() == Tag.WHILE) {
            whileStmt();
        }
        if (token.getTag() == Tag.SCAN) {
            readStmt();
            return eat(Tag.DOT_COMMA);
        }
        if (token.getTag() == Tag.PRINT) {
            readStmt();
            return eat(Tag.DOT_COMMA);
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
                token.getTag() == Tag.OR) {
            expression();
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
            if (!eat(Tag.END)) {
                return false;
            }
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
            if (!eat(Tag.CLOSE_PARENTHESES)) {
                return false;
            }
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
            if (!eat(Tag.CLOSE_PARENTHESES)) {
                return false;
            }
        }
        return true;
    }

    // writable ::= simple-expr | literal
    public boolean writable() {
        if (token.getTag() == Tag.IDENTIFIER ||
                token.getTag() == Tag.OPEN_PARENTHESES ||
                token.getTag() == Tag.NOT ||
                token.getTag() == Tag.MINUS) {
            simpleExpr();
        } else if (token.getTag() == Tag.LITERAL) {
            literal();
        }
        return true;
    }

    // expression​ ​::=​ ​simple-expr​ ​expression’
    public boolean expression() {
        if (token.getTag() == Tag.IDENTIFIER ||
                token.getTag() == Tag.OPEN_PARENTHESES ||
                token.getTag() == Tag.NOT ||
                token.getTag() == Tag.OR ||
                token.getTag() == Tag.MINUS) {
            simpleExpr();
            expressionPrime();
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

        }
        return true;
    }

    // simple-expr​ ​::=​ ​term​ ​simple-expr’
    public boolean simpleExpr() {
        if (token.getTag() == Tag.IDENTIFIER ||
                token.getTag() == Tag.OPEN_PARENTHESES ||
                token.getTag() == Tag.NOT ||
                token.getTag() == Tag.MINUS) {
            term();
            simpleExprPrime();
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
                token.getTag() == Tag.THEN ||
                token.getTag() == Tag.END ||
                token.getTag() == Tag.GT ||
                token.getTag() == Tag.EQ ||
                token.getTag() == Tag.GE || token.getTag() == Tag.LT ||
                token.getTag() == Tag.LE || token.getTag() == Tag.NOT) {
        }

        return true;
    }

    // term​ ​::=​ ​factor-a​ ​term’
    public boolean term() {
        return true;
    }

    // term’​ ​::=​ ​mulop​ ​factor-a​ ​term’​ ​|​ ​𝛌
    public boolean termPrime() {
        return true;
    }

    // fator-a ::= factor | "!" factor | "-" factor
    public boolean factorA() {
        return true;
    }

    // factor ::= identifier | constant | "(" expression ")"
    public boolean factor() {
        return true;
    }

    // relop ::= "==" | ">" | ">=" | "<" | "<=" | "<>"
    public boolean relop() {
        return true;
    }

    // addop ::= "+" | "-" | "||"
    public boolean addop() {
        return true;
    }

    // mulop ::= "*" | "/" | "&&"
    public boolean mulop() {
        return true;
    }

    // constant ::= integer_const | float_const | literal
    public boolean constant() {
        return true;
    }

    // integer_const ::= digit+
    public boolean integerConst() {
        return true;
    }

    // float_const ::= digit+“.”digit+
    public boolean floatConst() {
        return true;
    }

    // literal ::= " { " {caractere} " } "
    public boolean literal() {
        if (token.getTag() == Tag.LITERAL) {
            if (!eat(Tag.LITERAL)) {
                return false;
            }
        }
        return true;
    }

    // identifier ::= (letter | identifier’)
    public boolean identifier() {
        return true;
    }

    // identifier’ ::= ( _ | digit )*
    public boolean identifierPrime() {
        return true;
    }

    // letter ::= [A-za-z]
    public boolean letter() {
        return true;
    }

    // digit ::= [0-9]
    public boolean digit() {
        return true;
    }

    // caractere ::= um dos caracteres ASCII, exceto quebra de linha
    public boolean caractere() {
        return true;
    }

}
