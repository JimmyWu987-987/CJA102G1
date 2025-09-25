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
    sty_css_path varchar(300) NOT NULL
);

INSERT INTO sty (sty_css_path) VALUES
('style#1'), ('style#2'), ('style#3');

-- 刪除/建立 小農會員
DROP TABLE IF EXISTS fmem;
CREATE TABLE fmem (
	fmem_id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
	f_id varchar(10) NOT NULL,
	fmem_acc varchar(40) NOT NULL,
	fmem_pwd varchar(20) NOT NULL,
	acc_status tinyint NOT NULL DEFAULT 0,
	acc_desc varchar(200) DEFAULT NULL,
	fmem_name varchar(20) NOT NULL,
	fmem_mobile varchar(11) NOT NULL,
	fmem_tel varchar(11) DEFAULT NULL,
	fmem_email varchar(254) NOT NULL,
	fmem_zipcode varchar(6) NOT NULL,
	fmem_city varchar(5) NOT NULL,
	fmem_dist varchar(5) NOT NULL,
	fmem_addr varchar(40) NOT NULL,
	bank_code varchar(5) NOT NULL,
	bank_acc varchar(20) NOT NULL,
	reg_date datetime NOT NULL,
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
	prod_fee int DEFAULT NULL
);

INSERT INTO fmem (f_id, fmem_acc, fmem_pwd, acc_status, acc_desc, fmem_name, fmem_mobile, fmem_tel, fmem_email,
				   fmem_zipcode, fmem_city, fmem_dist, fmem_addr,  bank_code, bank_acc, reg_date, 
				   certi_status, fmem_pic, organic_pic, land_pic, insur_pic, store_pic, store_name, store_intro, sty_no, 
				   mkt_score, mkt_cnt, act_score, act_cnt, rpt_cnt, prod_fee) VALUES
('H237230756', 'user001', 'pwd12345', 0, NULL, '王小明', '0912-345678', NULL, 'user001@example.com', '101', '台北市', '中正區', '仁愛路一段100號', '004', '1234567890123456', '2024-05-01 10:00:00', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL),
('A182893231', 'user002', 'pwd12345', 0, NULL, '林小美', '0922-333444', NULL, 'user002@example.com', '102', '新北市', '板橋區', '文化路200號', '822', '2233445566778899', '2024-05-02 11:00:00', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL),
('A119254857', 'user003', 'pwd12345', 0, NULL, '陳大華', '0933-445566', NULL, 'user003@example.com', '103', '台中市', '西屯區', '市政路300號', '700', '3344556677889900', '2024-05-03 12:00:00', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL),
('E118270271', 'user004', 'pwd12345', 0, NULL, '張美麗', '0955-667788', NULL, 'user004@example.com', '104', '高雄市', '苓雅區', '光華路88號', '012', '4455667788990011', '2024-05-04 13:00:00', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL),
('F133927325', 'user005', 'pwd12345', 0, NULL, '吳志強', '0966-778899', NULL, 'user005@example.com', '221', '台南市', '東區', '東門路199號', '005', '5566778899001122', '2024-05-05 14:00:00', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL),
('J179726256', 'user006', 'pwd12345', 0, NULL, '葉志豪', '0977-889900', NULL, 'user006@example.com', '106', '新竹市', '東區', '關新路300號', '822', '6677889900112233', '2024-05-06 15:00:00', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL),
('L158944504', 'user007', 'pwd12345', 0, NULL, '簡文君', '0911-222333', NULL, 'user007@example.com', '107', '基隆市', '仁愛區', '忠孝路18號', '004', '7788990011223344', '2024-05-07 16:00:00', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL),
('M160270421', 'user008', 'pwd12345', 0, NULL, '朱庭瑜', '0933-777888', NULL, 'user008@example.com', '108', '桃園市', '中壢區', '中山路350號', '012', '8899001122334455', '2024-05-08 17:00:00', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL),
('N108676213', 'user009', 'pwd12345', 0, NULL, '劉家豪', '0922-111333', NULL, 'user009@example.com', '109', '宜蘭縣', '宜蘭市', '民權路68號', '700', '9900112233445566', '2024-05-09 18:00:00', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL),
('Q193833164', 'user010', 'pwd12345', 0, NULL, '黃靜怡', '0966-111222', NULL, 'user010@example.com', '100', '花蓮縣', '花蓮市', '和平路88號', '005', '0011223344556677', '2024-05-10 19:00:00', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL);


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
	fmem_id INT,   -- FK測試用先放 --
	pro_cate_id INT  -- FK測試用先放 --
) ENGINE InnoDB;

