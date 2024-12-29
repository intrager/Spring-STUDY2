package org.zerohan.exzero.domain;

import java.util.Objects;

public class Criteria {
    private int pageNum;
    private int amount;

    public Criteria() {
        this.pageNum = 1;
        this.amount = 10;
    }

    public int getAmount() {
        return amount;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        if(pageNum <= 0) {
            this.pageNum = 1;
            return;
        }
        this.pageNum = pageNum;
    }

    public void setAmount(int amount) {
        if(amount <= 10 || amount > 100) {
            this.amount = 10;
            return;
        }
        this.amount = amount;
    }

    public int getSkippedAmount() {
        return this.pageNum * this.amount;
    }

    public int getSkip() {
        return (this.pageNum - 1) * this.amount;
    }

    @Override
    public String toString() {
        return "Criteria(" +
                "pageNum=" + pageNum +
                ", amount=" + amount +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Criteria)) return false;
        Criteria criteria = (Criteria) o;
        return pageNum == criteria.pageNum && amount == criteria.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageNum, amount);
    }
}
