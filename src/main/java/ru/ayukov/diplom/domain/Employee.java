package ru.ayukov.diplom.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String info;

    @OneToOne(fetch = FetchType.EAGER,targetEntity = Users.class)
    @JoinColumn
    private Users users;

    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Team.class)
    @JoinColumn
    private Team team;

    public Employee(String firstName, String lastName, String patronymic, String info, Users users) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.info = info;
        this.users = users;
    }


}
