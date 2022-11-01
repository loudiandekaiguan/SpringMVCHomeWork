package com.lyf.service;

import com.github.pagehelper.Page;
import com.lyf.domain.Record;
import com.lyf.domain.User;
import entity.PageResult;

public interface RecordService {
    Integer addRecord(Record record);

    PageResult searchRecords(Record record, User user, Integer pageNum, Integer pageSize);

}
