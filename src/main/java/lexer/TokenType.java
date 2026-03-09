package lexer;

public enum TokenType {
  // Special
  EOF,

  // Literals
  INTEGER,
  FLOAT,
  IDENTIFIER,

  // Keywords
  IF,
  ELSE,
  LET,
  RETURN,
  SIN,
  COS,

  // Operators
  ASSIGN, // =
  EQUALS, // ==
  NOT_EQUALS, // !=
  PLUS, // +
  MINUS,  // -
  MULTIPLY, // *
  DIVIDE, // /
  LESS, // <
  GREATER,  // >
  LESS_EQUAL, // <=
  GREATER_EQUAL,  // >=

  // Delimiters
  LPAREN, // (
  RPAREN, // )
  LBRACE, // {
  RBRACE, // }
  COMMA,  // ,
  SEMICOLON // ;
}