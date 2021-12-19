package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UnitsRelation")
public class UnitsRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String baseUnit;
    private String calculatedUnit;
    private Double relation;

    public UnitsRelation(String baseUnit, String calculatedUnit, Double relation) {
        this.baseUnit = baseUnit;
        this.calculatedUnit = calculatedUnit;
        this.relation = relation;
    }

    public UnitsRelation() {
    }

    public String getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(String baseUnit) {
        this.baseUnit = baseUnit;
    }

    public String getCalculatedUnit() {
        return calculatedUnit;
    }

    public void setCalculatedUnit(String calculatedUnit) {
        this.calculatedUnit = calculatedUnit;
    }

    public Double getRelation() {
        return relation;
    }

    public void setRelation(Double relation) {
        this.relation = relation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
