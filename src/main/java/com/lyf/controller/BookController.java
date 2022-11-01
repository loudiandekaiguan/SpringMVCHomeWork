package com.lyf.controller;

import com.lyf.domain.Book;
import com.lyf.domain.User;
import com.lyf.service.BookService;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @RequestMapping("/selectNewbooks")
    public ModelAndView selectNewbooks(){
        int pageNum = 1;
        int pageSize = 5;
        PageResult pageResult = bookService.selectNewBooks(pageNum, pageSize);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("books_new");
        modelAndView.addObject("pageResult", pageResult);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/findById")
    public Result<Book> findById(String id){
        try{
            Book book = bookService.findById(id);
            if(book == null){
                return new Result<>(false, "查询失败");
            }
            return new Result<>(true, "查询成功", book);
        }catch (Exception e){
            System.out.println("err");
            return new Result<>(false, "查询失败");
        }
    }

    @ResponseBody
    @RequestMapping("/borrowBook")
    public Result borrowBook(Book book, HttpSession session){
        String pname = ((User) session.getAttribute("USER_SESSION")).getName();
        book.setBorrower(pname);
        System.out.println(book);
        try{
            Integer count = bookService.borrowBook(book);
            if(count != 1){
                System.out.println("no");
                System.out.println(count);
                return new Result(false, "借阅图书失败");
            }
            System.out.println("ok");
            return new Result(true, "借阅图书成功，请到行政中心领书");
        }catch (Exception e){
            System.out.println("------------");
            e.printStackTrace();
            return new Result(false, "借阅图书失败");
        }
    }

    @RequestMapping("/search")
    public ModelAndView search(Book book, Integer pageNum, Integer pageSize, HttpServletRequest request){
        if(pageNum == null){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 10;
        }

        PageResult pageResult = bookService.search(book, pageNum, pageSize);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("books");
        modelAndView.addObject("pageResult", pageResult);
        modelAndView.addObject("search", book);
        modelAndView.addObject("pageNum", pageNum);
        modelAndView.addObject("gourl", request.getRequestURI());
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/addBook")
    public Result<Object> addBook(Book book){
        try{
            Integer integer = bookService.addBook(book);
            if(integer != 1){
                return new Result<>(false, "新增图书失败");
            }else {
                return new Result<>(true, "新增图书成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result<>(false, "新增图书失败");
        }
    }

    @ResponseBody
    @RequestMapping("/editBook")
    public Result<Object> editBook(Book book){
        try{
            Integer integer = bookService.editBook(book);
            if(integer != 1){
                return new Result<>(false, "编辑图书失败");
            }else {
                return new Result<>(true, "编辑图书成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result<>(false, "编辑图书失败");
        }
    }

    @ResponseBody
    @RequestMapping("/searchBorrowed")
    public ModelAndView searchBorrowed(Book book, Integer pageNum, Integer pageSize, HttpServletRequest request){
        if(pageNum == null){
            pageNum = 1;
        }
        if(pageSize ==null){
            pageSize = 10;
        }
        User user = (User)request.getSession().getAttribute("USER_SESSION");
        PageResult<Book> bookPageResult = bookService.searchBorrowed(book, user, pageNum, pageSize);
        ModelAndView modelAndView = new ModelAndView("book_borrowed");
        modelAndView.addObject("pageResult", bookPageResult);
        modelAndView.addObject("search", book);
        modelAndView.addObject("pageNum", pageNum);
        modelAndView.addObject("gourl", request.getRequestURI());
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/returnBook")
    public Result returnBook(String id, HttpSession session){
        User user = (User) session.getAttribute("USER_SESSION");
        try{
            boolean b = bookService.returnBook(id, user);
            if(!b){
                return new Result(false, "还书失败");
            }
            return new Result(true, "还书申请成功，请到行政中心还书");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, "还书失败");
        }
    }

    @ResponseBody
    @RequestMapping("/returnConfirm")
    public Result returnConfirm(String id){
        try{
            Integer integer = bookService.returnConfirm(id);
            if(integer != 1){
                return new Result(false, "确认失败");
            }
            return new Result(true, "确认成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, "确认失败");
        }

    }
}
