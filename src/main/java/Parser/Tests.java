package Parser;

import Model.Statement.IStmt;

public class Tests {

    public static void testTokenizer(){
        Tokenizer tokenizer = new Tokenizer();

        tokenizer.tokenize("int a = 5; int b = 6; print(a + b);");
        for (Tokenizer.Token token : tokenizer.getTokens()) {
            System.out.println(token.token + " " + token.sequence);
        }
        System.out.println("\n");

        tokenizer.tokenize("""
                Ref int v;new(v,20);
                Ref Ref int a;
                new(a,v);
                print(rH(v));
                print(rH(rH(a))+5)
                """);
        for (Tokenizer.Token token : tokenizer.getTokens()) {
            System.out.println(token.token + " " + token.sequence);
        }
        System.out.println("\n");

        tokenizer.tokenize("""
                string varf;
                varf="test.in";
                openRFile(varf);
                int varc;
                readFile(varf,varc);
                print(varc);
                readFile(varf,varc);
                print(varc);
                closeRFile(varf);
                """);
        for (Tokenizer.Token token : tokenizer.getTokens()) {
            System.out.println(token.token + " " + token.sequence);
        }
        System.out.println("\n");
    }

    private static void testParser() {
        Tokenizer tokenizer = new Tokenizer();
        Parser parser = new Parser(tokenizer);
        IStmt stmt1 = parser.parse("int a; a = 5; int b; b = 6; print(a + b);");
        System.out.println(stmt1.toString());
        System.out.println("\n");
        IStmt stmt2 = parser.parse("""
                ref int v;new(v,20);
                ref ref int a;
                new(a,v);
                print(rH(v));
                print(rH(rH(a))+5);
                """);
        System.out.println(stmt2.toString());
        System.out.println("\n");
        IStmt stmt3 = parser.parse("""
                int v; v=4; while (v>0 (print(v);v=v-1;));print(v);
                """);
        System.out.println(stmt3.toString());
        System.out.println("\n");
        IStmt stmt4 = parser.parse("""
                int v; ref int a; v=10; new(a,22);
                thread(wH(a,30);v=32;print(v);print(rH(a)););
                print(v);print(rH(a));
                """);
        System.out.println(stmt4.toString());
        System.out.println("\n");
        IStmt stmt5 = parser.parse("""
                string varf;
                varf="test.in";
                openRFile(varf);
                int varc;
                readFile(varf,varc);
                print(varc);
                readFile(varf,varc);
                print(varc);
                closeRFile(varf);
                """);
        System.out.println(stmt5.toString());
        System.out.println("\n");

        IStmt stmt6 = parser.parse("""
               ref int v1; ref int v2; int x; int q;
               new(v1,20);new(v2,30);createLock(x);
               thread(
                thread(
                 lock(x);wH(v1,rH(v1)-1);unlock(x);
                 );
                 lock(x);wH(v1,rH(v1)*10);unlock(x);
                );
                lock(x); print(rH(v1)); unlock(x);
                """);
        System.out.println(stmt6.toString());
        System.out.println("\n");
        IStmt stmt7 = parser.parse("""
                int a; a=1; if(a==0 then (v=2;) else (v=3;));print(v);\s
                """);
        System.out.println(stmt7.toString());
        System.out.println("\n");



    }
    public static void main(String[] args) {
//        testTokenizer();
        testParser();
    }


}
