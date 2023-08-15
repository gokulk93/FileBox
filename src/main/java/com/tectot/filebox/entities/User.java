package com.tectot.filebox.entities;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_details")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String userRole;


    @ManyToOne
    @JoinColumn(name = "fk_org_id")
    @EqualsAndHashCode.Exclude
    private Organisation organisation;


}
