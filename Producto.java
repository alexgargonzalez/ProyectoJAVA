import java.io.Serializable;

public abstract class Producto implements Comparable<Producto>, Serializable {
    protected int id;
    protected String nombre;
    protected double precio;
    protected int stock;
    
    // Constructor
    public Producto(int id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        setPrecio(precio);
        setStock(stock);
    }

    public abstract double calcularEnvio();

    // Validación de precio
    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.precio = precio;
    }

    // Validación de stock
    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.stock = stock;
    }

    // Método para aplicar descuentos (puede sobrescribirse)
    public double aplicarDescuento(double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("Porcentaje inválido");
        }
        return precio * (1 - porcentaje/100);
    }

    // Getters (no hay setters para atributos críticos sin validación)
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    @Override
    public String toString() {
        return String.format("[ID: %d] %s - $%.2f (Stock: %d)", 
               id, nombre, precio, stock);
    }
}