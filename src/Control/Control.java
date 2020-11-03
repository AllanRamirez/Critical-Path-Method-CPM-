package Control;

import Modelo.*;
import Vista.View;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Control implements MouseListener, MouseMotionListener, ActionListener{
    /*para el boton de Limpiar poner en una linea: vista.draw = false;*/
    Modelo model;
    View view;
    Actividad primeraSeleccionada;
    Actividad segundaSeleccionada;
    public Point inicio, inicio2, destino;
    int seleccionada;
    
    public Control(Modelo model, View view){
        this.model = model;
        this.view = view;
        primeraSeleccionada = null;
        segundaSeleccionada = null;
        inicio = null;
        inicio2 = null;
        destino = null; 
        view.setModelo(model);
        view.setControl(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /*Creacion de Actividades*/
        if (e.getButton() == e.BUTTON1 && e.getClickCount() == 2 && e.getSource() == view){  
            JTextField identificador = new JTextField();
            JTextField duracion = new JTextField();
            Object[] message = {"Identificador: ", identificador, "Duracion: ", duracion};
            int option = JOptionPane.showConfirmDialog(null, message, "Nueva actividad", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String id = identificador.getText(); 
                id = id.toUpperCase();
                if (!model.existeActividad(id)){//si no se repite el id 
                    int dur;
                    try{
                        dur = Integer.parseInt(duracion.getText());
                    }catch(NumberFormatException ex){
                       dur = -1;
                    }   
                    if (id.length()==1 && dur>0){ //si los datos se ingresan corractamente entonces:
                        Point p = new Point(e.getX(), e.getY()); 
                        Actividad nuevaAct = new Actividad(id, dur, p);
                        model.agregarActividad(nuevaAct);
                    }else
                        JOptionPane.showMessageDialog(view, "Es posible que haya dejado espacios en blanco; si no es asi considere la siguiente informacion:\nIdentificador:  En este campo debe digitar solamente 1 letra.\nDuracion:  En este campo debe digitar un numero entero mayor a 0.","Datos de entrada no validos", JOptionPane.ERROR_MESSAGE);
                }else
                    JOptionPane.showMessageDialog(view, "Ya existe una actividad con ese identificador", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        /*Establece relaciones*/
        if (e.getButton() == e.BUTTON3 && e.getClickCount() == 1 && e.getSource() == view){ 
            if (model.getActividades().size() >= 2){
                seleccionada = find(e.getPoint());
                if (seleccionada != -1){ //si el mouse clickeo sobre una actividad 
                    view.draw = true;
                    if (primeraSeleccionada == null){//si no se ha seleccionado el origen
                        primeraSeleccionada = model.getActividades().get(seleccionada);
                        inicio = e.getPoint();
                    }else{//si ya se selecciono el origen, se establece el destino
                        segundaSeleccionada = model.getActividades().get(seleccionada);  
                        if (!model.existeRelacion(primeraSeleccionada, segundaSeleccionada)){
                            if (primeraSeleccionada != segundaSeleccionada){
                                String saltoLinea = System.getProperty("line.separator");
                                int op = JOptionPane.showConfirmDialog(view, "Desea establecer la siguiente relacion?" + saltoLinea + "Origen: '"+primeraSeleccionada.getId()+ "'" + saltoLinea + "Sucesor: '"+segundaSeleccionada.getId()+ "'" + saltoLinea + "(Nota: '"+ primeraSeleccionada.getId()+ "' sera predecesora de '"+segundaSeleccionada.getId()+"')","Confirmacion", JOptionPane.YES_NO_OPTION);
                                if (op == 0){ //al confirmar
                                    Relacion rela = new Relacion(primeraSeleccionada, segundaSeleccionada);
                                    Actividad actOrigen = rela.getOrigen();
                                    Actividad actSucesora = rela.getSucesora();
                                    actOrigen.agregarSucesor(actSucesora);
                                    actSucesora.agregarPredecesor(actOrigen);
                                    model.agregarRelacion(rela);
                                    model.determinarRutaCritica();
                                    view.draw = false;
                                    inicio = null;
                                    destino = null;
                                    primeraSeleccionada = null;
                                }else{ //al cancelar
                                    inicio = null;
                                    destino = null;
                                    primeraSeleccionada = null;
                                    view.draw = false;
                                    view.repaint();
                                }
                            }else{//al intentar relacionar el nodo con el mismo
                                JOptionPane.showMessageDialog(view, "Relacion no valida", "Error", JOptionPane.ERROR_MESSAGE);
                                inicio = null;
                                destino = null;
                                primeraSeleccionada = null;
                                view.draw = false;  
                                view.repaint();
                            }
                        }else{//al intentar establecer una relacion existente por segunda vez
                            JOptionPane.showMessageDialog(view, "Esta relacion ya fue establecida", "Error", JOptionPane.ERROR_MESSAGE);
                            inicio = null;
                            destino = null;
                            primeraSeleccionada = null;
                            view.draw = false;  
                            view.repaint();
                        }
                    }
                }   
            }else//al intentar establecer una relacion cuando solo hay una Actividad creada
                JOptionPane.showMessageDialog(view, "Deben haber como minimo 2 relaciones para establecer una relacion", "Error", JOptionPane.ERROR_MESSAGE);     
        }
}//fin del metodo mouseClicked()

    public int find(Point p) { //devuelve la posicion de una Actividad en la lista
        Actividad act;
        for (int i = 0; i < model.getActividades().size(); i++) {
            act = model.getActividades().get(i);
            Rectangle r = new Rectangle(act.getPoint().x, act.getPoint().y, 50, 50);
            if (r.contains(p)) 
                return i;
        }
        return -1;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
       inicio2 = e.getPoint();
       seleccionada = find(e.getPoint());
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.getSource() == view){
            if (seleccionada != -1){ //si clickeo sobre una actividad
                model.mover(model.getActividades().get(seleccionada), new Point(e.getX() - inicio2.x, e.getY() - inicio2.y));
                inicio2 = e.getPoint();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
      if (e.getSource() == view) {
            if (view.draw) {
                destino = e.getPoint();
                model.update();
            }
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "Limpiar":
                int op = JOptionPane.showConfirmDialog(view, "Esta a punto de eliminar el proyecto actual, desea continuar?", "Aviso", JOptionPane.YES_NO_OPTION);
                if (op == 0){
                    model.limpiar();
                    inicio = null;
                    destino = null;
                    primeraSeleccionada = null;
                    view.draw = false;  
                    view.repaint();
                }
                break;
                
            case "Guardar":
                guardar();
                break;
                
            case "Recuperar":
                recuperar();
                break;
                
            default:
                throw new AssertionError();
        }
    }
    
    public void guardar() {
        try {
            String nombre = "";
            JFileChooser file = new JFileChooser();
            FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
                    "xml files (*.xml)", "xml");
            file.setFileFilter(xmlfilter);
            file.showSaveDialog(view);
            File guarda = file.getSelectedFile();
            if (guarda != null) {
                FileWriter save = new FileWriter(guarda + ".xml");
                model.guardar(save);
                save.close();
                JOptionPane.showMessageDialog(null,"El archivo se ha guardado exitosamente","Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"Su archivo no se ha guardado","Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    public void recuperar() {
        String aux = "";
        String texto = "";
        try {
            JFileChooser file = new JFileChooser();
            FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
                    "xml files (*.xml)", "xml");
            file.setFileFilter(xmlfilter);
            file.showOpenDialog(view);
            File abre = file.getSelectedFile();
            if (abre != null) {
                FileReader archivos = new FileReader(abre);
                BufferedReader lee = new BufferedReader(archivos);
                model.recuperar(lee);
                lee.close();
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex + "" + "\nNo se ha encontrado el archivo", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

}
