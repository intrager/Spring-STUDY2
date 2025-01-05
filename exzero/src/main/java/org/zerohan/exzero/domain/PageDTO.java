package org.zerohan.exzero.domain;

public class PageDTO {
    private int startPage; // 시작 페이지 번호
    private int endPage; // 화면에서의 마지막 번호
    private boolean prev, next;

    private int total;
    private Criteria criteria;

    public PageDTO(Criteria criteria, int total) {
        this.criteria = criteria;
        this.total = total;

        this.endPage = (int) (Math.ceil(criteria.getPageNum() / 10.0)) * 10;
        this.startPage = this.endPage - 9;
        int realEnd = (int) (Math.ceil((total * 1.0) / criteria.getAmount()));

        if(realEnd <= this.endPage) {
            this.endPage = realEnd;
        }

        this.prev = this.startPage > 1;
        this.next = this.endPage < realEnd;
    }

    public int getStartPage() {
        return startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public boolean isPrev() {
        return prev;
    }

    public boolean isNext() {
        return next;
    }

    public int getTotal() {
        return total;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    @Override
    public String toString() {
        return "PageDTO(" +
                "startPage=" + startPage +
                ", endPage=" + endPage +
                ", prev=" + prev +
                ", next=" + next +
                ", total=" + total +
                ", criteria=" + criteria +
                ')';
    }
}
