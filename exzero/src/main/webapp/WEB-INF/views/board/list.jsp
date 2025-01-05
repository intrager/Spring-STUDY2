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
    <h6 class="m-0 font-weight-bold text-primary">Board List</h6>
  </div>
  <div class="card-body">
    <div class="table-responsive">
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
        <tbody id="eventBody">
          <c:forEach var="board" items="${list}">
            <tr data-bno="${board.bno}">
              <td><c:out value="${board.bno}" /></td>
              <td><c:out value="${board.title}" /></td>
              <td><c:out value="${board.writer}" /></td>
              <td><c:out value="${board.registerDate}" /></td>
              <td><c:out value="${board.updateDate}" /></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
      <div>
        <ul class="pagination">
          <c:if test="${pageMaker.prev}">
            <li class="page-item ">
              <a class="page-link" href="${pageMaker.startPage - 1}" tabindex="-1">Previous</a>
            </li>
          </c:if>
          <c:forEach begin="${pageMaker.startPage}" end="${pageMaker.endPage}" var="num">
            <li class="page-item ${criteria.pageNum == num ? 'active' : ''}">
              <a class="page-link" href="${num}">${num}</a>
            </li>
          </c:forEach>
          <c:if test="${pageMaker.next}">
            <li class="page-item">
              <a class="page-link" href="${pageMaker.endPage + 1}">Next</a>
            </li>
          </c:if>
        </ul>
      </div>
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

<%@include file="../includes/footer.jsp"%>

<script>
  const result = '${result}';
  const myModal = new bootstrap.Modal(document.getElementById('myModal'));
  
  if(result) {
    myModal.show();
  }

  const actionForm = document.getElementById("actionForm");
  
  document.getElementById('eventBody').addEventListener("click", (e) => {
    const target = e.target.closest("tr");
    const bno = target.dataset.bno;
    
    const before = document.getElementById("clonedActionForm");
    
    if(before) {
      before.remove();
    }
    
    const clonedActionForm = actionForm.cloneNode(true);
    clonedActionForm.setAttribute("action", `/board/read/\${bno}`);
    /*
      DOM 엘리먼트 안에 없어서
      Form submission canceled because the form is not connected 발생.
      id 집어넣은 후 body에 appendChild()로 붙여줘야 함.
     */
    clonedActionForm.setAttribute("id", "clonedActionForm");
    document.body.appendChild(clonedActionForm);
    
    clonedActionForm.submit();
  }, false);

  document.querySelector('.pagination').addEventListener("click", (e) => {
    e.preventDefault();
    const target = e.target;
    const targetPage = target.getAttribute("href");

    actionForm.setAttribute("action", "/board/list");
    actionForm.querySelector("input[name='pageNum']").value = targetPage;
    actionForm.submit();
  }, false);
</script>

<%@include file="../includes/end.jsp"%>