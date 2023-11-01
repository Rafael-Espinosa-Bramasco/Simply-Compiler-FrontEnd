/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import java.util.ArrayList;

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
    }

    // Variables
    ArrayList<TOKEN> TL;
    String AT;
    int Line;
    int TNum;
    
    // Functions
    private void LexicalAnalisys(String code){
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
            }else if(isOpenPar(c) || isClosePar(c) || isSemiColon(c)){
                AT = AT + c;

                aux = new TOKEN(AT,Line,TNum);
                
                TL.add(aux);
                AT = "";
                TNum++;
                
            }else if(c == ':'){
                AT = AT + c;
                i++;
                
                if(i < code.length() && code.charAt(i) == '='){
                    AT = AT + c;
                    
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
                            AT = AT + c;
                        }else{
                            i--;
                        }
                    }
                    case '>' -> {
                        i++;
                        if(i < code.length() && code.charAt(i) == '='){
                            AT = AT + c;
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
                // ERROR
                return;
            }
        }
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
    
    private boolean isSpace(TOKEN token){
        String c = token.getTokenName();
        
        return " ".equals(c);
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
    
    private boolean isID(TOKEN token){
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

    private boolean isRWord(String word){
       ArrayList<String> reservedWords = new ArrayList<>();
       reservedWords.add("begin");
       reservedWords.add("end");
       reservedWords.add("entero");
       reservedWords.add("real");
       reservedWords.add("if");
       reservedWords.add("else");
       reservedWords.add("while");
       reservedWords.add("endwhile");
       
       return reservedWords.contains(word);
          
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
    
    private boolean isFloat(TOKEN token){
        String t = token.getTokenName();
        return !(t.indexOf('.')== -1 || t.indexOf('.') == t.length()-1 || t.indexOf('.') != t.lastIndexOf('.'));
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
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

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

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Token Number", "Token ID", "Token", "Lexeme"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane3.setViewportView(jTextArea2);

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane4.setViewportView(jTextArea3);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel3.setText("Lexical Analisys");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel4.setText("Sintactic Analisys");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel5.setText("Semantic Analisys");

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
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jLabel1)
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
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
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
    }// </editor-fold>//GEN-END:initComponents

    private void AnalizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnalizeActionPerformed
        // TODO add your handling code here:
        this.LexicalAnalisys(this.SourceCode.getText());
        
        for(int i = 0 ; i < TL.size() ; i++){
            System.out.println(TL.get(i).getTokenName());
        }
    }//GEN-LAST:event_AnalizeActionPerformed

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
    private javax.swing.JButton Analize;
    private javax.swing.JTextArea SourceCode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    // End of variables declaration//GEN-END:variables
}
