package ru.rkniazev.testcodemark.models;

import javax.persistence.*;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    public Role() {
    }

    public String getName() {
        return name;
    }

    public Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{\"id\": \"" + id + "\",\"name\": \"" + name + "\"}";
    }
}
