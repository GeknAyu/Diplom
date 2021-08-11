package ru.ayukov.diplom.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Table
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userName;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    public Users(String userName, String password, Role role, Status status) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public Users(Long id, String userName, String password, Role role, Status status) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.status = status;
    }

}
