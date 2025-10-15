package com.farmtastic.qa.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.farmtastic.qa.model.Qa;
import com.farmtastic.qa.model.QaService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/qa")
public class QaController {

    @Autowired
    private QaService qaService;
    
    /**
     * 檢查使用者是否登入的輔助方法。
     * @param session HttpSession
     * @return 如果未登入則返回 true
     */
    private boolean isNotLoggedIn(HttpSession session) {
        return session.getAttribute("LOGGED_IN_ADMIN") == null;
    }

   
    @GetMapping("/list")
    public String listQas(Model model, HttpSession session) {
    	
        if (isNotLoggedIn(session)) return "redirect:/admin/login";
    	
        model.addAttribute("qaItems", qaService.findAll());
        return "back_end/logined/admin/qa/qaList"; // Return templates/qa-list.html
    }


    @GetMapping("/new")
    public String showAddForm(Model model, HttpSession session) {
        // 安全檢查
        if (isNotLoggedIn(session)) return "redirect:/admin/login";
    	
        model.addAttribute("qaItem", new Qa());
        model.addAttribute("pageTitle", "新增QA");
        return "back_end/logined/admin/qa/qaForm"; // Return templates/qa-form.html
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, HttpSession session) {
        // 安全檢查
        if (isNotLoggedIn(session)) return "redirect:/admin/login";
    	
        Qa qaItem = qaService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid QA Id:" + id));
        model.addAttribute("qaItem", qaItem);
        model.addAttribute("pageTitle", "修改QA");
        return "back_end/logined/admin/qa/qaForm"; // Re-use the same form
    }


    @PostMapping("/save")
    public String saveQa(@ModelAttribute("qaItem") Qa qa, HttpSession session) {
        // 安全檢查
        if (isNotLoggedIn(session)) return "redirect:/admin/login";
    	
        qaService.save(qa);
        return "redirect:/qa/list"; // Redirect back to the list page
    }
    

//    @GetMapping("/view/{id}")
//    public String viewQa(@PathVariable("id") Integer id, Model model) {
//        Qa qaItem = qaService.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid QA Id:" + id));
//        model.addAttribute("qaItem", qaItem);
//        return "qa-detail"; // Return templates/qa-detail.html
//    }

    
    @GetMapping("/delete/{id}")
    public String deleteQa(@PathVariable("id") Integer id, HttpSession session) {
        // 安全檢查
        if (isNotLoggedIn(session)) return "redirect:/admin/login";
    	
        qaService.deleteById(id);
        return "redirect:/qa/list";
    }
    
    
    //=======不用檢查========
    
    @GetMapping("/view")
    public String showFaqPage(Model model) {
        // 從 service 獲取所有 QA 資料
        model.addAttribute("qaItems", qaService.findAll());
        // 返回 templates/qa-accordion.html
        return "front_end/customer/unlogined/qa/qapage";
    }
    
    @GetMapping("/api/all")
    @ResponseBody // 加上此註解，Spring Boot 會自動將回傳的 List 轉換為 JSON 格式
    public List<Qa> getAllQasApi() {
        return qaService.findAll();
    }
    
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Qa> getQaById(@PathVariable("id") Integer id) {
        return qaService.findById(id)
                .map(ResponseEntity::ok) // 如果找到，回傳 200 OK
                .orElse(ResponseEntity.notFound().build()); // 沒找到，回傳 404
    }
}