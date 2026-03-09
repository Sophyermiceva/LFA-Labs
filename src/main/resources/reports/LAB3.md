
# Lexer Implementation in Java

## Overview

This project implements a simple lexer in Java.  
A lexer, also called a scanner or tokenizer, is a component used in lexical analysis. Its purpose is to read an input string character by character and convert it into a sequence of tokens.

Lexical analysis is one of the first stages of processing in a compiler or interpreter. Instead of working with raw text directly, the system first transforms the source code into structured elements such as keywords, identifiers, numbers, operators, and delimiters.

This implementation was created for a small expression-based language. It supports integer and floating-point numbers, identifiers, keywords, arithmetic operators, comparison operators, parentheses, braces, commas, and semicolons. It also recognizes trigonometric functions such as `sin` and `cos`.

## Objectives

The main objectives of this project are:

- to understand what lexical analysis is;
- to get familiar with how a lexer works internally;
- to implement a sample lexer in Java;
- to demonstrate how the lexer transforms input text into tokens.

## Project Structure

```text
src/main/java/org/example/lexer/
    Main.java
    Lexer.java
    Token.java
    TokenType.java
````

### File Description

**Main.java**
Contains the entry point of the program. It provides sample input and prints the generated tokens.

**Lexer.java**
Contains the main lexical analysis logic. It scans the input string from left to right and creates tokens.

**Token.java**
Represents a token. Each token has a type and a lexeme.

**TokenType.java**
Defines all supported token types using an enum.

## Supported Token Types

The lexer recognizes the following categories of tokens:

### Keywords

* `if`
* `else`
* `let`
* `return`
* `sin`
* `cos`

### Literals

* integers, for example: `12`
* floating-point numbers, for example: `3.14`

### Identifiers

* variable and function-like names, for example: `x`, `result`, `value1`

### Operators

* `=`
* `==`
* `!=`
* `+`
* `-`
* `*`
* `/`
* `<`
* `>`
* `<=`
* `>=`

### Delimiters

* `(`
* `)`
* `{`
* `}`
* `,`
* `;`

### Special Token

* `EOF` - marks the end of input

## How the Lexer Works

The lexer processes the input string one character at a time.

If the current character is whitespace, it is skipped.

If the current character is a digit, the lexer starts reading a numeric literal. It continues until the full integer or floating-point number is formed.

If the current character is a letter or underscore, the lexer starts reading an identifier. After the full word is read, the lexer checks whether it is a reserved keyword such as `if`, `else`, `sin`, or `cos`. If yes, it creates a keyword token; otherwise, it creates an identifier token.

If the current character is an operator or delimiter, the lexer creates the corresponding token. It also supports multi-character operators such as `==`, `!=`, `<=`, and `>=`.

If an unexpected character is found, the lexer throws an error.

At the end of the input, the lexer adds an `EOF` token.

## Example Input

```text
let x = 12;
let y = 3.14;
if (x >= 10) {
    return sin(x) + cos(y) / 2;
} else {
    return x != y;
}
```

## Example Output

```text
Token{type=LET, lexeme='let'}
Token{type=IDENTIFIER, lexeme='x'}
Token{type=ASSIGN, lexeme='='}
Token{type=INTEGER, lexeme='12'}
Token{type=SEMICOLON, lexeme=';'}
Token{type=LET, lexeme='let'}
Token{type=IDENTIFIER, lexeme='y'}
Token{type=ASSIGN, lexeme='='}
Token{type=FLOAT, lexeme='3.14'}
Token{type=SEMICOLON, lexeme=';'}
Token{type=IF, lexeme='if'}
Token{type=LPAREN, lexeme='('}
Token{type=IDENTIFIER, lexeme='x'}
Token{type=GREATER_EQUAL, lexeme='>='}
Token{type=INTEGER, lexeme='10'}
Token{type=RPAREN, lexeme=')'}
Token{type=LBRACE, lexeme='{'}
Token{type=RETURN, lexeme='return'}
Token{type=SIN, lexeme='sin'}
Token{type=LPAREN, lexeme='('}
Token{type=IDENTIFIER, lexeme='x'}
Token{type=RPAREN, lexeme=')'}
Token{type=PLUS, lexeme='+'}
Token{type=COS, lexeme='cos'}
Token{type=LPAREN, lexeme='('}
Token{type=IDENTIFIER, lexeme='y'}
Token{type=RPAREN, lexeme=')'}
Token{type=DIVIDE, lexeme='/'}
Token{type=INTEGER, lexeme='2'}
Token{type=SEMICOLON, lexeme=';'}
Token{type=RBRACE, lexeme='}'}
Token{type=ELSE, lexeme='else'}
Token{type=LBRACE, lexeme='{'}
Token{type=RETURN, lexeme='return'}
Token{type=IDENTIFIER, lexeme='x'}
Token{type=NOT_EQUALS, lexeme='!='}
Token{type=IDENTIFIER, lexeme='y'}
Token{type=SEMICOLON, lexeme=';'}
Token{type=RBRACE, lexeme='}'}
Token{type=EOF, lexeme=''}
```

## Why This Project Fits the Assignment

This project satisfies the assignment requirements because:

* it demonstrates understanding of lexical analysis;
* it explains the internal logic of a lexer;
* it implements a working sample lexer in Java;
* it supports more than a minimal calculator language by handling integers, floats, conditions, comparison operators, and trigonometric functions.

## Conclusion

This project demonstrates how a lexer transforms raw input text into a structured stream of tokens. It shows the importance of lexical analysis as an early stage in language processing. The implementation is simple enough to understand clearly, but also rich enough to tokenize a more expressive mini-language.
