# Exercícios clojure API

1. Atualização Parcial de Usuário
   Objetivo: Permitir que clientes atualizem apenas alguns campos de um usuário, sem precisar enviar todos os dados.

* Instruções:
  - Adicione uma rota `PATCH /users/:id`.
  - No corpo da requisição, apenas os campos que devem ser atualizados são fornecidos.
  - A função deve mesclar os novos dados com os dados existentes, atualizando apenas os campos especificados.

* Dicas:
  - Utilize `merge` para mesclar o mapa original do usuário com o novo mapa.
  - Retorne um status 200 com os dados do usuário atualizados.

2. Verificação de E-mail Único
   Objetivo: Garantir que o e-mail fornecido para um novo usuário ou em uma atualização seja único.

* Instruções:
  - Na criação e atualização de usuários, verifique se o e-mail fornecido já existe na lista de usuários.
  - Se o e-mail já estiver em uso, retorne um erro `409 Conflict` com uma mensagem indicando que o e-mail já está registrado.
  - Se o e-mail for único, continue o processo normalmente.

* Dicas:
  - Faça uma verificação com some para ver se algum usuário já possui o e-mail fornecido.
  - Essa funcionalidade ajuda a evitar duplicações de e-mails na base de dados.
