import parser.services.ParserService;

public class runner {
    public static void main(String[] args) {
        while(true){
            String fileName = "/home/victor/Projects/compiler/tests/parserTests/changed/test1v3.txt";
            ParserService parser;
            try {
                parser = new ParserService(fileName);
                parser.start();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
}
