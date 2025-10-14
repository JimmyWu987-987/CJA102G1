package com.farmtastic.act.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.farmtastic.act.model.Act;
import com.farmtastic.act.model.ActCate;
import com.farmtastic.act.model.ActImg;
import com.farmtastic.act.model.ActRepository;
import com.farmtastic.act.model.ActService;
import com.farmtastic.fmember.model.FmemService;
import com.farmtastic.ses.model.Ses;
import com.farmtastic.ses.model.SesService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@Validated
@RequestMapping("/fmem/act")
@SessionAttributes({"sessionAct"})
public class ActController {

    @Autowired
    private ActService actSvc;

    @Autowired
    private FmemService fmemSvc;
    
    @Autowired
    private SesService sesSvc;
    
    @Autowired
    private ActRepository actRepo;

	// =========== 消費者查詢活動 ============
    
    // 查已上架的所有活動
    @GetMapping("/listAllAct/{fmemId}")
    public String listAllByFmem(@PathVariable Integer fmemId, ModelMap model) {
        
        List<Act> actList = actSvc.findByFmemId(fmemId, Sort.by(Sort.Direction.ASC, "actId"));
        model.addAttribute("actList", actList);
        return "front_end/farmer/logined/fmemAct/listAllAct";
    }
    
    
    // 複合查詢
    
    
    

	// =========== 抓圖 ============

	// get抓主圖
    @GetMapping("/mainImg/{actId}")
    public void getActMainImg(@PathVariable Integer actId, HttpServletResponse response) {
        Act act = actSvc.getOneAct(actId);
        byte[] actMainImg = act.getActMainImg();
        writeImageToResponse(actMainImg, response);
    }

    // get其他活動圖
    @GetMapping("/img/{actId}/{imgId}")
    public void getActImg(@PathVariable Integer actId,
                          @PathVariable Integer imgId,
                          HttpServletResponse response) {
        Act act = actSvc.getOneAct(actId);

        if (act.getActImg() == null || act.getActImg().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        ActImg img = act.getActImg().stream()
                         .filter(i -> i.getActImgId().equals(imgId))
                         .findFirst()
                         .orElse(null);

        if (img == null || img.getActImg() == null || img.getActImg().length == 0) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        writeImageToResponse(img.getActImg(), response);
    }

    private void writeImageToResponse(byte[] imgBytes, HttpServletResponse response) {
        try {
            String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imgBytes));
            if (contentType == null) contentType = "image/jpeg"; // 預設 jpeg

            response.setContentType(contentType);
            response.getOutputStream().write(imgBytes);
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
    
    
    // ============ 活動詳細頁 (for 消費者) ============
    @GetMapping("/detail/{actId}")
    public String actDetail(@PathVariable Integer actId, ModelMap model) {
        Act act = actSvc.getOneAct(actId);		// 取得活動
                
        // 依分類ID排序
        List<ActCate> sortedCate = new ArrayList<>(act.getActCate());
        sortedCate.sort(Comparator.comparing(ActCate::getActCateId));
        model.addAttribute("actCateList", sortedCate);

        // 先依場次日期在依場次時間升冪排序
        Sort sort = Sort.by(Sort.Direction.ASC, "sesDate")
        				.and(Sort.by(Sort.Direction.ASC, "sesStart"));
        
        List<Ses> allSes = sesSvc.findByActId(actId, sort);
        List<Ses> launchedSes = allSes.stream()
            .filter(s -> s.getSesLaunStat() != null && s.getSesLaunStat().equals(1))
            .toList();
        model.addAttribute("sesList", launchedSes);
        model.addAttribute("act", act);
        model.addAttribute("sessionAct", act);
        
        return "front_end/customer/unlogined/actDetails/actDetails";
        
    }
    
    // ============ 輪播器圖片 ============
    @GetMapping("/carousel/{actId}")
    @ResponseBody
    public byte[][] getCarouselImages(@PathVariable Integer actId) {
        List<byte[]> imgs = actSvc.getAllActImagesForCarousel(actId);
        return imgs.toArray(new byte[0][]);
    }

    
}