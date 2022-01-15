package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class ImageDto {
    byte[] image;

    public ImageDto(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
