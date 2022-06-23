package com.example.biznes.model;

import com.example.biznes.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Car car;

    @OneToOne
    private User user;

    private OffsetDateTime startDate;

    private boolean isActive;

    private OffsetDateTime endDate;

    private float totalPrice;
}
