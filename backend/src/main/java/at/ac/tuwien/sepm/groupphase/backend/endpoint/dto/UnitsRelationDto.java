package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.NotNull;

public class UnitsRelationDto {
    private Long id;
    @NotNull
    private String baseUnit;
    @NotNull
    private String calculatedUnit;
    private Double relation;

    public UnitsRelationDto() {
    }

    public UnitsRelationDto(Long id, String baseUnit, String calculatedUnit, Double relation) {
        this.id = id;
        this.baseUnit = baseUnit;
        this.calculatedUnit = calculatedUnit;
        this.relation = relation;
    }

    public UnitsRelationDto(String baseUnit, String calculatedUnit, Double relation) {
        this.baseUnit = baseUnit;
        this.calculatedUnit = calculatedUnit;
        this.relation = relation;
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
