CREATE DATABASE IF NOT EXISTS farmtastic;
USE farmtastic;


-- 總共 31 個 Table

-- step 1. 

-- (1-1) 商店樣式 -> 小農會員（FK商品樣式編號）

-- (1-2) 小農商品類別 -> 小農商品（FK商品類別編號）（FK小農會員編號）-> 商品圖片（FK商品編號）
-- ps.「商城訂單」建立好才建「商城訂單明細」

-- (1-3) 商城廣告（FK商品編號）（FK小農會員編號）
-- ps.「一般會員」建立好才建「購物車」、「商品評論」、「檢舉表單」

-- (1-4) 活動分類 -> 活動（FK小農會員編號）-> 活動分類明細（FK活動編號）（FK分類編號）

-- (1-5) 活動照片（FK活動編號）-> 活動廣告（FK活動編號）（FK小農會員編號）

-- (1-6) 場次（FK小農會員編號）
-- ps.「一般會員」、「活動折價卷持有者明細」建立好才建「報名訂單」





-- 刪除/建立 商店樣式
DROP TABLE IF EXISTS sty;
CREATE TABLE sty (
	sty_no tinyint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    sty_css_path varchar(300) NOT NULL,
    sty_pic longblob DEFAULT NULL
);

INSERT INTO sty (sty_css_path) VALUES
('/css/sty/farmerStore1ColorNature.css'), 
('/css/sty/farmerStore1ColorWarm.css'), 
('/css/sty/farmerStore1ColorCold.css');

-- 刪除/建立 小農會員
DROP TABLE IF EXISTS fmem;
CREATE TABLE fmem (
	fmem_id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
	f_id varchar(10) NOT NULL UNIQUE,
	fmem_acc varchar(40) NOT NULL UNIQUE,
	fmem_pwd varchar(20) NOT NULL,
	acc_status tinyint NOT NULL DEFAULT 0,
	acc_desc varchar(200) DEFAULT NULL,
	fmem_name varchar(20) NOT NULL,
	fmem_mobile varchar(11) NOT NULL UNIQUE,
	fmem_tel varchar(11) DEFAULT NULL,
	fmem_email varchar(254) NOT NULL,
	fmem_zipcode varchar(6) NOT NULL,
	fmem_city varchar(5) NOT NULL,
	fmem_dist varchar(5) NOT NULL,
	fmem_addr varchar(40) NOT NULL,
	bank_code varchar(5) NOT NULL,
	bank_acc varchar(20) NOT NULL,
	reg_date datetime NOT NULL DEFAULT current_timestamp,
	certi_status tinyint NOT NULL DEFAULT 0,
	fmem_pic longblob DEFAULT NULL,
	organic_pic longblob DEFAULT NULL,
	land_pic longblob DEFAULT NULL,
	insur_pic longblob DEFAULT NULL,
	store_pic longblob DEFAULT NULL,
	store_name varchar(50) DEFAULT NULL,
	store_intro varchar(500) DEFAULT NULL,
	sty_no tinyint NOT NULL DEFAULT 1,
	mkt_score int DEFAULT NULL,
	mkt_cnt int DEFAULT NULL,
	act_score int DEFAULT NULL,
	act_cnt int DEFAULT NULL,
	rpt_cnt tinyint DEFAULT NULL,
	prod_fee int DEFAULT NULL,
    fail_attempts int NOT NULL DEFAULT 0,
	lock_time datetime DEFAULT NULL
);

INSERT INTO fmem (f_id, fmem_acc, fmem_pwd, acc_status, acc_desc, fmem_name, fmem_mobile, fmem_tel, fmem_email,
				   fmem_zipcode, fmem_city, fmem_dist, fmem_addr,  bank_code, bank_acc, reg_date, 
				   certi_status, fmem_pic, organic_pic, land_pic, insur_pic, store_pic, store_name, store_intro, sty_no, 
				   mkt_score, mkt_cnt, act_score, act_cnt, rpt_cnt, prod_fee, fail_attempts, lock_time) VALUES
('H237230756', 'test', '1234', 2, NULL, '王小明', '0912-345678', '03-29123456', 'user001@example.com', '913', '屏東縣', '萬丹鄉', '仁愛路一段100號', '004', '1234567890123456', '2024-05-01 10:00:00', 0, NULL, NULL, NULL, NULL, NULL, '有機田園', '我們致力提供無毒有機蔬菜，讓您吃得安心健康。', 1, NULL, NULL, NULL, NULL, NULL, 90, 0, NULL),
('A182893231', 'test0002', '00000000', 2, NULL, '林小美', '0922-333444', '02-29123456', 'user002@example.com', '406', '台中市', '北屯區', '文化路200號', '822', '22334456778899', '2024-05-02 11:00:00', 0, NULL, NULL, NULL, NULL, NULL, '果園市集', '果園市集堅持新鮮採摘當天配送，給您最自然的好味道。', 2, NULL, NULL, NULL, NULL, 1, 60, 0, NULL),
('A119254857', 'test0003', '00000000', 2, NULL, '陳大華', '0933-445566', '06-27889911', 'user003@example.com', '625', '嘉義縣', '布袋鎮', '市政路300號', '700', '3344556889900', '2024-05-03 12:00:00', 0, NULL, NULL, NULL, NULL, NULL, '自然之家', '以自然農法種植，拒絕農藥，打造健康生活。', 3, NULL, NULL, NULL, NULL, NULL, 90, 0, NULL),
('E118270271', 'user004', 'pwd12345', 2, NULL, '張美麗', '0955-667788', NULL, 'user004@example.com', '314', '新竹縣', '北埔鄉', '光華路88號', '012', '4455667788990011', '2024-05-04 13:00:00', 0, NULL, NULL, NULL, NULL, NULL, '自然坊', '自然坊致力於打造無添加的蔬果選擇，純粹無毒。', 1, NULL, NULL, NULL, NULL, NULL, 100, 0, NULL),
('F133927325', 'user005', 'pwd12345', 2, NULL, '吳志強', '0966-778899', '03-3522334', 'user005@example.com', '802', '高雄市', '苓雅區', '東門路199號', '005', '5578899001122', '2024-05-05 14:00:00', 0, NULL, NULL, NULL, NULL, NULL, '慧君園地', '我們販售無毒栽種的蔬果，希望您吃得安心。', 3, NULL, NULL, NULL, NULL, NULL, 30, 0, NULL),
('J179726256', 'user006', 'pwd12345', 2, NULL, '葉志豪', '0977-889900', '02-26543210', 'user006@example.com', '364', '苗栗縣', '大湖鄉', '關新路300號', '822', '6677889900112233', '2024-05-06 15:00:00', 0, NULL, NULL, NULL, NULL, NULL, '柏宏農園', '天然農法、友善土地，我們與自然共生。', 2, NULL, NULL, NULL, NULL, NULL, 60, 0, NULL),
('L158944504', 'user007', 'pwd12345', 2, NULL, '簡文君', '0911-222333', NULL, 'user007@example.com', '320', '桃園市', '中壢區', '關新路555號', '004', '7788990344', '2024-05-07 16:00:00', 0, NULL, NULL, NULL, NULL, NULL, '東螺溪休閒農場', '體驗農村生活，回歸大自然的懷抱', 1, NULL, NULL, NULL, NULL, NULL, 90, 0, NULL),
('M160270421', 'user008', 'pwd12345', 2, NULL, '朱庭瑜', '0933-777888', NULL, 'user008@example.com', '266', '宜蘭縣', '三星鄉', '中山路350號', '012', '88990011223455', '2024-05-08 17:00:00', 0, NULL, NULL, NULL, NULL, NULL, '田園日好', '位於山腳下的友善耕作小農，堅持使用天然堆肥，種植當季蔬果與特色作物，讓您品嚐到最純淨、最自然的土地滋味，體驗與自然共生的美好。', 1, NULL, NULL, NULL, NULL, NULL, 90, 0, NULL),
('N108676213', 'user009', 'pwd12345', 2, NULL, '劉家豪', '0922-111333', '03-3876543', 'user009@example.com', '701', '台南市', '安平區', '民權路68號', '700', '99001133445566', '2024-05-09 18:00:00', 0, NULL, NULL, NULL, NULL, NULL, '鄉村小鋪', '鄉村小鋪主打純天然的農產品，無添加、無毒，守護您的健康。', 2, NULL, NULL, NULL, NULL, NULL, 100, 0, NULL),
('Q193833164', 'user010', 'pwd12345', 2, NULL, '黃靜怡', '0966-111222', NULL, 'user010@example.com', '630', '雲林縣', '斗六市', '和平路88號', '005', '001122334455677', '2024-05-10 19:00:00', 0, NULL, NULL, NULL, NULL, NULL, '陽光果實屋', '專營各類有機水果，從育苗到採收皆嚴格把關，甜度高、風味佳。提供新鮮現採直送服務，讓您的餐桌充滿陽光的味道，享受健康無負擔的甜蜜。', 1, NULL, NULL, NULL, NULL, NULL, 30, 0, NULL),

('B167802934', 'user011', 'pwd12345', 2, NULL, '林冠廷', '0930-123456', NULL, 'user011@example.com', '558', '南投縣', '鹿谷鄉', '信義路五段88號', '812', '12345678912411', '2024-05-11 10:00:00', 0, NULL, NULL, NULL, NULL, NULL, '禾香米鋪', '傳承三代的手作米鋪，選用在地優良米種，以傳統工法細心碾製，米粒飽滿、口感香Q。也提供多樣米食製品，是您日常主食與送禮的好選擇。', 3, NULL, NULL, NULL, NULL, NULL, 60, 0, NULL),
('C138927483', 'user012', 'pwd12345', 2, NULL, '張語心', '0988-223344', NULL, 'user012@example.com', '515', '彰化縣', '大村鄉', '中正路123號', '700', '9988776433220', '2024-05-12 11:00:00', 0, NULL, NULL, NULL, NULL, NULL, '禾田農莊', '我們堅持自然農法，用心耕耘每一寸土地。', 2, NULL, NULL, NULL, NULL, 2, 60, 0, NULL),
('D189273645', 'user013', 'pwd12345', 2, NULL, '周子洋', '0911-556677', '06-2678990', 'user013@example.com', '973', '花蓮縣', '吉安鄉', '開元路1號', '005', '445566778890000', '2024-05-13 12:00:00', 0, NULL, NULL, NULL, NULL, NULL, '農鮮市集', '農鮮市集專營在地小農產品，讓您吃出健康與安心。', 1, NULL, NULL, NULL, NULL, NULL, 120, 0, NULL),
('E145987231', 'user014', 'pwd12345', 2, NULL, '黃柏睿', '0922-778899', '04-22334455', 'user014@example.com', '958', '台東縣', '池上鄉', '昌平路二段200號', '012', '3344577889911', '2024-05-14 13:00:00', 0, NULL, NULL, NULL, NULL, NULL, '綠野香草集', '專業栽種多種食用與藥用香草，採自然農法，不施農藥化肥。提供新鮮香草、乾燥花草茶、純露與精油等產品，為您的生活帶來療癒的芬芳。', 1, NULL, NULL, NULL, NULL, NULL, 90, 0, NULL),
('F176543219', 'user015', 'pwd12345', 2, NULL, '陳姿妤', '0900-112233', '07-5511223', 'user015@example.com', '981', '花蓮縣', '玉里鎮', '建工路188號', '822', '112455667788', '2024-05-15 14:00:00', 0, NULL, NULL, NULL, NULL, NULL, '小巷蜂蜜莊園', '養蜂人家的甜蜜事業，蜜源來自純淨山區花卉，堅持不混糖、不加工，生產高品質的龍眼蜜、百花蜜及蜂王乳。每一口都是大自然的精華。', 1, NULL, NULL, NULL, NULL, NULL, 60, 0, NULL),
('I192837465', 'user018', 'pwd12345', 2, NULL, '曾雅婷', '0933-667788', NULL, 'user018@example.com', '553', '南投縣', '水里鄉', '農田二路20號', '822', '12344321121', '2024-05-18 17:00:00', 0, NULL, NULL, NULL, NULL, NULL, '月光茶園', '專注於高山手工茶葉製作，從採摘、揉捻到烘焙，每個步驟都充滿匠人精神。茶湯清澈甘甜，香氣持久，為您帶來一份寧靜與美好。', 1, NULL, NULL, NULL, NULL, NULL, 60, 0, NULL),

('G133648294', 'user016', 'pwd12345', 3, '資料不正確', '賴政文', '0966-889977', NULL, 'user016@example.com', '114', '台北市', '內湖區', '成功路四段50號', '004', '7788990011223344', '2024-05-16 15:00:00', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL),
('H178234981', 'user017', 'pwd12345', 4, NULL, '吳書宏', '0977-112244', '05-22334455', 'user017@example.com', '600', '嘉義市', '西區', '文化路二段320號', '700', '9988776655443311', '2024-05-17 16:00:00', 0, NULL, NULL, NULL, NULL, NULL, '愛鄉有機', '愛鄉有機以守護土地為理念，提供最純粹的農產品。', 1, NULL, NULL, NULL, NULL, 4, 100, 0, NULL),
('J203948576', 'user019', 'pwd12345', 1, NULL, '簡詠恩', '0955-334455', '02-22119988', 'user019@example.com', '220', '新北市', '板橋區', '新海路66號', '005', '6900112233', '2024-05-19 18:00:00', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL),
('K209384756', 'user020', 'pwd12345', 0, NULL, '徐宏文', '0911-889900', NULL, 'user020@example.com', '701', '台南市', '東區', '中華東路一段1號', '012', '1122490011', '2024-05-20 19:00:00', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL);

-- 設定區分大小寫 (登入才會辨識大小寫)
ALTER TABLE fmem
MODIFY COLUMN fmem_acc VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;




-- 刪除/建立 小農類別商品
DROP TABLE IF EXISTS product_category;
CREATE TABLE product_category (
	PRO_CATE_ID int NOT NULL AUTO_INCREMENT,
    PRO_CATE_NAME varchar(100) NOT NULL,
    CONSTRAINT product_category_PRO_CATE_ID_KEY PRIMARY KEY (PRO_CATE_ID)
) ENGINE InnoDB;

