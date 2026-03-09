package lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
  private final String input;
  private int position;

  public Lexer(String input) {
    this.input = input;
    this.position = 0;
  }

  public List<Token> tokenize() {
    List<Token> tokens = new ArrayList<>();

    while (!isAtEnd()) {
      char current = peek();

      if (Character.isWhitespace(current)) {
        skipWhitespace();
        continue;
      }

      if (Character.isDigit(current)) {
        tokens.add(readNumber());
        continue;
      }

      if (Character.isLetter(current) || current == '_') {
        tokens.add(readIdentifierOrKeyword());
        continue;
      }

      switch (current) {
        case '=':
          if (match('=')) {
            tokens.add(new Token(TokenType.EQUALS, "=="));
          } else {
            advance();
            tokens.add(new Token(TokenType.ASSIGN, "="));
          }
          break;

        case '!':
          advance();
          if (match('=')) {
            tokens.add(new Token(TokenType.NOT_EQUALS, "!="));
          } else {
            throw new IllegalArgumentException("Unexpected character: !");
          }
          break;

        case '+':
          advance();
          tokens.add(new Token(TokenType.PLUS, "+"));
          break;

        case '-':
          advance();
          tokens.add(new Token(TokenType.MINUS, "-"));
          break;

        case '*':
          advance();
          tokens.add(new Token(TokenType.MULTIPLY, "*"));
          break;

        case '/':
          advance();
          tokens.add(new Token(TokenType.DIVIDE, "/"));
          break;

        case '<':
          advance();
          if (match('=')) {
            tokens.add(new Token(TokenType.LESS_EQUAL, "<="));
          } else {
            tokens.add(new Token(TokenType.LESS, "<"));
          }
          break;

        case '>':
          advance();
          if (match('=')) {
            tokens.add(new Token(TokenType.GREATER_EQUAL, ">="));
          } else {
            tokens.add(new Token(TokenType.GREATER, ">"));
          }
          break;

        case '(':
          advance();
          tokens.add(new Token(TokenType.LPAREN, "("));
          break;

        case ')':
          advance();
          tokens.add(new Token(TokenType.RPAREN, ")"));
          break;

        case '{':
          advance();
          tokens.add(new Token(TokenType.LBRACE, "{"));
          break;

        case '}':
          advance();
          tokens.add(new Token(TokenType.RBRACE, "}"));
          break;

        case ',':
          advance();
          tokens.add(new Token(TokenType.COMMA, ","));
          break;

        case ';':
          advance();
          tokens.add(new Token(TokenType.SEMICOLON, ";"));
          break;

        default:
          throw new IllegalArgumentException(
              "Unexpected character: '" + current + "' at position " + position
          );
      }
    }

    tokens.add(new Token(TokenType.EOF, ""));
    return tokens;
  }

  private Token readNumber() {
    StringBuilder builder = new StringBuilder();
    boolean hasDot = false;

    while (!isAtEnd()) {
      char current = peek();

      if (Character.isDigit(current)) {
        builder.append(current);
        advance();
      } else if (current == '.' && !hasDot) {
        hasDot = true;
        builder.append(current);
        advance();
      } else {
        break;
      }
    }

    String number = builder.toString();

    if (number.endsWith(".")) {
      throw new IllegalArgumentException("Invalid float number: " + number);
    }

    if (hasDot) {
      return new Token(TokenType.FLOAT, number);
    }
    return new Token(TokenType.INTEGER, number);
  }

  private Token readIdentifierOrKeyword() {
    StringBuilder builder = new StringBuilder();

    while (!isAtEnd()) {
      char current = peek();

      if (Character.isLetterOrDigit(current) || current == '_') {
        builder.append(current);
        advance();
      } else {
        break;
      }
    }

    String word = builder.toString();

    switch (word) {
      case "if":
        return new Token(TokenType.IF, word);
      case "else":
        return new Token(TokenType.ELSE, word);
      case "let":
        return new Token(TokenType.LET, word);
      case "return":
        return new Token(TokenType.RETURN, word);
      case "sin":
        return new Token(TokenType.SIN, word);
      case "cos":
        return new Token(TokenType.COS, word);
      default:
        return new Token(TokenType.IDENTIFIER, word);
    }
  }

  private void skipWhitespace() {
    while (!isAtEnd() && Character.isWhitespace(peek())) {
      advance();
    }
  }

  private char peek() {
    return input.charAt(position);
  }

  private void advance() {
    position++;
  }

  private boolean match(char expected) {
    if (isAtEnd()) {
      return false;
    }
    if (input.charAt(position) != expected) {
      return false;
    }
    position++;
    return true;
  }

  private boolean isAtEnd() {
    return position >= input.length();
  }
}