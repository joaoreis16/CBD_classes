
# 3.4 | Base de Dados com Temática Livre

**Tema**: Base de dados para a CP (Comboios de Portugal)

## Criação da KeySpace
```
create keyspace comboios with replication = {'class': 'SimpleStrategy', 'replication_factor' : 3};
use comboios;
```

## Criação das tabelas
```
create table comboio (id int, horario_id int, tipo text, origem text, destino text, paragens set<text>, primary key((id)) );

create table comboio_by_local (id int, horario_id int, tipo text, origem text, destino text, paragens set<text>, primary key((origem, destino)) );

create table comboio_by_origem (id int, horario_id int, tipo text, origem text, destino text, paragens set<text>, primary key((origem), id) );

create table horario (id int, horas_local map<text,text>, primary key(id) );

create table motorista (id int, comboio_id int, nome text, idade int, primary key((id)) );

create table motorista_by_comboio (id int, comboio_id int, nome text, idade int, primary key((comboio_id), id) );

create table cliente (id int, nome text, idade int, comboio_id int, comboios_destino list<int>, primary key(id));
```

## Inserção de dados

* **comboio**
```
insert into comboio (id, horario_id, tipo, origem, destino, paragens) values (1, 1, 'intercidades', 'Porto', 'Faro', {'Aveiro', 'Coimbra', 'lisboa'});
insert into comboio (id, horario_id, tipo, origem, destino, paragens) values (2, 2, 'suburbano', 'Porto', 'Aveiro', {'Ovar', 'Estarreja'});
insert into comboio (id, horario_id, tipo, origem, destino, paragens) values (3, 3, 'interregional', 'Braga', 'Porto', {'Tadim', 'Famalicão', 'Rio Tinto'});
insert into comboio (id, horario_id, tipo, origem, destino, paragens) values (4, 4, 'alfa', 'Braga', 'Faro', {'Porto', 'Aveiro', 'Coimbra', 'Leiria', 'lisboa', 'Beja'});
insert into comboio (id, horario_id, tipo, origem, destino, paragens) values (5, 5, 'intercidades', 'Coimbra', 'Lisboa', {'Aveiro', 'Leiria'});
insert into comboio (id, horario_id, tipo, origem, destino, paragens) values (6, 6, 'interregional', 'Faro', 'Lisboa', {'Portimão', 'Almada', 'Mação'});
insert into comboio (id, horario_id, tipo, origem, destino, paragens) values (7, 7, 'alfa', 'Porto', 'Lisboa', {'Aveiro', 'Coimbra'});
insert into comboio (id, horario_id, tipo, origem, destino, paragens) values (8, 8, 'suburbano', 'Aveiro', 'Porto', {'Cacia', 'Ovar', 'Coimbrões'});
insert into comboio (id, horario_id, tipo, origem, destino, paragens) values (9, 9, 'intercidades', 'Lisboa', 'Braga', {'Porto'});
insert into comboio (id, horario_id, tipo, origem, destino, paragens) values (10, 10, 'interregional', 'Braga', 'Leiria', {'Aveiro', 'Coimbra'});
insert into comboio (id, horario_id, tipo, origem, destino, paragens) values (11, 11, 'suburbano', 'Lisboa', 'Coimbra', {'Aveiro' , 'Santo Tirso'});
insert into comboio (id, horario_id, tipo, origem, destino, paragens) values (12, 12, 'suburbano', 'Ovar', 'Aveiro', {'Estarreja', 'Cacia'});
```

