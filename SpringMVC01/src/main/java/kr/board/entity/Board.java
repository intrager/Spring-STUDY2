package kr.board.entity;

public class Board {
	private int idx;	// 번호
	private String title;	// 제목
	private String content;	// 내용
	private String writer;	// 작성자
	private String indate;	// 작성일
	private int count;	// 조회수
	
	public int getIdx() {
		return idx;
	}
	public String getTitle() {
		return title;
	}
	public String getContent() {
		return content;
	}
	public String getWriter() {
		return writer;
	}
	public String getIndate() {
		return indate;
	}
	public int getCount() {
		return count;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public void setIndate(String indate) {
		this.indate = indate;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
