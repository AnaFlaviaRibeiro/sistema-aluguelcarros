import type {
  ClienteRequest,
  ClienteResponse,
  PedidoAluguelRequest,
  PedidoAluguelResponse,
  StatusPedido,
} from './types/api'

const BASE = '/api'

function extractMessage(data: unknown): string | null {
  if (data == null) return null
  if (typeof data === 'string') return data
  if (typeof data === 'object' && 'message' in data) {
    const m = (data as { message?: unknown }).message
    if (typeof m === 'string') return m
  }
  return null
}

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const res = await fetch(`${BASE}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  })
  const text = await res.text()
  let parsed: unknown = null
  if (text) {
    try {
      parsed = JSON.parse(text) as unknown
    } catch {
      parsed = text
    }
  }
  if (!res.ok) {
    const msg =
      extractMessage(parsed) || res.statusText || `HTTP ${res.status}`
    throw new Error(msg)
  }
  return parsed as T
}

export const api = {
  listarClientes(): Promise<ClienteResponse[]> {
    return request<ClienteResponse[]>('/clientes')
  },

  criarCliente(body: ClienteRequest): Promise<ClienteResponse> {
    return request<ClienteResponse>('/clientes', {
      method: 'POST',
      body: JSON.stringify(body),
    })
  },

  deletarCliente(id: number): Promise<void> {
    return request<void>(`/clientes/${id}`, { method: 'DELETE' })
  },

  criarPedido(body: PedidoAluguelRequest): Promise<PedidoAluguelResponse> {
    return request<PedidoAluguelResponse>('/pedidos', {
      method: 'POST',
      body: JSON.stringify(body),
    })
  },

  listarPedidosCliente(clienteId: number): Promise<PedidoAluguelResponse[]> {
    return request<PedidoAluguelResponse[]>(`/pedidos/cliente/${clienteId}`)
  },

  atualizarStatusPedido(
    id: number,
    status: StatusPedido,
  ): Promise<PedidoAluguelResponse> {
    return request<PedidoAluguelResponse>(`/pedidos/${id}/status/${status}`, {
      method: 'PATCH',
    })
  },
}
