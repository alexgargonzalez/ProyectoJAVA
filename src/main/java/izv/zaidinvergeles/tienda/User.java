/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package izv.zaidinvergeles.tienda;

/**
 *
 * @author alfon
 */
abstract class User {
    protected int id;
    protected String name;
    protected String password;
    protected String email;

    public User() {
        this.id = 0;
        this.name = " ";
        this.password = " ";
        this.email = " ";
    }
    
    public User(int id, String nombre, String password, String email) {
        this.id = id;
        this.name = nombre;
        this.password = password;
        this.email = email;
    }

    public abstract String getTipo();
}
