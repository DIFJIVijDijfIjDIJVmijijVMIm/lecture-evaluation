<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="include/nav.jsp" %>

<section class="container mt-3" style="max-width: 560px;">
	<form method="post" action="userJoinProc">
		<div class="form-group">
			<label>아이디</label>
			<input type="text" name="userName" required="required" class="form-control">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		</div>		
		<div class="form-group">
			<label>비밀번호</label>
			<input type="password" name="userPassword" required="required" class="form-control">
		</div>
		<div class="form-group">
			<label>이메일</label>
			<input type="email" name="userEmail" required="required" class="form-control">
			<input type="hidden" name="userEmailChecked" value="false">
		</div>
		<button type="submit" class="btn btn-primary">회원가입</button>
	</form>	
</section>

<%@ include file="include/footer.jsp" %>