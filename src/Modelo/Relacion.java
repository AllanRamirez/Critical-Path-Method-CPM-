package Modelo;

import java.io.FileWriter;
import java.io.IOException;

public class Relacion {
    private Actividad sucesora; 
    private Actividad origen; 
    
    public Relacion(Actividad origen, Actividad sucesora){
        this.origen = origen;
        this.sucesora = sucesora;
    }
    @Override
    public String toString (){
        String mensaje = "Origen: " + origen.getId() + "\n" + "Sucesor: " + sucesora.getId() + "\n"; 
        return mensaje;
    }
    
    public String getIdOrigen(String id){
        return origen.getId();
    }
    
    public Actividad getOrigen(){
        return origen;
    }
    
    public Actividad getSucesora(){
        return sucesora;
    }

   public void guardar(FileWriter save) throws IOException {
       save.write(origen.getId() + "\n");
       save.write(sucesora.getId() + "\n");
   }
     
}
