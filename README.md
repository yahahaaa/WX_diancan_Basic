#  微信点餐项目

## 1. 项目介绍

### 1.1 项目来源

- 该项目是学习于慕课网 https://coding.imooc.com/class/117.html 课程，根据老师所讲内容手敲肝下来，也对一些代码进行了微调。慕课网的正版内容，可以租用老师的认证服务号（认证服务号的注册需要营业执照和年费300元），用于微信支付和微信扫码登录（免费的公众号不具备此功能，网页授权功能和消息模板等功能可以在公众平台申请测试账号）等功能。
- 若想获取课程源码和教学视频，可邮箱私信：895148716@qq.com
- 该项目分为两个部分，前一部分是基于SpringBoot的单机版本，也是当前仓库的代码，后一部分采用SpringCloud微服务架构，待我代码肝完，可去对应仓库下载学习。

### 1.2 项目介绍

#### 1.2.1 接口API

项目前端部分基于VUE开发，由于本项目专注后台开发，所以前端代码已经编写完成可以直接使用，并面向约定uri在Controller层开发即可，接口API如下：

##### 1.2.1.1商品列表

```java
GET /sell/buyer/product/list
```

参数

```
无
```

返回

```json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "name": "热榜",
            "type": 1,
            "foods": [
                {
                    "id": "123456",
                    "name": "皮蛋粥",
                    "price": 1.2,
                    "description": "好吃的皮蛋粥",
                    "icon": "http://xxx.com",
                }
            ]
        },
        {
            "name": "好吃的",
            "type": 2,
            "foods": [
                {
                    "id": "123457",
                    "name": "慕斯蛋糕",
                    "price": 10.9,
                    "description": "美味爽口",
                    "icon": "http://xxx.com",
                }
            ]
        }
    ]
}
```

##### 1.2.1.2 创建订单

```java
POST /sell/buyer/order/create
```

参数

```json
name: "张三"
phone: "18868822111"
address: "慕课网总部"
openid: "ew3euwhd7sjw9diwkq" //用户的微信openid
items: [{
    productId: "1423113435324",
    productQuantity: 2 //购买数量
}]
```

返回

```json
{
  "code": 0,
  "msg": "成功",
  "data": {
      "orderId": "147283992738221" 
  }
}
```

##### 1.2.1.3 订单列表

```java
GET /sell/buyer/order/list
```

参数

```json
openid: 18eu2jwk2kse3r42e2e
page: 0 //从第0页开始
size: 10
```

返回

```json
{
  "code": 0,
  "msg": "成功",
  "data": [
    {
      "orderId": "161873371171128075",
      "buyerName": "张三",
      "buyerPhone": "18868877111",
      "buyerAddress": "慕课网总部",
      "buyerOpenid": "18eu2jwk2kse3r42e2e",
      "orderAmount": 0,
      "orderStatus": 0,
      "payStatus": 0,
      "createTime": 1490171219,
      "updateTime": 1490171219,
      "orderDetailList": null
    },
    {
      "orderId": "161873371171128076",
      "buyerName": "张三",
      "buyerPhone": "18868877111",
      "buyerAddress": "慕课网总部",
      "buyerOpenid": "18eu2jwk2kse3r42e2e",
      "orderAmount": 0,
      "orderStatus": 0,
      "payStatus": 0,
      "createTime": 1490171219,
      "updateTime": 1490171219,
      "orderDetailList": null
    }]
}
```

##### 1.2.1.4 查询订单详情

```java
GET /sell/buyer/order/detail
```

参数

```json
openid: 18eu2jwk2kse3r42e2e
orderId: 161899085773669363
```

返回

```json
{
    "code": 0,
    "msg": "成功",
    "data": {
          "orderId": "161899085773669363",
          "buyerName": "李四",
          "buyerPhone": "18868877111",
          "buyerAddress": "慕课网总部",
          "buyerOpenid": "18eu2jwk2kse3r42e2e",
          "orderAmount": 18,
          "orderStatus": 0,
          "payStatus": 0,
          "createTime": 1490177352,
          "updateTime": 1490177352,
          "orderDetailList": [
            {
                "detailId": "161899085974995851",
                "orderId": "161899085773669363",
                "productId": "157875196362360019",
                "productName": "招牌奶茶",
                "productPrice": 9,
                "productQuantity": 2,
                "productIcon": "http://xxx.com",
                "productImage": "http://xxx.com"
            }
        ]
    }
}
```

