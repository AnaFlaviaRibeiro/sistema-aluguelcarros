/**
 * Frota mock para demonstração: preenche matrícula, ano, marca, modelo e placa.
 * Imagens em `src/assets/frota/` (bundladas pelo Vite; sem pedidos à rede em runtime).
 */
import frotaCompass from '../assets/frota/frota-compass.jpg'
import frotaCorolla from '../assets/frota/frota-corolla.jpg'
import frotaHb20 from '../assets/frota/frota-hb20.jpg'
import frotaKicks from '../assets/frota/frota-kicks.jpg'
import frotaOnix from '../assets/frota/frota-onix.jpg'

export interface VeiculoCatalogo {
  id: string
  marca: string
  modelo: string
  ano: number
  /** Identificador fictício do documento do veículo */
  matricula: string
  /** Placa Mercosul fictícia, única por item */
  placa: string
  imagemUrl: string
  /** Sugestão de valor mensal (R$) */
  valorMensalSugerido: number
}

export const CATALOGO_VEICULOS: VeiculoCatalogo[] = [
  {
    id: 'onix',
    marca: 'Chevrolet',
    modelo: 'Onix LT',
    ano: 2023,
    matricula: 'MOCK-FROTA-001',
    placa: 'BRA2E19',
    imagemUrl: frotaOnix,
    valorMensalSugerido: 1290,
  },
  {
    id: 'hb20',
    marca: 'Hyundai',
    modelo: 'HB20 Sense',
    ano: 2024,
    matricula: 'MOCK-FROTA-002',
    placa: 'RIO8A77',
    imagemUrl: frotaHb20,
    valorMensalSugerido: 1380,
  },
  {
    id: 'kicks',
    marca: 'Nissan',
    modelo: 'Kicks SV',
    ano: 2023,
    matricula: 'MOCK-FROTA-003',
    placa: 'NIS1K42',
    imagemUrl: frotaKicks,
    valorMensalSugerido: 1890,
  },
  {
    id: 'corolla',
    marca: 'Toyota',
    modelo: 'Corolla XEi',
    ano: 2022,
    matricula: 'MOCK-FROTA-004',
    placa: 'TOY9C21',
    imagemUrl: frotaCorolla,
    valorMensalSugerido: 2150,
  },
  {
    id: 'compass',
    marca: 'Jeep',
    modelo: 'Compass Longitude',
    ano: 2023,
    matricula: 'MOCK-FROTA-005',
    placa: 'JEP4M88',
    imagemUrl: frotaCompass,
    valorMensalSugerido: 2650,
  },
]

export function veiculoCatalogoPorPlaca(placa: string): VeiculoCatalogo | undefined {
  return CATALOGO_VEICULOS.find((v) => v.placa === placa)
}
