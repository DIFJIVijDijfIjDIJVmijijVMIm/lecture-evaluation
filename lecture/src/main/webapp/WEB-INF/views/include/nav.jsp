<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>강의평가 웹 사이트</title>
<link rel="stylesheet" href="../css/bootstrap.min.css">
<link rel="stylesheet" href="../css/custom.css">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
	<a class="navbar-brand" href="/">강의평가 웹 사이트</a>
	<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbar" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div id="navbar" class="collapse navbar-collapse">
		<ul class="navbar-nav mr-auto">
			<li class="nav-item active">
				<a class="nav-link" href="/">메인</a>
			</li>
			<li class="nav-item dropdown">
				<a class="nav-link dropdown-toggle" id="dropdown" data-toggle="dropdown">
					회원 관리
				</a>
				<div class="dropdown-menu" aria-labelledby="dropdown">
					<c:choose>
						<c:when test="${empty sessionScope.user.userName}">
							<a class="dropdown-item" href="/userLogin">로그인</a>
							<a class="dropdown-item" href="/userJoin">회원가입</a>
						</c:when>
						<c:otherwise>
							<a class="dropdown-item" href="/userLogout">로그아웃</a>
						</c:otherwise>
					</c:choose>
				</div>
			</li>
			<li class="nav-item active">
				<a class="nav-link" href="/root/log">${sessionScope.user.userName}</a>
			</li>
		</ul>
	</div>
</nav>