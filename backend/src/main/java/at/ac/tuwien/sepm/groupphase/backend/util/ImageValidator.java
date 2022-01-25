package at.ac.tuwien.sepm.groupphase.backend.util;

import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class ImageValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public void validateImage(byte[] image) {
        byte ultimate = image[image.length - 1];
        byte penultimate = image[image.length - 2];

        if (image[0] == (byte) 0xFF && image[1] == (byte) 0xD8 && penultimate == (byte) 0xFF && ultimate == (byte) 0xD9) {        //Looks like JPEG
            return;
        }

        if (image[0] == (byte) 0x89 && image[1] == (byte) 0x50                                                              //Looks like PNG
            &&  image[2] == (byte) 0x4E && image[3] == (byte) 0x47
            &&  image[4] == (byte) 0x0D && image[5] == (byte) 0x0A
            &&  image[6] == (byte) 0x1A && image[7] == (byte) 0x0A) {
            return;
        }

        throw new ValidationException("The file is neither a PNG nor a JPEG");

    }
}
