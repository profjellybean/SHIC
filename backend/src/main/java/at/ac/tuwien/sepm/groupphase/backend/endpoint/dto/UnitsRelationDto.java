package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.NotNull;

public class UnitsRelationDto {
    private Long id;
    @NotNull
    private Long baseUnit;
    @NotNull
    private Long calculatedUnit;
    private Double relation;

    public UnitsRelationDto() {
    }

    public UnitsRelationDto(Long id, Long baseUnit, Long calculatedUnit, Double relation) {
        this.id = id;
        this.baseUnit = baseUnit;
        this.calculatedUnit = calculatedUnit;
        this.relation = relation;
    }

    public UnitsRelationDto(Long baseUnit, Long calculatedUnit, Double relation) {
        this.baseUnit = baseUnit;
        this.calculatedUnit = calculatedUnit;
        this.relation = relation;
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
