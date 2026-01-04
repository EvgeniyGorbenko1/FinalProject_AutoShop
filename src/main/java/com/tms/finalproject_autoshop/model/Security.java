package com.tms.finalproject_autoshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "security")
@Data
@EqualsAndHashCode(exclude = {"user"})
@ToString(exclude = {"user"})
@NoArgsConstructor
public class Security {
    @Id
    @SequenceGenerator(name = "security_generator", sequenceName = "security_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "security_generator")
    private Long id;
    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean isEnabled = false;
}
