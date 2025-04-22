/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package izv.zaidinvergeles.tienda;

/**
 *
 * @author alfon
 */
public class Product {
    private int id;
    private String name;
    private String description;
    private double price;

    // Constructor con ID (por si viene de la base de datos)
    public Product(int id, String nombre, String descripcion, double precio, int stock) {
        this.id = id;
        this.name = nombre;
        this.description = descripcion;
        this.price = precio;
    }

    // Constructor sin ID (por si vas a insertarlo)
    public Product(String nombre, String descripcion, double precio) {
        this.name = nombre;
        this.description = descripcion;
        this.price = precio;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Para mostrar en tablas o depurar
    @Override
    public String toString() {
        return name + " - $" + price;
    }

    
    
    
}
