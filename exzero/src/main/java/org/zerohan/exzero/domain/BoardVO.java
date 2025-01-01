package org.zerohan.exzero.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class BoardVO {
    private Long bno;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime registerDate;
    private LocalDateTime updateDate;

    public BoardVO(Long bno, String title, String content, String writer, LocalDateTime registerDate, LocalDateTime updateDate) {
        this.bno = bno;
        this.title = Objects.requireNonNull(title);
        this.content = Objects.requireNonNull(content);
        this.writer = Objects.requireNonNull(writer);
        this.registerDate = Objects.requireNonNull(registerDate);
        this.updateDate = Objects.requireNonNull(updateDate);
    }

    public Long getBno() {
        return bno;
    }

    public void setBno(Long bno) {
        this.bno = bno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "BoardVO(" +
                "bno=" + bno +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", writer='" + writer + '\'' +
                ", registerDate=" + registerDate +
                ", updateDate=" + updateDate +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardVO)) return false;
        BoardVO boardVO = (BoardVO) o;
        return Objects.equals(bno, boardVO.bno) && Objects.equals(title, boardVO.title) && Objects.equals(content, boardVO.content) && Objects.equals(writer, boardVO.writer) && Objects.equals(registerDate, boardVO.registerDate) && Objects.equals(updateDate, boardVO.updateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bno, title, content, writer, registerDate, updateDate);
    }


}
