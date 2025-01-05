<%--
  Created by IntelliJ IDEA.
  User: intra
  Date: 2025-01-03
  Time: 오후 11:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="../includes/header.jsp"%>

<!-- Page Heading -->
<h1 class="h3 mb-2 text-gray-800">Read</h1>
<p class="mb-4">DataTables is a third party plugin that is used to generate the demo table below.
  For more information about DataTables, please visit the <a target="_blank" href="https://datatables.net">official DataTables documentation</a>.</p>

<!-- DataTales Example -->
<div class="card shadow mb-4">
  <div class="card-header py-3">
    <h6 class="m-0 font-weight-bold text-primary">Board Read</h6>
  </div>
  <div class="card-body">
    <div class="input-group input-group-lg">
      <div class="input-group-prepend">
        <span class="input-group-text">Bno</span>
      </div>
      <input type="text" name="bno" class="form-control" value="<c:out value="${vo.bno}"/>" readonly />
    </div>
    <div class="input-group input-group-lg">
      <div class="input-group-prepend">
        <span class="input-group-text">Title</span>
      </div>
      <input type="text" name="title" class="form-control" value="<c:out value="${vo.title}"/>" readonly />
    </div>
    <div class="input-group input-group-lg">
      <div class="input-group-prepend">
        <span class="input-group-text">Content</span>
      </div>
      <input type="text" name="content" class="form-control" value="<c:out value="${vo.content}"/>" readonly />
    </div>
    <div class="input-group input-group-lg">
      <div class="input-group-prepend">
        <span class="input-group-text">Writer</span>
      </div>
      <input type="text" name="writer" class="form-control" value="<c:out value="${vo.writer}"/>" readonly />
    </div>
    <div class="input-group input-group-lg">
      <div class="input-group-prepend">
        <span class="input-group-text">RegisterDate</span>
      </div>
      <input type="text" name="registerDate" class="form-control" value="<c:out value="${vo.registerDate}"/>" readonly />
    </div>
    <div class="input-group input-group-lg">
      <button type="submit" id="list" class="btn btn-info">LIST</button>
      <button type="submit" id="modify" class="btn btn-warning">MODIFY</button>
    </div>
  </div>
</div>

<div id='myModal' class="modal" tabindex="-1">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Modal title</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <p>Modal body text goes here.</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div>

<form id="actionForm" method="get" action="/board/list">
  <input type="hidden" name="pageNum" value="<c:out value="${criteria.pageNum}"/>" />
  <input type="hidden" name="amount" value="<c:out value="${criteria.amount}"/>" />
  <c:if test="${criteria.types != null && criteria.keyword != null}">
    <c:forEach var="type" items="${criteria.types}">
      <input type="hidden" name="types" value="${type}" />
    </c:forEach>
    <input type="hidden" name="keyword" value="<c:out value="${criteria.keyword}"/>" />
  </c:if>
</form>

<%@include file="../includes/footer.jsp"%>

<script>
  const result = '${result}';
  const myModal = new bootstrap.Modal(document.getElementById('myModal'));
  if(result) {
    myModal.show();
  }
  
  const actionForm = document.getElementById("actionForm");
  const bno = '${vo.bno}';
  
  document.getElementById('list').addEventListener("click", (e) => {
    actionForm.setAttribute("action", "/board/list");
    actionForm.submit();
  }, false);
  
  document.getElementById('modify').addEventListener("click", (e) => {
    actionForm.setAttribute("action", `/board/modify/\${bno}`);
    actionForm.submit();
  }, false);
</script>

<%@include file="../includes/end.jsp"%>
