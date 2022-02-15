findSequence= function (seq) {

    var contacts = db.phones.find().toArray();
    var numbers_with_seq = [];

    for (var i = 0; i < contacts.length; i++){
        
        var number = contacts[i].display.split("-")[1];
        if (number.includes(seq)) {
            numbers_with_seq.push(number);
        }

    }

    return numbers_with_seq;
}
  
