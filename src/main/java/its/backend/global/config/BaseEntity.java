package its.backend.global.config;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 모든 엔티티들은 이 엔티티를 상속 받는다.
 * 이 엔티티에는 생성일, 수정일, 상태값이 포함된다.
 */
@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity {
    @CreationTimestamp
    @Column(updatable = false)
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    @Enumerated(value = EnumType.STRING)
    protected Status status = Status.valueOf(Status.ACTIVE.toString());

    public enum Status {
        ACTIVE, DELETE
    }

    public void updateStatus(Status Status) {
        this.status = status;
    }
}
