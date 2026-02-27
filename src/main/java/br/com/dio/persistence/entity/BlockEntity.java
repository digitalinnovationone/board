package br.com.dio.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
public class BlockEntity {

    private Long id;
    private OffsetDateTime blockedAt;
    private String blockReason;
    private OffsetDateTime unblockedAt;
    private String unblockReason;

    /**
     * Retorna true se o card está bloqueado atualmente.
     */
    public boolean isBlocked() {
        return blockedAt != null && unblockedAt == null;
    }
}
