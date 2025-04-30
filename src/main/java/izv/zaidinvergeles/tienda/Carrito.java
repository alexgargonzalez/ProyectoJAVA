/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package izv.zaidinvergeles.tienda;
import izv.zaidinvergeles.tienda.Product;
import java.util.ArrayList;

/**
 *
 * @author alfon
 */
public class Carrito {
    
    private ArrayList<Product> carrito;

    public Carrito(ArrayList<Product> carrito) {
        this.carrito = carrito;
    }

    public ArrayList<Product> getCarrito() {
        return carrito;
    }

    public void setCarrito(ArrayList<Product> carrito) {
        this.carrito = carrito;
    }
    
    public double getPrecioTotal(){
        double sumPrecioTotal = 0;
        for (Product producto : carrito){
            sumPrecioTotal += producto.getPrice();
        }
        return sumPrecioTotal;
    }
    
    
}
