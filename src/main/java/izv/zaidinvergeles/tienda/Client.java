/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package izv.zaidinvergeles.tienda;

/**
 *
 * @author alfon
 */
public class Client extends User{
    private String direction;

    public Client(int id, String nombre, String password, String email, String direccion) {
        super(id, nombre, password, email);
        this.direction = direccion;
    }

    @Override
    public String getTipo() {
        return "client";
    }

    public String getDirection() {
        return direction;
    }
}
