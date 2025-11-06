package com.app.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "refreshToken",nullable = false,unique = true)
    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime expiryDate;
}
