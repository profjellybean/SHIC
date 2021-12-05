package at.ac.tuwien.sepm.groupphase.backend.entity;



import javax.persistence.*;


@Entity
@Table(name = "Item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Quantity")
    private Long quantity;

    public Item(Long id) {
        this.id = id;
    }

    public Item() {

    }

    public Item(Long id, String name, Long quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

}
