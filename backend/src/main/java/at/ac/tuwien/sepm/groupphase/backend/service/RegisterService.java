package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Register;


public interface RegisterService {
    /**
     * Confirm an outstanding payment of a bill.
     */
    Register confirmPayment(Long registerId, Long billId, Long userId);

    /**
     * Find a register by a given id.
     *
     * @return the requested register
     */
    Register findOne(Long id);
}
