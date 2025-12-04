package com.tms.finalproject_autoshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity(name = "users")
@Component
@EqualsAndHashCode(exclude = {"spare_parts"})
@ToString(exclude = {"spare_parts"})
public class User {
    @Id
    @SequenceGenerator(name = "user_generator", sequenceName = "users_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "user_generator")
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    private Integer age;
    private LocalDateTime created;
    private LocalDateTime updated;


//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private SpareParts spareParts;
}