INSERT INTO product_category (PRO_CATE_NAME) VALUES
('蔬菜'),
('水果'),
('米糧'),
('茶葉'),
('香菇'),
('蜂蜜'),
('雞蛋'),
('堅果'),
('花卉'),
('海鮮');

-- 刪除/建立 小農商品
DROP TABLE IF EXISTS product;
CREATE TABLE product (
	pro_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	pro_name VARCHAR(100) NOT NULL,
	pro_stock INT NOT NULL,
	pro_price INT NOT NULL,
	pro_status INT NOT NULL DEFAULT 0,
	pro_score INT,
	pro_cnt INT,
	pro_from VARCHAR(10),
	pro_des VARCHAR(100),
	fmem_id INT,   -- FK測試用先放 --
	pro_cate_id INT  -- FK測試用先放 --
) ENGINE InnoDB;

INSERT INTO product (pro_name, pro_stock, fmem_id, pro_price, pro_status, pro_cate_id, pro_score, pro_cnt, pro_from, pro_des) VALUES
('有機高麗菜', 50, 1, 80, 1, 1, 5, 250, '屏東', '來自有機農場，口感清甜爽脆，通過嚴格驗證，無論清炒或入湯都美味，是您全家大小安心享用的健康蔬菜。'),
('無毒小黃瓜', 30, 2, 60, 1, 1, 4, 180, '彰化', '堅持無毒農法栽種，保留小黃瓜的天然清甜與爽脆多汁，適合涼拌、沙拉或直接鮮食，感受田園的清新滋味。'),
('富士蘋果', 120, 3, 150, 1, 2, 5, 300, '富士', '果肉細緻、香甜多汁，每一口都充滿濃郁果香與爽脆口感，是大人小孩都喜愛，送禮自用兩相宜的頂級水果。'),
('台灣香蕉', 85, 4, 45, 1, 2, 4, 210, '屏東', '在地小農用心栽培，Q彈香甜的絕佳口感，富含天然果糖與營養，是補充能量、運動前後的最佳點心選擇。'),
('台東池上米', 200, 5, 300, 1, 3, 5, 500, '台東', '來自台東縱谷的冠軍米，米粒晶瑩飽滿、口感Q彈，烹煮後香氣四溢，讓您品嚐來自純淨大地的原始好滋味。'),
('花蓮富里米', 180, 6, 280, 1, 3, 4, 450, '花蓮', '吸收秀姑巒溪純淨水源灌溉，米飯香Q可口，黏度與甜度俱佳，每一口都吃得到花東縱谷最自然的甘甜米香。'),
('高山烏龍茶', 45, 7, 500, 1, 4, 5, 150, '南投', '精選台灣高海拔茶區，茶湯金黃清澈，喉韻甘醇、蘭花香氣清幽，是喜愛清香型茶品者不容錯過的絕佳選擇。'),
('金萱紅茶', 60, 8, 350, 1, 4, 4, 120, '南投', '特有品種台茶12號製成，茶湯呈現迷人琥珀色，帶有獨特的天然奶香與果香，口感溫潤滑順不苦澀。'),
('日曬香菇', 90, 9, 220, 1, 5, 5, 200, '雲林', '遵循古法天然日曬，完整保留香菇的濃郁香氣與營養，口感Q彈厚實，是燉湯、滷肉、入菜的最佳提味幫手。'),
('新鮮杏鮑菇', 75, 10, 85, 1, 5, 4, 190, '苗栗', '菌柄肥厚、口感紮實Q彈似鮑魚，味道清香鮮甜，無論是煎、烤、炒或煮湯，都能展現其百搭的獨特美味。'),
('龍眼蜜', 110, 1, 450, 1, 6, 5, 100, '台中', '採集自龍眼花季的純淨蜂蜜，質地濃郁，帶有獨特的龍眼花香氣，滋味甜而不膩，是天然的養生滋補聖品。'),
('百花蜜', 95, 2, 400, 1, 6, 4, 90, '台中', '匯集四季百花精華，蜜源豐富，口感清香淡雅，層次豐富，富含多種天然酵素與維生素，是您日常保健的首選。'),
('放牧雞蛋', 250, 3, 120, 1, 7, 5, 350, '台南', '來自健康快樂的放牧雞，自由奔跑，蛋黃飽滿、色澤金黃，蛋白Q彈，口感香醇濃郁，每一口都吃得到營養。'),
('有機紅蘿蔔', 70, 4, 75, 1, 1, 4, 160, '雲林', '有機農法栽種，根莖飽滿、色澤鮮豔，口感清甜爽脆，富含β-胡蘿蔔素，是守護家人健康的營養美味蔬菜。'),
('香水鳳梨', 150, 5, 90, 1, 2, 5, 280, '宜蘭', '果肉纖維細緻，甜度高且帶有獨特香氣，風味濃郁多汁，一開箱便香氣四溢，讓您品嚐熱帶的甜蜜好滋味。'),
('有機糙米', 190, 6, 250, 1, 3, 4, 420, '花蓮', '保留完整米糠與胚芽，膳食纖維豐富，口感扎實有嚼勁，是追求健康飲食、體內環保的最佳主食新選擇。'),
('文山包種茶', 55, 7, 480, 1, 4, 5, 130, '台中', '產自坪林茶區的特色條形茶，茶湯蜜綠，入口甘醇滑潤，帶有清新優雅的蘭花香氣，是品茗者的絕佳選擇。'),
('黑木耳', 80, 8, 65, 1, 5, 4, 210, '新竹', '精選肉質肥厚的優質黑木耳，口感爽脆Q彈，富含膳食纖維與膠質，涼拌、快炒皆美味，是餐桌上的健康常客。'),
('紅心芭樂', 100, 9, 50, 1, 2, 4, 230, '高雄', '在地嚴選的紅心土芭樂，果肉軟Q、香氣濃郁，富含茄紅素與維生素C，獨特的酸甜古早滋味讓人回味無窮。'),
('在地小番茄', 140, 10, 70, 1, 2, 5, 260, '桃園', '小農新鮮直送的在地小番茄，皮薄多汁、酸甜可口，每一顆都充滿陽光的滋味，是開胃、解饞的天然零食。');

-- 刪除/建立 商品圖片
DROP TABLE IF EXISTS product_image;
CREATE TABLE product_image (
	pro_img_id int NOT NULL auto_increment,
    pro_id int NOT NULL, -- FK
    pro_img LONGBLOB DEFAULT NULL,
    CONSTRAINT product_image_pro_img_id_key PRIMARY KEY (pro_img_id)
) ENGINE InnoDB;

INSERT INTO product_image (pro_id, pro_img) VALUES
(1, NULL),
(2, NULL),
(3, NULL),
(4, NULL),
(5, NULL),
(6, NULL),
(7, NULL),
(8, NULL),
(9, NULL),
(10, NULL),
(11, NULL),
(12, NULL),
(13, NULL),
(14, NULL),
(15, NULL),
(16, NULL),
(17, NULL),
(18, NULL),
(19, NULL),
(20, NULL),
(1, NULL),
(5, NULL),
(8, NULL),
(15, NULL),
(2, NULL),
(10, NULL),
(4, NULL),
(12, NULL),
(7, NULL),
(19, NULL);

-- 刪除/建立 商城廣告
DROP TABLE IF EXISTS pro_ad;
CREATE TABLE pro_ad (
	pro_ad_id int NOT NULL AUTO_INCREMENT,  
    pro_ad_img longblob,
    -- 審核：0=編輯中,1=待審核,2=通過,3=未過,4=待繳費,5=已繳費
    pro_ad_revstat tinyint,
    pro_ad_revupd datetime,
    pro_ad_revremark varchar(100),
    -- 上下架：0=下架,1=上架
	pro_ad_launstat tinyint,
    pro_ad_launupd datetime,
    pro_ad_start date,
    pro_ad_end date,
    pro_ad_fee int,
    pro_ad_fee_end date,
    fmem_id int NOT NULL, -- FK
	pro_id int NOT NULL, -- FK
    CONSTRAINT pro_ad_pro_ad_id_key PRIMARY KEY (pro_ad_id)

) ENGINE InnoDB;

INSERT INTO pro_ad
(pro_ad_img, pro_ad_revstat, pro_ad_revupd, pro_ad_revremark,
 pro_ad_launstat, pro_ad_launupd, pro_ad_start, pro_ad_end,
 pro_ad_fee, pro_ad_fee_end, fmem_id, pro_id)
VALUES

-- 上架中：香水鳳梨
(NULL, 5, '2025-10-03 12:30:00', '已繳費',
 1, '2025-10-03 12:30:00', '2025-10-03', '2025-11-30',
 3000, '2025-10-03', 5, 15),

-- 上架中：高麗菜
(NULL, 5, '2025-09-28 10:10:00', '已繳費',
 1, '2025-09-28 10:10:00', '2025-09-28', '2025-11-30',
 2000, '2025-09-28', 1, 1),

-- 上架中：蘋果汁
(NULL, 5, '2025-10-02 11:05:00', '已繳費',
 1, '2025-10-02 11:05:00', '2025-10-02', '2025-11-30',
 2000, '2025-10-02', 3, 3),

-- 上架中：蜂蜜
(NULL, 5, '2025-10-01 09:20:00', '已繳費',
 1, '2025-10-01 09:20:00', '2025-10-01', '2025-11-30',
 3000, '2025-10-01', 2, 12),


-- 待審核：龍眼蜜
(NULL, 1, '2025-10-04 13:20:00', '待審核',
 0, '2025-10-04 13:20:00', '2025-10-04', '2025-11-30',
 2000, NULL, 1, 11),


-- 待審核：富里米
(NULL, 1, '2025-10-03 09:10:00', '待審核',
 0,'2025-10-03 09:10:00', '2025-10-19', '2025-11-27',
 1000, NULL, 6, 6),

-- 待繳費：高山烏龍茶
(NULL, 4, '2025-10-03 10:05:00', '待繳費',
 0,'2025-10-03 10:05:00', '2025-10-25', '2025-11-29',
 2000, '2025-10-21', 7, 7),

-- 不通過：金萱紅茶
(NULL, 3, '2025-10-01 18:25:00', '不通過：版權問題',
 0,'2025-10-01 18:25:00', NULL, NULL,
 1000, NULL, 8, 8),

-- 待繳費：日曬香菇
(NULL, 4, '2025-10-02 13:40:00', '待繳費',
 0, '2025-10-02 13:40:00', '2025-10-24', '2025-11-24',
 2000, '2025-10-28', 9, 9),

-- 待審核：台東池上米
(NULL, 1, '2025-10-03 09:05:00', '待審核',
 0, '2025-10-03 09:05:00', '2025-10-20', '2025-11-27',
 1000, NULL, 7, 5);

 -- 刪除/建立 活動廣告
DROP TABLE IF EXISTS act_ad;
CREATE TABLE act_ad (
    act_ad_id INT NOT NULL AUTO_INCREMENT,
    act_ad_img LONGBLOB,
    -- 審核：0=編輯中,1=待審核,2=通過,3=未過,4=待繳費,5=已繳費
    act_ad_revstat TINYINT NOT NULL DEFAULT 1,
    act_ad_revupd DATETIME DEFAULT CURRENT_TIMESTAMP,
    act_ad_revremark VARCHAR(100),
    -- 上下架：0=下架,1=上架
    act_ad_launstat TINYINT DEFAULT 0,
    act_ad_launupd DATETIME DEFAULT CURRENT_TIMESTAMP,
    act_ad_start DATE,
    act_ad_end DATE,
    act_ad_fee INT DEFAULT NULL,
    act_ad_fee_end DATE DEFAULT NULL,
    -- FK
    fmem_id INT NOT NULL,
    act_id INT NOT NULL,
    PRIMARY KEY (act_ad_id)
);


-- #1：已繳費→已下架（區間已過）【下田去！一日小農體驗】
INSERT INTO act_ad
(act_ad_img, act_ad_revstat, act_ad_revupd, act_ad_revremark,
 act_ad_launstat, act_ad_launupd, act_ad_start, act_ad_end,
 act_ad_fee, act_ad_fee_end, fmem_id, act_id)
VALUES
(NULL, 5, '2025-08-28 09:30:00', '已審核',
  0, '2025-10-01 00:00:00', '2025-09-01', '2025-09-30',
  1000, '2025-08-28', 1, 1),

-- #2：上架中【下田去！一日小農體驗】
(NULL, 5, '2025-09-28 09:30:00', '已審核',
  1, '2025-09-29 10:00:00', '2025-10-01', '2025-10-31',
  1000, '2025-09-28', 1, 1),

-- #3：上架中【小小牧場】
(NULL, 5, '2025-09-20 11:20:00', '已審核',
  1, '2025-09-21 08:30:00', '2025-10-01', '2025-11-30',
  2000, '2025-09-20', 2, 3),

-- #4：上架中【小村莊的故事之旅】
(NULL, 5, '2025-09-22 14:00:00', '已審核',
  1, '2025-09-23 09:00:00', '2025-10-01', '2025-12-31',
  3000, '2025-09-22', 1, 5),

-- #5：待審核【從產地到餐桌的秘密】
(NULL, 1, '2025-10-01 09:00:00', '待審核',
  0, '2025-10-01 09:00:00', '2025-10-01', '2025-10-31',
  1000, NULL, 3, 2),

-- #6：待審核【藍染工藝體驗課程】
(NULL, 1, '2025-10-05 10:10:00', '待審核',
  0, '2025-10-01 09:00:00', '2025-10-01', '2025-10-31',
  1000, NULL, 2, 4);


-- 刪除/建立 活動分類
drop table if exists actcate;
create table actcate (
 actcate_id			int not null auto_increment,
 actcate_name		varchar(10),
 constraint actcate_actcate_id_pk primary key (actcate_id));

