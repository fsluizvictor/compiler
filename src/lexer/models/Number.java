package lexer.models;

public class Number extends Token {

    public final int value;

    public Number(int value) {
        super(Tag.DIGIT);
        this.value = value;
    }

    public String toString() {
        return "" + value;
    }
}
