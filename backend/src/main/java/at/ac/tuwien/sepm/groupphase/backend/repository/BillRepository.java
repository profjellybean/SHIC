package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    /**
     * Get a bill by a given id.
     *
     * @return the requested bill
     */
    Bill getById(Long id);

    /**
     * Save a given bill.
     *
     * @return the saved bill
     */
    Bill save(Bill bill);

    /**
     * Adds up the sums of all Bills that were paid in the current month.
     *
     * @return sum
     */
    //@Query(value = "SELECT SUM(B.PRICE) FROM BILL AS B WHERE (B.REGISTER_ID = :registerId) AND (B.PAID_ON > @startMonthDate)", nativeQuery = true)
    @Query(value = "SELECT SUM(B.PRICE) FROM BILL AS B WHERE (B.REGISTER_ID = :registerId) AND (YEAR(B.PAID_ON) = YEAR(:today) AND MONTH(B.PAID_ON) = MONTH(:today))", nativeQuery = true)
    Double billSumOfCurrentMonth(@Param("registerId") Long registerId, @Param("today") LocalDate today);
}
