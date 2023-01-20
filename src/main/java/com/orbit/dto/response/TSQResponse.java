package com.orbit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TSQResponse {
    private String sessionId;
    private int channelCode;
    private String sourceInstitutionCode;
    private String responseCode;
}
