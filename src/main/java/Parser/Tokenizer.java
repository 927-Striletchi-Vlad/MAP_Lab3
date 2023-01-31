package Parser;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.LinkedList;

public class Tokenizer {
    
    private class TokenInfo {
        public final Pattern regex;
        public final int token;

        public TokenInfo(Pattern regex, int token) {
            super();
            this.regex = regex;
            this.token = token;
        }
    }

    public static class Token {
        public final int token;
        public final String sequence;

        public Token(int token, String sequence) {
            super();
            this.token = token;
            this.sequence = sequence;
        }
    }

    private LinkedList<TokenInfo> tokenInfos;
    private LinkedList<Token> tokens;

    public Tokenizer() {
        tokenInfos = new LinkedList<TokenInfo>();
        tokens = new LinkedList<Token>();
        init_symbols();
    }

    private void init_symbols(){
        add("int|bool|string|ref", 1);
        add("print|if|openRFile|readFile|closeRFile|rH|wH|thread|new|while|for|createLock|lock|unlock|nop", 2);
        add("then|else", 3);
        add("==|!=|<=|>=|<|>", 4);
        add("\\(", 5);
        add("\\)", 6);
        add(",",7);
        add("[+-]", 8);
        add("[*/]", 9);
        add(";", 10);
        add("=", 11);
        add("[0-9]+", 12);
        add("[a-zA-Z.][a-zA-Z0-9.]*", 13);
        add("\\{", 14);
        add("\\}", 15);
        add("\"", 16);
    }

    public void add(String regex, int token) {
        tokenInfos.add(new TokenInfo(Pattern.compile("^("+regex+")"), token));
    }

    public void tokenize(String str) {
        String s = "(" + new String(str) + ")";
        tokens.clear();
        while (!s.equals("")) {
            boolean match = false;

            for (TokenInfo info : tokenInfos) {
                Matcher m = info.regex.matcher(s);

                if (m.find()) {
                    match = true;
                    String tok = m.group().trim();
                    s = m.replaceFirst("");
                    s = s.trim();
                    tokens.add(new Token(info.token, tok));
                    break;
                }
            }

            if (!match) throw new RuntimeException("Unexpected character in input: "+s);
        }
    }

    public LinkedList<Token> getTokens() {
        return tokens;
    }

    
}

