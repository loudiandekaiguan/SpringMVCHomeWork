package com.lyf.controller;

import com.lyf.domain.Record;
import com.lyf.domain.User;
import com.lyf.service.RecordService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/record")
@Controller
public class RecordController {

    @Autowired
    private RecordService recordService;

    @RequestMapping("/searchRecords")
    private ModelAndView searchRecords(Record record, HttpServletRequest request, Integer pageNum, Integer pageSize){
        if(pageNum == null){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 10;
        }

        User user = (User) request.getSession().getAttribute("USER_SESSION");
        PageResult pageResult = recordService.searchRecords(record, user, pageNum, pageSize);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("record");
        modelAndView.addObject("pageResult", pageResult);
        modelAndView.addObject("search", record);
        modelAndView.addObject("pageNum", pageNum);
        modelAndView.addObject("gourl", request.getRequestURI());
        return modelAndView;
    }
}
