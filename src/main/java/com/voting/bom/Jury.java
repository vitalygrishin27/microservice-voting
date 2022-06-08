package com.voting.bom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Jury {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String EncryptedPassword;

    @Transient
    @Column(insertable = false, updatable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column
    private String secondName;

    @Column
    private String lastName;

    @Column
    private String position;

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable (name="contest_juries",
            joinColumns = @JoinColumn(name = "jury_id"),
            inverseJoinColumns = @JoinColumn(name="contest_id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<Contest> contests;

    @Lob
    private String photo = "";

    @Transient
    private MultipartFile file;

    @Transient
    private String token;
}
