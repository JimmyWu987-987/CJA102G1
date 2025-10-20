package com.farmtastic.news.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class NewsService {

	@Autowired
	private NewsRepository newsRepository;

    // 取得所有最新消息
    public List<News> getAllNews() {
        return newsRepository.findAll(Sort.by(Sort.Direction.DESC, "newsAt"));
    }

    // 根據 ID 取得單一消息
    public News getNewsById(long id) {
        Optional<News> optional = newsRepository.findById(id);
        News news = null;
        if (optional.isPresent()) {
            news = optional.get();
        } else {
            throw new RuntimeException("找不到 ID 為 " + id + " 的消息");
        }
        return news;
    }

    // 儲存或更新消息
    public void saveNews(News news) {
        if (news.getNewsAt() == null) {
            // 如果是新消息，設定發布時間為現在
            news.setNewsAt(LocalDateTime.now());
        }
        this.newsRepository.save(news);
    }

    // 根據 ID 刪除消息
    public void deleteNewsById(long id) {
        this.newsRepository.deleteById(id);
    }
    
    public List<News> getNewsByStatus(Integer status) {
        return newsRepository.findByNewsStatusOrderByNewsAtDesc(status);
    }
    
    public List<News> getNewsForFarmer(Integer fmemId) {
        return newsRepository.findNewsForFarmer(fmemId, Sort.by(Sort.Direction.DESC, "newsAt"));
    }
}
