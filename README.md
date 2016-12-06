# AndroidJsoup
研究使用解析网页数据，并将主要信息存储到本地数据库中，方便在电脑上查询。

## 参考资料
1. jsoup 官网地址

    `https://jsoup.org/`

2. jsoup Cookbook(中文版)

    `http://www.open-open.com/jsoup/`
3. jsoup 下载地址

    `https://jsoup.org/news/`


## 一些小技巧

### 使用说明
  官方文档里已经说的比较清楚，需要的知识点也不多。
  这里就不再絮叨一步步该怎么使用了，大家参考文档就可以了。
  下面简单说下需要的知识和一些小技巧。
  
1. html的结构
  Docment :整个页面
  Element:某一个节点
  
2. CSS选择器
    css里的选择器要熟悉(不熟悉也没关系，还有其他解析的方法)
    
3. 逻辑思路
  因为html页面通常是各种嵌套，所以头脑要清晰
  
4. 地址
  http://www.open-open.com/jsoup/parsing-a-document.htm
这里是中文文档，通过目录大家可以看出，没有太多难点。

https://jsoup.org/
这是官方网站

![ZhuoKeTeam1.png](http://upload-images.jianshu.io/upload_images/1383797-fa7be66f3cbf3875.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 一些小技巧

拿Chrome说明

假如我要抓取这个页面的某些数据
  https://gupiao.caimao.com/weixin/note/reader/view/53103
  
抓取画框里的数据，
一个Name，一个预期收益

![ZhuoKeTeam2.png](http://upload-images.jianshu.io/upload_images/1383797-834ff55071f8c35d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 通用步骤:

1. 在浏览器里按下F12查看html源码，点击这个按钮，然后点击想要的数据。
![Paste_Image.png](http://upload-images.jianshu.io/upload_images/1383797-e662a583add707c8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

2. 看右下角的框里。就是我们需要的css选择器语法。
![Paste_Image.png](http://upload-images.jianshu.io/upload_images/1383797-ee6953f64b2430ff.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
3. 在整个页面的环境下对数据进行分析
    分析要点:
    * 这个语法能不能唯一确定这个数据
    * 是否确定的为一个列表型的数据(不单指ul,il这样的)
    * 在Jsoup中的应用
    * 应对其他的问题

4. 举例说明

一、Name数据

![Paste_Image.png](http://upload-images.jianshu.io/upload_images/1383797-b2ea300efd35e033.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

进行完通用步骤的前两步后，第三步就要开始了。
  
1. 首先看这个语法能否唯一确定这个数据，最好的办法是让代码给我们测一下.
因为选择器选择后返回的是Elements，查看它的size就可以确定是否唯一。


     Document doc = Jsoup.parse(new URL(
					"https://gupiao.caimao.com/weixin/note/reader/view/53103"),
					6000);
			Elements elements = doc
					.body()
					.select("div#doc_section.doc_section.show_foot > div.user_card.user_masthead > a.clear_fix > p.name > span");
			System.out.println(elements.size());

好的，size为1，通过
![Paste_Image.png](http://upload-images.jianshu.io/upload_images/1383797-996d46724cf2c74f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

  ##### Tip:" > "这个符号的左右是有空格的
2. 唯一确定这个数据后，后面就几乎没问题了。
然后接first()再取其值:

        Element element = doc
					.body()
					.select("div#doc_section.doc_section.show_foot > div.user_card.user_masthead > a.clear_fix > p.name > span")
					.first();
			String text = element.text();
			System.out.println(text);

这样就拿到了第一个数据


二、预期收益
![Paste_Image.png](http://upload-images.jianshu.io/upload_images/1383797-edfd75ebbe4afda7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


1. 进行分析，还是那个小技巧，把右下角的css选择器放进代码中进行查看。
         Document doc = Jsoup.parse(new URL(
					"https://gupiao.caimao.com/weixin/note/reader/view/53103"),
					6000);
			Elements elements=doc.body().select("div#doc_section.doc_section.show_foot > div.note_detail > div.grid > p.font_red");
			System.out.println(elements.size());
发现是2，也就是这个选择语句不能唯一确定数据，那么就要另加分析了

观察数据附近和这个页面。

![Paste_Image.png](http://upload-images.jianshu.io/upload_images/1383797-d1f7315cf45f703b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

观察左边这个框，发现我们需要的数据周围有跟他样式相同的，然后看代码，也就是右边的框
这个结构是 一个大的div里包含了三个小的div，而且他们的class值是相同的，前两个里面的p标签的class值也是相同的。
这样我们可以确定，右下角那个选择语句选择的是这两个p标签，那么我们需要的是后面的那个，所以在jsoup中就可以用last()来确定，这样就唯一确定了我们需要的数据位置。

    Document doc = Jsoup.parse(new URL(
					"https://gupiao.caimao.com/weixin/note/reader/view/53103"),
					6000);
			Element element = doc
					.body()
					.select("div#doc_section.doc_section.show_foot > div.note_detail > div.grid > p.font_red")
					.last();
			String text = element.text();
			System.out.println(text);

##### Tip:这个数据就属于列表型的，选择器选择后可以用first()，last()或者get(int)来选择数据的位置

###  总结
jsoup本身不难，就是要勤加分析需要数据在整个页面的环境，以便于确定。
拿不准的数据可以放进代码里测一下，再复杂的html也都是相同的逻辑。

## 结束语
哎，前一个多小时写了好长，结果好像没保存还是咋的没能发布出去。
又写一遍省去了许多唠叨的地方，整体上还是有点唠叨的，其实就是分析的过程。
一篇渣文，希望对你有所帮助。