* **comboio_by_local**
```
insert into comboio_by_local (id, horario_id, tipo, origem, destino, paragens) values (1, 1, 'intercidades', 'Porto', 'Faro', {'Aveiro', 'Coimbra', 'lisboa'});
insert into comboio_by_local (id, horario_id, tipo, origem, destino, paragens) values (2, 2, 'suburbano', 'Porto', 'Aveiro', {'Ovar', 'Estarreja'});
insert into comboio_by_local (id, horario_id, tipo, origem, destino, paragens) values (3, 3, 'interregional', 'Braga', 'Porto', {'Tadim', 'Famalicão', 'Rio Tinto'});
insert into comboio_by_local (id, horario_id, tipo, origem, destino, paragens) values (4, 4, 'alfa', 'Braga', 'Faro', {'Porto', 'Aveiro', 'Coimbra', 'Leiria', 'lisboa', 'Beja'});
insert into comboio_by_local (id, horario_id, tipo, origem, destino, paragens) values (5, 5, 'intercidades', 'Coimbra', 'Lisboa', {'Aveiro', 'Leiria'});
insert into comboio_by_local (id, horario_id, tipo, origem, destino, paragens) values (6, 6, 'interregional', 'Faro', 'Lisboa', {'Portimão', 'Almada', 'Mação'});
insert into comboio_by_local (id, horario_id, tipo, origem, destino, paragens) values (7, 7, 'alfa', 'Porto', 'Lisboa', {'Aveiro', 'Coimbra'});
insert into comboio_by_local (id, horario_id, tipo, origem, destino, paragens) values (8, 8, 'suburbano', 'Aveiro', 'Porto', {'Cacia', 'Ovar', 'Coimbrões'});
insert into comboio_by_local (id, horario_id, tipo, origem, destino, paragens) values (9, 9, 'intercidades', 'Lisboa', 'Braga', {'Porto'});
insert into comboio_by_local (id, horario_id, tipo, origem, destino, paragens) values (10, 10, 'interregional', 'Braga', 'Leiria', {'Aveiro', 'Coimbra'});
insert into comboio_by_local (id, horario_id, tipo, origem, destino, paragens) values (11, 11, 'suburbano', 'Lisboa', 'Coimbra', {'Aveiro' , 'Santo Tirso'});
insert into comboio_by_local (id, horario_id, tipo, origem, destino, paragens) values (12, 12, 'suburbano', 'Ovar', 'Aveiro', {'Estarreja', 'Cacia'});
```

* **comboio_by_origem**
```
insert into comboio_by_origem (id, horario_id, tipo, origem, destino, paragens) values (1, 1, 'intercidades', 'Porto', 'Faro', {'Aveiro', 'Coimbra', 'lisboa'});
insert into comboio_by_origem (id, horario_id, tipo, origem, destino, paragens) values (2, 2, 'suburbano', 'Porto', 'Aveiro', {'Ovar', 'Estarreja'});
insert into comboio_by_origem (id, horario_id, tipo, origem, destino, paragens) values (3, 3, 'interregional', 'Braga', 'Porto', {'Tadim', 'Famalicão', 'Rio Tinto'});
insert into comboio_by_origem (id, horario_id, tipo, origem, destino, paragens) values (4, 4, 'alfa', 'Braga', 'Faro', {'Porto', 'Aveiro', 'Coimbra', 'Leiria', 'lisboa', 'Beja'});
insert into comboio_by_origem (id, horario_id, tipo, origem, destino, paragens) values (5, 5, 'intercidades', 'Coimbra', 'Lisboa', {'Aveiro', 'Leiria'});
insert into comboio_by_origem (id, horario_id, tipo, origem, destino, paragens) values (6, 6, 'interregional', 'Faro', 'Lisboa', {'Portimão', 'Almada', 'Mação'});
insert into comboio_by_origem (id, horario_id, tipo, origem, destino, paragens) values (7, 7, 'alfa', 'Porto', 'Lisboa', {'Aveiro', 'Coimbra'});
insert into comboio_by_origem (id, horario_id, tipo, origem, destino, paragens) values (8, 8, 'suburbano', 'Aveiro', 'Porto', {'Cacia', 'Ovar', 'Coimbrões'});
insert into comboio_by_origem (id, horario_id, tipo, origem, destino, paragens) values (9, 9, 'intercidades', 'Lisboa', 'Braga', {'Porto'});
insert into comboio_by_origem (id, horario_id, tipo, origem, destino, paragens) values (10, 10, 'interregional', 'Braga', 'Leiria', {'Aveiro', 'Coimbra'});
insert into comboio_by_origem (id, horario_id, tipo, origem, destino, paragens) values (11, 11, 'suburbano', 'Lisboa', 'Coimbra', {'Aveiro' , 'Santo Tirso'});
insert into comboio_by_origem (id, horario_id, tipo, origem, destino, paragens) values (12, 12, 'suburbano', 'Ovar', 'Aveiro', {'Estarreja', 'Cacia'});
```


