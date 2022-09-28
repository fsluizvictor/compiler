package lexer.models;

public class IntegerConst extends Token {

    public final int value;

    public IntegerConst(int value) {
        super(Tag.DIGIT);
        this.value = value;
    }

    public String toString() {
        return "" + value;
    }
}
