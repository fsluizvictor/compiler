package semantic.service;

public class SemanticErrorService {
    public void unexpectedTagError(int expected, int entry) {

    }

    public void variableAlreadyDeclaredError() {

    }

    public void variableNotDeclaredError(String lexeme) {
        System.out.println("Variável não declarada : " + lexeme);
    }
}