INSERT INTO product (pro_name, pro_stock, fmem_id, pro_price, pro_status, pro_cate_id, pro_score, pro_cnt, pro_from) VALUES
('有機高麗菜', 50, 1, 80, 1, 1, 5, 250, '屏東'),
('無毒小黃瓜', 30, 2, 60, 1, 1, 4, 180, '彰化'),
('富士蘋果', 120, 3, 150, 1, 2, 5, 300, '富士'),
('台灣香蕉', 85, 4, 45, 1, 2, 4, 210, '屏東'),
('台東池上米', 200, 5, 300, 1, 3, 5, 500, '台東'),
('花蓮富里米', 180, 6, 280, 1, 3, 4, 450, '花蓮'),
('高山烏龍茶', 45, 7, 500, 1, 4, 5, 150, '南投'),
('金萱紅茶', 60, 8, 350, 1, 4, 4, 120, '南投'),
('日曬香菇', 90, 9, 220, 1, 5, 5, 200, '雲林'),
('新鮮杏鮑菇', 75, 10, 85, 1, 5, 4, 190, '苗栗'),
('龍眼蜜', 110, 1, 450, 1, 6, 5, 100, '台中'),
('百花蜜', 95, 2, 400, 1, 6, 4, 90, '台中'),
('放牧雞蛋', 250, 3, 120, 1, 7, 5, 350, '台南'),
('有機紅蘿蔔', 70, 4, 75, 1, 1, 4, 160, '雲林'),
('香水鳳梨', 150, 5, 90, 1, 2, 5, 280, '宜蘭'),
('有機糙米', 190, 6, 250, 1, 3, 4, 420, '花蓮'),
('文山包種茶', 55, 7, 480, 1, 4, 5, 130, '台中'),
('黑木耳', 80, 8, 65, 1, 5, 4, 210, '新竹'),
('紅心芭樂', 100, 9, 50, 1, 2, 4, 230, '高雄'),
('在地小番茄', 140, 10, 70, 1, 2, 5, 260, '桃園');


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
	pro_ad_revid int NOT NULL AUTO_INCREMENT,
    pro_id int NOT NULL, -- FK
    fmem_id int NOT NULL, -- FK
    pro_ad_img longblob,
    pro_ad_revstat tinyint,
    pro_ad_revupd datetime,
    pro_ad_revremark varchar(100),
	pro_ad_launstat tinyint,
    pro_ad_launupd datetime,
    pro_ad_start date,
    pro_ad_end date,
    pro_ad_fee int,
    pro_ad_fee_end date,
    CONSTRAINT pro_ad_pro_ad_revid_key PRIMARY KEY (pro_ad_revid)

) ENGINE InnoDB;

INSERT INTO pro_ad (
	pro_id,
	fmem_id,
	pro_ad_img,
	pro_ad_revstat,
	pro_ad_revupd,
	pro_ad_revremark,
	pro_ad_launstat,
	pro_ad_launupd,
	pro_ad_start,
	pro_ad_end,
	pro_ad_fee,
	pro_ad_fee_end
) VALUES
(1, 1, NULL, 1, '2025-08-01 10:00:00', '待審核（編輯中）', NULL, NULL, '2025-09-01', '2025-09-30', 500, '2025-08-25'), -- 待審核（編輯中）
(5, 2, NULL, 2, '2025-08-05 15:30:00', '審核通過（已下架，待上架）', 0, '2025-08-05 15:30:00', '2025-09-10', '2025-10-10', 800, '2025-09-05'), -- 審核通過（已下架，待上架）
(12, 3, NULL, 3, '2025-08-08 11:20:00', '審核未過（缺件）,圖片尺寸不符', NULL, NULL, '2025-09-15', '2025-10-15', 750, '2025-09-10'), -- 審核未過（缺件）
(8, 4, NULL, 4, '2025-08-10 14:00:00', '待繳費', NULL, NULL, '2025-09-20', '2025-10-20', 1200, '2025-09-15'), -- 待繳費
(15, 5, NULL, 5, '2025-08-12 09:00:00', '已繳費（已上架）', 1, '2025-08-15 09:00:00', '2025-08-15', '2025-09-15', 1000, '2025-08-12'), -- 已繳費（已上架）
(20, 1, NULL, 2, '2025-08-13 16:00:00', '審核通過（已上架）', 1, '2025-08-14 10:00:00', '2025-08-14', '2025-09-14', 900, '2025-08-10'), -- 審核通過（已上架）
(4, 2, NULL, 1, '2025-08-15 08:30:00', '待審核', NULL, NULL, '2025-10-01', '2025-11-01', 600, '2025-09-25'), -- 待審核
(9, 3, NULL, 3, '2025-08-18 10:45:00', '審核未過（內容不符）廣告內容與商品不符', NULL, NULL, '2025-10-05', '2025-11-05', 700, '2025-09-30'), -- 審核未過（內容不符）
(11, 4, NULL, 5, '2025-08-20 11:00:00', '已上架', 1, '2025-08-20 12:00:00', '2025-08-20', '2025-09-20', 1100, '2025-08-18'), -- 已上架
(18, 5, NULL, 0, '2025-08-22 14:30:00', '編輯中', NULL, NULL, '2025-10-10', '2025-11-10', 950, '2025-10-05'); -- 編輯中


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
 act_revstat		tinyint default 0,
 act_revupd			datetime not null,
 act_revremark		varchar(1000),
 act_launstat		tinyint,
 act_launupd		datetime,
 fmem_id			int not null,
 act_score			int,
 act_cnt			int,
 constraint act_fmem_id_fk foreign key (fmem_id) references fmem (fmem_id),
 constraint act_act_id_pk primary key (act_id));

