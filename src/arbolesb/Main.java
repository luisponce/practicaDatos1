package arbolesb;

import arbolesb.Gui.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Main {
    
    private static FMainGui fMainGui;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                fMainGui = new FMainGui();
                fMainGui.setVisible(true);
                
                DOrden dialog = new DOrden(new javax.swing.JFrame(), true);
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
    
    public static void InsertarElementoEnArbol(int val) throws Exception {
        if (Nodo.EstaEnArbol(val)) {
            JOptionPane.showMessageDialog(fMainGui, "Elemento " + val +
                    " ya se encuentra en el arbol", "Error al insertar",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            Nodo.InsertarEnArbol(val);

            ActualizarInfo();

            JOptionPane.showMessageDialog(fMainGui, "Elemento " + val + " ingresado exitosamente",
                    "Exito al insertar", JOptionPane.INFORMATION_MESSAGE);
        }
        
        //TODO:pintar de nuevo el arbol
    }
    
    public static void EliminarElementoEnArbol(int val) throws Exception{
        Nodo.EliminarEnArbol(val);
        
        JOptionPane.showMessageDialog(fMainGui, "Elemento " + val + " eliminado exitosamente",
                    "Exito al Eliminar", JOptionPane.INFORMATION_MESSAGE);

        ActualizarInfo();
    }
    
    public static void BuscarElementoEnArbol(int val){
        if(Nodo.EstaEnArbol(val)){
            Nodo result = Nodo.BuscarEnArbol(val);
            String str = "El elemento " + val + " se encontro en el nivel " +
                    result.EncontrarNivel() + " en el nodo con valores: "
                    + result.getInfoString();
            
            JOptionPane.showMessageDialog(fMainGui, str,
                "Exito al buscar", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(fMainGui, "El elemento " + val + " no se encontro",
                "Error al buscar", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public static boolean EstaElemento(int val){
        return Nodo.EstaEnArbol(val);
    }
    
    public static void EstablecerOrden(int n) throws Exception{
        if(n>0) {
            Nodo.setN(n);
        } else {
            throw new Exception("Orden invalido");
        }
    }
    
    public static void LimpiarArbol() throws Exception {
        if(Nodo.getNumElementos()==0){
            throw new Exception("No hay arbol para eliminar");
        } else {
            Nodo.BorrarArbol();
            
            JOptionPane.showMessageDialog(fMainGui, "El arbol a sido eliminado",
                        "Exito al limpiar el Arbol", JOptionPane.INFORMATION_MESSAGE);

            ActualizarInfo();
        }
    }
    
    public static void CrearNuevoArbol() {
        try {
            LimpiarArbol();
            
            
        } catch (Exception ex) {
        }
        
        String str = JOptionPane.showInputDialog(fMainGui, "Ingrese nuevo orden", "Orden del nuevo arbol", JOptionPane.PLAIN_MESSAGE);
        try {
            Main.EstablecerOrden(Integer.parseInt(str));
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(fMainGui, ex.getMessage(), "Error en Ingreso", JOptionPane.WARNING_MESSAGE);
        }
        Main.ActualizarInfo();
    }
    
//    public static DefaultMutableTreeNode GenerateTree(){
//        
//    }
    
    public static void ActualizarInfo() {
        String n =  "" + Nodo.getN();
        String ele = "" + Nodo.getNumElementos();
        fMainGui.UpdateOrden(n);
        fMainGui.UpdateNumElementos(ele);
        
        fMainGui.CreateTree(Nodo.getRaiz());
    }
}
