package com.web.editor.model.dto.user.search;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
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

	
	
}
