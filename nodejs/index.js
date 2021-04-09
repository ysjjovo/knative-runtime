var express    = require('express');
var bodyParser = require('body-parser');
var app = express();
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
var main = process.env.MAIN;
if(!main){
    main = 'main'
}
var mainFunc = require('/opt/code')[main];

app.use((req,res, next) =>{
    const requestId = req.get('x-request-id');
    const log = function(){
        if(requestId){
            Array.prototype.unshift.call(arguments, requestId);
        }
        console.log.apply(this, arguments)
    };
    log('function start!');
    if(req.path === '/health'){
        res.send('ok!');
        log('function end!');
        return;
    }
    let value;
    try {
        let args;
        let query = req.query;
        if(Object.keys(query).length){
            args = query;
        }else{
            args = {};
        }
        for (var pki in Object.keys(req.body)){
            pk = req.body[pki];
            if( args.hasOwnProperty(pk)){
                continue;
            }
            args[pk] = req.body[pk];
        }

        value = mainFunc(args, log);
        log('function end!');
    } catch (e) {
        handleErr(res, e, log);
        log('function end!');
        return
    }
    Promise.resolve(value).then(function (result) {
        if(result === undefined){
            return
        }
        if (result.contentType && result.body && result.body instanceof Buffer) {
            res.set('Content-Type', result.contentType);
            res.send(result.body);
        } else {
            res.status(200).json(result);
        }
    }).catch(function (e) {
        handleErr(res, e);
    })
})

app.listen(8080).timeout = 0;

function handleErr(res,e, log){
    log(e);
    res.set('Content-Type', 'text/plain');
    res.status(500).send('err occurred!');
}

//todo async error cannot catch