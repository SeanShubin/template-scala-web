var require = {
    baseUrl: '/',
    paths: {
        'jquery': 'lib/jquery-2.1.1',
        'text': 'lib/text',
        'underscore': 'lib/underscore',
        'underscore.string': 'lib/underscore.string',
        'qunit': 'lib/qunit-1.15.0',
        'sinon': 'lib/sinon-1.10.3'
    },
    shim: {
        'qunit': {
            exports: 'QUnit',
            init: function () {
                QUnit.config.autoload = false;
                QUnit.config.autostart = false;
            }
        },
        'sinon': {
            exports: 'sinon'
        },
        'handlebars':{
            exports:'Handlebars'
        }
    }
};
