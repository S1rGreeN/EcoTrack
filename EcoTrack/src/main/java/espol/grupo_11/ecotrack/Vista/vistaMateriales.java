package espol.grupo_11.ecotrack.Vista;

import java.util.Scanner;

public class vistaMateriales {
    public void mostrarMenu(){
        Scanner scan = new Scanner(System.in);
        
        int opcion = 0;
        do{
            System.out.println("1. Crear Lote de Materiales");
            System.out.println("2. Editar Lote de Materiales");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scan.nextInt();
            switch(opcion){
                case 1:
                    this.crearLote(scan);
                    break;
                case 2:
                    this.editarLote(scan);
                    break;
                case 3:
                    System.out.println("Saliendo de materiales...");
                    break;
               
                default:
                    System.out.println("Opcion no valida");
            } 
        } while(opcion != 3);
        
    }

    // editar estos metodos despues
    private void crearLote(Scanner scan){
        System.out.println("Creando lote de materiales...");
    }
    private void editarLote(Scanner scan){
        System.out.println("Editando lote de materiales...");
    }
}
