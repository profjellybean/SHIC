package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;

import java.util.List;

public interface BillService {

    Bill findOne(Long id);

    List<Bill> findAll();
}
