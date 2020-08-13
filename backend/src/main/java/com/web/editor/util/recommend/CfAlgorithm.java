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


    // 유저의 북마크와 전체 유저의 리스트들, 유클리디안 거리
    public List<EditorDto> recommendByBookmark(List<EditorDto> bookmarks, List<EditorDto> editors) {
        
        List<EditorDto> recommendList = new ArrayList<>();
        // for 에디터마다 유클리디안 거리 구하기 (태그, 비디오 기술)
        L: for(EditorDto editor : editors) {
            for (EditorDto bookmark : bookmarks){
                if (bookmark.getUid().equals(editor.getUid()))  continue L;
            }
            double sim_before = 0;
            for (EditorDto bookmark : bookmarks) {
                // double sim = ( sumXY- ((sumX*sumY)/N) )/ Math.sqrt( (sumPowX - ((sumX * sumX) / N)) * (sumPowY - ((sumY * sumY)/N)));
                // 유클리디안 거리
                double sum = 0;
                
                // pay
                double bookPay = 1;
                double ediPay = editorPay(Double.parseDouble(bookmark.getPay()), Double.parseDouble(editor.getPay()));
                sum +=(bookPay - ediPay) * (bookPay - ediPay);
                // tag
                double bookTag = 1;
                double ediTag =  editorTag(bookmark.getTags(), editor.getTags());
                sum += (bookTag - ediTag) * (bookTag - ediTag);
                // skill
                double bookSkill = 1;
                double ediSkill =  editorSkill(bookmark.getSkill(), editor.getSkill());
                sum += (bookSkill - ediSkill) * (bookSkill - ediSkill);

                double sim = 1 / (1 + Math.sqrt(sum));

                if (sim_before < sim) {
                    editor.setSimilarity(sim);
                // recommendList.offer(bookmark);
                    if (sim_before != 0)    recommendList.remove(recommendList.size()-1);
                    recommendList.add(editor);
                    sim_before = sim;
                }
            }   
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
        // 태그를 문자열 배열로
        String[] tags = book.split(",");
        int eq = 0;

        for (String tag: tags) {
            // 태그를 포함하면
            if (edi.contains(tag))  eq++; 
        }
        return eq/tags.length * 1;
    }

    // 스킬 점수화
    private double editorSkill(String book, String edi) {
        String[] skills = book.split(",");
        int eq = 0;

        for (String skill: skills) {
            // 태그를 포함하지 않으면
            if (!edi.contains(skill))  eq++; 
        }
        return eq/skills.length * 1;
    }

    // 금액 점수화
    private double editorPay(double book, double edi){
        return (book - Math.abs(book-edi)) / book * 1;
    }


}