/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package espol.grupo_11.ecotrack;

import java.util.Scanner;

import espol.grupo_11.ecotrack.Vista.*;
/**
 *
 * @author Usuario
 */
public class EcoTrack {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int opcion = 0;
        vistaInventario vistaInventario = new vistaInventario();
        vistaMateriales vistaMateriales = new vistaMateriales();
        vistaRutas vistaRutas = new vistaRutas();
        vistaEstadisticas vistaEstadisticas = new vistaEstadisticas();
        do{ 
            mostrarMenu();
            opcion = scan.nextInt();
            switch (opcion){
                case 1: 
                    vistaInventario.mostrarMenu();
                    break;
                case 2:
                    vistaMateriales.mostrarMenu();
                    break;
                case 3:
                    vistaRutas.mostrarMenu();
                    break;
                case 4:
                    vistaEstadisticas.mostrarMenu();
                    break;
                case 5:
                    System.out.println("Saliendo del programa...");
                default:
                    System.out.println("Opcion no valida");
            }
        } while(opcion!=10);
        scan.close();
    }
    public static void mostrarMenu(){
        System.out.println("----- EcoTrack -----");
        System.out.println("1. Inventario");
        System.out.println("2. Clasificacion de Materiales");
        System.out.println("3. Rutas y prioridades");
        System.out.println("4. Estadisticas");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opcion: ");
    }
}

