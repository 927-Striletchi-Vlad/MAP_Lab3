package Parser;
import Model.Expression.*;
import Model.Statement.*;
import Model.Type.*;
import Model.Value.IntValue;
import Model.Value.StringValue;
import Model.Value.Value;
import Parser.Tokenizer.Token;
import javafx.scene.shape.VLineTo;

import java.util.HashMap;
import java.util.LinkedList;

//Token 20 is for already-processed statements
//Token 21 is for already-processed expressions

/*
    RULES:
    PRINT: print (a)
    ASSIGN: a = 5
    IF: if (a == 5 then ( print (a) ) else ( print (b) ));
    WHILE: while (a == 5  (print (a)) );
    FOR: for (a = 5 ; a < 10 ; a = a + 1 ; (print (a)) );
    COMPOUND: ( print (a) ; print (b) ; )


 */
public class Parser {
    Tokenizer tokenizer;
    LinkedList<Token> tokens;
    HashMap<String, IStmt> statements;
    HashMap<String, Expression> expressions;
    public Parser(Tokenizer tokenizer){
        this.tokenizer = tokenizer;
        statements = new HashMap<>();
        expressions = new HashMap<>();
    }

    public int depth(){
        int maxDepth=0;
        int depth = 0;
        for (Token token : tokens){
            if (token.token == 5){
                depth++;
                if (depth > maxDepth){
                    maxDepth = depth;
                }
            }
            if (token.token == 6){
                depth--;
            }
        }
        return maxDepth;
    }

    public int[] findInnermostBlock(){
        int[] block = new int[2];
        int desiredDepth = depth();
        // find the start of the innermost block, which is the depth-th '('
        int start = 0;
        int end = 0;
        int i = 0;
        int depth = 0;
        for (Token token : tokens){
            if (token.token == 5){
                depth++;
                if (depth == desiredDepth){
                    start = i;
                }

            }
            if (token.token == 6){
                if (depth == desiredDepth){
                    end = i;
                    break;
                }
                depth--;
            }
            i++;
        }

        // if the innermost block starts with a keyword, extend the selection to include the keyword
        if(start>0){
            if(tokens.get(start-1).token == 2){
                start--;
            }
        }

        block[0] = start;
        block[1] = end;
        return block;
    }

    public void parseInnermostBlock(){
        int[] block = findInnermostBlock();
        int start = block[0];
        int end = block[1];

        // parse the innermost block
        if(tokens.get(start).token == 2){
            // if the innermost block starts with a keyword, parse it as a statement
            parseSingleStatement(start, end);
        } else {
            // if the innermost block doesn't start with a keyword, parse it as a
            // sequence of statements that must be combined into a CompoundStmt
            parseCompoundStatement(start, end);
        }
    }

    private void parseSingleStatement(int start, int end) {
        String keyword = tokens.get(start).sequence;
        tokens.remove(start);
        end--;
        switch(keyword){
            case "print":
                parsePrintStatement(start, end);
                break;
            case "if":
                parseIfStatement(start, end);
                break;
            case "while":
                parseWhileStatement(start, end);
                break;
            case "for":
                parseForStatement(start, end);
                break;
            case "openRFile":
                parseOpenRFileStatement(start, end);
                break;
            case "readFile":
                parseReadFileStatement(start, end);
                break;
            case "closeRFile":
                parseCloseRFileStatement(start, end);
                break;
            case "rH":
                parseRHStatement(start, end);
                break;
            case "wH":
                parseWHStatement(start, end);
                break;
            case "thread":
                parseThreadStatement(start, end);
                break;
            case "createLock":
                parseNewLockStatement(start, end);
                break;
            case "lock":
                parseLockStatement(start, end);
                break;
            case "unlock":
                parseUnlockStatement(start, end);
                break;
            case "new":
                parseNewStatement(start, end);
                break;
            default:
                System.out.println("Error: unknown keyword " + keyword);
                break;
        }
    }

