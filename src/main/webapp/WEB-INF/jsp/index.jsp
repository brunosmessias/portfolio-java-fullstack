<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Portfólio de Projetos - Gestão Inteligente</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="Sistema de gestão de portfólio de projetos">

  <!-- Bootstrap 5.3 CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <!-- Bootstrap Icons -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

  <style>
      :root {
          --primary: #2563eb;
          --primary-dark: #1d4ed8;
          --secondary: #64748b;
          --success: #059669;
          --warning: #d97706;
          --danger: #dc2626;
          --info: #0891b2;
          --light: #f8fafc;
          --dark: #0f172a;
          --border: #e2e8f0;
          --shadow: 0 1px 3px 0 rgb(0 0 0 / 0.1), 0 1px 2px -1px rgb(0 0 0 / 0.1);
          --shadow-lg: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1);
      }

      body {
          background: linear-gradient(135deg, var(--light) 0%, #e2e8f0 100%);
          font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', system-ui, sans-serif;
          min-height: 100vh;
      }

      /* Header */
      .navbar {
          backdrop-filter: blur(20px);
          background: rgba(255, 255, 255, 0.95) !important;
          border-bottom: 1px solid var(--border);
      }

      .navbar-brand {
          font-weight: 700;
          font-size: 1.5rem;
          color: var(--primary) !important;
      }

      /* Cards */
      .project-card {
          background: white;
          border: 1px solid var(--border);
          border-radius: 12px;
          box-shadow: var(--shadow);
          transition: all 0.3s ease;
          overflow: hidden;
      }

      .project-card:hover {
          transform: translateY(-2px);
          box-shadow: var(--shadow-lg);
      }

      /* Status badges */
      .status-badge {
          padding: 0.375rem 0.75rem;
          border-radius: 6px;
          font-size: 0.75rem;
          font-weight: 600;
          text-transform: uppercase;
          letter-spacing: 0.025em;
      }

      /* Risk badges */
      .risk-badge {
          padding: 0.25rem 0.5rem;
          border-radius: 4px;
          font-size: 0.7rem;
          font-weight: 500;
      }

      .risk-baixo {
          background-color: #dcfce7;
          color: #059669;
      }

      .risk-medio {
          background-color: #fef3c7;
          color: #d97706;
      }

      .risk-alto {
          background-color: #fee2e2;
          color: #dc2626;
      }

      /* Buttons */
      .btn-primary {
          background: var(--primary);
          border-color: var(--primary);
          border-radius: 8px;
          font-weight: 500;
          padding: 0.5rem 1rem;
      }

      .btn-primary:hover {
          background: var(--primary-dark);
          border-color: var(--primary-dark);
          transform: translateY(-1px);
      }

      .action-btn {
          width: 36px;
          height: 36px;
          border-radius: 8px;
          display: inline-flex;
          align-items: center;
          justify-content: center;
          font-size: 0.875rem;
          transition: all 0.2s ease;
      }

      .action-btn:hover {
          transform: translateY(-1px);
      }

      .card {
          background: white;
          border: 1px solid var(--border);
          border-radius: 12px;
          box-shadow: var(--shadow);
          padding: 1.5rem;
          margin-bottom: 2rem;
      }

      /* Mobile responsive */
      @media (max-width: 768px) {
          .navbar-brand {
              font-size: 1.25rem;
          }

          .project-card {
              margin-bottom: 1rem;
          }

          .table-responsive {
              display: none;
          }

          .mobile-cards {
              display: block !important;
          }
      }

      @media (min-width: 769px) {
          .mobile-cards {
              display: none !important;
          }
      }


      /* Alerts */
      .alert {
          border: none;
          border-radius: 8px;
          font-weight: 500;
      }

      .alert-success {
          background-color: #f0fdf4;
          color: #15803d;
          border-left: 4px solid var(--success);
      }

      .alert-danger {
          background-color: #fef2f2;
          color: #dc2626;
          border-left: 4px solid var(--danger);
      }
  </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg sticky-top" role="navigation" aria-label="Navegação principal">
  <div class="container-fluid">
    <a class="navbar-brand" href="#" aria-label="Portfólio - Página inicial">
      <i class="bi bi-kanban-fill me-2" aria-hidden="true"></i>
      Portfólio
    </a>

    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
            aria-controls="navbarNav" aria-expanded="false" aria-label="Alternar navegação">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav ms-auto">
        <li class="nav-item">
          <a class="btn btn-primary" href="${pageContext.request.contextPath}/projetos/novo"
             role="button" aria-label="Criar novo projeto">
            <i class="bi bi-plus-lg me-1" aria-hidden="true"></i>
            Novo Projeto
          </a>
        </li>
      </ul>
    </div>
  </div>
</nav>

<!-- Main Content -->
<main class="container py-4" role="main">
  <!-- Header -->
  <div class="d-flex justify-content-between align-items-center mb-4">
    <div>
      <h1 class="h2 text-dark mb-1">Meus Projetos</h1>
      <p class="text-muted mb-0">Gerencie seu portfólio de projetos</p>
    </div>
  </div>

  <!-- Alerts -->
  <div id="alerts-container" role="alert" aria-live="polite">
    <c:if test="${not empty mensagemSucesso}">
      <div class="alert alert-success alert-dismissible fade show" role="alert">
        <i class="bi bi-check-circle me-2" aria-hidden="true"></i>
        <strong>Sucesso!</strong> ${mensagemSucesso}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Fechar alerta"></button>
      </div>
    </c:if>

    <c:if test="${not empty mensagemErro}">
      <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle me-2" aria-hidden="true"></i>
        <strong>Erro!</strong> ${mensagemErro}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Fechar alerta"></button>
      </div>
    </c:if>
  </div>

  <div class="card">
    <div class="row g-3">
      <div class="col-md-5">
        <label for="statusFilter" class="form-label visually-hidden">Filtrar por status</label>
        <select class="form-select" id="statusFilter" aria-label="Filtrar por status">
          <option value="">Todos os status</option>
          <option value="PLANEJADO">Planejado</option>
          <option value="INICIADO">Iniciado</option>
          <option value="EM_ANDAMENTO">Em Andamento</option>
          <option value="ENCERRADO">Encerrado</option>
          <option value="CANCELADO">Cancelado</option>
        </select>
      </div>
      <div class="col-md-5">
        <label for="riskFilter" class="form-label visually-hidden">Filtrar por risco</label>
        <select class="form-select" id="riskFilter" aria-label="Filtrar por nível de risco">
          <option value="">Todos os riscos</option>
          <option value="BAIXO">Baixo</option>
          <option value="MEDIO">Médio</option>
          <option value="ALTO">Alto</option>
        </select>
      </div>
      <div class="col-md-2">
        <button type="button" class="btn btn-outline-secondary w-100" id="clearFilters"
                aria-label="Limpar todos os filtros">
          <i class="bi bi-arrow-clockwise me-1" aria-hidden="true"></i>
          Limpar
        </button>
      </div>
    </div>
  </div>

  <!-- Desktop Table View -->
  <div class="table-responsive d-none d-md-block card">
    <table class="table table-hover align-middle overflow-hidden"
           role="table" aria-label="Lista de projetos">
      <thead class="table-light">
      <tr>
        <th scope="col">Projeto</th>
        <th scope="col">Status</th>
        <th scope="col">Risco</th>
        <th scope="col">Orçamento</th>
        <th scope="col">Gerente</th>
        <th scope="col">Prazo</th>
        <th scope="col" class="text-center">Ações</th>
      </tr>
      </thead>
      <tbody id="projectsTableBody">
      <c:forEach items="${projetos}" var="p" varStatus="status">
        <tr data-status="${p.status}" data-risk="${p.risco}" data-name="${p.nome}">
          <td>
            <div>
              <h6 class="mb-1 fw-semibold">${p.nome}</h6>
            </div>
          </td>
          <td>
            <span class="status-badge status-${fn:toLowerCase(p.status)}"
                  role="status" aria-label="Status: ${p.status}">
                ${p.status}
            </span>
          </td>
          <td>
            <c:if test="${not empty p.risco}">
              <span class="risk-badge risk-${fn:toLowerCase(p.risco)}"
                    role="status" aria-label="Risco: ${p.risco}">
                  ${p.risco}
              </span>
            </c:if>
          </td>
          <td>
            <strong class="text-success">
              <fmt:formatNumber value="${p.orcamento}" type="currency" currencySymbol="R$ "/>
            </strong>
          </td>
          <td>
            <c:if test="${not empty p.gerente}">
              <div class="d-flex align-items-center">
                <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-2"
                     style="width: 32px; height: 32px; font-size: 0.75rem;">
                    ${fn:substring(p.gerente.nome, 0, 2)}
                </div>
                <span class="fw-medium">${p.gerente.nome}</span>
              </div>
            </c:if>
          </td>
          <td>
            <c:if test="${not empty p.dataPrevisaoFim}">
              <small class="text-muted">
                <i class="bi bi-calendar-event me-1" aria-hidden="true"></i>
                  ${p.dataPrevisaoFim}
              </small>
              <c:if test="${p.dataPrevisaoFim lt now and p.status ne 'ENCERRADO'}">
                <br><span class="badge bg-danger">Atrasado</span>
              </c:if>
            </c:if>
          </td>
          <td class="text-center">
            <div class="btn-group" role="group" aria-label="Ações do projeto ${p.nome}">
              <a href="${pageContext.request.contextPath}/projetos/${p.id}"
                 class="action-btn btn btn-outline-primary btn-sm"
                 title="Visualizar projeto" aria-label="Visualizar projeto ${p.nome}">
                <i class="bi bi-eye" aria-hidden="true"></i>
              </a>

              <button type="button" class="action-btn btn btn-outline-danger btn-sm"
                      onclick="confirmarExclusao('${p.id}', '${p.nome}')"
                      title="Excluir projeto" aria-label="Excluir projeto ${p.nome}"
                <c:if test="${p.status == 'INICIADO' || p.status == 'EM_ANDAMENTO' || p.status == 'ENCERRADO'}">
                  disabled aria-disabled="true"
                </c:if>>
                <i class="bi bi-trash" aria-hidden="true"></i>
              </button>
            </div>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>

  <!-- Mobile Cards View -->
  <div class="mobile-cards d-md-none">
    <c:forEach items="${projetos}" var="p" varStatus="status">
      <div class="project-card mb-3" data-status="${p.status}" data-risk="${p.risco}" data-name="${p.nome}">
        <div class="card-body p-3">
          <div class="d-flex justify-content-between align-items-start mb-2">
            <h6 class="card-title mb-1 fw-semibold">${p.nome}</h6>
            <span class="status-badge status-${fn:toLowerCase(p.status)}">${p.status}</span>
          </div>

          <c:if test="${not empty p.descricao}">
            <p class="card-text text-muted small mb-2">${p.descricao}</p>
          </c:if>

          <div class="row g-2 mb-3">
            <div class="col-6">
              <small class="text-muted d-block">Orçamento</small>
              <strong class="text-success">
                <fmt:formatNumber value="${p.orcamento}" type="currency" currencySymbol="R$ "/>
              </strong>
            </div>
            <div class="col-6">
              <small class="text-muted d-block">Risco</small>
              <c:if test="${not empty p.risco}">
                <span class="risk-badge risk-${fn:toLowerCase(p.risco)}">${p.risco}</span>
              </c:if>
            </div>
          </div>

          <c:if test="${not empty p.gerente}">
            <div class="d-flex align-items-center mb-3">
              <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-2"
                   style="width: 24px; height: 24px; font-size: 0.7rem;">
                  ${fn:substring(p.gerente.nome, 0, 2)}
              </div>
              <small class="text-muted">Gerente: ${p.gerente.nome}</small>
            </div>
          </c:if>

          <div class="d-flex justify-content-between align-items-center">
            <c:if test="${not empty p.dataPrevisaoFim}">
              <small class="text-muted">
                <i class="bi bi-calendar-event me-1" aria-hidden="true"></i>
                  ${p.dataPrevisaoFim}
              </small>
            </c:if>

            <div class="btn-group btn-group-sm">
              <a href="${pageContext.request.contextPath}/projetos/${p.id}"
                 class="btn btn-outline-primary btn-sm" title="Visualizar">
                <i class="bi bi-eye" aria-hidden="true"></i>
              </a>
              <button type="button" class="btn btn-outline-danger btn-sm"
                      onclick="confirmarExclusao('${p.id}', '${p.nome}')" title="Excluir"
                <c:if test="${p.status == 'INICIADO' || p.status == 'EM_ANDAMENTO' || p.status == 'ENCERRADO'}">
                  disabled
                </c:if>>
                <i class="bi bi-trash" aria-hidden="true"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>

  <!-- Empty State -->
  <div id="emptyState" class="text-center py-5 d-none">
    <div class="mb-3">
      <i class="bi bi-folder-x display-1 text-muted" aria-hidden="true"></i>
    </div>
    <h5 class="text-muted">Nenhum projeto encontrado</h5>
    <p class="text-muted mb-4">Não há projetos que correspondam aos filtros selecionados.</p>
    <button type="button" class="btn btn-outline-primary" onclick="clearAllFilters()">
      <i class="bi bi-arrow-clockwise me-1" aria-hidden="true"></i>
      Limpar Filtros
    </button>
  </div>

  <!-- Pagination -->
  <nav aria-label="Navegação de páginas" class="mt-4">
    <ul class="pagination justify-content-center" id="pagination">
      <!-- Pagination will be populated by JavaScript -->
    </ul>
  </nav>
</main>

<!-- Delete Confirmation Modal -->
<div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header border-0">
        <h5 class="modal-title" id="deleteModalLabel">
          <i class="bi bi-exclamation-triangle text-warning me-2" aria-hidden="true"></i>
          Confirmar Exclusão
        </h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fechar"></button>
      </div>
      <div class="modal-body">
        <p>Tem certeza que deseja excluir o projeto <strong id="projectName"></strong>?</p>
        <div class="alert alert-warning">
          <i class="bi bi-info-circle me-2" aria-hidden="true"></i>
          <strong>Atenção:</strong> Esta ação não pode ser desfeita.
        </div>
      </div>
      <div class="modal-footer border-0">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
        <button type="button" class="btn btn-danger" id="confirmDelete">
          <i class="bi bi-trash me-1" aria-hidden="true"></i>
          Excluir Projeto
        </button>
      </div>
    </div>
  </div>
</div>

<!-- Hidden form for deletion -->
<form id="deleteForm" method="post" style="display: none;">
  <input type="hidden" name="_method" value="DELETE"/>
</form>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
    let filteredProjects = [];
    let allProjects = [];

    // Initialize page
    document.addEventListener('DOMContentLoaded', function () {
        initializeProjects();
        setupEventListeners();
    });

    function initializeProjects() {
        const rows = document.querySelectorAll('#projectsTableBody tr, .mobile-cards .project-card');
        allProjects = Array.from(rows).map(row => ({
            element: row,
            name: row.dataset.name?.toLowerCase() || '',
            status: row.dataset.status || '',
            risk: row.dataset.risk || ''
        }));

        filteredProjects = [...allProjects];
    }

    function setupEventListeners() {
        // Filter functionality
        document.getElementById('statusFilter').addEventListener('change', filterProjects);
        document.getElementById('riskFilter').addEventListener('change', filterProjects);

        // Clear filters
        document.getElementById('clearFilters').addEventListener('click', clearAllFilters);

        // Auto-dismiss alerts after 5 seconds
        setTimeout(() => {
            document.querySelectorAll('.alert-dismissible').forEach(alert => {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            });
        }, 5000);
    }

    function filterProjects() {
        const statusFilter = document.getElementById('statusFilter').value;
        const riskFilter = document.getElementById('riskFilter').value;

        filteredProjects = allProjects.filter(project => {
            const matchesStatus = !statusFilter || project.status === statusFilter;
            const matchesRisk = !riskFilter || project.risk === riskFilter;

            return matchesStatus && matchesRisk;
        });

        updateProjectDisplay();
    }

    function updateProjectDisplay() {
        // Hide all projects
        allProjects.forEach(project => {
            project.element.style.display = 'none';
        });

        filteredProjects.forEach(project => {
            project.element.style.display = '';
        });

        const emptyState = document.getElementById('emptyState');
        if (filteredProjects.length === 0) {
            emptyState.classList.remove('d-none');
        } else {
            emptyState.classList.add('d-none');
        }
    }

    function clearAllFilters() {
        document.getElementById('statusFilter').value = '';
        document.getElementById('riskFilter').value = '';

        filteredProjects = [...allProjects];
        updateProjectDisplay();
    }

    function confirmarExclusao(projectId, projectName) {
        document.getElementById('projectName').textContent = projectName;

        const confirmButton = document.getElementById('confirmDelete');
        confirmButton.onclick = function () {
            const form = document.getElementById('deleteForm');
            form.action = `${window.location.origin}${window.location.pathname}/projetos/${projectId}`;
            form.submit();
        };

        const deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
        deleteModal.show();
    }

    function debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }
</script>

</body>
</html>