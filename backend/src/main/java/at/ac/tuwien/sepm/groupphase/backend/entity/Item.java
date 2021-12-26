package at.ac.tuwien.sepm.groupphase.backend.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;


@Entity
@Table(name = "Item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name")
    private String name;

    @OneToOne //(name = "Quantity")
    private UnitOfQuantity quantity;

    @Column(name = "groupId")
    private Long groupId;

    public Item(Long id) {
        this.id = id;
    }

    public Item() {
    }

    public Item(Long id, String name, UnitOfQuantity quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public Item(Long id, String name, UnitOfQuantity quantity, Long groupId) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.groupId = groupId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UnitOfQuantity getQuantity() {
        return quantity;
    }

    public void setQuantity(UnitOfQuantity quantity) {
        this.quantity = quantity;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return Objects.equals(id, item.id) && Objects.equals(name, item.name)
            && Objects.equals(quantity, item.quantity) && Objects.equals(groupId, item.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, quantity, groupId);
    }

    @Override
    public String toString() {
        return "Item{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", quantity=" + quantity
            + ", groupId=" + groupId
            + '}';
    }
}