    private void parseNewStatement(int start, int end) {
        //remove the '('
        tokens.remove(start);
        end--;
        //next token is the variable name
        String varName = tokens.get(start).sequence;
        tokens.remove(start);
        end--;
        //remove the ','
        tokens.remove(start);
        end--;
        //remove the ')'
        tokens.remove(end);
        end--;
        //between start and end is the expression, might need to be reduced
        parseSimple(start, end);
        //now at start is the expression
        Expression exp;
        switch (tokens.get(start).token){
            case 21:
                exp = expressions.get(tokens.get(start).sequence);
                break;
            case 12:
                exp = new ValueExpression(new IntValue(Integer.parseInt(tokens.get(start).sequence)));
                break;
            case 13:
                exp = new ValueExpression(new StringValue(tokens.get(start).sequence));
                break;
            default:
                exp = null;
                break;
        }
        //remove the expression
        tokens.remove(start);
        //build the NewStmt
        IStmt res = new NewStmt(varName, exp);
        statements.put(res.toString(), res);
        tokens.add(start, new Token(20, res.toString()));
    }

    private void parseUnlockStatement(int start, int end) {
        //remove the '('
        tokens.remove(start);
        //next token is the variable name
        String varName = tokens.get(start).sequence;
        tokens.remove(start);
        //remove the ')'
        tokens.remove(start);
        //build the UnlockStmt
        IStmt res = new UnlockStmt(varName);
        statements.put(res.toString(), res);
        tokens.add(start, new Token(20, res.toString()));
    }

    private void parseLockStatement(int start, int end) {
        //remove the '('
        tokens.remove(start);
        //next token is the variable name
        String varName = tokens.get(start).sequence;
        tokens.remove(start);
        //remove the ')'
        tokens.remove(start);
        //build the LockStmt
        IStmt res = new LockStmt(varName);
        statements.put(res.toString(), res);
        tokens.add(start, new Token(20, res.toString()));
    }

    private void parseNewLockStatement(int start, int end) {
        //remove the '('
        tokens.remove(start);
        //next token is the variable name
        String varName = tokens.get(start).sequence;
        tokens.remove(start);
        //remove the ')'
        tokens.remove(start);
        //build the NewLockStmt
        IStmt res = new NewLockStmt(varName);
        statements.put(res.toString(), res);
        tokens.add(start, new Token(20, res.toString()));
    }

    private void parseThreadStatement(int start, int end) {
        //reduce it to a single statement
        parseCompoundStatement(start, end);
        //the first token is the statement
        IStmt stmt = statements.get(tokens.get(start).sequence);
        tokens.remove(start);
        //build the ThreadStmt
        IStmt res = new ThreadStmt(stmt);
        statements.put(res.toString(), res);
        tokens.add(start, new Token(20, res.toString()));
    }

    private void parseCompoundStatement(int start, int end) {
        //remove the '('
        tokens.remove(start);
        end--;


        //store all the ';' positions, going backwards
        LinkedList<Integer> semicolons = new LinkedList<>();
        for (int i = end; i >= start; i--){
            if (tokens.get(i).token == 10){
                semicolons.add(i);
            }
        }
        //parse the statements
        for (int startSemicolon : semicolons){
            parseSimple(startSemicolon + 1, end);
            end = startSemicolon - 1;
        }
        //parse the very first statement
        parseSimple(start, end);
        //find the end again,the first ')' is the end
        for (int i = start; i <= tokens.size()-1; i++){
            if (tokens.get(i).token == 6){
                end = i;
                break;
            }
        }
        //remove the ')'
        tokens.remove(end);
        end--;
        //remove tha last ';'
        tokens.remove(end);
        end--;
        //start building the CompoundStmt
        IStmt res = statements.get(tokens.get(end).sequence);
        tokens.remove(end);
        end--;
        //skip the ';', if there is one
        if(end >= start) {
            tokens.remove(end);
            end--;
        }
        while (end >= start){
            IStmt stmt = statements.get(tokens.get(end).sequence);
            tokens.remove(end);
            res = new CompoundStmt(stmt, res);
            statements.put(res.toString(), res);
            end--;
            //skip the ';', if there is one
            if(end >= start) {
                tokens.remove(end);
                end--;
            }
        }
        //add the CompoundStmt to the statements list
        statements.put(res.toString(), res);
        tokens.add(start, new Token(20, res.toString()));
    }

    private void parseWHStatement(int start, int end) {
        //remove the ')'
        tokens.remove(end);
        end--;
        //remove the '('
        tokens.remove(start);
        //the first token is the variable
        String var = tokens.get(start).sequence;
        tokens.remove(start);
        //next token is ',', remove it
        tokens.remove(start);
        //the next token is the expression
        //parse the expression
        parseArithmeticExpression(start, end);
        //after parsing the expression, the next token is the expression
        Expression exp = expressions.get(tokens.get(start).sequence);
        tokens.remove(start);
        //build the WriteHeapStmt
        IStmt res = new WriteHeapStmt(var, exp);
        statements.put(res.toString(), res);
        tokens.add(start, new Token(20, res.toString()));
    }

