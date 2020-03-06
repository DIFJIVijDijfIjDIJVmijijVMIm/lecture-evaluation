<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="include/nav.jsp" %>

<section class="container">
	<form action="/evalSearch" method="get" class="form-inline mt-3">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<select name="lectureDivide" class="form-control mx-1 mt-2">
			<option value="전체" >전체</option>
			<option value="전공" <c:if test="${sessionScope.lectureDivide eq '전공'}">selected</c:if> >전공</option>
			<option value="교양" <c:if test="${sessionScope.lectureDivide eq '교양'}">selected</c:if> >교양</option>
			<option value="기타" <c:if test="${sessionScope.lectureDivide eq '기타'}">selected</c:if> >기타</option>
		</select>
		<select name="searchType" class="form-control mx-1 mt-2">
			<option value="최신순">최신순</option>
			<option value="추천순" <c:if test="${sessionScope.searchType eq '추천순'}">selected</c:if>>추천순</option>
		</select>
		<input type="text" name="searchText" class="form-control mx-1 mt-2" placeholder="내용을 입력하세요.">
		<input type="hidden" name="page" value="1">
		<button type="submit" class="btn btn-primary mx-1 mt-2">검색</button>
		<a class="btn btn-primary mx-1 mt-2" data-toggle="modal" href="#registerModal">등록하기</a>
		<a class="btn btn-danger mx-1 mt-2" data-toggle="modal" href="#reportModal">신고</a>
	</form>	
	<c:forEach var="eval" items="${eval}" varStatus="status">
		<div class="card bg-light mt-3">
			<div class="card-header bg-light">
				<div class="row">
					<div class="col-8 text-left">${eval.lectureName}&nbsp;<small>${eval.professorName}</small></div>
					<div class="col04 text-right">
						총합 <span style="color: red;">${eval.totalScore}</span>
					</div>
				</div>
			</div>
			<div class="card-body">
				<h5 class="card-title">
					${eval.evaluationTitle} <small>(${eval.lectureYear}년 ${eval.semesterDivide})</small>
				</h5>
				<p class="card-text">${eval.evaluationContent}</p>
				<div class="row">
					<div class="col-9 text-left">
						성적 <span style="color: red;">${eval.creditScore}</span>
						널널 <span style="color: red;">${eval.comfortableScore}</span>
						강의 <span style="color: red;">${eval.lectureScore}</span>
						<span style="color: green;">(추천: ${eval.likeCount}★)</span>
					</div>
					<c:choose>
						<c:when test="${empty sessionScope.user.userName}">
							<div class="col-3 text-right">
								<a onclick="return alert('로그인 해주십시오')" href="/home?page=1">추천</a>
								<a onclick="return alert('로그인 해주십시오')" href="/home?page=1">삭제</a>
							</div>
						</c:when>
						<c:otherwise>
							<div class="col-3 text-right">
								<a onclick="return confirm('추천하시겠습니까?')" href="/evalLike/${eval.id}/${sessionScope.user.id}">추천</a>
								<a onclick="return confirm('삭제하시겠습니까?')" href="/evalDelete/${eval.id}/${sessionScope.user.id}">삭제</a>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</c:forEach>	
</section>
<ul class="pagination justify-content-center mt-3">
		<c:choose>
			<c:when test="${sessionScope.searching eq 'searching'}">
				<li class="page-item">
					<c:choose>
						<c:when test="${sessionScope.page == 1}">
							<a class="page-link disabled" >이전</a>
						</c:when>
						<c:otherwise>
							<a class="page-link" href="/evalSearch?page=${sessionScope.page-1}&lectureDivide=${sessionScope.lectureDivide}&searchType=${sessionScope.searchType}&searchText=${sessionScope.searchText}">이전</a>
						</c:otherwise>
					</c:choose>
				</li>
				<c:forEach begin="1" end="${sessionScope.evalSize}" var="page">
					<li class="page-item">
						<a class="page-link" href="/evalSearch?page=${page}&lectureDivide=${sessionScope.lectureDivide}&searchType=${sessionScope.searchType}&searchText=${sessionScope.searchText}">${page}</a>
					</li>
				</c:forEach>
				<li class="page-item">
					<c:choose>
						<c:when test="${sessionScope.page == sessionScope.evalSize}">
							<a class="page-link disabled" >다음</a>
						</c:when>
						<c:otherwise>
							<a class="page-link" href="/evalSearch?page=${sessionScope.page+1}&lectureDivide=${sessionScope.lectureDivide}&searchType=${sessionScope.searchType}&searchText=${sessionScope.searchText}">다음</a>
						</c:otherwise>
					</c:choose>
				</li>
			</c:when>
			<c:otherwise>
				<li class="page-item">
					<c:choose>
						<c:when test="${sessionScope.page == 1}">
							<a class="page-link disabled" >이전</a>
						</c:when>
						<c:otherwise>
							<a class="page-link" href="/home?page=${sessionScope.page-1}">이전</a>
						</c:otherwise>
					</c:choose>
				</li>
				<c:forEach begin="1" end="${sessionScope.evalSize}" var="page">
					<li class="page-item">
						<a class="page-link" href="/home?page=${page}">${page}</a>
					</li>
				</c:forEach>
				<li class="page-item">
					<c:choose>
						<c:when test="${sessionScope.page == sessionScope.evalSize}">
							<a class="page-link disabled" >다음</a>
						</c:when>
						<c:otherwise>
							<a class="page-link" href="/home?page=${sessionScope.page+1}">다음</a>
						</c:otherwise>
					</c:choose>
				</li>
			</c:otherwise>
	</c:choose>
