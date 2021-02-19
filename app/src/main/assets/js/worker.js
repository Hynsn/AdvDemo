//var i=0;
//
//function timedCount()
//{
//    i=i+1;
//    postMessage(i);
//    setTimeout("timedCount()",500);
//}
//
//timedCount();

//self.addEventListener('message', function(e) {
//  var data = e.data;
//  switch (data.cmd) {
//    case 'start':
//      self.postMessage('WORKER STARTED: ' + data.msg);
//      break;
//    case 'stop':
//      self.postMessage('WORKER STOPPED: ' + data.msg + '. (buttons will no longer work)');
//      self.close();
//      break;
//    default:
//      self.postMessage('Unknown command: ' + data.msg);
//  };
//}, false);

self.addEventListener('message', function(e) {
  var str = JSON.stringify(e.data);
  self.postMessage(str);
}, false);

