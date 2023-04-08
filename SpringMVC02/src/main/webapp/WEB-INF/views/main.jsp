<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Spring MVC02</title>
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
  			url : "boardList.do",
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
  	  		listHTML += "<td>" + obj.title + "</td>";
  	  		listHTML += "<td>" + obj.writer + "</td>";
  	  		listHTML += "<td>" + obj.indate + "</td>";
  	  		listHTML += "<td>" + obj.count + "</td>";
  	  		listHTML += "</tr>";
  		});
  		listHTML += "</table>";
  		$("#dataList").html(listHTML);
  	}
  </script>
</head>
<body>
 
<div class="container">
  <h2>Spring MVC02</h2>
  <div class="panel panel-default">
    <div class="panel-heading">BOARD</div>
    <div class="panel-body" id="dataList">Panel Content</div>
    <div class="panel-body" >Panel Content</div>
    <div class="panel-footer">다무신사랑해</div>
  </div>
</div>

</body>
</html>
