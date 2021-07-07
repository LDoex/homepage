package com.oyyk.homepage.req;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PageReq {
    @Positive(message = "页码不能为0")
    private int page;

    @NotNull(message = "每页条数不能为空")
    @Max(value = 500, message = "每页条数不能超过500")
    private int size;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageReq{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }
}