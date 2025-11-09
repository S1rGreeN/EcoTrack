package espol.grupo_11.ecotrack.Vista;

import java.util.Scanner;

public class vistaRutas {
    
    public void mostrarMenu(){
        Scanner scan = new Scanner(System.in);
        
        int opcion = 0;
        do{
            System.out.println("1. ");
            System.out.println("2. ");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scan.nextInt();
            switch(opcion){
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    System.out.println("Saliendo de rutas...");
                    break;
               
                default:
                    System.out.println("Opcion no valida");
            } 
        } while(opcion != 3);
        scan.close();
    }
}
