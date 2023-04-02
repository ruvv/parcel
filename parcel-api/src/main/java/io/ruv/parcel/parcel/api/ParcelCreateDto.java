package io.ruv.parcel.parcel.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ParcelCreateDto {

    private String source;
    private String destination;
    private String description;
}
