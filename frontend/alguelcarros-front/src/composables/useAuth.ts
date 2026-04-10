import { computed, ref } from 'vue'
import type { LoginResponse, TipoAgente, TipoUsuario } from '../types/api'

const TOKEN_KEY = 'aluguelcarros_token'
const SESSION_KEY = 'aluguelcarros_session'

export type SessionInfo = {
  tipo: TipoUsuario
  userId: number
  nome: string
  tipoAgente: TipoAgente | null
}

const token = ref<string | null>(localStorage.getItem(TOKEN_KEY))
const session = ref<SessionInfo | null>(readSession())

function readSession(): SessionInfo | null {
  const raw = localStorage.getItem(SESSION_KEY)
  if (!raw) return null
  try {
    const s = JSON.parse(raw) as Partial<SessionInfo>
    if (!s.tipo || s.userId == null || !s.nome) return null
    return {
      tipo: s.tipo,
      userId: s.userId,
      nome: s.nome,
      tipoAgente: s.tipoAgente ?? null,
    }
  } catch {
    return null
  }
}

export function useAuth() {
  const isAuthenticated = computed(() => !!token.value)
  const tipo = computed(() => session.value?.tipo ?? null)
  const isCliente = computed(() => tipo.value === 'CLIENTE')
  const isAgente = computed(() => tipo.value === 'AGENTE')
  const isBanco = computed(() => session.value?.tipoAgente === 'BANCO')
  const nome = computed(() => session.value?.nome ?? '')
  const userId = computed(() => session.value?.userId ?? null)

  function setAuth(login: LoginResponse) {
    token.value = login.token
    const s: SessionInfo = {
      tipo: login.tipo,
      userId: login.userId,
      nome: login.nome,
      tipoAgente: login.tipoAgente,
    }
    session.value = s
    localStorage.setItem(TOKEN_KEY, login.token)
    localStorage.setItem(SESSION_KEY, JSON.stringify(s))
  }

  function clearAuth() {
    token.value = null
    session.value = null
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(SESSION_KEY)
  }

  function getToken(): string | null {
    return token.value
  }

  return {
    token,
    session,
    isAuthenticated,
    tipo,
    isCliente,
    isAgente,
    isBanco,
    nome,
    userId,
    setAuth,
    clearAuth,
    getToken,
  }
}
