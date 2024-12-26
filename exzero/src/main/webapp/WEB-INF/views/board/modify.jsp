<%--
  Created by IntelliJ IDEA.
  User: intra
  Date: 2025-01-04
  Time: 오전 12:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="../includes/header.jsp"%>

<!-- DataTales Example -->
<div class="card shadow mb-4">
  <div class="card-header py-3">
    <h6 class="m-0 font-weight-bold text-primary">Board Read</h6>
  </div>
  <div class="card-body">
    <form id="actionForm" method="post">
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
        <input type="text" name="title" class="form-control" value="<c:out value="${vo.title}"/>" />
      </div>
      <div class="input-group input-group-lg">
        <div class="input-group-prepend">
          <span class="input-group-text">Content</span>
        </div>
        <input type="text" name="content" class="form-control" value="<c:out value="${vo.content}"/>" />
      </div>
      <div class="input-group input-group-lg">
        <div class="input-group-prepend">
          <span class="input-group-text">Writer</span>
        </div>
        <input type="text" class="form-control" value="<c:out value="${vo.writer}"/>" readonly />
      </div>
      <div class="input-group input-group-lg">
        <div class="input-group-prepend">
          <span class="input-group-text">RegisterDate</span>
        </div>
        <input type="text" class="form-control" value="<c:out value="${vo.registerDate}"/>" readonly />
      </div>
      <div class="input-group input-group-lg">
        <button type="submit" id="list" class="btn btn-info">LIST</button>
        <button type="submit" id="modify" class="btn btn-warning">MODIFY</button>
        <button type="submit" id="remove" class="btn btn-danger">REMOVE</button>
      </div>
    </form>
  </div>
</div>

<%@include file="../includes/footer.jsp"%>

<script>
  const bno = '${vo.bno}';
  const actionForm = document.getElementById('actionForm');
  
  document.getElementById('list').addEventListener("click", (e) => {
    window.location = "/board/list";
  }, false);

  document.getElementById('modify').addEventListener("click", (e) => {
    e.preventDefault();
    e.stopPropagation();
    
    actionForm.action = `/board/modify/\${bno}`;
    actionForm.method = 'post';
    actionForm.submit();
  }, false);
  
  document.getElementById('remove').addEventListener("click", (e) => {
    e.preventDefault();
    e.stopPropagation();
    
    actionForm.action = `/board/remove/\${bno}`;
    actionForm.method = 'post';
    actionForm.submit();
  });
</script>

<%@include file="../includes/end.jsp"%>
