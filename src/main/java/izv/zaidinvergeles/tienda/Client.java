package izv.zaidinvergeles.tienda;

import java.util.ArrayList;

public class Client extends User {

    private String id;
    private Carrito carro; // Carrito del cliente

    public Client() {
        this.carro = new Carrito(); // Inicializar el carrito
    }

    public Client(String nombre, String password, String email) {
        super(nombre, password, email);
        this.carro = new Carrito(); // Inicializar el carrito
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTipo() {
        return "client";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Carrito getCarro() {
        return carro;
    }

    public void setCarro(Carrito carro) {
        this.carro = carro;
    }

    // Método para agregar un producto al carrito
    public void agregarProductoAlCarrito(Product producto) {
        carro.agregarProducto(producto);
    }

    // Método para obtener el total del carrito
    public double obtenerTotalCarrito() {
        return carro.getPrecioTotal();
    }

    // Método para vaciar el carrito
    public void vaciarCarrito() {
        carro.vaciarCarrito();
    }
}