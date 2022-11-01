package com.lyf.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lyf.dao.RecordDao;
import com.lyf.domain.Record;
import com.lyf.domain.User;
import com.lyf.service.RecordService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    private RecordDao recordDao;


    @Override
    public Integer addRecord(Record record) {
        return recordDao.addRecord(record);
    }

    @Override
    public PageResult searchRecords(Record record, User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if(!"ADMIN".equals(user.getRole())){
            record.setBorrower(user.getName());
        }
        Page<Record> records = recordDao.searchRecords(record);
        return new PageResult(records.getTotal(), records.getResult());
    }
}
