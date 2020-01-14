## mario</br>
## 云漫画源码</br></br>
漫画阅读 可根据规则添加可用漫画网站</br></br>
利用webview jsoup抓取网站数据</br></br>
```/*
** 参数说明
** ========EL
** html解析操作 <div class="div" id="img"> select.div 或 select#img
** @区分操作(select@attr)  !区分操作与参数(get!0)
**
**========nextPageMethod
** 数据翻页
** @#区分操作(javascript或loadUrl) @!区分参数(仅loadUrl时可用 利用url+position加载)
**
*/

{
  "web": "ss",//网页名称，可随意，取用于展示
  "rank": [//排行榜，首页展示
    {
      "url": "", //排行榜地址
      "mainEl": "select!div.main ul a.pic"//获取所有排行榜数据，用于循环获取所有信息
      "linkEl": "attr!href",
      "titleEl": "select!h3@text",
      "imageEl": "select!img@attr!src",
      "introEl": "select!p@get!1@text",
      "prepareMethod": "javascript:change('week')",//网页获取完成后第一个需要的操作
      "nextPageMethod": "javascript@#next_page()"//
    }
  ],
  "classify": [
  //分类
    {
      "title": "冒险",
      "url": "http:",
      "mainEl": "select!div.main ul a.pic",
      "linkEl": "attr!href",
      "titleEl": "select!h3@text",
      "imageEl": "select!img@attr!src",
      "introEl": "select!p@get!0@text",
      "nextPageMethod": "loadUrl@#http://xxxx/lists/15/@!position/"
    }
  ],
  "search": {
  //搜索
    "url": "http://",
    "mainEl": "select!div.main ul a.pic",
    "linkEl": "attr!href",
    "titleEl": "select!h3@text",
    "imageEl": "select!img@attr!src",
    "introEl": "select!p@get!0@text",
    "nextPageMethod": ""
  },
  "detail": {
    //详情页数据
    "detailMainEl": "div.main div.clearfix.section3 div.pic",
    "titleEl": "select!h3@text",
    "imageEl": "select!img@attr!src",
    "authorEl": "select!p@get!0@text",
    "classifyEl": "select!p@get!1@text",
    "popularEl": "select!p@get!2@text",
    "statusEl": "select!p@get!3@text",
    "timeEl": "select!p@get!4@text",
    //集数
    "episodeMainEl": "div#list_block p.zlist a",
    "episodeSort": "negative",//集数排列正序反序列(从上到下,0到100为正序)positive(正)/negative(反)
    "episodeEl": "attr!href"
  },
    "read": {//阅读页面
      "jointUrl": "left@px?t=",//url拼接, left前面right后面mid中间
      "indexEl": "select!div#img_list span@text",
      "imageUrl": "select!div#img_list img@attr!src",
      "introUrl": "select!input#hdTitle2#spt2@src!value",
      "prepareMethod": "",
      "nextPageMethod": "javascript@#pageList()"
    }
}
```
