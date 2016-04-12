import mysql.connector

'''
This file is part of "Harta Banilor Publici".

    "Harta Banilor Publici" is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    "Harta Banilor Publici" is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
'''




def getFirstOrder(cursor):
    for (id, contract_title, address, location_lat, location_lng, company, start_date, categories, price, currency,
         CPVCodeID, CPVCode, contract_nr, justify, buyer) in cursor:
        return {
            'id': id,
            'contract_title': contract_title,
            'address': address,
            'location_lat': location_lat,
            'location_lng': location_lng,
            'company': company,
            'start_date': str(start_date),
            'categories': str(categories).split(" "),
            'CPVCodeID': CPVCodeID,
            'CPVCode': CPVCode,
            'contract_nr': contract_nr,
            'price': price,
            'currency': currency,
            'buyer': buyer,
            'justify': int(justify)
        }


def getAllOrder(cursor):
    results = []
    for (id, contract_title, address, location_lat, location_lng, company, start_date, categories, price, currency,
         CPVCodeID, CPVCode, contract_nr, justify, buyer) in cursor:
        results.append({
            'id': id,
            'contract_title': contract_title,
            'address': address,
            'location_lat': location_lat,
            'location_lng': location_lng,
            'company': company,
            'start_date': str(start_date),
            'categories': str(categories).split(" "),
            'CPVCodeID': CPVCodeID,
            'CPVCode': CPVCode,
            'contract_nr': contract_nr,
            'price': price,
            'currency': currency,
            'buyer': buyer,
            'justify': int(justify)
        })
    return results


def get_orders(area):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = ("SELECT id, location_lat, location_lng, contract_title, company, buyer FROM contracte"
             " WHERE"
             " location_lat BETWEEN %s AND %s"
             " AND location_lng BETWEEN %s AND %s order by price desc")

    cursor.execute(query, area)

    result = []
    for (id, location_lat, location_lng, contract_title, company, buyer) in cursor:
        result.append({
            'id': id,
            'lat': location_lat,
            'lng': location_lng,
            'title': contract_title,
            'company': company,
            'buyer': buyer
        })

    cursor.close()
    cnx.close()

    return result


def get_buyers():
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()
    result = []

    query = ("SELECT buyer, location_lat, location_lng, sum(price) as sum1 FROM contracte"
             " GROUP BY buyer")
    cursor.execute(query)

    for (buyer, location_lat, location_lng, sum1) in cursor:
        result.append({
            'buyer': buyer,
            'lat': location_lat,
            'lng': location_lng,
            'sum': sum1
        })

    cursor.close()
    cnx.close()

    return result


def getInitData():
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = ("SELECT count(DISTINCT(buyer)) as buyers, count(DISTINCT(company)) as companies,"
            " count(1) as contracts, sum(price) as sumprice, sum(justify) as justifies"
            " FROM contracte")

    cursor.execute(query)

    for (buyers, companies, contracts, sumprice, justifies) in cursor:
        cursor.close()
        cnx.close()

        return {
                'buyers': str(buyers),
                'companies': str(companies),
                'contracts': str(contracts),
                'sumprice': str(sumprice),
                'justifies': str(justifies)
               }



def get_order(orderId):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = "SELECT id, contract_title, address, location_lat, location_lng, company, start_date, categories, price, currency, CPVCodeID, CPVCode, contract_nr, justify, buyer  \
             FROM contracte WHERE id = '%d'" % (orderId)
    cursor.execute(query)

    result = getFirstOrder(cursor)

    cursor.close()
    cnx.close()
    return result


def get_firm_order_nr(firmName):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query ="select count(1) as count from contracte where company = '%s'" % (firmName)

    cursor.execute(query)
    for count in cursor:
        cursor.close()
        cnx.close()
        return count[0]


def get_firm_order_sum(firmName):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = "select sum(price) as totalPrice from contracte where company like '%s'" % firmName

    cursor.execute(query)
    for totalPrice in cursor:
        cursor.close()
        cnx.close()
        return totalPrice[0]


def get_firm_top_orders(firmName):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = "SELECT id, contract_title, address, location_lat, location_lng, company, start_date, categories, price, currency, CPVCodeID, CPVCode, contract_nr, justify, buyer \
             from contracte where company like '%s' order by price desc limit 10" % (firmName)
    cursor.execute(query)

    results = getAllOrder(cursor)

    cursor.close()
    cnx.close()
    return results


