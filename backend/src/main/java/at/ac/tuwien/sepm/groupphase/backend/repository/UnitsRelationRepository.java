package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.UnitsRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnitsRelationRepository extends JpaRepository<UnitsRelation, Long> {

    List<UnitsRelation> findAll();

    UnitsRelation findUnitsRelationByBaseUnitAndCalculatedUnit(String baseUnit, String calculatedUnit);

    List<UnitsRelation> findAllByBaseUnitOrCalculatedUnit(Long baseUnit, Long calculatedUnit);

    @Override
    void deleteAll();
}
