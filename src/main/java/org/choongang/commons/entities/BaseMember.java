package org.choongang.commons.entities;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter @Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseMember extends Base {
    @CreatedDate
    @Column(length = 40, updatable = false)
    private String createdBy;
    @LastModifiedDate
    @Column(length = 40, insertable = false)
    private String modifiedBy;
}
