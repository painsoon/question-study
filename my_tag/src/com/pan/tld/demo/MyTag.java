package com.pan.tld.demo;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

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
			if(name.equals("psn")){
				out.print("Hello， 潘帅！" );
			}
			out.print("Hello! " + name);   
		} catch (Exception e) { 
			throw new JspException(e); 
		}   
		return EVAL_PAGE;   
	}

}
