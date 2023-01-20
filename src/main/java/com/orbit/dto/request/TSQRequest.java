package com.orbit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TSQRequest {
    private String sessionId;
    private int channelCode;
    private String sourceInstitutionCode;
    private String responseCode;
}
