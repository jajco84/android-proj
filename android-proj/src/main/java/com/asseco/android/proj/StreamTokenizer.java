package com.asseco.android.proj;

import java.io.StringReader;


/**
 * The StreamTokenizer class takes an input stream and parses it into "tokens", allowing the tokens to be read one at a time. The parsing process is controlled by a table and a number of flags that can be set to various states. The stream tokenizer can recognize identifiers, numbers, quoted strings, and various comment style
 *
 * This is a crude c# implementation of Java's StreamTokenizer class.
 */
public class StreamTokenizer {
    //private final NumberFormatInfo _nfi = CultureInfo.InvariantCulture.NumberFormat;
    private TokenType _currentTokenType = TokenType.Word;
    private StringReader _reader;
    private String _currentToken = new String();
    private int _lineNumber = 1;
    private int _colNumber = 1;
    private boolean _ignoreWhitespace;

    /**
     * Initializes a new instance of the StreamTokenizer class.
     *
     * @param reader           A TextReader with some text to read.
     * @param ignoreWhitespace Flag indicating whether whitespace should be ignored.
     * @throws Exception the exception
     */
    public StreamTokenizer(StringReader reader, boolean ignoreWhitespace) throws Exception {
        //if (reader == null)
        //throw new  Exception("reader");

        _reader = reader;
        _ignoreWhitespace = ignoreWhitespace;
    }

    /**
     * Determines a characters type (e.g. number, symbols, character).
     *
     * @param character The character to determine.
     * @return The TokenType the character is.
     */
    private static TokenType getType(char character) throws Exception {
        if (Character.isDigit(character))
            return TokenType.Number;

        if (Character.isLetter(character))
            return TokenType.Word;

        if (character == '\n')
            return TokenType.Eol;

        if (Character.isWhitespace(character) || Character.isISOControl(character))
            return TokenType.Whitespace;

        return TokenType.Symbol;
    }

    /**
     * The current line number of the stream being read.
     *
     * @return the line number
     * @throws Exception the exception
     */
    public int getLineNumber() throws Exception {
        return _lineNumber;
    }

    /**
     * The current column number of the stream being read.
     *
     * @return the column
     * @throws Exception the exception
     */
    public int getColumn() throws Exception {
        return _colNumber;
    }

    /**
     * Gets ignore whitespace.
     *
     * @return the ignore whitespace
     * @throws Exception the exception
     */
    public boolean getIgnoreWhitespace() throws Exception {
        return _ignoreWhitespace;
    }

    /**
     * If the current token is a number, this field contains the value of that number.
     *
     * If the current token is a number, this field contains the value of that number. The current token is a number when the value of the ttype field is TT_NUMBER.
     *
     * FormatException Current token is not a number in a valid format.
     *
     * @return the numeric value
     * @throws Exception the exception
     */
    public double getNumericValue() throws Exception {
        String number = getStringValue();
        if (getTokenType() == TokenType.Number)
            return Double.parseDouble(number);

        String s = String.format("The token '%s' is not a number at line %d column %d.", number, getLineNumber(), getColumn());
        throw new IllegalArgumentException(s);
    }

    /**
     * If the current token is a word token, this field contains a string giving the characters of the word token.
     *
     * @return the string value
     * @throws Exception the exception
     */
    public String getStringValue() throws Exception {
        return _currentToken;
    }

    /**
     * Gets the token type of the current token.
     *
     * @return token type
     * @throws Exception the exception
     */
    public TokenType getTokenType() throws Exception {
        return _currentTokenType;
    }

    /**
     * Returns the next token.
     *
     * @param ignoreWhitespace Determines is whitespace is ignored. True if whitespace is to be ignored.
     * @return The TokenType of the next token.
     * @throws Exception the exception
     */
    public TokenType nextToken(boolean ignoreWhitespace) throws Exception {
        return ignoreWhitespace ? nextNonWhitespaceToken() : nextTokenAny();
    }

    /**
     * Returns the next token.
     *
     * @return The TokenType of the next token.
     * @throws Exception the exception
     */
    public TokenType nextToken() throws Exception {
        return nextToken(getIgnoreWhitespace());
    }

    private TokenType nextTokenAny() throws Exception {
        _currentToken = "";
        _currentTokenType = TokenType.Eof;
        int finished = _reader.read();
        boolean isNumber = false;
        boolean isWord = false;
        while (finished != -1) {
            char currentCharacter = (char) finished;
            // char nextCharacter = (char)_reader.Peek();
            _reader.mark(1);
            char nextCharacter = (char) _reader.read();
            _reader.reset();

            _currentTokenType = getType(currentCharacter);
            TokenType nextTokenType = getType(nextCharacter);
            // handling of words with _
            if (isWord && currentCharacter == '_')
                _currentTokenType = TokenType.Word;

            // handing of words ending in numbers
            if (isWord && _currentTokenType == TokenType.Number)
                _currentTokenType = TokenType.Word;

            if (!isNumber) {
                if (_currentTokenType == TokenType.Word && nextCharacter == '_') {
                    //enable words with _ inbetween
                    nextTokenType = TokenType.Word;
                    isWord = true;
                }

                if (_currentTokenType == TokenType.Word && nextTokenType == TokenType.Number) {
                    //enable words ending with numbers
                    nextTokenType = TokenType.Word;
                    isWord = true;
                }

            }

            // handle negative numbers
            if (currentCharacter == '-' && nextTokenType == TokenType.Number && isNumber == false) {
                _currentTokenType = TokenType.Number;
                nextTokenType = TokenType.Number;
            }

            // this handles numbers with a decimal point
            if (isNumber && nextTokenType == TokenType.Number && currentCharacter == '.')
                _currentTokenType = TokenType.Number;

            if (_currentTokenType == TokenType.Number && nextCharacter == '.' && isNumber == false) {
                nextTokenType = TokenType.Number;
                isNumber = true;
            }

            // this handles numbers with a scientific notation
            if (isNumber) {
                if (_currentTokenType == TokenType.Number && nextCharacter == 'E') {
                    nextTokenType = TokenType.Number;
                }

                if (currentCharacter == 'E' && (nextCharacter == '-' || nextCharacter == '+')) {
                    _currentTokenType = TokenType.Number;
                    nextTokenType = TokenType.Number;
                }

                if ((currentCharacter == 'E' || currentCharacter == '-' || currentCharacter == '+') && nextTokenType == TokenType.Number) {
                    _currentTokenType = TokenType.Number;
                }

            }

            _colNumber++;
            if (_currentTokenType == TokenType.Eol) {
                _lineNumber++;
                _colNumber = 1;
            }

            _currentToken = _currentToken + currentCharacter;
            if (_currentTokenType != nextTokenType)
                finished = -1;
            else if (_currentTokenType == TokenType.Symbol && currentCharacter != '-')
                finished = -1;
            else
                finished = _reader.read();
        }
        return _currentTokenType;
    }

    /**
     * Returns next token that is not whitespace.
     *
     * @return
     */
    private TokenType nextNonWhitespaceToken() throws Exception {
        TokenType tokentype = nextTokenAny();
        while (tokentype == TokenType.Whitespace || tokentype == TokenType.Eol)
            tokentype = nextTokenAny();
        return tokentype;
    }

}


