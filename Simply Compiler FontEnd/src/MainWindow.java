/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author rafael
 */
public class MainWindow extends javax.swing.JFrame {

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        
        this.TokensTableModel = (DefaultTableModel) this.TokensTable.getModel();
        this.lastToken = null;
    }

    // Variables
    ArrayList<TOKEN> TL;
    String AT;
    int Line;
    int TNum;
    
    boolean error;
    boolean semError;
    
    TOKEN lastToken;
    
    int lastDT;
    STO aux1;
    ArrayList<STO> Symbols;
    
    int ifCount;
    int whileCount;
    
    int lval, rval;
    boolean compError;
    
    DefaultTableModel TokensTableModel;
    
    TreeNode AnalisisTree;
    
    ArrayList<TreeNode> toEXP;
    
    ArrayList<QUAD> quadruplo;
    
    // Functions
    private void fillTokensTable(){
        clearTokensTable();
        
        TOKEN aux;
        for(int i = 0 ; i < TL.size() ; i++){
            aux = TL.get(i);
            TokensTableModel.addRow(new Object[]{aux.getTokenName(),aux.getNumLine(),aux.getNumToken(),aux.getTokenType()});
        }
    }
    
    private void clearTokensTable(){
        while(TokensTableModel.getRowCount() > 0){
            TokensTableModel.removeRow(0);
        }
    }
    
    private void guessTokensType(){
        for(int i = 0 ; i < TL.size() ; i++){
            if(isRWord(TL.get(i))){
                if(isBlockRW(TL.get(i))){
                    TL.get(i).setTokenType("Block Delimiter");
                }else if(isDecisionRW(TL.get(i))){
                    TL.get(i).setTokenType("Decision Making Reserved Word");
                }else if(isWhileBlockRW(TL.get(i))){
                    TL.get(i).setTokenType("While Block");
                }else if(isWhileEndBlockRW(TL.get(i))){
                    TL.get(i).setTokenType("While Block Ends");
                }else if(isEnteroKW(TL.get(i))){
                    TL.get(i).setTokenType("Integer Keyword");
                }else if(isRealKW(TL.get(i))){
                    TL.get(i).setTokenType("Float Keyword");
                }else if(isOut(TL.get(i))){
                    TL.get(i).setTokenType("Out Keyword");
                }else if(isIn(TL.get(i))){
                    TL.get(i).setTokenType("In Keyword");
                }
            }else if(isType(TL.get(i))){
                if(isEntero(TL.get(i))){
                    TL.get(i).setTokenType("Integer Type Constant");
                }else{
                    TL.get(i).setTokenType("Float Type Constant");
                }
            }else if(isSemiColon(TL.get(i))){
                TL.get(i).setTokenType("SemiColon");
            }else if(isComa(TL.get(i))){
                TL.get(i).setTokenType("Coma");
            }else if(isAsignSymbol(TL.get(i))){
                TL.get(i).setTokenType("Asign Symbol");
            }else if(isOpenPar(TL.get(i))){
                TL.get(i).setTokenType("Open Parenthesis");
            }else if(isClosePar(TL.get(i))){
                TL.get(i).setTokenType("Close Parenthesis");
            }else if(isLogicalOperator(TL.get(i))){
                TL.get(i).setTokenType("Logical Operator");
            }else if(isMathOperator(TL.get(i))){
                TL.get(i).setTokenType("Math Operator");
            }else if(isFlux(TL.get(i))){
                TL.get(i).setTokenType("Flux Operator");
            }else if(isID(TL.get(i))){
                TL.get(i).setTokenType("Identifier");
            }
        }
    }
    
    private boolean LexicalAnalisys(String code){
        TL = new ArrayList<>();
        TOKEN aux;
        AT = "";
        Line = 1;
        TNum = 1;
        char c;
        
        for(int i = 0 ; i < code.length() ; i++){
            c = code.charAt(i);
            
            if(isAlpha(c)){
                // Is identifier or reserved word
                AT = AT + c;
                i++;
                
                while(i < code.length() && (isAlpha(code.charAt(i)) || isNumber(code.charAt(i)))){
                    AT = AT + code.charAt(i);
                    i++;
                }
                
                aux = new TOKEN(AT,Line,TNum);
                
                TL.add(aux);
                AT = "";
                TNum++;
                i--;
                
            }else if(isNumber(c)){
                // is number
                // check if is integer or float
                AT = AT + c;
                i++;
                
                while(i < code.length() && (isNumber(code.charAt(i)))){
                    AT = AT + code.charAt(i);
                    i++;
                }
                
                if(i < code.length() && (isPoint(code.charAt(i)))){
                    AT = AT + code.charAt(i);
                    i++;
                    
                    while(i < code.length() && (isNumber(code.charAt(i)))){
                        AT = AT + code.charAt(i);
                        i++;
                    }
                }
                
                aux = new TOKEN(AT,Line,TNum);
                
                TL.add(aux);
                AT = "";
                TNum++;
                i--;
                
            }else if(isNewLine(c)){
                Line++;
                TNum = 1;
            }else if(isOpenPar(c) || isClosePar(c) || isSemiColon(c) || isComa(c)){
                AT = AT + c;

                aux = new TOKEN(AT,Line,TNum);
                
                TL.add(aux);
                AT = "";
                TNum++;
                
            }else if(c == ':'){
                AT = AT + c;
                i++;
                
                if(i < code.length() && code.charAt(i) == '='){
                    AT = AT + '=';
                    
                    aux = new TOKEN(AT,Line,TNum);
                
                    TL.add(aux);
                    AT = "";
                    TNum++;
                }
            }else if(isMathOperator(c)){
                AT = AT + c;

                aux = new TOKEN(AT,Line,TNum);
                
                TL.add(aux);
                AT = "";
                TNum++;
            }else if(isSimplyLogicalOperator(c)){
                AT = AT + c;
                
                switch(c){
                    case '<' -> {
                        i++;
                        
                        if(i < code.length() && (code.charAt(i) == '=' || code.charAt(i) == '>')){
                            AT = AT + code.charAt(i);
                        }else{
                            i--;
                        }
                    }
                    case '>' -> {
                        i++;
                        if(i < code.length() && code.charAt(i) == '=' || code.charAt(i) == '>'){
                            AT = AT + code.charAt(i);
                        }else{
                            i--;
                        }
                    }
                }
                
                aux = new TOKEN(AT,Line,TNum);
                
                TL.add(aux);
                AT = "";
                TNum++;
            
            }else if(!isIgnorable(c)){
                JOptionPane.showMessageDialog(this, "Lexical Error: \n" + '\'' + c + '\'' + " is not a accepted character!.");
                return false;
            }
        }
        
        guessTokensType();
        
        fillTokensTable();
        
        return true;
    }
    
    private boolean isIgnorable(char c){
        return isSpace(c) || "\t".equals(String.valueOf(c));
    }
    
    private boolean isNumber(char c){
        switch(c){
            case '0','1','2','3','4','5','6','7','8','9' -> {return true;}
        }
        return false;
    }

    private boolean isAlpha(char c){
        int charCode = c;

        return (charCode >= 65 && charCode <= 90) || (charCode >= 97 && charCode <= 122);
    }

    private boolean isSpace(char c){
        return c == ' ';
    }
    
    private boolean isComa(char c){
        return c == ',';
    }
    
    private boolean isNewLine(char c){
        return "\n".equals(String.valueOf(c));
    }
    
    private boolean isPoint(char c){
        return c == '.';
    }
    
    private boolean isSemiColon(char c){
        return c == ';';
    }
    
    private boolean isOpenPar(char c){
        return c == '(';
    }
    
    private boolean isClosePar(char c){
        return c == ')';
    }
    
    private boolean isSimplyLogicalOperator(char c){
        switch(c){
            case '=','<','>' -> {return true;}
        }
        return false;
    }
    
    private boolean isMathOperator(char c){
        switch(c){
            case '+','-','/','*' -> {return true;}
        }
        return false;
    }
    
    private boolean isSemiColon(TOKEN token){
        String c = token.getTokenName();
        
        return ";".equals(c);
    }
    
    private boolean isOpenPar(TOKEN token){
        String c = token.getTokenName();
        
        return "(".equals(c);
    }
    
    private boolean isClosePar(TOKEN token){
        String c = token.getTokenName();
        
        return ")".equals(c);
    }
    
    private boolean isLogicalOperator(TOKEN token){
        String t = token.getTokenName();
        
        switch(t){
            case "=","<=",">=","<",">","<>" -> {return true;}
        }
        return false;
    }
    
    private boolean isMathOperator(TOKEN token){
        String t = token.getTokenName();
        
        switch(t){
            case "+","-","/","*" -> {return true;}
        }
        return false;
    }
    
    private boolean isMathOperator(String t){
        switch(t){
            case "+","-","/","*" -> {return true;}
        }
        return false;
    }
    
     private boolean isComa(TOKEN t){
        return ",".equals(t.getTokenName());
    }
     
    private boolean isFlux(TOKEN t){
        return ">>".equals(t.getTokenName());
    }
    
    private boolean isOut(TOKEN t){
        return "out".equals(t.getTokenName());
    }
    
    private boolean isIn(TOKEN t){
        return "in".equals(t.getTokenName());
    }
    
    private boolean isID(TOKEN token){
        if(isRWord(token)){
            return false;
        }
        
        String t = token.getTokenName();
        
        if(!isAlpha(t.charAt(0))){
            return false;
        }
        
        for(int i = 1 ; i < t.length() ; i++){
            if(!isNumber(t.charAt(i)) || !isAlpha(t.charAt(i))){
                return false;
            }
        }
        
        return true;
    }
    
    private boolean isID(String t){
        if(isRWord(t)){
            return false;
        }
        
        if(!isAlpha(t.charAt(0))){
            return false;
        }
        
        for(int i = 1 ; i < t.length() ; i++){
            if(!isNumber(t.charAt(i)) || !isAlpha(t.charAt(i))){
                return false;
            }
        }
        
        return true;
    }

    private boolean isRWord(TOKEN T){
       String word = T.getTokenName();
       ArrayList<String> reservedWords = new ArrayList<>();
       reservedWords.add("begin");
       reservedWords.add("end");
       reservedWords.add("entero");
       reservedWords.add("real");
       reservedWords.add("out");
       reservedWords.add("in");
       reservedWords.add("if");
       reservedWords.add("else");
       reservedWords.add("while");
       reservedWords.add("endwhile");
       
       return reservedWords.contains(word);
          
    }
    
    private boolean isRWord(String word){
       ArrayList<String> reservedWords = new ArrayList<>();
       reservedWords.add("begin");
       reservedWords.add("end");
       reservedWords.add("entero");
       reservedWords.add("real");
       reservedWords.add("out");
       reservedWords.add("in");
       reservedWords.add("if");
       reservedWords.add("else");
       reservedWords.add("while");
       reservedWords.add("endwhile");
       
       return reservedWords.contains(word);
          
    }
    
    private boolean isEnteroKW(TOKEN T){
        return "entero".equals(T.getTokenName());
    }
    
    private boolean isRealKW(TOKEN T){
        return "real".equals(T.getTokenName());
    }
    
    private boolean isAsignSymbol(TOKEN T){
        return ":=".equals(T.getTokenName());
    }
    
    private boolean isBlockRW(TOKEN T){
        switch(T.getTokenName()){
            case "begin", "end" -> {return true;}
        }
        
        return false;
    }
    
    private boolean isWhileBlockRW(TOKEN T){
        return "while".equals(T.getTokenName());
    }
    
    private boolean isWhileEndBlockRW(TOKEN T){
        return "endwhile".equals(T.getTokenName());
    }
    
    private boolean isDecisionRW(TOKEN T){
        switch(T.getTokenName()){
            case "if", "else" -> {return true;}
        }
        
        return false;
    }
    
    private boolean isIf(TOKEN T){
        return "if".equals(T.getTokenName());
    }
    
    private boolean isElse(TOKEN T){
        return "else".equals(T.getTokenName());
    }
    
    private boolean isWhile(TOKEN T){
        return "while".equals(T.getTokenName());
    }
    
    private boolean isEnd(TOKEN T){
        return "end".equals(T.getTokenName());
    }
    
    private boolean isType(TOKEN T){
        return isEntero(T) || isFloat(T);
    }
    
    private boolean isDT(TOKEN T){
        return isEnteroKW(T) || isRealKW(T);
    }
    
    private boolean isEntero(TOKEN token){
        String t = token.getTokenName();
        for(int i=0; i< t.length(); i++){
            if(!isNumber(t.charAt(i))){
                return false;
            }
        }
        return true;
    }
    
    private boolean isEntero(String t){
        for(int i=0; i< t.length(); i++){
            if(!isNumber(t.charAt(i))){
                return false;
            }
        }
        return true;
    }
    
    private boolean isFloat(TOKEN token){
        String t = token.getTokenName();
        return !(t.indexOf('.')== -1 || t.indexOf('.') == t.length()-1 || t.indexOf('.') != t.lastIndexOf('.'));
    }
    
    private boolean isFloat(String t){
        return !(t.indexOf('.')== -1 || t.indexOf('.') == t.length()-1 || t.indexOf('.') != t.lastIndexOf('.'));
    }
    
    //===========================================//
    //            SintacticAnalyzer              //
    //===========================================//
    
    private boolean  programa(ArrayList<TOKEN> TL){
        
        this.toEXP = new ArrayList<>();
        
        this.AnalisisTree = new TreeNode("<P>");
        
        if(TL.isEmpty()){
            sintaxError("No tokens given");
            return false;
        }
        Symbols = new ArrayList<>();
        ArrayList<TOKEN> TLcopy = (ArrayList<TOKEN>)TL.clone();
        boolean d;
        boolean o;
        
        ifCount = 0;
        whileCount = 0;
        
        if("begin".equals(TLcopy.get(0).getTokenName()) && "end".equals(TLcopy.get(TLcopy.size()-1).getTokenName())){
            lastToken = TLcopy.get(0);
            TLcopy.remove(0);
            TLcopy.remove(TLcopy.size()-1);
            
            this.AnalisisTree.addSon(new TreeNode("<D>"));
            
            if(!(d = declaraciones(TLcopy, this.AnalisisTree.getSon(0)))){
                return false;
            }

            this.AnalisisTree.addSon(new TreeNode("<O>"));
            
            o = ordenes(TLcopy, this.AnalisisTree.getSon(1));

            return ( d && o) && ifCount == 0 && whileCount == 0;
        }
        
        sintaxError("begin at the start and end at the finish");
        return false;
    }
    
   
    private boolean declaraciones(ArrayList<TOKEN> TL, TreeNode T){
        if(TL.isEmpty() || !declaracion(TL, T)){
            sintaxError("Init Section and Orders Section");
            return false;
        }
        if(!isSemiColon(TL.get(0))){
            sintaxError("SemiColon");
            return false;
        }
        lastToken = TL.get(0);
        TL.remove(0);
        
        return sig_declaracion(TL, T);
    }
  
    private boolean sig_declaracion(ArrayList<TOKEN> TL, TreeNode T){
        if(TL.isEmpty() || !isDT(TL.get(0))){
            return true;
        }
        if(!declaracion(TL, T)){
            return false;
        }
        if(!isSemiColon(TL.get(0))){
            sintaxError("SemiColon");
            return false;
        }
        lastToken = TL.get(0);
        TL.remove(0);
        return sig_declaracion(TL, T);
    }
    
    private boolean declaracion(ArrayList<TOKEN> TL, TreeNode T){
        if(TL.isEmpty() || !tipo(TL)){
            return false;
        }
        
        T.addSon(new TreeNode(lastToken.getTokenName()));
        
        return lista_variables(TL, T.getSon(T.getSonsSize() - 1));
    }
    private boolean tipo(ArrayList<TOKEN> TL){
        if(isDT(TL.get(0))){
            aux1 = new STO();
            lastDT = (TL.get(0).getTokenName().equals("entero") ? 0 : 1);
            aux1.setDT(lastDT);
            
            lastToken = TL.get(0);
            TL.remove(0);
            return true;
        }
        sintaxError("A data type");
        return false;
    }
    private boolean lista_variables(ArrayList<TOKEN> TL, TreeNode T){
        if(TL.isEmpty() || !identificador(TL)){
            return false;
        }
        
        T.addSon(new TreeNode(lastToken.getTokenName()));
        
        if(symbolExists(lastToken.getTokenName())){
            semanticError("Symbol '".concat(lastToken.getTokenName()).concat("' is beign re-declared."));
        }else{
            aux1.setID(lastToken.getTokenName());
            aux1.setValue("N/A");

            Symbols.add(aux1);
        }
        
        return sig_lista_variables(TL, T);
    }
    
    private boolean sig_lista_variables(ArrayList<TOKEN> TL, TreeNode T){
        if(TL.isEmpty() || isSemiColon(TL.get(0))){
            return true;
        }
        
        if(!TL.isEmpty() && isComa(TL.get(0))){
            lastToken = TL.get(0);
            aux1 = new STO();
            aux1.setDT(lastDT);
            
            TL.remove(0);
            return lista_variables(TL, T);
        }
        
        sintaxError("Coma");
        return false;
    }
    
    private boolean identificador(ArrayList<TOKEN> TL){
       if(isID(TL.get(0))){
           lastToken = TL.get(0);
           
           TL.remove(0);
           return true;
       }
       sintaxError("Identifier");
       return false;
    }
    
    private boolean ordenes(ArrayList<TOKEN> TL, TreeNode T){
        if(orden(TL, T)){
            if(!TL.isEmpty() && isSemiColon(TL.get(0))){
                lastToken = TL.get(0);
                TL.remove(0);
            }else{
                sintaxError("Semicolon");
                return false;
            }
            
            return sig_ordenes(TL, T);
        }
        
        return false;
    }
    
    private boolean sig_ordenes(ArrayList<TOKEN> TL, TreeNode T){
        if(TL.isEmpty()){
            return true;
        }
        else if(isElse(TL.get(0)) && ifCount > 0){
            return true;
        }
        else if(isEnd(TL.get(0)) && ifCount > 0){
            ifCount--;
            return true;
        }
        else if(isWhileEndBlockRW(TL.get(0)) && whileCount > 0){
            whileCount--;
            return true;
        }
        else if(!TL.isEmpty() && (isElse(TL.get(0)) && ifCount <= 0 || isEnd(TL.get(0)) && ifCount <= 0 || isWhileEndBlockRW(TL.get(0)) && whileCount > 0)){
            return false;
        }
        
        if(orden(TL, T)){
            if(!TL.isEmpty() && isSemiColon(TL.get(0))){
                lastToken = TL.get(0);
                TL.remove(0);
            }else{
                sintaxError("SemiColon");
                return false;
            }
            
            return sig_ordenes(TL, T);
        }
        
        return false;
    }
    
    private boolean orden(ArrayList<TOKEN> TL, TreeNode T){
        
        if(!TL.isEmpty() && isID(TL.get(0))){
            return asignar(TL, T);
        }else if(!TL.isEmpty() && isIf(TL.get(0))){
            return condicion(TL, T);
        }else if(!TL.isEmpty() && isWhile(TL.get(0))){
            return bucle_while(TL, T);
        }else if(!TL.isEmpty() && isOut(TL.get(0))){
            return out(TL, T);
        }else if(!TL.isEmpty() && isIn(TL.get(0))){
            return in(TL, T);
        }
        
        sintaxError("Identifier or if or while");
        return false;
    }
    
    private boolean condicion(ArrayList<TOKEN> TL, TreeNode T){
        // check if
        if(!TL.isEmpty() && !isIf(TL.get(0))){
            sintaxError("if");
            return false;
        }
        
        lastToken = TL.get(0);
        TL.remove(0);
        ifCount++;
        
        TreeNode ifNode = new TreeNode("if");
        T.addSon(ifNode);
        
        if(!TL.isEmpty() && isOpenPar(TL.get(0))){
            ArrayList<TOKEN> compList = new ArrayList<>();
            compList.add(TL.get(0));
            
            lastToken = TL.get(0);
            TL.remove(0);
            
            int opCount = 1;
            while(!TL.isEmpty() && opCount > 0){
                if(isOpenPar(TL.get(0))){
                    opCount++;
                }else if(isClosePar(TL.get(0))){
                    opCount--;
                }
                compList.add(TL.get(0));
                TL.remove(0);
            }
            
            if(opCount >= 1){
                sintaxError("Balanced parenthesis");
                return false;
            }else{
                lastToken = TL.get(0);
                compList.remove(0);
                compList.remove(compList.size() - 1);
                
                if(comparacion(compList, ifNode)){
                    TreeNode is = new TreeNode("is");
                    ifNode.addSon(is);
                    
                    if(ordenes(TL,is)){
                        return sig_condicion(TL, ifNode);
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            }
        }
        
        return false;
    }
    
    private boolean sig_condicion(ArrayList<TOKEN> TL, TreeNode T){
        if(TL.isEmpty()){
            sintaxError("end or else");
            return false;
        }
        
        if(!TL.isEmpty() && isEnd(TL.get(0))){
            lastToken = TL.get(0);
            TL.remove(0);
            return true;
        }else if(!TL.isEmpty() && isElse(TL.get(0))){
            lastToken = TL.get(0);
            TL.remove(0);
            
            TreeNode elseNode = new TreeNode("else");
            T.addSon(elseNode);
            
            if(ordenes(TL, elseNode)){
                if(!TL.isEmpty() && isEnd(TL.get(0))){
                    lastToken = TL.get(0);
                    TL.remove(0);
                    return true;
                }
                sintaxError("end");
                return false;
            }
            return false;
        }
        
        sintaxError("end or else");
        return false;
    }
    
    private boolean comparacion(ArrayList<TOKEN> TL, TreeNode T){
        
        TreeNode LV, RV, OP;
        
        if(operador(TL,0)){
            
            LV = new TreeNode(lastToken.getTokenName());
            
            if(condicion_op(TL)){
                
                OP = new TreeNode(lastToken.getTokenName());
                
                if(operador(TL,1)){
                    
                    RV = new TreeNode(lastToken.getTokenName());
                    
                    OP.addSon(LV);
                    OP.addSon(RV);
                    
                    T.addSon(OP);
                    
                    if(!compError){
                        if(lval != rval){
                            if(lval == 0){
                                semanticError("Incompatible Data Types! (Entero and Real)");
                            }else{
                                semanticError("Incompatible Data Types! (Real and Entero)");
                            }
                        }
                    }
                    compError = false;
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean condicion_op(ArrayList<TOKEN> TL){
        if(!TL.isEmpty() && isLogicalOperator(TL.get(0))){
            lastToken = TL.get(0);
            TL.remove(0);
            return true;
        }
        sintaxError("Logical Operator");
        return false;
    }
    
    private boolean operador(ArrayList<TOKEN> TL, int side){
        if(!TL.isEmpty() && isID(TL.get(0))){
            if(!symbolExists(TL.get(0).getTokenName())){
                semanticError("The ID '".concat(TL.get(0).getTokenName()).concat("' doesnt Exist!"));
                compError = true;
            }else{
                if(side == 0){
                    lval = getSymbolDT(TL.get(0).getTokenName());
                }else{
                    rval = getSymbolDT(TL.get(0).getTokenName());
                }
            }
            
            return identificador(TL);
        }else if(isEntero(TL.get(0))){
            if(side == 0){
                lval = 0;
            }else{
                rval = 0;
            }
            return numeros(TL);
        }else if(isFloat(TL.get(0))){
            if(side == 0){
                lval = 1;
            }else{
                rval = 1;
            }
            return numeros(TL);
        }
        sintaxError("Identifier or Number");
        return false;
    }
    
    private boolean out(ArrayList<TOKEN> TL, TreeNode T){
        if(isOut(TL.get(0))){
            TreeNode outNode = new TreeNode("out");
            
            lastToken = TL.get(0);
            TL.remove(0);
            
            if(!TL.isEmpty() && isFlux(TL.get(0))){
                lastToken = TL.get(0);
                TL.remove(0);
                
                rval = -1;
                
                if(this.expresion_arit(TL)){
                    outNode.addSon(this.convertToExpressionTree());
                    T.addSon(outNode);
                    
                    return true;
                }
            }
            
            sintaxError("Flux operator >>");
            return false;
        }
        
        sintaxError("out Keyword");
        return false;
    }
    
    private boolean in(ArrayList<TOKEN> TL, TreeNode T){
        if(isIn(TL.get(0))){
            lastToken = TL.get(0);
            TL.remove(0);
            
            TreeNode inNode = new TreeNode("in");
            T.addSon(inNode);
            
            if(!TL.isEmpty() && isFlux(TL.get(0))){
                lastToken = TL.get(0);
                TL.remove(0);
                
                if(identificador(TL)){
                    
                    inNode.addSon(new TreeNode(lastToken.getTokenName()));
                    
                    return true;
                }
            }
            
            sintaxError("Flux operator >>");
        }
        
        sintaxError("in Keyword");
        return false;
    }
    
    private boolean numeros(ArrayList<TOKEN> TL){
        if(!TL.isEmpty() && isEntero(TL.get(0))){
            return numero_entero(TL);
        }
        if(!TL.isEmpty() && isFloat(TL.get(0))){
            return numero_real(TL);
        }
        sintaxError("entero or real");
        return false;
    }
    
    private boolean numero_entero(ArrayList<TOKEN> TL){
        if(!TL.isEmpty() && isEntero(TL.get(0))){
            lastToken = TL.get(0);
            TL.remove(0);
            return true;
        }
        sintaxError("entero number");
        return false;
    }
    
    private boolean numero_real(ArrayList<TOKEN> TL){
        if(!TL.isEmpty() && isFloat(TL.get(0))){
            lastToken = TL.get(0);
            TL.remove(0);
            return true;
        }
        sintaxError("real number");
        return false;
    }
    
    private boolean bucle_while(ArrayList<TOKEN> TL, TreeNode T){
        if(!TL.isEmpty() && isWhile(TL.get(0))){
            lastToken = TL.get(0);
            TL.remove(0);
            
            TreeNode whileNode = new TreeNode("while");
            T.addSon(whileNode);
            
            whileCount++;
            if(!TL.isEmpty() && isOpenPar(TL.get(0))){
                ArrayList<TOKEN> compList = new ArrayList<>();
                compList.add(TL.get(0));
                
                lastToken = TL.get(0);
                TL.remove(0);

                int opCount = 1;
                while(!TL.isEmpty() && opCount > 0){
                    if(isOpenPar(TL.get(0))){
                        opCount++;
                    }else if(isClosePar(TL.get(0))){
                        opCount--;
                    }
                    compList.add(TL.get(0));
                    TL.remove(0);
                }

                if(opCount >= 1){
                    sintaxError("Balanced parenthesis");
                    return false;
                }else{
                    lastToken = TL.get(0);
                    compList.remove(0);
                    compList.remove(compList.size() - 1);

                    if(comparacion(compList, whileNode)){
                        TreeNode ws = new TreeNode("ws");
                        whileNode.addSon(ws);
                        
                        if(ordenes(TL, ws)){
                            if(!TL.isEmpty() && isWhileEndBlockRW(TL.get(0))){
                                lastToken = TL.get(0);
                                TL.remove(0);
                                return true;
                            }
                            sintaxError("endwhile");
                        }else{
                            return false;
                        }
                    }else{
                        return false;
                    }
                }
            }
           
        }
        return false;
    }
    
    private boolean morePrecedence(String o1, String o2){
        switch(o1){
            case "+", "-" -> {
                switch(o2){
                    case "+", "-" -> {
                        return false;
                    }
                    case "*", "/" -> {
                        return false;
                    }
                }
            }
            case "*", "/" -> {
                switch(o2){
                    case "+", "-" -> {
                        return true;
                    }
                    case "*", "/" -> {
                        return false;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean diffClass(String o1, String o2){
        switch(o1){
            case "+", "-" -> {
                switch(o2){
                    case "+", "-" -> {
                        return false;
                    }
                    case "*", "/" -> {
                        return true;
                    }
                }
            }
            case "*", "/" -> {
                switch(o2){
                    case "+", "-" -> {
                        return true;
                    }
                    case "*", "/" -> {
                        return false;
                    }
                }
            }
        }
        return false;
    }
    
    private TreeNode convertToExpressionTree(){
        ArrayList<TreeNode> COR = new ArrayList<>();
        
        // Correct list
        for(int i = 0 ; i < this.toEXP.size() ; i++){
            COR.add(this.toEXP.get(i));
        }
        
        ArrayList<TreeNode> postFixResult = new ArrayList<>();
        Stack<TreeNode> operatorStack = new Stack<>();
        
        int openpar = 0;
        for(int i = 0 ; i < COR.size() ; i++){
            if(isID(COR.get(i).getItem()) || isFloat(COR.get(i).getItem()) || isEntero(COR.get(i).getItem())){
                postFixResult.add(COR.get(i));
            }else if(")".equals(COR.get(i).getItem()) && !operatorStack.isEmpty()){
                postFixResult.add(operatorStack.pop());
            }else if("(".equals(COR.get(i).getItem())){
                openpar++; 
            }else if(isMathOperator(COR.get(i).getItem())){
                // is Operator
                if(operatorStack.isEmpty() && openpar == 0){
                    operatorStack.add(COR.get(i));
                }else if(openpar > 0){
                    openpar--;
                    operatorStack.add(COR.get(i));
                }else if(!this.morePrecedence(COR.get(i).getItem(), operatorStack.peek().getItem()) && diffClass(COR.get(i).getItem(), operatorStack.peek().getItem())){
                    postFixResult.add(operatorStack.pop());
                    operatorStack.add(COR.get(i));
                }else{
                    operatorStack.add(COR.get(i));
                }
            }
        }
        
        while(!operatorStack.isEmpty()){
            postFixResult.add(operatorStack.pop());
        }
        
        for(int i = 0 ; i < postFixResult.size() ; i++){
            System.out.print(postFixResult.get(i).getItem());
        }
        System.out.println("");
        System.out.println("");
        
        // Create nodes to return
        TreeNode R = null, O, L = null;
        int index;
        
        while(postFixResult.size() > 1){
            index = this.getLeafOperatorIndex(postFixResult);
            O = postFixResult.get(index);
            R = postFixResult.get(index - 1);
            L = postFixResult.get(index - 2);
            
            O.addSon(L);
            O.addSon(R);
            
            postFixResult.remove(index - 2);
            postFixResult.remove(index - 2);
        }
        
        this.toEXP.clear();
        
        return postFixResult.get(0);
    }
    
    private int getLeafOperatorIndex(ArrayList<TreeNode> a){
        for(int i = 0 ; i < a.size() ; i++){
            if(a.get(i).isLeaf() && isMathOperator(a.get(i).getItem())){
                return i;
            }
        }
        
        return -1;
    }
    
    private boolean asignar(ArrayList<TOKEN> TL, TreeNode T){
        
        if(!TL.isEmpty() && identificador(TL)){
            if(!symbolExists(lastToken.getTokenName())){
                semanticError("The ID '".concat(lastToken.getTokenName()).concat("' doesnt Exist!"));
                compError = true;
            }else{
                lval = getSymbolDT(lastToken.getTokenName());
            }
            
            TreeNode LV = new TreeNode(lastToken.getTokenName());
            
            if(isAsignSymbol(TL.get(0))){
                lastToken = TL.get(0);
                TL.remove(0);
                
                TreeNode A = new TreeNode(lastToken.getTokenName());
                
                rval = -1; // is entero... i think
                
                A.addSon(LV);
                
                if(expresion_arit(TL)){
                    
                    A.addSon(this.convertToExpressionTree());

                    T.addSon(A);
                    
                    if(!compError){
                        if(lval != rval){
                            if(lval == 0){
                                semanticError("Incompatible Data Types! (Entero and Real)");
                            }else{
                                semanticError("Incompatible Data Types! (Real and Entero)");
                            }
                        }
                    }
                    compError = false;
                    return true;
                }
                return false;
            }
            sintaxError("Asign Symbol");
        }
        return false;
    }
    
    private boolean expresion_arit(ArrayList<TOKEN> TL){
        if(!TL.isEmpty() && isOpenPar(TL.get(0))){
                ArrayList<TOKEN> compList = new ArrayList<>();
                compList.add(TL.get(0));
                
                lastToken = TL.get(0);
                TL.remove(0);
                
                this.toEXP.add(new TreeNode(lastToken.getTokenName()));

                int opCount = 1;
                while(!TL.isEmpty() && opCount > 0){
                    if(isOpenPar(TL.get(0))){
                        opCount++;
                    }else if(isClosePar(TL.get(0))){
                        opCount--;
                    }
                    
                    compList.add(TL.get(0));
                    TL.remove(0);
                }

                if(opCount >= 1){
                    sintaxError("Balanced parenthesis");
                    return false;
                }else{
                    lastToken = compList.get(0);
                    compList.remove(0);
                    compList.remove(compList.size() - 1);

                    if(expresion_arit(compList)){
                        this.toEXP.add(new TreeNode(")"));
                        return exp_arit(TL);
                    }else{
                        return false;
                    }
                }
            }else if(!TL.isEmpty() && isID(TL.get(0))){
                if(identificador(TL)){
                    
                    this.toEXP.add(new TreeNode(lastToken.getTokenName()));
                    
                    if(!symbolExists(lastToken.getTokenName())){
                        semanticError("The ID '".concat(lastToken.getTokenName()).concat("' doesnt Exist!"));
                        compError = true;
                    }else{
                        if(rval == -1){
                            rval = getSymbolDT(lastToken.getTokenName());
                        }else{
                            if(rval != getSymbolDT(lastToken.getTokenName()) && !compError){
                                rval = getSymbolDT(lastToken.getTokenName());
                                compError = true;
                                semanticError("Mixing data types on expression!.");
                            }
                        }
                    }
                    return(exp_arit(TL));
                }    
            }else if(!TL.isEmpty() && isNumber(TL.get(0))){
                if(numeros(TL)){
                    
                    this.toEXP.add(new TreeNode(lastToken.getTokenName()));
                    
                    if(rval == -1){
                        if(isEntero(lastToken)){
                            rval = 0;
                        }else{
                            rval = 1;
                        }
                    }else{
                        if(isEntero(lastToken)){
                            if(rval != 0 && !compError){
                                rval = 1;
                                compError = true;
                                semanticError("Mixing data types on expression!.");
                            }
                        }else{
                            if(rval != 1 && !compError){
                                rval = 0;
                                compError = true;
                                semanticError("Mixing data types on expression!.");
                            }
                        }
                    }
                    return(exp_arit(TL));
                }   
        }
        sintaxError("Open parenthesis, identifier or number");
        return false;
    }
    
    private boolean exp_arit(ArrayList<TOKEN> TL){
        if(TL.isEmpty() || isSemiColon(TL.get(0))){
            return true;
        }
        
        if(operador_arit(TL)){
            
            this.toEXP.add(new TreeNode(lastToken.getTokenName()));
            
            if(expresion_arit(TL)){
                return exp_arit(TL);
            }
        }
        
        return false;
    }
    
    private boolean operador_arit(ArrayList<TOKEN> TL){
        if(!TL.isEmpty() && isMathOperator(TL.get(0))){
            lastToken = TL.get(0);
            TL.remove(0);
            return true;
        }
        sintaxError("Arithmetic operator");
        return false;
    }
    
    private boolean isNumber(TOKEN T){
       return isEntero(T) || isFloat(T);
    }
    
    private void clearAllFields(){
        this.compError = false;
        this.lastToken = null;
        this.error = false;
        this.semError = false;
        this.clearTokensTable();
        this.SintaxMessages.setText("");
        this.SemanticMessages.setText("");
    }
    
    private boolean symbolExists(String s){
        for(int i = 0 ; i < Symbols.size() ; i++){
            if(Symbols.get(i).getID().equals(s)){
                return true;
            }
        }
        return false;
    }
    
    private int getSymbolDT(String s){
        for(int i = 0 ; i < Symbols.size() ; i++){
            if(Symbols.get(i).getID().equals(s)){
                return Symbols.get(i).getDT();
            }
        }
        return -1;
    }
    
    private void sintaxError(String message){
        if(!this.error){
            if(lastToken == null){
                addSintaxError("Sintax Error: \n");
                addSintaxError("\tAl Line ");
                addSintaxError("N/A");
                addSintaxError(", Token Number ");
                addSintaxError("N/A");
                addSintaxError(":\n\t");
                
                addSintaxError("N/A\n\t");
                
                addSintaxError("^\n");
                addSintaxError("Expected: ");
                addSintaxError(message);
                
            }else{
                addSintaxError("Sintax Error: \n");
                addSintaxError("\tAl Line ");
                addSintaxError(String.valueOf(lastToken.getNumLine()));
                addSintaxError(", Token Number ");
                addSintaxError(String.valueOf(lastToken.getNumToken() + 1));
                addSintaxError(":\n\t");
                
                int len = 0;
                boolean flag = true;
                for(int i = 0 ; i < TL.size() ; i++){
                    if(lastToken.comp(TL.get(i))){
                        flag = false;
                        len += 3;
                    }
                    
                    if(lastToken.getNumLine() == TL.get(i).getNumLine()){
                        addSintaxError(TL.get(i).getTokenName());
                        addSintaxError(" ");
                        
                        if(flag){
                            len += TL.get(i).getTokenName().length() + 1;
                        }
                    }
                }
                
                addSintaxError("\n\t");
                
                for(int i = 1 ; i < len ; i++){
                    addSintaxError("*");
                }
                addSintaxError("^\n");
                addSintaxError("Expected: ");
                addSintaxError(message);
            }
            
            this.error = true;
        }
    }
    
    private void semanticError(String message){
        if(lastToken == null){
            addSemanticError("Semantic Error: \n");
            addSemanticError("\tAl Line ");
            addSemanticError("N/A");
            addSemanticError(", Token Number ");
            addSemanticError("N/A");
            addSemanticError(":\n\t");

            addSemanticError("N/A\n\t");

            addSemanticError("^\n");
            addSemanticError("Expected: ");
            addSemanticError(message);

        }else{
            addSemanticError("Semantic Error: \n");
            addSemanticError("\tAl Line ");
            addSemanticError(String.valueOf(lastToken.getNumLine()));
            addSemanticError(", Token Number ");
            addSemanticError(String.valueOf(lastToken.getNumToken()));
            addSemanticError(":\n\t");

            int len = 0;
            boolean flag = true;
            for(int i = 0 ; i < TL.size() ; i++){
                if(lastToken.comp(TL.get(i))){
                    flag = false;
                }

                if(lastToken.getNumLine() == TL.get(i).getNumLine()){
                    addSemanticError(TL.get(i).getTokenName());
                    addSemanticError(" ");

                    if(flag){
                        len += TL.get(i).getTokenName().length() + 1;
                    }
                }
            }

            addSemanticError("\n\t");

            for(int i = 1 ; i < len ; i++){
                addSemanticError("*");
            }
            addSemanticError("^\n");
            addSemanticError("More Info.: ");
            addSemanticError(message);
        }
        
        addSemanticError("\n\n");
        
        this.semError = true;
    }
    
    private void addSintaxError(String s){
        this.SintaxMessages.setText(this.SintaxMessages.getText().concat(s));
    }
    
    private void addSemanticError(String s){
        this.SemanticMessages.setText(this.SemanticMessages.getText().concat(s));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        SourceCode = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        Analize = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TokensTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        SintaxMessages = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        SemanticMessages = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        AddFile = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Compiler FontEnd");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Simply Compiler FrontEnd");

        SourceCode.setColumns(20);
        SourceCode.setRows(5);
        jScrollPane1.setViewportView(SourceCode);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Code");

        Analize.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Analize.setText("Analize");
        Analize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnalizeActionPerformed(evt);
            }
        });

        TokensTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Token ID", "Code Line", "Token Number", "Lexeme"
            }
        ));
        TokensTable.setShowGrid(true);
        jScrollPane2.setViewportView(TokensTable);

        SintaxMessages.setEditable(false);
        SintaxMessages.setColumns(20);
        SintaxMessages.setRows(5);
        jScrollPane3.setViewportView(SintaxMessages);

        SemanticMessages.setEditable(false);
        SemanticMessages.setColumns(20);
        SemanticMessages.setRows(5);
        jScrollPane4.setViewportView(SemanticMessages);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel3.setText("Lexical Analisys");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel4.setText("Sintax Analisys");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel5.setText("Semantic Analisys");

        AddFile.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        AddFile.setText("Add File");
        AddFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(AddFile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(Analize, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3))
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(jScrollPane4))))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(AddFile, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(70, 70, 70)
                                .addComponent(Analize, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                                .addGap(18, 18, 18))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(58, 58, 58)))))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    private void processDeclarations(TreeNode t){
        for(int i = 0 ; i < t.getSonsSize() ; i++){
            this.addDT(t.getSon(i));
        }
    }
    
    private void addDT(TreeNode t){
        for(int i = 0 ; i < t.getSonsSize() ; i++){
            this.addVariableD(t.getSon(i),t.getItem());
        }
    }
    
    private void addVariableD(TreeNode t, String type){
        this.quadruplo.add(new QUAD(type,t.getItem(),"",""));
    }
    
    private void processOrders(TreeNode t){
        for(int i = 0 ; i < t.getSonsSize() ; i++){
            switch(t.getSon(i).getItem()){
                case "while" -> {
                    orderWhile(t.getSon(i));
                }
                case "in" -> {
                    orderIn(t.getSon(i));
                }
                case "out" -> {
                    orderOut(t.getSon(i));
                }
                case "if" -> {
                    orderIf(t.getSon(i));
                }
                case ":=" -> {
                    orderAsign(t.getSon(i));
                }
            }
        }
    }
    
    private String opositeOperator(String operator){
        switch(operator){
                case "<" -> {
                    return ">=";
                }
                case ">" -> {
                    return "<=";
                }
                case "<=" -> {
                    return ">";
                }
                case ">=" -> {
                    return "<";
                }
                case "=" -> {
                    return "<>";
                }
                case "<>" -> {
                    return "=";
                }
            }
        return "";
    }
    
    private void orderWhile(TreeNode t){
        
        this.quadruplo.add(new QUAD(opositeOperator(t.getSon(0).getItem()), t.getSon(0).getSon(0).getItem(), t.getSon(0).getSon(1).getItem(), "" ));
        this.quadruplo.add(new QUAD("if", "(".concat(String.valueOf(this.quadruplo.size()-1)).concat(")"), "", ""));
        int ifPos = this.quadruplo.size()-1;
        this.processOrders(t.getSon(1));
        this.quadruplo.get();
        
    }
    
    private void orderIn(TreeNode t){
        this.quadruplo.add(new QUAD(t.getItem(),t.getSon(0).getItem(),"",""));
    }
    
    private void orderOut(TreeNode t){}
    
    private void orderIf(TreeNode t){
        this.quadruplo.add(new QUAD(opositeOperator(t.getSon(0).getItem()),t.getSon(0).getSon(0).getItem(),t.getSon(0).getSon(1).getItem(),""));
        this.quadruplo.add(new QUAD("if","(".concat(String.valueOf(this.quadruplo.size() - 1)).concat(")"),"",""));
        
        if(t.getSonsSize() == 2){
            this.processOrders(t.getSon(1));
        }else{
            this.processOrders(t.getSon(1));
            this.processOrders(t.getSon(2));
        }
    }
    
    private void orderAsign(TreeNode t){}
    
    private void AnalizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnalizeActionPerformed
        // TODO add your handling code here:
        this.clearAllFields();
        boolean lexicalAnalisys = this.LexicalAnalisys(this.SourceCode.getText());
        
        if(!lexicalAnalisys){
            return;
        }
        
        boolean sintaxAnalisysResult = this.programa(TL);
        
        if(sintaxAnalisysResult && !semError){
            this.quadruplo = new ArrayList<>();
            
            this.processDeclarations(this.AnalisisTree.getSon(0));
            this.processOrders(this.AnalisisTree.getSon(1));
        }
        
        if(sintaxAnalisysResult){
            this.SintaxMessages.setText("No sintax errors during analisys.");
        }else{
            JOptionPane.showMessageDialog(this, "Sintax Error: \nSee Sintax Analisys field to get more info.");
        }
        
        if(!semError){
            this.SemanticMessages.setText("No semantic errors during analisys.");
        }else{
            JOptionPane.showMessageDialog(this, "Semantic Error: \nSee Semantic Analisys field to get more info.");
        }
    }//GEN-LAST:event_AnalizeActionPerformed

    private void AddFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddFileActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));

        int seleccion = fileChooser.showOpenDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            File file = new File(path);
            try (FileReader fr = new FileReader(file))
            {
                char[] chars = new char[(int)file.length()];
                fr.read(chars);
 
                String fileContent = new String(chars);
                //fileContent = fileContent.replaceAll("[\\p{Z}\\p{C}]+", "");
                //fileContent = fileContent.replaceAll(" ", "");
                fileContent = fileContent.replace("\r", "");
                this.SourceCode.setText(fileContent);
                this.clearAllFields();
                boolean lexicalAnalisys = this.LexicalAnalisys(fileContent);
                if(!lexicalAnalisys){
                    return;
                }
                boolean sintaxAnalisysResult = this.programa(TL);
                
                if(sintaxAnalisysResult && !semError){
                    // 3AC
                }
                
                if(sintaxAnalisysResult){
                    this.SintaxMessages.setText("No sintax errors during analisys.");
                }else{
                    JOptionPane.showMessageDialog(this, "Sintax Error: \nSee Sintax Analisys field to get more info.");
                }
                
                if(!semError){
                    this.SemanticMessages.setText("No semantic errors during analisys.");
                }else{
                    JOptionPane.showMessageDialog(this, "Semantic Error: \nSee Semantic Analisys field to get more info.");
                }
            }
            catch (IOException e) {
            e.printStackTrace();
            }
        }
       
    }//GEN-LAST:event_AddFileActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddFile;
    private javax.swing.JButton Analize;
    private javax.swing.JTextArea SemanticMessages;
    private javax.swing.JTextArea SintaxMessages;
    private javax.swing.JTextArea SourceCode;
    private javax.swing.JTable TokensTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables
}
