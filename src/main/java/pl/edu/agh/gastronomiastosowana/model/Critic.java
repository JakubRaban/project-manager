package pl.edu.agh.gastronomiastosowana.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Critic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int criticID;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "critic")
    private Set<Rating> ratings = new HashSet<>();;

    public Critic(String name, String surname, String email){
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
    public Critic(){
        //hib
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public int getCriticID() {
        return criticID;
    }
}
