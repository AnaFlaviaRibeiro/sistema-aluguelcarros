export type TipoUsuario = 'CLIENTE' | 'AGENTE'

export type TipoProprietario = 'CLIENTE' | 'EMPRESA' | 'BANCO'

export type StatusPedido =
  | 'PENDENTE'
  | 'EM_ANALISE'
  | 'APROVADO'
  | 'REPROVADO'
  | 'CANCELADO'

export type TipoContrato = 'PADRAO' | 'COM_CREDITO'

export interface EmpregoRequest {
  entidadeEmpregadora: string
  renda: number
}

export interface RegistroClienteRequest {
  nome: string
  email: string
  senha: string
  rg: string
  cpf: string
  endereco: string
  profissao: string
  empregos: EmpregoRequest[]
}

export interface AtualizarClienteRequest {
  nome: string
  email: string
  senha: string
  rg: string
  cpf: string
  endereco: string
  profissao: string
  empregos: EmpregoRequest[]
}

export interface EmpregoResponse {
  id: number
  entidadeEmpregadora: string
  renda: number
}

export interface ClientePerfilResponse {
  id: number
  nome: string
  email: string
  rg: string
  cpf: string
  endereco: string
  profissao: string
  empregos: EmpregoResponse[]
}

export interface LoginRequest {
  email: string
  senha: string
}

export type TipoAgente = 'EMPRESA' | 'BANCO'

export interface LoginResponse {
  token: string
  tipo: TipoUsuario
  userId: number
  nome: string
  tipoAgente: TipoAgente | null
}

export interface PedidoCriacaoClienteRequest {
  valorMensal: number
  prazoMeses: number
  matricula: string
  ano: number
  marca: string
  modelo: string
  placa: string
  proprietarioTipo: TipoProprietario
}

export type PedidoAtualizacaoClienteRequest = PedidoCriacaoClienteRequest

export interface PedidoAluguelResponse {
  id: number
  dataPedido: string
  status: StatusPedido
  valorMensal: number
  prazoMeses: number
  clienteId: number
  nomeCliente: string
  matricula: string
  ano: number
  marca: string
  placaAutomovel: string
  modeloAutomovel: string
  proprietarioTipo: string
  nomeAgenteAvaliador: string | null
  numeroContrato: string | null
  tipoContrato: string | null
  numeroCredito: string | null
  statusCredito: string | null
  /** Instituição bancária que concedeu o crédito, quando houver */
  nomeBancoConcedenteCredito: string | null
}

export interface ContratoVinculoRequest {
  numeroContrato: string
  dataInicio: string
  dataFim: string
  tipoContrato: TipoContrato
  numeroCredito?: string
  valorAprovado?: number
}

/** Vincular crédito a um contrato PADRAO já registrado pela locadora */
export interface CreditoVinculoRequest {
  numeroCredito: string
  valorAprovado: number
}
