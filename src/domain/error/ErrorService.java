package domain.error;

public class ErrorService {
    public void showError(int line, String grammar) {
        System.out.println(" Erro Sintático na linha " + line + " e produção " + grammar) ;
    }
}
