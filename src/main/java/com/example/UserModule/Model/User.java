package com.example.UserModule.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // @Column(unique = true)
    private String userName;

    private String password;
    private String name;
    private String email;
    private String phone;
    private String UserRole="USER";
    private boolean enabled=true;

}
