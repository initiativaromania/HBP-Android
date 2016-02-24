import csv
import MySQLdb

mydb = MySQLdb.connect(host='localhost',
        user='root',
        passwd='',
        db='ir-investitii')
cursor = mydb.cursor()

csv_data = csv.reader(file('contracts_final.csv'))
for row in csv_data:
    print row
    cursor.execute('INSERT INTO contracte(contract_title, \
            address, location_lat, location_lng, company, \
            start_date, categories, price, currency, \
            justify, CPVCodeID, CPVCode, contract_nr, \
            buyer)' \
            'VALUES("%s", "%s", "%s", "%s", "%s", "%s",\
            "%s", "%s", "%s", "%s", "%s", "%s", \
            "%s", "%s")', 
            row)

#csv_data = csv.reader(file('test.csv'))
#for row in csv_data:
#
#    cursor.execute('INSERT INTO testcsv(names, \
#            classes, mark )' \
#            'VALUES("%s", "%s", "%s")', 
#            row)

#close the connection to the database.
mydb.commit()
cursor.close()
print "Done"
