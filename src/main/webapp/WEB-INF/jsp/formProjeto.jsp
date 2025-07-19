<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title><c:out value="${projeto.id == null ? 'Novo' : 'Editar'}"/> Projeto - Portfólio</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description"
        content="<c:out value="${projeto.id == null ? 'Criar novo' : 'Editar'}"/> projeto no sistema de gestão de portfólio">

  <!-- Bootstrap 5.3 CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <!-- Bootstrap Icons -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

  <style>
      :root {
          --primary: #2563eb;
          --primary-dark: #1d4ed8;
          --primary-light: #dbeafe;
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
          --gradient: linear-gradient(135deg, var(--light) 0%, #e2e8f0 100%);
      }

      body {
          background: var(--gradient);
          font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', system-ui, sans-serif;
          min-height: 100vh;
      }

      /* Navbar */
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

      /* Form Container */
      .form-container {
          background: white;
          border: 1px solid var(--border);
          border-radius: 16px;
          box-shadow: var(--shadow-lg);
          overflow: hidden;
      }

      .form-header {
          background: linear-gradient(135deg, var(--primary), var(--info));
          color: white;
          padding: 2rem;
          margin: -1px -1px 0 -1px;
      }

      .form-header h1 {
          font-size: 1.75rem;
          font-weight: 700;
          margin-bottom: 0.5rem;
      }

      .form-header p {
          opacity: 0.9;
          margin-bottom: 0;
      }

      .form-body {
          padding: 2rem;
      }

      /* Form Sections */
      .form-section {
          background: #f8fafc;
          border: 1px solid var(--border);
          border-radius: 12px;
          padding: 1.5rem;
          margin-bottom: 1.5rem;
      }

      .form-section h3 {
          font-size: 1.125rem;
          font-weight: 600;
          color: var(--dark);
          margin-bottom: 1rem;
          display: flex;
          align-items: center;
          gap: 0.5rem;
      }

      .form-section h3 i {
          color: var(--primary);
      }

      /* Form Controls */
      .form-label {
          font-weight: 600;
          color: var(--dark);
          margin-bottom: 0.5rem;
          display: flex;
          align-items: center;
          gap: 0.25rem;
      }

      .form-label .required {
          color: var(--danger);
      }

      .form-control, .form-select {
          border: 2px solid var(--border);
          border-radius: 8px;
          padding: 0.75rem 1rem;
          font-size: 0.95rem;
          transition: all 0.2s ease;
          background-color: white;
      }

      .form-control:focus, .form-select:focus {
          border-color: var(--primary);
          box-shadow: 0 0 0 0.2rem rgba(37, 99, 235, 0.15);
          background-color: white;
      }

      .form-control:invalid {
          border-color: var(--danger);
      }

      .form-control:valid {
          border-color: var(--success);
      }

      /* Multi-select */
      .members-select {
          min-height: 150px !important;
          padding: 0.5rem;
      }

      .members-select option {
          padding: 0.5rem;
          border-radius: 4px;
          margin: 2px 0;
      }

      .members-select option:checked {
          background: var(--primary) !important;
          color: white;
      }

      /* Buttons */
      .btn {
          border-radius: 8px;
          font-weight: 500;
          padding: 0.75rem 1.5rem;
          transition: all 0.2s ease;
      }

      .btn-primary {
          background: var(--primary);
          border-color: var(--primary);
      }

      .btn-primary:hover {
          background: var(--primary-dark);
          border-color: var(--primary-dark);
          transform: translateY(-1px);
      }

      .btn-outline-secondary {
          border-color: var(--border);
          color: var(--secondary);
      }

      .btn-outline-secondary:hover {
          background: var(--secondary);
          border-color: var(--secondary);
          transform: translateY(-1px);
      }

      /* Alerts */
      .alert {
          border: none;
          border-radius: 12px;
          font-weight: 500;
          margin-bottom: 1.5rem;
      }

      .alert-danger {
          background-color: #fef2f2;
          color: #dc2626;
          border-left: 4px solid var(--danger);
      }

      .alert-info {
          background-color: #eff6ff;
          color: #1e40af;
          border-left: 4px solid var(--info);
      }

      /* Loading States */
      .btn-loading {
          position: relative;
          color: transparent !important;
      }

      .btn-loading:after {
          content: '';
          position: absolute;
          width: 16px;
          height: 16px;
          top: 50%;
          left: 50%;
          margin-left: -8px;
          margin-top: -8px;
          border-radius: 50%;
          border: 2px solid transparent;
          border-top-color: currentColor;
          animation: spin 1s linear infinite;
      }

      @keyframes spin {
          0% {
              transform: rotate(0deg);
          }
          100% {
              transform: rotate(360deg);
          }
      }

      /* Tooltips */
      .form-tooltip {
          position: relative;
          cursor: help;
      }

      .form-tooltip .tooltip-text {
          visibility: hidden;
          width: 200px;
          background-color: var(--dark);
          color: white;
          text-align: center;
          border-radius: 6px;
          padding: 5px 10px;
          position: absolute;
          z-index: 1;
          top: -35px;
          left: 50%;
          margin-left: -100px;
          font-size: 0.8rem;
          opacity: 0;
          transition: opacity 0.3s;
      }

      .form-tooltip:hover .tooltip-text {
          visibility: visible;
          opacity: 1;
      }

      /* Mobile Responsive */
      @media (max-width: 768px) {
          .form-body {
              padding: 1.5rem;
          }

          .form-header {
              padding: 1.5rem;
          }

          .form-section {
              padding: 1rem;
          }
      }

      /* Character Counter */
      .char-counter {
          font-size: 0.8rem;
          color: var(--secondary);
          text-align: right;
          margin-top: 0.25rem;
      }

      .char-counter.warning {
          color: var(--warning);
      }

      .char-counter.danger {
          color: var(--danger);
      }

      /* Date inputs enhancement */
      input[type="date"] {
          position: relative;
      }

      input[type="date"]::-webkit-calendar-picker-indicator {
          position: absolute;
          right: 1rem;
          color: var(--primary);
          font-size: 1.1rem;
      }
  </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg sticky-top" role="navigation" aria-label="Navegação principal">
  <div class="container-fluid">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/" aria-label="Portfólio - Voltar à página inicial">
      <i class="bi bi-kanban-fill me-2" aria-hidden="true"></i>
      Portfólio
    </a>
  </div>
</nav>

<!-- Main Content -->
<main class="container py-4" role="main">

  <!-- Form Container -->
  <div class="form-container">
    <!-- Header -->
    <div class="form-header">
      <h1>
        <i class="bi bi-${projeto.id == null ? 'plus-circle' : 'pencil-square'} me-2" aria-hidden="true"></i>
        <c:out value="${projeto.id == null ? 'Novo Projeto' : 'Editar Projeto'}"/>
      </h1>
      <p>
        <c:choose>
          <c:when test="${projeto.id == null}">
            Preencha as informações abaixo para criar um novo projeto em seu portfólio.
          </c:when>
          <c:otherwise>
            Atualize as informações do projeto "${projeto.nome}".
          </c:otherwise>
        </c:choose>
      </p>
    </div>

    <!-- Form Body -->
    <div class="form-body">
      <!-- Alerts -->
      <div id="alerts-container" role="alert" aria-live="polite">
        <c:if test="${not empty erro}">
          <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle me-2" aria-hidden="true"></i>
            <strong>Erro!</strong> ${erro}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Fechar alerta"></button>
          </div>
        </c:if>
      </div>

      <!-- Form -->
      <form method="post"
            action="${pageContext.request.contextPath}/projetos${not empty projeto.id ? '/' : ''}${not empty projeto.id ? projeto.id : ''}"
            id="projectForm"
            novalidate
            role="form"
            aria-label="Formulário de <c:out value="${projeto.id == null ? 'criação' : 'edição'}"/> de projeto">

        <c:if test="${not empty projeto.id}">
          <input type="hidden" name="_method" value="PUT"/>
          <input type="hidden" name="id" value="${projeto.id}"/>
        </c:if>

        <!-- Informações Básicas -->
        <div class="form-section">
          <h3>
            <i class="bi bi-info-circle" aria-hidden="true"></i>
            Informações Básicas
          </h3>

          <div class="row g-3">
            <div class="col-md-8">
              <label for="nome" class="form-label">
                Nome do Projeto
                <span class="required" aria-label="obrigatório">*</span>
              </label>
              <input type="text"
                     id="nome"
                     name="nome"
                     value="${projeto.nome}"
                     class="form-control"
                     required
                     maxlength="200"
                     aria-describedby="nomeHelp nomeCounter"
                     oninput="updateCharCounter(this, 'nomeCounter', 200)"/>
              <div id="nomeHelp" class="form-text">Digite um nome descritivo para o projeto.</div>
              <div id="nomeCounter" class="char-counter">0/200</div>
            </div>

            <div class="col-md-4">
              <label for="orcamento" class="form-label">
                Orçamento
                <span class="required" aria-label="obrigatório">*</span>
              </label>
              <div class="input-group">
                <span class="input-group-text">R$</span>
                <input type="number"
                       id="orcamento"
                       name="orcamento"
                       value="${projeto.orcamento}"
                       class="form-control"
                       step="0.01"
                       min="0"
                       required
                       aria-describedby="orcamentoHelp"
                       oninput="formatCurrency(this)"/>
              </div>
              <div id="orcamentoHelp" class="form-text">Valor previsto para o projeto.</div>
            </div>
          </div>

          <div class="row g-3 mt-1">
            <div class="col-md-4">
              <label for="dataInicio" class="form-label">
                Data de Início
                <span class="required" aria-label="obrigatório">*</span>
              </label>
              <input type="date"
                     id="dataInicio"
                     name="dataInicio"
                     value="${projeto.dataInicio}"
                     class="form-control"
                     required
                     aria-describedby="dataInicioHelp"
                     onchange="validateDates()"/>
              <div id="dataInicioHelp" class="form-text">Quando o projeto deve começar.</div>
            </div>

            <div class="col-md-4">
              <label for="dataPrevisaoFim" class="form-label">
                Previsão de Término
                <span class="required" aria-label="obrigatório">*</span>
              </label>
              <input type="date"
                     id="dataPrevisaoFim"
                     name="dataPrevisaoFim"
                     value="${projeto.dataPrevisaoFim}"
                     class="form-control"
                     required
                     aria-describedby="dataFimHelp"
                     onchange="validateDates()"/>
              <div id="dataFimHelp" class="form-text">Data prevista para conclusão.</div>
            </div>

            <div class="col-md-4">
              <label for="risco" class="form-label">
                Nível de Risco
                <span class="form-tooltip">
                                    <i class="bi bi-question-circle text-muted" aria-hidden="true"></i>
                                    <span class="tooltip-text">Avalie o risco geral do projeto considerando complexidade, prazo e recursos.</span>
                                </span>
              </label>
              <select id="risco" name="risco" class="form-select" aria-describedby="riscoHelp">
                <option value="">Selecione o risco</option>
                <c:forEach items="${riscos}" var="r">
                  <option value="${r}" ${r == projeto.risco ? 'selected' : ''}>${r}</option>
                </c:forEach>
              </select>
              <div id="riscoHelp" class="form-text">Classifique o nível de risco do projeto.</div>
            </div>
          </div>
        </div>

        <!-- Gestão -->
        <div class="form-section">
          <h3>
            <i class="bi bi-people" aria-hidden="true"></i>
            Equipe e Gestão
          </h3>

          <div class="row g-3">
            <div class="col-md-6">
              <label for="gerenteId" class="form-label">
                Gerente do Projeto
                <span class="required" aria-label="obrigatório">*</span>
              </label>
              <select id="gerenteId" name="gerenteId" class="form-select" required aria-describedby="gerenteHelp">
                <option value="">Selecione o gerente</option>
                <c:forEach items="${gerentes}" var="g">
                  <option value="${g.id}" ${g.id == projeto.gerente.id ? 'selected' : ''}>${g.nome}</option>
                </c:forEach>
              </select>
              <div id="gerenteHelp" class="form-text">Responsável pela gestão do projeto.</div>
            </div>

            <!-- Status (apenas em edição) -->
            <c:if test="${not empty projeto.id}">
              <div class="col-md-6">
                <label for="status" class="form-label">
                  Status do Projeto
                  <span class="form-tooltip">
                                        <i class="bi bi-question-circle text-muted" aria-hidden="true"></i>
                                        <span class="tooltip-text">O status atual do projeto no ciclo de vida.</span>
                                    </span>
                </label>
                <select id="status" name="status" class="form-select" aria-describedby="statusHelp">
                  <c:forEach items="${statusList}" var="s">
                    <option value="${s}" ${s == projeto.status ? 'selected' : ''}>${s}</option>
                  </c:forEach>
                </select>
                <div id="statusHelp" class="form-text">Atualize conforme o progresso do projeto.</div>
              </div>
            </c:if>
          </div>

          <div class="row g-3 mt-1">
            <div class="col-12">
              <label for="membrosIds" class="form-label">
                Membros da Equipe
                <span class="form-tooltip">
                                    <i class="bi bi-question-circle text-muted" aria-hidden="true"></i>
                                    <span
                                      class="tooltip-text">Selecione todos os membros que participarão do projeto.</span>
                                </span>
              </label>
              <select id="membrosIds"
                      name="membrosIds"
                      class="form-select members-select"
                      multiple
                      aria-describedby="membrosHelp">
                <c:forEach items="${pessoas}" var="p">
                  <option value="${p.id}"
                          <c:if test="${projeto.membros != null && projeto.membros.contains(p)}">selected</c:if>>
                      ${p.nome}
                  </option>
                </c:forEach>
              </select>
              <div id="membrosHelp" class="form-text">
                <i class="bi bi-info-circle me-1" aria-hidden="true"></i>
                Segure <kbd>Ctrl</kbd> (Windows) ou <kbd>⌘</kbd> (Mac) para selecionar múltiplos membros.
                <span id="selectedCount" class="ms-2 text-primary fw-semibold"></span>
              </div>
            </div>
          </div>
        </div>

        <!-- Descrição -->
        <div class="form-section">
          <h3>
            <i class="bi bi-file-text" aria-hidden="true"></i>
            Detalhes do Projeto
          </h3>

          <div class="row g-3">
            <div class="col-12">
              <label for="descricao" class="form-label">Descrição</label>
              <textarea id="descricao"
                        name="descricao"
                        rows="4"
                        class="form-control"
                        maxlength="5000"
                        aria-describedby="descricaoHelp descricaoCounter"
                        oninput="updateCharCounter(this, 'descricaoCounter', 5000)"
                        placeholder="Descreva os objetivos, escopo e principais entregas do projeto...">${projeto.descricao}</textarea>
              <div id="descricaoHelp" class="form-text">Forneça uma descrição detalhada do projeto (opcional).</div>
              <div id="descricaoCounter" class="char-counter">0/5000</div>
            </div>
          </div>
        </div>

        <!-- Actions -->
        <div class="d-flex gap-3 justify-content-end flex-wrap">
          <a href="${pageContext.request.contextPath}/"
             class="btn btn-outline-secondary"
             role="button"
             aria-label="Cancelar e voltar à lista de projetos">
            <i class="bi bi-arrow-left me-1" aria-hidden="true"></i>
            Cancelar
          </a>

          <button type="submit"
                  id="submitBtn"
                  class="btn btn-primary"
                  aria-label="Salvar projeto">
            <i class="bi bi-check-circle me-1" aria-hidden="true"></i>
            <c:out value="${projeto.id == null ? 'Criar Projeto' : 'Salvar Alterações'}"/>
          </button>
        </div>
      </form>
    </div>
  </div>
</main>

<!-- Keyboard Shortcuts Modal -->
<div class="modal fade" id="shortcutsModal" tabindex="-1" aria-labelledby="shortcutsModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="shortcutsModalLabel">
          <i class="bi bi-keyboard me-2" aria-hidden="true"></i>
          Atalhos de Teclado
        </h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fechar"></button>
      </div>
      <div class="modal-body">
        <div class="row g-3">
          <div class="col-6">
            <kbd>Ctrl + S</kbd>
          </div>
          <div class="col-6">Salvar formulário</div>

          <div class="col-6">
            <kbd>Esc</kbd>
          </div>
          <div class="col-6">Cancelar/Voltar</div>

          <div class="col-6">
            <kbd>Alt + N</kbd>
          </div>
          <div class="col-6">Focar no campo Nome</div>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // Initialize page
    document.addEventListener('DOMContentLoaded', function () {
        setupFormValidation();
        setupCharCounters();
        initializeFormValues();
    });

    function setupFormValidation() {
        const form = document.getElementById('projectForm');

        form.addEventListener('submit', function (e) {
            e.preventDefault();

            if (validateForm()) {
                showLoadingState();
                form.submit();
            }
        });

        // Real-time validation
        const inputs = form.querySelectorAll('input[required], select[required]');
        inputs.forEach(input => {
            input.addEventListener('blur', validateField);
            input.addEventListener('input', clearFieldError);
        });
    }

    function validateForm() {
        let isValid = true;
        const form = document.getElementById('projectForm');

        // Clear previous errors
        form.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
        form.querySelectorAll('.invalid-feedback').forEach(el => el.remove());

        // Validate required fields
        const requiredFields = form.querySelectorAll('input[required], select[required]');
        requiredFields.forEach(field => {
            if (!field.value.trim()) {
                showFieldError(field, 'Este campo é obrigatório.');
                isValid = false;
            }
        });

        // Validate dates
        const dataInicio = document.getElementById('dataInicio');
        const dataPrevisaoFim = document.getElementById('dataPrevisaoFim');

        if (dataInicio.value && dataPrevisaoFim.value) {
            if (new Date(dataInicio.value) >= new Date(dataPrevisaoFim.value)) {
                showFieldError(dataPrevisaoFim, 'A data de término deve ser posterior à data de início.');
                isValid = false;
            }
        }

        // Validate budget
        const orcamento = document.getElementById('orcamento');
        if (orcamento.value && parseFloat(orcamento.value) <= 0) {
            showFieldError(orcamento, 'O orçamento deve ser maior que zero.');
            isValid = false;
        }

        return isValid;
    }

    function validateField(e) {
        const field = e.target;
        clearFieldError(field);

        if (field.hasAttribute('required') && !field.value.trim()) {
            showFieldError(field, 'Este campo é obrigatório.');
            return false;
        }

        return true;
    }

    function showFieldError(field, message) {
        field.classList.add('is-invalid');

        const feedback = document.createElement('div');
        feedback.className = 'invalid-feedback';
        feedback.textContent = message;

        field.parentNode.appendChild(feedback);
    }

    function clearFieldError(field) {
        if (typeof field === 'object' && field.target) {
            field = field.target;
        }

        field.classList.remove('is-invalid');
        const feedback = field.parentNode.querySelector('.invalid-feedback');
        if (feedback) {
            feedback.remove();
        }
    }

    function validateDates() {
        const dataInicio = document.getElementById('dataInicio');
        const dataPrevisaoFim = document.getElementById('dataPrevisaoFim');

        if (dataInicio.value && dataPrevisaoFim.value) {
            const inicio = new Date(dataInicio.value);
            const fim = new Date(dataPrevisaoFim.value);

            if (inicio >= fim) {
                showFieldError(dataPrevisaoFim, 'A data de término deve ser posterior à data de início.');
            } else {
                clearFieldError(dataPrevisaoFim);
            }
        }
    }

    function setupCharCounters() {
        const fields = [
            {input: 'nome', counter: 'nomeCounter', max: 200},
            {input: 'descricao', counter: 'descricaoCounter', max: 5000}
        ];

        fields.forEach(field => {
            const input = document.getElementById(field.input);
            if (input) {
                updateCharCounter(input, field.counter, field.max);
            }
        });
    }

    function updateCharCounter(input, counterId, maxLength) {
        const counter = document.getElementById(counterId);
        if (!counter) return;

        const length = input.value.length;
        counter.textContent = `${length}/${maxLength}`;

        // Update counter color based on usage
        counter.classList.remove('warning', 'danger');
        if (length > maxLength * 0.8) {
            counter.classList.add('warning');
        }
        if (length > maxLength * 0.95) {
            counter.classList.remove('warning');
            counter.classList.add('danger');
        }
    }

    function formatCurrency(input) {
        // Basic currency formatting (you can enhance this)
        const value = parseFloat(input.value);
        if (!isNaN(value) && value >= 0) {
            // Format to 2 decimal places
            input.value = value.toFixed(2);
        }
    }


    function showLoadingState() {
        const submitBtn = document.getElementById('submitBtn');
        submitBtn.disabled = true;
        submitBtn.classList.add('btn-loading');

        // Re-enable after 10 seconds as fallback
        setTimeout(() => {
            submitBtn.disabled = false;
            submitBtn.classList.remove('btn-loading');
        }, 10000);
    }

    function initializeFormValues() {
        // Initialize character counters with existing values
        const nome = document.getElementById('nome');
        const descricao = document.getElementById('descricao');

        if (nome && nome.value) {
            updateCharCounter(nome, 'nomeCounter', 200);
        }

        if (descricao && descricao.value) {
            updateCharCounter(descricao, 'descricaoCounter', 5000);
        }
    }
</script>

</body>
</html>