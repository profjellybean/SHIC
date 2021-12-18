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
    private Long baseUnit;
    private Long calculatedUnit;
    private Double relation;

    public UnitsRelation(Long baseUnit, Long calculatedUnit, Double relation) {
        this.baseUnit = baseUnit;
        this.calculatedUnit = calculatedUnit;
        this.relation = relation;
    }

    public UnitsRelation() {
    }

    public Long getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(Long baseUnit) {
        this.baseUnit = baseUnit;
    }

    public Long getCalculatedUnit() {
        return calculatedUnit;
    }

    public void setCalculatedUnit(Long calculatedUnit) {
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
}
