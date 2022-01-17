package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "UnitOfQuantity")
public class UnitOfQuantity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name", unique = true)
    private String name;

    @Column(name = "GroupId")
    private Long groupId;

    public UnitOfQuantity() {
    }

    public UnitOfQuantity(String name) {
        this.name = name;
    }

    public UnitOfQuantity(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getGroupId() {
        return groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UnitOfQuantity that = (UnitOfQuantity) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "UnitOfQuantity{"
            +
            "id=" + id
            +
            ", name='" + name + '\''
            +
            '}';
    }
}
