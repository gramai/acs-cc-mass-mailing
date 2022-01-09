package ro.acs.ssa.cc.mailing.userservice.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "bank_users")
public class BankUserEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column
    private String email;

    @Column
    private String iban;

    @Column
    private String availableAmount;

    @Basic(optional = false)
    @CreationTimestamp
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Basic(optional = false)
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date lastUpdatedOn;
}
