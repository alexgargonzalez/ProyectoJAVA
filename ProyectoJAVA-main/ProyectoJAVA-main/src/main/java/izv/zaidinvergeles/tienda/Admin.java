/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package izv.zaidinvergeles.tienda;

/**
 *
 * @author alfon
 */
public class Admin extends User{
    public Admin(String nombre, String password, String email) {
        super(nombre, password, email);
    }

    @Override
    public String getTipo() {
        return "admin";
    }
}
