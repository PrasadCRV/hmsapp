package com.hmsapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "mobile", nullable = false)
    private String mobile;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "no_of_guests", nullable = false)
    private Long noOfGuests;

    @Column(name = "guest_name", nullable = false)
    private String guestName;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

}