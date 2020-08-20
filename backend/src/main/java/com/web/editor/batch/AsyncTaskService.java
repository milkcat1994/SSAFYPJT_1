package com.web.editor.batch;

import com.web.editor.model.service.request.RequestService;
import com.web.editor.model.service.user.search.SearchService;
import com.web.editor.model.service.user.search.redis.SearchRedisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AsyncTaskService {
    
	@Autowired
	SearchRedisService searchRedisService;

	@Autowired
	SearchService searchService;

	@Autowired
    RequestService requestService;
    
    /*
     * 해당 파일에서 백그라운드에서 실행할 로직을 구현한다.
     */
	// 초 * 1000
	// 1시간 : 3600000
	// 1일 : 86400000
	@Scheduled(fixedDelay = 3600000)
	public void SaveDatatoRedis() {
		try {
			System.out.println("Redis Update Start");

			// 검색 기록 모두 삭제
			// long deleteSearch = searchRedisService.deleteKeys("search:");
			// System.out.println("검색기록 "+deleteSearch+"개 삭제");
			
			// DB내용 Redis로 끌어올 필요 있음. 
			searchRedisService.deleteKeys("uid:*");
			searchRedisService.deleteKeys("uids*");
			searchRedisService.deleteKeys("nickname:uids:*");
			searchRedisService.portfolioAndBookmarkSave(searchService.joinBookmarks());
			searchRedisService.deleteKeys("nickname:review:");
			searchRedisService.portfolioAndVideoSave(searchService.joinVideos());
			searchRedisService.requestAndReviewSave(requestService.joinScores());
			searchRedisService.makeUserInfo();
			searchRedisService.deleteKeys("videoType:*");
			searchRedisService.deleteKeys("videoStyle:*");
			searchRedisService.deleteKeys("videoSkill:*");
			searchRedisService.searchRequestVideoInfoSave(requestService.searchRequestVideoInfo());
			searchRedisService.deleteKeys("tag:*");
			searchRedisService.deleteKeys("tags:*");
			searchRedisService.portfolioTagSave(searchService.searchPortfolioTag());
			
			long deleteSearch = searchRedisService.deleteKeys("search:");
			// nickname과 uid Hash삭제
			searchRedisService.deleteKeys("nickname:uid:");
			System.out.println("Redis Update End");
			
		} catch (RedisConnectionFailureException e) {
			// e.printStackTrace();
			System.out.println("Redis연결에 실패하여 검색 기록을 삭제 및 DB에 저장 할 수 없습니다.");
		}
	}
}