insert into act values
  /*到目前都正常上架&有人評價過*/
 (null, '下田去！一日小農體驗', '2025-07-01','2025-12-30',
 '捲起袖子、赤腳踩在田裡，親手插秧、採收蔬果，感受最真實的農村日常。',
 200, 2, '2025-05-10 10:20:30', null, 1, '2025-05-15 09:20:30', 1, 101, 23),
 
 /*審核未過*/
 (null, '從產地到餐桌的秘密', '2025-10-25','2026-03-31',
 '透過遊戲與教學，讓大小朋友了解食材來源，培養珍惜食物的心。',
 10000, 3, '2025-08-26 08:20:00', '報名費用有疑慮，請再次確認。', null, null, 3, null, null),
 
 /*到目前都正常上架&有人評價過*/
 (null, '小小牧場', '2025-03-15','2025-10-31',
 '餵小羊、抱兔子，近距離接觸可愛動物，體驗牧場生活樂趣。',
 399, 2, '2025-01-10 14:10:30', null, 1, '2025-01-15 16:00:30', 2, 168, 38),
 
 /*有人評價過此活動, 此活動已結束並下架*/
 (null, '藍染工藝體驗課程', '2024-12-01','2025-06-10',
 '親手體驗藍染工藝，學習天然染色技巧，創作獨一無二的布藝作品。',
 700, 2, '2024-10-27 19:10:30', '已修正金額，審核通過', 0, '2025-06-11 00:00:00', 2, 666, 150),
 
 /*審核已通過但還沒上架*/
 (null, '小村莊的故事之旅', '2025-10-01','2026-02-28',
 '在導覽老師帶領下，認識農村的歷史、風俗與文化典故。',
 299, 2, '2025-08-28 10:20:30', null, 0, null, 1, null, null);



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
 constraint actimg_act_id_fk foreign key (act_id) references act (act_id),
 constraint actimg_actimg_id_pk primary key (actimg_id));

insert into actimg values (null, null, 1), (null, null, 1), (null, null, 1),
						  (null, null, 3), (null, null, 3);
                          
                          
-- 刪除/建立 場次
drop table if exists ses;
create table ses (
 ses_id				int not null auto_increment,
 ses_date			date not null,
 ses_start			time not null,
 ses_end			time not null,
 reg_start			date not null,
 reg_end			date not null,
 minppl				int not null default 1,
 maxppl				int not null,
 ses_fee			int not null,
 notice				int not null default 1,
 ses_launstat		tinyint not null default 0,
 ses_launupd		datetime,
 reg_stat			tinyint not null default 0,
 headcount			int default 0,
 act_id				int not null,
 constraint ses_act_id_fk foreign key (act_id) references act (act_id),
 constraint ses_ses_id_pk primary key (ses_id));																									/* 0現正報名中 1成團 2不成團.取消*/
																																/* 0下 1上*/	    /* 3場次異動.需再確認是否成團 4取消場次 5場次已圓滿結束 6結案 */		
                    /* (場次id, 場次date, 開始時間, 結束時間, 開始報名date, 報名截止(確認是否成團)date, 人數下限, 人數上限, 報名費, 行前通知, 場次狀態, 場次狀態更新時間, 報名狀態, 報名人數, 活動id)*/
						/*目前是1.3.4有上架過可以寫場次, 4只能寫已結束的*/
 
 					   /*圓滿結束*/
insert into ses values (null, '2025-08-08', '15:00', '17:30', '2025-07-01', '2025-08-01',  5, 20, 230, 3, 0, '2025-08-08 17:30:00', 5, 18, 1),
					   /*還在報名中...這邊我有修改上架狀態更新時間&修正場次狀態*/
					   (null, '2025-10-10', '14:00', '16:30', '2025-08-25', '2025-10-01',  5, 20, 200, 3, 1, '2025-08-11 14:10:09', 0, 10, 1),
                       /*不成團, 取消...這邊我有修改上架狀態跟人數 (確定取消的話人數應要歸0? 這樣後台才不會用金額成以人數結果撥款過來? 是不是應該要以場次算啊) */
                       (null, '2025-03-20', '10:30', '11:30', '2025-01-20', '2025-03-10',  5, 15, 399, 2, 0, '2025-03-10 00:00:00', 2, 0, 3),
					   /* 場次有異動, 還未確認是否成團*/                       
                       (null, '2025-10-05', '15:00', '16:00', '2025-08-10', '2025-09-20',  5, 15, 449, 2, 1, '2025-08-05 00:00:00', 3, 4, 3),
                       /* 場次取消 (10/7有地震導致部分設施要維修、直接取消) */
                       (null, '2025-10-10', '16:30', '17:30', '2025-08-25', '2025-10-01', 10, 20, 499, 3, 0, '2025-10-07 00:00:00', 4, 0, 3),
                       /* 結案 */
                       (null, '2025-06-05', '14:00', '16:30', '2025-04-25', '2025-05-25', 15, 30, 700, 3, 1, '2025-06-30 00:00:00', 6, 25, 4),
                       /* 成團, 活動尚未進行 */
                       (null, '2025-09-10', '14:00', '16:30', '2025-07-20', '2025-09-01',  5, 15, 200, 3, 1, '2025-09-01 00:00:00', 1, 11, 1);


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
	mem_acc varchar(40) NOT NULL UNIQUE,
	mem_pwd varchar(20) NOT NULL,
	acc_status tinyint NOT NULL DEFAULT 0,
	mem_name varchar(20) NOT NULL,
    mem_birthday date NOT NULL,
	mem_mobile varchar(11) NOT NULL,
	mem_email varchar(254) NOT NULL,
	mem_zipcode varchar(6) NOT NULL,
	mem_city varchar(5) NOT NULL,
	mem_dist varchar(5) NOT NULL,
	mem_addr varchar(40) NOT NULL,
	reg_date datetime NOT NULL DEFAULT current_timestamp,
	mem_point int NOT NULL DEFAULT 0
);