* **horario**
```
insert into horario (id, horas_local) values (1, {'06:30':'Porto', '07:30':'Aveiro', '09:00':'Coimbra', '10:30':'Lisboa', '11:30':'Faro'} );
insert into horario (id, horas_local) values (2, {'18:30':'Porto', '18:50':'Ovar', '19:05':'Estarreja', '19:25':'Aveiro'} );
insert into horario (id, horas_local) values (3, {'13:15':'Braga', '13:35':'Tadim', '13:56':'Famalicão', '14:09':'Rio Tinto', '14:24':'Porto'} );
insert into horario (id, horas_local) values (4, {'05:15':'Braga', '06:55':'Porto', '08:05':'Aveiro', '09:42':'Coimbra', '10:25':'Leiria', '12:10':'lisboa','14:10':'Beja', '15:30':'Faro'} );
insert into horario (id, horas_local) values (5, {'16:31':'Coimbra', '15:10':'Aveiro', '15:55':'Leiria', '17:05':'Lisboa'} );
insert into horario (id, horas_local) values (6, {'06:30':'Faro', '07:30':'Portimão', '09:00':'Almada', '10:30':'Mação', '11:30':'Lisboa'} );
insert into horario (id, horas_local) values (7, {'18:30':'Porto', '18:50':'Aveiro', '19:05':'Coimbra', '19:25':'Lisboa'} );
insert into horario (id, horas_local) values (8, {'10:30':'Aveiro', '10:40':'Cacia', '11:05':'Ovar', '11:25':'Coimbrões', '11:40':'Porto'} );
insert into horario (id, horas_local) values (9, {'21:30':'Lisboa', '22:50':'Porto', '23:45':'Braga'} );
insert into horario (id, horas_local) values (10, {'18:30':'Braga', '18:50':'Aveiro', '19:05':'Coimbra', '19:25':'Leiria'} );
insert into horario (id, horas_local) values (11, {'20:30':'Lisboa', '21:50':'Aveiro', '22:30':'Santo Tirso', '23:25':'Coimbra'} );
insert into horario (id, horas_local) values (12, {'18:30':'Ovar', '18:50':'Estarreja', '19:05':'Cacia', '19:25':'Aveiro'} );
insert into horario (id, horas_local) values (13, {'16:30':'Porto', '17:30':'Aveiro', '18:00':'Coimbra', '19:30':'Lisboa', '20:30':'Faro'} );
insert into horario (id, horas_local) values (14, {'16:30':'Porto', '17:30':'Aveiro', '18:00':'Coimbra', '19:30':'Lisboa', '20:30':'Faro'} );
```

* **motorista**
```
insert into motorista (id, comboio_id , nome , idade ) values (1, 5, 'Reis', 53);
insert into motorista (id, comboio_id , nome , idade ) values (2, 1, 'Bernas', 24);
insert into motorista (id, comboio_id , nome , idade ) values (3, 2, 'Serras', 67);
insert into motorista (id, comboio_id , nome , idade ) values (4, 8, 'Artur', 41);
insert into motorista (id, comboio_id , nome , idade ) values (5, 9, 'Mariana', 32);
insert into motorista (id, comboio_id , nome , idade ) values (6, 12, 'Fahla', 56);
insert into motorista (id, comboio_id , nome , idade ) values (7, 11, 'Diogo', 36);
insert into motorista (id, comboio_id , nome , idade ) values (8, 5, 'Ricardo', 40);
insert into motorista (id, comboio_id , nome , idade ) values (9, 1, 'Vasco', 43);
insert into motorista (id, comboio_id , nome , idade ) values (10, 6, 'Eva', 55);
insert into motorista (id, comboio_id , nome , idade ) values (11, 4, 'Paulo', 60);
insert into motorista (id, comboio_id , nome , idade ) values (12, 7, 'Gonçalo', 27);
insert into motorista (id, comboio_id , nome , idade ) values (13, 3, 'Humberto', 27);
```

