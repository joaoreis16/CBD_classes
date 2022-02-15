# Aula Prática CBD

# Key-Value Databases

chave-valor são os tipos de NoSQL mais simples
* chave unica e um value contendo qualquer info

Key-value pairs 
* Key (id) - usualmente uma string
* value - can be anything

um bucket consegue suportar tipos de dados grandes como BLOBs (Basic Large Objects)

KVs são sistemas baseados em coluna designados pela eficiência

## vantagens destas bases de dados
* alta tolerância a falhas
* Schema-less oferece uma caminho mais facil para mudar os requisitos dos dados
* eficiente
* modelo de data muito simples
* grande escalabilidade horizontalmente sobre centas ou milhares de servers

 ...
 
 
# Redis

Redis é uma base de dados do tipo chave-valor NoSQL open-source, onde tipos de dados mais complexos podem ser guardados 
* In-memory key-value store

```redis-cli ping``` se o sistema responder com PONG, redis está ativo

```DBSIZE``` número total de chaves no redis 

O ponto forte do redis é possuir uma poderosa estrutura de dados

## tipos de dados
* string
* list - lista de elementos podem ser guardados numa chave
* set
* sorted set
* hash - dicionario de campos e valores

## alguns comandos 

* SET testkey hello  -> definir à chave testkey um valor
* EXISTS testkey -> perguntar se existe aquela chave
* TYPE testkey -> indica o tipo do valor da chave
* KEYS * -> lista todas as keys
* DEL testkey -> elimina os dados guardados com aquela chave
* GET testkey -> indica o valor da chave
* CLEAR -> limpa o ecra do termianl
* FLUSHDB -> elimina todas as keys selecionadas na base de dados
* BGSAVE -> salva o currente dataset

INCR [key] -> incrementa um valor guardado em key (tem de ser um inteiro)


# Comandos para os diferentes tipos de dados

## Lists

* lpush(key, value)
* rpush(key, value)
    --> adiciona um novo elemento à head/tail (left/right)

* linsert(key before|after pivot value)
    --> insere um elemento antes/depois de outro

* lpop(key)
* rpop(key)
    --> remove e retorna o primeiro/último elemento (left/right)

* lindex(key, index)
    --> obtem um elemento pelo seu indice

* lrange(key, start, stop)
    --> lista os elementos da lista de uma certa posição até outra



#### comando(s) usados para carregar o ficheiro no Redis 

cat names_counting.txt | redis-cli --pipe