insert into actcate values (1,'農事體驗'), (2,'食農教育'), (3,'親子同樂'), (4,'動物互動'), (5, '手作工藝'),
						   (6,'戶外體驗'), (7,'導覽活動'), (8,'季節限定'), (9,'其它');



-- 刪除/建立 活動
drop table if exists act;
create table act (
 act_id				int not null auto_increment,
 act_name			varchar(30) not null,
 act_start			date,
 act_end			date,
 act_des			varchar(1000),
 act_fee			int not null,
 act_stat			tinyint default 0,
 act_upd			datetime not null,
 act_remark			varchar(1000),
 act_launstat		tinyint,
 act_launupd		datetime,
 fmem_id			int not null,
 act_score			int default 0,
 act_cnt			int default 0,
 act_mainimg		longblob,		/* 活動主圖, 活動一覽頁面會顯示的圖片 */
 constraint act_fmem_id_fk foreign key (fmem_id) references fmem (fmem_id),
 constraint act_act_id_pk primary key (act_id));

insert into act values
  /*到目前都正常上架&有人評價過*/
 (null, '下田去！一日小農體驗', '2025-07-01','2025-12-30',
 '捲起袖子、赤腳踩在田裡，親手插秧、採收蔬果，感受最真實的農村日常。',
 200, 2, '2025-05-10 10:20:30', null, 1, '2025-05-15 09:20:30', 1, null, null, null),
 
 /*審核未過*/
 (null, '從產地到餐桌的秘密', '2025-10-25','2026-03-31',
 '透過遊戲與教學，讓大小朋友了解食材來源，培養珍惜食物的心。',
 10000, 3, '2025-08-26 08:20:00', '報名費用有疑慮，請再次確認。', null, null, 3, null, null, null),
 
 /*到目前都正常上架&有人評價過*/
 (null, '小小牧場', '2025-03-15','2025-10-31',
 '餵小羊、抱兔子，近距離接觸可愛動物，體驗牧場生活樂趣。',
 399, 2, '2025-01-10 14:10:30', null, 1, '2025-01-15 16:00:30', 2, null, null, null),
 
 /*有人評價過此活動, 此活動已結束並下架*/
 (null, '藍染工藝體驗課程', '2024-12-01','2025-06-10',
 '親手體驗藍染工藝，學習天然染色技巧，創作獨一無二的布藝作品。',
 700, 2, '2024-10-27 19:10:30', '已修正金額，審核通過', 0, '2025-06-11 00:00:00', 2, null, null, null),
 
 /*審核已通過但還沒上架*/
 (null, '小村莊的故事之旅', '2025-10-01','2026-02-28',
 '在導覽老師帶領下，認識農村的歷史、風俗與文化典故。',
 299, 2, '2025-08-28 10:20:30', null, 0, null, 1, null, null, null);



-- 刪除/建立 活動分類明細
drop table if exists actcate_list;
create table actcate_list (
 act_id				int	not null,
 actcate_id			int not null,
 constraint actcate_list_act_id_fk foreign key (act_id) references act (act_id),
 constraint actcate_list_actcate_id_fk foreign key (actcate_id) references actcate (actcate_id),
 constraint actcate_list_act_id_actcate_id_pk primary key (act_id, actcate_id));
 
 insert into actcate_list values (1, 1), (1, 3), (1, 6),
								 (2, 2), (2, 3),
                                 (3, 3), (3, 4), (3, 6), (3, 7),
                                 (4, 3), (4, 5),
                                 (5, 3), (5, 6), (5, 7);
                                 
                                 
-- 刪除/建立 活動照片
drop table if exists actimg;
create table actimg (
 actimg_id			int not null auto_increment,
 act_img			longblob,
 act_id				int not null,
 actimg_order		int,		/* 活動圖片的順序, 應該會用到, 總之先加進來 */
 constraint actimg_act_id_fk foreign key (act_id) references act (act_id),
 constraint actimg_actimg_id_pk primary key (actimg_id));

insert into actimg values (null, null, 1, 1), (null, null, 1, 2), (null, null, 1, 3),
						  (null, null, 3, 1), (null, null, 3, 2),
                          (null, null, 4, 1),
                          (null, null, 5, 1), (null, null, 5, 2), (null, null, 5, 3), (null, null, 5, 4), (null, null, 5, 5);
                          
                          
-- 刪除/建立 場次
drop table if exists ses;
create table ses (
 ses_id				int not null auto_increment,
 ses_date			date not null,
 ses_start			time not null,
 ses_end			time not null,
 reg_end			date not null,
 minppl				int not null default 1,
 maxppl				int not null,
 ses_fee			int not null,
 ses_launstat		tinyint not null default 0,
 ses_launupd		datetime,
 reg_stat			tinyint not null default 5,
 headcount			int default 0,
 act_id				int not null,
 constraint ses_act_id_fk foreign key (act_id) references act (act_id),
 constraint ses_ses_id_pk primary key (ses_id));																									/* 0報名中 1成團 2不成團、取消場次*/
																													/* 0下 1上*/	   				/* 3已完成場次 4取消場次 5尚未開始報名(因為未上架) */		
                    /* (場次id, 場次date, 開始時間, 結束時間, 報名截止(確認是否成團)date, 人數下限, 人數上限, 報名費, 場次狀態, 場次狀態更新時間, 報名狀態, 報名人數, 活動id)*/
						/*目前是1.3.4有上架過可以寫場次, 4只能寫已結束的*/
 
 					   /*圓滿結束 */
insert into ses values (null, '2025-08-08', '15:00', '17:30', '2025-08-01',  5, 20, 230, 0, '2025-08-08 17:30:00', 3, null, 1),
					   /*還在報名中...這邊我有修改上架狀態更新時間&修正場次狀態*/
					   (null, '2025-10-10', '14:00', '16:30', '2025-10-01',  5, 20, 200, 1, '2025-08-11 14:10:09', 0, null, 1),
                       /*不成團, 取消 */
                       (null, '2025-03-20', '10:30', '11:30', '2025-03-10',  5, 15, 399, 0, '2025-03-10 00:00:00', 2, null, 3),
					   /* 報名中 */                       
                       (null, '2025-10-05', '15:00', '16:00', '2025-09-20',  5, 15, 449, 1, '2025-08-05 00:00:00', 0, null, 3),
                       /* 場次取消 (10/7有地震導致部分設施要維修、直接取消) */
                       (null, '2025-10-10', '16:30', '17:30', '2025-10-01', 10, 20, 499, 0, '2025-10-07 00:00:00', 4, null, 3),
                       /* 圓滿結束 */
                       (null, '2025-06-05', '14:00', '16:30', '2025-05-25', 15, 30, 700, 1, '2025-06-30 00:00:00', 3, null, 4),
                       /* 成團, 活動尚未進行 */
                       (null, '2025-09-10', '14:00', '16:30', '2025-09-01',  5, 15, 200, 1, '2025-09-01 00:00:00', 1, null, 1);

-- 刪除/建立 活動廣告






-- step 2. 

-- (2-1) 一般會員
 
-- (2-2) 商品折價卷 -> 商品折價卷持有者明細（FK商品折價卷編號）（FK一般會員編號）

-- (2-3) 商城訂單（FK一般會員編號）（FK商品折價卷持有者流水號） -> 商城訂單明細（FK訂單編號）（FK商品編號）

-- (2-4) 購物車（FK一般會員編號）（FK商品編號） -> 檢舉表單(FK商品編號) -> 商品評論（FK商品編號）（FK一般會員編號）

-- (2-5) 活動折價卷 -> 活動折價卷持有者明細（FK活動折價卷編號）（FK一般會員編號）

-- (2-6) 報名訂單（FK場次編號）（FK一般會員編號）（FK活動折價卷持有者流水號）

-- (2-7) 商品收藏清單（FK一般會員編號）（FK商品編號） -> 活動收藏清單（FK一般會員編號）（FK活動編號）


-- 刪除/建立 一般會員
DROP TABLE IF EXISTS mem;
CREATE TABLE mem (
	mem_id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
	mem_acc varchar(40) NOT NULL,
	mem_pwd varchar(100) NULL,
	acc_status tinyint NOT NULL DEFAULT 0,
	mem_name varchar(20) NOT NULL,
    mem_birthday date NULL,
	mem_mobile varchar(11) NULL UNIQUE,
	mem_email varchar(254) NOT NULL UNIQUE,
	mem_zipcode varchar(6) NULL,
	mem_city varchar(5) NULL,
	mem_dist varchar(5) NULL,
	mem_addr varchar(40) NULL,
	reg_date datetime NOT NULL DEFAULT current_timestamp,
	mem_point int NOT NULL DEFAULT 0,
    auth_provider VARCHAR(10) DEFAULT 'LOCAL',
	provider_id VARCHAR(255) DEFAULT NULL
);

INSERT INTO mem (mem_acc, mem_pwd, acc_status, mem_name, mem_birthday, mem_mobile, mem_email, mem_zipcode, mem_city, mem_dist, mem_addr, reg_date, mem_point) VALUES
('test', '$2a$12$RJiZ/95O5j.PrtKewDo0TubXJr3xPb3tlwkehmo5fJLNgMqFUMMbu', 1, '謝維綺', '1980-11-26', '0910-380143', 'pamela8508@gmail.com', '320', '桃園市', '中壢區', '仁和街35號', '2022-08-26 10:30:00', 150),
('test0002', '$2a$10$BaV4KcvPo5KfXA2YuPzMcuSDZwHzUePvfiIWTtpVx0zXCAazE7STa', 1, '胡得軒', '1996-10-11', '0916-518593', 'henson1654@hotmail.com', '600', '嘉義市', '西區', '世賢路2段5號', '2022-08-26 11:29:30', 110),
('test0003', '$2a$10$BaV4KcvPo5KfXA2YuPzMcuSDZwHzUePvfiIWTtpVx0zXCAazE7STa', 1, '宋柯雯', '1993-08-07', '0961-388330', 'arianna6146@hotmail.com', '511', '彰化縣', '社頭鄉', '中山路1段38號10樓之10', '2022-09-01 12:00:59', 253),
('valine203', '$2a$10$BaV4KcvPo5KfXA2YuPzMcuSDZwHzUePvfiIWTtpVx0zXCAazE7STa', 1, '郭實祐', '1990-11-01', '0937-453975', 'jeffrey2062@icloud.com', '360', '苗栗縣', '苗栗市', '宜春路62號', '2023-03-05 09:08:05', 20),
('alphaWolf2031', '$2a$10$BaV4KcvPo5KfXA2YuPzMcuSDZwHzUePvfiIWTtpVx0zXCAazE7STa', 1, '林蓁蓓', '1978-09-08', '0972-375934', 'kaylynn3676@hotmail.com', '803', '高雄市', '鹽埕區', '大成街98號', '2023-04-01 01:01:10', 5),
('SkyHunter77', '$2a$10$BaV4KcvPo5KfXA2YuPzMcuSDZwHzUePvfiIWTtpVx0zXCAazE7STa', 0, '何俞維', '1979-05-23', '0961-063659', 'hampden3392@gmail.com', '555', '南投縣', '魚池鄉', '日月街24號9樓之11', '2023-10-05 20:58:09', 16),
('tiger_XR9821', '$2a$10$BaV4KcvPo5KfXA2YuPzMcuSDZwHzUePvfiIWTtpVx0zXCAazE7STa', 1, '連之義', '1992-07-28', '0915-476888', 'richards2316@gmail.com', '931', '屏東縣', '佳冬鄉', '民學路7號', '2024-06-16 22:10:00', 0),
('UtFeobef152', '$2a$10$BaV4KcvPo5KfXA2YuPzMcuSDZwHzUePvfiIWTtpVx0zXCAazE7STa', 1, '許洋竹', '1985-05-19', '0956-715009', 'mendoza8324@gmail.com', '803', '高雄市', '鹽埕區', '大勇市場5號', '2024-10-20 14:20:35', 88),
('RavenX42ZpLm', '$2a$10$BaV4KcvPo5KfXA2YuPzMcuSDZwHzUePvfiIWTtpVx0zXCAazE7STa', 1, '陳婉術', '1982-06-21', '0924-554240', 'evangeline3888@gmail.com', '110', '臺北市', '信義區', '信義路5段16號6樓之6', '2024-11-20 11:15:20', 23),
('AlphaX9273', '$2a$10$BaV4KcvPo5KfXA2YuPzMcuSDZwHzUePvfiIWTtpVx0zXCAazE7STa', 0, '黃珍育', '1987-05-21', '0939-682988', 'debbie7435@outlook.com', '882', '澎湖縣', '望安鄉', '花嶼9號', '2024-12-01 08:54:53', 225),
('skyline_83x', '$2a$10$BaV4KcvPo5KfXA2YuPzMcuSDZwHzUePvfiIWTtpVx0zXCAazE7STa', 0, '李大仁', '1965-02-28', '0911-222333', 'member001@example.com', '100', '台北市', '大安區', '信義路100號', '2025-01-01 10:00:00', 20),
('nova88_rider_12', '$2a$10$BaV4KcvPo5KfXA2YuPzMcuSDZwHzUePvfiIWTtpVx0zXCAazE7STa', 1, '王小美', '1969-01-08', '0922-333444', 'member002@example.com', '221', '新北市', '板橋區', '中山路200號', '2025-02-05 11:00:00', 28),
('xtrmcoder207', '$2a$10$BaV4KcvPo5KfXA2YuPzMcuSDZwHzUePvfiIWTtpVx0zXCAazE7STa', 1, '張志豪', '1989-04-28', '0933-444555', 'member003@example.com', '401', '台中市', '北區', '學士路300號', '2025-03-18 12:00:00', 0),
('alpha_2099zx', 'ZetaStorm99', 1, '陳玉芬', '1998-07-14', '0944-555666', 'member004@example.com', '801', '高雄市', '前金區', '五福路88號', '2025-03-18 13:00:00', 0),
('deltaWave7192', 'vR7@bLpW25', 2, '林建宏', '2000-01-19', '0955-666777', 'member005@example.com', '700', '台南市', '中西區', '民生路199號', '2025-05-25 14:00:00', 97),
('neorunner92', 'Alpha42moon', 0, '曾雅婷', '2002-10-26', '0966-777888', 'member006@example.com', '300', '新竹市', '東區', '光復路250號', '2025-06-06 15:00:00', 53),
('alpha3x9z1t', 'Skyline@Z9', 2, '游信宏', '2004-08-20', '0977-888999', 'member007@example.com', '970', '花蓮縣', '花蓮市', '中正路10號', '2025-07-13 16:00:00', 7),
('storm1987_wave', 'maxwell302', 0, '洪詠欣', '1999-08-31', '0988-999000', 'member008@example.com', '260', '宜蘭縣', '羅東鎮', '中山路一段88號', '2025-08-30 17:00:00', 5),
('midnight_42_zz', 'Tiger88@Run', 1, '邱柏睿', '2005-09-21', '0911-000222', 'member009@example.com', '600', '嘉義市', '西區', '垂楊路120號', current_timestamp(), 0),
('echo_delta_1209', 'Xp92kLo@1', 1, '簡心怡', '1988-12-25', '0922-000333', 'member010@example.com', '540', '南投縣', '南投市', '中興路300號', current_timestamp(), 0);

