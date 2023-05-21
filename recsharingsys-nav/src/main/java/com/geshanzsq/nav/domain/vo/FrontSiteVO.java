package com.geshanzsq.nav.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
public class FrontSiteVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 站点id
     */
    private Long siteId;

    /**
     * 站点名称
     */
    private String siteName;

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 站点路径
     */
    private String sitePath;

    /**
     * 站点描述
     */
    private String siteDescription;

    /**
     * 站点地址
     */
    private String siteUrl;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 站点点赞量
     */
    private Integer likeCount;

    /**
     * 站点收藏量
     */
    private Integer collectCount;


}
