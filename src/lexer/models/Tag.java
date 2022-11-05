package lexer.models;

public class Tag {

    public final static int
            // Reserved words
            PROGRAM = 256,
            START = 257,
            EXIT = 258,
            INT = 259,
            FLOAT = 260,
            STRING = 261,
            IF = 262,
            THEN = 263,
            END = 264,
            ELSE = 265,
            DO = 266,
            WHILE = 267,
            SCAN = 268,
            PRINT = 269,

            // Comments
            COMMENT_LINE = 270, // "//"
            COMMENT_BLOCK = 271, // "/**/"

            // SYMBOLS
            OPEN_PARENTHESES = 40, // "("
            CLOSE_PARENTHESES = 41, // ")"
            DOT_COMMA = 59, // ";"
            COMMA = 44, // ","
            ASSIGN = 61, // "="

            // MATH SYMBOLS
            PLUS = 43, // "+"
            MINUS = 45, // "-"
            MULTIPLICATION = 42, // "*"
            DIVISION = 47, // "/"

            // LOGIC SYMBOLS
            EQ = 282, // "==",
            GT = 62, // ">",
            GE = 284, // ">=",
            LT = 60, // "<",
            LE = 286, // "<=",
            //NE = 287, // "!=",
            OR = 288, // "||",
            AND = 289, // "&&",
            NOT = 33, // "!",
            NE = 290, // "<>",


            // ANOTHERS TOKENS
            IDENTIFIER = 291,
            NUMBER = 292,
            DIGIT = 293,
            FLOAT_CONST = 294,
            LITERAL = 295,
            UNDERSCORE = 298,

            ERROR = 296,
            EOF = 297;
            
}