-- 設定區分大小寫 (登入才會辨識大小寫)
ALTER TABLE mem
MODIFY COLUMN mem_acc VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;



-- 刪除/建立 商品折價卷
DROP TABLE IF EXISTS  pro_cpn;
CREATE TABLE pro_cpn(
    pro_cpn_id INT NOT NULL AUTO_INCREMENT,
    cpn_source VARCHAR(20) NOT NULL DEFAULT 'COMMON'  COMMENT '折價券用途',
    cpn_name VARCHAR(50) NOT NULL,
    disc_type TINYINT NOT NULL COMMENT '0:滿額折抵,1: 百分比',
    disc_value DECIMAL(10,2) NOT NULL,
    min_spend INT,
    start_date DATE,
    valid_days INT,
    cpn_desc VARCHAR(200) COMMENT '折價券規則描述',
    is_active TINYINT NOT NULL COMMENT '0:未啟用,1:啟用',
    crt_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
    appl_scope TINYINT NOT NULL DEFAULT 0 COMMENT '0: 全館, 1: 指定小農, 2: 指定商品',
   CONSTRAINT  pro_cpn_id_pk PRIMARY KEY (pro_cpn_id)
) ENGINE=InnoDB;

INSERT INTO pro_cpn 
(cpn_name, disc_type, disc_value, min_spend, start_date, valid_days, cpn_desc, is_active, appl_scope)
VALUES
-- 1註冊會員：新客專屬 9 折券（14天內有效）
('新客專屬9折券', 1, 0.90, NULL, NULL, 14, '新會員註冊後14天內可使用，全館適用', 1, 0),
('新客專屬抵100', 0, 100, NULL, NULL, 14, '新會員註冊後14天內可使用，全館適用', 1, 0),
-- 2️生日會員：生日當月 85 折券（30天內有效）
('生日85折券', 1, 0.85, NULL, NULL, 30, '生日當月發放，全館適用，30天內有效', 1, 0),
('生日折200', 0,200, NULL, NULL, 30, '生日當月發放，全館適用，30天內有效', 1, 0),
-- 3轉盤：轉盤折扣券（30天內有效）
('轉盤折200', 0,200, NULL, NULL, 30, '轉盤折200，全館適用，30天內有效', 1, 0),
('轉盤折100', 0,100, NULL, NULL, 30, '轉盤折100，全館適用，30天內有效', 1, 0);

-- 刪除/建立 商品折價卷持有者明細
DROP TABLE IF EXISTS mem_pro_cpn;
CREATE TABLE mem_pro_cpn (
    cpn_holder_detail_id INT NOT NULL AUTO_INCREMENT,
    pro_cpn_id INT NOT NULL,     -- FK
    mem_id INT NOT NULL,         -- FK
    pro_ord_id INT  NULL,     -- FK
    cpn_use_status TINYINT NOT NULL COMMENT '0:未使用,1:已使用,2:已過期',
    crt_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
    rcv_at DATETIME NOT NULL,
    eff_start DATE NOT NULL,
    eff_end DATE,
    used_at DATETIME,
    CONSTRAINT cpn_holder_detail_id_pk PRIMARY KEY (cpn_holder_detail_id)
) ENGINE=InnoDB;

INSERT INTO mem_pro_cpn
(pro_cpn_id, mem_id, pro_ord_id, cpn_use_status, rcv_at, eff_start, eff_end, used_at)
VALUES
(2, 1, NULL, 0, NOW(), CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY), NULL),
(1, 1, NULL, 0, NOW(), DATE_SUB(CURDATE(), INTERVAL 3 DAY), DATE_ADD(CURDATE(), INTERVAL 11 DAY), NULL),
(3, 1, NULL, 0, NOW(), CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY), NULL),
(4, 1, NULL, 0, NOW(), DATE_SUB(CURDATE(), INTERVAL 3 DAY), DATE_ADD(CURDATE(), INTERVAL 11 DAY), NULL),
-- 1️.新註冊會員：未使用中（有效期內）
(1, 11, NULL, 0, NOW(), DATE_SUB(CURDATE(), INTERVAL 3 DAY), DATE_ADD(CURDATE(), INTERVAL 11 DAY), NULL),

-- 2️.生日會員：未使用（今天生日當月）
(2, 4, NULL, 0, NOW(), CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY), NULL),
(1, 4, NULL, 0, NOW(), DATE_SUB(CURDATE(), INTERVAL 3 DAY), DATE_ADD(CURDATE(), INTERVAL 11 DAY), NULL),

-- 3️.生日會員：已使用
(2, 2, 1, 1, '2025-09-01 10:00:00', '2025-09-01', '2025-09-30', '2025-09-15 13:00:00'),

-- 4️.測試過期狀況
(1, 3, NULL, 2, '2025-06-01 09:00:00', '2025-06-01', '2025-06-15', NULL),

-- 5️.新註冊會員：剛領取，未使用
(1, 12, NULL, 0, NOW(), CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY), NULL);

-- 刪除/建立 商城訂單
DROP TABLE IF EXISTS pro_order;
CREATE TABLE pro_order(
	pro_ord_id INT NOT NULL AUTO_INCREMENT,
	mem_id INT NOT NULL, -- FK
	cpn_holder_detail_id INT, -- FK
	pro_ord_date DATETIME NOT NULL,
	pro_ord_status TINYINT NOT NULL default 0,
	pro_pay_status TINYINT NOT NULL default 0,
	pro_total INT NOT NULL,
	pro_ord_ship_fee INT NOT NULL default 0,
	pro_ord_cpndisc INT DEFAULT NULL default 0,
	pro_ord_pointdisc INT NOT NULL default 0,
	pro_ord_pointget INT NOT NULL,
	pro_ord_grand_total INT NOT NULL,
	pro_ord_comm VARCHAR(200) DEFAULT NULL,
	pro_ord_payment TINYINT NOT NULL default 0,
	pro_ord_shipment TINYINT NOT NULL default 0,
	pro_tracking_no VARCHAR(30) DEFAULT NULL,
	pro_ord_shipdate DATETIME DEFAULT NULL,
    PRO_ORD_NAME VARCHAR(20) NOT NULL,
	PRO_ORD_MOBILE VARCHAR(11) NOT NULL,
	PRO_ORD_EMAIL VARCHAR(254) NOT NULL,
	PRO_ORD_ADDR VARCHAR(100) NOT NULL,
    PRO_ORD_ALLOC_STATUS TINYINT NOT NULL default 0,
    PRO_ORD_ALLOC_TOTAL INT,
    PRO_ORD_ALLOC_SEND_FMEM INT,
	CONSTRAINT pro_order_pro_ord_id_pk PRIMARY KEY (pro_ord_id)
)ENGINE InnoDB; 

INSERT INTO PRO_ORDER (
	MEM_ID, CPN_HOLDER_DETAIL_ID, PRO_ORD_DATE, PRO_ORD_STATUS, PRO_PAY_STATUS, 
	PRO_TOTAL, PRO_ORD_SHIP_FEE, PRO_ORD_CPNDISC, PRO_ORD_POINTDISC, PRO_ORD_POINTGET, 
	PRO_ORD_GRAND_TOTAL, PRO_ORD_COMM, PRO_ORD_PAYMENT, PRO_ORD_SHIPMENT, PRO_TRACKING_NO, 
	PRO_ORD_SHIPDATE, PRO_ORD_NAME, PRO_ORD_MOBILE, PRO_ORD_EMAIL, PRO_ORD_ADDR, PRO_ORD_ALLOC_STATUS
) VALUES
-- ===================================================================================================
-- MEM_ID 1: 謝維綺 (3 筆訂單 - FMEM_ID 1)
-- ===================================================================================================
(1, NULL, '2025-08-01 10:00:00', 3, 1, 950, 60, 50, 0, 10, 960, '請盡快出貨', 0, 0, 'SF10101010', '2025-08-03 09:00:00', '謝維綺', '0910-380143', 'pamela8508@gmail.com', '320桃園市中壢區仁和街35號', '0'),
(1, NULL, '2025-08-03 16:30:00', 4, 1, 600, 60, 0, 0, 6, 660, NULL, 1, 1, '711A1010101', '2025-08-05 14:00:00', '謝維綺', '0910-380143', 'pamela8508@gmail.com', '320桃園市中壢區仁和街35號', '0'),
(1, NULL, '2025-08-10 11:00:00', 0, 0, 400, 60, 0, 0, 0, 460, '付款完成通知', 1, 0, NULL, NULL, '謝維綺', '0910-380143', 'pamela8508@gmail.com', '320桃園市中壢區仁和街35號', '0'),


-- ===================================================================================================
-- MEM_ID 2: 胡得軒 (3 筆訂單 - FMEM_ID 2)
-- ===================================================================================================
(2, NULL, '2025-08-01 14:00:00', 2, 1, 550, 60, 0, 0, 5, 610, NULL, 1, 1, 'FAMIB101010', '2025-08-02 11:00:00', '胡得軒', '0916-518593', 'henson1654@hotmail.com', '600嘉義市西區世賢路2段5號', '0'),
(2, NULL, '2025-08-06 17:00:00', 1, 0, 300, 60, 100, 0, 0, 260, '請務必檢查數量', 0, 0, NULL, NULL, '胡得軒', '0916-518593', 'henson1654@hotmail.com', '600嘉義市西區世賢路2段5號', '0'),
(2, NULL, '2025-08-15 09:30:00', 3, 1, 800, 60, 0, 0, 8, 860, NULL, 0, 0, 'SF20202020', '2025-08-17 14:00:00', '胡得軒', '0916-518593', 'henson1654@hotmail.com', '600嘉義市西區世賢路2段5號', '0'), -- 修正: 850 -> 800 (Item總和)；910 -> 860 (實付)

-- ===================================================================================================
-- MEM_ID 3: 宋柯雯 (4 筆訂單 - FMEM_ID 3, 4, 5)
-- ===================================================================================================
(3, NULL, '2025-08-02 11:30:00', 3, 1, 1100, 60, 50, 100, 10, 1010, '黑豬肉請分裝', 0, 0, 'SF30303030', '2025-08-04 10:30:00', '宋柯雯', '0961-388330', 'arianna6146@hotmail.com', '511彰化縣社頭鄉中山路1段38號10樓之10', '0'),
(3, NULL, '2025-08-08 10:00:00', 2, 1, 470, 0, 0, 0, 5, 470, '急件！', 0, 0, 'HMM11223344', '2025-08-09 15:00:00', '宋柯雯', '0961-388330', 'arianna6146@hotmail.com', '511彰化縣社頭鄉中山路1段38號10樓之10', '0'), -- 修正: 570 -> 470 (Item總和)；570 -> 470 (實付)
(3, NULL, '2025-08-15 14:00:00', 0, 0, 450, 60, 0, 0, 0, 510, NULL, 1, 1, NULL, NULL, '宋柯雯', '0961-388330', 'arianna6146@hotmail.com', '511彰化縣社頭鄉中山路1段38號10樓之10', '0'),
(3, NULL, '2025-08-16 16:00:00', 0, 0, 600, 60, 100, 0, 0, 560, '隔日付款', 1, 0, NULL, NULL, '宋柯雯', '0961-388330', 'arianna6146@hotmail.com', '511彰化縣社頭鄉中山路1段38號10樓之10', '0'),

-- ===================================================================================================
-- MEM_ID 4: 郭實祐 (3 筆訂單 - FMEM_ID 1, 3, 4)
-- ===================================================================================================
(4, NULL, '2025-08-03 09:00:00', 4, 1, 1200, 0, 0, 0, 12, 1200, '退貨原因：數量錯誤', 1, 0, 'SF40404040', '2025-08-03 09:00:00', '郭實祐', '0937-453975', 'jeffrey2062@icloud.com', '360苗栗縣苗栗市宜春路62號', '0'),
(4, NULL, '2025-08-09 13:30:00', 3, 1, 900, 60, 50, 0, 8, 910, NULL, 0, 1, '711B4040404', '2025-08-11 12:00:00', '郭實祐', '0937-453975', 'jeffrey2062@icloud.com', '360苗栗縣苗栗市宜春路62號', '0'), -- 修正: 800 -> 900 (Item總和)；810 -> 910 (實付)
(4, NULL, '2025-08-12 17:00:00', 6, 1, 350, 60, 0, 0, 0, 410, '已退款', 0, 0, 'SF50505050', '2025-08-15 10:00:00', '郭實祐', '0937-453975', 'jeffrey2062@icloud.com', '360苗栗縣苗栗市宜春路62號', '0'), -- 修正: 300 -> 350 (Item總和)；360 -> 410 (實付)

