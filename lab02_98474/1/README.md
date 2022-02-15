# MongoDB

* é uma base de dados orientada a ***documentos*** representados numa estrutura JSON.
* é um projeto de código-aberto

## Ligar o servidor MongoDB

```
mongod --dbpath 3ano/CBD/lab02
```

ou, apenas, 

```
mongod
```

## correr MongoDB 

```
mongo
```


## comandos mais utilizados

* ```use [database name]``` --> criar uma base de dados
* ```db``` --> indica a base de dados selecionada
* ```db.movie.insert({"name": "tutorials point"})```
* ```db.dropDatabase()``` --> eliminar uma base de dados
* ```show dbs``` --> lista das base de dados ativas
* ```db.createCollection([name], [options])``` -->
    ```
        db.createCollection("mycollection")
    ```

    ```
        db.createCollection("mycol", { capped : true, autoIndexID : true, size : 6142800, max : 10000 } ){
            "ok" : 0,
            "errmsg" : "BSON field 'create.autoIndexID' is an unknown field.",
            "code" : 40415,
            "codeName" : "Location40415"
        }
    ```
* ```db.mycollection.drop()``` --> eliminar coleção
* ```db.users.insert([document])``` --> inserir documentos
* ```db.users.insertMany([documentos])``` --> insere vários documentos
* ```db.mycol.find()``` --> irá apresentar todos os documentos de forma não-estruturada
* ```db.COLLECTION_NAME.update(SELECTION_CRITERIA, UPDATED_DATA)``` --> irá dar update nos dados selecionados