    private void parseRHStatement(int start, int end) {
        //remove the ')'
        tokens.remove(end);
        end--;
        //remove the '('
        tokens.remove(start);
        end--;
        //the last token is the variable
        String var = tokens.get(end).sequence;
        tokens.remove(end);
        end--;
        Expression res = new ReadHeapExpression(new VariableExpression(var));
        expressions.put(res.toString(), res);
        tokens.add(start, new Token(21, res.toString()));
    }

    private void parseCloseRFileStatement(int start, int end) {
        //remove the ')'
        tokens.remove(end);
        end--;
        //remove the '('
        tokens.remove(start);
        //the last token is the filename
        String filename = tokens.get(end).sequence;
        tokens.remove(end);
        end--;
        IStmt res = new CloseReadFileStmt(new ValueExpression(new StringValue(filename)));
        statements.put(res.toString(), res);
        tokens.add(start, new Token(20, res.toString()));
    }

    private void parseReadFileStatement(int start, int end) {
        //remove the ')'
        tokens.remove(end);
        end--;
        //remove the '('
        tokens.remove(start);
        end--;
        //the next token is the file name
        String filename = tokens.get(start).sequence;
        tokens.remove(start);
        //next token is ',', discard
        tokens.remove(start);
        //next token is the variable name
        String varName = tokens.get(start).sequence;
        tokens.remove(start);
        IStmt res = new ReadFileStmt(new ValueExpression(new StringValue(filename)), new StringValue(varName));
        statements.put(res.toString(), res);
        tokens.add(start, new Token(20, res.toString()));

    }

    private void parseOpenRFileStatement(int start, int end) {
        //remove the ')'
        tokens.remove(end);
        end--;
        //remove the '('
        tokens.remove(start);
        end--;
        //the last token is the filename
        String filename = tokens.get(end).sequence;
        tokens.remove(end);
        end--;
        IStmt res = new OpenReadFileStmt(new ValueExpression(new StringValue(filename)));
        statements.put(res.toString(), res);
        tokens.add(start, new Token(20, res.toString()));
    }

    private void parseForStatement(int start, int end) {
        //remove the ')'
        tokens.remove(end);
        end--;
        //remove the '('
        tokens.remove(start);
        //split the tokens into 4 parts, separated by ';'
        int[] separators = new int[3];
        int i = 0;
        int j = 0;
        for (Token token : tokens){
            if (token.token == 7){
                separators[i] = j;
                i++;
            }
            j++;
        }
        //the first part is the initialization
        //first token is the variable
        String var = tokens.get(start).sequence;
        tokens.remove(start);
        //second token is the '=', remove it
        tokens.remove(start);
        //the rest is the first expression
        parseArithmeticExpression(start, separators[0]);
        Expression firstExpression = expressions.get(tokens.get(start).sequence);
        tokens.remove(start);
        //the second part is the condition
        //skip the first token, which is the variable, and the second token, which is the '<'
        tokens.remove(start);
        tokens.remove(start);
        //the rest is the second expression
        parseArithmeticExpression(start, separators[1]);
        Expression secondExpression = expressions.get(tokens.get(start).sequence);
        tokens.remove(start);
        //the third part is the increment
        //skip the first token, which is the variable, and the second token, which is the '='
        tokens.remove(start);
        tokens.remove(start);
        //the rest is the third expression
        parseArithmeticExpression(start, separators[2]);
        Expression thirdExpression = expressions.get(tokens.get(start).sequence);
        tokens.remove(start);
        //the last token is the statement
        IStmt statement = statements.get(tokens.get(end).sequence);
        tokens.remove(end);
        //build the ForStmt
        IStmt res = new ForStmt(var, firstExpression, secondExpression, thirdExpression, statement);
        statements.put(res.toString(), res);
        tokens.add(start, new Token(20, res.toString()));

    }

