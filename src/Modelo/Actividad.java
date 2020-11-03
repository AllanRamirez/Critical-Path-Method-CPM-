package Modelo;

import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

public class Actividad extends Observable{
    private int duracion;
    private String id;
    private int IL; //inicio lejano 
    private int IC; //inicio cercano
    private int TL; //terminacion lejana
    private int TC; //terminacion cercana
    private int holgura; 
    private Point p; //coordenadas par graficar
    boolean esDeRuta;
    private ArrayList<Actividad> sucesoras;
    private ArrayList<Actividad> predecesoras;
    
    public Actividad(String id, int duracion, Point p){
        this.duracion = duracion;
        this.id = id;
        this.IC = 0;
        this.IL = 0;
        this.TC = 0;
        this.TL = 0;
        this.holgura = -1;
        this.p = p;
        esDeRuta = false;
        sucesoras = new ArrayList<>();
        predecesoras = new ArrayList<>();
    }

    public ArrayList<Actividad> getSucesoras() {
        return sucesoras;
    }

    public ArrayList<Actividad> getPredecesoras() {
        return predecesoras;
    }
    
     @Override
     public String toString(){
         String mensaje = "Identificador: " + id + "\n" + "Duracion: " + duracion + "\n" + "X = " + p.getX() + "\n" + "Y = " + p.getY() + "\n";
         return mensaje;
     }

    /**
     *
     * @param save
     * @throws IOException
     */
    public void guardar(FileWriter save) throws IOException {
        save.write(id + "\n" + duracion + "\n" + p.x + "\n" + p.y + "\n"+esDeRuta + "\n");
        Relacion d;
    }
     
     public void agregarPredecesor(Actividad a){ 
         predecesoras.add(a);
         setChanged();
         notifyObservers();           
     }
     
     public void agregarSucesor(Actividad a){
         sucesoras.add(a);
         setChanged();
         notifyObservers();
     }
     
     public void verSucesoras(){
         if (sucesoras.isEmpty())
             System.out.println("No tiene actividades sucesoras");
         else{
            Actividad aux;
            System.out.println("Actividades sucesoras: ");
            for (int i=0; i<sucesoras.size(); i++){
                aux = sucesoras.get(i);
                System.out.println(aux.getId());      
            }
         }
     }
     
      public void verPredecesoras(){
         if (predecesoras.isEmpty())
             System.out.println("No tiene actividades Predecesoras");
         else{
            Actividad aux;
            System.out.println("Actividades predecesoras: ");
            for (int i=0; i<predecesoras.size(); i++){
                aux = predecesoras.get(i);
                System.out.println(aux.getId());      
            }
         }
     }

    public boolean getEsDeRuta() {
        return esDeRuta;
    }

    public void setEsDeRuta(boolean esDeRuta) {
        this.esDeRuta = esDeRuta;
    }
      
    public String getId(){
         return this.id;
     }

    public int getIL() {
        return IL;
    }

    public void setIL(int IL) {
        this.IL = IL;
    }

    public int getIC() {
        return IC;
    }

    public void setIC(int IC) {
        this.IC = IC;
    }

    public int getTL() {
        return TL;
    }

    public void setTL(int TL) {
        this.TL = TL;
    }

    public int getTC() {
        return TC;
    }

    public void setTC(int TC) {
        this.TC = TC;
    }

    public int getHolgura() {
        return holgura;
    }
    
    public int getDuracion()
    {
        return duracion;
    }

    public void setHolgura(int holgura) {
        this.holgura = holgura;
    }

    public Point getPoint() {
        return p;
    }

    public void setPoint(Point p) {
        this.p = p;
    }
    
    public void setPointX(int x){
        this.p.x = x;
    }
     
    public void setPointY(int y){
        this.p.y = y;
    }
    
    
}
