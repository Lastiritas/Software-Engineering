f = open('in.txt', 'r');

id_to_payto = {}
payto_to_id = {}

for line in f:
		# INSERT INTO PAYTO VALUES(1,'Cineplex')
		if line.startswith('INSERT INTO PAYTO VALUES'):
			line = line.split('(')[1]
			line = line.split(')')[0]	# should now have the values inside the ()

			tokens = line.split(',')

			id_to_payto[tokens[0]] = tokens[1]
			payto_to_id[tokens[1]] = tokens[0]

f.close()

for i in id_to_payto.keys():
	print(i + '->' + id_to_payto[i] + '->' + payto_to_id[id_to_payto[i]])

file_in = open('in.txt', 'r')
file_out = open('out.txt', 'w')

for line in file_in:
	# INSERT INTO EXPENSES VALUES(1,20131103,100,0,'',1)
	if line.startswith('INSERT INTO EXPENSES VALUES'):
		line = line.split('(')[1]
		line = line.split(')')[0]	# should now have the values inside the ()

		tokens = line.split(',')
		tokens[5] = payto_to_id[id_to_payto[tokens[5]]]

		file_out.write('INSERT INTO EXPENSES VALUES(')
		file_out.write(tokens[0])
		file_out.write(',')
		file_out.write(tokens[1])
		file_out.write(',')
		file_out.write(tokens[2])
		file_out.write(',')
		file_out.write(tokens[3])
		file_out.write(',')
		file_out.write(tokens[4])
		file_out.write(',')
		file_out.write(tokens[5])
		file_out.write(')\n')
	# INSERT INTO PAYTO VALUES(
	elif line.startswith('INSERT INTO PAYTO VALUES'):
		temp = line.split('(')[1]
		temp = temp.split(')')[0]	# should now have the values inside the ()

		tokens = temp.split(',')

		if tokens[0] in payto_to_id.values():
			file_out.write(line)		
	else:
		file_out.write(line)
