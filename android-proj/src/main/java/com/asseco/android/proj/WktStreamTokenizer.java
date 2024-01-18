package com.asseco.android.proj;


import java.io.StringReader;

/**
 * Reads a stream of Well Known Text (wkt) string and returns a stream of tokens.
 */
public class WktStreamTokenizer extends StreamTokenizer {
    /**
     * Initializes a new instance of the WktStreamTokenizer class.
     * The WktStreamTokenizer class ais in reading WKT streams.
     *
     * @param reader A TextReader that contains
     * @throws Exception the exception
     */
    public WktStreamTokenizer(StringReader reader) throws Exception {
        super(reader, true);
    }

    /**
     * Reads a token and checks it is what is expected.
     *
     * @param expectedToken The expected token.
     * @throws Exception the exception
     */
    public void readToken(String expectedToken) throws Exception {
        nextToken();
        if (!getStringValue().equals(expectedToken)) {
            String s = String.format("Expecting ('%s') but got a '%s' at line %d column %d.", expectedToken, getStringValue(), getLineNumber(), getColumn());
            throw new IllegalArgumentException(s);
        }

    }

    /**
     * Reads a string inside double quotes.
     *
     * White space inside quotes is preserved.
     *
     * @return The string inside the double quotes.
     * @throws Exception the exception
     */
    public String readDoubleQuotedWord() throws Exception {
        String word = "";
        if (!getStringValue().equals("\""))
            readToken("\"");

        nextToken(false);
        while (!getStringValue().equals("\"")) {
            word = word + getStringValue();
            nextToken(false);
        }
        return word;
    }

    /**
     * Reads the authority and authority code.
     *
     * @return the authority
     * @throws Exception the exception
     */
    public Authority readAuthority() throws Exception {
        //AUTHORITY["EPGS","9102"]]
        if (!getStringValue().equals("AUTHORITY"))
            readToken("AUTHORITY");

        Authority aut = new Authority();
        readToken("[");
        aut.authority = readDoubleQuotedWord();
        readToken(",");
        nextToken();
        if (getTokenType() == TokenType.Number)
            aut.authorityCode = (long) getNumericValue();
        else {
            // RefSupport<long> refVar___0 = new RefSupport<long>();
            // long.TryParse(readDoubleQuotedWord(), NumberStyles.Any, _nfi, refVar___0);
            //authorityCode.setValue(refVar___0.getValue());
            aut.authorityCode = Long.parseLong(readDoubleQuotedWord());
        }
        readToken("]");
        return aut;
    }

    /**
     * The type Authority.
     */
    public class Authority {
        /**
         * The Authority.
         */
        public String authority;
        /**
         * The Authority code.
         */
        public long authorityCode;
    }
}


