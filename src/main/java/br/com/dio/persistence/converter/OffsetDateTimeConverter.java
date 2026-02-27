package br.com.dio.persistence.converter;

import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class OffsetDateTimeConverter {

    private static final ZoneOffset UTC = ZoneOffset.UTC;

    public static OffsetDateTime toOffsetDateTime(Timestamp timestamp) {
        return nonNull(timestamp) ? OffsetDateTime.ofInstant(timestamp.toInstant(), UTC) : null;
    }

    public static Timestamp toTimestamp(OffsetDateTime dateTime) {
        return nonNull(dateTime) ? Timestamp.valueOf(dateTime.atZoneSameInstant(UTC).toLocalDateTime()) : null;
    }
}
