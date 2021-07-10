package com.oyyk.homepage.req;

import javax.validation.constraints.NotEmpty;

public class OthersSaveReq {
    private Long id;

    @NotEmpty(message = "简介内容不能为空")
    private String content;

    @NotEmpty(message = "页脚信息不能为空")
    private String footer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", content=").append(content);
        sb.append(", footer=").append(footer);
        sb.append("]");
        return sb.toString();
    }
}