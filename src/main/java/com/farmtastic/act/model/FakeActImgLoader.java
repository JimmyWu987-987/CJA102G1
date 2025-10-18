package com.farmtastic.act.model;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//@Component  // 先不要用這個檔案
public class FakeActImgLoader implements CommandLineRunner {

    @Autowired
    private ActRepository actRepo;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        String basePath = "src/main/resources/static/images/FakeActImg/";

//		!!!!!!!!      sql 要把所有 actimg 的空資料建起來    !!!   
	Map<Integer, String[]> actImgMap = Map.of(
		1, new String[]{"fake1-1.jpg", "fake1-2.jpg", "fake1-3.jpg"},
		2, new String[]{},
		3, new String[]{"fake3-1.jpg", "fake3-2.jpg"},
		4, new String[]{"fake4-1.jpg"},
		5, new String[]{"fake5-1.jpg", "fake5-2.jpg", "fake5-3.jpg", "fake5-4.jpg", "fake5-5.jpg"}
        );
	
	
	
	Map<Integer, String> mainImgMap = Map.of(
            1, "fakeMain1.jpg", 2, "fakeMain2.jpg", 3, "fakeMain3.jpg",
            4, "fakeMain4.jpg", 5, "fakeMain5.jpg"
            );
	
	for (Integer actId : mainImgMap.keySet()) {

        Act act = actRepo.findById(actId)
                         .orElseThrow(() -> new RuntimeException("找不到活動 ID = " + actId));

        // 主圖
        String mainFile = mainImgMap.get(actId);
        act.setActMainImg(Files.readAllBytes(Paths.get(basePath + mainFile)));

        // 活動圖
        List<ActImg> actImgList = act.getActImg();
        String[] files = actImgMap.getOrDefault(actId, new String[]{});
        for (int i = 0; i < files.length && i < actImgList.size(); i++) {
            byte[] imgBytes = Files.readAllBytes(Paths.get(basePath + files[i]));
            actImgList.get(i).setActImg(imgBytes);
        }

        actRepo.save(act);
    }
}
}