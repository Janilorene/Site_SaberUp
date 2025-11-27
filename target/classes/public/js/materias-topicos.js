// Estrutura de séries, matérias e tópicos
const materiasTopicos = {
  "1": {
    "algebra": {
      nome: "Álgebra Básica",
      icone: "bi-calculator",
      topicos: ["Expressões Algébricas", "Equações", "Inequações"]
    },
    "geometria": {
      nome: "Geometria Plana",
      icone: "bi-triangle",
      topicos: ["Triângulos", "Polígonos", "Círculo"]
    }
  },
  "2": {
    "funcoes": {
      nome: "Funções",
      icone: "bi-graph-up",
      topicos: ["Função Afim", "Função Quadrática"]
    },
    "geometria-espacial": {
      nome: "Geometria Espacial",
      icone: "bi-circle",
      topicos: ["Prismas", "Pirâmides", "Cilindro"]
    },
    "trigonometria": {   // nova matéria na série 2
      nome: "Trigonometria",
      icone: "bi-triangle",
      topicos: ["Seno e Cosseno", "Tangente"]
    }
  },
  "3": {
    "probabilidade": {
      nome: "Probabilidade",
      icone: "bi-percent",
      topicos: ["Probabilidade Básica", "Eventos Independentes"]
    },
    "estatistica": {
      nome: "Estatística",
      icone: "bi-bar-chart",
      topicos: ["Distribuição Normal", "Medidas de Tendência"]
    }
  }
};

// Disponibiliza globalmente
window.materiasTopicos = materiasTopicos;
