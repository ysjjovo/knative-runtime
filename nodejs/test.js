/**
 node test.js eyJ0eXBlIjoidGV4dCIsImV4dCI6Imh0bWwifQ==
 **/
var args = process.argv.splice(2);
var params = args[0];
try{
    params = JSON.parse(Buffer.from(params , 'base64').toString());
}catch (e) {
    console.log('argument must be json format!', e);
    return;
}

var main = require('/opt/code')['main'];
if (typeof main !== 'function'){
    console.log(`function:${funcName} not exists!`);
    return
}

const log = function(){
    console.log.apply(this, arguments)
};
Promise.resolve(main(params, log)).then(function (result) {
    if(result === undefined){
        return
    }
    console.log('--boundary--');
    if (result.contentType && result.body && result.body instanceof Buffer) {
        result.body = result.body.toString('base64');
    }
    console.log(JSON.stringify(result));
});