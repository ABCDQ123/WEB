## WebView jsoup</br>
## WebView</br></br>
webview jsoup</br></br>
## 规则：
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
  "web": "ss",//网页
  "rank": [//首页
    {
      "url": "", //排行
      "mainEl": "select!div.main ul a.pic"//排行数据
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
      "title": "??",
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
  //search
    "url": "http://",
    "mainEl": "select!div.main ul a.pic",
    "linkEl": "attr!href",
    "titleEl": "select!h3@text",
    "imageEl": "select!img@attr!src",
    "introEl": "select!p@get!0@text",
    "nextPageMethod": ""
  },
  "detail": {
    //详情
    "detailMainEl": "div.main div.clearfix.section3 div.pic",
    "titleEl": "select!h3@text",
    "imageEl": "select!img@attr!src",
    "authorEl": "select!p@get!0@text",
    "classifyEl": "select!p@get!1@text",
    "popularEl": "select!p@get!2@text",
    "statusEl": "select!p@get!3@text",
    "timeEl": "select!p@get!4@text",
    //排序数
    "episodeMainEl": "div#list_block p.zlist a",
    "episodeSort": "negative",//排列正序反序列(从上到下,0到100为正序)positive(正)/negative(反)
    "episodeEl": "attr!href"
  },
    "read": {//内容
      "jointUrl": "left@px?t=",//url拼接, left right mid
      "indexEl": "select!div#img_list span@text",
      "imageUrl": "select!div#img_list img@attr!src",
      "introUrl": "select!input#hdTitle2#spt2@src!value",
      "prepareMethod": "",
      "nextPageMethod": "javascript@#pageList()"
    }
}
```
