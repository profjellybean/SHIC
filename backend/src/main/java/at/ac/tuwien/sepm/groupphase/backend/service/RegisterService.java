package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Register;


public interface RegisterService {
    /**
     * Confirm an outstanding payment of a bill.
     */
    Register confirmPayment(Long registerId, Long billId, String username);

    /**
     * Find a register by a given id.
     *
     * @return the requested register
     */
    Register findOne(Long id);

    /**
     * Adds up the sums of all Bills that were paid in the current month.
     *
     * @return sum
     */
    Double billSumOfCurrentMonth(String userName);
}
