package com.lyf.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyf.dao.BookDao;
import com.lyf.domain.Book;
import com.lyf.domain.Record;
import com.lyf.domain.User;
import com.lyf.service.BookService;
import com.lyf.service.RecordService;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    BookDao bookDao;
    @Autowired
    RecordService recordSer;

    @Override
    public PageResult<Book> selectNewBooks(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Book> page = bookDao.selectNewBooks();
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    public Book findById(String id) {
        return bookDao.findById(id);
    }

    @Override
    public Integer borrowBook(Book book) {
        Book bById = this.findById(book.getId().toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        book.setBorrowTime(simpleDateFormat.format(new Date()));
        book.setStatus("1");
        book.setPrice(bById.getPrice());
        book.setUploadTime(bById.getUploadTime());
        return bookDao.editBook(book);
    }

    @Override
    public PageResult search(Book book, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Book> page = bookDao.searchBooks(book);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public Integer addBook(Book book) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        book.setUploadTime(simpleDateFormat.format(new Date()));
        return bookDao.addBook(book);
    }

    @Override
    public Integer editBook(Book book) {
        return bookDao.editBook(book);
    }

    @Override
    public PageResult searchBorrowed(Book book, User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Book> page;
        book.setBorrower(user.getName());
        if("ADMIN".equals(user.getRole())){
            page = bookDao.selectBorrowed(book);
        }else {
            page = bookDao.selectMyBorrowed(book);
        }
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public boolean returnBook(String id, User user) {
        Book book = this.findById(id);
        boolean equals = book.getBorrower().equals(user.getName());
        if(equals){
            book.setStatus("2");
            bookDao.editBook(book);
        }
        return equals;
    }

    @Override
    public Integer returnConfirm(String id) {
        Book book = this.findById(id);
        Record record = this.setRecord(book);
        book.setStatus("0");
        book.setBorrower("");
        book.setBorrowTime("");
        book.setReturnTime("");
        Integer count = bookDao.editBook(book);
        if(count == 1){
            return recordSer.addRecord(record);
        }
        return 0;
    }

    private Record setRecord(Book book){
        Record record = new Record();
        record.setBookname(book.getName());
        record.setBookisbn(book.getIsbn());
        record.setBorrower(book.getBorrower());
        record.setBorrowTime(book.getBorrowTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date());
        record.setRemandTime(format);
        return record;
    }
}
