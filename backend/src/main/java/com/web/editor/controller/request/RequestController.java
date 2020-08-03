package com.web.editor.controller.request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.StringTokenizer;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.editor.model.dto.request.NotifyDto;
import com.web.editor.model.dto.request.RequestDto;
import com.web.editor.model.dto.request.RequestStatusDto;
import com.web.editor.model.dto.request.RequestTagDto;
import com.web.editor.model.service.request.RequestService;

import io.swagger.annotations.ApiOperation;

//http://localhost:8080/swagger-ui.html

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/request")
public class RequestController {

	@Autowired
	private RequestService requestService;

	// 요청 검색
	@ApiOperation(value = "rid로 요청상세검색")
	@GetMapping("/{rid}")
	public Object searchRequest(@PathVariable int rid) {
		RequestDto dto = requestService.searchRequest(rid);
		if (dto != null) {
			// 태그 검색(태그 리스트를 문자열로 변환)
			String tag = TagsToString(rid);
			dto.setTag_list(tag);
			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("not exist", HttpStatus.NO_CONTENT);
		}
	}

	// 요청 리스트
	// 요청자(일반회원)의 리스트
	@ApiOperation(value = "요청자(일반회원)의 리스트")
	@GetMapping("/req/{request_nickname}")
	public Object searchListRequest(@PathVariable String request_nickname) throws UnsupportedEncodingException {
		List<RequestDto> requestList = requestService.searchListRequest(decodeURL(request_nickname));

		if (!requestList.isEmpty()) {
			return new ResponseEntity<>(requestList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("fail", HttpStatus.NO_CONTENT);
		}
	}
	// done_flag 0~2, nickname
	@ApiOperation(value = "일반회원의 요청 리스트 done_flag(0: 요청, 1: 진행, 2: 완료))")
	@GetMapping("/req/{nickname}/{done_flag}")
	public Object searchListFlagRequest(@PathVariable String nickname, @PathVariable int done_flag)
			throws UnsupportedEncodingException {
		RequestStatusDto statusDto = new RequestStatusDto();
		statusDto.setDone_flag(done_flag);
		statusDto.setNickname(decodeURL(nickname));

		List<RequestDto> requestList = requestService.searchListFlagRequest(statusDto);

		if (!requestList.isEmpty()) {
			return new ResponseEntity<>(requestList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("fail", HttpStatus.NO_CONTENT);
		}
	} 
	
	// 요청받는사람(편집자)의 리스트
	@ApiOperation(value = "요청받는사람(편집자)의 리스트")
	@GetMapping("/res/{response_nickname}")
	public Object searchListResponse(@PathVariable String response_nickname) throws UnsupportedEncodingException {
		
		List<RequestDto> responseList = requestService.searchListResponse(decodeURL(response_nickname));
		
		if (!responseList.isEmpty()) {
			return new ResponseEntity<>(responseList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("fail", HttpStatus.NO_CONTENT);
		}
	}
	@ApiOperation(value = "편집자의 요청 리스트 done_flag(0: 요청, 1: 진행, 2: 완료))")
	@GetMapping("/res/{nickname}/{done_flag}")
	public Object searchListFlagResponse(@PathVariable String nickname, @PathVariable int done_flag)
			throws UnsupportedEncodingException {
		RequestStatusDto statusDto = new RequestStatusDto();
		statusDto.setDone_flag(done_flag);
		statusDto.setNickname(decodeURL(nickname));

		List<RequestDto> responseList = requestService.searchListFlagResponse(statusDto);

		if (!responseList.isEmpty()) {
			return new ResponseEntity<>(responseList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("fail", HttpStatus.NO_CONTENT);
		}
	}


	// 요청 등록 및 알림 등록
	@ApiOperation(value = "요청 및 알림(type은 request) 등록, \"success\" 또는 \"fail\"반환")
	@PostMapping
	public ResponseEntity<String> insertRequest(@Valid @RequestBody RequestDto requestDto) {
		int result = requestService.insertRequest(requestDto);
		// 등록시 기본값 0
		requestDto.setDone_flag(0);
		
   	   	if (result > 0) {
			// 알림 함께 등록
			addNotify(requestDto);
			// 태그 함께 등록
			addTag(requestDto.getTag_list(), requestDto.getRid());

   			return new ResponseEntity<String>("success", HttpStatus.OK);
   		} else {
   			return new ResponseEntity<String>("fail", HttpStatus.NOT_FOUND);
   		}
	}
	
	// 요청 수락
    @ApiOperation(value = "해당 요청서 수락(done_flag를 1로), \"success\" 또는 \"fail\"반환")
   	@PutMapping("/accept/{rid}")
   	public ResponseEntity<String> acceptRequest(@PathVariable int rid) {
   		int result = requestService.acceptRequest(rid);
    	
   	   	if (result > 0) {
   			return new ResponseEntity<String>("success", HttpStatus.OK);
   		} else {
   			return new ResponseEntity<String>("fail", HttpStatus.NOT_FOUND);
   		}
	}
	// 요청 완료
    @ApiOperation(value = "해당 요청서 완료(done_flag를 2로), \"success\" 또는 \"fail\"반환")
   	@PutMapping("/done/{rid}")
   	public ResponseEntity<String> doneRequest(@PathVariable int rid) {
   		int result = requestService.doneRequest(rid);
    	
   	   	if (result > 0) {
   			return new ResponseEntity<String>("success", HttpStatus.OK);
   		} else {
   			return new ResponseEntity<String>("fail", HttpStatus.NOT_FOUND);
   		}
   	}
	// 요청 거절
    @ApiOperation(value = "해당 요청서 거절(done_flag를 5로), \"success\" 또는 \"fail\"반환")
   	@PutMapping("/deny/{rid}")
   	public ResponseEntity<String> denyRequest(@PathVariable int rid) {
   		int result = requestService.denyRequest(rid);
    	
   	   	if (result > 0) {
   			return new ResponseEntity<String>("success", HttpStatus.OK);
   		} else {
   			return new ResponseEntity<String>("fail", HttpStatus.NOT_FOUND);
   		}
   	}

	// 요청 삭제
    @ApiOperation(value = "rid에 해당하는 요청 삭제, \"success\" 또는 \"fail\"반환")
   	@DeleteMapping("{rid}")
   	public ResponseEntity<String> deleteRequest(@PathVariable int rid) {
		// 해당 요청서의 태그들 삭제
		requestService.deleteReqTag(rid);
    	int result = requestService.deleteRequest(rid);
    	
  	   	if (result > 0) {
   			return new ResponseEntity<String>("success", HttpStatus.OK);
   		} else {
   			return new ResponseEntity<String>("fail", HttpStatus.NOT_FOUND);
   		}
	}
	
	// 알림 조회
	@ApiOperation(value = "회원의 알림 리스트(limit 5)")
	@GetMapping("/notify/{notify_nickname}")	// 일반회원 (요청자)
	public Object searchNotify(@PathVariable String notify_nickname) throws UnsupportedEncodingException {
		List<NotifyDto> notifyList = requestService.searchNotify(decodeURL(notify_nickname));
		if (notifyList.size() >= 0) {
			return new ResponseEntity<>(notifyList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("fail", HttpStatus.NO_CONTENT);
		}
	}

	// 알림 삭제
    @ApiOperation(value = "nid에 해당하는 요청알림 삭제, \"success\" 또는 \"fail\"반환")
   	@DeleteMapping("/notify/{nid}")
   	public ResponseEntity<String> deleteNotify(@PathVariable int nid) {
    	int result = requestService.deleteNotify(nid);
    	
  	   	if (result > 0) {
   			return new ResponseEntity<String>("success", HttpStatus.OK);
   		} else {
   			return new ResponseEntity<String>("fail", HttpStatus.NOT_FOUND);
   		}
	}

	// 알림 수정(읽음)
	@ApiOperation(value = "nid에 해당하는 요청알림을 읽음,  \"success\" 또는 \"fail\"반환")
	@PutMapping("/notify/{nid}")
	public ResponseEntity<String> updateNotify(@PathVariable int nid) {
		int result = requestService.updateNotify(nid);
    	
		if (result > 0) {
		  	return new ResponseEntity<String>("success", HttpStatus.OK);
	  	} else {
		  	return new ResponseEntity<String>("fail", HttpStatus.NOT_FOUND);
	  	}
	}

	// 알림 수정2(회원이 알림을 전부 읽음)
	@ApiOperation(value = "회원이 요청알림을 전부 읽음,  \"success\" 또는 \"fail\"반환")
	@PutMapping("/notify/read/{notify_nickname}")
	public ResponseEntity<String> updateAllNotify(@PathVariable String notify_nickname)
			throws UnsupportedEncodingException {
		int result = requestService.updateAllNotify(decodeURL(notify_nickname));

		if (result > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("fail", HttpStatus.NOT_FOUND);
		}
	}

	private String decodeURL(String target) throws UnsupportedEncodingException {
		return URLDecoder.decode(target, StandardCharsets.UTF_8.toString());
	}

	// 알림 등록
	private void addNotify(RequestDto dto){
		// 일반회원 알림 등록
		NotifyDto notifyDtoStd = new NotifyDto();
		notifyDtoStd.setNotify_nickname(dto.getRequest_nickname());
		notifyDtoStd.setRead_flag(0);
		notifyDtoStd.setNotify_type("request");	// 알림 타입은 request
		notifyDtoStd.setRequest_nickname(dto.getRequest_nickname());
		notifyDtoStd.setResponse_nickname(dto.getResponse_nickname());
		requestService.insertNotify(notifyDtoStd);
		
		// 편집자 알림 등록
		NotifyDto notifyDtoEdi = new NotifyDto();
		notifyDtoEdi.setNotify_nickname(dto.getResponse_nickname());
		notifyDtoEdi.setRead_flag(0);
		notifyDtoEdi.setNotify_type("request");	// 알림 타입은 request
		notifyDtoEdi.setRequest_nickname(dto.getRequest_nickname());
		notifyDtoEdi.setResponse_nickname(dto.getResponse_nickname());
		requestService.insertNotify(notifyDtoEdi);
	}

	// 태그 검색
	@ApiOperation(value = "해당 요청서의 태그(rid)")
	@GetMapping("/tag/{rid}")
	public ResponseEntity<String> searchTag(@PathVariable int rid) {
		// 태그 검색(태그 리스트를 문자열로 변환)
		String tag = TagsToString(rid);

		return new ResponseEntity<String>(tag, HttpStatus.OK);

	}		
	

	// 태그 파싱, 등록
	private void addTag(String tags, int rid){
		tags = tags.trim();	// 앞뒤 공백 제거
		// #기준으로 나눔
		StringTokenizer st = new StringTokenizer(tags, "#");
		
		while(st.hasMoreTokens()) {
			// 태그 생성
			String tag = st.nextToken().trim();		// 앞뒤 공백 제거
			RequestTagDto tagDto = new RequestTagDto();
			tagDto.setTag_name(tag.toString());
			tagDto.setRequest_form_rid(rid);
			// 태그 등록
			requestService.insertTag(tagDto);
		}
	}

	// 요청서의 태그들을 문자열로
	private String TagsToString(int rid){
		// 해당 요청서의 태그 리스트
		List<RequestTagDto> tagList = requestService.searchTag(rid);

		// 태그 문자열
		StringBuilder tagStr = new StringBuilder();
		for (RequestTagDto tag : tagList){
			tagStr.append("#" + tag.getTag_name()+" ");
		}
		return tagStr.toString();
	}

}