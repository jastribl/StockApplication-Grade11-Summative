// Generated by CoffeeScript 1.9.3
(function() {
  var StockList, db, stockListTable;

  db = require('../utilities/DB');

  stockListTable = db.get('stocklist');

  StockList = {
    getStockListOrdered: function() {
      return stockListTable.find({}, {
        sort: {
          stockname: 1
        }
      }, function(err, stockList) {
        if (err) {
          throw err;
        }
        return stockList;
      });
    },
    getStockByName: function(stockName) {
      return stockListTable.findOne({
        stockname: stockname
      }, function(err, initialValues) {
        if (err) {
          throw err;
        }
        return initialValues;
      });
    },
    doesStockWithNameExist: function(stockname) {
      return stockListTable.count({
        'stockname': stockname
      }).then(function(count) {
        return count !== 0;
      });
    },
    addStock: function(stock) {
      return stockListTable.insert(stock);
    },
    removeStock: function(stock) {
      return stockListTable.remove({
        stockname: stock.stockname
      });
    }
  };

  module.exports = StockList;

}).call(this);