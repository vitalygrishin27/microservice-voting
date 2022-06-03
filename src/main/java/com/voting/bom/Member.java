package com.voting.bom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column
    private String secondName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String description;

  /*  @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable (name="contest_members",
            joinColumns = @JoinColumn(name = "contest_id"),
            inverseJoinColumns = @JoinColumn(name="member_id"))
    @Fetch(FetchMode.SUBSELECT)*/

    @ManyToOne
    @JoinColumn(name = "id_contest")
    private Contest contest;

    @Lob
    private String photo = "";

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Performance> performances;

    @Transient
    private MultipartFile file;


    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
