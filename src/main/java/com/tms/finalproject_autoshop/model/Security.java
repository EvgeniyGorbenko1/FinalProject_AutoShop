package com.tms.finalproject_autoshop.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "security")
@Data
@EqualsAndHashCode(exclude = {"users"})
@ToString(exclude = {"users"})
@NoArgsConstructor
public class Security {
    @Id
    @SequenceGenerator(name = "security_generator", sequenceName = "security_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "security_generator")
    private Integer id;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}
