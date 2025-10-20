package com.farmtastic.news.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.fmember.model.FmemService;
import com.farmtastic.news.model.News;
import com.farmtastic.news.model.NewsService;


@Controller
public class Newscontroller {

	@Autowired
	private NewsService newsService;
	
	@Autowired
	private FmemService fmemService;
	
	
	//==========================================
//    * 顯示發送訊息給小農的表單頁面
//    * @param model 用於將空的 News 物件和所有小農列表綁定到表單
//    * @return 發送訊息表單的模板名稱
  
    
   @GetMapping("/showSendMessageForm")
   public String showSendMessageForm(Model model) {
       News news = new News();
       // 從 FarmerService 取得所有小農的列表
       List<Fmem> allFarmers = fmemService.getAll();

       model.addAttribute("news", news);
       // 將小農列表傳遞給前端
       model.addAttribute("allFarmers", allFarmers);
       return "back_end/logined/admin/news/message_to_farmer";
   }

   /**
    * 儲存發送給小農的訊息 (此方法不需修改，Spring 會自動綁定 farmer)
    * @param news 從表單綁定的 News 物件 (包含指定的 farmer)
    * @return 重定向到消息列表頁面
    */
   @PostMapping("/sendMessageToFarmer")
   public String sendMessageToFarmer(@ModelAttribute("news") News news) {
       // 自動將狀態設定為 0 (小農)
       news.setNewsStatus(0);
       newsService.saveNews(news);
       return "redirect:/news";
   }
	
   	//=============================================
	/**
	 * 顯示所有最新消息列表
	 * 
	 * @param model 用於將資料傳遞給視圖
	 * @return 消息列表頁面的模板名稱
	 */
	@GetMapping("/news")
	public String viewHomePage(Model model) {
		model.addAttribute("listNews", newsService.getAllNews());
		return "back_end/logined/admin/news/news_list";
	}

	/**
	 * 顯示新增消息的表單頁面
	 * 
	 * @param model 用於將空的 News 物件綁定到表單
	 * @return 新增消息表單的模板名稱
	 */
	@GetMapping("/newnewsform")
	public String newNewsForm(Model model) {
		News news = new News();
		model.addAttribute("news", news);
		return "add_news";
	}

	/**
	 * 儲存新的消息
	 * 
	 * @param News 從表單綁定的 News 物件
	 * @return 重定向到首頁
	 */
	@PostMapping("/savenews")
	public String saveNews(@ModelAttribute("news") News news) {
		newsService.saveNews(news);
		return "redirect:/news";
	}

	/**
	 * 顯示修改消息的表單頁面
	 * 
	 * @param id    要修改的消息 ID
	 * @param model 用於將找到的消息資料傳遞給視圖
	 * @return 修改消息表單的模板名稱
	 */
	@GetMapping("/editnews/{id}")
	public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {
		// 從 service 取得 News
		News news = newsService.getNewsById(id);
		// 將 News 設定為 model attribute 來預填表單
		model.addAttribute("news", news);
		return "back_end/logined/admin/news/edit_news";
	}

	/**
	 * 更新消息
	 * 
	 * @param id   要更新的消息 ID
	 * @param News 從表單綁定的 News 物件
	 * @return 重定向到首頁
	 */
	@PostMapping("/updatenews/{id}")
	public String updateNews(@PathVariable("id") long id, @ModelAttribute("News") News news) {
		News existingNews = newsService.getNewsById(id);
		existingNews.setNewsTitle(news.getNewsTitle());
		existingNews.setNewsContent(news.getNewsContent());
        existingNews.setNewsStatus(news.getNewsStatus()); 
		existingNews.setNewsAt(LocalDateTime.now()); // 更新發布時間為現在
		newsService.saveNews(existingNews);
		return "redirect:/news";
	}

	/**
	 * 刪除消息
	 * 
	 * @param id 要刪除的消息 ID
	 * @return 重定向到首頁
	 */
	@GetMapping("/deletenews/{id}")
	public String deleteNews(@PathVariable(value = "id") long id) {
		this.newsService.deleteNewsById(id);
		return "redirect:/news";
	}
	
    @GetMapping("/fmem/forfnews")
    public String showFarmerNews(Model model) {
        // 狀態 0 代表「小農」
        model.addAttribute("farmerNewsList", newsService.getNewsByStatus(0));
        return "front_end/farmer/logined/fmemnews/for_f_news";
    }
    
    @GetMapping("/forcusnews")
    public String showCusNews(Model model) {
        // 狀態 1 代表「消費者」
        model.addAttribute("cusNewsList", newsService.getNewsByStatus(1));
        return "front_end/customer/unlogined/news/for_c_news";
    }
    
    //文章
    @GetMapping("/news/{id}")
    public String showNewsDetail(@PathVariable("id") long id, Model model) {
        News news = newsService.getNewsById(id);
        model.addAttribute("news", news);
        return "front_end/customer/unlogined/news/newsdetail";
    }
    
    @GetMapping("/fmem/news/{id}")
    public String showFmemNewsDetail(@PathVariable("id") long id, Model model) {
        News news = newsService.getNewsById(id);
        model.addAttribute("news", news);
        return "front_end/farmer/logined//fmemnews/fmemNewsDetail";
    }
}
