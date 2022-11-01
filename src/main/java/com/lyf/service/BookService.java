package com.lyf.service;

import com.github.pagehelper.Page;
import com.lyf.domain.Book;
import com.lyf.domain.User;
import entity.PageResult;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface BookService {
    PageResult selectNewBooks(Integer pageNum, Integer pageSize);

    Book findById(String id);

    Integer borrowBook(Book book);

    PageResult search(Book book, Integer pageNum, Integer pageSize);

    Integer addBook(Book book);

    Integer editBook(Book book);

    PageResult<Book> searchBorrowed(Book book, User user, Integer pageNum, Integer pageSize);

    boolean returnBook(String id, User user);

    Integer returnConfirm(String id);
}
