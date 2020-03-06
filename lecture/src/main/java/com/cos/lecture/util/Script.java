package com.cos.lecture.util;

public class Script {
	   public static String Back(String msg) {
	      StringBuffer sb = new StringBuffer();
	      sb.append("<script>");
	      sb.append("alert('" + msg + "');");
	      sb.append("history.back();");
	      sb.append("</script>");

	      return sb.toString();
	   }

	   public static String href(String msg, String location) {
	      StringBuffer sb = new StringBuffer();
	      sb.append("<script>"); 
	      sb.append("alert('" + msg + "');");
	      sb.append("location.href = '" + location + "';");
	      sb.append("</script>");

	      return sb.toString();
	   }
	}