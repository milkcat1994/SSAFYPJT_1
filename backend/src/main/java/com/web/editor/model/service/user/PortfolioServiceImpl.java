package com.web.editor.model.service.user;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.web.editor.model.dto.user.Portfolio;
import com.web.editor.model.dto.user.PortfolioInfo;
import com.web.editor.model.dto.user.PortfolioNicknameUpdateRequest;
import com.web.editor.model.dto.user.PortfolioTag;
import com.web.editor.model.dto.user.PortfolioTagSaveRequest;
import com.web.editor.model.dto.user.PortfolioReview;
import com.web.editor.model.dto.user.PortfolioReviewSaveRequest;
import com.web.editor.model.dto.user.PortfolioSchedule;
import com.web.editor.model.dto.user.PortfolioScheduleDeleteRequest;
import com.web.editor.model.dto.user.PortfolioScheduleSaveRequest;
import com.web.editor.model.dto.user.PortfolioVideo;
import com.web.editor.model.dto.user.PortfolioVideoDeleteRequest;
import com.web.editor.model.dto.user.PortfolioVideoSaveRequest;
import com.web.editor.model.mapper.user.PortfolioMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PortfolioServiceImpl implements PortfolioService {
    @Autowired
    private PortfolioMapper portfolioMapper;

    @Override
    public Portfolio findPortfolioByUid(String uid) {
        Portfolio portfolio = null;
        try {
            portfolio = portfolioMapper.findPortfolioByUid(uid);
            // 찾지 못한 경우
            if (portfolio == null) {
                portfolio = new Portfolio();
            }
            return portfolio;
        } catch (SQLException e) {
            e.printStackTrace();
            return portfolio;
        }
    }

    @Override
    public List<PortfolioVideo> findVideoByUid(String uid) {
        List<PortfolioVideo> video = null;
        try {
            video = portfolioMapper.findVideoByUid(uid);
            // 찾지 못한 경우
            if (video == null) {
                video = new ArrayList<>();
            }
            return video;
        } catch (SQLException e) {
            e.printStackTrace();
            return video;
        }
    }

    @Override
    public List<PortfolioTag> findTagByUid(String uid) {
        List<PortfolioTag> portfolioTag = null;
        try {
            portfolioTag = portfolioMapper.findTagByUid(uid);
            // 찾지 못한 경우
            if (portfolioTag == null) {
                portfolioTag = new ArrayList<>();
            }
            return portfolioTag;
        } catch (SQLException e) {
            e.printStackTrace();
            return portfolioTag;
        }
    }

    @Override
    public List<PortfolioSchedule> findScheduleByUid(String uid) {
        List<PortfolioSchedule> schedule = null;
        try {
            schedule = portfolioMapper.findScheduleByUid(uid);
            // 찾지 못한 경우
            if (schedule == null) {
                schedule = new ArrayList<>();
            }
            return schedule;
        } catch (SQLException e) {
            e.printStackTrace();
            return schedule;
        }
    }

    // @Override
    // public List<PortfolioReview> findReviewByUid(String uid) {
    // List<PortfolioReview> review = null;
    // try {
    // review = portfolioMapper.findReviewByUid(uid);
    // // 찾지 못한 경우
    // if (review == null) {
    // review = new ArrayList<>();
    // }
    // return review;
    // } catch (SQLException e) {
    // e.printStackTrace();
    // return review;
    // }
    // }

    @Override
    public int portfolioInitSave(String uid) {
        try {
            int res = portfolioMapper.portfolioInitSave(uid);
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int videoSave(PortfolioVideoSaveRequest portfolioVideoSaveRequest) {
        try {
            int res = portfolioMapper.videoSave(portfolioVideoSaveRequest);
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int tagSave(PortfolioTagSaveRequest portfolioTagSaveRequest) {
        try {
            // PortfolioTagSaveRequest pfts = null;
            // int uid = portfolioTagSaveRequest.getPortfolioUid();
            // StringTokenizer st = new
            // StringTokenizer(portfolioTagSaveRequest.getTagName().trim(), "#");

            // int res = 0;
            // while (st.hasMoreTokens()) {
            // pfts = new PortfolioTagSaveRequest(uid, st.nextToken().trim());
            // res = portfolioMapper.tagSave(pfts);
            // if (res < 0)
            // break;
            // }
            int res = portfolioMapper.tagSave(portfolioTagSaveRequest);
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int scheduleSave(PortfolioScheduleSaveRequest scheduleSaveRequest) {
        try {
            int res = portfolioMapper.scheduleSave(scheduleSaveRequest);
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int deleteVideos(PortfolioVideoDeleteRequest portfolioVideoDeleteRequest) {
        try {
            int res = portfolioMapper.deleteVideos(portfolioVideoDeleteRequest);
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int deleteSchedule(PortfolioScheduleDeleteRequest portfolioScheduleDeleteRequest) {
        try {
            int res = portfolioMapper.deleteSchedule(portfolioScheduleDeleteRequest);
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int portfolioUpdate(PortfolioInfo portfolioInfo) {
        try {
            int res = portfolioMapper.portfolioUpdate(portfolioInfo);
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int deleteTags(String uid) {
        try {
            int res = portfolioMapper.deleteTags(Integer.parseInt(uid));
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int portfolioNicknameUpdate(PortfolioNicknameUpdateRequest portfolioNicknameUpdateRequest) {
        try {
            int res = portfolioMapper.portfolioNicknameUpdate(portfolioNicknameUpdateRequest);
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}