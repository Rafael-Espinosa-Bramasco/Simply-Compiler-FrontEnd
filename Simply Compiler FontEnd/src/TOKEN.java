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

    TOKEN(String name, int line, int token){
        this.tokenName = name;
        this.numLine = line;
        this.numToken = token;
    }

    String getTokenName(){
        return this.tokenName;
    }
    int getNumLine(){
        return this.numLine;
    }
    int getNumToken(){
        return this.numToken;
    }
}
