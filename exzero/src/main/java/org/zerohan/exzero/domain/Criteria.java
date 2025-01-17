package org.zerohan.exzero.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Objects;

public class Criteria {
    private int pageNum;
    private int amount;

    private String[] types;
    private String keyword;

    private String typeWords;

    public Criteria() {
        this.pageNum = 1;
        this.amount = 10;
    }

    public Criteria(String[] types, String keyword) {
        this.pageNum = 1;
        this.amount = 10;
        this.types = types;
        this.keyword = keyword;
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

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
        if(types != null && types.length > 0) {
            typeWords = String.join("", types);
        }
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTypeWords() {
        return typeWords;
    }

    public void setTypeWords(String typeWords) {
        this.typeWords = typeWords;
    }

    @Override
    public String toString() {
        return "Criteria(" +
                "pageNum=" + pageNum +
                ", amount=" + amount +
                ", types=" + Arrays.toString(types) +
                ", keyword='" + keyword + '\'' +
                ", typeWords='" + typeWords + '\'' +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Criteria)) return false;
        Criteria criteria = (Criteria) o;
        return pageNum == criteria.pageNum && amount == criteria.amount && Objects.deepEquals(types, criteria.types) && Objects.equals(keyword, criteria.keyword) && Objects.equals(typeWords, criteria.typeWords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageNum, amount, Arrays.hashCode(types), keyword, typeWords);
    }
}
