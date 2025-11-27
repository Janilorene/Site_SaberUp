document.addEventListener("DOMContentLoaded", () => {
  const jogosRecentes = [
    { titulo: "Fundamentos de Álgebra", data: "24/09/2025", pontuacao: "85%", link: "fundamentos-algebra.html" },
    { titulo: "Geometria", data: "23/09/2025", pontuacao: "92%", link: "geometria.html" },
    { titulo: "Aritmética Modular", data: "22/09/2025", pontuacao: "78%", link: "aritmetica-modular.html" },
  ];

  const lista = document.getElementById("lista-jogos");
  jogosRecentes.forEach(jogo => {
    const li = document.createElement("li");
    li.innerHTML = `<a href="${jogo.link}">${jogo.titulo}</a> - ${jogo.data} - Pontuação: ${jogo.pontuacao}`;
    lista.appendChild(li);
  });

  document.getElementById("btn-voltar").addEventListener("click", () => {
    window.location.href = "index.html";
  });

  document.getElementById("btn-editar").addEventListener("click", () => {
    alert("Função de edição de perfil em desenvolvimento!");
  });
});