package com.centralserver.demo.domain.record.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RunRecordUpdateDTO {

    private String title;      // 제목 수정 가능
    private Boolean bookmark;  // 북마크 수정 가능(true/false)
}