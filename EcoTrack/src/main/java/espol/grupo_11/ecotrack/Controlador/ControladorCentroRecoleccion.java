package espol.grupo_11.ecotrack.Controlador;
import espol.grupo_11.ecotrack.Modelo.CentroRecoleccion;
import espol.grupo_11.ecotrack.Modelo.Residuo;
import espol.grupo_11.ecotrack.Modelo.Residuo.TipoResiduo;
import espol.grupo_11.ecotrack.Utilitarios.ArrayList;
import java.io.Serializable;



public class ControladorCentroRecoleccion implements Serializable {
    private CentroRecoleccion centroRecoleccion;

    public ControladorCentroRecoleccion(CentroRecoleccion centroRecoleccion){
        this.centroRecoleccion = centroRecoleccion;
    }


    /*Cuando el metodo de reloeccion se implemente, 
    se debe actualizar la ultimaRecoleccion y la cantidadResiduosRecolectados
    a su vez que se vacia la pila del centro de recoleccion.
    */
    public ArrayList<Residuo> iniciarRecoleccionCarrito(){


        ArrayList<Residuo> residuosRecolectados = new ArrayList<Residuo>();
        
        //Codigo heavy


        return residuosRecolectados;
    }


    public boolean procesarResiduos(){

        //Codigo medio heavy
        return true;
    } 


    public boolean nuevoResiduo(String id, String nombre, TipoResiduo tipo, double peso, String zona, int prioridad){

        return true;
    }




    
}
