package com.geshanzsq.nav.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
public class NavAboutDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String aboutWebDescription;

    private String aboutWebContent;

    private String aboutWebEmail;

    private String avatar;

    private String nickName;


    public NavAboutDto() {
    }
}
