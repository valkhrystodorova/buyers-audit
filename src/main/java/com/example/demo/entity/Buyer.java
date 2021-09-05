package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "buyers")
public class Buyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone")
    private String phone;
    @Column(name = "birth_date")
    private ZonedDateTime birthdate;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "buyer_id")
    private List<Purchase> purchases;

    public Buyer(String firstName, String lastName, String phone, ZonedDateTime birthdate, List<Purchase> purchases) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.birthdate = birthdate;
        this.purchases = purchases;
    }
}
