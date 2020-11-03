package Principal;

import Control.Control;
import Modelo.Modelo;
import Vista.View;

public class Main {

    public static void main(String[] args) {
        Modelo model = new Modelo();
        View view = new View();
        Control controller = new Control(model, view);    
    }
    
}
