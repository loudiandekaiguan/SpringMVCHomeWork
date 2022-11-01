package com.lyf.dao;

import com.github.pagehelper.Page;
import com.lyf.domain.Book;
import com.lyf.domain.User;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface BookDao {
    @Results(id = "bookMap",
            value = {
                    @Result(id = true,column = "book_id",property = "id"),
                    @Result(column = "book_name",property = "name"),
                    @Result(column = "book_isbn",property = "isbn"),
                    @Result(column = "book_press",property = "press"),
                    @Result(column = "book_author",property = "author"),
                    @Result(column = "book_pagination",property = "pagination"),
                    @Result(column = "book_price",property = "price"),
                    @Result(column = "book_uploadtime",property = "uploadTime"),
                    @Result(column = "book_status",property = "status"),
                    @Result(column = "book_borrower",property = "borrower"),
                    @Result(column = "book_borrowtime",property = "borrowTime"),
                    @Result(column = "book_returntime",property = "returnTime")
            }
    )
    @Select("select * from book where book_status != '3' order by book_uploadtime desc")
    Page<Book> selectNewBooks();

    @ResultMap("bookMap")
    @Select("select * from book where book_id = #{id}")
    Book findById(String id);

    Integer editBook(Book book);


    @ResultMap("bookMap")
    @Select({
            "<script>\n" +
            "    select * from book\n" +
            "    where book_status != '3'\n" +
            "    <if test=\"name != null\">\n" +
            "        and book_name like concat('%', #{name}, '%')\n" +
            "    </if>\n" +
            "    <if test=\"press != null\">\n" +
            "        and book_press like concat('%', #{name}, '%')\n" +
            "    </if>\n" +
            "    <if test=\"author != null\">\n" +
            "        and book_author like concat('%', #{name}, '%')\n" +
            "    </if>\n" +
            "    order by book_status\n" +
            "</script>"
    })
    Page<Book> searchBooks(Book book);

    Integer addBook(Book book);

    @Select(
            {"<script>" +
                    "SELECT * FROM book " +
                    "where book_borrower=#{borrower}" +
                    "AND book_status ='1'"+
                    "<if test=\"name != null\"> AND  book_name  like  CONCAT('%',#{name},'%')</if>" +
                    "<if test=\"press != null\"> AND book_press like  CONCAT('%', #{press},'%') </if>" +
                    "<if test=\"author != null\"> AND book_author like  CONCAT('%', #{author},'%')</if>" +
                    "or book_status ='2'"+
                    "<if test=\"name != null\"> AND  book_name  like  CONCAT('%',#{name},'%')</if>" +
                    "<if test=\"press != null\"> AND book_press like  CONCAT('%', #{press},'%') </if>" +
                    "<if test=\"author != null\"> AND book_author like  CONCAT('%', #{author},'%')</if>" +
                    "order by book_borrowtime" +
                    "</script>"})
    @ResultMap("bookMap")
    Page<Book> selectBorrowed(Book book);

    @Select({"<script>"  +
            "SELECT * FROM book " +
            "where book_borrower=#{borrower}" +
            "AND book_status in('1','2')"+
            "<if test=\"name != null\"> AND  book_name  like  CONCAT('%',#{name},'%')</if>" +
            "<if test=\"press != null\"> AND book_press like  CONCAT('%', #{press},'%') </if>" +
            "<if test=\"author != null\"> AND book_author like  CONCAT('%', #{author},'%')</if>" +
            "order by book_borrowtime" +
            "</script>"})
    @ResultMap("bookMap")
    Page<Book> selectMyBorrowed(Book book);

}
