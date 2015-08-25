// Generated by CoffeeScript 1.9.3
(function() {
  var Entries, db, entriesTable;

  db = require('../utilities/DB');

  entriesTable = db.get('entries');

  Entries = {
    getAllEntriesOrdered: function() {
      return entriesTable.find({}, {
        sort: {
          stockname: 1,
          year: 1,
          month: 1,
          day: 1,
          tradenumber: 1
        }
      }, function(err, entries) {
        if (err) {
          throw err;
        }
        return entries;
      });
    },
    getEntriesForStockOrdered: function(stockname) {
      return entriesTable.find({
        stockname: stockname
      }, {
        sort: {
          year: 1,
          month: 1,
          day: 1,
          tradenumber: 1
        }
      }, function(err, entries) {
        if (err) {
          throw err;
        }
        return entries;
      });
    },
    removeEntry: function(entry) {
      return entriesTable.remove({
        stockname: entry.stockname,
        year: entry.year,
        month: entry.month,
        day: entry.day,
        tradenumber: entry.tradenumber
      }, function(err) {
        if (err) {
          throw err;
        }
      });
    },
    getEntryCountMatchingData: function(entry) {
      return entriesTable.count({
        stockname: entry.stockname,
        year: entry.year,
        month: entry.month,
        day: entry.day,
        tradenumber: entry.tradenumber
      });
    },
    insertEntry: function(entry) {
      return entriesTable.insert(entry);
    },
    removeAllEntriesForStockWithName: function(stockName) {
      return entriesTable.remove({
        stockname: stockName
      }, function(err) {
        if (err) {
          throw err;
        }
      });
    }
  };

  module.exports = Entries;

}).call(this);