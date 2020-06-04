package com.yuantu.dev.sys.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResetPswDTO {
    private String username;
    private String oldPsw;
    private String newPsw;

}
