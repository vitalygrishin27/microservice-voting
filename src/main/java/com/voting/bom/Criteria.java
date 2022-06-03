package com.voting.bom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Criteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

  /*  @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "category_criteria",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "criteria_id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<Category> categories;*/

    @JsonIgnore
    @OneToMany(mappedBy = "criteria", fetch = FetchType.LAZY)
    private Collection<Mark> marks;
}
