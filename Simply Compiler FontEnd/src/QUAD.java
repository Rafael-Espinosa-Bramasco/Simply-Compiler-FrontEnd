/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author rafael
 */
public class QUAD {
    
    String v1, v2, v3, v4;
    
    public QUAD(){
        v1 = v2 = v3 = v4 = "";
    }
    
    public QUAD(String x, String y, String z, String x1){
        v1 = x;
        v2 = y;
        v3 = z;
        v4 = x1;
    }
    
    public void setV1(String x){
        v1 = x;
    }
    
    public void setV2(String x){
        v2 = x;
    }
    
    public void setV3(String x){
        v3 = x;
    }
    
    public void setV4(String x){
        v4 = x;
    }
    
    public String getV1(){
        return v1;
    }
    
    public String getV2(){
        return v2;
    }
    
    public String getV3(){
        return v3;
    }
    
    public String getV4(){
        return v4;
    }
}