-- ===================================================================================================
-- MEM_ID 5: 林蓁蓓 (3 筆訂單 - FMEM_ID 2, 3, 4)
-- ===================================================================================================
(5, NULL, '2025-08-05 12:00:00', 5, 1, 600, 60, 0, 0, 0, 660, '已寄出退貨商品', 1, 1, 'POST50505050', '2025-08-07 15:00:00', '林蓁蓓', '0972-375934', 'kaylynn3676@hotmail.com', '803高雄市鹽埕區大成街98號', '0'),
(5, NULL, '2025-08-13 09:30:00', 2, 1, 1000, 60, 50, 0, 10, 1010, NULL, 0, 0, 'SF60606060', '2025-08-14 11:00:00', '林蓁蓓', '0972-375934', 'kaylynn3676@hotmail.com', '803高雄市鹽埕區大成街98號', '0'),
(5, NULL, '2025-08-17 15:00:00', 0, 0, 240, 60, 0, 0, 0, 300, '請於期限內付款', 1, 1, NULL, NULL, '林蓁蓓', '0972-375934', 'kaylynn3676@hotmail.com', '803高雄市鹽埕區大成街98號', '0'),

-- ===================================================================================================
-- MEM_ID 6-20: 新增會員訂單 (每位 2-3 筆訂單，隨機分配 FMEM_ID 1-5)
-- ===================================================================================================
-- MEM_ID 6: 何俞維 (2 筆訂單 - FMEM_ID 5, 2)
(6, NULL, '2025-08-18 10:00:00', 3, 1, 450, 60, 0, 0, 4, 510, NULL, 0, 0, 'SF70707070', '2025-08-20 12:00:00', '何俞維', '0961-063659', 'hampden3392@gmail.com', '555南投縣魚池鄉日月街24號9樓之11', '0'),
(6, NULL, '2025-08-22 14:00:00', 1, 1, 300, 60, 50, 0, 0, 310, '已付款待出貨', 0, 1, NULL, NULL, '何俞維', '0961-063659', 'hampden3392@gmail.com', '555南投縣魚池鄉日月街24號9樓之11', '0'),

-- MEM_ID 7: 連之義 (2 筆訂單 - FMEM_ID 3, 1)
(7, NULL, '2025-08-25 15:30:00', 2, 1, 500, 60, 0, 0, 5, 560, NULL, 0, 0, 'POST778899', '2025-08-26 13:00:00', '連之義', '0915-476888', 'richards2316@gmail.com', '931屏東縣佳冬鄉民學路7號', '0'),
(7, NULL, '2025-09-01 11:00:00', 3, 1, 800, 60, 100, 0, 7, 760, '超商取貨', 1, 1, '711C778899', '2025-09-03 16:00:00', '連之義', '0915-476888', 'richards2316@gmail.com', '931屏東縣佳冬鄉民學路7號', '0'),

-- MEM_ID 8: 許洋竹 (2 筆訂單 - FMEM_ID 4, 3)
(8, NULL, '2025-09-03 17:00:00', 0, 0, 350, 60, 0, 0, 0, 410, NULL, 1, 0, NULL, NULL, '許洋竹', '0956-715009', 'mendoza8324@gmail.com', '803高雄市鹽埕區大勇市場5號', '0'), -- 修正: 370 -> 350 (Item總和)；430 -> 410 (實付)
(8, NULL, '2025-09-05 10:30:00', 4, 1, 1000, 0, 50, 0, 9, 950, '已退貨申請', 0, 0, 'SF88990011', '2025-09-06 10:30:00', '許洋竹', '0956-715009', 'mendoza8324@gmail.com', '803高雄市鹽埕區大勇市場5號', '0'),

-- MEM_ID 9: 陳婉術 (3 筆訂單 - FMEM_ID 1, 5, 4)
(9, NULL, '2025-09-08 14:30:00', 3, 1, 600, 60, 0, 0, 6, 660, NULL, 0, 1, 'FAMIC990011', '2025-09-10 12:00:00', '陳婉術', '0924-554240', 'evangeline3888@gmail.com', '110臺北市信義區信義路5段16號6樓之6', '0'),
(9, NULL, '2025-09-12 16:00:00', 1, 0, 400, 60, 50, 0, 0, 410, '尚未付款', 1, 0, NULL, NULL, '陳婉術', '0924-554240', 'evangeline3888@gmail.com', '110臺北市信義區信義路5段16號6樓之6', '0'),
(9, NULL, '2025-09-15 11:00:00', 2, 1, 350, 60, 0, 0, 3, 410, NULL, 0, 0, 'POST112233', '2025-09-16 14:00:00', '陳婉術', '0924-554240', 'evangeline3888@gmail.com', '110臺北市信義區信義路5段16號6樓之6', '0'), -- 修正: 370 -> 350 (Item總和)；430 -> 410 (實付)

-- MEM_ID 10: 黃珍育 (2 筆訂單 - FMEM_ID 2, 3)
(10, NULL, '2025-09-18 09:00:00', 3, 1, 550, 60, 0, 0, 6, 610, NULL, 1, 1, '711D223344', '2025-09-20 10:00:00', '黃珍育', '0939-682988', 'debbie7435@outlook.com', '882澎湖縣望安鄉花嶼9號', '0'),
(10, NULL, '2025-09-21 17:30:00', 1, 1, 400, 60, 50, 0, 4, 410, NULL, 0, 0, NULL, NULL, '黃珍育', '0939-682988', 'debbie7435@outlook.com', '882澎湖縣望安鄉花嶼9號', '0'), -- 修正: 500 -> 400 (Item總和)；510 -> 410 (實付)

-- MEM_ID 11: 李大仁 (2 筆訂單 - FMEM_ID 4, 5)
(11, NULL, '2025-09-23 11:00:00', 3, 1, 370, 60, 0, 0, 6, 430, '請放管理室', 0, 0, 'SF99001122', '2025-09-25 09:00:00', '李大仁', '0911-222333', 'member001@example.com', '100台北市大安區信義路100號', '0'), -- 修正: 590 -> 370 (Item總和)；650 -> 430 (實付)
(11, NULL, '2025-09-26 15:00:00', 2, 1, 450, 60, 0, 0, 5, 510, NULL, 1, 1, 'FAMID990011', '2025-09-27 15:00:00', '李大仁', '0911-222333', 'member001@example.com', '100台北市大安區信義路100號', '0'),

-- MEM_ID 12: 王小美 (3 筆訂單 - FMEM_ID 1, 2, 3)
(12, NULL, '2025-09-28 10:00:00', 0, 0, 400, 60, 50, 0, 0, 410, NULL, 1, 0, NULL, NULL, '王小美', '0922-333444', 'member002@example.com', '221新北市板橋區中山路200號', '0'),
(12, NULL, '2025-09-30 14:00:00', 3, 1, 600, 60, 0, 0, 6, 660, NULL, 0, 1, '711E887766', '2025-10-02 11:00:00', '王小美', '0922-333444', 'member002@example.com', '221新北市板橋區中山路200號', '0'),
(12, NULL, '2025-10-01 16:30:00', 1, 1, 500, 60, 0, 0, 5, 560, '已出貨', 0, 0, NULL, NULL, '王小美', '0922-333444', 'member002@example.com', '221新北市板橋區中山路200號', '0'),

-- MEM_ID 13: 張志豪 (2 筆訂單 - FMEM_ID 5, 4)
(13, NULL, '2025-10-03 11:30:00', 2, 1, 650, 60, 0, 0, 7, 710, '急需', 0, 0, 'POST334455', '2025-10-04 15:00:00', '張志豪', '0933-444555', 'member003@example.com', '401台中市北區學士路300號', '0'),
(13, NULL, '2025-10-05 09:00:00', 0, 0, 350, 60, 50, 0, 0, 360, '尚未轉帳', 1, 1, NULL, NULL, '張志豪', '0933-444555', 'member003@example.com', '401台中市北區學士路300號', '0'), -- 修正: 370 -> 350 (Item總和)；380 -> 360 (實付)

-- MEM_ID 14: 陳玉芬 (3 筆訂單 - FMEM_ID 1, 2, 5)
(14, NULL, '2025-10-06 14:30:00', 3, 1, 400, 60, 0, 0, 4, 460, NULL, 0, 1, 'FAMIE445566', '2025-10-08 11:00:00', '陳玉芬', '0944-555666', 'member004@example.com', '801高雄市前金區五福路88號', '0'),
(14, NULL, '2025-10-09 16:00:00', 1, 1, 300, 60, 0, 0, 3, 360, NULL, 1, 0, NULL, NULL, '陳玉芬', '0944-555666', 'member004@example.com', '801高雄市前金區五福路88號', '0'),
(14, NULL, '2025-10-10 10:00:00', 5, 1, 450, 60, 50, 0, 0, 460, '已退貨', 0, 0, 'SF11223344', '2025-10-12 15:00:00', '陳玉芬', '0944-555666', 'member004@example.com', '801高雄市前金區五福路88號', '0'),

-- MEM_ID 15: 林建宏 (3 筆訂單 - FMEM_ID 3, 4, 1)
(15, NULL, '2025-10-11 15:00:00', 2, 1, 400, 60, 0, 0, 5, 460, '請注意包裝', 0, 0, 'SF55667788', '2025-10-12 17:00:00', '林建宏', '0955-666777', 'member005@example.com', '700台南市中西區民生路199號', '0'), -- 修正: 500 -> 400 (Item總和)；560 -> 460 (實付)
(15, NULL, '2025-10-13 09:30:00', 3, 1, 600, 0, 100, 0, 4, 490, NULL, 1, 0, 'SF99001133', '2025-10-15 11:00:00', '林建宏', '0955-666777', 'member005@example.com', '700台南市中西區民生路199號', '0'), -- 修正: 590 -> 600 (Item總和)；490 -> 490 (實付 - 剛好正確)
(15, NULL, '2025-10-15 16:00:00', 4, 1, 1500, 60, 0, 0, 15, 1560, '已申請退貨', 0, 1, '711F001122', '2025-10-17 14:00:00', '林建宏', '0955-666777', 'member005@example.com', '700台南市中西區民生路199號', '0'),

-- MEM_ID 16: 曾雅婷 (2 筆訂單 - FMEM_ID 5, 2)
(16, NULL, '2025-10-16 10:30:00', 0, 0, 650, 60, 0, 0, 0, 710, NULL, 1, 0, NULL, NULL, '曾雅婷', '0966-777888', 'member006@example.com', '300新竹市東區光復路250號', '0'),
(16, NULL, '2025-10-17 12:00:00', 3, 1, 300, 60, 50, 0, 2, 310, NULL, 0, 1, 'FAMIG002233', '2025-10-19 10:00:00', '曾雅婷', '0966-777888', 'member006@example.com', '300新竹市東區光復路250號', '0'),

-- MEM_ID 17: 游信宏 (2 筆訂單 - FMEM_ID 3, 4)
(17, NULL, '2025-10-18 15:00:00', 2, 1, 500, 60, 0, 0, 5, 560, NULL, 0, 0, 'POST445566', '2025-10-19 18:00:00', '游信宏', '0977-888999', 'member007@example.com', '970花蓮縣花蓮市中正路10號', '0'),
(17, NULL, '2025-10-20 09:00:00', 1, 0, 240, 60, 0, 0, 0, 300, '待轉帳', 1, 1, NULL, NULL, '游信宏', '0977-888999', 'member007@example.com', '970花蓮縣花蓮市中正路10號', '0'),

-- MEM_ID 18: 洪詠欣 (2 筆訂單 - FMEM_ID 1, 5)
(18, NULL, '2025-10-21 11:00:00', 3, 1, 400, 60, 50, 0, 3, 410, NULL, 0, 1, '711H556677', '2025-10-23 11:00:00', '洪詠欣', '0988-999000', 'member008@example.com', '260宜蘭縣羅東鎮中山路一段88號', '0'),
(18, NULL, '2025-10-24 16:30:00', 0, 0, 450, 60, 0, 0, 0, 510, NULL, 1, 0, NULL, NULL, '洪詠欣', '0988-999000', 'member008@example.com', '260宜蘭縣羅東鎮中山路一段88號', '0'),

-- MEM_ID 19: 邱柏睿 (2 筆訂單 - FMEM_ID 2, 3)
(19, NULL, '2025-10-25 10:00:00', 3, 1, 300, 60, 0, 0, 3, 360, NULL, 0, 0, 'SF66778899', '2025-10-27 12:00:00', '邱柏睿', '0911-000222', 'member009@example.com', '600嘉義市西區垂楊路120號', '0'),
(19, NULL, '2025-10-26 14:00:00', 1, 0, 600, 60, 100, 0, 0, 560, NULL, 1, 1, NULL, NULL, '邱柏睿', '0911-000222', 'member009@example.com', '600嘉義市西區垂楊路120號', '0'),

-- MEM_ID 20: 簡心怡 (2 筆訂單 - FMEM_ID 4, 1)
(20, NULL, '2025-10-27 11:30:00', 2, 1, 600, 60, 0, 0, 6, 660, NULL, 1, 0, 'POST778890', '2025-10-28 15:00:00', '簡心怡', '0922-000333', 'member010@example.com', '540南投縣南投市中興路300號', '0'), -- 修正: 590 -> 600 (Item總和)；650 -> 660 (實付)
(20, NULL, '2025-10-28 09:00:00', 3, 1, 300, 60, 50, 0, 3, 310, NULL, 0, 1, '711I889900', '2025-10-30 11:00:00', '簡心怡', '0922-000333', 'member010@example.com', '540南投縣南投市中興路300號', '0'); -- 修正: 400 -> 300 (Item總和)；410 -> 310 (實付)
-- 刪除/建立 商城訂單明細
DROP TABLE IF EXISTS pro_order_item;
CREATE TABLE pro_order_item(
	pro_id INT NOT NULL,	-- PK,FK
	pro_ord_id INT NOT NULL, -- PK,FK
	pro_unitprice INT NOT NULL,
	pro_amount INT NOT NULL DEFAULT 1,
	pro_subtotal INT NOT NULL,
	CONSTRAINT pro_order_item_pro_ord_id_pro_ord_id_pk PRIMARY KEY (pro_id,pro_ord_id)
)ENGINE InnoDB;


