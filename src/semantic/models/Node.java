package semantic.models;

import lexer.models.Tag;

public class Node {
    public int type = Tag.VOID_SEMANTIC_TAG;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
