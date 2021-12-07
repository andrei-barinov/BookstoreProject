package com.bookstore.admin.pagin;

import com.bookstore.admin.service.UserService;
import com.bookstore.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

public class PagingAndSortingHelper {
    private ModelAndViewContainer model;
    private String moduleURL;
    private String listName;

    public PagingAndSortingHelper(ModelAndViewContainer model, String moduleURL, String listName) {
        this.model = model;
        this.moduleURL = moduleURL;
        this.listName = listName;
    }

    public void updateModelAttributes(int pageNum, Page<?> page){
        List<?> listItems = page.getContent();
        int pageSize = page.getSize();

        long startCount = (pageNum - 1) * pageSize + 1;
        long endCount = startCount + pageSize - 1;

        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute(listName, listItems);
        model.addAttribute("moduleURL", moduleURL);
    }
}
