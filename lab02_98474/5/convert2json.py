
f = open("music.txt", "r")
fout = open("music.json", "w")
lines = f.readlines()

for line in lines:
    json = ""
    for i in range(len(line)-1):
        if line[i] == ',' and line[i+1] == '{':
            fout.write("\n")
            continue
        fout.write(line[i])


