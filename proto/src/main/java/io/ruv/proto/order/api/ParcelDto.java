package io.ruv.proto.order.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ParcelDto {

    private long id;
    private ParcelStatus status;

    private String source;
    private String destination;
    private String description;

    private String createdBy;
    private String assignedTo;

    private Instant createdAt;
    private Instant updatedAt;
}
