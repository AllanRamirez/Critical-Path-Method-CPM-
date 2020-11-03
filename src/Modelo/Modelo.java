package Modelo;

import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import org.w3c.dom.*; //DOM para el procesamiento de xml
import javax.xml.parsers.*; //procesamiento de doc xml

public class Modelo extends Observable { 
    private ArrayList<Relacion> relaciones;
    private ArrayList<Actividad> actividades;
    /*private Relacion inicio; //Siempre estan creadas las actividades inicio y fin
    private Relacion fin;*/
    
    public Modelo(){
        relaciones = new ArrayList<>();
        actividades = new ArrayList<>();
        /*inicio = null;
        fin = null;*/
    }
    
    public void agregarActividad(Actividad a){
        actividades.add(a);
        setChanged();
        notifyObservers();
    }
    
    public void agregarRelacion(Relacion r){
        relaciones.add(r);
        setChanged();
        notifyObservers();
    }
    
    public void verInfoActividades(){
       Actividad aux;
       System.out.println("Cantidad de actividades: "+actividades.size()+ "\n");
       for (int i=0; i<actividades.size(); i++){
           aux = actividades.get(i);
           System.out.print(aux.toString());
           System.out.println("IC: " + aux.getIC() + " TC: " + aux.getTC());
           System.out.println("IL: " + aux.getIL() + " TL: " + aux.getTL());
           System.out.println("Holgura: " + aux.getHolgura());
           aux.verSucesoras();
           aux.verPredecesoras();
           System.out.println();
       } 
    }
    
    public void verRelaciones() {
       Relacion aux;
       for (int i=0; i<relaciones.size(); i++){
           aux = relaciones.get(i);
           System.out.println(aux.toString());
       }
    }
   
