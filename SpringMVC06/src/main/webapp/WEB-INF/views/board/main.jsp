<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<c:set var="mvo" value="${SPRING_SECURITY_CONTEXT.authentication.principal}" />
<c:set var="auth" value="${SPRING_SECURITY_CONTEXT.authentication.authorities}" />
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Spring MVC06</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
  <script type="text/javascript">
  	$(document).ready(function() {
  		loadDataList();
  	});
  	
  	function loadDataList() {
  		// 서버와 통신 : 데이터 리스트 가져오기
  		$.ajax({
  			url : "board/all",
  			type : "get",
  			dataType : "JSON",
  			success : makeView,
  			error : function() { alert("error"); }
  		});
  	}
  							  // 		0  1   2
  	function makeView(data) { // data=[{}, {}, {}, ...]
  		var listHTML = "<table class='table table-bordered'>";
  		listHTML += "<tr>";
  		listHTML += "<td>번호</td>";
  		listHTML += "<td>제목</td>";
  		listHTML += "<td>작성자</td>";
  		listHTML += "<td>작성일</td>";
  		listHTML += "<td>조회수</td>";
  		listHTML += "</tr>";
  		$.each(data, function(index, obj) { // obj={"idx":5, "title":"게시판~~"}
  			listHTML += "<tr>";
  	  		listHTML += "<td>" + obj.idx + "</td>";
  	  		listHTML += "<td id='board_title" + obj.idx + "'><a href='javascript:viewContent(" + obj.idx + ")'>" + obj.title + "</a></td>";
  	  		listHTML += "<td>" + obj.writer + "</td>";
  	  		listHTML += "<td>" + obj.indate.split(' ')[0] + "</td>";
  	  		listHTML += "<td id='count" + obj.idx + "'>" + obj.count + "</td>";
  	  		listHTML += "</tr>";
  	  		
  	  		listHTML += "<tr id='content" + obj.idx + "' style='display:none'>";
  	  		listHTML += "<td>내용</td>";
  	  		listHTML += "<td colspan='4'>";
  	  		listHTML += "<textarea id='area" + obj.idx + "' readonly rows='7' class='form-control'></textarea>";
  	  		if("${mvo.member.memID}" == obj.memID) {
	  	  		listHTML += "<br/>";
	 			listHTML += "<span id='update_content" + obj.idx + "'><button class='btn btn-success btn-sm' onClick='updateContentForm(" + obj.idx + ")'>수정화면</button></span>&nbsp";
	 			listHTML += "<button class='btn btn-warning btn-sm' onClick='deleteContent(" + obj.idx + ")'>삭제</button>";
  	  		} else {
	  	  		listHTML += "<br/>";
	 			listHTML += "<span id='update_content" + obj.idx + "'><button class='btn btn-success btn-sm' onClick='updateContentForm(" + obj.idx + ")' disabled>수정화면</button></span>&nbsp";
	 			listHTML += "<button class='btn btn-warning btn-sm' onClick='deleteContent(" + obj.idx + ")' disabled>삭제</button>";	
  	  		}
 			listHTML += "</td>";
  	  		listHTML += "</tr>";
  		});
  		if(${!empty mvo.member}) {
	  		listHTML += "<tr>";
	  		listHTML += "<td colspan='5'>";
	  		listHTML += "<button class='btn btn-primary btn-sm' onClick='transformingForm()'>글쓰기</button>";
	  		listHTML += "</td>";
	  		listHTML += "</tr>";
	  		listHTML += "</table>";
  		}
  		$("#dataList").html(listHTML);
  		viewList();
  	}
  							  
  	function transformingForm() {
  		$("#dataList").css("display", "none"); // 감춘다
  		$("#writeForm").css("display", "block"); // 보여준다
  	}
  	
  	function viewList() {
  		$("#dataList").css("display", "block"); // 감춘다
  		$("#writeForm").css("display", "none"); // 보여준다
  	}
  	
  	function insertBoard() {
  		var formData = $("#registerForm").serialize();
  		var csrfHeaderName = "${_csrf.headerName}";
  		var csrfTokenValue ="${_csrf.token}";
  		
  		$.ajax({
  			url : "board/new",
  			type : "post",
  			data : formData,
  			success : loadDataList,
  			beforeSend : function(xhr) {
  				xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
  			},
  			error : function() { alert("error"); }
  		});
  		
  		$("#clearForm").trigger("click");
  	}
  	
  	function viewContent(idx) {
  		var csrfHeaderName = "${_csrf.headerName}";
  		var csrfTokenValue = "${_csrf.token}";
  		
  		if($("#content"+idx).css("display") == "none") { // 열릴 때
  			$.ajax({
  				url : `board/\${idx}`,
  				type : "get",
  				dataType : "json",
  				success : function(data) {   // data={ ... , "content" : ~~~`}
  					$("#area"+idx).val(data.content);
  				},
  				error : function() { alert("error"); }
  			});
  			
  			$("#content"+idx).css("display", "table-row");
  			$("#area"+idx).attr("readonly", true);
  		} else {  // 닫힐 때
  			$("#content"+idx).css("display", "none");
  			
  			$.ajax({
  				url : `board/count/\${idx}`,
  				type : "put",
  				dataType : "json",
  				beforeSend : function(xhr) {
  					xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
  				},
  				success : function(data) {
  					$("#count"+idx).text(data.count); // html도 가능
  				},
  				error : function() {alert("error");}
  			});
  		}
  	}
  	
  	function deleteContent(idx) {
  		var csrfHeaderName = "${_csrf.headerName}";
  		var csrfTokenValue = "${_csrf.token}";
  		
  		$.ajax({
  			url : `board/\${idx}`,
  			type : "delete",
  			beforeSend : function(xhr) {
  				xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
  			},
  			success : loadDataList,
  			error : function() { alert("error"); }
  		});
  	}
  	
  	function updateContentForm(idx) {
  		$("#area"+idx).attr("readonly", false);
  		var title = $("#board_title"+idx).text();
  		var newInputTag = "<input type='text' id='new_title" + idx + "' class='form-control' value='" + title + "'/>";
  		$("#board_title"+idx).html(newInputTag);
  		
  		var newButton = "<button class='btn btn-primary btn-sm' onClick='updateContent(" + idx + ")'>수정</button>";
  		$("#update_content"+idx).html(newButton);
  	}
  	
  	function updateContent(idx) {
  		var title = $("#new_title"+idx).val();
  		var content = $("#area"+idx).val();
  		var csrfHeaderName = "${_csrf.headerName}";
  		var csrfTokenValue = "${_csrf.token}";
  		
  		$.ajax({
  			url : "board/update",
  			type : "patch",
  			contentType : 'application/json;charset=utf-8',
  			data : JSON.stringify({"idx" : idx, "title" : title, "content" : content}),
  			beforeSend : function(xhr) {
  				xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
  			},
  			success : loadDataList,
  			error : function() { alert("error"); }
  		});
  	}
  </script>
