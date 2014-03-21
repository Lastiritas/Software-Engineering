import re

fin = open('in.txt', 'r')
fout = open('out.txt', 'w')

# '2013-08-26 15:00:00'

for line in fin:
	if line.startswith('INSERT') and line.find('EXPENSES') != -1:
		line = re.sub(r"'(....)-(..)-(..)'", r'\1\2\3', line)

	print(line)
	fout.write(line)

fin.close()
fout.close()