package lexer.models;

public class Literal extends Token {

    public final String literal;

    public Literal(String literal) {
        super(Tag.LITERAL);
        this.literal = literal;
    }

    public String toString() {
        return "" + literal;
    }

    public String getLiteral() {
        return literal;
    }
}
