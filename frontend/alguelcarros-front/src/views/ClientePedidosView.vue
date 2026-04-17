<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { api } from '../api'
import { useFeedback } from '../composables/useFeedback'
import type { PedidoAluguelResponse, StatusPedido, TipoProprietario } from '../types/api'
import { CATALOGO_VEICULOS, veiculoCatalogoPorPlaca } from '../data/catalogoVeiculos'
import type { VeiculoCatalogo } from '../data/catalogoVeiculos'

const { show } = useFeedback()
const catalogoIdSelecionado = ref<string | null>(null)

const pedidos = ref<PedidoAluguelResponse[]>([])
const carregando = ref(false)
const editandoId = ref<number | null>(null)

const novo = reactive({
  valorMensal: 1500,
  prazoMeses: 12,
  matricula: '',
  ano: new Date().getFullYear(),
  marca: '',
  modelo: '',
  placa: '',
  proprietarioTipo: 'CLIENTE' as TipoProprietario,
})

const edicao = reactive({ ...novo })

function aplicarDoCatalogo(v: VeiculoCatalogo) {
  catalogoIdSelecionado.value = v.id
  novo.matricula = v.matricula
  novo.ano = v.ano
  novo.marca = v.marca
  novo.modelo = v.modelo
  novo.placa = v.placa
  novo.valorMensal = v.valorMensalSugerido
}

function thumbPedido(p: PedidoAluguelResponse): string | undefined {
  return veiculoCatalogoPorPlaca(p.placaAutomovel)?.imagemUrl
}

function pillClass(s: StatusPedido) {
  const m: Record<StatusPedido, string> = {
    PENDENTE: 'pill-warn',
    EM_ANALISE: 'pill-info',
    APROVADO: 'pill-ok',
    REPROVADO: 'pill-bad',
    CANCELADO: 'pill-muted',
  }
  return m[s] ?? 'pill-muted'
}

async function carregar() {
  carregando.value = true
  try {
    pedidos.value = await api.clienteListarPedidos()
  } catch (e) {
    show('err', e instanceof Error ? e.message : 'Falha ao listar pedidos.')
  } finally {
    carregando.value = false
  }
}

async function criar() {
  try {
    await api.clienteCriarPedido({ ...novo })
    show('ok', 'Pedido criado.')
    catalogoIdSelecionado.value = null
    Object.assign(novo, {
      valorMensal: 1500,
      prazoMeses: 12,
      matricula: '',
      ano: new Date().getFullYear(),
      marca: '',
      modelo: '',
      placa: '',
      proprietarioTipo: 'CLIENTE' as TipoProprietario,
    })
    await carregar()
  } catch (e) {
    show('err', e instanceof Error ? e.message : 'Falha ao criar pedido.')
  }
}

function iniciarEdicao(p: PedidoAluguelResponse) {
  editandoId.value = p.id
  edicao.valorMensal = Number(p.valorMensal)
  edicao.prazoMeses = p.prazoMeses
  edicao.matricula = p.matricula
  edicao.ano = p.ano
  edicao.marca = p.marca
  edicao.modelo = p.modeloAutomovel
  edicao.placa = p.placaAutomovel
  edicao.proprietarioTipo = p.proprietarioTipo as TipoProprietario
}

async function salvarEdicao() {
  if (editandoId.value == null) return
  try {
    await api.clienteAtualizarPedido(editandoId.value, { ...edicao })
    show('ok', 'Pedido atualizado.')
    editandoId.value = null
    await carregar()
  } catch (e) {
    show('err', e instanceof Error ? e.message : 'Falha ao atualizar.')
  }
}

async function cancelarPedido(id: number) {
  if (!confirm('Cancelar este pedido?')) return
  try {
    await api.clienteCancelarPedido(id)
    show('ok', 'Pedido cancelado.')
    await carregar()
  } catch (e) {
    show('err', e instanceof Error ? e.message : 'Falha ao cancelar.')
  }
}

onMounted(() => {
  void carregar()
})
</script>

