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
import com.web.editor.model.dto.request.RequestReview;
import com.web.editor.model.dto.request.RequestReviewSaveRequest;
import com.web.editor.model.dto.request.RequestStatusDto;
import com.web.editor.model.dto.request.RequestTagDto;
import com.web.editor.model.response.BasicResponse;
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
			String tags = tagsToString(rid);
			dto.setTag_list(tags);
			return new ResponseEntity<>(dto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("not exist", HttpStatus.NO_CONTENT);
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

		for (RequestDto requestDto : requestList) {
			if(requestDto.getVideo_type().equals("pers"))
			requestDto.setVideo_type("개인용");
			else if(requestDto.getVideo_type().equals("comm"))
				requestDto.setVideo_type("상업용");

			if(requestDto.getVideo_style().equals("kids"))
				requestDto.setVideo_style("키즈");
			else if(requestDto.getVideo_style().equals("game"))
				requestDto.setVideo_style("게임");
			else if(requestDto.getVideo_style().equals("musi"))
				requestDto.setVideo_style("음악/댄스");
			else if(requestDto.getVideo_style().equals("food"))
				requestDto.setVideo_style("푸드/쿠킹");
			else if(requestDto.getVideo_style().equals("vlog"))
				requestDto.setVideo_style("V-log");
			else if(requestDto.getVideo_style().equals("movi"))
				requestDto.setVideo_style("영화/애니메이션");
			else if(requestDto.getVideo_style().equals("anim"))
				requestDto.setVideo_style("동물");
			else if(requestDto.getVideo_style().equals("beau"))
				requestDto.setVideo_style("뷰티/패션");
			else if(requestDto.getVideo_style().equals("spor"))
				requestDto.setVideo_style("스포츠");
			
			if(requestDto.getVideo_skill().length() > 0){
				StringTokenizer st = new StringTokenizer(requestDto.getVideo_skill(), ",");
				String skills = "";
				while(st.hasMoreTokens()){
					String skill = st.nextToken();
					if(skill.equals("colr")){
						skills += ", 색/밝기 조정";
					} else if(skill.equals("audi")){
						skills += ", 음향";
					} else if(skill.equals("moti")){
						skills += ", 모션그래픽";
					} else if(skill.equals("capt")){
						skills += ", 자막";
					} else if(skill.equals("intr")){
						skills += ", 인트로";
					} else if(skill.equals("outr")){
						skills += ", 아웃트로";
					}
				}
				int len = skills.length();
				skills = skills.substring(2,len);
				requestDto.setVideo_skill(skills);;
			}

		}

		if (!requestList.isEmpty()) {
			return new ResponseEntity<>(requestList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("fail", HttpStatus.NO_CONTENT);
		}
	}

	// 요청받는사람(편집자)의 리스트

	@ApiOperation(value = "편집자의 요청 리스트 done_flag(0: 요청, 1: 진행, 2: 완료))")
	@GetMapping("/res/{nickname}/{done_flag}")
	public Object searchListFlagResponse(@PathVariable String nickname, @PathVariable int done_flag)
			throws UnsupportedEncodingException {
		
		RequestStatusDto statusDto = new RequestStatusDto();
		statusDto.setDone_flag(done_flag);
		statusDto.setNickname(decodeURL(nickname));

		List<RequestDto> responseList = requestService.searchListFlagResponse(statusDto);

		for (RequestDto requestDto : responseList) {
			if(requestDto.getVideo_type().equals("pers"))
			requestDto.setVideo_type("개인용");
			else if(requestDto.getVideo_type().equals("comm"))
				requestDto.setVideo_type("상업용");

			if(requestDto.getVideo_style().equals("kids"))
				requestDto.setVideo_style("키즈");
			else if(requestDto.getVideo_style().equals("game"))
				requestDto.setVideo_style("게임");
			else if(requestDto.getVideo_style().equals("musi"))
				requestDto.setVideo_style("음악/댄스");
			else if(requestDto.getVideo_style().equals("food"))
				requestDto.setVideo_style("푸드/쿠킹");
			else if(requestDto.getVideo_style().equals("vlog"))
				requestDto.setVideo_style("V-log");
			else if(requestDto.getVideo_style().equals("movi"))
				requestDto.setVideo_style("영화/애니메이션");
			else if(requestDto.getVideo_style().equals("anim"))
				requestDto.setVideo_style("동물");
			else if(requestDto.getVideo_style().equals("beau"))
				requestDto.setVideo_style("뷰티/패션");
			else if(requestDto.getVideo_style().equals("spor"))
				requestDto.setVideo_style("스포츠");
			
			if(requestDto.getVideo_skill().length() > 0){
				StringTokenizer st = new StringTokenizer(requestDto.getVideo_skill(), ",");
				String skills = "";
				while(st.hasMoreTokens()){
					String skill = st.nextToken();
					if(skill.equals("colr")){
						skills += ", 색/밝기 조정";
					} else if(skill.equals("audi")){
						skills += ", 음향";
					} else if(skill.equals("moti")){
						skills += ", 모션그래픽";
					} else if(skill.equals("capt")){
						skills += ", 자막";
					} else if(skill.equals("intr")){
						skills += ", 인트로";
					} else if(skill.equals("outr")){
						skills += ", 아웃트로";
					}
				}
				int len = skills.length();
				skills = skills.substring(2,len);
				requestDto.setVideo_skill(skills);;
			}

		}
		
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
		// 등록시 기본값 0
		requestDto.setDone_flag(0);
		requestDto.setVideo_skill(requestDto.getVideo_skill().substring(1));

		int result = requestService.insertRequest(requestDto);

		if (result > 0) {
			// 알림 함께 등록
			addNotify(requestDto, "request");
			// 태그 함께 등록
			if (requestDto.getTags() != null || 
				(requestDto.getTags() != null && requestDto.getTags().size() > 0))
				addTag(requestDto.getTags(), requestDto.getRid());

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
			// 요청 수락 시 일반회원이 알림을 받음
			RequestDto dto = requestService.searchRequest(rid);
			String reqNickname = dto.getResponse_nickname();
			String resNickname = dto.getRequest_nickname(); // 일반회원이 알림받는 사람이됨
			dto.setRequest_nickname(reqNickname);
			dto.setResponse_nickname(resNickname);

			addNotify(dto, "accepted");
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
			// 요청자가 알림을 받는 사람이 됨
			RequestDto dto = requestService.searchRequest(rid);
			String reqNickname = dto.getResponse_nickname();
			String resNickname = dto.getRequest_nickname(); // 일반회원이 알림받는 사람이됨
			dto.setRequest_nickname(reqNickname);
			dto.setResponse_nickname(resNickname);
			addNotify(dto, "review");
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("fail", HttpStatus.NOT_FOUND);
		}
	}

	// 리뷰 완료
	@ApiOperation(value = "해당 요청서 리뷰 완료(done_flag를 3로), \"success\" 또는 \"fail\"반환")
	@PutMapping("/doneReview/{rid}")
	public ResponseEntity<String> doneReview(@PathVariable int rid) {
		int result = requestService.doneReview(rid);

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

	// 알림 조회
	@ApiOperation(value = "회원의 알림 리스트(limit 5)")
	@GetMapping("/notify/{response_nickname}") // 일반회원 (요청자)
	public Object searchNotify(@PathVariable String response_nickname) throws UnsupportedEncodingException {
		List<NotifyDto> notifyList = requestService.searchNotify(decodeURL(response_nickname));
		if (notifyList.size() >= 0) {
			BasicResponse result = new BasicResponse();
			result.data = notifyList.size() + "";
			result.status = true;
			// 5개만 보내줌
			if (notifyList.size() > 5) result.object = notifyList.subList(0, 5);
			else result.object = notifyList;
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("fail", HttpStatus.NO_CONTENT);
		}
	}


	// 알림 수정2(회원이 알림을 전부 읽음)
	@ApiOperation(value = "회원이 요청알림을 전부 읽음,  \"success\" 또는 \"fail\"반환")
	@PutMapping("/notify/read/{response_nickname}")
	public ResponseEntity<String> updateAllNotify(@PathVariable String response_nickname)
			throws UnsupportedEncodingException {
		int result = requestService.updateAllNotify(decodeURL(response_nickname));

		if (result > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("fail", HttpStatus.NOT_FOUND);
		}
	}

	private String decodeURL(String target) throws UnsupportedEncodingException {
		return URLDecoder.decode(target, StandardCharsets.UTF_8.toString());
	}

	// 알림 등록
	private void addNotify(RequestDto dto, String type) {
		// 편집자 알림 등록
		NotifyDto notifyDto = new NotifyDto();
		notifyDto.setRead_flag(0);
		notifyDto.setNotify_type(type); // 알림 타입은 request or review
		notifyDto.setRequest_nickname(dto.getRequest_nickname()); // 알림 요청한사람
		notifyDto.setResponse_nickname(dto.getResponse_nickname()); // 알림 받는사람
		requestService.insertNotify(notifyDto);
	}

	// 태그 파싱, 등록
	private int addTag(List<String> tags, int rid) {

		for (String tag : tags){
			RequestTagDto tagDto = new RequestTagDto();
			tagDto.setTag_name(tag);
			tagDto.setRequest_form_rid(rid);
			
			int res = requestService.insertTag(tagDto);
			if (res < 0) {
				return -1;
			}
		}
		return 1;

	}

	// 요청서의 태그들을 문자열로
	private String tagsToString(int rid) {
		// 해당 요청서의 태그 리스트
		List<RequestTagDto> tagList = requestService.searchTag(rid);

		// 태그 문자열
		StringBuilder tagStr = new StringBuilder();
		for (RequestTagDto tag : tagList) {
			tagStr.append("#" + tag.getTag_name() + " ");
		}
		return tagStr.toString();
	}

	
	// 요청서의 리뷰 조회
	@ApiOperation(value = "요청서의 리뷰 조회")
	@GetMapping("/review/{rid}")
	public Object searchReview(@PathVariable String rid) {
		RequestReview review = requestService.searchReview(Integer.parseInt(rid));

		if (review != null) {
			return new ResponseEntity<>(review, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("fail", HttpStatus.NO_CONTENT);
		}
	}

	
	// 요청서의 리뷰
	@ApiOperation(value = "요청서의 리뷰 등록")
	@PostMapping("/review")
	public Object insertReview(@RequestBody RequestReviewSaveRequest requestReviewSaveRequest) {
		float avg = requestReviewSaveRequest.getFinishScore() + requestReviewSaveRequest.getKindnessScore() + requestReviewSaveRequest.getVideoScore();
		requestReviewSaveRequest.setScoreAvg(avg/3);
		int result = requestService.insertReview(requestReviewSaveRequest);
		
		if (result > 0) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("fail", HttpStatus.NO_CONTENT);
		}
	}
	
	// 요청서의 리뷰
	@ApiOperation(value = "요청서의 리뷰 삭제")
	@DeleteMapping("/review/{rid}")
	public Object deleteReview(@PathVariable String rid) {
		int result = requestService.deleteReview(Integer.parseInt(rid));
		//삭제 이후 해당 request는 상태를 4(리뷰 삭제됨)로 변경해야한다.
		result = requestService.deleteReviewAndUpdateRequest(Integer.parseInt(rid));

		if (result > 0) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("fail", HttpStatus.NO_CONTENT);
		}
	}
	
}
