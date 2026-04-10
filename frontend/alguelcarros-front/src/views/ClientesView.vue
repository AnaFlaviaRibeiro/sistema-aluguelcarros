<script setup lang="ts">
import { useClientesPage } from '../composables/useRentalsApp'

const {
  clientes,
  novoCliente,
  salvarCliente,
  removerCliente,
} = useClientesPage()
</script>

<template>
  <div class="page">
    <header class="page-head">
      <h1>Clientes</h1>
      <p class="page-desc">
        Cadastre pessoas físicas e mantenha a base alinhada com os pedidos de
        aluguel.
      </p>
    </header>

    <section class="panel">
      <h2 class="panel-title">Novo cliente</h2>
      <form class="form-grid" @submit.prevent="salvarCliente">
        <label class="field">
          <span>Nome</span>
          <input v-model="novoCliente.nome" placeholder="Nome completo" required />
        </label>
        <label class="field">
          <span>E-mail</span>
          <input
            v-model="novoCliente.email"
            type="email"
            placeholder="email@exemplo.com"
            required
          />
        </label>
        <label class="field">
          <span>Senha</span>
          <input
            v-model="novoCliente.senha"
            type="password"
            placeholder="••••••••"
            required
          />
        </label>
        <label class="field">
          <span>RG</span>
          <input v-model="novoCliente.rg" placeholder="RG" required />
        </label>
        <label class="field">
          <span>CPF</span>
          <input v-model="novoCliente.cpf" placeholder="CPF" required />
        </label>
        <label class="field">
          <span>Endereço</span>
          <input v-model="novoCliente.endereco" placeholder="Rua, número…" required />
        </label>
        <label class="field">
          <span>Profissão</span>
          <input v-model="novoCliente.profissao" placeholder="Profissão" required />
        </label>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary">Cadastrar cliente</button>
        </div>
      </form>
    </section>

    <section class="panel">
      <h2 class="panel-title">Lista de clientes</h2>
      <div v-if="clientes.length" class="table-wrap">
        <table class="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>E-mail</th>
              <th>CPF</th>
              <th class="th-actions" />
            </tr>
          </thead>
          <tbody>
            <tr v-for="c in clientes" :key="c.id">
              <td class="mono">{{ c.id }}</td>
              <td>{{ c.nome }}</td>
              <td>{{ c.email }}</td>
              <td>{{ c.cpf }}</td>
              <td class="td-actions">
                <button type="button" class="btn btn-danger-sm" @click="removerCliente(c.id)">
                  Excluir
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <p v-else class="empty">Nenhum cliente cadastrado ainda.</p>
    </section>
  </div>
</template>

<style scoped>
.page {
  max-width: 960px;
  margin: 0 auto;
}

.page-head {
  margin-bottom: 1.75rem;
}

.page-head h1 {
  margin: 0 0 0.35rem;
  font-size: 1.65rem;
  font-weight: 700;
  color: var(--text);
  letter-spacing: -0.02em;
}

.page-desc {
  margin: 0;
  font-size: 0.95rem;
  color: var(--text-muted);
  line-height: 1.5;
  max-width: 42rem;
}

.panel {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 1.35rem 1.5rem 1.5rem;
  margin-bottom: 1.25rem;
  box-shadow: var(--shadow-card);
}

.panel-title {
  margin: 0 0 1.1rem;
  font-size: 1.05rem;
  font-weight: 600;
  color: var(--text);
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 1rem 1rem;
  align-items: end;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}

.field span {
  font-size: 0.78rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--text-soft);
}

.field input {
  padding: 0.55rem 0.65rem;
  border: 1px solid var(--border-input);
  border-radius: 8px;
  font: inherit;
  background: #fff;
  transition: border-color 0.15s ease, box-shadow 0.15s ease;
}

.field input:focus {
  outline: none;
  border-color: var(--accent);
  box-shadow: 0 0 0 3px rgb(37 99 235 / 15%);
}

.form-actions {
  grid-column: 1 / -1;
  padding-top: 0.25rem;
}

.btn {
  font: inherit;
  cursor: pointer;
  border-radius: 8px;
  border: 1px solid transparent;
  font-weight: 600;
}

.btn-primary {
  padding: 0.6rem 1.15rem;
  background: linear-gradient(135deg, var(--accent) 0%, var(--accent-dark) 100%);
  color: #fff;
}

.btn-primary:hover {
  filter: brightness(1.05);
}

.btn-danger-sm {
  padding: 0.35rem 0.65rem;
  font-size: 0.82rem;
  font-weight: 600;
  background: #fff;
  border-color: #fecaca;
  color: #b91c1c;
}

.btn-danger-sm:hover {
  background: #fef2f2;
}

.table-wrap {
  overflow-x: auto;
  margin-top: 0.25rem;
  border-radius: 10px;
  border: 1px solid var(--border);
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.875rem;
}

.data-table th,
.data-table td {
  text-align: left;
  padding: 0.65rem 0.85rem;
  border-bottom: 1px solid var(--border);
}

.data-table tbody tr:last-child td {
  border-bottom: none;
}

.data-table tbody tr:hover {
  background: var(--surface-muted);
}

.data-table th {
  font-size: 0.72rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: var(--text-soft);
  font-weight: 600;
  background: var(--surface-muted);
}

.th-actions,
.td-actions {
  width: 1%;
  white-space: nowrap;
  text-align: right;
}

.mono {
  font-variant-numeric: tabular-nums;
  color: var(--text-muted);
}

.empty {
  margin: 0.75rem 0 0;
  color: var(--text-muted);
  font-size: 0.92rem;
}
</style>
