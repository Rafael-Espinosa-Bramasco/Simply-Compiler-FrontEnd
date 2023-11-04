/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author rafael
 */
public class TOKEN {
    
    private String tokenName;
    private int  numLine;
    private int  numToken;
    private String tokenType;
    
    public TOKEN(String name, int line, int token , String type){
        this.tokenName = name;
        this.numLine = line;
        this.numToken = token;
        this.tokenType = type;
    }
    public TOKEN(String name, int line, int token){
        this.tokenName = name;
        this.numLine = line;
        this.numToken = token;
    }

    public String getTokenName(){
        return this.tokenName;
    }
    public int getNumLine(){
        return this.numLine;
    }
    public int getNumToken(){
        return this.numToken;
    }
    
    public void setTokenType(String type){
        this.tokenType = type;
    }
    public String getTokenType(){
        return this.tokenType;
    }
    
    public boolean comp(TOKEN T){
        return this.getNumLine() == T.getNumLine() && this.getNumToken() == T.getNumToken() && this.getTokenName().equals(T.getTokenName());
    }
}
