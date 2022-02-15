findCapicua = function () {

    var contacts = db.phones.find().toArray();
    var capicua = [];

    for (var i = 0; i < contacts.length; i++){
        
        var number = contacts[i].display.split("-")[1] ;
        var reverseNum = number.split("").reverse().join("");
        if (number.localeCompare(reverseNum) == 0){
            capicua.push(number);
        }
    }

    return capicua;
}
  