##### 1.2.1.5 取消订单

```java
POST /sell/buyer/order/cancel
```

参数

```json
openid: 18eu2jwk2kse3r42e2e
orderId: 161899085773669363
```

返回

```json
{
    "code": 0,
    "msg": "成功",
    "data": null
}
```

##### 1.2.1.6 获取openid

由于cookie中没有openid，前端会重定向到这个访问接口用于微信网页授权获取openid

```java
重定向 /sell/wechat/authorize
```

参数

```json
returnUrl: http://xxx.com/abc  //【必填】
```

返回 （获取到openid后重新访问微信点餐前端页面）

```
http://xxx.com/abc?openid=oZxSYw5ldcxv6H0EU67GgSXOUrVg
```

##### 1.2.1.7 支付订单

创建订单成功后并重定向到支付订单接口,支付订单成功再重定向到订单详情页面

```java
重定向 /sell/pay/create
```

参数

```json
orderId: 161899085773669363
returnUrl: http://xxx.com/abc/order/161899085773669363
```

返回

```java
http://xxx.com/abc/order/161899085773669363
```

#### 1.2.2 前端展示

- 买家端（手机微信端）

<img src="https://github.com/Leeeeeming/picture/blob/main/%E5%AE%A2%E6%88%B7%E7%AB%AF%E5%B1%95%E7%A4%BA.jpg?raw=true?"/>

前端页面类似于饿了么，商家信息和评论信息采用静态设置，商品信息存储在数据库。

- 卖家端（PC Web端）

<img src="https://github.com/Leeeeeming/picture/blob/main/%E5%8D%96%E5%AE%B6%E7%AB%AF%E5%89%8D%E7%AB%AF%E9%A1%B5%E9%9D%A2.png?raw=true" alt="image-20210124133906864" style="zoom:50%;" />

#### 1.2.3 项目环境

- IDEA

- SpringBoot 2.2.1
- jdk8
- MySQL 5.7
- Redis 3.2.8
- Maven 3.3.9
- Nginx 1.11.7
- 虚拟机环境 centos7.3

#### 1.2.4 项目架构

### 1.3 功能模块

- 卖家端（手机端）
  - 下单
  - 查询订单
  - 取消订单
  - 支付订单
- 卖家端（PC端）
  - 类目增删
  - 商品上下架
  - 接单
  - 查询订单
  - 取消订单

## 2.快速使用

### 2.1 虚拟机配置

镜像下载地址：

https://pan.baidu.com/s/13PmKp7xjhrbv6xxOWH90-Q

MySQL、Redis、Nginx和前端代码已内置到Centos系统中，下载Oracle VM VirtualBox，将centos镜像导入启动。

**虚拟机登录**

- 账号 root
- 密码 123456

**路径**

- jdk 
  - 路径 /usr/local/jdk1.8.0_111

- nginx

  - 路径 /usr/local/nginx
  - 启动 nginx（默认开机自启）
  - 重启 nginx -s reload

- mysql

  - 配置文件路径 /etc/my.conf

  - 账号 root

  - 密码 123456

  - 端口 3306

  - 启动 systemctl start mysqld

  - 停止 systemctl stop mysqld

    

- redis
  - 路径 /usr/local/redis
  - 配置 /etc/reis.conf
  - 端口 6379
  - 密码 123456
  - 启动 systemctl start redis（开启已启动）
  - 停止 systemctl stop redis

#### 2.1.1 域名设置

项目中通过访问 **sell.com** 本地域名访问Nginx，并转发到前端页面（微信端），设置域名的具体流程如下：

