package br.com.dio.dto;

import java.time.OffsetDateTime;

public record CardDetailsDTO(
        Long id,
        String title,
        String description,
        boolean blocked,
        OffsetDateTime blockedAt,
        String blockReason,
        int blocksAmount,
        Long columnId,
        String columnName
) {

    public boolean isBlocked() {
        return blocked;
    }

    public boolean isInColumn(Long columnId) {
        return this.columnId.equals(columnId);
    }
}