    public void leerXML(){
        try{
            File fxmlFile=new File("datos.xml");
            DocumentBuilderFactory dbFactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder=dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fxmlFile);
            doc.getDocumentElement().normalize();
          
            NodeList nodos= doc.getElementsByTagName("Actividad");
            int cantidadNodos =nodos.getLength();
            for (int i=0; i<cantidadNodos; i++){
                 Node actNodo =nodos.item(i);
                 if(actNodo.getNodeType()==Node.ELEMENT_NODE){
                     Element actElement =(Element)actNodo;
                     String id = actElement.getAttribute("id");
                     int x = Integer.parseInt(actElement.getAttribute("x"));
                     int y = Integer.parseInt(actElement.getAttribute("y"));
                     Point p = new Point(x, y);
                     Actividad act =new Actividad (id, Integer.parseInt(actElement.getAttribute("duracion")), p);
                     actividades.add(act);
                 }
            }
          
            NodeList rela = doc.getElementsByTagName("Relacion");
            int cantidadRela = rela.getLength();
            for (int i=0; i<cantidadRela; i++){
                Node actNodo = rela.item(i);
                if(actNodo.getNodeType()==Node.ELEMENT_NODE){
                    Element actElement =(Element)actNodo;
                    String id=actElement.getAttribute("actividad");
                    String id2=actElement.getAttribute("sucesor");
                    Actividad actOrigen = obtenerActividad(id);
                    Actividad actSucesora = obtenerActividad(id2);
                    
                    Relacion r = new Relacion(actOrigen, actSucesora);
                    relaciones.add(r);
                }
            }
        }catch(Exception n){
            n.printStackTrace();
        }  
    }
    
    public Actividad obtenerActividad(String id){
        Actividad aux;
        for (int i=0; i<actividades.size(); i++){
            aux = actividades.get(i);
            if (aux.getId().equals(id))
                return aux;
        }
        return null;
    }

    public void estableceRelaciones(){ /*obtiene cada una de las relaciones y con ellas establece
                                       las actividades sucesoras y predecesoras*/
        for (int i=0; i<relaciones.size(); i++){
            Relacion auxRela = relaciones.get(i);
            Actividad actOrigen = auxRela.getOrigen();
            Actividad actSucesora = auxRela.getSucesora();
            actOrigen.agregarSucesor(actSucesora);
            actSucesora.agregarPredecesor(actOrigen);
        }
    }
    
    public boolean existeRelacion(Actividad origen, Actividad destino){//retorna true si la relacion existe
        for (int i=0; i<origen.getSucesoras().size(); i++){
            if (origen.getSucesoras().get(i).equals(destino))
                return true;
        }
        return false;
    }
    
    public boolean existeActividad(String id){
        for (int i=0; i<actividades.size(); i++){
            if (actividades.get(i).getId().equals(id))
                return true;  
        }
        return false;
    }
    
    public Actividad getEstado(String id) {
        for (int i = 0; i < actividades.size(); i++) {
            if (actividades.get(i).getId().equals(id)) {
                return actividades.get(i);
            }
        }
        return null;
    }
    private void calculoICTC(){ 
        Actividad tmp = null;
        Actividad tmp2 = null;      
        ArrayList sucesoras = null;
        ArrayList predecesoras = null;
        int mayorTC=0;
              
        for(int i=0; i<actividades.size(); i++){
            tmp = actividades.get(i); 
                                                 //si NO hay actividades predecesoras
            if(tmp.getPredecesoras().isEmpty()){ //actividades que no dependen de nadie para iniciar
                tmp.setIC(0);
                tmp.setTC(tmp.getDuracion());   
            } else { //si SI hay actividades predecesoras
                mayorTC = 0;
                predecesoras = tmp.getPredecesoras();
                for(int j = 0; j<predecesoras.size(); j++){
                    tmp2 = (Actividad)predecesoras.get(j); //get element from arraylist
                    if (tmp2.getTC() > mayorTC)
                        mayorTC = tmp2.getTC();
                    tmp.setIC(mayorTC);
                    tmp.setTC(tmp.getIC()+tmp.getDuracion());   
                }
            }
            
            if (!tmp.getSucesoras().isEmpty()){ //si SI hay actividades sucesoras
                sucesoras = tmp.getSucesoras();
                for(int j = 0; j< sucesoras.size(); j++){
                    tmp2 = (Actividad)sucesoras.get(j); //get element from arraylist
                    tmp2.setIC(tmp.getTC());
                    tmp2.setTC(tmp2.getIC()+tmp2.getDuracion());
                }
            }   
        }
    }//fin del metodo calculoICTC();
   
    private void calculoILTL(){
        Actividad tmp = null;
        Actividad tmp2 = null;      
        ArrayList sucesoras = null;
        ArrayList predecesoras = null;
        int ILmenor = 0;
       
        for (int i=actividades.size()-1; i>=0; i--){
            tmp = actividades.get(i);
            sucesoras = tmp.getSucesoras();
            if (sucesoras.isEmpty()){ //si NO tiene actividades sucesoras 
                predecesoras = tmp.getPredecesoras();
                tmp.setTL(tmp.getTC());
                tmp.setIL(tmp.getTL()-tmp.getDuracion());
                
                for (int j=0; j<predecesoras.size(); j++){
                    tmp2 = (Actividad)predecesoras.get(j);
                    tmp2.setTL(tmp.getIL());
                    tmp2.setIL(tmp2.getTL()-tmp2.getDuracion());
                }   
            }
            else { //si SI tiene actividades sucesoras
                sucesoras = tmp.getSucesoras();
                ILmenor = 1000;
                for (int k=0; k<sucesoras.size(); k++){
                    tmp2 = (Actividad)sucesoras.get(k);
                    tmp2.setHolgura(tmp2.getTL()-tmp2.getTC());
                    if (tmp2.getIL() < ILmenor)
                        ILmenor = tmp2.getIL();
                    tmp.setTL(ILmenor);
                }   
                tmp.setIL(tmp.getTL()-tmp.getDuracion());
            }
        }
        
    }//fin del metodo calculoILTL();
    
    private void calculoHolgura(){
        Actividad aux;
        for (int i=0; i<actividades.size(); i++){
            aux = actividades.get(i);
            if (!aux.getSucesoras().isEmpty() || !aux.getPredecesoras().isEmpty()){
                aux.setHolgura(aux.getTL() - aux.getTC());
                if (aux.getHolgura() == 0)
                    aux.setEsDeRuta(true);
                else
                    aux.setEsDeRuta(false);
            }  
        }
    }
    
    public void determinarRutaCritica(){
        calculoICTC();
        calculoILTL();
        calculoHolgura();
    }

    public ArrayList<Relacion> getRelaciones() {
        return relaciones;
    }

    public void setRelaciones(ArrayList<Relacion> relaciones) {
        this.relaciones = relaciones;
    }

    public ArrayList<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(ArrayList<Actividad> actividades) {
        this.actividades = actividades;
    }
    
    public void update() {
        setChanged();
        notifyObservers(null);
    }
    
    @Override
    public void addObserver(java.util.Observer o) {
        super.addObserver(o);
        setChanged();
        notifyObservers(null);
    }
    
    public void limpiar() {
        actividades.clear();
        relaciones.clear();
        setChanged();
        notifyObservers(null);
    } 
    
    public void mover(Actividad act, Point p) {
        Point aux = new Point(act.getPoint().x + p.x, act.getPoint().y + p.y);
        act.setPoint(aux);
        setChanged();
        notifyObservers(null);
    }
   
    public void guardar(FileWriter save) throws IOException {
        Actividad d;
        Relacion t;
        save.write(actividades.size() + "\n");
        for (int i = 0; i < actividades.size(); i++) {
            d = actividades.get(i);
            d.guardar(save);
        }
        save.write(relaciones.size() + "\n");
        for (int j = 0; j < relaciones.size(); j++) {
            t = relaciones.get(j);
            t.guardar(save);
        }
    }
    
    public void recuperar(BufferedReader lee) throws IOException {
        Graphics g = null;
        Actividad d;
        int n = Integer.parseInt(lee.readLine());
        for (int i = 0; i < n; i++) {
            String id = lee.readLine();
            int duracion = Integer.parseInt(lee.readLine());
            int x = Integer.parseInt(lee.readLine());
            int y = Integer.parseInt(lee.readLine());
            String ruta=lee.readLine();
             if(ruta.equals("true")){
             d = new Actividad(id, duracion, new Point(x, y));
             d.setEsDeRuta(true);
             actividades.add(d);
            }
             else{
                 d = new Actividad(id, duracion, new Point(x, y));
            actividades.add(d);
             }    
        }
        
        int cant = Integer.parseInt(lee.readLine());
        for (int j = 0; j < cant; j++) {
           String inicio = lee.readLine();
           String destino = lee.readLine();
           Actividad aux1 = getEstado(inicio);
           Actividad aux2 = getEstado(destino);
           relaciones.add(new Relacion(aux1, aux2));
        }
        setChanged();
        notifyObservers(null);
    }
   
    @Override
    public boolean equals(Object o
    ) {
        return super.equals(o); //To change body of generated methods, choose Tools | Templates.
    }

}