import mysql.connector


def getFirstOrder(cursor):
    for (id, location_name, location_lat, location_lng, company, start_date, end_date, categories, description, price,
         currency) in cursor:
        return {
            'id': id,
            'location_name': location_name,
            'location_lat': location_lat,
            'location_lng': location_lng,
            'company': company,
            'start_date': str(start_date),
            'end_date': str(end_date),
            'categories': str(categories).split(","),
            'description': description,
            'price': price,
            'currency': currency
        }


def getAllOrder(cursor):
    results = []
    for (id, location_name, location_lat, location_lng, company, start_date, end_date, categories, description, price,
         currency) in cursor:
        results.append({
            'id': id,
            'location_name': location_name,
            'location_lat': location_lat,
            'location_lng': location_lng,
            'company': company,
            'start_date': str(start_date),
            'end_date': str(end_date),
            'categories': str(categories).split(","),
            'description': description,
            'price': int(price),
            'currency': str(currency)
        })
    return results


def get_orders(area):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = ("SELECT id, location_lat, location_lng FROM contracte"
             " WHERE"
             " location_lat BETWEEN %s AND %s"
             " AND location_lng BETWEEN %s AND %s")

    cursor.execute(query, area)

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


def get_order(orderId):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = "SELECT id, location_name, location_lat, location_lng, company, start_date, end_date, categories, description, price, currency  \
             FROM contracte WHERE id = '%d'" % (orderId)
    cursor.execute(query)

    result = getFirstOrder(cursor)

    cursor.close()
    cnx.close()
    return result


def get_firm_order_nr(firmName):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = "select count(1) as count from contracte where company like '%s'" % firmName

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

    query = "SELECT id, location_name, location_lat, location_lng, company, start_date, end_date, categories, description, price, currency \
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

    query = "SELECT id, location_name, location_lat, location_lng, company, start_date, end_date, categories, description, price, currency \
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
        return categories[0]


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

    print (results)
    cursor.close()
    cnx.close()
    return results


def get_orders_by_name(areaName):
    cnx = mysql.connector.connect(user='root', database='ir-investitii')
    cursor = cnx.cursor()

    query = "SELECT id, location_lat, location_lng FROM contracte \
            WHERE location_name like '%s%s%s'" % ("%", areaName, "%")

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
