package at.ac.tuwien.sepm.groupphase.backend.entity;



import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.UnitOfQuantity;

import javax.persistence.*;


@Entity
@Table(name = "Item")
public class Item {

    public Item() {}

    public Item(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Quantity")
    private UnitOfQuantity quantity;

    public Item(Long id) {
        this.id = id;
    }

    public Item() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public UnitOfQuantity getQuantity() {
        return quantity;
    }

    public void setQuantity(UnitOfQuantity quantity) {
        this.quantity = quantity;
    }

}
