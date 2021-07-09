package com.oyyk.homepage.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DocSaveReq {
    private Long id;

    @NotNull(message = "外部类不能为空")
    private Long outcateId;

    @NotNull(message = "父类不能为空")
    private Long parent;

    @NotEmpty(message = "名称不能为空")
    private String name;

    @NotNull(message = "顺序不能为空")
    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;

    @NotEmpty(message = "内容不能为空")
    private String content;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOutcateId() {
        return outcateId;
    }

    public void setOutcateId(Long outcateId) {
        this.outcateId = outcateId;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "DocSaveReq{" +
                "id=" + id +
                ", outcateId=" + outcateId +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", viewCount=" + viewCount +
                ", voteCount=" + voteCount +
                ", content='" + content + '\'' +
                '}';
    }
}