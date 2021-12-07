package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;

import java.util.List;

public interface BillService {

    /**
     * Find a bill by a given id
     *
     * @return the requested bill
     */
    Bill findOne(Long id);

    /**
     * Find all bills
     *
     * @return a list of all bills
     */
    List<Bill> findAll();
}
