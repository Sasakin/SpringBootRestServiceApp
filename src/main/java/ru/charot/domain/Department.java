package ru.charot.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "closed")
    private Boolean closed;

    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER)
    private List<Person> persons;


    public boolean isClosed() {
        return closed;
    }
}