package com.bookstore.admin.controller;

import com.bookstore.common.exception.CustomerNotFoundException;
import com.bookstore.admin.util.pagination.PagingAndSortingHelper;
import com.bookstore.admin.util.pagination.PagingAndSortingParam;
import com.bookstore.admin.repository.StateRepository;
import com.bookstore.admin.service.CustomerService;
import com.bookstore.common.entity.Country;
import com.bookstore.common.entity.Customer;
import com.bookstore.common.entity.State;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final StateRepository stateRepository;

    @GetMapping("/customers")
    public String listFirstPage(){
        return "redirect:/customers/page/1?sortField=firstName&sortDir=asc";
    }

    @GetMapping("/customers/page/{pageNum}")
    public String listByPage(
            @PagingAndSortingParam(listName = "listCustomers", moduleURL = "/customers") PagingAndSortingHelper helper,
                             @PathVariable(name = "pageNum") int pageNum, Model model
    ){
        customerService.listByPage(pageNum, helper);
        return "customers/customers";
    }

    @GetMapping("/customers/{id}/enabled/{status}")
    public String updateCustomerEnableStatus(@PathVariable("id") Integer id,
                                             @PathVariable("status") boolean enabled,
                                             RedirectAttributes redirectAttributes){
        customerService.updateCustomerEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The Customer ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/customers";
    }

    @GetMapping("/customers/detail/{id}")
    public String viewCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra){
        try{
            Customer customer = customerService.get(id);
            model.addAttribute("customer", customer);

            return "customers/customer_detail_modal";
        }catch (CustomerNotFoundException ex){
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/customers";
        }
    }

    @GetMapping("/customers/edit/{id}")
    public String editCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra){
        try{
            Customer customer = customerService.get(id);
            List<Country> countries = customerService.listAllCountries();
            List<State> states = stateRepository.findByCountryOrderByNameAsc(customer.getCountry());

            model.addAttribute("listStates", states);
            model.addAttribute("listCountries", countries);
            model.addAttribute("customer", customer);
            model.addAttribute("pageTitle", String.format("Edit Customer (ID: %d)", id));

            return "customers/customer_form";
        } catch (CustomerNotFoundException ex){
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/customers";
        }
    }

    @PostMapping("/customers/save")
    public String saveCustomer(Customer customer, Model model, RedirectAttributes ra){
        customerService.save(customer);
        ra.addFlashAttribute("message", "The customer ID " + customer.getId() + " has been update successfully");
        return "redirect:/customers";
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable Integer id, RedirectAttributes ra){
        try{
            customerService.delete(id);
            ra.addFlashAttribute("message", "The customer ID " + id + " has been update successfully");
        } catch (CustomerNotFoundException ex){
            ra.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/customers";
    }

}
