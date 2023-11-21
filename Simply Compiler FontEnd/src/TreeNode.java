
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author rafael
 */
public class TreeNode {
    
    ArrayList<TreeNode> sons;
    
    String item;
    
    public TreeNode(String s){
        this.item = s;
        this.sons = new ArrayList<>();
    }
    
    public boolean isLeaf(){
        return this.sons.isEmpty();
    }
    
    public String getItem(){
        return this.item;
    }
    
    public void addSon(TreeNode n){
        this.sons.add(n);
    }
    
    public void addSon(int i, TreeNode n){
        this.sons.add(i, n);
    }
    
    public TreeNode getSon(TreeNode n){
        int x;
        if((x = this.sons.indexOf(n)) > -1){
            return this.sons.get(x);
        }
        return null;
    }
    
    public TreeNode getSon(int i){
        return this.sons.get(i);
    }
    
    public int getSonsSize(){
        return this.sons.size();
    }
}
