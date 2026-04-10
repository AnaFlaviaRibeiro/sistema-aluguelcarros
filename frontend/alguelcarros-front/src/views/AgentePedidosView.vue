<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { api } from '../api'
import { useAuth } from '../composables/useAuth'
import { useFeedback } from '../composables/useFeedback'
import type {
  ClientePerfilResponse,
  PedidoAluguelResponse,
  StatusPedido,
  TipoContrato,
} from '../types/api'

const { show } = useFeedback()
const auth = useAuth()

const pedidos = ref<PedidoAluguelResponse[]>([])
const carregando = ref(false)

const contratoPedido = ref<PedidoAluguelResponse | null>(null)

const contratantePedidoId = ref<number | null>(null)
const contratante = ref<ClientePerfilResponse | null>(null)
const carregandoContratante = ref(false)
const cf = reactive({
  numeroContrato: '',
  dataInicio: '',
  dataFim: '',
  tipoContrato: 'PADRAO' as TipoContrato,
  numeroCredito: '',
  valorAprovado: '' as string | number,
})

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

function proximosStatus(atual: StatusPedido): StatusPedido[] {
  if (atual === 'PENDENTE') return ['EM_ANALISE', 'REPROVADO']
  if (atual === 'EM_ANALISE') return ['APROVADO', 'REPROVADO']
  return []
}

async function carregar() {
  carregando.value = true
  try {
    pedidos.value = await api.agenteListarPedidos()
  } catch (e) {
    show('err', e instanceof Error ? e.message : 'Falha ao listar.')
  } finally {
    carregando.value = false
  }
}

async function avaliar(p: PedidoAluguelResponse, ev: Event) {
  const sel = ev.target as HTMLSelectElement
  const novo = sel.value as StatusPedido
  if (!novo) return
  try {
    await api.agenteAvaliarPedido(p.id, novo)
    show('ok', 'Status atualizado.')
    sel.value = ''
    await carregar()
  } catch (e) {
    show('err', e instanceof Error ? e.message : 'Falha na avaliação.')
    sel.value = ''
  }
}

function abrirContrato(p: PedidoAluguelResponse) {
  contratoPedido.value = p
  cf.numeroContrato = ''
  const hoje = new Date().toISOString().slice(0, 10)
  cf.dataInicio = hoje
  cf.dataFim = hoje
  cf.tipoContrato = 'PADRAO'
  cf.numeroCredito = ''
  cf.valorAprovado = ''
}

async function enviarContrato() {
  if (!contratoPedido.value) return
  const body = {
    numeroContrato: cf.numeroContrato,
    dataInicio: cf.dataInicio,
    dataFim: cf.dataFim,
    tipoContrato: cf.tipoContrato,
    numeroCredito:
      cf.tipoContrato === 'COM_CREDITO' ? String(cf.numeroCredito) : undefined,
    valorAprovado:
      cf.tipoContrato === 'COM_CREDITO' ? Number(cf.valorAprovado) : undefined,
  }
  try {
    await api.agenteRegistrarContrato(contratoPedido.value.id, body)
    show('ok', 'Contrato registrado.')
    contratoPedido.value = null
    await carregar()
  } catch (e) {
    show('err', e instanceof Error ? e.message : 'Falha ao registrar contrato.')
  }
}

const podeCredito = computed(() => auth.isBanco.value)

const somaRendasDeclaradas = computed(() => {
  if (!contratante.value?.empregos?.length) return 0
  return contratante.value.empregos.reduce(
    (acc, e) => acc + Number(e.renda),
    0,
  )
})

async function verContratante(p: PedidoAluguelResponse) {
  if (contratantePedidoId.value === p.id) {
    contratantePedidoId.value = null
    contratante.value = null
    return
  }
  contratantePedidoId.value = p.id
  contratante.value = null
  carregandoContratante.value = true
  try {
    contratante.value = await api.agenteContratanteDoPedido(p.id)
  } catch (e) {
    show('err', e instanceof Error ? e.message : 'Falha ao carregar contratante.')
    contratantePedidoId.value = null
  } finally {
    carregandoContratante.value = false
  }
}

onMounted(() => {
  void carregar()
})
</script>

