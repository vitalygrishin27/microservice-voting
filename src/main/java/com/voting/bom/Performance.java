package com.voting.bom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_member")
    private Member member;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_contest")
    private Contest contest;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;

    @Column(name = "turn_number", columnDefinition = "integer default 0")
    private int turnNumber = 0;

    @Transient
    String fullName;

    @Transient
    String place;

    @Transient
    String tempId;

    @Transient
    Long categoryId;

    @Transient
    private String token;

    @Transient
    private List<Mark> summaryMarks;

    @OneToMany(mappedBy = "performance", fetch = FetchType.LAZY)
    private List<Mark> marks;

    @Override
    public String toString() {
        return "Performance{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Performance that = (Performance) o;
        return id.equals(that.id);
    }

    public static final Comparator<Performance> COMPARE_BY_TURN_NUMBER = Comparator.comparingInt(Performance::getTurnNumber);

}