<template>
  <div class="page">
    <header class="page-head">
      <h1>Meus pedidos de aluguel</h1>
      <p class="page-desc">
        Inclua e altere pedidos enquanto estiverem pendentes; cancele enquanto pendente ou em análise.
      </p>
    </header>

    <section class="panel">
      <h2 class="panel-title">Novo pedido</h2>
      <p class="catalogo-lead">
        Escolha um carro da frota de demonstração para preencher marca, modelo, ano, matrícula e placa
        automaticamente (pode ajustar os valores antes de enviar).
      </p>
      <div class="catalogo-grid" role="list">
        <button
          v-for="v in CATALOGO_VEICULOS"
          :key="v.id"
          type="button"
          class="cat-card"
          :class="{ 'cat-card--on': catalogoIdSelecionado === v.id }"
          role="listitem"
          @click="aplicarDoCatalogo(v)"
        >
          <img :src="v.imagemUrl" :alt="`${v.marca} ${v.modelo}`" class="cat-img" loading="lazy" />
          <span class="cat-meta">
            <span class="cat-titulo">{{ v.marca }} {{ v.modelo }}</span>
            <span class="cat-sub">{{ v.ano }} · {{ v.placa }} · a partir de R$ {{ v.valorMensalSugerido }}/mês</span>
          </span>
        </button>
      </div>
      <form class="form-grid" @submit.prevent="criar">
        <label class="field">
          <span>Valor mensal</span>
          <input v-model.number="novo.valorMensal" type="number" min="1" step="0.01" required />
        </label>
        <label class="field">
          <span>Prazo (meses)</span>
          <input v-model.number="novo.prazoMeses" type="number" min="1" required />
        </label>
        <label class="field">
          <span>Matrícula</span>
          <input v-model="novo.matricula" required />
        </label>
        <label class="field">
          <span>Ano</span>
          <input v-model.number="novo.ano" type="number" min="1900" required />
        </label>
        <label class="field">
          <span>Marca</span>
          <input v-model="novo.marca" required />
        </label>
        <label class="field">
          <span>Modelo</span>
          <input v-model="novo.modelo" required />
        </label>
        <label class="field">
          <span>Placa</span>
          <input v-model="novo.placa" required />
        </label>
        <label class="field">
          <span>Proprietário do veículo</span>
          <select v-model="novo.proprietarioTipo" required>
            <option value="CLIENTE">Cliente</option>
            <option value="EMPRESA">Empresa</option>
            <option value="BANCO">Banco</option>
          </select>
        </label>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary">Enviar pedido</button>
        </div>
      </form>
    </section>

    <section class="panel">
      <h2 class="panel-title">Meus pedidos</h2>
      <p v-if="carregando" class="muted">Carregando…</p>
      <div v-else-if="pedidos.length" class="table-wrap">
        <table class="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Data</th>
              <th>Status</th>
              <th>Valor/mês</th>
              <th>Veículo</th>
              <th>Contrato / crédito</th>
              <th />
            </tr>
          </thead>
          <tbody>
            <tr v-for="p in pedidos" :key="p.id">
              <td>{{ p.id }}</td>
              <td>{{ p.dataPedido }}</td>
              <td>
                <span class="pill" :class="pillClass(p.status)">{{ p.status }}</span>
              </td>
              <td>{{ p.valorMensal }}</td>
              <td>
                <div class="veiculo-cell">
                  <img
                    v-if="thumbPedido(p)"
                    :src="thumbPedido(p)"
                    alt=""
                    class="thumb"
                    width="44"
                    height="32"
                  />
                  <span>{{ p.placaAutomovel }} — {{ p.modeloAutomovel }}</span>
                </div>
              </td>
              <td class="small">
                <template v-if="p.numeroContrato">
                  {{ p.numeroContrato }} ({{ p.tipoContrato }})
                  <span v-if="p.numeroCredito">
                    · Crédito {{ p.numeroCredito }}
                    <template v-if="p.nomeBancoConcedenteCredito">
                      — {{ p.nomeBancoConcedenteCredito }}
                    </template>
                  </span>
                </template>
                <span v-else class="muted">—</span>
              </td>
              <td class="actions">
                <button
                  v-if="p.status === 'PENDENTE'"
                  type="button"
                  class="btn-link"
                  @click="iniciarEdicao(p)"
                >
                  Editar
                </button>
                <button
                  v-if="p.status === 'PENDENTE' || p.status === 'EM_ANALISE'"
                  type="button"
                  class="btn-link danger"
                  @click="cancelarPedido(p.id)"
                >
                  Cancelar
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <p v-else class="muted">Nenhum pedido ainda.</p>
    </section>

    <div v-if="editandoId != null" class="panel overlay-block">
      <h2 class="panel-title">Editar pedido #{{ editandoId }}</h2>
      <p class="muted small">Somente pedidos <strong>PENDENTE</strong> podem ser alterados.</p>
      <form class="form-grid" @submit.prevent="salvarEdicao">
        <label class="field">
          <span>Valor mensal</span>
          <input v-model.number="edicao.valorMensal" type="number" min="1" step="0.01" required />
        </label>
        <label class="field">
          <span>Prazo (meses)</span>
          <input v-model.number="edicao.prazoMeses" type="number" min="1" required />
        </label>
        <label class="field">
          <span>Matrícula</span>
          <input v-model="edicao.matricula" required />
        </label>
        <label class="field">
          <span>Ano</span>
          <input v-model.number="edicao.ano" type="number" min="1900" required />
        </label>
        <label class="field">
          <span>Marca</span>
          <input v-model="edicao.marca" required />
        </label>
        <label class="field">
          <span>Modelo</span>
          <input v-model="edicao.modelo" required />
        </label>
        <label class="field">
          <span>Placa</span>
          <input v-model="edicao.placa" required />
        </label>
        <label class="field">
          <span>Proprietário</span>
          <select v-model="edicao.proprietarioTipo" required>
            <option value="CLIENTE">Cliente</option>
            <option value="EMPRESA">Empresa</option>
            <option value="BANCO">Banco</option>
          </select>
        </label>
        <div class="form-actions">
          <button type="button" class="btn-secondary" @click="editandoId = null">Fechar</button>
          <button type="submit" class="btn btn-primary">Salvar</button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.page {
  max-width: 960px;
  margin: 0 auto;
}

