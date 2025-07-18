<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${projeto.id == null ? 'Novo' : 'Editar'}"/> Projeto</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <style>body {
        background-color: #f8f9fa
    }</style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
    <div class="container-fluid">
        <a class="navbar-brand text-primary" href="${pageContext.request.contextPath}/">
            <i class="bi bi-kanban"></i> Portfólio
        </a>
    </div>
</nav>

<div class="container py-4">
    <h2 class="mb-4 text-primary"><c:out value="${projeto.id == null ? 'Novo Projeto' : 'Editar Projeto'}"/></h2>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger" role="alert">
                ${erro}
        </div>
    </c:if>

    <form method="post"
          action="${pageContext.request.contextPath}/projetos${not empty projeto.id ? '/' : ''}${not empty projeto.id ? projeto.id : ''}"
          class="row g-3">

        <c:if test="${not empty projeto.id}">
            <input type="hidden" name="_method" value="PUT"/>
            <input type="hidden" name="id" value="${projeto.id}"/>
        </c:if>

        <div class="col-md-6">
            <label class="form-label">Nome</label>
            <input type="text" name="nome" value="${projeto.nome}" class="form-control" required/>
        </div>

        <div class="col-md-3">
            <label class="form-label">Data Início</label>
            <input type="date" name="dataInicio" value="${projeto.dataInicio}" class="form-control" required/>
        </div>

        <div class="col-md-3">
            <label class="form-label">Previsão Fim</label>
            <input type="date" name="dataPrevisaoFim" value="${projeto.dataPrevisaoFim}" class="form-control" required/>
        </div>

        <div class="col-md-6">
            <label class="form-label">Gerente</label>
            <select name="gerenteId" class="form-select" required>
                <c:forEach items="${gerentes}" var="g">
                    <option value="${g.id}" ${g.id == projeto.gerente.id ? 'selected' : ''}>${g.nome}</option>
                </c:forEach>
            </select>
        </div>

        <div class="col-md-3">
            <label class="form-label">Orçamento</label>
            <input type="number" step="0.01" name="orcamento" value="${projeto.orcamento}" class="form-control"
                   required/>
        </div>

        <div class="col-md-3">
            <label class="form-label">Risco</label>
            <select name="risco" class="form-select">
                <c:forEach items="${riscos}" var="r">
                    <option value="${r}" ${r == projeto.risco ? 'selected' : ''}>${r}</option>
                </c:forEach>
            </select>
        </div>

        <!-- Status (apenas em edição) -->
        <c:if test="${not empty projeto.id}">
            <div class="col-md-4">
                <label class="form-label">Status</label>
                <select name="status" class="form-select">
                    <c:forEach items="${statusList}" var="s">
                        <option value="${s}" ${s == projeto.status ? 'selected' : ''}>${s}</option>
                    </c:forEach>
                </select>
            </div>
        </c:if>

        <c:choose>
        <c:when test="${empty projeto.id}">
        <div class="col-12">
            </c:when>
            <c:otherwise>
            <div class="col-8">
                </c:otherwise>
                </c:choose>
                <label class="form-label">Membros do Projeto</label>
                <select name="membrosIds" class="form-select" multiple size="5">
                    <c:forEach items="${pessoas}" var="p">
                        <option value="${p.id}"
                                <c:if test="${projeto.membros != null && projeto.membros.contains(p)}">selected</c:if>>
                                ${p.nome}
                        </option>
                    </c:forEach>
                </select>
                <div class="form-text">Segure Ctrl (Windows) ou Command (Mac) para selecionar vários.</div>
            </div>

            <div class="col-12">
                <label class="form-label">Descrição</label>
                <textarea name="descricao" rows="3" class="form-control">${projeto.descricao}</textarea>
            </div>

            <div class="col-12">
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-check-circle"></i> Salvar
                </button>
                <a href="${pageContext.request.contextPath}/" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left"></i> Voltar
                </a>
            </div>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>