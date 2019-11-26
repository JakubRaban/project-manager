package pl.edu.agh.gastronomiastosowana.model;

import com.sun.istack.NotNull;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Critic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int criticID;

    @Transient private StringProperty name;
    @Transient private StringProperty surname;
    @Transient private StringProperty email;

    public Critic(String name, String surname, String email){
        this.name = new SimpleStringProperty(this, "name");
        this.surname = new SimpleStringProperty(this, "surname");
        this.email = new SimpleStringProperty(this, "email");
    }
    public Critic(){
        //hib
    }

    public StringProperty nameProperty() {
        return name;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public void setSurnameProperty(String surname) {
        this.surname.set(surname);
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public String getSurname(String surname) {
        return this.surname.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false, unique = true)
    public String getEmail() {
        return this.email.get();
    }

    public int getCriticID() {
        return criticID;
    }
}
