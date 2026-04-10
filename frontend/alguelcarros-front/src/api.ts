import type {
  AtualizarClienteRequest,
  ClientePerfilResponse,
  ContratoVinculoRequest,
  LoginRequest,
  LoginResponse,
  PedidoAluguelResponse,
  PedidoAtualizacaoClienteRequest,
  PedidoCriacaoClienteRequest,
  RegistroClienteRequest,
  StatusPedido,
} from './types/api'
import { useAuth } from './composables/useAuth'

const BASE = '/api'

function extractMessage(data: unknown): string | null {
  if (data == null) return null
  if (typeof data === 'string') return data
  if (typeof data === 'object' && 'message' in data) {
    const m = (data as { message?: unknown }).message
    if (typeof m === 'string') return m
  }
  if (typeof data === 'object' && '_embedded' in data) {
    const err = (data as { _embedded?: { errors?: { message?: string }[] } })
      ._embedded?.errors?.[0]
    if (err?.message) return err.message
  }
  return null
}

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const auth = useAuth()
  const t = auth.getToken()
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
    ...(options.headers as Record<string, string> | undefined),
  }
  if (t) {
    headers.Authorization = `Bearer ${t}`
  }
  const res = await fetch(`${BASE}${path}`, {
    ...options,
    headers,
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
  login(body: LoginRequest): Promise<LoginResponse> {
    return request<LoginResponse>('/auth/login', {
      method: 'POST',
      body: JSON.stringify(body),
    })
  },

  registrarCliente(body: RegistroClienteRequest): Promise<ClientePerfilResponse> {
    return request<ClientePerfilResponse>('/auth/registro/cliente', {
      method: 'POST',
      body: JSON.stringify(body),
    })
  },

  clientePerfil(): Promise<ClientePerfilResponse> {
    return request<ClientePerfilResponse>('/cliente/perfil')
  },

  clienteAtualizarPerfil(body: AtualizarClienteRequest): Promise<ClientePerfilResponse> {
    return request<ClientePerfilResponse>('/cliente/perfil', {
      method: 'PUT',
      body: JSON.stringify(body),
    })
  },

  clienteListarPedidos(): Promise<PedidoAluguelResponse[]> {
    return request<PedidoAluguelResponse[]>('/cliente/pedidos')
  },

  clienteBuscarPedido(id: number): Promise<PedidoAluguelResponse> {
    return request<PedidoAluguelResponse>(`/cliente/pedidos/${id}`)
  },

  clienteCriarPedido(body: PedidoCriacaoClienteRequest): Promise<PedidoAluguelResponse> {
    return request<PedidoAluguelResponse>('/cliente/pedidos', {
      method: 'POST',
      body: JSON.stringify(body),
    })
  },

  clienteAtualizarPedido(
    id: number,
    body: PedidoAtualizacaoClienteRequest,
  ): Promise<PedidoAluguelResponse> {
    return request<PedidoAluguelResponse>(`/cliente/pedidos/${id}`, {
      method: 'PUT',
      body: JSON.stringify(body),
    })
  },

  clienteCancelarPedido(id: number): Promise<PedidoAluguelResponse> {
    return request<PedidoAluguelResponse>(`/cliente/pedidos/${id}/cancelar`, {
      method: 'PATCH',
    })
  },

  agenteListarPedidos(): Promise<PedidoAluguelResponse[]> {
    return request<PedidoAluguelResponse[]>('/agente/pedidos')
  },

  agenteContratanteDoPedido(pedidoId: number): Promise<ClientePerfilResponse> {
    return request<ClientePerfilResponse>(`/agente/pedidos/${pedidoId}/contratante`)
  },

  agenteAvaliarPedido(id: number, status: StatusPedido): Promise<PedidoAluguelResponse> {
    return request<PedidoAluguelResponse>(`/agente/pedidos/${id}/status/${status}`, {
      method: 'PATCH',
    })
  },

  agenteRegistrarContrato(
    pedidoId: number,
    body: ContratoVinculoRequest,
  ): Promise<PedidoAluguelResponse> {
    return request<PedidoAluguelResponse>(`/agente/pedidos/${pedidoId}/contrato`, {
      method: 'POST',
      body: JSON.stringify(body),
    })
  },
}
