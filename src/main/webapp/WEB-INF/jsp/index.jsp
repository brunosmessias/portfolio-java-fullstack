<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Portfólio de Projetos</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap 5.3 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Ícones -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        :root {
            --bs-primary: #0d6efd;
            --bs-primary-rgb: 13, 110, 253;
        }

        body {
            background-color: #f8f9fa;
        }

        .navbar-brand {
            font-weight: 600;
        }

        .btn-primary {
            background-color: var(--bs-primary);
            border-color: var(--bs-primary);
        }

        .table-hover tbody tr:hover {
            background-color: rgba(var(--bs-primary-rgb), .075);
        }
    </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
    <div class="container-fluid">
        <a class="navbar-brand text-primary" href="#">
            <i class="bi bi-kanban"></i> Portfólio
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menu">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="menu">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/projetos/novo">
                        <i class="bi bi-plus-circle"></i> Novo Projeto
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- Conteúdo -->
<div class="container py-4">
    <h2 class="mb-4 text-primary">Projetos</h2>

    <c:if test="${not empty mensagemSucesso}">
        <div class="alert alert-success" role="alert">
                ${mensagemSucesso}
        </div>
    </c:if>
    <c:if test="${not empty mensagemErro}">
        <div class="alert alert-danger" role="alert">
                ${mensagemErro}
        </div>
    </c:if>

    <!-- Tabela responsiva -->
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light">
            <tr>
                <th>Nome</th>
                <th>Status</th>
                <th>Orçamento</th>
                <th width="120"></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${projetos}" var="p">
                <tr>
                    <td>${p.nome}</td>
                    <td>
                        <span class="badge bg-primary">${p.status}</span>
                    </td>
                    <td>R$ ${p.orcamento}</td>

                    <td>
                        <a class="btn btn-sm btn-outline-warning me-2"
                           href="${pageContext.request.contextPath}/projetos/${p.id}"
                           title="Editar">
                            <i class="bi bi-pencil"></i>
                        </a>

                        <form method="post"
                              action="${pageContext.request.contextPath}/projetos/${p.id}"
                              style="display:inline;">
                            <input type="hidden" name="_method" value="DELETE"/>
                            <button type="submit" class="btn btn-sm btn-danger"
                                    onclick="return confirm('Tem certeza que deseja excluir este projeto?');"
                                    <c:if test="${p.status == 'INICIADO' || p.status == 'EM_ANDAMENTO' || p.status == 'ENCERRADO'}">disabled</c:if>>
                                <i class="bi bi-trash"></i>
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>