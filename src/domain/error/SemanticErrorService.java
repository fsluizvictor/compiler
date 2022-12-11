package domain.error;

public class SemanticErrorService {
    public void unexpectedTagError(int expected, int entry, int line) {
        System.out.println("Erro Semântico - Tipo não experado - Experado : " + expected + " Enviado : " + entry + " Linha : " + line);
    }

    public void variableAlreadyDeclaredError(String lexeme, int line) {
        System.out.println("Erro Semântico - Variável já declarada : " + lexeme + " Linha : " + line);
    }

    public void variableNotDeclaredError(String lexeme, int line) {
        System.out.println("Erro Semântico - Variável não declarada : " + lexeme + " Linha : " + line);
    }
}
