<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ include file="include/nav.jsp" %>

<section class="container mt-3" style="max-width: 560px;">
	<form method="get" action="userLoginProc">
		<div class="form-group">
			<label>아이디</label>
			<input type="text" name="userName" class="form-control">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		</div>
		<div class="form-group">
			<label>비밀번호</label>
			<input type="password" name="userPassword" class="form-control">
		</div>
		<button type="submit" class="btn btn-primary">로그인</button>
	</form>	
</section>

<%@ include file="include/footer.jsp" %>