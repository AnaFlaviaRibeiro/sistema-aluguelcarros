<script setup lang="ts">
import type { StatusPedido } from '../types/api'
import { usePedidosPage } from '../composables/useRentalsApp'

const {
  clientes,
  pedidos,
  clienteSelecionado,
  novoPedido,
  statusOptions,
  carregarPedidos,
  salvarPedido,
  onStatusChange,
} = usePedidosPage()

function pillClass(status: StatusPedido): string {
  const map: Record<StatusPedido, string> = {
    PENDENTE: 'pill-warn',
    EM_ANALISE: 'pill-info',
    APROVADO: 'pill-ok',
    REPROVADO: 'pill-bad',
    CANCELADO: 'pill-muted',
  }
  return map[status] ?? 'pill-muted'
}
</script>

<template>
  <div class="page">
    <header class="page-head">
      <h1>Pedidos de aluguel</h1>
      <p class="page-desc">
        Escolha um cliente para ver os pedidos, atualize status e registre novas
        solicitações com dados do veículo.
      </p>
    </header>

    <section class="panel">
      <h2 class="panel-title">Pedidos por cliente</h2>
      <div class="toolbar">
        <label class="field field-inline">
          <span>Cliente</span>
          <select
            v-model.number="clienteSelecionado"
            class="select"
            @change="carregarPedidos"
          >
            <option :value="null">Selecione…</option>
            <option v-for="c in clientes" :key="c.id" :value="c.id">
              {{ c.id }} — {{ c.nome }}
            </option>
          </select>
        </label>
        <button type="button" class="btn btn-secondary" @click="carregarPedidos">
          Atualizar lista
        </button>
      </div>

      <div v-if="pedidos.length" class="table-wrap">
        <table class="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Data</th>
              <th>Status</th>
              <th>Valor / mês</th>
              <th>Prazo</th>
              <th>Veículo</th>
              <th>Alterar</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="p in pedidos" :key="p.id">
              <td class="mono">{{ p.id }}</td>
              <td>{{ p.dataPedido }}</td>
              <td>
                <span class="pill" :class="pillClass(p.status)">{{ p.status }}</span>
              </td>
              <td class="mono">{{ p.valorMensal }}</td>
              <td>{{ p.prazoMeses }} meses</td>
              <td>{{ p.placaAutomovel }} — {{ p.modeloAutomovel }}</td>
              <td>
                <select
                  class="select select-compact"
                  :value="p.status"
                  @change="onStatusChange(p.id, $event)"
                >
                  <option v-for="s in statusOptions" :key="s" :value="s">
                    {{ s }}
                  </option>
                </select>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <p v-else-if="clienteSelecionado != null" class="empty">
        Nenhum pedido para este cliente.
      </p>
      <p v-else class="empty">Selecione um cliente para carregar os pedidos.</p>
    </section>

    <section class="panel">
      <h2 class="panel-title">Novo pedido</h2>
      <form class="form-grid" @submit.prevent="salvarPedido">
        <label class="field">
          <span>Cliente</span>
          <select v-model="novoPedido.clienteId" class="select" required>
            <option value="" disabled>Selecione…</option>
            <option v-for="c in clientes" :key="c.id" :value="c.id">
              {{ c.nome }}
            </option>
          </select>
        </label>
        <label class="field">
          <span>Valor mensal</span>
          <input
            v-model.number="novoPedido.valorMensal"
            type="number"
            min="1"
            step="0.01"
            required
          />
        </label>
        <label class="field">
          <span>Prazo (meses)</span>
          <input v-model.number="novoPedido.prazoMeses" type="number" min="1" required />
        </label>
        <label class="field">
          <span>Matrícula</span>
          <input v-model="novoPedido.matricula" placeholder="Matrícula" required />
        </label>
        <label class="field">
          <span>Ano</span>
          <input
            v-model.number="novoPedido.ano"
            type="number"
            min="1900"
            :max="new Date().getFullYear() + 1"
            required
          />
        </label>
        <label class="field">
          <span>Marca</span>
          <input v-model="novoPedido.marca" placeholder="Marca" required />
        </label>
        <label class="field">
          <span>Modelo</span>
          <input v-model="novoPedido.modelo" placeholder="Modelo" required />
        </label>
        <label class="field">
          <span>Placa</span>
          <input v-model="novoPedido.placa" placeholder="ABC1D23" required />
        </label>
        <label class="field">
          <span>Tipo proprietário</span>
          <select v-model="novoPedido.proprietarioTipo" class="select" required>
            <option value="CLIENTE">CLIENTE</option>
            <option value="EMPRESA">EMPRESA</option>
            <option value="BANCO">BANCO</option>
          </select>
        </label>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary">Criar pedido</button>
        </div>
      </form>
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
  margin: 0 0 1rem;
  font-size: 1.05rem;
  font-weight: 600;
  color: var(--text);
}

.toolbar {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  align-items: flex-end;
  margin-bottom: 1rem;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}

.field-inline {
  min-width: min(100%, 280px);
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
}

.field input:focus {
  outline: none;
  border-color: var(--accent);
  box-shadow: 0 0 0 3px rgb(37 99 235 / 15%);
}

.select {
  padding: 0.55rem 0.65rem;
  border: 1px solid var(--border-input);
  border-radius: 8px;
  font: inherit;
  background: #fff;
  min-width: 0;
}

.select:focus {
  outline: none;
  border-color: var(--accent);
}

.select-compact {
  max-width: 160px;
  font-size: 0.82rem;
}

.btn {
  font: inherit;
  cursor: pointer;
  border-radius: 8px;
  font-weight: 600;
}

.btn-primary {
  padding: 0.6rem 1.15rem;
  border: none;
  background: linear-gradient(135deg, var(--accent) 0%, var(--accent-dark) 100%);
  color: #fff;
}

.btn-primary:hover {
  filter: brightness(1.05);
}

.btn-secondary {
  padding: 0.55rem 1rem;
  background: var(--surface-muted);
  border: 1px solid var(--border);
  color: var(--text);
}

.btn-secondary:hover {
  border-color: var(--accent);
  color: var(--accent);
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 1rem;
  align-items: end;
}

.form-actions {
  grid-column: 1 / -1;
  padding-top: 0.25rem;
}

.table-wrap {
  overflow-x: auto;
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

.mono {
  font-variant-numeric: tabular-nums;
  color: var(--text-muted);
}

.pill {
  display: inline-block;
  padding: 0.2rem 0.55rem;
  border-radius: 999px;
  font-size: 0.72rem;
  font-weight: 600;
  letter-spacing: 0.02em;
}

.pill-warn {
  background: #fffbeb;
  color: #b45309;
}

.pill-info {
  background: #eff6ff;
  color: #1d4ed8;
}

.pill-ok {
  background: #ecfdf5;
  color: #047857;
}

.pill-bad {
  background: #fef2f2;
  color: #b91c1c;
}

.pill-muted {
  background: #f1f5f9;
  color: #475569;
}

.empty {
  margin: 0.5rem 0 0;
  color: var(--text-muted);
  font-size: 0.92rem;
}
</style>
