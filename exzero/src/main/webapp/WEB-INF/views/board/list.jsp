<%--
  Created by IntelliJ IDEA.
  User: intra
  Date: 2025-01-04
  Time: 오전 11:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="../includes/header.jsp"%>

<!-- Page Heading -->
<h1 class="h3 mb-2 text-gray-800">Tables</h1>
<p class="mb-4">DataTables is a third party plugin that is used to generate the demo table below.
  For more information about DataTables, please visit the <a target="_blank" href="https://datatables.net">official DataTables documentation</a>.</p>

<!-- DataTales Example -->
<div class="card shadow mb-4">
  <div class="card-header py-3">
    <h6 class="m-0 font-weight-bold text-primary">DataTables Example</h6>
  </div>
  <div class="card-body">
    <div class="table-responsive">
      <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
        <thead>
          <tr>
            <th>Bno</th>
            <th>Title</th>
            <th>Writer</th>
            <th>RegisterDate</th>
            <th>UpdateDate</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="board" items="${list}">
            <tr>
              <th><c:out value="${board.bno}" /></th>
              <th><c:out value="${board.title}" /></th>
              <th><c:out value="${board.writer}" /></th>
              <th><c:out value="${board.registerDate}" /></th>
              <th><c:out value="${board.updateDate}" /></th>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</div>

<%@include file="../includes/footer.jsp"%>

<script>
  const name = 'AAA';
  
  console.log();
</script>

<%@include file="../includes/end.jsp"%>