    private void parseWhileStatement(int start, int end) {
        //remove the ')'
        tokens.remove(end);
        end--;
        //remove the '('
        tokens.remove(start);
        end--;
        //the last token is the statement
        IStmt statement = statements.get(tokens.get(end).sequence);
        tokens.remove(end);
        end--;
        //what is left is the condition
        parseLogicExpression(start, end);
        Expression condition = expressions.get(tokens.get(start).sequence);
        tokens.remove(start);
        //build the WhileStmt
        IStmt res = new WhileStmt(condition, statement);
        statements.put(res.toString(), res);
        tokens.add(start, new Token(20, res.toString()));

    }

    private void parseIfStatement(int start, int end) {
        //remove the '('
        tokens.remove(start);
        end--;
        //find the "then" as the stopping point, and reduce the condition
        int i = start;
        while(tokens.get(i).token != 3){
            i++;
        }
        //reduce the condition
        parseLogicExpression(start, i-1);

        //condition is the first token
        Expression condition = expressions.get(tokens.get(start).sequence);

        tokens.remove(start);
        //remove the "then"
        tokens.remove(start);
        //the first statement is the next token
        IStmt firstStatement = statements.get(tokens.get(start).sequence);
        tokens.remove(start);
        //remove the "else"
        tokens.remove(start);
        //the second statement is the next token
        IStmt secondStatement = statements.get(tokens.get(start).sequence);
        tokens.remove(start);
        //remove the ')'
        tokens.remove(start);
        //create the IfStmt
        IStmt ifStmt = new Model.Statement.IfStmt(condition, firstStatement, secondStatement);
        statements.put(ifStmt.toString(), ifStmt);
        tokens.add(start, new Token(20, ifStmt.toString()));


    }

    private void parseLogicExpression(int start, int end) {
        //first reduce the arithmetic expressions
        parseArithmeticExpression(start, end);
        //after this, there are only 3 tokens: the first operand, the operator, and the second operand
        Expression e1;
        switch(tokens.get(start).token){
            case 12:
                e1 = new ValueExpression(new IntValue(Integer.parseInt(tokens.get(start).sequence)));
                break;
            case 13:
                e1 = new VariableExpression(tokens.get(start).sequence);
                break;
            case 21:
                e1 = expressions.get(tokens.get(start).sequence);
            default:
                System.out.println("Error: unknown token " + tokens.get(start).token);
                e1 = null;
                break;
        }
        tokens.remove(start);
        String operator = tokens.get(start).sequence;
        tokens.remove(start);
        Expression e2;
        switch(tokens.get(start).token){
            case 12:
                e2 = new ValueExpression(new IntValue(Integer.parseInt(tokens.get(start).sequence)));
                break;
            case 13:
                e2 = new VariableExpression(tokens.get(start).sequence);
                break;
            case 21:
                e2 = expressions.get(tokens.get(start).sequence);
            default:
                System.out.println("Error: unknown token " + tokens.get(start).token);
                e2 = null;
                break;
        }
        tokens.remove(start);
        //build the expression
        Expression res = new LogicExpression(e1, e2, operator);
        expressions.put(res.toString(), res);
        tokens.add(start, new Token(21, res.toString()));

    }


    private int[] parseArithmeticExpression(int i, int i1) {
        //first reduce the multiplications and divisions(token 9), then the additions and subtractions(token 8)
        while(checkForMultiplicationOrDivision(i, i1)){
            reduceMultiplicationOrDivision(i, i1);
            i1 -= 2;
        }
        while(checkForAdditionOrSubtraction(i, i1)){
            reduceAdditionOrSubtraction(i, i1);
            i1 -= 2;
        }
        return new int[]{i, i1};
    }

    private void reduceAdditionOrSubtraction(int i, int i1) {
        Expression e1;
        Expression e2;

        for(int j = i; j <= i1; j++){
            if(tokens.get(j).token == 8){
                //it's an addition or subtraction
                switch(tokens.get(j-1).token){
                    case(13):
                        //it's a variable
                        e1 = new VariableExpression(tokens.get(j-1).sequence);
                        break;
                    case(12):
                        //it's a number
                        e1 = new ValueExpression(new IntValue(Integer.parseInt(tokens.get(j-1).sequence)));
                        break;
                    case(21):
                        //it's a statement
                        e1 = expressions.get(tokens.get(j-1).sequence);
                        break;
                    default:
                        System.out.println("Error: unknown token " + tokens.get(j-1).token);
                        return;
                }
                switch(tokens.get(j+1).token){
                    case(13):
                        //it's a variable
                        e2 = new VariableExpression(tokens.get(j+1).sequence);
                        break;
                    case(12):
                        //it's a number
                        e2 = new ValueExpression(new IntValue(Integer.parseInt(tokens.get(j+1).sequence)));
                        break;
                    case(21):
                        //it's a statement
                        e2 = expressions.get(tokens.get(j+1).sequence);
                        break;
                    default:
                        System.out.println("Error: unknown token " + tokens.get(j+1).token);
                        return;
                }

                Expression result = new ArithmeticExpression(tokens.get(j).sequence.charAt(0), e1, e2);
                expressions.put(result.toString(), result);
                for(int k = j-1; k <= j+1; k++){
                    tokens.remove(j-1);
                }
                tokens.add(j-1, new Token(21, result.toString()));

                return;
            }
        }
    }

