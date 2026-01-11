package espol.grupo_11.ecotrack.Controlador;
import espol.grupo_11.ecotrack.Modelo.CentroRecoleccion;
import espol.grupo_11.ecotrack.Modelo.Residuo;
import espol.grupo_11.ecotrack.Modelo.Residuo.TipoResiduo;
import espol.grupo_11.ecotrack.Modelo.Zona;
import espol.grupo_11.ecotrack.Utilitarios.ArrayList;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Iterator;



public class ControladorCentroRecoleccion implements Serializable, Runnable  {
    private CentroRecoleccion centroRecoleccion;

    public ControladorCentroRecoleccion(CentroRecoleccion centroRecoleccion){
        this.centroRecoleccion = centroRecoleccion;
    }


    /*Cuando el metodo de reloeccion se implemente, 
    se debe actualizar la ultimaRecoleccion y la cantidadResiduosRecolectados
    a su vez que se vacia la pila del centro de recoleccion.
    */
    public void run(){
        //Codigo heavy
        Iterator<Zona> it= centroRecoleccion.getZonasUrbanas().iterator();
        try{
            Thread.sleep(10);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        
        int contadorIteracionPQ = centroRecoleccion.getZonasUrbanas().size();
        
        while(it.hasNext() && contadorIteracionPQ>=0){
            Zona zonaActual = it.next();
            if(zonaActual.getUltimaRecoleccion() == null){
                --contadorIteracionPQ;
                int contador = 0;
                System.out.println("Recoleccion en zona: "+zonaActual.getCodigo());
                
                while(contador<20){
                    contador++;
                    Residuo copiaResiduo  = zonaActual.removeFirstListaResiduos();
                    if(copiaResiduo != null){
                        centroRecoleccion.addPilaResiduos(copiaResiduo);
                        centroRecoleccion.addColaResiduos(copiaResiduo);
                        zonaActual.actualizarRecoleccion();
                        
                    } else {
                        break;
                    }
                        //centroRecoleccion.addPilaResiduos(zonaActual.removeFirstListaResiduos());
                } 
                //zonaActual = it.next();
                try{
                    Thread.sleep(5);
                } catch(InterruptedException e){
                    System.out.println("Error en el hilo de recoleccion");
                }
            } else if(zonaActual.getUltimaRecoleccion().plusDays(2).isEqual(LocalDateTime.now()) 
                || zonaActual.getUltimaRecoleccion().plusDays(2).isBefore(LocalDateTime.now())){
                    --contadorIteracionPQ;
                int contador = 0;
                System.out.println("Recoleccion en zona: "+zonaActual.getCodigo());
                
                while(contador<20){
                    contador++;
                    Residuo copiaResiduo  = zonaActual.removeFirstListaResiduos();
                    if(copiaResiduo != null){
                        centroRecoleccion.addPilaResiduos(copiaResiduo);
                        centroRecoleccion.addColaResiduos(copiaResiduo);
                        zonaActual.actualizarRecoleccion();
                    } else {
                        break;
                    }
                        //centroRecoleccion.addPilaResiduos(zonaActual.removeFirstListaResiduos());
                } 
                //zonaActual = it.next();
                try{
                    Thread.sleep(5);
                } catch(InterruptedException e){
                    System.out.println("Error en el hilo de recoleccion");
                }
            }
            
        }
        centroRecoleccion.printZonasUrbanas();
        int contador =1;
        for(Residuo r : centroRecoleccion.getPilaResiduos()){
            System.out.println(contador + " " + r.toString());
            contador++;
        }
    }
    
    public void iniciarRecoleccionCarrito(){
        Thread hiloRecoleccion = new Thread(this);
        hiloRecoleccion.start();
    }


    public boolean procesarResiduos(){
        
        //Codigo medio heavy
        return true;
    } 


    public boolean nuevoResiduo(String id, String nombre, TipoResiduo tipo, double peso, String zona, int prioridad){

        return true;
    }




    
}
