package com.ennova.pubinfocommon.vo;

import lombok.Data;

@Data
public class PageUtil {
    private int pageSize;
    private int totalCount;
    private int currentPage;
    private int totalPage;
    private boolean next;
    private boolean previous;

    public PageUtil(int pageSize, int totalCount, int currentPage) {
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPage = calTotalPage(totalCount, pageSize);
        if (currentPage <= this.totalPage) {
            this.currentPage = currentPage;
        } else {
            this.currentPage = totalPage;
        }
        this.next = this.currentPage < this.totalPage;
        this.previous = this.currentPage > 1;
    }

    private int calTotalPage(int totalCount, int pageSize) {
        if (pageSize == 0) {
            return 1;
        }
        int size = totalCount / pageSize;
        int mod = totalCount % pageSize;
        if (mod != 0) {
            size++;
        }
        return totalCount == 0 ? 1 : size;
    }

}
