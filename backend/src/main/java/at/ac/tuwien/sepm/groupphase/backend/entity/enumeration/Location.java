package at.ac.tuwien.sepm.groupphase.backend.entity.enumeration;

public enum Location {
    fridge((long) 1),
    freezer((long) 2),
    shelf((long) 3);

    public final Long value;

    Location(Long value) {
        this.value = value;
    }


}
