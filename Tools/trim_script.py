fin = open('in.txt', 'r')
fout = open('out.txt', 'w')

for line in fin:
	if line.startswith('INSERT') and line.find('EXPENSES') != -1:
		tokens = line.split('ARRAY')
		line = tokens[0]
		line = line[:-3] + ')\n'

	print(line)
	fout.write(line)

fin.close()
fout.close()