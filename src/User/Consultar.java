package User;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CrisRodFe <your.name at your.org>
 */
public class Consultar extends javax.swing.JDialog {

    /**
     * Creates new form Consultar
     */
    public Consultar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        buscarTermino = new javax.swing.JTextField();
        botonBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        terminosEncontrados = new javax.swing.JTextArea();
        limpiar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Introducir término para búsqueda:");

        botonBuscar.setText("Buscar");
        botonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBuscarActionPerformed(evt);
            }
        });

        terminosEncontrados.setEditable(false);
        terminosEncontrados.setColumns(20);
        terminosEncontrados.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        terminosEncontrados.setRows(5);
        terminosEncontrados.setBorder(null);
        jScrollPane1.setViewportView(terminosEncontrados);

        limpiar.setText("Limpiar");
        limpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiarActionPerformed(evt);
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
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buscarTermino, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botonBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(limpiar, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buscarTermino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonBuscar)
                    .addComponent(limpiar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>                        
    /**
     * Busca el término introducido en la base de datos y devuelve sus valores.
     * Buscará también terminos similares y los mostrará.
     * @param evt 
     */
    private void botonBuscarActionPerformed(java.awt.event.ActionEvent evt) { 
       //Establecemos la conexión con la base de datos 
       ConexionAOracle cao = new ConexionAOracle();
       Connection conn = cao.construirConexion("XE","HR","usuario");
       
       //Extraemos el término a buscar en la BD.
       String buscar = buscarTermino.getText();
       String parte = buscarTermino.getText().substring(2);
       
       //Describimos la sentencia SQL que hará la búsqueda en la BD.
       String sqlS = "SELECT palabra, trad.*,visitas FROM terminos, TABLE(traducciones) trad "
               + "WHERE palabra='"+buscar+"' OR palabra like "+"'"+"%"+parte+"'";
       PreparedStatement ps;
        try {
            //Ejecutamos la sentencia SQL.
            ps = conn.prepareStatement(sqlS);  
            
            //Recogemos el resultado de la consulta.
            ResultSet rs = ps.executeQuery(sqlS);
            
            String pal = null,idioma,trad,visitas;
            //Iteramos sobre los resultados de la consulta SQL:
            
            while(rs.next()){                             
                //Extraemos los valores de cada uno de los resultados de la consulta.
                pal = rs.getString(1);
                idioma =rs.getString(2);
                trad = rs.getString(3);
                visitas = String.valueOf(rs.getInt(4));
                //Imprimimos en un JTextArea los datos de cada objeto encontrado.                
                terminosEncontrados.append(pal.toUpperCase()+"\n"
                                        +trad+"("+idioma+").\n\n"+
                                        "Consultas: "+visitas+"\n\n");
                
                //Llamamos al método encargado de sumar uno a la variable visitas y ejecutamos.
                CallableStatement cst = conn.prepareCall("{call incrementar(?)}");                 
                cst.setString(1,pal);
                cst.executeUpdate();
                cst.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Consultar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }                                           
    /**
     * Limpiamos los campos del panel.
     * @param evt 
     */
    private void limpiarActionPerformed(java.awt.event.ActionEvent evt) {                                        
        buscarTermino.setText("");
        terminosEncontrados.setText("");
    }                                       
    
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
            java.util.logging.Logger.getLogger(Consultar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Consultar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Consultar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Consultar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Consultar dialog = new Consultar(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify                     
    private javax.swing.JButton botonBuscar;
    private javax.swing.JTextField buscarTermino;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton limpiar;
    private javax.swing.JTextArea terminosEncontrados;
    // End of variables declaration                   
}