INSERT INTO PRO_ORDER_ITEM (PRO_ID, PRO_ORD_ID, PRO_UNITPRICE, PRO_AMOUNT, PRO_SUBTOTAL) VALUES
-- -----------------------------------------------------------------------------------
-- MEM_ID 1 (FMEM_ID 1)
(1, 1, 400, 1, 400), (2, 1, 150, 1, 150), (11, 1, 200, 2, 400), -- 訂單 1: 950
(1, 2, 400, 1, 400), (11, 2, 200, 1, 200), -- 訂單 2: 600
(1, 3, 400, 1, 400), -- 訂單 3: 400

-- -----------------------------------------------------------------------------------
-- MEM_ID 2 (FMEM_ID 2)
(3, 4, 300, 1, 300), (4, 4, 250, 1, 250), -- 訂單 4: 550
(12, 5, 300, 1, 300), -- 訂單 5: 300
(3, 6, 300, 1, 300), (4, 6, 250, 2, 500), -- 訂單 6: 800 (總計 850)

-- -----------------------------------------------------------------------------------
-- MEM_ID 3 (FMEM_ID 3, 4, 5)
(5, 7, 500, 1, 500), (6, 7, 600, 1, 600), -- 訂單 7: 1100 (FMEM_ID 3)
(7, 8, 350, 1, 350), (8, 8, 120, 1, 120), (14, 8, 250, 0, 0), -- 訂單 8: 470 (總計 570) (FMEM_ID 4)
(9, 9, 450, 1, 450), -- 訂單 9: 450 (FMEM_ID 5)
(10, 10, 200, 3, 600), -- 訂單 10: 600 (FMEM_ID 5)

-- -----------------------------------------------------------------------------------
-- MEM_ID 4 (FMEM_ID 1, 3, 4)
(1, 11, 400, 3, 1200), -- 訂單 11: 1200 (FMEM_ID 1)
(5, 12, 500, 1, 500), (6, 12, 600, 0, 0), (13, 12, 400, 1, 400), -- 訂單 12: 900 (總計 800) (FMEM_ID 3)
(7, 13, 350, 1, 350), -- 訂單 13: 350 (總計 300) (FMEM_ID 4)

-- -----------------------------------------------------------------------------------
-- MEM_ID 5 (FMEM_ID 2, 3, 4)
(3, 14, 300, 2, 600), -- 訂單 14: 600 (FMEM_ID 2)
(5, 15, 500, 2, 1000), -- 訂單 15: 1000 (FMEM_ID 3)
(8, 16, 120, 2, 240), -- 訂單 16: 240 (FMEM_ID 4)

-- -----------------------------------------------------------------------------------
-- MEM_ID 6 (FMEM_ID 5, 2)
(9, 17, 450, 1, 450), -- 訂單 17: 450 (FMEM_ID 5)
(12, 18, 300, 1, 300), -- 訂單 18: 300 (FMEM_ID 2)

-- -----------------------------------------------------------------------------------
-- MEM_ID 7 (FMEM_ID 3, 1)
(5, 19, 500, 1, 500), -- 訂單 19: 500 (FMEM_ID 3)
(1, 20, 400, 2, 800), -- 訂單 20: 800 (FMEM_ID 1)

-- -----------------------------------------------------------------------------------
-- MEM_ID 8 (FMEM_ID 4, 3)
(7, 21, 350, 1, 350), (8, 21, 120, 0, 0), -- 訂單 21: 350 (總計 370) (FMEM_ID 4)
(5, 22, 500, 2, 1000), -- 訂單 22: 1000 (FMEM_ID 3)

-- -----------------------------------------------------------------------------------
-- MEM_ID 9 (FMEM_ID 1, 5, 4)
(1, 23, 400, 1, 400), (2, 23, 150, 0, 0), (11, 23, 200, 1, 200), -- 訂單 23: 600 (FMEM_ID 1)
(9, 24, 450, 0, 0), (10, 24, 200, 2, 400), -- 訂單 24: 400 (FMEM_ID 5)
(7, 25, 350, 1, 350), (8, 25, 120, 0, 0), -- 訂單 25: 350 (總計 370) (FMEM_ID 4)

-- -----------------------------------------------------------------------------------
-- MEM_ID 10 (FMEM_ID 2, 3)
(3, 26, 300, 1, 300), (4, 26, 250, 1, 250), -- 訂單 26: 550 (FMEM_ID 2)
(6, 27, 600, 0, 0), (13, 27, 400, 1, 400), (5, 27, 500, 0, 0), -- 訂單 27: 400 (總計 500) (FMEM_ID 3)

-- -----------------------------------------------------------------------------------
-- MEM_ID 11 (FMEM_ID 4, 5)
(8, 28, 120, 1, 120), (14, 28, 250, 1, 250), (7, 28, 350, 0, 0), -- 訂單 28: 370 (總計 590) (FMEM_ID 4)
(9, 29, 450, 1, 450), -- 訂單 29: 450 (FMEM_ID 5)

-- -----------------------------------------------------------------------------------
-- MEM_ID 12 (FMEM_ID 1, 2, 3)
(1, 30, 400, 1, 400), -- 訂單 30: 400 (FMEM_ID 1)
(3, 31, 300, 2, 600), -- 訂單 31: 600 (FMEM_ID 2)
(5, 32, 500, 1, 500), -- 訂單 32: 500 (FMEM_ID 3)

-- -----------------------------------------------------------------------------------
-- MEM_ID 13 (FMEM_ID 5, 4)
(9, 33, 450, 1, 450), (10, 33, 200, 1, 200), -- 訂單 33: 650 (FMEM_ID 5)
(7, 34, 350, 1, 350), (8, 34, 120, 0, 0), -- 訂單 34: 350 (總計 370) (FMEM_ID 4)

-- -----------------------------------------------------------------------------------
-- MEM_ID 14 (FMEM_ID 1, 2, 5)
(1, 35, 400, 1, 400), -- 訂單 35: 400 (FMEM_ID 1)
(12, 36, 300, 1, 300), -- 訂單 36: 300 (FMEM_ID 2)
(9, 37, 450, 1, 450), -- 訂單 37: 450 (FMEM_ID 5)

-- -----------------------------------------------------------------------------------
-- MEM_ID 15 (FMEM_ID 3, 4, 1)
(6, 38, 600, 0, 0), (13, 38, 400, 1, 400), (5, 38, 500, 0, 0), -- 訂單 38: 400 (總計 500) (FMEM_ID 3)
(7, 39, 350, 1, 350), (14, 39, 250, 1, 250), -- 訂單 39: 600 (總計 590) (FMEM_ID 4)
(1, 40, 400, 3, 1200), (2, 40, 150, 2, 300), -- 訂單 40: 1500 (FMEM_ID 1)

-- -----------------------------------------------------------------------------------
-- MEM_ID 16 (FMEM_ID 5, 2)
(9, 41, 450, 1, 450), (10, 41, 200, 1, 200), -- 訂單 41: 650 (FMEM_ID 5)
(3, 42, 300, 1, 300), -- 訂單 42: 300 (FMEM_ID 2)

-- -----------------------------------------------------------------------------------
-- MEM_ID 17 (FMEM_ID 3, 4)
(5, 43, 500, 1, 500), -- 訂單 43: 500 (FMEM_ID 3)
(8, 44, 120, 2, 240), -- 訂單 44: 240 (FMEM_ID 4)

-- -----------------------------------------------------------------------------------
-- MEM_ID 18 (FMEM_ID 1, 5)
(1, 45, 400, 1, 400), -- 訂單 45: 400 (FMEM_ID 1)
(9, 46, 450, 1, 450), -- 訂單 46: 450 (FMEM_ID 5)

-- -----------------------------------------------------------------------------------
-- MEM_ID 19 (FMEM_ID 2, 3)
(3, 47, 300, 1, 300), -- 訂單 47: 300 (FMEM_ID 2)
(6, 48, 600, 1, 600), -- 訂單 48: 600 (FMEM_ID 3)

-- -----------------------------------------------------------------------------------
-- MEM_ID 20 (FMEM_ID 4, 1)
(7, 49, 350, 1, 350), (14, 49, 250, 1, 250), -- 訂單 49: 600 (總計 590) (FMEM_ID 4)
(2, 50, 150, 2, 300) -- 訂單 50: 300 (總計 300) (FMEM_ID 1)
;

-- 刪除/建立 購物車
DROP TABLE IF EXISTS shopping_cart;
CREATE TABLE shopping_cart(
	mem_id INT NOT NULL, -- pk.fk
	pro_id INT NOT NULL, -- pk.fk
    cart_name VARCHAR(100) NOT NULL,
    cart_unitprice INT NOT NULL,
	cart_amount INT NOT NULL DEFAULT 1,
    cart_subtotal INT NOT NULL,
	CONSTRAINT shopping_cart_mem_id_pro_id_pk PRIMARY KEY (mem_id,pro_id)
)ENGINE InnoDB;
-- INSERT INTO shopping_cart (mem_id, pro_id, cart_name, cart_unitprice, cart_amount, cart_subtotal) VALUES



-- 刪除/建立 檢舉表單
DROP TABLE IF EXISTS pro_report;
CREATE TABLE pro_report(
	pro_rpt_id INT NOT NULL AUTO_INCREMENT,
	pro_id INT NOT NULL, -- FK
	mem_id INT NOT NULL, -- FK
	pro_rpt_status TINYINT NOT NULL,
	pro_rpt_at DATETIME NOT NULL,
	pro_rpt_title VARCHAR(50) NOT NULL,
	pro_rpt_cont VARCHAR(1000) NOT NULL,
	CONSTRAINT pro_report_pro_rpt_id_pk PRIMARY KEY (pro_rpt_id)
)ENGINE InnoDB;

INSERT INTO pro_report (pro_id, mem_id, pro_rpt_status, pro_rpt_at, pro_rpt_title, pro_rpt_cont) VALUES
(20, 15, 1, '2025-08-28 10:14:00', '商品描述不符', '購買的商品與網站上的圖片和描述差異太大，實際收到品質較差。'),
(1, 1, 3, '2025-08-29 14:30:00', '價格不合理', '該商品在其他地方價格遠低於此處，懷疑有不當抬價行為。'),
(10, 12, 0, '2025-09-01 09:00:00', '重複上架', '這項商品疑似被重複上架，請協助確認。'),
(7, 18, 2, '2025-09-02 16:20:00', '商品為仿冒品', '收到的商品標示不清，懷疑為非正版商品。'),
(13, 10, 1, '2025-09-03 11:45:00', '內容涉及不當言論', '商品介紹內容含有攻擊性或不雅文字，應予下架。');

-- 刪除/建立 商品評論
DROP TABLE IF EXISTS pro_com;
CREATE TABLE pro_com(
	pro_com_id INT NOT NULL AUTO_INCREMENT,
	pro_id INT NOT NULL, -- FK
	mem_id INT NOT NULL, -- FK
	pro_com_content VARCHAR(500) DEFAULT NULL,
	pro_com_time DATETIME DEFAULT NULL,
	pro_com_rate TINYINT NOT NULL,
    CONSTRAINT pro_com_pro_com_id_pk PRIMARY KEY (pro_com_id)
)ENGINE InnoDB;

INSERT INTO pro_com (pro_id, mem_id, pro_com_content, pro_com_time, pro_com_rate) VALUES
(1, 1, '高麗菜很新鮮，口感清脆，家人都說讚！', '2025-08-01 10:30:00', 5),
(1, 15, '包裝很完整，運送快速，服務非常好。', '2025-08-03 14:00:00', 4),
(1, 19, '好吃！下次會再回購。', '2025-08-05 18:20:00', 5),
(2, 2, '小黃瓜很翠綠，沒有農藥味，吃得很安心。', '2025-08-02 11:45:00', 5),
(2, 17, '品質很好，物超所值。', '2025-08-04 09:30:00', 4),
(3, 3, '蘋果香甜多汁，很適合打成果汁或直接吃。', '2025-08-03 16:10:00', 5),
(4, 4, '香蕉熟度剛好，甜而不膩，小朋友很喜歡。', '2025-08-05 12:55:00', 4),
(4, 1, '出貨速度快，包裝得很仔細，沒有碰撞。', '2025-08-06 17:30:00', 5),
(5, 5, '米飯粒粒分明，口感很好，煮出來的飯特別香。', '2025-08-06 15:00:00', 5),
(5, 12, '米質優良，會繼續支持。', '2025-08-08 08:00:00', 5),
(5, 13, '送貨很準時，包裝保護得很好。', '2025-08-09 11:20:00', 4),
(6, 6, '米飯香甜，CP值很高。', '2025-08-07 10:40:00', 4),
(7, 7, '茶葉香氣濃郁，泡出來的茶很回甘。', '2025-08-08 13:25:00', 5),
(7, 2, '送禮自用兩相宜。', '2025-08-09 15:00:00', 5),
(7, 10, '茶葉很耐泡，值得推薦。', '2025-08-10 18:00:00', 4),
(8, 8, '紅茶味道很棒，很順口。', '2025-08-09 16:50:00', 4),
(8, 20, '品質穩定，是日常的好選擇。', '2025-08-11 12:30:00', 5),
(9, 9, '香菇很厚實，香氣十足，煮湯很美味。', '2025-08-10 19:15:00', 5),
(9, 11, '乾燥香菇品質優良，泡發後很飽滿。', '2025-08-12 10:00:00', 5),
(10, 10, '杏鮑菇很新鮮，簡單料理就很好吃。', '2025-08-11 14:00:00', 4),
(10, 16, '菇類品質很棒，會再回購。', '2025-08-13 14:00:00', 4),
(11, 1, '龍眼蜜很純，有獨特的香氣。', '2025-08-12 11:30:00', 5),
(11, 4, '很棒的產品，值得支持小農。', '2025-08-14 11:00:00', 5),
(12, 2, '百花蜜味道豐富，很適合泡檸檬水。', '2025-08-13 16:00:00', 4),
(13, 3, '雞蛋品質很好，蛋黃很飽滿。', '2025-08-14 10:00:00', 5),
(13, 6, '放牧雞蛋吃得安心，價格也很實惠。', '2025-08-15 16:00:00', 5),
(13, 8, '蛋很新鮮，品質有保證。', '2025-08-16 11:00:00', 5),
(13, 11, '包裝很仔細，收到沒有破裂。', '2025-08-17 14:00:00', 4),
(14, 4, '紅蘿蔔很甜，沒有怪味，小朋友也愛吃。', '2025-08-15 13:00:00', 4),
(15, 5, '鳳梨香甜，果肉多汁，新鮮。', '2025-08-16 17:00:00', 5),
(16, 6, '糙米口感Q彈，很健康。', '2025-08-17 10:00:00', 4),
(16, 12, '米很香，品質很好。', '2025-08-18 13:00:00', 4),
(17, 7, '包種茶很清香，回沖很多次都還有味道。', '2025-08-18 15:30:00', 5),
(17, 19, '茶葉包裝很精美，送禮很有面子。', '2025-08-19 12:00:00', 5),
(18, 8, '黑木耳很脆，品質優良。', '2025-08-19 18:00:00', 4),
(19, 9, '芭樂很甜，口感清脆。', '2025-08-20 10:30:00', 4),
(19, 15, '新鮮又好吃，值得推薦。', '2025-08-21 15:00:00', 5),
(20, 10, '小番茄很甜，很像水果，當零食吃很棒。', '2025-08-21 11:00:00', 5),
(20, 18, '小番茄品質穩定，每次買都很好吃。', '2025-08-22 17:00:00', 5);