INSERT INTO mem (mem_acc, mem_pwd, acc_status, mem_name, mem_birthday, mem_mobile, mem_email, mem_zipcode, mem_city, mem_dist, mem_addr, reg_date, mem_point) VALUES
('zeusMax9914', 'Karma88@Rise', 1, '謝維綺', '1980-11-26', '0910-380143', 'pamela8508@gmail.com', '320', '桃園市', '中壢區', '仁和街35號', '2022-08-26 10:30:00', 150),
('NovaSkyline203', 'Lx657XhM', 1, '胡得軒', '1996-10-11', '0916-518593', 'henson1654@hotmail.com', '600', '嘉義市', '西區', '世賢路2段5號', '2022-08-26 11:29:30', 110),
('TigerRun88', '9y7wqUwv', 0, '宋柯雯', '1993-08-07', '0961-388330', 'arianna6146@hotmail.com', '511', '彰化縣', '社頭鄉', '中山路1段38號10樓之10', '2022-09-01 12:00:59', 253),
('valine203', 'echo92Xx@', 0, '郭實祐', '1990-11-01', '0937-453975', 'jeffrey2062@icloud.com', '360', '苗栗縣', '苗栗市', '宜春路62號', '2023-03-05 09:08:05', 20),
('alphaWolf2031', '7Y2rTPbZ', 0, '林蓁蓓', '1978-09-08', '0972-375934', 'kaylynn3676@hotmail.com', '803', '高雄市', '鹽埕區', '大成街98號', '2023-04-01 01:01:10', 5),
('SkyHunter77', 'cDMz4q55', 0, '何俞維', '1979-05-23', '0961-063659', 'hampden3392@gmail.com', '555', '南投縣', '魚池鄉', '日月街24號9樓之11', '2023-10-05 20:58:09', 16),
('tiger_XR9821', '82AY43Pw', 1, '連之義', '1992-07-28', '0915-476888', 'richards2316@gmail.com', '931', '屏東縣', '佳冬鄉', '民學路7號', '2024-06-16 22:10:00', 0),
('UtFeobef152', 'Jupiter9@Lx', 1, '許洋竹', '1985-05-19', '0956-715009', 'mendoza8324@gmail.com', '803', '高雄市', '鹽埕區', '大勇市場5號', '2024-10-20 14:20:35', 88),
('RavenX42ZpLm', 'windyX421@', 1, '陳婉術', '1982-06-21', '0924-554240', 'evangeline3888@gmail.com', '110', '臺北市', '信義區', '信義路5段16號6樓之6', '2024-11-20 11:15:20', 23),
('AlphaX9273', 't7pQkvx2', 0, '黃珍育', '1987-05-21', '0939-682988', 'debbie7435@outlook.com', '882', '澎湖縣', '望安鄉', '花嶼9號', '2024-12-01 08:54:53', 225),
('skyline_83x', 'MarsCode903', 0, '李大仁', '1965-02-28', '0911-222333', 'member001@example.com', '100', '台北市', '大安區', '信義路100號', '2025-01-01 10:00:00', 20),
('nova88_rider_12', 'novaRun88z', 1, '王小美', '1969-01-08', '0922-333444', 'member002@example.com', '221', '新北市', '板橋區', '中山路200號', '2025-02-05 11:00:00', 28),
('xtrmcoder207', 'Qq29@delta73', 1, '張志豪', '1989-04-28', '0933-444555', 'member003@example.com', '401', '台中市', '北區', '學士路300號', '2025-03-18 12:00:00', 0),
('alpha_2099zx', 'ZetaStorm99', 1, '陳玉芬', '1998-07-14', '0944-555666', 'member004@example.com', '801', '高雄市', '前金區', '五福路88號', '2025-03-18 13:00:00', 0),
('deltaWave7192', 'vR7@bLpW25', 2, '林建宏', '2000-01-19', '0955-666777', 'member005@example.com', '700', '台南市', '中西區', '民生路199號', '2025-05-25 14:00:00', 97),
('neorunner92', 'Alpha42moon', 0, '曾雅婷', '2002-10-26', '0966-777888', 'member006@example.com', '300', '新竹市', '東區', '光復路250號', '2025-06-06 15:00:00', 53),
('alpha3x9z1t', 'Skyline@Z9', 2, '游信宏', '2004-08-20', '0977-888999', 'member007@example.com', '970', '花蓮縣', '花蓮市', '中正路10號', '2025-07-13 16:00:00', 7),
('storm1987_wave', 'maxwell302', 0, '洪詠欣', '1999-08-31', '0988-999000', 'member008@example.com', '260', '宜蘭縣', '羅東鎮', '中山路一段88號', '2025-08-30 17:00:00', 5),
('midnight_42_zz', 'Tiger88@Run', 1, '邱柏睿', '2005-09-21', '0911-000222', 'member009@example.com', '600', '嘉義市', '西區', '垂楊路120號', current_timestamp(), 0),
('echo_delta_1209', 'Xp92kLo@1', 1, '簡心怡', '1988-12-25', '0922-000333', 'member010@example.com', '540', '南投縣', '南投市', '中興路300號', current_timestamp(), 0);

