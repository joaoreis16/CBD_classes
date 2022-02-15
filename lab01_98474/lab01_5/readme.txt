# Estruturas usadas no exercício 1.5

Para a resolução deste exercício, faz-se uso de 3 keys:
* users - key para os utilizadores
* seguidores:<username> - key para os seguidores
* mensagens:<username> - key para as mensagens

## key para os utilizadores (users)

Foi utilizado um Set, uma vez que não duplica os dados ao adicionar um utilizador com o mesmo nome.

## key para os seguidores (seguidores:<username>)

Foi utilizado um outro Set, pelo mesmo motivo do anterior (a não duplicação dos dados). Cada utilizador tem o seu Set de seguidores.

## key para as mensagens (mensagens:<username>)

Foi utilizado uma Lista com todas as mensagens enviadas por cada utilizador.