# Prioriza — Calendário inteligente

**Projeto acadêmico individual | Java 21 + Spring Boot + Thymeleaf + HTML/CSS puro + H2.**

Frase da página inicial: **Sua rotina mais leve, um dia de cada vez.**

## Páginas

1. **Início:** mensagem inicial e links.
2. **Painel:** uma tabela clássica mostrando prioridade e sugestão automáticas.
3. **Calendário:** calendário mensal com as tarefas no dia escolhido; navegação entre meses.
4. **Tarefas:** cadastrar, listar, editar, concluir, reabrir e excluir.
5. **Detalhes da tarefa:** subtarefas, percentual de conclusão, cronômetro e registro de minutos.
6. **Categorias:** cadastrar e excluir categorias (impede excluir se tiver tarefas).
7. **Perfil:** nome e minutos disponíveis por dia.

## Banco de dados (5 tabelas)

- `categorias` — id, nome
- `tarefas` — id, titulo, descricao, prazo, minutos_estimados, concluida, **categoria_id (FK)**
- `subtarefas` — id, titulo, concluida, **tarefa_id (FK)**
- `registros_foco` — id, data, minutos, **tarefa_id (FK)**
- `perfis` — id, nome, minutos_disponiveis

Relacionamentos: **Categoria 1:N Tarefa; Tarefa 1:N Subtarefa; Tarefa 1:N RegistroFoco**.

## O que é automático?

- **Prioridade:** tarefa atrasada, de hoje, alta (até 2 dias), média (até 7 dias) ou baixa (depois).
- **Sugestão do dia:** o backend coloca tarefas não concluídas em ordem de prazo e sugere "Fazer hoje" enquanto há minutos disponíveis no perfil. As demais recebem "Próximo dia". É uma sugestão simples, não altera as datas cadastradas.
- **Progresso das subtarefas:** percentual calculado automaticamente.
- **Tempo de foco:** minutos registrados pelo cronômetro são somados no backend.
- **Calendário:** gera automaticamente as semanas e mostra atividades em cada dia.

## Como executar no Codespaces

Exige **Java 21**. Abra o terminal na pasta que contém `pom.xml`:

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

Abra a porta **8080** pela aba **Ports** do Codespaces. Para parar, pressione `Ctrl+C`.
O banco é armazenado em `dados/calendario.mv.db`, criado automaticamente.
**Não há login:** o projeto foi simplificado para demonstração local/atividade. Não publique com dados particulares.

## Para substituir os arquivos do projeto antigo sem perder commits

Faça isso **dentro do seu repositório existente**, mantendo a pasta oculta `.git`.
Faça backup antes e coloque este ZIP na raiz do repositório.

```bash
# ATENÇÃO: apaga apenas o código antigo dentro de src (seus commits continuam no Git).
rm -rf src
unzip -o Calendario-Inteligente-Prioriza.zip -d .
chmod +x mvnw
./mvnw clean test
./mvnw spring-boot:run
```

Depois de testar, em outro terminal:

```bash
git add -A
git commit -m "feat: calendario inteligente completo"
git push
```

Não exclua a pasta `.git`. O ZIP **não** inclui `.git`, banco antigo nem arquivos de compilação.
O banco novo `dados/calendario.mv.db` não conflita com o banco antigo `dados/prioriza.mv.db`.

## Organização das pastas

```text
src/main/java/br/com/prioriza/
  PriorizaApplication.java
  controller/   (rotas e formulários)
  model/        (entidades JPA / tabelas)
  repository/   (operações no banco)
  service/      (cálculos automáticos)
src/main/resources/
  templates/    (páginas HTML + Thymeleaf)
  static/css/   (CSS puro)
  static/js/    (JS puro só para o cronômetro)
  application.properties
src/test/       (teste de inicialização)
pom.xml         (dependências Maven)
```
