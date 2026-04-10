import { onMounted, reactive, ref } from 'vue'
import { api } from '../api'
import type {
  ClienteResponse,
  PedidoAluguelResponse,
  StatusPedido,
  TipoProprietario,
} from '../types/api'

const clientes = ref<ClienteResponse[]>([])
const pedidos = ref<PedidoAluguelResponse[]>([])
const loading = ref(false)
const feedback = ref<{ kind: 'ok' | 'err'; text: string } | null>(null)

const clienteSelecionado = ref<number | null>(null)

const novoCliente = reactive({
  nome: '',
  email: '',
  senha: '',
  rg: '',
  cpf: '',
  endereco: '',
  profissao: '',
})

const novoPedido = reactive({
  clienteId: '' as number | '',
  valorMensal: 1500,
  prazoMeses: 12,
  matricula: '',
  ano: new Date().getFullYear(),
  marca: '',
  modelo: '',
  placa: '',
  proprietarioTipo: 'CLIENTE' as TipoProprietario,
})

const statusOptions: StatusPedido[] = [
  'PENDENTE',
  'EM_ANALISE',
  'APROVADO',
  'REPROVADO',
  'CANCELADO',
]

let clientesLoaded = false

function show(kind: 'ok' | 'err', text: string) {
  feedback.value = { kind, text }
  setTimeout(() => {
    feedback.value = null
  }, 6000)
}

async function carregarClientes() {
  loading.value = true
  try {
    clientes.value = await api.listarClientes()
    clientesLoaded = true
  } catch (e) {
    show('err', e instanceof Error ? e.message : 'Falha ao listar clientes.')
  } finally {
    loading.value = false
  }
}

async function garantirClientes() {
  if (!clientesLoaded && !loading.value) {
    await carregarClientes()
  }
}

async function salvarCliente() {
  loading.value = true
  try {
    await api.criarCliente({
      nome: novoCliente.nome,
      email: novoCliente.email,
      senha: novoCliente.senha,
      rg: novoCliente.rg,
      cpf: novoCliente.cpf,
      endereco: novoCliente.endereco,
      profissao: novoCliente.profissao,
    })
    show('ok', 'Cliente criado.')
    Object.assign(novoCliente, {
      nome: '',
      email: '',
      senha: '',
      rg: '',
      cpf: '',
      endereco: '',
      profissao: '',
    })
    await carregarClientes()
  } catch (e) {
    show('err', e instanceof Error ? e.message : 'Falha ao criar cliente.')
  } finally {
    loading.value = false
  }
}

async function removerCliente(id: number) {
  if (!confirm('Remover este cliente?')) return
  loading.value = true
  try {
    await api.deletarCliente(id)
    show('ok', 'Cliente removido.')
    if (clienteSelecionado.value === id) {
      clienteSelecionado.value = null
      pedidos.value = []
    }
    await carregarClientes()
  } catch (e) {
    show('err', e instanceof Error ? e.message : 'Falha ao remover.')
  } finally {
    loading.value = false
  }
}

async function carregarPedidos() {
  if (clienteSelecionado.value == null) {
    pedidos.value = []
    return
  }
  loading.value = true
  try {
    pedidos.value = await api.listarPedidosCliente(clienteSelecionado.value)
  } catch (e) {
    show('err', e instanceof Error ? e.message : 'Falha ao listar pedidos.')
  } finally {
    loading.value = false
  }
}

async function salvarPedido() {
  if (novoPedido.clienteId === '') {
    show('err', 'Escolha o cliente do pedido.')
    return
  }
  loading.value = true
  try {
    await api.criarPedido({
      clienteId: Number(novoPedido.clienteId),
      valorMensal: Number(novoPedido.valorMensal),
      prazoMeses: Number(novoPedido.prazoMeses),
      matricula: novoPedido.matricula,
      ano: Number(novoPedido.ano),
      marca: novoPedido.marca,
      modelo: novoPedido.modelo,
      placa: novoPedido.placa,
      proprietarioTipo: novoPedido.proprietarioTipo,
    })
    show('ok', 'Pedido criado.')
    clienteSelecionado.value = Number(novoPedido.clienteId)
    await carregarPedidos()
  } catch (e) {
    show('err', e instanceof Error ? e.message : 'Falha ao criar pedido.')
  } finally {
    loading.value = false
  }
}

function onStatusChange(pedidoId: number, ev: Event) {
  const el = ev.target as HTMLSelectElement
  void mudarStatus(pedidoId, el.value as StatusPedido)
}

async function mudarStatus(pedidoId: number, status: StatusPedido) {
  loading.value = true
  try {
    await api.atualizarStatusPedido(pedidoId, status)
    show('ok', 'Status atualizado.')
    await carregarPedidos()
  } catch (e) {
    show('err', e instanceof Error ? e.message : 'Falha ao atualizar status.')
  } finally {
    loading.value = false
  }
}

export function useRentalsApp() {
  return {
    clientes,
    pedidos,
    loading,
    feedback,
    clienteSelecionado,
    novoCliente,
    novoPedido,
    statusOptions,
    show,
    carregarClientes,
    garantirClientes,
    salvarCliente,
    removerCliente,
    carregarPedidos,
    salvarPedido,
    onStatusChange,
    mudarStatus,
  }
}

export function useClientesPage() {
  const app = useRentalsApp()
  onMounted(() => {
    void app.carregarClientes()
  })
  return app
}

export function usePedidosPage() {
  const app = useRentalsApp()
  onMounted(() => {
    void app.garantirClientes()
  })
  return app
}