</head>
<body>
<div class="container">
  <jsp:include page="../common/header.jsp"></jsp:include>
  <h3>회원 게시판</h3>
  <div class="panel panel-default">
    <div class="panel-heading">BOARD</div>
    <div class="panel-body" id="dataList">Panel Content</div>
    <div class="panel-body" id="writeForm" style="display: none">
		<form id="registerForm">
			<input type="hidden" id="memID" name="memID" value="${mvo.member.memID}" />
			<table class="table table-bordered">
				<tr>
					<td>제목</td>
					<td><input type="text" id="title" name="title" class="form-control" /></td>
				</tr>
				<tr>
					<td>내용</td>
					<td><textarea rows="7" id="content" name="content" class="form-control"></textarea></td>
				</tr>
				<tr>
					<td>작성자</td>
					<td><input type="text" id="writer" name="writer" class="form-control" value="${mvo.member.memName}" readonly /></td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<button class="btn btn-success btn-sm" onClick="insertBoard()">등록</button>
						<button type="reset" id="clearForm" class="btn btn-warning btn-sm">취소</button>
						<button type="button" class="btn btn-info btn-sm" onClick="viewList()">목록보기</button>
					</td>
				</tr>
			</table>
		</form>
	</div>
    <div class="panel-footer">다무신사랑해</div>
  </div>
</div>

</body>
</html>
