export interface ClienteResponse {
  id: number
  nome: string
  email: string
  rg: string
  cpf: string
  endereco: string
  profissao: string
}

export interface ClienteRequest {
  nome: string
  email: string
  senha: string
  rg: string
  cpf: string
  endereco: string
  profissao: string
}

export type TipoProprietario = 'CLIENTE' | 'EMPRESA' | 'BANCO'

export type StatusPedido =
  | 'PENDENTE'
  | 'EM_ANALISE'
  | 'APROVADO'
  | 'REPROVADO'
  | 'CANCELADO'

export interface PedidoAluguelRequest {
  clienteId: number
  valorMensal: number
  prazoMeses: number
  matricula: string
  ano: number
  marca: string
  modelo: string
  placa: string
  proprietarioTipo: TipoProprietario
}

export interface PedidoAluguelResponse {
  id: number
  dataPedido: string
  status: StatusPedido
  valorMensal: string
  prazoMeses: number
  clienteId: number
  nomeCliente: string
  placaAutomovel: string
  modeloAutomovel: string
}
