package at.ac.tuwien.sepm.groupphase.backend.util;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class ItemStorageValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public void validateItemStorageDto(ItemStorageDto itemStorageDto) {
        if (itemStorageDto.getNotes() != null && itemStorageDto.getNotes().length() > 511) {
            throw new ValidationException("Notes can be a maximum of 512 characters long");
        }
        if (itemStorageDto.getName() == null || itemStorageDto.getName().length() > 63) {
            throw new ValidationException("Names can be a maximum of 64 characters long");
        }

    }
}
