package espol.grupo_11.ecotrack.Vista;

import java.util.Scanner;

public class vistaInventario {
    public void mostrarMenu(){
        Scanner scan = new Scanner(System.in);
        
        int opcion = 0;
        do{
            this.mostrarInventario();
            System.out.println("1. Agregar Inventario");
            System.out.println("2. Editar Inventario");
            System.out.println("3. Cambiar orden");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scan.nextInt();
            switch(opcion){
                case 1:
                    this.agregarInventario(scan);
                    break;
                case 2:
                    this.editarInventario(scan);
                    break;
                case 3:
                    this.cambiarOrden(scan);
                    break;
                case 4:
                    System.out.println("Saliendo del inventario...");
                    break;
                default:
                    System.out.println("Opcion no valida");
            } 
        } while(opcion != 3);
        
    }

    // editar estos metodos despues
    private void mostrarInventario(){
        System.out.println("Mostrando inventario...");
    }
    private void agregarInventario(Scanner scan){
        System.out.println("Agregando inventario...");
    }
    private void editarInventario(Scanner scan){
        System.out.println("Editando inventario...");
    }
    private void cambiarOrden(Scanner scan){
        System.out.println("Cambiando orden...");
    }
}