-- 刪除/建立 活動折價卷
DROP TABLE IF EXISTS act_cpn;
CREATE TABLE act_cpn (
    act_cpn_id INT NOT NULL AUTO_INCREMENT,
	cpn_source VARCHAR(20) NOT NULL DEFAULT 'COMMON'  COMMENT '折價券用途',
    cpn_name VARCHAR(50) NOT NULL,
    disc_type TINYINT NOT NULL COMMENT '0:滿額折抵,1: 百分比',
    disc_value DECIMAL(10,2) NOT NULL,
    min_spend INT,
    start_date DATE,
    valid_days INT,
    cpn_desc VARCHAR(200) COMMENT '折價券規則描述',
    is_active TINYINT NOT NULL COMMENT '0:未啟用,1:啟用',
    crt_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
 CONSTRAINT act_cpn_id_pk PRIMARY KEY (act_cpn_id)
) ENGINE=InnoDB;

INSERT INTO act_cpn 
(cpn_name, disc_type, disc_value, min_spend, start_date, valid_days, cpn_desc, is_active)
VALUES
-- 1️註冊會員：新客專屬 9 折券（無期限）
('新客專屬9折券', 1, 0.90, NULL, NULL, NULL, '新會員首次註冊即可獲得，無使用期限，全館適用', 1),

-- 2️註冊會員：新客專屬 折1000（無期限）
('新客專屬折1000', 0, 1000, NULL, NULL, NULL, '新會員註冊立即獲得，無使用期限，全館適用', 1),

-- 3️生日會員：生日當月 85 折券（30天內有效）
('生日85折券', 1, 0.85, NULL, NULL, 30, '生日當月發放，全館適用，30天內有效', 1),

-- 4️生日會員：生日當月折500（30天內有效）
('生日折500', 0, 500, NULL, NULL, 30, '生日當月發放，全館適用，30天內有效', 1);

-- 刪除/建立 活動折價卷持有者明細
DROP TABLE IF EXISTS mem_act_cpn;
CREATE TABLE mem_act_cpn (
    cpn_holder_detail_id INT NOT NULL AUTO_INCREMENT,
    act_cpn_id INT NOT NULL, -- FK
    mem_id INT NOT NULL,     -- FK
    -- reg_id INT NULL;      -- FK
    cpn_use_status TINYINT NOT NULL COMMENT '0:未使用,1:已使用,2:已過期',
    crt_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
    rcv_at DATETIME NOT NULL,-- 領券時間
    eff_start DATE NOT NULL, -- 實際生效起
    eff_end DATE,-- 實際失效止
    used_at DATETIME,
    CONSTRAINT cpn_holder_detail_id_pk PRIMARY KEY (cpn_holder_detail_id)
) ENGINE=InnoDB;

-- 活動折價券持有者明細
-- 新客專屬9折券 (無期限)
INSERT INTO mem_act_cpn (act_cpn_id, mem_id, cpn_use_status, rcv_at, eff_start, eff_end)
VALUES (1, 1, 0, NOW(), CURDATE(), NULL);

-- 新客專屬折1000 (無期限)
INSERT INTO mem_act_cpn (act_cpn_id, mem_id, cpn_use_status, rcv_at, eff_start, eff_end)
VALUES (2, 1, 0, NOW(), CURDATE(), NULL);

-- 生日85折券 (30天內有效)
INSERT INTO mem_act_cpn (act_cpn_id, mem_id, cpn_use_status, rcv_at, eff_start, eff_end)
VALUES (3, 1, 0, NOW(), CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY));

-- 生日折500 (30天內有效)
INSERT INTO mem_act_cpn (act_cpn_id, mem_id, cpn_use_status, rcv_at, eff_start, eff_end)
VALUES (4, 1, 0, NOW(), CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY));



-- 刪除/建立 活動訂單
DROP TABLE IF EXISTS reg;
CREATE TABLE `reg` (
  `reg_id` INT NOT NULL AUTO_INCREMENT,              -- 活動報名訂單編號 PK
  `reg_at` DATETIME NOT NULL,                        -- 下單時間
  `reg_stat` TINYINT NOT NULL DEFAULT 0,             -- 0 成立(已付款) 1 取消(待退款) 2 已退款 3 活動已完成 4 已撥款 5 已結案
  `reg_name` VARCHAR(30) NOT NULL,                   -- 聯絡人姓名
  `reg_mob` VARCHAR(20) NOT NULL,                    -- 聯絡人手機
  `reg_mail` VARCHAR(100) NOT NULL,                  -- 聯絡人 email
  `reg_count` INT NOT NULL DEFAULT 1,                -- 報名人數
  -- ------------------------------評論------------------------------------------
  `act_rate` TINYINT,                                -- 活動評分
  `act_comm` VARCHAR(500),                           -- 活動評論
  `act_commat` DATETIME,                             -- 活動評論時間
  `act_commreply` VARCHAR(500),                      -- 活動評論回覆
  -- -------------------------------fk------------------------------------------
  `ses_id` INT NOT NULL,                             -- 場次編號(FK)
  `mem_id` INT NOT NULL,                             -- 一般會員(FK)
  `cpn_holder_detail_id` INT,                        -- 折價券持有者明細(FK) (可為 NULL)
  -- -----------------------------金額計算-----------------------------------------
  `reg_total` INT NOT NULL,                          -- 訂單總金額
  `reg_pointdisc` INT NOT NULL DEFAULT 0,            -- 折抵點數
  `reg_pointget` INT NOT NULL DEFAULT 0,             -- 回饋點數
  `reg_cpndisc` INT NOT NULL DEFAULT 0,              -- 折價券折抵
  `reg_grand_total` INT NOT NULL,                    -- 實付金額
  PRIMARY KEY (`reg_id`)
);

INSERT INTO reg
(ses_id, mem_id, reg_at, reg_count,
 reg_total, reg_cpndisc, reg_pointdisc, reg_grand_total, reg_pointget,
 reg_stat, reg_name, reg_mob, reg_mail,
 act_comm, act_rate, act_commat, act_commreply, cpn_holder_detail_id)
VALUES
-- ===== 前 6 筆 mem_id = 1 (reg_id=1~6) =====
(1,1,'2025-10-10 09:10:00',2, 3000,200,100,2700,27, 0,'王小明','0912-000-001','user1@example.com',
  NULL,NULL,NULL,NULL,NULL),
(2,1,'2025-10-10 10:20:00',1, 1500,  0,  0,1500,15, 1,'王小明','0912-000-001','user1@example.com',
  NULL,NULL,NULL,NULL,NULL),
(3,1,'2025-10-10 11:30:00',3, 4500,300,  0,4200,42, 2,'王小明','0912-000-001','user1@example.com',
  NULL,NULL,NULL,NULL,NULL),
-- reg_id = 4 (完成/有評論+回覆)
(4,1,'2025-10-10 12:40:00',2, 3000,  0,200,2800,28, 3,'王小明','0912-000-001','user1@example.com',
  '導覽很用心，流程順暢，家人都玩得很開心！',5,'2025-10-12 09:00:00','感謝支持～歡迎再來！',NULL),
-- reg_id = 5 (已撥款/有評論)
(5,1,'2025-10-10 13:50:00',1, 1500,100,100,1300,13, 4,'王小明','0912-000-001','user1@example.com',
  '整體不錯，但集合地點指示可再清楚一些。',4,'2025-10-12 10:15:00',NULL,NULL),
-- reg_id = 6 展示用
 (1,1,'2025-10-09 12:30:00',2, 3000,  0,  0,3000,30, 3,'王小明','0922-000-002','user2@example.com',
  NULL,NULL,'2025-10-11 16:20:00',NULL,NULL),

-- ===== 其餘 14 筆 (reg_id=7~20) =====
(1,2,'2025-10-09 09:00:00',1, 1500,  0,  0,1500,15, 0,'李小華','0922-000-002','user2@example.com',
  NULL,NULL,NULL,NULL,NULL),
(1,2,'2025-10-09 10:10:00',2, 3000,150,  0,2850,28, 1,'李小華','0922-000-002','user2@example.com',
  NULL,NULL,NULL,NULL,NULL),
(1,2,'2025-10-09 11:20:00',3, 4500,  0,300,4200,42, 2,'李小華','0922-000-002','user2@example.com',
  NULL,NULL,NULL,NULL,NULL),
-- reg_id = 10 (完成/有評論)
(1,2,'2025-10-09 12:30:00',2, 3000,  0,  0,3000,30, 3,'李小華','0922-000-002','user2@example.com',
  '講解專業，時間安排剛好。',4,'2025-10-11 16:20:00',NULL,NULL),

-- reg_id = 11 (已撥款/有評論+回覆)
(4,3,'2025-10-08 09:00:00',1, 1500,  0,  0,1500,15, 4,'張偉','0933-000-003','user3@example.com',
  '活動還不錯，但人有點多，等候時間稍長。',3,'2025-10-11 18:05:00','收到建議，後續會分流控管人數，謝謝反饋！',NULL),
-- reg_id = 12 (已結案/有評論)
(5,3,'2025-10-08 10:15:00',2, 3000,100,  0,2900,29, 5,'張偉','0933-000-003','user3@example.com',
  '小農很熱情，帶回家的產品品質很好！',5,'2025-10-11 19:10:00',NULL,NULL),

(6,4,'2025-10-07 08:20:00',4, 6000,  0,200,5800,58, 0,'林小姐','0955-000-004','user4@example.com',
  NULL,NULL,NULL,NULL,NULL),
(7,4,'2025-10-07 09:30:00',1, 1500, 50, 50,1400,14, 1,'林小姐','0955-000-004','user4@example.com',
  NULL,NULL,NULL,NULL,NULL),
(1,4,'2025-10-07 10:40:00',2, 3000,  0,  0,3000,30, 2,'林小姐','0955-000-004','user4@example.com',
  NULL,NULL,NULL,NULL,NULL),

-- reg_id = 16 (完成/有評論+回覆)
(2,5,'2025-10-06 14:00:00',3, 4500,200,  0,4300,43, 3,'黃同學','0966-000-005','user5@example.com',
  '親子友善，孩子們超愛體驗環節～',4,'2025-10-10 17:40:00','謝謝分享～下次會新增更多親子關卡！',NULL),
-- reg_id = 17 (已撥款/有評論+回覆)
(3,5,'2025-10-06 15:10:00',2, 3000,  0,100,2900,29, 4,'黃同學','0966-000-005','user5@example.com',
  '臨時改期通知較晚，行程有受影響。',2,'2025-10-10 18:30:00','抱歉造成不便，我們已調整通知流程並提供補償方案。',NULL),

-- reg_id = 18 (已結案/有評論)
(4,6,'2025-10-05 16:20:00',1, 1500,  0,  0,1500,15, 5,'趙先生','0977-000-006','user6@example.com',
  '風景漂亮、餐點好吃，整體大推！',5,'2025-10-10 20:00:00',NULL,NULL),
(5,6,'2025-10-05 17:30:00',4, 6000,300,200,5500,55, 0,'趙先生','0977-000-006','user6@example.com',
  NULL,NULL,NULL,NULL,NULL),
(6,6,'2025-10-05 18:40:00',2, 3000,  0,  0,3000,30, 1,'趙先生','0977-000-006','user6@example.com',
  NULL,NULL,NULL,NULL,NULL),
(7,6,'2025-10-05 19:50:00',3, 4500,  0,300,4200,42, 2,'趙先生','0977-000-006','user6@example.com',
  NULL,NULL,NULL,NULL,NULL);





-- 刪除/建立 商品收藏清單
DROP TABLE IF EXISTS favo_pro;
CREATE TABLE favo_pro (
    mem_id INT,  -- PK,FK
    pro_id INT,  -- PK,FK
    CONSTRAINT favo_pro_pk PRIMARY KEY (mem_id, pro_id)
) ENGINE=InnoDB;

INSERT INTO favo_pro (mem_id, pro_id) VALUES
(1, 1),   -- 會員1 收藏 商品1
(2, 3),   -- 會員2 收藏 商品3
(3, 5),   -- 會員3 收藏 商品5
(4, 7),   -- 會員4 收藏 商品7
(5, 9),   -- 會員5 收藏 商品9
(6, 2),   -- 會員6 收藏 商品2
(7, 4),   -- 會員7 收藏 商品4
(8, 6),   -- 會員8 收藏 商品6
(9, 8),   -- 會員9 收藏 商品8
(10, 10); -- 會員10 收藏 商品10


