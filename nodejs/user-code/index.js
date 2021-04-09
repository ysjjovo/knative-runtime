var fs = require("fs");
module.exports = {
    main: function (params) {
        return handle(params);
    },
    post$register: function (params) {
        if(params.type === 'json'){
            return {
                originParams: params,
                env: process.env,
            }
        }
        return new Promise(((resolve, reject) => {
            fs.readFile(`/opt/code/register_success.html`, (err, data) => {
                if (err) {
                    reject(err);
                    return;
                }
                try{
                    resolve({
                        contentType: `text/html`,
                        body: Buffer.from(data.toString().replace('${name}',params.name))
                    })
                }catch(e){
                    reject(e)
                }
            })
        }))
    },

    get$favicon_ico: function () {
        return new Promise(((resolve, reject) => {
            fs.readFile(`/opt/code/favicon.ico`, (err, data) => {
                if (err) {
                    reject(err);
                    return;
                }
                resolve({
                    contentType: `image/x-ico`,
                    body: data
                })
            })
        }))
    },
    get$example: function (params) {
        return handle(params);
    }
}

function handle(params) {
    return new Promise(((resolve, reject) => {
        var type = params.type;
        var ext = params.ext;
        fs.readFile(`/opt/code/${type}.${params.ext}`, (err, data) => {
            if (err) {
                reject(err);
                return;
            }
            ext = params.subType ? params.subType : ext;
            resolve({
                contentType: `${type}/${ext}`,
                body: data
            })
        })
    }))
}
