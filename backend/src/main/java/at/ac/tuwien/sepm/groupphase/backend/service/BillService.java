package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;

import java.util.List;

public interface BillService {

    /**
     * Find a bill by a given id.
     *
     * @return the requested bill
     */
    Bill findOne(Long id);

    /**
     * delete bill by given id.
     */
    void deleteById(Long id);

    /**
     * Find all bills with the register id.
     *
     * @param registerId the id of the regsiter
     * @return a list of all bills
     */
    List<Bill> findAll(Long registerId);

    /**
     * Creates a bill.
     *
     * @param bill the bill that will be added
     */
    Bill bill(Bill bill);

    /**
     * Updates a bill.
     *
     * @param billDtoToBill the updated bill
     * @return the updated bill
     */
    Bill updateBill(Bill billDtoToBill);
}
