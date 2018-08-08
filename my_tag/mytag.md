###   

当我们在写页面时会引入一些标签库很帅的功能就有了，比如JSTL实现表达式、流程、循环、URL控制等，shiro标签库用来做权限控制等。
是否有想过标签这东西是怎么起作用的？自己写个标签库梳理一下原理记录一下，虽然很简单但是解决了心中很久以来的疑惑。
* 理论依据：当web容器启动时会将jsp转化为servlet编译成class，当有情求时jsp容器执行到taglib时会搜索WEB-INF/META-INF/*.tld文件，
根据uri创建tld的映射关系。tld是标签库的描述文件。找到对应的<tag>标签，读到<tag-class>内容找到相应的tag handler的对象的操作。
标签被拦截后利用反射得到属性值做一些业务操作复杂可以关联数据库然后把结果返回给页面。


### 标签处理类
标签处理类必须继承TagSupport类，一般情况下覆写doStartTag 或 doEndTag就够啦。
当jsp解析这个标签的时候，在“<”处触发 doStartTag 事件，在“>”时触发 doEndTag 事件。通常在 doStartTag 里进行初始化,流程选择操作，在 doEndTag 里后续页面输出控制。 
```
public class MyTag extends TagSupport {
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int doEndTag() throws JspException {   
		try {   
			JspWriter out = pageContext.getOut();   
      //处理各种业务需求
			if(name.equals("psn")){
				out.print("Hello， 潘帅！" );
			}else{
				out.print("Hello! " + name);   
			}
		} catch (Exception e) { 
			throw new JspException(e); 
		}   
		return EVAL_PAGE;   
	}

}
```
*  标签处理程序

　　int doStartTag() throws JspException---处理开始标签

　　int doEndTag() throws JspException---处理结束标签

　　Tag getParent()/void setParent(Tag t)---获得/设置标签的父标签

　　void setPageContext(PageContext pc)--- pageContext 属性的 setter 方法

　　void release() 释放获得的所有资源

　doStartTag()和doEndTag()方法的返回值说明：

　　SKIP_BODY　　　　　 表示不用处理标签体，直接调用doEndTag()方法。

　　SKIP_PAGE　　　　　 忽略标签后面的jsp(SUN企业级应用的首选)内容。

　　EVAL_PAGE　　　　　 处理标签后，继续处理jsp(SUN企业级应用的首选)后面的内容。

　　EVAL_BODY_BUFFERED 表示需要处理标签体，且需要重新创建一个缓冲(调用setBodyContent方法)。

　　EVAL_BODY_INCLUDE　 表示在现有的输出流对象中处理标签体，但绕过setBodyContent()和doInitBody()方法

　　EVAL_BODY_AGAIN　　　　 对标签体循环处理。(存在于javax.servlet.jsp.tagext.IterationTag接口中)
  
  ### tld文件 描述标签库
  
  ```
  <short-name>test</short-name>   <!--指定Tag Library默认的前缀名(prefix)-->
	
	<tag>   
		<name>outme</name>          <!-- 设定Tag的名字  -->
		<tag-class>com.pan.tld.demo.MyTag</tag-class>    <!-- 设定Tag的处理类 -->
		
<!-- 		设定标签的主体(body)内容: empty：表示标签中没有body；  -->
<!-- 		JSP：表示标签的body中可以加入JSP程序代码；  -->
<!-- 		tagdependent：表示标签中的内容由标签自己去处理 -->
		<body-content>empty</body-content>       
		 
		<attribute>   
			<name>name</name>   <!-- 属性名称 -->
			<required>false</required>   <!-- 属性是否必需的，默认为false -->
			<rtexprvalue>false</rtexprvalue>   <!-- 属性值是否可以为request-time表达式，也就是类似于< %=…% >的表达式 -->
		</attribute>   
	</tag>   
  ```
  
  ###  jsp中引用
  
  ```
  <html>
<%@ taglib uri="/WEB-INF/tld/out.tld" prefix="test"%>   
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

 <test:outme name="qqq"/>   

</body>
</html>
```
  
  
  
  