    private boolean checkForAdditionOrSubtraction(int i, int i1) {
        for(int j = i; j <= i1; j++){
            if(tokens.get(j).token == 8){
                return true;
            }
        }
        return false;
    }

    private void reduceMultiplicationOrDivision(int i, int i1) {
        Expression e1;
        Expression e2;

        for(int j = i; j <= i1; j++){
            if(tokens.get(j).token == 9){
                //it's a multiplication or division
                switch(tokens.get(j-1).token){
                    case(13):
                        //it's a variable
                        e1 = new VariableExpression(tokens.get(j-1).sequence);
                        break;
                    case(12):
                        //it's a number
                        e1 = new ValueExpression(new IntValue(Integer.parseInt(tokens.get(j-1).sequence)));
                        break;
                    case(21):
                        //it's a statement
                        e1 = expressions.get(tokens.get(j-1).sequence);
                        break;
                    default:
                        System.out.println("Error: unknown token " + tokens.get(j-1).token);
                        return;
                }
                switch(tokens.get(j+1).token){
                    case(13):
                        //it's a variable
                        e2 = new VariableExpression(tokens.get(j+1).sequence);
                        break;
                    case(12):
                        //it's a number
                        e2 = new ValueExpression(new IntValue(Integer.parseInt(tokens.get(j+1).sequence)));
                        break;
                    case(21):
                        //it's a statement
                        e2 = expressions.get(tokens.get(j+1).sequence);
                        break;
                    default:
                        System.out.println("Error: unknown token " + tokens.get(j+1).token);
                        return;
                }

                Expression result = new ArithmeticExpression(tokens.get(j).sequence.charAt(0), e1, e2);
                expressions.put(result.toString(), result);
                for(int k = j-1; k <= j+1; k++){
                    tokens.remove(j-1);
                }
                tokens.add(j-1, new Token(21, result.toString()));

                return;
            }
        }

    }

    private boolean checkForMultiplicationOrDivision(int i, int i1) {
        for(int j = i; j <= i1; j++){
            if(tokens.get(j).token == 9){
                return true;
            }
        }
        return false;
    }

    //reduces to a single token a sequence of tokens between the indexes.
    //the sequence can contain arithmetic operations, and equality operations
    private void parseSimple(int start, int end){
        if(end-start==0){
            return;
        }
        if(containsDeclaration(start, end)){
            //reduce the declaration
            int[] aux = parseDeclaration(start, end);
            start = aux[0];
            end = aux[1];
        }
        if(containsArithmeticExpression(start, end)){
            //reduce the arithmetic expression
            int[] aux = parseArithmeticExpression(start, end);
            start = aux[0];
            end = aux[1];
        }
        if(containsEqualityExpression(start, end)){
            //reduce the equality expression
            int[] aux = parseEqualityExpression(start, end);
            start = aux[0];
            end = aux[1];
        }


    }

    private int[] parseDeclaration(int start, int end) {
        //varName is the last token
        String varName = tokens.get(end).sequence;
        //remove the last token
        tokens.remove(end);
        end--;
        //take all remaining tokens and build the VariableDeclarationStmt
        Type type = null;
        switch(tokens.get(end).sequence){
            case("int"):
                type = new IntType();
                break;
            case("bool"):
                type = new BoolType();
                break;
            case("string"):
                type = new StringType();
                break;
            default:
                System.out.println("Error: unknown type " + tokens.get(end).sequence);
                return new int[]{start, end};
        }
        tokens.remove(end);
        end--;
        for (int i = end; i >= start; i--) {
            switch(tokens.get(i).sequence){
                case("ref"):
                    type = new ReferenceType(type);
                    break;
                default:
                    System.out.println("Error: unknown type " + tokens.get(end).sequence);
                    return new int[]{start, end};
            }
            tokens.remove(i);
            end--;
        }
        VariableDeclarationStmt stmt = new VariableDeclarationStmt(varName, type);
        statements.put(stmt.toString(), stmt);
        tokens.add(start, new Token(20, stmt.toString()));
        return new int[]{start, start};
    }