-- 刪除/建立 活動收藏清單

DROP TABLE IF EXISTS favo_act;
CREATE TABLE favo_act (
    mem_id INT,  -- PK,FK
    act_id INT,  -- PK,FK
    CONSTRAINT favo_act_pk PRIMARY KEY (mem_id, act_id)
) ENGINE=InnoDB;




-- step 3. 
-- (3-1) 最新消息清單

-- (3-2) 常見QA清單

-- (3-2) 管理員職稱表 -> 管理員（FK職稱編號） -> 功能權限 -> 角色權限（FK職稱編號）（FK權限編號）


-- 最新消息清單
CREATE TABLE news (
    news_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    fmem_id INT NULL,
    news_title VARCHAR(50) NOT NULL,
    news_cont VARCHAR(1000) NOT NULL,
    news_at DATETIME NOT NULL,
    news_status INT NOT NULL,

    CONSTRAINT fk_news_fmem FOREIGN KEY (fmem_id) REFERENCES fmem(fmem_id)
);

INSERT INTO news (news_title, news_cont, news_at, news_status) VALUES
('網站更新', '我們的網站已經更新至最新版本，提供更好的使用者體驗。', '2024-05-20 10:30:00', 0),
('夏季特賣會', '所有商品8折優惠，只到月底！', '2024-06-01 15:00:00', 1);


-- 常見QA清單
CREATE TABLE qa_list (
    qa_id INT NOT NULL AUTO_INCREMENT COMMENT 'QA編號',
    qa_title VARCHAR(50) NULL COMMENT 'QA標題',
    qa_cont VARCHAR(500) NULL COMMENT 'QA內容',
    PRIMARY KEY (qa_id)
) COMMENT='常見QA清單';

INSERT INTO qa_list (qa_title, qa_cont) VALUES
('訂購後多久可以收到商品？', '我們會在確認訂單後的1-2個工作天內為您出貨。一般來說，北部地區約需1-2天，中南部地區約需2-3天送達。為確保品質，所有新鮮蔬果均採用冷藏配送。'),
('請問商品的產地是哪裡？', '我們網站上的所有農產品均來自與我們合作的台灣在地小農。您可以在每個商品頁面下方找到詳細的產地資訊以及農友介紹，讓您買得安心，吃得健康！'),
('網站提供哪些付款方式？', '我們目前提供線上信用卡付款 (支援 VISA, Mastercard, JCB)、網路ATM轉帳以及貨到付款三種方式，方便您選擇最適合的付款方式。'),
('如果收到的水果有損傷怎麼辦？', '若您收到的商品有任何損傷或品質問題，請務必在24小時內拍照並透過客服信箱或官方LINE與我們聯繫。我們將盡速為您處理退款或補寄事宜，保障您的權益。'),
('如何報名農事體驗活動？', '您可以在「活動體驗」專區瀏覽所有行程，選擇您有興趣的活動並完成線上付款即可。若活動當日因天候不佳而取消，我們將會主動與您聯繫，您可以選擇全額退款或改期參加。');
-- 管理員職稱表
CREATE TABLE admin_type (
	admin_type_id INT NOT NULL PRIMARY KEY,
	admin_type_name VARCHAR(20) NOT NULL
);
INSERT INTO admin_type (admin_type_id, admin_type_name) VALUES
(1, '超級管理員'),
(2, '中級管理員'),
(3, '低級人員');

-- 管理員
CREATE TABLE administrator (
	admin_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	admin_acc VARCHAR(20) NOT NULL,
	admin_pwd VARCHAR(20) NOT NULL,
	admin_status TINYINT NOT NULL DEFAULT 0,
	admin_name VARCHAR(20) NOT NULL,
	admin_email VARCHAR(40) NOT NULL,
	admin_mobile VARCHAR(11) NOT NULL,
	admin_pic LONGBLOB,
	admin_type_id INT NOT NULL  -- FK測試用先放 --
);
	
INSERT INTO administrator (admin_type_id, admin_acc, admin_pwd, admin_status, admin_name, admin_email, admin_mobile) VALUES
(1, 'admin1', 'pwd123', 1, '張三', 'admin1@email.com', '0912345678'),
(2, 'admin2', 'pwd456', 1, '李四', 'admin2@email.com', '0912345679'),
(3, 'admin3', 'pwd789', 0, '王五', 'admin3@email.com', '0912345680');

-- 功能權限
CREATE TABLE admin_function (
	admin_func_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	admin_func_name VARCHAR(20) NOT NULL,
	admin_func_des VARCHAR(100) NOT NULL
);
	
INSERT INTO admin_function (admin_func_id, admin_func_name, admin_func_des) VALUES
(1, '網站總管理', '商品活動廣告上架、下架、編輯，管理員管理'),
(2, '商城管理', '查看、處理商城商品'),
(3, '活動管理', '查看、管理活動資料'),
(4, '廣告管理', '查看、管理商城廣告活動資料'),
(5, '金流管理', '查看、管理商城金流資料'),
(6, '會員管理', '查看、管理商城會員資料'),
(7, '折價券管理', '查看、管理商城折價券資料'),
(8, '最新消息管理', '查看、管理最新消息資料'),
(9, 'QA管理', '查看、管理商城QA資料');


-- 角色權限
CREATE TABLE admin_type_func_list (
	admin_type_id INT NOT NULL,  -- FK測試用先放 --
	admin_func_id INT NOT NULL,  -- FK測試用先放 --
	PRIMARY KEY (admin_type_id, admin_func_id)
);

INSERT INTO admin_type_func_list (admin_type_id, admin_func_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 1),
(2, 2),
(3, 2);




-- step1.
-- 建立 FK
-- 小農會員 FK 商店樣式編號
ALTER TABLE fmem
ADD CONSTRAINT fmem_styno_fk FOREIGN KEY (sty_no) REFERENCES sty(sty_no);

-- 小農商品 FK 商品類別編號
-- 小農商品 FK 小農編號
ALTER TABLE product
ADD CONSTRAINT pro_pro_cate_id_fk FOREIGN KEY (pro_cate_id) REFERENCES product_category(pro_cate_id),
ADD CONSTRAINT pro_fmem_id_fk FOREIGN KEY (fmem_id) REFERENCES fmem(fmem_id);

-- 商品圖片 FK 商品編號
ALTER TABLE product_image
ADD CONSTRAINT product_image_product_fk FOREIGN KEY (pro_id) REFERENCES product(pro_id);

-- 商城廣告 FK 商品編號
-- 商城廣告 FK 小農會員編號
ALTER TABLE pro_ad
ADD CONSTRAINT pro_ad_product_FK FOREIGN KEY (pro_id) REFERENCES product(pro_id),
ADD CONSTRAINT pro_ad_fmem_ID_FK FOREIGN KEY (fmem_id) REFERENCES fmem(fmem_id);


-- step 2. FK
-- (2-1) 一般會員
-- (2-2) 商品折價卷持有者明細（FK商品折價卷編號）（FK一般會員編號）
ALTER TABLE mem_pro_cpn
ADD CONSTRAINT mem_pro_cpn_fk FOREIGN KEY (pro_cpn_id) REFERENCES pro_cpn(pro_cpn_id),
ADD CONSTRAINT mem_pro_cpn_mem_fk FOREIGN KEY (mem_id) REFERENCES mem(mem_id);


-- (2-3) 
-- 商城訂單（FK一般會員編號）（FK商品折價卷持有者流水號）
ALTER TABLE pro_order
ADD CONSTRAINT pro_order_mem_fk FOREIGN KEY (mem_id) REFERENCES mem(mem_id),
ADD CONSTRAINT pro_order_mem_pro_cpn_fk FOREIGN KEY (cpn_holder_detail_id) REFERENCES mem_pro_cpn(cpn_holder_detail_id);
-- 商城訂單明細（FK訂單編號）（FK商品編號）
ALTER TABLE pro_order_item
ADD CONSTRAINT pro_order_item_product_fk FOREIGN KEY (pro_id) REFERENCES product(pro_id),
ADD CONSTRAINT pro_order_item_pro_order_fk FOREIGN KEY (pro_ord_id) REFERENCES pro_order(pro_ord_id);


-- (2-4) 
-- 購物車（FK一般會員編號）（FK商品編號）
ALTER TABLE shopping_cart
ADD CONSTRAINT shopping_cart_mem_fk FOREIGN KEY (mem_id) REFERENCES mem(mem_id),
ADD CONSTRAINT shopping_cart_product_fk FOREIGN KEY (pro_id) REFERENCES product(pro_id);
-- 檢舉表單(FK商品編號)(FK一般會員編號)
ALTER TABLE pro_report
ADD CONSTRAINT pro_report_product_fk FOREIGN KEY (pro_id) REFERENCES product(pro_id),
ADD CONSTRAINT pro_report_mem_fk FOREIGN KEY (mem_id) REFERENCES mem(mem_id);
-- 商品評論（FK商品編號）（FK一般會員編號）
ALTER TABLE pro_com
ADD CONSTRAINT pro_com_product_fk FOREIGN KEY (pro_id) REFERENCES product(pro_id),
ADD CONSTRAINT pro_com_mem_fk FOREIGN KEY (mem_id) REFERENCES mem(mem_id);


-- (2-5) 活動折價卷
-- 活動折價卷持有者明細（FK活動折價卷編號）（FK一般會員編號）
ALTER TABLE mem_act_cpn
ADD CONSTRAINT mem_act_cpn_fk FOREIGN KEY (act_cpn_id) REFERENCES act_cpn(act_cpn_id),
ADD CONSTRAINT mem_act_cpn_mem_fk FOREIGN KEY (mem_id) REFERENCES mem(mem_id);
-- ADD CONSTRAINT mem_act_cpn_reg_fk FOREIGN KEY (reg_id) REFERENCES reg(reg_id);

-- (2-6) 
-- 報名訂單（FK場次編號）（FK一般會員編號）（FK活動折價卷持有者流水號）
-- 報名訂單 FK 場次編號
-- 報名訂單 FK 一般會員編號
-- 報名訂單 FK 折價券持有者明細（可為 NULL）
ALTER TABLE reg
  ADD CONSTRAINT reg_ses_FK  FOREIGN KEY (ses_id)  REFERENCES ses(ses_id),
  ADD CONSTRAINT reg_mem_FK  FOREIGN KEY (mem_id)  REFERENCES mem(mem_id),
  ADD CONSTRAINT reg_cpn_holder_detail_FK
      FOREIGN KEY (cpn_holder_detail_id) REFERENCES mem_act_cpn(cpn_holder_detail_id);



-- (2-7) 
-- 商品收藏清單（FK一般會員編號）（FK商品編號） 
ALTER TABLE favo_pro 
ADD CONSTRAINT favo_pro_mem_fk FOREIGN KEY (mem_id) REFERENCES mem(mem_id) ON DELETE CASCADE,
ADD CONSTRAINT favo_pro_pro_fk FOREIGN KEY (pro_id) REFERENCES product(pro_id) ON DELETE CASCADE;

-- 活動收藏清單（FK一般會員編號）（FK活動編號）
ALTER TABLE favo_act  -- 家慶
ADD CONSTRAINT favo_act_mem_fk FOREIGN KEY (mem_id) REFERENCES mem(mem_id) ON DELETE CASCADE,
ADD CONSTRAINT favo_act_act_fk FOREIGN KEY (act_id) REFERENCES act(act_id) ON DELETE CASCADE;


-- (3-2) 管理員職稱表 -> 
-- 管理員（FK職稱編號） -> 功能權限 -> 角色權限（FK職稱編號）（FK權限編號）
ALTER TABLE administrator
ADD CONSTRAINT admin_type_id_fk
FOREIGN KEY (admin_type_id) REFERENCES admin_type(admin_type_id);

ALTER TABLE admin_type_func_list
ADD CONSTRAINT atfl_admin_type_id_fk
FOREIGN KEY (admin_type_id) REFERENCES admin_type(admin_type_id);

ALTER TABLE admin_type_func_list
ADD CONSTRAINT atfl_admin_func_id_fk
FOREIGN KEY (admin_func_id) REFERENCES admin_function(admin_func_id);

-- 活動廣告 FK 活動編號
-- 活動廣告 FK 小農會員編號
ALTER TABLE act_ad
ADD CONSTRAINT act_ad_act_FK FOREIGN KEY (act_id) REFERENCES act(act_id),
ADD CONSTRAINT act_ad_fmem_ID_FK FOREIGN KEY (fmem_id) REFERENCES fmem(fmem_id);




-- 動態抓相關資料進資料庫內
SET SQL_SAFE_UPDATES = 0;
UPDATE ses s
SET s.headcount = (
	SELECT COALESCE(SUM(r.reg_count), 0)
	FROM reg r
	WHERE r.ses_id = s.ses_id
		AND r.reg_stat IN (0, 3, 4, 5) 
);
SET SQL_SAFE_UPDATES = 1;

SET SQL_SAFE_UPDATES = 0;
UPDATE act a
SET a.act_score = (
    SELECT COALESCE(SUM(r.act_rate), 0)
    FROM reg r
    JOIN ses s ON r.ses_id = s.ses_id
    WHERE s.act_id = a.act_id
		AND r.act_rate IS NOT NULL
		AND r.reg_stat IN (3, 4, 5)
);

SET SQL_SAFE_UPDATES = 1;

SET SQL_SAFE_UPDATES = 0; -- 允許無 WHERE 條件的 UPDATE

-- 更新 Act 表格中的 act_cnt 欄位
UPDATE act a
SET a.act_cnt = (
	SELECT COUNT(r.reg_id)
	FROM reg r
	JOIN ses s ON r.ses_id = s.ses_id
	WHERE s.act_id = a.act_id
		AND r.act_rate IS NOT NULL
		AND r.reg_stat IN (3, 4, 5)
);

SET SQL_SAFE_UPDATES = 1;