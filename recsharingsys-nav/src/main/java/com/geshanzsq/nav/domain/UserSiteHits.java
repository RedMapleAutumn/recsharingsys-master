package com.geshanzsq.nav.domain;

import com.geshanzsq.common.core.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户站点点击量
 */
@Data
public class UserSiteHits extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 网站ID
     */
    private Long siteId;

    /**
     * 用户网站点击量
     */
    private Long hits;

}