    private boolean containsDeclaration(int start, int end) {
        for(int i = start; i <= end; i++){
            if(tokens.get(i).token == 1){
                return true;
            }
        }
        return false;
    }

    private boolean containsEqualityExpression(int start, int end) {
        for(int i = start; i <= end; i++){
            if(tokens.get(i).token == 11){
                return true;
            }
        }
        return false;
    }

    private boolean containsArithmeticExpression(int start, int end) {
        for(int i = start; i <= end; i++){
            if(tokens.get(i).token == 8 || tokens.get(i).token == 9){
                return true;
            }
        }
        return false;
    }

    private int[] parseEqualityExpression(int start, int end) {
        String varName = tokens.get(start).sequence;
        Expression e1;
        //find the equality operator
        int i = start+1;
        //check the right side
        switch(tokens.get(i+1).token){
            case(13):
                //it's a variable
                e1 = new VariableExpression(tokens.get(i+1).sequence);
                break;
            case(12):
                //it's a number
                e1 = new ValueExpression(new IntValue(Integer.parseInt(tokens.get(i+1).sequence)));
                break;
            case(21):
                //it's a statement
                e1 = expressions.get(tokens.get(i+1).sequence);
                break;
            case(16):
                //it's a string
                //remove the first quote
                tokens.remove(i+1);
                end--;
                e1 = new ValueExpression(new StringValue(tokens.get(i+1).sequence));
                //tokens.remove(i+1);
                //end--;
                //remove the last quote
                tokens.remove(i+2);
                end--;
                break;
            default:
                System.out.println("Error: unknown token " + tokens.get(i+1).token);
                return new int[]{start, end};
        }

        IStmt result = new AssignStmt(varName, e1);
        statements.put(result.toString(), result);
        for(int k = i-1; k <= i+1; k++){
            tokens.remove(i-1);
        }
        tokens.add(i-1, new Token(20, result.toString()));

        return new int[]{start, end-2};

    }

    private void parsePrintStatement(int start, int end) {
        // if it contains exactly one token between the parantheses, it's either a variable(token 13),
        // a constant(token 12), or an already parsed expression (token 20)

        //reduce it to a single expression
        parseSimple(start+1, end-1);

        //find the end again, first ')'
        for(int i = start; i <= tokens.size(); i++){
            if(tokens.get(i).token == 6){
                end = i;
                break;
            }
        }
        //once reduced, treat the different cases
        switch(tokens.get(start+1).token){
            case(13):
                //it's a variable
                String variableName = tokens.get(start+1).sequence;
                IStmt result = new PrintStmt(new VariableExpression(variableName));
                statements.put(result.toString(), result);

                for(int i = start; i <= end; i++){
                    tokens.remove(start);
                }
                tokens.add(start, new Token(20, result.toString()));
                break;
            case(12):
                //it's a constant
                int constant = Integer.parseInt(tokens.get(start+1).sequence);
                result = new PrintStmt(new ValueExpression(new IntValue(constant)));
                statements.put(result.toString(), result);

                for(int i = start; i <= end; i++){
                    tokens.remove(start);
                }
                tokens.add(start, new Token(20, result.toString()));
                break;
            case(21):
                //it's an expression
                String expression = tokens.get(start+1).sequence;
                result = new PrintStmt(expressions.get(expression));
                statements.put(result.toString(), result);

                for(int i = start; i <= end; i++){
                    tokens.remove(start);
                }
                tokens.add(start, new Token(20, result.toString()));
                break;
            default:
                System.out.println("Error: unknown token " + tokens.get(start+1).token);
                return;
        }

    }



    public IStmt parse(String program){
        tokenizer.tokenize(program);
        tokens = tokenizer.getTokens();

        while(tokens.size() > 1 && depth() > 0){
            parseInnermostBlock();
        }
        if(tokens.size() !=1){
            System.out.println("Error: invalid syntax");
            return null;
        }
        return statements.get(tokens.get(0).sequence);
    }


}
