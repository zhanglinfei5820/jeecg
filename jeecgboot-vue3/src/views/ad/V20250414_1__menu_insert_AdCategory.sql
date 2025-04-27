-- 注意：该页面对应的前台目录为views/ad文件夹下
-- 如果你想更改到其他目录，请修改sql中component字段对应的值


INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external) 
VALUES ('2025041407532120250', NULL, '广告分类表', '/ad/adCategoryList', 'ad/AdCategoryList', NULL, NULL, 0, NULL, '1', 0.00, 0, NULL, 1, 0, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2025-04-14 19:53:25', NULL, NULL, 0);

-- 权限控制sql
-- 新增
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2025041407532120251', '2025041407532120250', '添加广告分类表', NULL, NULL, 0, NULL, NULL, 2, 'ad:ad_category:add', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-04-14 19:53:25', NULL, NULL, 0, 0, '1', 0);
-- 编辑
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2025041407532120252', '2025041407532120250', '编辑广告分类表', NULL, NULL, 0, NULL, NULL, 2, 'ad:ad_category:edit', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-04-14 19:53:25', NULL, NULL, 0, 0, '1', 0);
-- 删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2025041407532120253', '2025041407532120250', '删除广告分类表', NULL, NULL, 0, NULL, NULL, 2, 'ad:ad_category:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-04-14 19:53:25', NULL, NULL, 0, 0, '1', 0);
-- 批量删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2025041407532120254', '2025041407532120250', '批量删除广告分类表', NULL, NULL, 0, NULL, NULL, 2, 'ad:ad_category:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-04-14 19:53:25', NULL, NULL, 0, 0, '1', 0);
-- 导出excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2025041407532120255', '2025041407532120250', '导出excel_广告分类表', NULL, NULL, 0, NULL, NULL, 2, 'ad:ad_category:exportXls', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-04-14 19:53:25', NULL, NULL, 0, 0, '1', 0);
-- 导入excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2025041407532120256', '2025041407532120250', '导入excel_广告分类表', NULL, NULL, 0, NULL, NULL, 2, 'ad:ad_category:importExcel', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-04-14 19:53:25', NULL, NULL, 0, 0, '1', 0);