* **motorista_by_comboio**
```
insert into motorista_by_comboio (id, comboio_id , nome , idade ) values (1, 5, 'Reis', 53);
insert into motorista_by_comboio (id, comboio_id , nome , idade ) values (2, 1, 'Bernas', 24);
insert into motorista_by_comboio (id, comboio_id , nome , idade ) values (3, 2, 'Serras', 67);
insert into motorista_by_comboio (id, comboio_id , nome , idade ) values (4, 8, 'Artur', 41);
insert into motorista_by_comboio (id, comboio_id , nome , idade ) values (5, 9, 'Mariana', 32);
insert into motorista_by_comboio (id, comboio_id , nome , idade ) values (6, 12, 'Fahla', 56);
insert into motorista_by_comboio (id, comboio_id , nome , idade ) values (7, 11, 'Diogo', 36);
insert into motorista_by_comboio (id, comboio_id , nome , idade ) values (8, 5, 'Ricardo', 40);
insert into motorista_by_comboio (id, comboio_id , nome , idade ) values (9, 1, 'Vasco', 43);
insert into motorista_by_comboio (id, comboio_id , nome , idade ) values (10, 6, 'Eva', 55);
insert into motorista_by_comboio (id, comboio_id , nome , idade ) values (11, 4, 'Paulo', 60);
insert into motorista_by_comboio (id, comboio_id , nome , idade ) values (12, 7, 'Gonçalo', 27);
insert into motorista_by_comboio (id, comboio_id , nome , idade ) values (13, 3, 'Humberto', 27);
```

* **cliente**
```
insert into cliente (id, nome, idade, comboio_id, comboios_destino) values (1, 'Edu', 12, 6, [4, 1]);
insert into cliente (id, nome, idade, comboio_id, comboios_destino) values (2, 'Bastos', 64, 4, [2, 5, 7]);
insert into cliente (id, nome, idade, comboio_id, comboios_destino) values (3, 'Carina', 43, 5, [10, 4, 12]);
insert into cliente (id, nome, idade, comboio_id, comboios_destino) values (4, 'Black', 22, 3, [4]);
insert into cliente (id, nome, idade, comboio_id, comboios_destino) values (5, 'Rui', 15, 5, [4, 7, 8]);
insert into cliente (id, nome, idade, comboio_id, comboios_destino) values (6, 'Magalhães', 73, 5, [1, 2, 3, 4]);
insert into cliente (id, nome, idade, comboio_id, comboios_destino) values (7, 'Ferreira', 45, 8, [11]);
insert into cliente (id, nome, idade, comboio_id, comboios_destino) values (8, 'Luís', 23, 1, []);
insert into cliente (id, nome, idade, comboio_id, comboios_destino) values (9, 'André', 65, 1, [6, 9, 7]);
insert into cliente (id, nome, idade, comboio_id, comboios_destino) values (10, 'Tomás', 34, 6, [10, 11]);
insert into cliente (id, nome, idade, comboio_id, comboios_destino) values (11, 'Chicanço', 38, 2, [5, 1, 4, 8]);
insert into cliente (id, nome, idade, comboio_id, comboios_destino) values (12, 'Fabricio', 41, 1, [6]);
```

## Indices
```
create index destiny on comboios.comboio_by_local(destino); 
create index type on comboio(tipo);
```

## Update & Delete
```
update comboio set tipo='alfa' where id=5;
update horario set horas_local+={'20:00':'Lisboa'} where id=10;
update motorista set idade=35 where id=1;
update cliente set comboios_destino=[4,5,6] where id=12;
update comboio_by_local set tipo='alfa' where origem='Braga' and destino='Leiria';

delete from horario where id=14;
delete comboios_destino FROM cliente where id=7;
delete from motorista where id=13;
delete from motorista_by_comboio where comboio_id=3;
delete nome from cliente where id=11;
```

