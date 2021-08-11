package ru.ayukov.diplom.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long number;

    private String nameSprint;

    //private StatusSprint statusSprint;

    private Date dateStart;
    private Date dateEnd;

    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Board.class)
    @JoinColumn
    private Board board;

    public Sprint(Long number, String nameSprint, Date dateStart, Date dateEnd, Board board) {
        this.number = number;
        this.nameSprint = nameSprint;
        //this.statusSprint = statusSprint;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.board = board;
    }
}
