package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.repository.TrashOrUsedRepository;
import org.springframework.stereotype.Component;

@Component
public class TrashOrUsedGenerator {
    private final TrashOrUsedRepository trashOrUsedRepository;

    public TrashOrUsedGenerator(TrashOrUsedRepository trashOrUsedRepository) {
        this.trashOrUsedRepository = trashOrUsedRepository;
    }

    void generate() {
        trashOrUsedRepository.findAll();
    }
}
