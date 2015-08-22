// Generated by CoffeeScript 1.9.3
(function() {
  var Entries, StockList, controller, express, insertAndReCalculate;

  express = require('express');

  controller = express.Router();

  Entries = require('../models/Entries');

  StockList = require('../models/StockList');

  controller.get('/stock', function(req, res) {
    var query, stockname;
    query = req.query;
    stockname = query.stockname;
    return StockList.doesStockExit(query).then(function(stockExists) {
      var editId, error, liveEditEntry;
      if (stockExists) {
        liveEditEntry = null;
        if (query.edit) {
          editId = query._id;
          Entries.getEntryById(query._id).then(function(entry) {
            return liveEditEntry = entry;
          });
        } else {
          editId = false;
          liveEditEntry = query;
        }
        return Entries.getEntriesForStockOrdered(stockname).then(function(entries) {
          entries['stockname'] = stockname;
          return res.render('stock', {
            entries: entries,
            liveEditEntry: liveEditEntry,
            editId: editId
          });
        });
      } else {
        error = {
          status: '404',
          stack: 'You have attemped to gail access to stock ' + stockname + '\n But you do not have that stock!'
        };
        return res.render('error', {
          error: error
        });
      }
    });
  });

  controller.post('/addentry', function(req, res) {
    var entry;
    entry = req.body;
    return Entries.getEntryCountMatchingData(entry).then(function(count) {
      var query;
      if (count === 0) {
        insertAndReCalculate(entry);
        return res.redirect('/stock?stockname=' + entry.stockname);
      } else {
        query = '';
        Object.keys(entry).forEach(function(k) {
          return query += '&' + k + '=' + entry[k];
        });
        return res.redirect('/stock?' + query);
      }
    });
  });

  controller.post('/editmode', function(req, res) {
    var entry;
    entry = req.body;
    return res.redirect('/stock?stockname=' + entry.stockname + '&edit=true' + '&_id=' + entry._id);
  });

  controller.post('/editentry', function(req, res) {
    var entry;
    entry = req.body;
    Entries.removeEntryById(entry._id).then(function() {});
    return Entries.getEntryCountMatchingData(entry).then(function(count) {
      insertAndReCalculate(entry);
      if (count === 0) {
        return res.redirect('/stock?stockname=' + entry.stockname);
      } else {
        return res.redirect('stock?stockname=' + entry.stockname + '&edit=true' + '&_id=' + entry._id);
      }
    });
  });

  controller.post('/canceledit', function(req, res) {
    var entry;
    entry = req.body;
    return res.redirect('stock?stockname=' + entry.stockname);
  });

  controller.post('/deleteentry', function(req, res) {
    var entry;
    entry = req.body;
    Entries.removeEntryById(entry._id).then(function() {
      if (err) {
        return res.send('There was a problem deleting the information to the database.');
      }
    });
    return res.redirect('stock?stockname=' + entry.stockname);
  });

  module.exports = controller;

  insertAndReCalculate = function(newEntry) {
    Entries.insertEntry(newEntry);
    return Entries.getEntriesForStockOrdered(newEntry.stockname).then(function(entries) {
      return StockList.getStockByName(newEntry.stockname).then(function(initialValues) {
        var entry, i, lastEntry, len, ref, results;
        lastEntry = {
          quanity: initialValues.number,
          totalshares: initialValues.number,
          acbperunit: (ref = initialValues.number === 0) != null ? ref : {
            0: initialValues.acb / initialValues.number
          },
          acbtotal: initialValues.acb
        };
        results = [];
        for (i = 0, len = entries.length; i < len; i++) {
          entry = entries[i];
          results.push(Entries.removeEntryById(entry._id).then(function() {
            if (entry.buysell === 'buy') {
              entry.totalshares = lastEntry.totalshares + entry.quanity;
              entry.acbtotal = lastEntry.acbtotal + (entry.price * entry.quanity) + entry.commission;
              entry.acbperunit = entry.acbtotal / entry.totalshares;
            } else if (entry.buysell === 'sell') {
              entry.totalshares = lastEntry.totalshares - entry.quanity;
              if (entry.totalshares < 0) {
                entry.problem = true;
              }
              if (entry.totalshares === 0) {
                entry.acbtotal = 0;
                entry.acbperunit = 0;
              } else {
                entry.acbtotal = lastEntry.getACBTotal - (entry.quanity * lastEntry.acbtotal / lastEntry.totalshares);
                entry.acbperunit = entry.acbtotal / entry.totalshares;
              }
              entry.capitalgainloss = ((entry.price * entry.quanity) - entry.commission) - (lastEntry.acbperunit * entry.quanity);
            }
            Entries.insertEntry(entry);
            return lastEntry = entry;
          }));
        }
        return results;
      });
    });
  };

}).call(this);
