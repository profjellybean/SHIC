package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import java.util.Objects;

@Entity
@Table(name = "Location")
public class LocationClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name")
    private String name;

    @Column
    private Long storageId;

    public LocationClass() {

    }

    public LocationClass(Long id, String name, Long storageId) {
        this.id = id;
        this.name = name;
        this.storageId = storageId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocationClass locationClass = (LocationClass) o;
        return Objects.equals(id, locationClass.id) && Objects.equals(name, locationClass.name) && Objects.equals(storageId, locationClass.storageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, storageId);
    }

    @Override
    public String toString() {
        return "Location{"
            + "id="
            + id
            + ", name='"
            + name
            + '\''
            + ", storageId="
            + storageId
            + '}';
    }
}