def justify(orderId):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = "UPDATE contracte SET justify = justify + 1 WHERE id = %d" % orderId

    cursor.execute(query)
    cursor.close()
    cnx.commit()
    cnx.close()
    return None


def categoryOrderNr(categoryName):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = "select count(1) as count from contracte where categories like '%s%s%s'" % ("%", categoryName, "%")

    cursor.execute(query)
    for count in cursor:
        cursor.close()
        cnx.close()
        return count[0]


def categoryOrderSum(categoryName):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = "select sum(price) as price from contracte where categories like '%s%s%s'" % ("%", categoryName, "%")

    cursor.execute(query)
    for price in cursor:
        cursor.close()
        cnx.close()
        return price[0]


def categoryOrders(categoryName):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = "SELECT id, contract_title, address, location_lat, location_lng, company, start_date, categories, price, currency, CPVCodeID, CPVCode, contract_nr, justify, buyer \
             from contracte where categories like '%s%s%s' order by price desc limit 10" % ("%", categoryName, "%")
    cursor.execute(query)

    results = getAllOrder(cursor)

    cursor.close()
    cnx.close()
    return results


def categorySimilar(categoryName):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = "SELECT categories \
             from contracte where categories like '%s%s%s' order by rand() limit 1" % ("%", categoryName, "%")
    cursor.execute(query)

    for categories in cursor:
        cursor.close()
        cnx.close()
        return categories[0].split(" ")


def getTop10Firm():
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = "SELECT company, sum(price) as allContractsSum FROM contracte group by company ORDER BY sum(price) desc limit 10"
    cursor.execute(query)

    results = []
    for (company, allContractsSum) in cursor:
        results.append({
            'company': str(company),
            'allContractsSum': int(allContractsSum)
        })

    cursor.close()
    cnx.close()
    return results


def get_orders_by_areaName(areaName):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = "SELECT id, location_lat, location_lng FROM contracte \
            WHERE address like '%s%s%s'" % ("%", areaName, "%")

    cursor.execute(query)

    result = []
    for (id, location_lat, location_lng) in cursor:
        result.append({
            'id': id,
            'lat': location_lat,
            'lng': location_lng
        })
        if (len(result) > 100):
            break

    cursor.close()
    cnx.close()

    return result


def get_top_orders(area):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = ("SELECT id, contract_title, price FROM contracte"
             " WHERE"
             " location_lat BETWEEN %s AND %s"
             " AND location_lng BETWEEN %s AND %s order by price desc")

    cursor.execute(query, area)

    result = []
    for (id, contract_title, price) in cursor:
        result.append({
            'id': id,
            'contract_title': contract_title,
            'price': str(price)
        })

    cursor.close()
    cnx.close()

    return result


def getTop10VotedContracts():
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = "SELECT id, contract_title, price, justify FROM contracte ORDER BY justify desc limit 10"
    cursor.execute(query)

    results = []
    for (id, contract_title, price, justify) in cursor:
        results.append({
            'id': id,
            'contract_title': str(contract_title),
            'price': str(price),
            'justify': int(justify)
        })

    cursor.close()
    cnx.close()
    return results


def getOrdersForBuyer(buyerName):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = "SELECT id, contract_title, address, location_lat, location_lng, company, start_date, categories, price, currency, CPVCodeID, CPVCode, contract_nr, justify, buyer \
             from contracte where buyer like '%s' order by price desc" % (buyerName)
    cursor.execute(query)

    results = getAllOrder(cursor)

    cursor.close()
    cnx.close()
    return results


def getFirmsForBuyer(buyerName):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = "SELECT distinct company as company\
             from contracte where buyer like '%s'" % (buyerName)
    cursor.execute(query)

    results = []
    for (company) in cursor:
        results.append({
            'name': company[0]
        })

    cursor.close()
    cnx.close()
    return results


def getBuyersForFirm(firmName):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = "SELECT distinct buyer as buyer\
             from contracte where company like '%s'" % (firmName)
    cursor.execute(query)

    results = []
    for (buyer) in cursor:
        results.append({
            'name': buyer[0]
        })

    cursor.close()
    cnx.close()
    return results
