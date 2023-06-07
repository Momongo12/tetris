package org.example.playerservice.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Date;

@Data
@Table("players")
public class Player {
    @Id
    @Column("player_id")
    private Long playerId;

    @Column("name")
    private String name;

    @Column("username")
    private String username;

    @Column("email")
    private String email;

    @Column("password")
    private String password;

    @Column("date_of_registration")
    private Date dateOfRegistration;

    @MappedCollection(idColumn = "player_id")
    private PlayerStatistic playerStatistic;

    public Player(String name, String username, String email, String password, Date dateOfRegistration) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateOfRegistration = dateOfRegistration;
    }
}