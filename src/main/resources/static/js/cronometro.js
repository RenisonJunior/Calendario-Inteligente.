// JavaScript puro. O tempo só é salvo no banco quando o usuário clica em Registrar tempo.
(() => {
  const visor = document.getElementById('tempo');
  const formulario = document.getElementById('registrarForm');
  if (!visor || !formulario) return;

  let segundosSalvos = 0;
  let inicio = 0;
  let rodando = false;

  function segundosAtuais() {
    return segundosSalvos + (rodando ? Math.floor((Date.now() - inicio) / 1000) : 0);
  }
  function mostrar() {
    const total = segundosAtuais();
    const min = String(Math.floor(total / 60)).padStart(2, '0');
    const seg = String(total % 60).padStart(2, '0');
    visor.textContent = min + ':' + seg;
  }
  document.getElementById('iniciar').addEventListener('click', () => {
    if (!rodando) { inicio = Date.now(); rodando = true; }
  });
  document.getElementById('pausar').addEventListener('click', () => {
    if (rodando) { segundosSalvos = segundosAtuais(); rodando = false; }
    mostrar();
  });
  document.getElementById('zerar').addEventListener('click', () => {
    segundosSalvos = 0; rodando = false; mostrar();
  });
  formulario.addEventListener('submit', (evento) => {
    const minutos = Math.ceil(segundosAtuais() / 60);
    if (minutos < 1) { evento.preventDefault(); alert('Inicie o cronômetro antes de registrar.'); return; }
    if (minutos > 1440) { evento.preventDefault(); alert('O máximo por registro é 1440 minutos.'); return; }
    document.getElementById('minutosFocoCampo').value = minutos;
  });
  setInterval(mostrar, 250);
  mostrar();
})();