</ul>
<div class="modal fade" id="registerModal" tabindex="-1" role="dialog" aria-labelledby="modal" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="modal">평가 등록</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">					
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<c:choose>
				<c:when test="${empty sessionScope.user.userName}">
					<div class="modal-body">
						<div class="form-row">
							<span>로그인을 해야 등록하기를 할 수 있습니다.</span>
						</div>
					</div>							
				</c:when>
				<c:otherwise>
					<div class="modal-body">
						<form action="/evalRegister/${sessionScope.user.id}" method="post">
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
							<div class="form-row">
								<div class="form-group col-sm-6">
									<label>강의명</label>
									<input type="text" name="lectureName" class="form-control" required="required"  maxlength="20">
								</div>
								<div class="form-group col-sm-6">
									<label>교수명</label>
									<input type="text" name="professorName" class="form-control" required="required"  maxlength="20">
								</div>
							</div>
							<div class="form-row">
								<div class="form-group col-sm-4">
									<label>수강 연도</label>
									<select name="lectureYear" class="form-control">
										<option value="2011">2011</option>
										<option value="2012">2012</option>
										<option value="2013">2013</option>
										<option value="2014">2014</option>
										<option value="2015">2015</option>
										<option value="2016">2016</option>
										<option value="2017">2017</option>
										<option value="2018">2018</option>
										<option value="2019">2019</option>
										<option value="2020" selected>2020</option>
										<option value="2021">2021</option>
										<option value="2022">2022</option>
										<option value="2023">2023</option>
									</select>
								</div>
								<div class="form-group col-sm-4">
									<label>수강 학기</label>
									<select name="semesterDivide" class="form-control">
										<option value="1학기" selected>1학기</option>
										<option value="여름학기">여름학기</option>
										<option value="2학기">2학기</option>
										<option value="겨울학기">겨울학기</option>
									</select>
								</div>
								<div class="form-group col-sm-4">
									<label>강의 구분</label>
									<select name="lectureDivide" class="form-control">
										<option value="전공" selected>전공</option>
										<option value="교양">교양</option>
										<option value="기타">기타</option>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label>제목</label>
								<input type="text" name="evaluationTitle" class="form-control" required="required"  maxlength="30">
							</div>
							<div class="form-group">
								<label>내용</label>
								<textarea name="evaluationContent" class="form-control" required="required"  maxlength="2048" style="height: 180px;"></textarea>
							</div>
							<div class="form-row">
								<div class="form-group col-sm-3">
									<label>총합</label>
									<select name="totalScore" class="form-control">
										<option value="A" selected>A</option>
										<option value="B">B</option>
										<option value="C">C</option>
										<option value="D">D</option>
										<option value="F">F</option>
									</select>
								</div>
								<div class="form-group col-sm-3">
									<label>성적</label>
									<select name="creditScore" class="form-control">
										<option value="A" selected>A</option>
										<option value="B">B</option>
										<option value="C">C</option>
										<option value="D">D</option>
										<option value="F">F</option>
									</select>
								</div>
								<div class="form-group col-sm-3">
									<label>널널</label>
									<select name="comfortableScore" class="form-control">
										<option value="A" selected>A</option>
										<option value="B">B</option>
										<option value="C">C</option>
										<option value="D">D</option>
										<option value="F">F</option>
									</select>
								</div>
								<div class="form-group col-sm-3">
									<label>강의</label>
									<select name="lectureScore" class="form-control">
										<option value="A" selected>A</option>
										<option value="B">B</option>
										<option value="C">C</option>
										<option value="D">D</option>
										<option value="F">F</option>
									</select>
								</div>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
								<button type="submit" class="btn btn-primary">등록하기</button>
							</div>
						</form>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
<div class="modal fade" id="reportModal" tabindex="-1" role="dialog" aria-labelledby="modal" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="modal">신고하기</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<c:choose>
				<c:when test="${empty sessionScope.user.userName}">
					<div class="modal-body">
						<div class="form-row">
							<span>로그인을 해야 신고하기를 할 수 있습니다.</span>
						</div>
					</div>							
				</c:when>
				<c:otherwise>
					<div class="modal-body">
						<form action="/reportSend/${sessionScope.user.userEmail}" method="post">
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
							<div class="form-group">
								<label>신고 제목</label>
								<input type="text" name="reportTitle" class="form-control" maxlength="30">
							</div>
							<div class="form-group">
								<label>신고 내용</label>
								<textarea name="reportContent" class="form-control" maxlength="2048" style="height: 180px;"></textarea>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
								<button type="submit" class="btn btn-danger">신고하기</button>
							</div>
						</form>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>

<%@ include file="include/footer.jsp" %>