package com.farmtastic.twmap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.twmap.model.TwMapService;

@Controller
@RequestMapping("/twMap")
public class TwMapController {

	@Autowired
    private TwMapService twMapService;
	
	
	// GET /twMap?city=台南市   （沒給就用台南市當預設）
	    @GetMapping("")
	    public String showTwMap(
	            @RequestParam(name = "city", defaultValue = "") String city,
	            Model model) {

	        model.addAttribute("city", city);
	        model.addAttribute("farmers", twMapService.findByCity(city));
	        return "front_end/customer/unlogined/twMap"; 
	    }
	 
	 // 頁面上的 th:fragment="farmersList"
	    @GetMapping("/farmersFragment")
	    public String farmersFragment(@RequestParam String city, Model model) {
	        model.addAttribute("farmers", twMapService.findByCity(city));
	        return "front_end/customer/unlogined/twMap :: farmersList"; 
	    }
	 
	    
	    
	    @GetMapping(value = "/img/{id}", produces = MediaType.IMAGE_GIF_VALUE)
	    @ResponseBody
	    public byte[] fmemImage(@PathVariable Integer id) {
	        Fmem f = twMapService.findOne(id);
	        if (f == null || f.getStorePic() == null) {
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	        }
	        return f.getStorePic();
	    }
	    
	    
	    
	    
}
