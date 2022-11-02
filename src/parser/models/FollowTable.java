package parser.models;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lexer.models.Tag;

public class FollowTable {
    
    Map<String, List<Integer>> follow = new HashMap<>();

    public FollowTable() {
        // program
        List<Integer> program = new ArrayList<>();
        program.add(Tag.EOF);
        follow.put("program", program);

        // declList
        // [A-Za-z],_, [0-9], if, do, scan, print
        List<Integer> declList = new ArrayList<>();
        declList.add(Tag.IDENTIFIER);
        declList.add(Tag.UNDERSCORE); // underline - underscore
        declList.add(Tag.DIGIT); // [0-9]
        declList.add(Tag.IF);
        declList.add(Tag.DO);
        declList.add(Tag.SCAN);
        declList.add(Tag.PRINT);
        follow.put("declList", declList);

        // decl
        //int, float, string,[A-Za-z],_, [0-9], if, do, scan, print
        List<Integer> decl = new ArrayList<>();
        decl.add(Tag.INT);
        decl.add(Tag.FLOAT);
        decl.add(Tag.STRING);
        decl.add(Tag.IDENTIFIER);
        decl.add(Tag.UNDERSCORE);
        decl.add(Tag.DIGIT);
        decl.add(Tag.IF);
        decl.add(Tag.DO);
        decl.add(Tag.SCAN);
        decl.add(Tag.PRINT);
        follow.put("decl", decl);

        // identList
        List<Integer> identList = new ArrayList<>();
        identList.add(Tag.DOT_COMMA);
        follow.put("identList", identList);

        //type
        //[A-Za-z],_, [0-9]
        List<Integer> type = new ArrayList<>();
        type.add(Tag.IDENTIFIER);
        type.add(Tag.UNDERSCORE);
        type.add(Tag.DIGIT);
        follow.put("type", type);

        //stmtList
        // exit, end, else, while
        List<Integer> stmtList = new ArrayList<>();
        stmtList.add(Tag.EXIT);
        stmtList.add(Tag.END);
        stmtList.add(Tag.ELSE);
        stmtList.add(Tag.WHILE);
        follow.put("stmtList", stmtList);

        //stmt
        //[A-Za-z],_, [0-9], if, do, scan, print, exit, end, else, while
        List<Integer> stmt = new ArrayList<>();
        stmt.add(Tag.IDENTIFIER);
        stmt.add(Tag.UNDERSCORE);
        stmt.add(Tag.DIGIT);
        stmt.add(Tag.IF);
        stmt.add(Tag.DO);
        stmt.add(Tag.SCAN);
        stmt.add(Tag.PRINT);
        stmt.add(Tag.EXIT);
        stmt.add(Tag.END);
        stmt.add(Tag.ELSE);
        stmt.add(Tag.WHILE);
        follow.put("stmt", stmt);

        //assingStmt
        List<Integer> assingStmt = new ArrayList<>();
        assingStmt.add(Tag.DOT_COMMA);  // ;
        follow.put("assingStmt", assingStmt);

        //ifStmt
        //[A-Za-z],_, [0-9], if, do, scan, print
        List<Integer> ifStmt = new ArrayList<>();
        ifStmt.add(Tag.IDENTIFIER);
        ifStmt.add(Tag.UNDERSCORE);
        ifStmt.add(Tag.DIGIT);
        ifStmt.add(Tag.IF);
        ifStmt.add(Tag.DO);
        ifStmt.add(Tag.SCAN);
        ifStmt.add(Tag.PRINT);
        follow.put("ifStmt", ifStmt);

        //ifStmtPrime
        //[A-Za-z],_, [0-9], if, do, scan, print
        List<Integer> ifStmtPrime = new ArrayList<>();
        ifStmtPrime.add(Tag.IDENTIFIER);
        //ifStmtPrime.add(Tag.UNDERSCORE);
        ifStmtPrime.add(Tag.DIGIT);
        ifStmtPrime.add(Tag.IF);
        ifStmtPrime.add(Tag.DO);
        ifStmtPrime.add(Tag.SCAN);
        ifStmtPrime.add(Tag.PRINT);
        follow.put("ifStmtPrime", ifStmtPrime);

        //condition
        List<Integer> condition = new ArrayList<>();
        condition.add(Tag.THEN);
        condition.add(Tag.END);
        follow.put("condition", condition);

        //whileStmt
        // [A-Za-z],_, [0-9], if, do, scan, print, exit, end, else, while
        List<Integer> whileStmt = new ArrayList<>();
        whileStmt.add(Tag.IDENTIFIER);
        whileStmt.add(Tag.UNDERSCORE);
        whileStmt.add(Tag.DIGIT);
        whileStmt.add(Tag.IF);
        whileStmt.add(Tag.DO);
        whileStmt.add(Tag.SCAN);
        whileStmt.add(Tag.PRINT);
        whileStmt.add(Tag.EXIT);
        whileStmt.add(Tag.END);
        whileStmt.add(Tag.ELSE);
        whileStmt.add(Tag.WHILE);
        follow.put("whileStmt", whileStmt);

        //stmtSufix
        // [A-Za-z],_, [0-9], if, do, scan, print, exit, end, else, while
        List<Integer> stmtSufix = new ArrayList<>();
        stmtSufix.add(Tag.IDENTIFIER);
        stmtSufix.add(Tag.UNDERSCORE);
        stmtSufix.add(Tag.DIGIT);
        stmtSufix.add(Tag.IF);
        stmtSufix.add(Tag.DO);
        stmtSufix.add(Tag.SCAN);
        stmtSufix.add(Tag.PRINT);
        stmtSufix.add(Tag.EXIT);
        stmtSufix.add(Tag.END);
        stmtSufix.add(Tag.ELSE);
        stmtSufix.add(Tag.WHILE);
        follow.put("stmtSufix", stmtSufix);

        //readStmt
        List<Integer> readStmt = new ArrayList<>();
        readStmt.add(Tag.DOT_COMMA);
        follow.put("readStmt", readStmt);

        //writeStmt
        List<Integer> writeStmt = new ArrayList<>();
        writeStmt.add(Tag.DOT_COMMA);
        follow.put("writeStmt", writeStmt);

        //writable
        List<Integer> writable = new ArrayList<>();
        writable.add(Tag.CLOSE_PARENTHESES);
        follow.put("writable", writable);

        //expression
        List<Integer> expression = new ArrayList<>();
        expression.add(Tag.THEN);
        expression.add(Tag.END);
        expression.add(Tag.CLOSE_PARENTHESES);
        follow.put("expression", expression);

        //expressionPrime
        List<Integer> expressionPrime = new ArrayList<>();
        expressionPrime.add(Tag.THEN);
        expressionPrime.add(Tag.END);
        expressionPrime.add(Tag.CLOSE_PARENTHESES);
        follow.put("expressionPrime", expressionPrime);

        //simpleExpr
        //  ), then, end, ==, >, >=, <, <=, <>
        List<Integer> simpleExpr = new ArrayList<>();
        simpleExpr.add(Tag.CLOSE_PARENTHESES);
        simpleExpr.add(Tag.THEN);
        simpleExpr.add(Tag.END);
        simpleExpr.add(Tag.EQ);
        simpleExpr.add(Tag.GT);
        simpleExpr.add(Tag.GE);
        simpleExpr.add(Tag.LT);
        simpleExpr.add(Tag.LE);
        simpleExpr.add(Tag.NE);
        follow.put("simpleExpr", simpleExpr);

        //simpleExprPrime
        // ), then, end, ==, >, >=, <, <=, <>
        List<Integer> simpleExprPrime = new ArrayList<>();
        simpleExprPrime.add(Tag.CLOSE_PARENTHESES);
        simpleExprPrime.add(Tag.THEN);
        simpleExprPrime.add(Tag.END);
        simpleExprPrime.add(Tag.EQ);
        simpleExprPrime.add(Tag.GT);
        simpleExprPrime.add(Tag.GE);
        simpleExprPrime.add(Tag.LT);
        simpleExprPrime.add(Tag.LE);
        simpleExprPrime.add(Tag.NE);
        follow.put("simpleExprPrime", simpleExprPrime);

        //term
        //  ), then, end,==, >, >=, <, <=, <>, -, ||, +
        List<Integer> term = new ArrayList<>();
        term.add(Tag.CLOSE_PARENTHESES);
        term.add(Tag.THEN);
        term.add(Tag.END);
        term.add(Tag.EQ);
        term.add(Tag.GT);
        term.add(Tag.GE);
        term.add(Tag.LT);
        term.add(Tag.LE);
        term.add(Tag.NE);
        term.add(Tag.MINUS);
        term.add(Tag.OR);
        term.add(Tag.PLUS);
        follow.put("term", term);

        //termPrime
        //  ), then, end,==, >, >=, <, <=, <>, -, ||, +
        List<Integer> termPrime = new ArrayList<>();
        termPrime.add(Tag.CLOSE_PARENTHESES);
        termPrime.add(Tag.THEN);
        termPrime.add(Tag.END);
        termPrime.add(Tag.EQ);
        termPrime.add(Tag.GT);
        termPrime.add(Tag.GE);
        termPrime.add(Tag.LT);
        termPrime.add(Tag.LE);
        termPrime.add(Tag.NE);
        termPrime.add(Tag.MINUS);
        termPrime.add(Tag.OR);
        termPrime.add(Tag.PLUS);
        follow.put("termPrime", termPrime);

        //factorA
        //  ), then, end,==, >, >=, <, <=, <>, -, ||, +, *,/, &&
        List<Integer> factorA = new ArrayList<>();
        factorA.add(Tag.CLOSE_PARENTHESES);
        factorA.add(Tag.THEN);
        factorA.add(Tag.END);
        factorA.add(Tag.EQ);
        factorA.add(Tag.GT);
        factorA.add(Tag.GE);
        factorA.add(Tag.LT);
        factorA.add(Tag.LE);
        factorA.add(Tag.NE);
        factorA.add(Tag.MINUS);
        factorA.add(Tag.OR);
        factorA.add(Tag.PLUS);
        factorA.add(Tag.MULTIPLICATION);
        factorA.add(Tag.DIVISION);
        factorA.add(Tag.AND);
        follow.put("factorA", factorA);

        //factor
        // ), then, end,==, >, >=, <, <=, <>, -, ||, +, *,/, &&
        List<Integer> factor = new ArrayList<>();
        factor.add(Tag.CLOSE_PARENTHESES);
        factor.add(Tag.THEN);
        factor.add(Tag.END);
        factor.add(Tag.EQ);
        factor.add(Tag.GT);
        factor.add(Tag.GE);
        factor.add(Tag.LT);
        factor.add(Tag.LE);
        factor.add(Tag.NE);
        factor.add(Tag.MINUS);
        factor.add(Tag.OR);
        factor.add(Tag.PLUS);
        factor.add(Tag.MULTIPLICATION);
        factor.add(Tag.DIVISION);
        factor.add(Tag.AND);
        follow.put("factor", factor);

        //relop
        // [A-Za-z],_, [0-9], ", (, !, -
        List<Integer> relop = new ArrayList<>();
        relop.add(Tag.IDENTIFIER);
        relop.add(Tag.UNDERSCORE);
        relop.add(Tag.DIGIT);
        relop.add(Tag.LITERAL);
        relop.add(Tag.OPEN_PARENTHESES);
        relop.add(Tag.NOT);
        relop.add(Tag.MINUS);
        follow.put("relop", relop);

        //addop
        // [A-Za-z],_, [0-9], ", (, !, -
        List<Integer> addop = new ArrayList<>();
        addop.add(Tag.IDENTIFIER);
        addop.add(Tag.UNDERSCORE);
        addop.add(Tag.DIGIT);
        addop.add(Tag.LITERAL);
        addop.add(Tag.OPEN_PARENTHESES);
        addop.add(Tag.NOT);
        addop.add(Tag.MINUS);
        follow.put("addop", addop);

        //mulop
        // [A-Za-z],_,[0-9],{, ", (, !, -
        List<Integer> mulop = new ArrayList<>();
        mulop.add(Tag.IDENTIFIER);
        mulop.add(Tag.UNDERSCORE);
        mulop.add(Tag.DIGIT);
        mulop.add(Tag.LITERAL); // { 
        mulop.add(Tag.LITERAL);
        mulop.add(Tag.OPEN_PARENTHESES);
        mulop.add(Tag.NOT);
        mulop.add(Tag.MINUS);
        follow.put("mulop", mulop);

        //constant
        // ), then, end,==, >, >=, <, <=, <>, -, ||, +, *,/, &&
        List<Integer> constant = new ArrayList<>();
        constant.add(Tag.CLOSE_PARENTHESES);
        constant.add(Tag.THEN);
        constant.add(Tag.END);
        constant.add(Tag.EQ);
        constant.add(Tag.GT);
        constant.add(Tag.GE);
        constant.add(Tag.LT);
        constant.add(Tag.LE);
        constant.add(Tag.NE);
        constant.add(Tag.MINUS);
        constant.add(Tag.OR);
        constant.add(Tag.PLUS);
        constant.add(Tag.MULTIPLICATION);
        constant.add(Tag.DIVISION);
        constant.add(Tag.AND);
        follow.put("constant", constant);

        //integerConst
        // ), then, end,==, >, >=, <, <=, <>, -, ||, +, *,/, &&
        List<Integer> integerConst = new ArrayList<>();
        integerConst.add(Tag.CLOSE_PARENTHESES);
        integerConst.add(Tag.THEN);
        integerConst.add(Tag.END);
        integerConst.add(Tag.EQ);
        integerConst.add(Tag.GT);
        integerConst.add(Tag.GE);
        integerConst.add(Tag.LT);
        integerConst.add(Tag.LE);
        integerConst.add(Tag.NE);
        integerConst.add(Tag.MINUS);
        integerConst.add(Tag.OR);
        integerConst.add(Tag.PLUS);
        integerConst.add(Tag.MULTIPLICATION);
        integerConst.add(Tag.DIVISION);
        integerConst.add(Tag.AND);
        follow.put("integerConst", integerConst);

        // float_const
        // ), then, end,==, >, >=, <, <=, <>, -, ||, +, *,/, &&
        List<Integer> literal = new ArrayList<>();
        floatConst.add(Tag.CLOSE_PARENTHESES);
        floatConst.add(Tag.THEN);
        floatConst.add(Tag.END);
        floatConst.add(Tag.EQ);
        floatConst.add(Tag.GT);
        floatConst.add(Tag.GE);
        floatConst.add(Tag.LT);
        floatConst.add(Tag.LE);
        floatConst.add(Tag.NE);
        floatConst.add(Tag.MINUS);
        floatConst.add(Tag.OR);
        floatConst.add(Tag.PLUS);
        floatConst.add(Tag.MULTIPLICATION);
        floatConst.add(Tag.DIVISION);
        floatConst.add(Tag.AND);
        follow.put("floatConst", floatConst);

        //literal
        // ), then, end,==, >, >=, <, <=, <>, -, ||, +, *,/, &&
        List<Integer> literal = new ArrayList<>();
        literal.add(Tag.CLOSE_PARENTHESES);
        literal.add(Tag.THEN);
        literal.add(Tag.END);
        literal.add(Tag.EQ);
        literal.add(Tag.GT);
        literal.add(Tag.GE);
        literal.add(Tag.LT);
        literal.add(Tag.LE);
        literal.add(Tag.NE);
        literal.add(Tag.MINUS);
        literal.add(Tag.OR);
        literal.add(Tag.PLUS);
        literal.add(Tag.MULTIPLICATION);
        literal.add(Tag.DIVISION);
        literal.add(Tag.AND);
        follow.put("literal", literal);

        //identifier
        // [A-Za-z],_, [0-9],;, =,), then, end,==, >, >=, <, <=, <>, -, ||, +, *,/, &&
        List<Integer> identifier = new ArrayList<>();
        identifier.add(Tag.IDENTIFIER);
        identifier.add(Tag.UNDERSCORE); // UNDERSCORE
        identifier.add(Tag.DIGIT);
        identifier.add(Tag.DOT_COMMA);
        identifier.add(Tag.ASSIGN);
        identifier.add(Tag.CLOSE_PARENTHESES);
        identifier.add(Tag.THEN);
        identifier.add(Tag.END);
        identifier.add(Tag.EQ);
        identifier.add(Tag.GT);
        identifier.add(Tag.GE);
        identifier.add(Tag.LT);
        identifier.add(Tag.LE);
        identifier.add(Tag.NOT);
        identifier.add(Tag.MINUS);
        identifier.add(Tag.OR);
        identifier.add(Tag.PLUS);
        identifier.add(Tag.MULTIPLICATION);
        identifier.add(Tag.DIVISION);
        identifier.add(Tag.AND);
        follow.put("identifier", identifier);

        //identifier'
        // [A-Za-z],_, [0-9],;, =,), then, end,==, >, >=, <, <=, <>, -, ||, +, *,/, &&
        List<Integer> identifierPrime = new ArrayList<>();
        identifierPrime.add(Tag.IDENTIFIER);
        identifierPrime.add(Tag.UNDERSCORE); // UNDERSCORE
        identifierPrime.add(Tag.DIGIT);
        identifierPrime.add(Tag.DOT_COMMA);
        identifierPrime.add(Tag.ASSIGN);
        identifierPrime.add(Tag.CLOSE_PARENTHESES);
        identifierPrime.add(Tag.THEN);
        identifierPrime.add(Tag.END);
        identifierPrime.add(Tag.EQ);
        identifierPrime.add(Tag.GT);
        identifierPrime.add(Tag.GE);
        identifierPrime.add(Tag.LT);
        identifierPrime.add(Tag.LE);
        identifierPrime.add(Tag.NOT);
        identifierPrime.add(Tag.MINUS);
        identifierPrime.add(Tag.OR);
        identifierPrime.add(Tag.PLUS);
        identifierPrime.add(Tag.MULTIPLICATION);
        identifierPrime.add(Tag.DIVISION);
        identifierPrime.add(Tag.AND);
        follow.put("identifierPrime", identifierPrime);
    
        //identifier'
        // [A-Za-z],_, [0-9],;, =,), then, end,==, >, >=, <, <=, <>, -, ||, +, *,/, &&
        List<Integer> letter = new ArrayList<>();
        letter.add(Tag.IDENTIFIER);
        letter.add(Tag.UNDERSCORE); // UNDERSCORE
        letter.add(Tag.DIGIT);
        letter.add(Tag.DOT_COMMA);
        letter.add(Tag.ASSIGN);
        letter.add(Tag.CLOSE_PARENTHESES);
        letter.add(Tag.THEN);
        letter.add(Tag.END);
        letter.add(Tag.EQ);
        letter.add(Tag.GT);
        letter.add(Tag.GE);
        letter.add(Tag.LT);
        letter.add(Tag.LE);
        letter.add(Tag.NOT);
        letter.add(Tag.MINUS);
        letter.add(Tag.OR);
        letter.add(Tag.PLUS);
        letter.add(Tag.MULTIPLICATION);
        letter.add(Tag.DIVISION);
        letter.add(Tag.AND);
        follow.put("letter", letter);

        //identifier'
        // ), then, end,==, >, >=, <, <=, <>, -, ||, +, *, [A-Za-z],_, [0-9],;, =,/, &&
        List<Integer> digit = new ArrayList<>();
        digit.add(Tag.CLOSE_PARENTHESES);
        digit.add(Tag.THEN);
        digit.add(Tag.END);
        digit.add(Tag.EQ);
        digit.add(Tag.GT);
        digit.add(Tag.GE);
        digit.add(Tag.LT);
        digit.add(Tag.LE);
        digit.add(Tag.NOT);
        digit.add(Tag.MINUS);
        digit.add(Tag.OR);
        digit.add(Tag.PLUS);
        digit.add(Tag.MULTIPLICATION);
        digit.add(Tag.IDENTIFIER);
        digit.add(Tag.UNDERSCORE); // UNDERSCORE
        digit.add(Tag.DIGIT);
        digit.add(Tag.DOT_COMMA);
        digit.add(Tag.ASSIGN);
        digit.add(Tag.DIVISION);
        digit.add(Tag.AND);
        follow.put("digit", digit);

        List<Integer> caractere = new ArrayList<>();
        caractere.add(Tag.LITERAL);  // }
        follow.put("caractere", caractere);
    }

    public List<Integer> getFollow(String key) {
        return follow.get(key);
    }
}
