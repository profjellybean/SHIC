package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="APPLICATION_GROUP")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
/*
    @OneToMany
    @Column
    private Set<ApplicationUser> user;
*/
    @Column
    private Long groupId;

    public Group(Set<ApplicationUser> user, Long groupId) {
        //this.user = user;
        this.groupId = groupId;
    }

    public Group() {

    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
