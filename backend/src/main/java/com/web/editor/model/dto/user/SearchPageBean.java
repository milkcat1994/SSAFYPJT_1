package com.web.editor.model.dto.user;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class SearchPageBean implements Serializable {

    private int uid;
    private String nickname;
    private String payMin;
    private String description;
    
    
	private List<Integer> searchType;
	
	/**웹 페이지 링크*/
	private String 	pagelink;
	/**조회할 페이지 번호*/
	private int 	pageNo;
	/**한 페이지에 표시할 데이타 수*/
	private int 	interval = 5;
	/**한 페이지에 표시할 시작 번호*/
	private int 	start=1;
	/**한 페이지에 표시할 끝 번호 */
	private int 	end=interval;
	
	

//	public List<Integer> getSearchType() {
//		return searchType;
//	}
//	public void setSearchType(List<Integer> searchType) {
//		this.searchType = searchType;
//	}
	public String getPagelink() {
		return pagelink;
	}
	public void setPagelink(String pagelink) {
		this.pagelink = pagelink;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}

	
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Integer> getSearchType() {
		return searchType;
	}
	public void setSearchType(List<Integer> searchType) {
		this.searchType = searchType;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPayMin() {
		return payMin;
	}
	public void setPayMin(String payMin) {
		this.payMin = payMin;
	}

	
	
}
