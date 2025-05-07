package izv.zaidinvergeles.tienda;

import java.util.ArrayList;

public class Carrito {
    
    private ArrayList<Product> carrito;

    public Carrito() {
        this.carrito = new ArrayList();
    }

    public ArrayList<Product> getCarrito() {
        return carrito;
    }

    public void agregarProducto(Product producto) {
        carrito.add(producto);
    }

    public double getPrecioTotal() {
        double sumPrecioTotal = 0;
        for (Product producto : carrito) {
            sumPrecioTotal += producto.getPrice();
        }
        return sumPrecioTotal;
    }

    public void vaciarCarrito() {
        carrito.clear();
    }
}