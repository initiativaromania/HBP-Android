from flask import Flask, jsonify
from flask import request

import db
import map

app = Flask(__name__)

 #TODO: add time interval

@app.route('/getOrders', methods=['GET'])
def getOrders():
    lat = float(request.args.get('lat'))
    lng = float(request.args.get('lng'))
    zoom = int(request.args.get('zoom'))


    return jsonify({
        'orders': db.get_orders(map.get_map_area(lat, lng, zoom))
    })


@app.route('/getOrder', methods=['GET'])
def getOrderDetails():
    id = int(request.args.get('id'))
    return jsonify(db.get_order(id))


@app.route('/getFirm', methods=['GET'])
def getFirmDetails():
    firmName = request.args.get('name')

    return jsonify({
        'ordersNr': int(db.get_firm_order_nr(firmName)),
        'ordersSum': int(db.get_firm_order_sum(firmName)),
        'topOrders': db.get_firm_top_orders(firmName)
    })


@app.route('/justify', methods=['GET'])
def justify():
    orderId = int(request.args.get('id'))
    db.justify(orderId)
    return jsonify({})


@app.route('/getCategorydetails', methods=['GET'])
def getCategorydetails():
    categoryName = request.args.get('categoryName')

    return jsonify({
        'name': categoryName,
        'orderNr': int(db.categoryOrderNr(categoryName)),
        'orderSum': int(db.categoryOrderSum(categoryName)),
        'orders': db.categoryOrders(categoryName),
        'similarCategories': db.categorySimilar(categoryName)
    })

@app.route('/getTop10Firm', methods=['GET'])
def getTop10Firm():
    return jsonify({'all':db.getTop10Firm()})


@app.route('/getTop10VotedContracts', methods=['GET'])
def getTop10VotedContracts():
    return jsonify({'all':db.getTop10VotedContracts()})


@app.route('/getStatisticsArea', methods=['GET'])
def getStatisticsArea():
    lat = float(request.args.get('lat'))
    lng = float(request.args.get('lng'))
    zoom = int(request.args.get('zoom'))

    orders = db.get_top_orders(map.get_map_area(lat, lng, zoom))
    if len(orders) == 0:
        return jsonify({})

    # obtain top 10 categories
    topCategories = []
    for order in orders:
        topCategories.extend(db.get_order(order['id'])['categories'])
    topCategories = list(set(topCategories))

    return jsonify({
        'orders': orders[:100],
        'categories': topCategories[:10]
    })

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')
