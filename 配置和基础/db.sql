create table  `Oms_Cart_Item`
(
       `Id`              LONGLONG(20) auto_increment primary key not null,
       `Member_Id`       LONGLONG(20) comment '会员号',
       `Product_Id`      LONGLONG(20) comment '产品号',
       `Total_Price`     LONGLONG(20),
       `Price`           NEWDECIMAL,
       `Is_Checked`      VAR_STRING(10) default 1,
       `Product_Pic`     VAR_STRING(200),
       `Quantity`        LONGLONG(20) comment '购买数量',
       `Product_Name`    VAR_STRING(200)
);
alter table `Oms_Cart_Item` comment= '购物车项';
create table  `Oms_Order`
(
       `Id`              LONGLONG(20) auto_increment primary key not null,
       `Member_Id`       LONGLONG(20) not null,
       `Order_Code`      VAR_STRING(40),
       `Create_Time`     TIMESTAMP(19) comment '创建时间',
       `Modify_Time`     TIMESTAMP(19) comment '修改时间',
       `Total_Amount`    LONGLONG(12) comment '总金额',
       `Final_Amount`    LONGLONG(12) comment '最终成交金额',
       `Pay_Time`        TIMESTAMP(19) comment '支付时间',
       `Post_Time`       TIMESTAMP(19) comment '发货时间',
       `Confirm_Time`    TIMESTAMP(19) comment '确认收货时间',
       `Recevice_Name`   VAR_STRING(40) not null,
       `Recevice_Phone`  VAR_STRING(32) not null comment '收货人电话',
       `Recevice_Post_Code` VAR_STRING(32) comment '收货人邮编',
       `Recevice_Province` VAR_STRING(32) comment '省份/直辖市',
       `Recevice_City`   VAR_STRING(32) comment '城市',
       `Recevice_Region` VAR_STRING(32) comment '区',
       `Recevice_Detail_Address` VAR_STRING(200) comment '详细地址',
       `Member_Name`     VAR_STRING(255),
       `Status`          LONG(1) default 0 comment '订单状态 0->未支付 1->支付 ',
       `Note`            VAR_STRING(500) comment '备注'
);
alter table `Oms_Order` comment= '订单表';
create table  `Oms_Order_Item`
(
       `Id`              LONGLONG(20) auto_increment primary key not null,
       `Order_Id`        LONG(20),
       `Product_Id`      LONGLONG(20) comment '商品编号',
       `Order_Code`      VAR_STRING(40) comment '订单号',
       `Product_Name`    VAR_STRING(200),
       `Quantity`        LONGLONG comment '购买数量',
       `Total_Amount`    LONGLONG(20),
       `Product_Pic`     VAR_STRING(400)
);
alter table `Oms_Order_Item` comment= '订单详情';
create table  `Pms_Catalog1`
(
       `Id`              LONGLONG(20) auto_increment primary key not null,
       `Name`            VAR_STRING(64)
);
alter table `Pms_Catalog1` comment= '一级分类';
create table  `Pms_Catalog2`
(
       `Id`              LONGLONG(20) auto_increment primary key not null,
       `Name`            VAR_STRING(64),
       `Catalog1_Id`     LONGLONG(20)
);
alter table `Pms_Catalog2` comment= '二级分类';

create table  `Pms_Catalog3`
(
       `Id`              LONGLONG(20) auto_increment primary key not null,
       `Name`            VAR_STRING(64),
       `Catalog2_Id`     LONGLONG(20)
);
alter table `Pms_Catalog3` comment= '三级分类'
create table  `Pms_Product`
(
       `Id`              LONGLONG(20) auto_increment primary key not null,
       `Catalog1_Id`     LONGLONG(20),
       `Catalog2_Id`     LONGLONG(20),
       `Catalog3_Id`     LONGLONG(20),
       `Product_Name`    VAR_STRING(64),
       `Price`           NEWDECIMAL(13,2) default 0000000000.00,
       `Stock`           LONG,
       `Note`            VAR_STRING(200) default 无,
       `Default_Img`     VAR_STRING(200)
);
alter table `Pms_Product` comment= '商品表';

create table  `Pms_Product_Image`
(
       `Id`              LONGLONG(20) auto_increment primary key not null,
       `Product_Id`      LONGLONG(20),
       `Img_Url`         VAR_STRING(200)
);
alter table `Pms_Product_Image` comment= '商品图片';
create table  `t_Admin`
(
       `Id`              LONG auto_increment primary key not null comment '编号',
       `UserName`        VAR_STRING(20),
       `PassWord`        VAR_STRING(40) comment '密码',
       `Create_Time`     TIMESTAMP(19) default now(),
       `Modify_Time`     TIMESTAMP(19) default now()
);
create unique index `IDXU_t_Admin_UserName` on `t_Admin`(`UserName`);
alter table `t_Admin` comment= '数据表1 管理员';
create table  `Ums_Member`
(
       `Id`              LONGLONG(20) auto_increment primary key not null,
       `UserName`        VAR_STRING(64) comment '用户名',
       `PassWord`        VAR_STRING(64) comment '密码',
       `Nickname`        VAR_STRING(64) comment '昵称',
       `Phone`           VAR_STRING(64) comment '手机号码',
       `Status`          LONG(1) comment '帐号启用状态:0->禁用；1->启用',
       `Icon`            VAR_STRING(500) comment '头像',
       `Gender`          LONG(1) comment '性别：0->未知；1->男；2->女',
       `City`            VAR_STRING(64) comment '所做城市',
       `Job`             VAR_STRING(100) comment '职业',
       `Signature`       VAR_STRING(200) comment '个性签名',
       `Create_Time`     TIMESTAMP(19) default now(),
       `Modify_Time`     TIMESTAMP(19) default now(),
       `Email`           VAR_STRING(32) comment '邮箱'
);
create unique index `IDXU_Ums_Member_UserName` on `Ums_Member`(`UserName`);
create unique index `IDXU_Ums_Member_Phone` on `Ums_Member`(`Phone`);
alter table `Ums_Member` comment= '会员表';
create table  `Ums_Member_Receive_Address`
(
       `Id`              LONGLONG(20) auto_increment primary key not null,
       `Member_Id`       LONGLONG(20),
       `Name`            VAR_STRING(100) comment '收货人名称',
       `Phone`           VAR_STRING(64),
       `Default_Status`  LONG(1) comment '是否为默认',
       `Post_Code`       VAR_STRING(100) comment '邮政编码',
       `Province`        VAR_STRING(100) comment '省份/直辖市',
       `City`            VAR_STRING(100) comment '城市',
       `Region`          VAR_STRING(100) comment '区',
       `Detail_Address`  VAR_STRING(128) comment '详细地址(街道)'
);
alter table `Ums_Member_Receive_Address` comment= '会员收货地址表';

