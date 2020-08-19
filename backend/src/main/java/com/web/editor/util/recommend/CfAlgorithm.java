package com.web.editor.util.recommend;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.web.editor.model.dto.user.recommend.EditorDto;
import com.web.editor.model.service.user.BookmarkService;

import org.springframework.beans.factory.annotation.Autowired;

public class CfAlgorithm {

    @Autowired
    BookmarkService bookmarkService;

    final int MaxScore = 1;
    double weightTag = 0;
    int tagPerBookmark;

    // 유저의 북마크와 전체 유저의 리스트들, 유클리디안 거리
    public List<EditorDto> recommendByBookmark(EditorDto bookmarks, List<EditorDto> editors, String myUid) {

        String[] bookmarkUid = bookmarks.getUid().split(",");
        int cnt = bookmarkUid.length;
        tagPerBookmark = bookmarks.getTag().split(",").length / cnt;
        // 북마크 하나당 평균 태그개수마다 가중치
        weightTag = (double)tagPerBookmark * 0.1;
        if (weightTag > 1) weightTag = 0.5;

        List<EditorDto> recommendList = new ArrayList<>();
        // for 에디터마다 유클리디안 거리 구하기 (태그, 비디오 기술)
        L: for(EditorDto editor : editors) {
            if (editor.getTag() == null && editor.getSkill() == null) continue;
            
            // 본인 제외
            if (editor.getUid().equals(myUid)) continue;

            // 북마크와 일치하는 에디터 제외
            if (cnt > 0) {
                for (String uid : bookmarkUid){
                    if (uid.equals(editor.getUid()))  {
                        cnt--;
                        continue L;
                    }
                }
            }
            // 유클리디안 거리
            double sum = 0;
            
            double bookTag = MaxScore;
            double ediTag =  editorTag(bookmarks.getTag(), editor.getTag());
            sum += (double)(bookTag - ediTag) * (bookTag - ediTag);
            // skill
            double bookSkill = MaxScore;
            double ediSkill =  editorSkill(bookmarks.getSkill(), editor.getSkill());
            sum += (double)(bookSkill - ediSkill) * (bookSkill - ediSkill);

            // 연관성이 없을때
            if (ediTag == 0 && ediSkill == 0) {
                continue L;
            }

            double sim = 1 / (1 + Math.sqrt(sum));

            editor.setSimilarity(sim);
            recommendList.add(editor);
        }
        
        recommendList.sort(new Comparator<EditorDto>(){
            @Override
            public int compare(EditorDto o1, EditorDto o2) {
                double a = o1.getSimilarity();
                double b = o2.getSimilarity();
                if (a > b) return -1;
                if (a < b) return 1;
                else return 0;
            }
        });
        
        return recommendList;
    }

    // 태그 점수화
    private double editorTag(String book, String edi) {
        if (book == null || edi == null) return 0;
        // 태그를 문자열 배열로
        String[] bookTags = book.split(",");
        int eq = 0;
    
        for (String bookTag: bookTags) {
            // 태그를 포함하면
            if (edi.trim().contains(bookTag.trim()))  eq+=1; 
        }
        double tagScore = eq/(double)tagPerBookmark * MaxScore * (1 + weightTag);

        if (tagScore > 1) return 1;
        return tagScore;
    }

    // 스킬 점수화
    private double editorSkill(String book, String edi) {
        if (book == null || edi == null) return 0;
        String[] bookSkills = book.split(",");
        int eq = 0;

        for (String bookSkill: bookSkills) {
            if (edi.trim().contains(bookSkill.trim()))  eq+=1; 
        }
        double skillScore = eq/(double)bookSkills.length * MaxScore * 1;

        return skillScore;
    }

}
