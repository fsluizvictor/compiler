package parser.services;

import java.io.FileNotFoundException;
import java.util.Hashtable;

import lexer.models.*;
import lexer.services.*;
import semantic.models.Node;
import semantic.models.SymbolTableElement;
import semantic.service.SemanticErrorService;

public class ParserService {

    private final LexerService lexer;
    private Token token;
    private boolean isEOF;
    private ErrorService errorService;
    private SemanticErrorService semanticErrorService;

    /**
     * @param filename
     * @throws FileNotFoundException
     */
    public ParserService(String filename) throws FileNotFoundException {
        lexer = new LexerService(filename);
        errorService = new ErrorService();
        this.isEOF = false;
        semanticErrorService = new SemanticErrorService();
    }

    private void advance() {
        try {
            // lê o arquivo
            token = lexer.scan();
            System.out.println(token);
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

    /**
     * @param tag
     * @return
     */
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
    public Node program() {

        Node node = new Node();

        if (!eat(Tag.START)) {
            errorService.showError(lexer.line, "program");
            return node;
        }

        if (token.getTag() == Tag.INT || token.getTag() == Tag.STRING || token.getTag() == Tag.FLOAT) {
            declList();
        }

        stmtList();

        if(!eat(Tag.EXIT)){
            return node;
        }

        return node;
    }

    // decl-list ::= decl {decl}
    public Node declList() {
        Node node = new Node();
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

        return node;
    }

    // decl ::= type ident-list ";"
    public Node decl() {
        Node node = new Node();
        if (token.getTag() == Tag.INT ||
                token.getTag() == Tag.STRING ||
                token.getTag() == Tag.FLOAT) {
            node.setType(type().getType());
            identList(node.getType());
            if (!eat(Tag.DOT_COMMA)) {
                return node;
            }
        } else {
            errorService.showError(lexer.line, "decl");
        }

        return node;
    }

    // ident-list ::= identifier {"," identifier}
    public Node identList(int type) {
        Node node = new Node();
        if (token.getTag() == Tag.IDENTIFIER) {
            identifier(type);
            while (token.getTag() == Tag.COMMA) {
                if (!eat(Tag.COMMA)) {
                    return node;
                }
                identifier(type);
            }
        } else {
            errorService.showError(lexer.line, "ident-list");
        }

        return node;
    }

    // type ::= int | float | string
    public Node type() {
        Node node = new Node();
        if (token.getTag() == Tag.INT) {
            if (!eat(Tag.INT)) {
                return node;
            }
            node.setType(Tag.INT);
        } else if (token.getTag() == Tag.STRING) {
            if (!eat(Tag.STRING)) {
                return node;
            }
            node.setType(Tag.STRING);
        } else if (token.getTag() == Tag.FLOAT) {
            if (!eat(Tag.FLOAT)) {
                return node;
            }
            node.setType(Tag.FLOAT);
        } else {
            errorService.showError(lexer.line, "type");
        }
        return node;
    }

    // stmt-list ::= stmt {stmt}
    public Node stmtList() {
        Node node = new Node();
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
        return node;
    }

    // stmt ::= assign-stmt ";" | if-stmt | while-stmt | read-stmt ";" | write-stmt
    // ";"
    public Node stmt() {
        Node node = new Node();
        if (token.getTag() == Tag.IDENTIFIER) {
            assignStmt();
            if (!eat(Tag.DOT_COMMA)) {
                return node;
            }
        } else if (token.getTag() == Tag.IF) {
            ifStmt();
        } else if (token.getTag() == Tag.WHILE) {
            whileStmt();
        } else if (token.getTag() == Tag.SCAN) {
            readStmt();
            if (!eat(Tag.DOT_COMMA)) {
                return node;
            }
        } else if (token.getTag() == Tag.PRINT) {
            writeStmt();
            if (!eat(Tag.DOT_COMMA)) {
                return node;
            }
        } else {
            errorService.showError(lexer.line, "stmt");
        }
        return node;
    }

    // assign-stmt ::= identifier "=" simple_expr
    public Node assignStmt() {
        Node node = new Node();
        int aux;
        if (token.getTag() == Tag.IDENTIFIER) {

            node.setType(identifier(Tag.VOID_SEMANTIC_TAG).getType());

            if (!eat(Tag.ASSIGN)) {
                return node;
            }

            // check if is the same type both values to expression
            aux = simpleExpr().getType();
            if (aux != node.type) {
                semanticErrorService.unexpectedTagError(node.getType(), aux);
                node.setType(node.getType());
            }
        } else {
            errorService.showError(lexer.line, "assign-stmt");
        }
        return node;
    }

    // if-stmt​ ​::=​ ​if​ ​condition​ ​then​ ​stmt-list​ ​if-stmt’
    public Node ifStmt() {
        Node node = new Node();
        if (token.getTag() == Tag.IF) {
            if (!eat(Tag.IF)) {
                return node;
            }
            condition();
            if (!eat(Tag.THEN)) {
                return node;
            }
            stmtList();
            ifStmtPrime();
        } else {
            errorService.showError(lexer.line, "if-stmt");
        }
        return node;
    }

    // if-stmt’​ ​::=​ ​end​ ​|​ ​else​ ​stmt-list​ ​end
    public Node ifStmtPrime() {
        Node node = new Node();
        if (token.getTag() == Tag.END) {
            if (!eat(Tag.END)) {
                return node;
            }
        } else if (token.getTag() == Tag.ELSE) {
            if (!eat(Tag.ELSE)) {
                return node;
            }
            stmtList();
            if (token.getTag() == Tag.END) {
                if (!eat(Tag.END)) {
                    return node;
                }
            }
        } else {
            errorService.showError(lexer.line, "if-stmt'");
        }

        return node;
    }

    // condition ::= expression
    public Node condition() {
        Node node = new Node();
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

        return node;
    }

    // while-stmt ::= do stmt-list stmt-sufix
    public Node whileStmt() {
        Node node = new Node();
        if (token.getTag() == Tag.DO) {
            if (!eat(Tag.DO)) {
                return node;
            }
            stmtList();
            stmtSufix();
        } else {
            errorService.showError(lexer.line, "while-stmt");
        }
        return node;
    }

    // stmt-sufix ::= while condition end
    public Node stmtSufix() {
        Node node = new Node();
        if (token.getTag() == Tag.WHILE) {
            if (!eat(Tag.WHILE)) {
                return node;
            }
            condition();
            if (!eat(Tag.END)) {
                return node;
            }
        } else {
            errorService.showError(lexer.line, "stmt-sufix");
        }
        return node;
    }

    // read-stmt ::= scan "(" identifier ")"
    public Node readStmt() {
        Node node = new Node();
        if (token.getTag() == Tag.SCAN) {
            if (!eat(Tag.SCAN)) {
                return node;
            }
            if (!eat(Tag.OPEN_PARENTHESES)) {
                return node;
            }

            node.setType(identifier(Tag.VOID_SEMANTIC_TAG).getType());

            if (!eat(Tag.CLOSE_PARENTHESES)) {
                return node;
            }
        } else {
            errorService.showError(lexer.line, "read-stmt");
        }
        return node;
    }

    // write-stmt ::= print "(" writable ")"
    public Node writeStmt() {
        Node node = new Node();
        if (token.getTag() == Tag.PRINT) {
            if (!eat(Tag.PRINT)) {
                return node;
            }
            if (!eat(Tag.OPEN_PARENTHESES)) {
                return node;
            }

            node.setType(writable().getType());

            if (!eat(Tag.CLOSE_PARENTHESES)) {
                return node;
            }
        } else {
            errorService.showError(lexer.line, "write-stmt");
        }
        return node;
    }

    // writable ::= simple-expr | literal
    public Node writable() {
        Node node = new Node();
        if (token.getTag() == Tag.IDENTIFIER ||
                token.getTag() == Tag.DIGIT ||
                token.getTag() == Tag.FLOAT_CONST ||
                token.getTag() == Tag.OPEN_PARENTHESES ||
                token.getTag() == Tag.NOT ||
                token.getTag() == Tag.MINUS) {

            node.setType(simpleExpr().getType());

        } else if (token.getTag() == Tag.LITERAL) {

            node.setType(literal().getType());

        } else {
            errorService.showError(lexer.line, "writable");
        }
        return node;
    }

    // expression​ ​::=​ ​simple-expr​ ​expression’
    public Node expression() {
        Node node = new Node();
        if (token.getTag() == Tag.IDENTIFIER ||
                token.getTag() == Tag.DIGIT ||
                token.getTag() == Tag.FLOAT_CONST ||
                token.getTag() == Tag.OPEN_PARENTHESES ||
                token.getTag() == Tag.NOT ||
                token.getTag() == Tag.OR ||
                token.getTag() == Tag.MINUS) {
            node.setType(simpleExpr().getType());
            expressionPrime(node.getType());
        } else {
            errorService.showError(lexer.line, "expression​");
        }
        return node;
    }

    // expression’​ ​::=​ ​ ​relop​ ​simple-expr​ ​|​ ​𝛌
    public Node expressionPrime(int type) {
        Node node = new Node();
        if (token.getTag() == Tag.GT ||
                token.getTag() == Tag.EQ ||
                token.getTag() == Tag.GE ||
                token.getTag() == Tag.LT ||
                token.getTag() == Tag.LE ||
                token.getTag() == Tag.NOT) {
            relop();
            node.setType(simpleExpr().getType());

            if (node.getType() != type) {
                semanticErrorService.unexpectedTagError(type, node.getType());
                node.setType(type);
            }

        } else if (token.getTag() == Tag.THEN ||
                token.getTag() == Tag.END ||
                token.getTag() == Tag.CLOSE_PARENTHESES) {

        } else {
            errorService.showError(lexer.line, "expression'");
        }
        return node;
    }

    // simple-expr​ ​::=​ ​term​ ​simple-expr’
    public Node simpleExpr() {
        Node node = new Node();
        if (token.getTag() == Tag.IDENTIFIER ||
                token.getTag() == Tag.OPEN_PARENTHESES ||
                token.getTag() == Tag.NOT ||
                token.getTag() == Tag.DIGIT ||
                token.getTag() == Tag.FLOAT_CONST ||
                token.getTag() == Tag.MINUS) {
            node.setType(term().getType());
            simpleExprPrime(node.getType());
        } else {
            errorService.showError(lexer.line, "simple-expr​");
        }
        return node;
    }

    // TODO: add a semantic validation
    // simple-expr’​ ​::=​ ​addop​ ​term​ ​simple-expr’​ ​|​ ​𝛌
    public Node simpleExprPrime(int type) {
        Node node = new Node();
        if (token.getTag() == Tag.PLUS ||
                token.getTag() == Tag.MINUS ||
                token.getTag() == Tag.OR) {

            addop();

            node.setType(term().getType());

            // check the correct types of elements
            if (node.getType() != Tag.INT ||
                    node.getType() != Tag.FLOAT ||
                    node.getType() != Tag.STRING) {
                semanticErrorService.unexpectedTagError(type, node.getType());
                node.setType(type);
            }

            simpleExprPrime(node.getType());
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

        return node;
    }

    // term​ ​::=​ ​factor-a​ ​term’
    public Node term() {
        Node node = new Node();
        if (token.getTag() == Tag.IDENTIFIER ||
                token.getTag() == Tag.OPEN_PARENTHESES ||
                token.getTag() == Tag.NOT ||
                token.getTag() == Tag.DIGIT ||
                token.getTag() == Tag.FLOAT_CONST ||
                token.getTag() == Tag.MINUS) {
            node.setType(factorA().getType());
            termPrime();
        } else {
            errorService.showError(lexer.line, "term");
        }
        return node;
    }

    // TODO : add a semantic validation
    // term’​ ​::=​ ​mulop​ ​factor-a​ ​term’​ ​|​ ​𝛌
    public Node termPrime() {
        Node node = new Node();
        if (token.getTag() == Tag.MULTIPLICATION ||
                token.getTag() == Tag.DIVISION ||
                token.getTag() == Tag.AND) {

            mulop();

            node.setType(factorA().getType());

            if (node.getType() != Tag.INT ||
                    node.getType() != Tag.FLOAT ||
                    node.getType() != Tag.STRING) {
                semanticErrorService.unexpectedTagError(Tag.INT, node.getType());
                semanticErrorService.unexpectedTagError(Tag.FLOAT, node.getType());
                semanticErrorService.unexpectedTagError(Tag.STRING, node.getType());
            }

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
        return node;
    }

    // Verificar = ok
    // fator-a ::= factor | "!" factor | "-" factor
    public Node factorA() {
        Node node = new Node();
        if (token.getTag() == Tag.IDENTIFIER ||
                token.getTag() == Tag.DIGIT ||
                token.getTag() == Tag.FLOAT_CONST ||
                token.getTag() == Tag.LITERAL ||
                token.getTag() == Tag.OPEN_PARENTHESES) {
            node.setType(factor().getType());
        } else if (token.getTag() == Tag.NOT) {
            if (!eat(Tag.NOT)) {
                return node;
            }
            node.setType(factor().getType());
        } else if (token.getTag() == Tag.MINUS) {
            if (!eat(Tag.MINUS)) {
                return node;
            }
            node.setType(factor().getType());
        } else {
            errorService.showError(lexer.line, "fator-a");
        }
        return node;
    }

    // verificar - ok
    // factor ::= identifier | constant | "(" expression ")"
    public Node factor() {
        Node node = new Node();
        if (token.getTag() == Tag.IDENTIFIER) {
            node.setType(identifier(Tag.VOID_SEMANTIC_TAG).getType());
        } else if (token.getTag() == Tag.DIGIT ||
                token.getTag() == Tag.FLOAT_CONST ||
                token.getTag() == Tag.LITERAL) {
            node.setType(constant().getType());
        } else if (token.getTag() == Tag.OPEN_PARENTHESES) {
            if (!eat(Tag.OPEN_PARENTHESES)) {
                return node;
            }
            node.setType(expression().getType());
            if (!eat(Tag.CLOSE_PARENTHESES)) {
                return node;
            }
        } else {
            errorService.showError(lexer.line, "factor");
        }
        return node;
    }

    // relop ::= "==" | ">" | ">=" | "<" | "<=" | "<>"
    public Node relop() {
        Node node = new Node();
        if (token.getTag() == Tag.EQ) {
            if (!eat(Tag.EQ)) {
                return node;
            }
        } else if (token.getTag() == Tag.GT) {
            if (!eat(Tag.GT)) {
                return node;
            }
        } else if (token.getTag() == Tag.GE) {
            if (!eat(Tag.GE)) {
                return node;
            }
        } else if (token.getTag() == Tag.LT) {
            if (!eat(Tag.LT)) {
                return node;
            }
        } else if (token.getTag() == Tag.LE) {
            if (!eat(Tag.LE)) {
                return node;
            }
        } else if (token.getTag() == Tag.NE) {
            if (!eat(Tag.NE)) {
                return node;
            }
        } else {
            errorService.showError(lexer.line, "relop");
        }
        return node;
    }

    // addop ::= "+" | "-" | "||"
    public Node addop() {
        Node node = new Node();
        if (token.getTag() == Tag.PLUS) {
            if (!eat(Tag.PLUS)) {
                return node;
            }
        } else if (token.getTag() == Tag.MINUS) {
            if (!eat(Tag.MINUS)) {
                return node;
            }
        } else if (token.getTag() == Tag.OR) {
            if (!eat(Tag.OR)) {
                return node;
            }
        } else {
            errorService.showError(lexer.line, "addop");
        }
        return node;
    }

    // mulop ::= "*" | "/" | "&&"
    public Node mulop() {
        Node node = new Node();
        if (token.getTag() == Tag.MULTIPLICATION) {
            if (!eat(Tag.MULTIPLICATION)) {
                return node;
            }
        } else if (token.getTag() == Tag.DIVISION) {
            if (!eat(Tag.DIVISION)) {
                return node;
            }
        } else if (token.getTag() == Tag.AND) {
            if (!eat(Tag.AND)) {
                return node;
            }
        } else {
            errorService.showError(lexer.line, "mulop");
        }
        return node;
    }

    // constant ::= integer_const | float_const | literal
    public Node constant() {
        Node node = new Node();
        if (token.getTag() == Tag.DIGIT) {
            node.setType(integerConst().getType());
        } else if (token.getTag() == Tag.FLOAT_CONST) {
            node.setType(floatConst().getType());
        } else if (token.getTag() == Tag.LITERAL) {
            node.setType(literal().getType());
        } else {
            errorService.showError(lexer.line, "constant");
        }
        return node;
    }

    // verificar - ok
    // integer_const ::= digit+
    public Node integerConst() {
        Node node = new Node();
        if (token.getTag() == Tag.DIGIT) {
            if (!eat(Tag.DIGIT)) {
                return node;
            }
            node.setType(Tag.DIGIT);
        } else {
            errorService.showError(lexer.line, "integer_const");
        }
        return node;
    }

    // verificar - ok
    // float_const ::= digit+“.”digit+
    public Node floatConst() {
        Node node = new Node();
        if (token.getTag() == Tag.FLOAT_CONST) {
            if (!eat(Tag.FLOAT_CONST)) {
                return node;
            }
            node.setType(Tag.FLOAT_CONST);
        } else {
            errorService.showError(lexer.line, "float_const");
        }
        return node;
    }

    // literal ::= " { " {caractere} " } "
    public Node literal() {
        Node node = new Node();
        if (token.getTag() == Tag.LITERAL) {
            if (!eat(Tag.LITERAL)) {
                return node;
            }
            node.setType(Tag.LITERAL);
        } else {
            errorService.showError(lexer.line, "literal");
        }
        return node;
    }

    // TODO: add a semantic validation
    // identifier ::= (letter | _ ) (letter | digit )*
    public Node identifier(int type) {
        Node node = new Node();

        // check the uniqueness of each variable name
        Hashtable<String, SymbolTableElement> sybleTable = lexer.getWords();

        if (token.getTag() == Tag.IDENTIFIER) {
            if (!eat(Tag.IDENTIFIER)) {
                return node;
            }

            SymbolTableElement element = sybleTable.get(token.getLexeme());

            if (!element.equals(null)) {
                if (type == Tag.VOID_SEMANTIC_TAG) {
                    if (element.getType() == Tag.VOID_SEMANTIC_TAG) {
                        semanticErrorService.variableNotDeclaredError(element.getWord().getLexeme());
                    } else {
                        node.setType(element.getType());
                    }
                } else {
                    node.type = type;
                    if(element.getType() == Tag.VOID_SEMANTIC_TAG ){
                        element.setType(type);
                        lexer.setWords(sybleTable);
                    }else{
                        semanticErrorService.variableAlreadyDeclaredError();
                    }
                }
            }

        } else {
            errorService.showError(lexer.line, "identifier");
        }
        return node;
    }
}