1. **修改虚拟机Nginx配置文件，修改完配置文件后输入 nginx -s reload 重启nginx**

   - 输入命令定位到nginx.conf目录下：

     ```bash
     ## 定位到配置文件目录下
     cd /usr/local/nginx/conf
     ## 修改配置文件
     vim nginx.conf
     ```

   - 将 配置文件中 server_name 对应的服务域名改为 **sell.com**

   - 将proxy_pass请求转发地址改为主机IP地址（要改为自己电脑的主机ip可以通过**ipconfig**命令查看ipv4地址）+ 项目的url前缀地址（前缀地址在项目的application.yml配置文件中设置）

     ```bash
     server_name sell.com
     
     ## 当直接访问sell.com时nginx会将请求转发至vue前端
     ## 当访问sell.com/sell/* 时会转发至IDEA的后端tomcat服务器中
     location /sell/{
     	proxy_pass http://192.168.0.104:80/sell/;
     }
     ```

   ![nginx配置.PNG](https://github.com/Leeeeeming/picture/blob/main/nginx%E9%85%8D%E7%BD%AE.PNG?raw=true)

   

2. **修改hosts文件**

   输入命令 ： **ifconfig** ，如下图圈红部分我的虚拟机ip地址为192.168.0.106，注意连接方式选择桥接模式，并选择对应的网卡，可以与本机互ping测试，注意确保本机ip与虚拟机ip在同一网段下

   ![image-20210124145802319](https://github.com/Leeeeeming/picture/blob/main/%E8%99%9A%E6%8B%9F%E6%9C%BAIP%E5%9C%B0%E5%9D%80.png?raw=true)

   在 **C:\Windows\System32\drivers\etc** 地址下使用notepad++打开**hosts**（修改hosts文件需要管理员权限，notepad++会弹窗提示获取）文件，在尾部添加**192.168.0.106 sell.com**使sell.com本地域名映射到对应的ip地址上，这样在主机的浏览器上直接输入sell.com就可以访问虚拟机，并由nginx做消息的转发

<img src="https://github.com/Leeeeeming/picture/blob/main/hosts.PNG?raw=true" style="zoom:67%;" />

#### 2.1.2 内网穿透

在完成微信网页授权请求微信的认证服务器返回code时，微信会回调接口，为了能够让微信端访问到我们本机的tomcat服务器，我们需要使用内网穿透（NATAPP）将本机映射到外网域名中，而且我们必须要在微信公众平台自己的账号下（测试号也可以设置）提前设置好一个外网的域名。

在完成微信支付时，也需要向微信提供一个notify接口，用于支付后微信向tomcat告知支付状态的接口（必须有），也需要使用内网穿透。

本项目中还有很多地方用到了内网穿透，而且本身也不贵（最便宜的一个月12块），下面是在win10平台上开启NATAPP，将

**http://weixin-diancan.natapp1.cc**映射到了本机tomcat服务器

<img src="https://github.com/Leeeeeming/picture/blob/main/natapp.PNG?raw=true" alt="natapp.PNG" style="zoom: 80%;" />

#### 2.1.3 前端请求转发设置

项目中前端有两个地方会触发请求的转发，分别是：

- 登录时，若cookie中没有存储用户的openid，就会重定向到authorized认证接口，向微信请求获取用户openid
- 当用户点击支付按钮后，先经过创建订单，生成订单表和订单详情表后向前端返回success信息和orderId，然后前端重定向到支付接口，完成微信支付

需要在前端index.js更改转发地址

输入命令

```bash
## 定位到index.js
cd /opt/code/sell_fe_buyer/config
## 修改文件
vim index.js
```

修改的内容：注意我填写的是我自己的注册的内网穿透的域名

```bash
sellUrl : 'http://sell.com',
## 微信网页授权接口
openidUrl : 'http://weixin-diancan.natapp1.cc/sell/wechat/authorize',
## 微信支付接口
wechatPayUrl : 'http://weixin-diancan.natapp1.cc/sell/pay/create'
```

![indexjs.PNG](https://github.com/Leeeeeming/picture/blob/main/indexjs.PNG?raw=true)

#### 2.2 手机端代理设置

由于只在电脑上设置了域名和ip的映射， 在手机微信端请求sell.com无法识别虚拟机IP地址

**解决方案：**

首先在win10平台下载fiddler抓包工具，可以抓取手机上的http的请求信息，也可以给手机做代理。然后确保手机和电脑在同一个网络下，然后打开手机wifi设置里面的代理设置，填写主机IP和端口号（fiddler默认端口号为8888）
