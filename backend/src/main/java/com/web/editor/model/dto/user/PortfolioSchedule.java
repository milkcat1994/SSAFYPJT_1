package com.web.editor.model.dto.user;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Repository
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortfolioSchedule {
    private int scheduleNo;
    private int portfolioUid;
    private Date startDate;
    private Date endDate;
    private int scheduleType;
}