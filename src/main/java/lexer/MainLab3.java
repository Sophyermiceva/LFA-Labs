package lexer;

import java.util.List;

public class MainLab3 {

  public static void main(String[] args) {
    String input = """
        let x = 12;
        let y = 3.14;
        if (x >= 10) {
            return sin(x) + cos(y) / 2;
        } else {
            return x != y;
        }
        """;

    Lexer lexer = new Lexer(input);
    List<Token> tokens = lexer.tokenize();

    for (Token token : tokens) {
      System.out.println(token);
    }
  }
}