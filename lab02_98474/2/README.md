
Colocar os dados do ficheiro "restaurants.json" na sua instalação local do mongo
```
mongoimport --db cbd --collection restaurants --drop --file 2/restaurants.json
```

verificar se os dados foram carregados no servidor
```
$ mongo
> use cbd
> show collections
restaurants
> db.restaurants.count()
3772
```