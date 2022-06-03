package com.voting.bom;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "contest_juries",
            joinColumns = @JoinColumn(name = "contest_id"),
            inverseJoinColumns = @JoinColumn(name = "jury_id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<Jury> juries;

    @JsonIgnore
    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY)
    private List<Member> members;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "contest_categories",
            joinColumns = @JoinColumn(name = "contest_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<Category> categories;

    @Lob
    private String photo = "";

    @Transient
    private MultipartFile file;

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY)
    private List<Performance> performances;

    @Transient
    private Performance activePerformance;

    @Transient
    private List<String> juryLastNames;

    public Contest(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Contest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


}