<template>
  <div class="page">
    <header class="page-head">
      <h1>Painel do Agente</h1>
      <p class="page-desc">
        Consulte os dados do contratante (identificação e rendas declaradas) antes de avaliar. Coloque
        pedidos em análise, aprove ou reprove; após parecer positivo, registre o contrato (com crédito,
        se for o caso, pelo banco agente).
      </p>
    </header>

    <section class="panel">
      <h2 class="panel-title">Fila de pedidos</h2>
      <p v-if="carregando" class="muted">Carregando…</p>
      <div v-else-if="pedidos.length" class="table-wrap">
        <table class="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Cliente</th>
              <th>Data</th>
              <th>Status</th>
              <th>Valor/mês</th>
              <th>Veículo</th>
              <th>Contratante</th>
              <th>Avaliar</th>
              <th>Contrato</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="p in pedidos" :key="p.id">
              <td>{{ p.id }}</td>
              <td>{{ p.nomeCliente }}</td>
              <td>{{ p.dataPedido }}</td>
              <td>
                <span class="pill" :class="pillClass(p.status)">{{ p.status }}</span>
                <div v-if="p.nomeAgenteAvaliador" class="mini muted">
                  {{ p.nomeAgenteAvaliador }}
                </div>
              </td>
              <td>{{ p.valorMensal }}</td>
              <td>{{ p.placaAutomovel }} — {{ p.modeloAutomovel }}</td>
              <td>
                <button
                  type="button"
                  class="btn-link"
                  :class="{ active: contratantePedidoId === p.id }"
                  @click="verContratante(p)"
                >
                  {{ contratantePedidoId === p.id ? 'Ocultar' : 'Ver dados' }}
                </button>
              </td>
              <td>
                <select
                  v-if="proximosStatus(p.status).length"
                  class="select"
                  @change="avaliar(p, $event)"
                >
                  <option value="">Ação…</option>
                  <option v-for="s in proximosStatus(p.status)" :key="s" :value="s">
                    {{ s }}
                  </option>
                </select>
                <span v-else class="muted">—</span>
              </td>
              <td>
                <template v-if="p.status === 'APROVADO' && !p.numeroContrato">
                  <button type="button" class="btn-link" @click="abrirContrato(p)">
                    Registrar
                  </button>
                </template>
                <template v-else-if="p.numeroContrato">
                  <span class="small">{{ p.numeroContrato }}</span>
                  <span v-if="p.numeroCredito" class="mini muted"><br />Crédito {{ p.numeroCredito }}</span>
                </template>
                <span v-else class="muted">—</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <p v-else class="muted">Nenhum pedido.</p>
    </section>

    <section
      v-if="contratantePedidoId != null"
      class="panel contratante-panel"
      aria-live="polite"
    >
      <h2 class="panel-title">
        Contratante — pedido #{{ contratantePedidoId }}
      </h2>
      <p v-if="carregandoContratante" class="muted">Carregando dados…</p>
      <template v-else-if="contratante">
        <dl class="dados-grid">
          <dt>Nome</dt>
          <dd>{{ contratante.nome }}</dd>
          <dt>E-mail</dt>
          <dd>{{ contratante.email }}</dd>
          <dt>RG</dt>
          <dd>{{ contratante.rg }}</dd>
          <dt>CPF</dt>
          <dd>{{ contratante.cpf }}</dd>
          <dt>Endereço</dt>
          <dd>{{ contratante.endereco }}</dd>
          <dt>Profissão</dt>
          <dd>{{ contratante.profissao }}</dd>
        </dl>
        <h3 class="sub-title">Rendas declaradas (empregos)</h3>
        <p class="muted small">
          Soma das rendas informadas pelo cliente:
          <strong>R$ {{ somaRendasDeclaradas.toFixed(2) }}</strong>
          · Valor do pedido: <strong>R$ {{ pedidos.find((x) => x.id === contratantePedidoId)?.valorMensal }}</strong>/mês
        </p>
        <div v-if="contratante.empregos.length" class="table-wrap inner">
          <table class="data-table">
            <thead>
              <tr>
                <th>Empregador</th>
                <th>Renda (R$)</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="emp in contratante.empregos" :key="emp.id">
                <td>{{ emp.entidadeEmpregadora }}</td>
                <td>{{ emp.renda }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <p v-else class="muted">Nenhum emprego cadastrado.</p>
      </template>
    </section>

    <section v-if="contratoPedido" class="panel contrato-panel">
      <h2 class="panel-title">Contrato para pedido #{{ contratoPedido.id }}</h2>
      <form class="form-grid" @submit.prevent="enviarContrato">
        <label class="field">
          <span>Número do contrato</span>
          <input v-model="cf.numeroContrato" required />
        </label>
        <label class="field">
          <span>Início</span>
          <input v-model="cf.dataInicio" type="date" required />
        </label>
        <label class="field">
          <span>Fim</span>
          <input v-model="cf.dataFim" type="date" required />
        </label>
        <label class="field">
          <span>Tipo</span>
          <select v-model="cf.tipoContrato" required>
            <option value="PADRAO">Padrão (sem crédito)</option>
            <option v-if="podeCredito" value="COM_CREDITO">Com contrato de crédito</option>
          </select>
        </label>
        <template v-if="cf.tipoContrato === 'COM_CREDITO'">
          <label class="field">
            <span>Número do crédito</span>
            <input v-model="cf.numeroCredito" required />
          </label>
          <label class="field">
            <span>Valor aprovado</span>
            <input v-model.number="cf.valorAprovado" type="number" min="0.01" step="0.01" required />
          </label>
        </template>
        <div class="form-actions">
          <button type="button" class="btn-secondary" @click="contratoPedido = null">Cancelar</button>
          <button type="submit" class="btn btn-primary">Salvar contrato</button>
        </div>
      </form>
    </section>
  </div>
</template>

<style scoped>
.page {
  max-width: 1100px;
  margin: 0 auto;
}

.page-head h1 {
  margin: 0 0 0.35rem;
  font-size: 1.6rem;
  font-weight: 700;
}

.page-desc {
  margin: 0 0 1.25rem;
  color: var(--text-muted);
  font-size: 0.95rem;
  max-width: 46rem;
  line-height: 1.5;
}

.panel {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 1.35rem 1.5rem;
  margin-bottom: 1.25rem;
  box-shadow: var(--shadow-card);
}

.contrato-panel {
  border-color: rgb(37 99 235 / 30%);
}

.contratante-panel {
  border-color: rgb(5 150 105 / 35%);
  background: linear-gradient(180deg, rgb(236 253 245 / 50%) 0%, var(--surface) 100%);
}

.dados-grid {
  display: grid;
  grid-template-columns: minmax(100px, 140px) 1fr;
  gap: 0.35rem 1rem;
  font-size: 0.88rem;
  margin: 0 0 1rem;
}

.dados-grid dt {
  margin: 0;
  font-weight: 600;
  color: var(--text-soft);
  font-size: 0.72rem;
  text-transform: uppercase;
}

.dados-grid dd {
  margin: 0;
  color: var(--text);
}

.sub-title {
  margin: 0 0 0.5rem;
  font-size: 0.95rem;
  font-weight: 600;
  color: var(--text);
}

.table-wrap.inner {
  margin-top: 0.5rem;
}

.btn-link.active {
  text-decoration: underline;
}

.panel-title {
  margin: 0 0 1rem;
  font-size: 1.05rem;
  font-weight: 600;
}

.table-wrap {
  overflow-x: auto;
  border: 1px solid var(--border);
  border-radius: 10px;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.82rem;
}

.data-table th,
.data-table td {
  padding: 0.55rem 0.6rem;
  border-bottom: 1px solid var(--border);
  text-align: left;
  vertical-align: top;
}

.data-table th {
  background: var(--surface-muted);
  font-size: 0.68rem;
  text-transform: uppercase;
  color: var(--text-soft);
}

.select {
  max-width: 160px;
  font-size: 0.8rem;
  padding: 0.35rem;
  border-radius: 6px;
  border: 1px solid var(--border-input);
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

.mini {
  font-size: 0.72rem;
  margin-top: 0.2rem;
}

.btn-link {
  background: none;
  border: none;
  color: var(--accent);
  font-weight: 600;
  cursor: pointer;
  font-size: 0.82rem;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
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

.muted {
  color: var(--text-muted);
}

.small {
  font-size: 0.82rem;
}
</style>
