-- 添加状态数据字典
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type) 
VALUES ('ad_status', '广告状态', 'ad_status', '广告状态', 0, 'admin', NOW(), NULL, NULL, 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) 
VALUES 
('ad_status_0', 'ad_status', '禁用', '0', '禁用', 1, 1, 'admin', NOW(), NULL, NULL),
('ad_status_1', 'ad_status', '启用', '1', '启用', 2, 1, 'admin', NOW(), NULL, NULL);

-- 添加评分数据字典
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type) 
VALUES ('driver_rating', '司机评分', 'driver_rating', '司机评分', 0, 'admin', NOW(), NULL, NULL, 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) 
VALUES 
('driver_rating_1', 'driver_rating', '1星', '1', '1星', 1, 1, 'admin', NOW(), NULL, NULL),
('driver_rating_2', 'driver_rating', '2星', '2', '2星', 2, 1, 'admin', NOW(), NULL, NULL),
('driver_rating_3', 'driver_rating', '3星', '3', '3星', 3, 1, 'admin', NOW(), NULL, NULL),
('driver_rating_4', 'driver_rating', '4星', '4', '4星', 4, 1, 'admin', NOW(), NULL, NULL),
('driver_rating_5', 'driver_rating', '5星', '5', '5星', 5, 1, 'admin', NOW(), NULL, NULL);

-- 修改商户表，添加新字段
ALTER TABLE ad_merchant 
ADD COLUMN driver_name varchar(50) COMMENT '司机姓名',
ADD COLUMN mobile varchar(20) COMMENT '手机号码',
ADD COLUMN rating int COMMENT '评分',
ADD COLUMN id_card varchar(18) COMMENT '身份证号',
ADD COLUMN id_card_front varchar(255) COMMENT '身份证正面照',
ADD COLUMN id_card_back varchar(255) COMMENT '身份证反面照',
ADD COLUMN driver_license varchar(255) COMMENT '驾驶证图片'; 