-- 刪除/建立 商品折價卷
DROP TABLE IF EXISTS  pro_cpn;
CREATE TABLE pro_cpn(
    pro_cpn_id INT NOT NULL AUTO_INCREMENT,
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
-- 1. 滿額折抵券（滿500折100，全館）
('滿500折100', 0, 100.00, 500, '2025-09-01', NULL, '消費滿500折100元', 1, 0),

-- 2. 百分比折扣券（全館85折，有效期30天）
('全館85折', 1, 0.85, NULL, NULL, 30, '領後30天內有效', 1, 0),

-- 3. 滿1000折200（全館）
('滿1000折200', 0, 200.00, 1000, '2025-09-01', NULL, '消費滿1000折200元', 1, 0),

-- 4. 9折券（全館，新品專用）
('全館9折券', 1, 0.90, NULL, '2025-09-15', 15, '全館適用，限新品', 1, 0),

-- 5. 滿300折50（尚未啟用）
('滿300折50 (尚未啟用)', 0, 50.00, 300, '2025-10-01', NULL, '活動預備用券', 0, 0),

-- 6. 滿200折20（週末限定）
('滿200折20', 0, 20.00, 200, '2025-09-05', 7, '週末限定折抵', 1, 0),

-- 7. 全館8折券
('全館8折券', 1, 0.80, NULL, '2025-09-10', 10, '全館適用，限時8折', 1, 0),

-- 8. 滿1500折300
('滿1500折300', 0, 300.00, 1500, '2025-09-20', NULL, '全館滿1500折300元', 1, 0),

-- 9. 新客專屬9折券
('新客專屬9折券', 1, 0.90, NULL, NULL, 14, '新註冊會員14天內使用', 1, 0),

-- 10. 預購商品折100
('預購折100', 0, 100.00, 600, '2025-09-25', 10, '預購商品專屬折抵', 1, 0);


-- 刪除/建立 商品折價卷持有者明細
DROP TABLE IF EXISTS mem_pro_cpn;
CREATE TABLE mem_pro_cpn (
    cpn_holder_detail_id INT NOT NULL AUTO_INCREMENT,
    pro_cpn_id INT NOT NULL, -- FK
    mem_id INT NOT NULL,     -- FK
    cpn_use_status TINYINT NOT NULL COMMENT '0:未使用,1:已使用,2:已過期',
    crt_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
    rcv_at DATETIME NOT NULL,
    eff_start DATE NOT NULL,
    eff_end DATE,
    used_at DATETIME,
    CONSTRAINT cpn_holder_detail_id_pk PRIMARY KEY (cpn_holder_detail_id)
) ENGINE=InnoDB;

INSERT INTO mem_pro_cpn
(pro_cpn_id, mem_id, cpn_use_status, rcv_at, eff_start, eff_end, used_at)
VALUES
-- 未使用（還在有效期內）
(1, 1, 0, DATE_SUB(NOW(), INTERVAL 10 DAY), '2025-08-25', '2025-09-25', NULL),
(2, 2, 0, DATE_SUB(NOW(), INTERVAL 7 DAY), '2025-09-01', '2025-09-30', NULL),

-- 已使用（在有效期內使用過）
(3, 3, 1, DATE_SUB(NOW(), INTERVAL 15 DAY), '2025-08-20', '2025-09-20', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(4, 4, 1, DATE_SUB(NOW(), INTERVAL 12 DAY), '2025-08-28', '2025-09-28', DATE_SUB(NOW(), INTERVAL 2 DAY)),

-- 已過期（沒用到）
(5, 5, 2, DATE_SUB(NOW(), INTERVAL 25 DAY), '2025-07-20', '2025-08-20', NULL),
(6, 6, 2, DATE_SUB(NOW(), INTERVAL 30 DAY), '2025-07-25', '2025-08-25', NULL),

-- 未使用（剛領，還有效）
(7, 7, 0, DATE_SUB(NOW(), INTERVAL 3 DAY), '2025-09-05', '2025-09-30', NULL),

-- 已使用（昨天用掉）
(8, 8, 1, DATE_SUB(NOW(), INTERVAL 5 DAY), '2025-09-01', '2025-09-25', DATE_SUB(NOW(), INTERVAL 1 DAY)),

-- 已過期（有效期已結束，沒使用）
(9, 9, 2, DATE_SUB(NOW(), INTERVAL 40 DAY), '2025-07-01', '2025-07-31', NULL),

-- 未使用（快到期）
(10, 10, 0, DATE_SUB(NOW(), INTERVAL 2 DAY), '2025-09-01', '2025-09-10', NULL);


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
	pro_ord_cpndisc INT NOT NULL default 0,
	pro_ord_pointdisc INT NOT NULL default 0,
	pro_ord_pointget INT NOT NULL,
	pro_ord_grand_total INT NOT NULL,
	pro_ord_comm VARCHAR(200) DEFAULT NULL,
	pro_ord_payment TINYINT NOT NULL default 0,
	pro_ord_shipment TINYINT NOT NULL default 0,
	pro_tracking_no VARCHAR(30),
	pro_ord_shipdate DATETIME,
	CONSTRAINT pro_order_pro_ord_id_pk PRIMARY KEY (pro_ord_id)
)ENGINE InnoDB;

INSERT INTO pro_order (
    mem_id, 
    cpn_holder_detail_id, 
    pro_ord_date, 
    pro_ord_status, 
    pro_pay_status,
    pro_total, 
    pro_ord_ship_fee, 
    pro_ord_cpndisc, 
    pro_ord_pointdisc,
    pro_ord_pointget, 
    pro_ord_grand_total, 
    pro_ord_comm,
    pro_ord_payment, 
    pro_ord_shipment, 
    pro_tracking_no, 
    pro_ord_shipdate
) VALUES
-- 1. 使用折價券和點數 已付款，已到貨，信用卡+宅配
(1, 1, '2025-08-30 15:00:00', 3, 1, 1200, 80, 100, 50, FLOOR(1200*0.05), 1200 + 80 - 100 - 50, NULL, 0, 0, 'SF123456789', '2025-09-01 10:00:00'),
-- 2. 只使用折價券 已付款，出貨中，轉帳+超取
(2, 2, '2025-08-29 10:30:00', 2, 1, 800, 80, 80, 0, FLOOR(800*0.05), 800 + 80 - 80 - 0, NULL, 1, 1, 'HT987654321', '2025-08-31 15:00:00'),
-- 3. 只使用點數 已付款，申請退貨，信用卡+宅配
(3, NULL, '2025-08-28 12:45:00', 4, 1, 1500, 80, 0, 150, FLOOR(1500*0.05), 1500 + 80 - 0 - 150, NULL, 0, 0, 'SF111222333', '2025-08-29 11:00:00'),
-- 4. 不使用任何折扣 成立訂單，未付款，貨到付款+宅配
(4, NULL, '2025-08-27 18:00:00', 0, 0, 500, 80, 0, 0, FLOOR(500*0.05), 500 + 80, NULL, 2, 0, NULL, NULL),
-- 5. 使用折價券和點數 已付款，退貨中，信用卡+超取
(5, 5, '2025-08-26 20:15:00', 5, 1, 950, 80, 95, 20, FLOOR(950*0.05), 950 + 80 - 95 - 20, NULL, 0, 1, 'HT444555666', '2025-08-27 17:00:00'),
-- 6. 只使用折價券 已付款，已到貨，轉帳+宅配
(6, 6, '2025-08-25 09:00:00', 3, 1, 720, 80, 72, 0, FLOOR(720*0.05), 720 + 80 - 72, NULL, 1, 0, 'SF223344556', '2025-08-26 12:00:00'),
-- 7. 不使用任何折扣 已付款，已退貨，信用卡+超取
(7, NULL, '2025-08-24 11:00:00', 6, 1, 350, 80, 0, 0, FLOOR(350*0.05), 350 + 80, NULL, 0, 1, 'HT778899001', '2025-08-25 10:00:00'),
-- 8. 只使用點數 成立訂單，未付款，超商取貨付款+超取
(8, NULL, '2025-08-23 14:00:00', 0, 0, 2500, 80, 0, 250, FLOOR(2500*0.05), 2500 + 80 - 250, NULL, 3, 1, NULL, NULL),
-- 9. 使用折價券和點數 已付款，已到貨，信用卡+宅配
(9, 9, '2025-08-22 16:30:00', 3, 1, 1800, 80, 200, 80, FLOOR(1800*0.05), 1800 + 80 - 200 - 80, NULL, 0, 0, 'HT112233445', '2025-08-23 10:30:00'),
-- 10. 不使用任何折扣 已付款，出貨中，轉帳+宅配
(10, NULL, '2025-08-21 17:00:00', 2, 1, 400, 80, 0, 0, FLOOR(400*0.05), 400 + 80, NULL, 1, 0, 'SF334455667', '2025-08-22 13:00:00'),
-- 11. 只使用折價券 已付款，已到貨，信用卡+超取
(11, 1, '2025-08-20 18:00:00', 3, 1, 650, 80, 100, 0, FLOOR(650*0.05), 650 + 80 - 100, NULL, 0, 1, 'HT556677889', '2025-08-21 11:00:00'),
-- 12. 不使用任何折扣 已付款，已到貨，轉帳+超取
(12, NULL, '2025-08-19 19:30:00', 3, 1, 1000, 80, 0, 0, FLOOR(1000*0.05), 1000 + 80, NULL, 1, 1, 'SF778899001', '2025-08-20 15:00:00'),
-- 13. 使用折價券和點數 已付款，已到貨，信用卡+宅配
(13, 2, '2025-08-18 20:00:00', 3, 1, 1100, 80, 80, 100, FLOOR(1100*0.05), 1100 + 80 - 80 - 100, NULL, 0, 0, 'HT990011223', '2025-08-19 14:00:00'),
-- 14. 只使用折價券 已付款，已到貨，轉帳+宅配
(14, 3, '2025-08-17 08:00:00', 3, 1, 750, 80, 150, 0, FLOOR(750*0.05), 750 + 80 - 150, NULL, 1, 0, 'SF123123123', '2025-08-18 10:00:00'),
-- 15. 不使用任何折扣 已付款，已到貨，信用卡+超取
(15, NULL, '2025-08-16 09:00:00', 3, 1, 200, 80, 0, 0, FLOOR(200*0.05), 200 + 80, NULL, 0, 1, 'HT456456456', '2025-08-17 12:00:00'),
-- 16. 只使用點數 已付款，已到貨，轉帳+宅配
(16, NULL, '2025-08-15 10:00:00', 3, 1, 1800, 80, 0, 180, FLOOR(1800*0.05), 1800 + 80 - 180, NULL, 1, 0, 'SF789789789', '2025-08-16 14:00:00'),
-- 17. 使用折價券和點數 已付款，出貨中，信用卡+宅配
(17, 4, '2025-08-14 11:00:00', 2, 1, 3000, 80, 250, 200, FLOOR(3000*0.05), 3000 + 80 - 250 - 200, NULL, 0, 0, 'HT111333555', '2025-08-15 11:00:00'),
-- 18. 不使用任何折扣 已付款，已到貨，信用卡+超取
(18, NULL, '2025-08-13 12:00:00', 3, 1, 450, 80, 0, 0, FLOOR(450*0.05), 450 + 80, NULL, 0, 1, 'SF222444666', '2025-08-14 15:00:00'),
-- 19. 只使用折價券 已取消訂單，未出貨，轉帳+宅配
(19, 5, '2025-08-12 13:00:00', 1, 1, 900, 80, 150, 0, FLOOR(900*0.05), 900 + 80 - 150, NULL, 1, 0, NULL, NULL),
-- 20. 不使用任何折扣 成立訂單，未付款，貨到付款+超取
(20, NULL, '2025-08-11 14:00:00', 0, 0, 600, 80, 0, 0, FLOOR(600*0.05), 600 + 80, NULL, 2, 1, NULL, NULL);


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

INSERT INTO pro_order_item (pro_id, pro_ord_id, pro_unitprice, pro_amount, pro_subtotal) VALUES
-- pro_ord_id: 1, pro_total: 1200
(3, 1, 150, 8, 1200),
-- pro_ord_id: 2, pro_total: 800
(12, 2, 400, 2, 800),
-- pro_ord_id: 3, pro_total: 1500
(7, 3, 500, 3, 1500),
-- pro_ord_id: 4, pro_total: 500
(17, 4, 480, 1, 480),
(18, 4, 65, 1, 65),
-- pro_ord_id: 5, pro_total: 950
(11, 5, 450, 2, 900),
(19, 5, 50, 1, 50),
-- pro_ord_id: 6, pro_total: 720
(6, 6, 280, 2, 560),
(10, 6, 85, 2, 170),
-- pro_ord_id: 7, pro_total: 350
(8, 7, 350, 1, 350),
-- pro_ord_id: 8, pro_total: 2500
(5, 8, 300, 8, 2400),
(20, 8, 70, 1, 70),
(1, 8, 80, 1, 80),
-- pro_ord_id: 9, pro_total: 1800
(5, 9, 300, 6, 1800),
-- pro_ord_id: 10, pro_total: 400
(12, 10, 400, 1, 400),
-- pro_ord_id: 11, pro_total: 650
(9, 11, 220, 2, 440),
(1, 11, 80, 1, 80),
(2, 11, 60, 2, 120),
-- pro_ord_id: 12, pro_total: 1000
(7, 12, 500, 2, 1000),
-- pro_ord_id: 13, pro_total: 1100
(13, 13, 120, 4, 480),
(15, 13, 90, 5, 450),
(10, 13, 85, 2, 170),
-- pro_ord_id: 14, pro_total: 750
(17, 14, 480, 1, 480),
(18, 14, 65, 4, 260),
(4, 14, 45, 1, 45),
-- pro_ord_id: 15, pro_total: 200
(13, 15, 120, 1, 120),
(1, 15, 80, 1, 80),
-- pro_ord_id: 16, pro_total: 1800
(5, 16, 300, 6, 1800),
-- pro_ord_id: 17, pro_total: 3000
(5, 17, 300, 10, 3000),
-- pro_ord_id: 18, pro_total: 450
(11, 18, 450, 1, 450),
-- pro_ord_id: 19, pro_total: 900
(15, 19, 90, 10, 900),
-- pro_ord_id: 20, pro_total: 600
(2, 20, 60, 10, 600);


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
-- 1
('活動滿500折50', 0, 50.00, 500, '2025-09-01', NULL, '活動消費滿500折50元', 1),
-- 2
('活動滿1000折200', 0, 200.00, 1000, '2025-09-05', NULL, '活動消費滿1000折200元', 1),
-- 3
('活動85折券', 1, 0.85, NULL, NULL, 30, '活動報名後30天有效85折', 1),
-- 4
('活動9折券', 1, 0.90, NULL, '2025-09-10', 15, '活動期間9折', 1),
-- 5
('早鳥專屬100元券', 0, 100.00, 400, '2025-08-20', NULL, '早鳥報名專屬優惠', 1),
-- 6
('團體報名8折', 1, 0.80, NULL, '2025-09-15', 10, '三人以上團報享8折', 1),
-- 7
('學生專屬50元券', 0, 50.00, 200, '2025-09-01', 20, '學生報名專屬，滿200折50', 1),
-- 8
('VIP專屬7折券', 1, 0.70, NULL, NULL, 7, 'VIP專屬7折，限7天使用', 1),
-- 9
('預備活動券（尚未啟用）', 0, 150.00, 600, '2025-10-01', NULL, '活動預備用券', 0),
-- 10
('滿1500折300', 0, 300.00, 1500, '2025-09-20', NULL, '活動消費滿1500折300元', 1);

-- 刪除/建立 活動折價卷持有者明細
DROP TABLE IF EXISTS mem_act_cpn;
CREATE TABLE mem_act_cpn (
    cpn_holder_detail_id INT NOT NULL AUTO_INCREMENT,
    act_cpn_id INT NOT NULL, -- FK
    mem_id INT NOT NULL,     -- FK
    cpn_use_status TINYINT NOT NULL COMMENT '0:未使用,1:已使用,2:已過期',
    crt_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
    rcv_at DATETIME NOT NULL,-- 領券時間
    eff_start DATE NOT NULL, -- 實際生效起
    eff_end DATE,-- 實際失效止
    used_at DATETIME,
    CONSTRAINT cpn_holder_detail_id_pk PRIMARY KEY (cpn_holder_detail_id)
) ENGINE=InnoDB;

-- 活動折價券持有者明細
INSERT INTO mem_act_cpn
(act_cpn_id, mem_id, cpn_use_status, rcv_at, eff_start, eff_end, used_at)
VALUES
-- 未使用（有效中）--
(1, 1, 0, DATE_SUB(NOW(), INTERVAL 10 DAY), '2025-08-20', '2025-09-20', NULL),
(2, 2, 0, DATE_SUB(NOW(), INTERVAL 5 DAY), '2025-08-25', '2025-09-25', NULL),

-- 已使用
(3, 3, 1, DATE_SUB(NOW(), INTERVAL 15 DAY), '2025-08-10', '2025-09-10', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(4, 4, 1, DATE_SUB(NOW(), INTERVAL 20 DAY), '2025-08-05', '2025-09-05', DATE_SUB(NOW(), INTERVAL 1 DAY)),

-- 已過期（沒使用過）
(5, 5, 2, DATE_SUB(NOW(), INTERVAL 30 DAY), '2025-07-20', '2025-08-20', NULL),
(6, 6, 2, DATE_SUB(NOW(), INTERVAL 25 DAY), '2025-07-25', '2025-08-25', NULL),

-- 未使用（剛領，還在有效期）
(7, 7, 0, DATE_SUB(NOW(), INTERVAL 1 DAY), '2025-09-01', '2025-09-30', NULL),

-- 已使用（當日使用）
(8, 8, 1, DATE_SUB(NOW(), INTERVAL 2 DAY), '2025-08-28', '2025-09-28', NOW()),

-- 已過期（有效期已經過去）
(9, 9, 2, DATE_SUB(NOW(), INTERVAL 40 DAY), '2025-07-01', '2025-07-31', NULL),

-- 未使用（即將到期）
(10, 10, 0, DATE_SUB(NOW(), INTERVAL 3 DAY), '2025-08-29', '2025-09-10', NULL);





-- 刪除/建立 報名訂單





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






-- step 3. 
-- (3-1) 最新消息清單

-- (3-2) 常見QA清單

-- (3-2) 管理員職稱表 -> 管理員（FK職稱編號） -> 功能權限 -> 角色權限（FK職稱編號）（FK權限編號）


-- 最新消息清單
CREATE TABLE news (
	news_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	news_title VARCHAR(50) NOT NULL,
	news_cont VARCHAR(1000) NOT NULL,
	news_at DATETIME NOT NULL
);

INSERT INTO news (news_title, news_cont, news_at) VALUES
('網站更新', '我們的網站已經更新至最新版本，提供更好的使用者體驗。', '2024-05-20 10:30:00'),
('夏季特賣會', '所有商品8折優惠，只到月底！', '2024-06-01 15:00:00');


-- 常見QA清單
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
	admin_func_id INT NOT NULL PRIMARY KEY,
	admin_func_name VARCHAR(20) NOT NULL,
	admin_func_des VARCHAR(100) NOT NULL
);
	
INSERT INTO admin_function (admin_func_id, admin_func_name, admin_func_des) VALUES
(1, '網站總管理', '商品活動廣告上架、下架、編輯，管理員管理'),
(2, '商城管理', '查看、處理商城商品'),
(3, '活動管理', '查看、管理活動資料');

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

-- (2-6) 
-- 報名訂單（FK場次編號）（FK一般會員編號）（FK活動折價卷持有者流水號）
ALTER TABLE mem_act_cpn
ADD CONSTRAINT mem_act_cpn_fk FOREIGN KEY (act_cpn_id) REFERENCES act_cpn(act_cpn_id),
ADD CONSTRAINT mem_act_cpn_mem_fk FOREIGN KEY (mem_id) REFERENCES mem(mem_id);

-- (2-7) 
-- 商品收藏清單（FK一般會員編號）（FK商品編號） 
ALTER TABLE favo_pro 
ADD CONSTRAINT favo_pro_mem_fk FOREIGN KEY (mem_id) REFERENCES mem(mem_id) ON DELETE CASCADE,
ADD CONSTRAINT favo_pro_pro_fk FOREIGN KEY (pro_id) REFERENCES product(pro_id) ON DELETE CASCADE;

-- 活動收藏清單（FK一般會員編號）（FK活動編號）


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