## Queries

1. Todas as paragens do comboio com id = 2
```
cqlsh:comboios> select paragens from comboio where id = 2;

 paragens
-----------------------
 {'Estarreja', 'Ovar'}
```

2. Todos os comboios com sentido Porto - Lisboa
```
cqlsh:comboios> select * from comboio_by_local where origem='Porto' and destino='Lisboa';

 origem | destino | horario_id | id | paragens              | tipo
--------+---------+------------+----+-----------------------+------
  Porto |  Lisboa |          7 |  7 | {'Aveiro', 'Coimbra'} | alfa
```

3. O horário do comboio com sentido Faro - Lisboa
```
Não é possível fazer esta querie porque cassandra não permite join operations.
Neste caso, se a base de dados fosse relacional era só fazer um join entre a tabela comboio e horario.
Uma solução possível é criar uma nova tabela para assim ser possível realizar a querie.
```

4. O horário com id=7
```
cqlsh:comboios> select horas_local from horario where id=7;

 horas_local
------------------------------------------------------------------------------
 {'18:30': 'Porto', '18:50': 'Aveiro', '19:05': 'Coimbra', '19:25': 'Lisboa'}
```

5. A lista de comboios que um determinado cliente tem que apanhar para chegar ao seu destino
```
cqlsh:comboios> select nome, comboios_destino from cliente where id=6;

 nome      | comboios_destino
-----------+------------------
 Magalhães |     [1, 2, 3, 4]
```

6. Todos os comboios que partem do Porto
```
cqlsh:comboios> select * from comboio_by_origem where origem='Porto';

 origem | id | destino | horario_id | paragens                        | tipo
--------+----+---------+------------+---------------------------------+--------------
  Porto |  1 |    Faro |          1 | {'Aveiro', 'Coimbra', 'lisboa'} | intercidades
  Porto |  2 |  Aveiro |          2 |           {'Estarreja', 'Ovar'} |    suburbano
  Porto |  7 |  Lisboa |          7 |           {'Aveiro', 'Coimbra'} |         alfa
```

7. As paragens que o cliente com id=3 irá ter que fazer no comboio onde se situa
```
Não é possível fazer esta querie porque cassandra não permite join operations.
Neste caso, se a base de dados fosse relacional era só fazer um join entre a tabela comboio e cliente.
Uma solução possível é criar uma nova tabela para assim ser possível realizar a querie.
```

8. A idade média dos motoristas da CP que conduzem o comboio com id=5
```
cqlsh:comboios> select avg(idade) as avg_age FROM motorista_by_comboio WHERE comboio_id=5;

 avg_age
---------
      46
```

usar os indices

9. Todos os comboios que têm como destino Faro
```
cqlsh:comboios> select * from comboio_by_local where destino='Faro';

 origem | destino | horario_id | id | paragens                                                   | tipo
--------+---------+------------+----+------------------------------------------------------------+--------------
  Porto |    Faro |          1 |  1 |                            {'Aveiro', 'Coimbra', 'lisboa'} | intercidades
  Braga |    Faro |          4 |  4 | {'Aveiro', 'Beja', 'Coimbra', 'Leiria', 'Porto', 'lisboa'} |         alfa
```

10. 3 comboios do tipo suburbano
```
cqlsh:comboios> select * from comboio where tipo='suburbano' limit 3;

 id | destino | horario_id | origem | paragens                       | tipo
----+---------+------------+--------+--------------------------------+-----------
 11 | Coimbra |         11 | Lisboa |      {'Aveiro', 'Santo Tirso'} | suburbano
  8 |   Porto |          8 | Aveiro | {'Cacia', 'Coimbrões', 'Ovar'} | suburbano
  2 |  Aveiro |          2 |  Porto |          {'Estarreja', 'Ovar'} | suburbano
```