.page-head h1 {
  margin: 0 0 0.35rem;
  font-size: 1.6rem;
  font-weight: 700;
  color: var(--text);
}

.page-desc {
  margin: 0 0 1.25rem;
  color: var(--text-muted);
  font-size: 0.95rem;
  max-width: 40rem;
}

.panel {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 1.35rem 1.5rem;
  margin-bottom: 1.25rem;
  box-shadow: var(--shadow-card);
}

.panel-title {
  margin: 0 0 1rem;
  font-size: 1.05rem;
  font-weight: 600;
}

.catalogo-lead {
  margin: -0.35rem 0 1rem;
  font-size: 0.88rem;
  color: var(--text-muted);
  line-height: 1.45;
}

.catalogo-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 0.75rem;
  margin-bottom: 1.25rem;
}

.cat-card {
  display: flex;
  flex-direction: column;
  align-items: stretch;
  text-align: left;
  padding: 0;
  border: 1px solid var(--border);
  border-radius: 12px;
  overflow: hidden;
  background: var(--surface-muted);
  cursor: pointer;
  font: inherit;
  transition:
    border-color 0.15s ease,
    box-shadow 0.15s ease;
}

.cat-card:hover {
  border-color: rgb(37 99 235 / 35%);
  box-shadow: var(--shadow-card-hover);
}

.cat-card--on {
  border-color: var(--accent);
  box-shadow: 0 0 0 2px rgb(37 99 235 / 18%);
}

.cat-img {
  width: 100%;
  aspect-ratio: 16 / 10;
  object-fit: cover;
  display: block;
}

.cat-meta {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
  padding: 0.55rem 0.65rem 0.65rem;
}

.cat-titulo {
  font-size: 0.82rem;
  font-weight: 700;
  color: var(--text);
}

.cat-sub {
  font-size: 0.68rem;
  color: var(--text-muted);
  line-height: 1.35;
}

.veiculo-cell {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.thumb {
  border-radius: 6px;
  object-fit: cover;
  flex-shrink: 0;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 1rem;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}

.field span {
  font-size: 0.72rem;
  font-weight: 600;
  text-transform: uppercase;
  color: var(--text-soft);
}

.field input,
.field select {
  padding: 0.5rem 0.6rem;
  border: 1px solid var(--border-input);
  border-radius: 8px;
  font: inherit;
}

.form-actions {
  grid-column: 1 / -1;
  display: flex;
  gap: 0.5rem;
}

.btn {
  font: inherit;
  cursor: pointer;
  border-radius: 8px;
  padding: 0.55rem 1rem;
  font-weight: 600;
  border: none;
}

.btn-primary {
  background: linear-gradient(135deg, var(--accent) 0%, var(--accent-dark) 100%);
  color: #fff;
}

.btn-secondary {
  background: var(--surface-muted);
  border: 1px solid var(--border);
}

.table-wrap {
  overflow-x: auto;
  border: 1px solid var(--border);
  border-radius: 10px;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.85rem;
}

.data-table th,
.data-table td {
  padding: 0.55rem 0.65rem;
  border-bottom: 1px solid var(--border);
  text-align: left;
}

.data-table th {
  background: var(--surface-muted);
  font-size: 0.7rem;
  text-transform: uppercase;
  color: var(--text-soft);
}

.pill {
  display: inline-block;
  padding: 0.15rem 0.45rem;
  border-radius: 999px;
  font-size: 0.7rem;
  font-weight: 600;
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

.actions {
  white-space: nowrap;
}

.btn-link {
  background: none;
  border: none;
  color: var(--accent);
  font-weight: 600;
  cursor: pointer;
  font-size: 0.82rem;
  margin-right: 0.5rem;
}

.btn-link.danger {
  color: #b91c1c;
}

.muted {
  color: var(--text-muted);
}

.small {
  font-size: 0.82rem;
}

.overlay-block {
  border-color: rgb(37 99 235 / 35%);
}
</style>
