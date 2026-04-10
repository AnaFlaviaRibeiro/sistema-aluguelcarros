import type { EmpregoRequest } from '../types/api'

export type EmpregoLinha = EmpregoRequest & { rowKey: number }

let novoId = 0

function alocarKeyNegativa(): number {
  novoId -= 1
  return novoId
}

export function linhaEmpregoVazia(): EmpregoLinha {
  return {
    entidadeEmpregadora: '',
    renda: 0,
    rowKey: alocarKeyNegativa(),
  }
}

export function linhasEmpregoDaApi(
  items: { id: number; entidadeEmpregadora: string; renda: number | string }[],
): EmpregoLinha[] {
  return items.map((e) => ({
    entidadeEmpregadora: e.entidadeEmpregadora,
    renda: normalizarRenda(e.renda),
    rowKey: e.id,
  }))
}

export function normalizarRenda(v: number | string): number {
  if (typeof v === 'number' && !Number.isNaN(v)) return v
  const n = parseFloat(String(v).replace(',', '.'))
  return Number.isNaN(n) ? 0 : n
}

export function empregosParaRequest(linhas: EmpregoLinha[]): EmpregoRequest[] {
  return linhas
    .filter((e) => e.entidadeEmpregadora.trim() && normalizarRenda(e.renda) > 0)
    .map((e) => ({
      entidadeEmpregadora: e.entidadeEmpregadora.trim(),
      renda: normalizarRenda(e.renda),
    }))
}
