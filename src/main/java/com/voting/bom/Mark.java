package com.voting.bom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer value;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_jury")
    private Jury jury;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_performance")
    private Performance performance;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_criteria")
    private Criteria criteria;

    @Transient
    private String criteriaName;

    @Transient
    private String juryLastName;

    @Transient
    private Long criteriaId;

    @Override
    public String toString() {
        return "Mark{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
