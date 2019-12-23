package pl.edu.agh.gastronomiastosowana.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;

@Entity
public class Critic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int criticID;

    @Transient
    private StringProperty name;
    @Transient
    private StringProperty surname;
    @Transient
    private StringProperty email;

    public Critic(String name, String surname, String email) {
        this.name = new SimpleStringProperty(this, "name");
        this.surname = new SimpleStringProperty(this, "surname");
        this.email = new SimpleStringProperty(this, "email");
    }

    public Critic() {}



    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public StringProperty nameProperty() {
        return name;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false)
    public String getSurname(String surname) {
        return this.surname.get();
    }
    public void setSurname(String surname) {
        this.surname.set(surname);
    }
    public StringProperty surnameProperty() {
        return surname;
    }

    @Access(AccessType.PROPERTY)
    @Column(nullable = false, unique = true)
    public String getEmail() {
        return this.email.get();
    }
    public void setEmail(String email) {
        this.email.set(email);
    }
    public StringProperty emailProperty() {
        return email;
    }

    public int getCriticID() {
        return criticID;
    }
}
