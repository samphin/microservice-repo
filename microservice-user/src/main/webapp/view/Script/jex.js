(function () {
    var global = this;

    /*************************************************** Function ***************************************************/

    /*函数反柯理化*/
    Function.prototype._uncurry = function () {
        var me = this,
            args = arguments,
            is = Object._isFunction(me);
        return function () {
            if (!is) return null;
            if (arguments.length === 0) { arguments.length = 1; }
            if (Object._isNullOrUndefined(arguments[0])) { arguments[0] = args[0]; }
            return Function.prototype.call.apply(me, arguments);
        };
    };

    /*将 function 中的 this 指向一个固定的对象*/
    Function.prototype._this = function () {
        var me = this,
            args = arguments,
            is = Object._isFunction(me);
        return function () {
            return is ? Function.prototype.apply.apply(me, [args[0], arguments]) : null;
        };
    };

    /*************************************************** Function ***************************************************/




    /*************************************************** RegExp ***************************************************/

    /*字符串的正则替换方法*/
    RegExp.prototype._replace = function () {
        return String.prototype.replace.call(Object._toStr(arguments[0]), this, arguments[1] || '');
    };

    /*************************************************** RegExp ***************************************************/




    /*************************************************** Number ***************************************************/

    /*获取十六进制*/
    Number.prototype._hex = function () {
        var str = Object._toInt(this).toString(16);
        return String.prototype._lfill.call(str, Object._toInt(arguments[0]) - str.length, '0');
    };

    /*执行指定次数的动作*/
    Number.prototype._times = function () {
        if (Object._isFunction(arguments[0])) {
            var i = 0, len = Object._toInt(this);
            for (; i < len; i++) { Function.prototype.call.apply(arguments[0], [i, i]); }
        }
    };

    /*用多个字符填充到数字左边*/
    Number.prototype._lfill = function () {
        return String.prototype._lfill.apply(String(this), [arguments[0], arguments[1] || '0']);
    };

    /*用多个字符填充到数字右边*/
    Number.prototype._rfill = function () {
        return String.prototype._rfill.apply(String(this), [arguments[0], arguments[1] || '0']);
    };

    /*用多个字符填充到数字左边*/
    Number.prototype._lpad = function () {
        return String.prototype._lpad.apply(String(this), [arguments[0], arguments[1] || '0']);
    };

    /*用多个字符填充到数字右边*/
    Number.prototype._rpad = function () {
        return String.prototype._rpad.apply(String(this), [arguments[0], arguments[1] || '0']);
    };

    /*************************************************** Number ***************************************************/




    /*************************************************** Date ***************************************************/

    /*日期加 多少天*/
    Date.prototype._addDays = function () {
        return Date.prototype._addHours.call(this, Object._toNumber(arguments[0]) * 24);
    };

    /*日期加 多少时*/
    Date.prototype._addHours = function () {
        return Date.prototype._addMinutes.call(this, Object._toNumber(arguments[0]) * 60);
    };

    /*日期加 多少分*/
    Date.prototype._addMinutes = function () {
        return Date.prototype._addSeconds.call(this, Object._toNumber(arguments[0]) * 60);
    };

    /*日期加 多少秒*/
    Date.prototype._addSeconds = function () {
        return Date.prototype._addMilliseconds.call(this, Object._toNumber(arguments[0]) * 1000);
    };

    /*日期加 多少毫秒*/
    Date.prototype._addMilliseconds = function () { return new Date(this.getTime() + Object._toNumber(arguments[0])); };

    /*计算两个日期之间的差值*/
    Date.prototype._diff = function () {
        var ms, date = arguments[0];
        if (!Object._isDate(this) || !Object._isDate(date)) return NaN;
        ms = this.getTime() - date.getTime();
        return ({ ms: ms, ss: ms / 1000, mm: ms / 1000 / 60, hh: ms / 1000 / 60 / 60, dd: ms / 1000 / 60 / 60 / 24 })[arguments[1] || 'ms'];
    };

    /*计算两个日期之间的差值*/
    Date.prototype._diff2 = function () {
        if (Object._isDate(this) && Object._isDate(arguments[0])) {
            var ms_ = this.getTime() - arguments[0].getTime(), ms = ms_ % 1000,
                ss_ = parseInt(ms_ / 1000), ss = ss_ % 60, mm_ = parseInt(ss_ / 60), mm = mm_ % 60,
                hh_ = parseInt(mm_ / 60), hh = hh_ % 24, dd_ = parseInt(hh_ / 24);
            return { dd: dd_, hh: hh, mm: mm, ss: ss, ms: ms };
        } return null;
    };

    /*计算两个日期之间的差值 格式化成字符串*/
    Date.prototype._diff2_format = function () {
        var diff = Date.prototype._diff2.call(
            this, arguments[0]), format = Object._toStr(arguments[1]) || 'dd hh:mm:ss:ms';
        if (!!diff) {
            format = format.replace(/dd/g, diff.dd);
            format = format.replace(/ms/g, Number.prototype._lpad.call(diff.ms, 3));
            return format.replace(/h{1,2}|m{1,2}|s{1,2}/g, function (m) {
                return m.length === 1 ? diff[m + m] : Number.prototype._lpad.call(diff[m], 2);
            });
        } return '';
    };

    /*日期转换为对象*/
    Date.prototype._toSysDate = Date.prototype._toObject = function () { return new sys.Date(this); };

    /*日期转换为字符串*/
    Date.prototype._toStr = function () { return sys.Date.prototype.toString.apply(new sys.Date(this), arguments); };

    /*************************************************** Date ***************************************************/




    /*************************************************** String ***************************************************/

    /*去第几个字符*/
    String.prototype._item = function (i) { return this[i] || String(this)[i] || this.charAt(i); };

    /*第一个字符*/
    String.prototype._firstChar = function () { return String.prototype._item.call(this, 0); };

    /*最后一个字符*/
    String.prototype._lastChar = function () { return String.prototype._item.call(this, this.length - 1); };

    /*去掉字符串左边的空白字符*/
    String.prototype._ltrim = function () { return String.prototype.replace.call(this || '', /^\s+/g, ''); };

    /*去掉字符串右边的空白字符*/
    String.prototype._rtrim = function () { return String.prototype.replace.call(this || '', /\s+$/g, ''); };

    /*去掉字符串左右两边的空白字符*/
    String.prototype._trim = function () { return String.prototype.replace.call(this || '', /^\s+|\s+$/g, ''); };

    /*去掉字符串左边的给定字符*/
    String.prototype._ltrimChar = function () {
        return String.prototype.replace.call(this || '', RegExp('^(' + (RegExp._or_Repl.apply(null, arguments) || '\\s') + ')+', 'g'), '');
    };

    /*去掉字符串右边的给定字符*/
    String.prototype._rtrimChar = function () {
        return String.prototype.replace.call(this || '', RegExp('(' + (RegExp._or_Repl.apply(null, arguments) || '\\s') + ')+$', 'g'), '');
    };

    /*去掉字符串左右两边的给定字符*/
    String.prototype._trimChar = function () {
        return (function (str) {
            return String.prototype.replace.call(this || '', RegExp('^(' + str + ')+|(' + str + ')+$', 'g'), '');
        }).call(this, RegExp._or_Repl.apply(null, arguments) || '\\s');
    };

    /*用多个字符填充到字符串左边*/
    String.prototype._lfill = function () {
        return String.prototype._repeat.call(arguments[1] || ' ', arguments[0]) + (this || '');
    };

    /*用多个字符填充到字符串右边*/
    String.prototype._rfill = function () {
        return this + String.prototype._repeat.call(arguments[1] || ' ', arguments[0]);
    };

    /*用多个字符填充到字符串左边*/
    String.prototype._lpad = function () {
        return String.prototype._lfill.call(this, Object._toInt(arguments[0]) - this.length, arguments[1] || ' ');
    };

    /*用多个字符填充到字符串右边*/
    String.prototype._rpad = function () {
        return String.prototype._rfill.call(this, Object._toInt(arguments[0]) - this.length, arguments[1] || ' ');
    };

    /*将字符串重复多次返回新的字符串*/
    String.prototype._repeat = function () {
        var len = Object._toInt(arguments[0]) + 1; return len >= 0 ? new Array(len).join(this || '') : '';
    };

    /*连接字符串*/
    String.prototype._join = function () {
        return Array.prototype._join.apply(
            Object._toArray(arguments)._splice(0, Object._isFunction(arguments[0]) ? 1 : 0),
            [this, arguments[0]]);
    };

    /*将 JSON 字符串转换成对象*/
    String.prototype._jsonParse = function () {
        try { return typeof JSON != 'undefined' && JSON.parse(this) || Function('return ' + this + ';')(); } catch (e) { }
    };

    /*将字符串 URL 转换成对象*/
    String.prototype._toUrl = function () { return new sys.Url(this); };

    /*设置 URL 的查询字符串*/
    String.prototype._setUrlQuery = function () {
        var query = arguments[0],
            url = String.prototype._toUrl.call(this),
            new_query = Object._isString(query) ? String._toQuery(query) : query,
            search = !!arguments[1] ? new_query : Object._copy(String._toQuery((url || {}).search), new_query);
        return !!url ? url.set('search', '?' + Object._toQueryString(search)).toString() : this;
    };

    /*获取 URL 字符串中的查询字符串对象*/
    String.prototype._getUrlQuery = function () {
        return String.prototype._toQuery.call((String.prototype._toUrl.call(this) || {}).search);
    };

    /*查询字符串转换成 Object*/
    String.prototype._toQuery = function () {
        var query = {},
            queryStr = Object._toStr(this)._trimChar('?', '&');
        Array.prototype._each.call(
            queryStr.split('&'), function (i, v) {
                if (v === '') return;
                var kv = String.prototype.split.call(v, '=');
                query[String._decodeURI(kv[0] || '')._trim()] = String._decodeURI(kv[1] || '');
            });
        return query;
    };

    /*根据查询字符串获取值*/
    String.prototype._queryStr = function () { return String.prototype._toQuery.call(this)[arguments[0]] || ''; };

    /*遍历每个字符*/
    String.prototype._each = function () { Object._each(Object._toStr(this), arguments[0]); };

    /*字符串转成数字*/
    String.prototype._toNumber = function () { return Object._toNumber(this); };

    /*字符串转成整数*/
    String.prototype._toInt = function () { return Object._toInt(this); };

    /*URL编码字符串*/
    String.prototype._encodeURI = function () { return encodeURIComponent(this); };

    /*URL解码字符串*/
    String.prototype._decodeURI = function () { return decodeURIComponent(this); };

    /*是否包含某字符串*/
    String.prototype._contains = function () {
        return RegExp('(' + RegExp._or_Repl.apply(null, arguments) + ')').test(this);
    };

    /*是否包含某字符串，不区分大小写*/
    String.prototype._contains2 = function () {
        return RegExp('(' + RegExp._or_Repl.apply(null, arguments) + ')', 'i').test(this);
    };

    /*是否是某字符串开头*/
    String.prototype._startWith = function () {
        return RegExp('^(' + RegExp._or_Repl.apply(null, arguments) + ')').test(this);
    };

    /*是否是某字符串开头，不区分大小写*/
    String.prototype._startWith2 = function () {
        return RegExp('^(' + RegExp._or_Repl.apply(null, arguments) + ')', 'i').test(this);
    };

    /*是否是某字符串结尾*/
    String.prototype._endWith = function () {
        return RegExp('(' + RegExp._or_Repl.apply(null, arguments) + ')$').test(this);
    };

    /*是否是某字符串开头，不区分大小写*/
    String.prototype._endWith2 = function () {
        return RegExp('(' + RegExp._or_Repl.apply(null, arguments) + ')$', 'i').test(this);
    };

    /*获取 Unicode*/
    String.prototype._toUnicode = function () {
        var code, array = [];
        for (var i = 0, len = this.length; i < len; i++) {
            code = this.charCodeAt(i);
            Array.prototype.push.call(array,
                arguments[0] || code > 126 || code < 32 ?
                    '\\u' + Number.prototype._hex.call(code, 4) : this.charAt(i));
        }
        return array.join('');
    };

    /*将 Unicode 字符串转换成普通字符串*/
    String.prototype._fromUnicode = function () {
        return unescape(this.replace(
            /\\u([\dA-F]{4})/gi, function (m, v) { return String.fromCharCode(parseInt(v, 16)); }));
    };

    /*原始字符串*/
    String.prototype._repl = function () {
        var dict = { "'": "\\'", '"': '\\"', '\\': '\\\\', '\b': '\\b', '\f': '\\f', '\n': '\\n', '\r': '\\r', '\t': '\\t' };
        return this.replace(/['"\\\b\f\n\r\t]/g, function (m) { return dict[m]; });
    };

    /*对字符串进行 HTML 编码*/
    String.prototype._encodeHTML = function () {
        var re = !!arguments[0] ? /&|<|>|"|'/g : /&(?!#?\w+;)|<|>|"|'/g,
            dict = { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&apos;' };
        return this.replace(re, function (m) { return dict[m]; });
    };

    /*对字符串进行 HTML 解码*/
    String.prototype._decodeHTML = function () {
        var dict = { '&amp;': '&', '&lt;': '<', '&gt;': '>', '&quot;': '"', '&apos;': "'" };
        return this.replace(/&#?\w+;/ig, function (m) {
            var v = String(m).toLowerCase();
            return dict[v] || (v.indexOf('&#') === 0 ? String.fromCharCode(v.substring(2, v.length - 1)) : m);
        });
    };

    /*字符串模版编译*/
    String.prototype._templateCompile = function (key) { return String.prototype.___templateCompile.call(this, { key: key }); };

    /*字符串模版编译*/
    String.prototype.___templateCompile = function () {
        var me = this,
            array = [], func = null,
            source = '', args = arguments,
            settings = args[0], key = settings.key,
            cmstr = settings.cmstr || '--', lstr = settings.lstr || '<#', rstr = settings.rstr || '#>';

        args.callee.___templateCache = args.callee.___templateCache || {};
        if (!!key && Object._isFunction(args.callee.___templateCache[key])) { return args.callee.___templateCache[key]; }

        array.push(
            'var $array = [], $me = this;\n',
            'var $args = Object._toArray(arguments);\n',
            'var $write = Array.prototype.push._this($array);\n',
            'var $data = $me.$data = this.$data = Object._isNullOrUndefined($args[0]) ? {} : $args[0];\n');

        String.prototype._templateAnalyze.apply(me, [
            lstr + cmstr, cmstr + rstr, function () {
                if (!this.isLogic) {
                    return String.prototype._templateAnalyze.apply(this.string, [
                        lstr, rstr, function () {
                            if (this.isLogic) {
                                var str = this.string._ltrim();
                                switch (str.charAt(0)) {
                                    case '=':
                                        return str.charAt(1) === '=' ?
                                            array.push('$write(Object._toStr(', str.substring(2), '));\n') :
                                            array.push('$write(String.prototype._encodeHTML.call(Object._toStr(', str.substring(1), ')));\n');
                                    default: return array.push(str, '\n');
                                }
                            }
                            return array.push('$write("', String.prototype._repl.call(this.string), '");\n');
                        }
                    ]);
                }
            }
        ]);

        source = array._push('return $array.join("");').join('');

        try { func = Function(source); } catch (e) { throw new_Error(e.stack); }

        return args.callee.___templateCache[key || ''] = function () {
            try { return Function.prototype.apply.apply(func, [{ $source: source }, arguments]); }
            catch (e) { throw new_Error(e.stack); }
        };

        function new_Error() {
            return new Error(String._format(
                'Template error: {0}\n\n{1}\n\n{2}', arguments[0], me, source));
        }
    };

    /*字符串模版格式解析*/
    String.prototype._templateAnalyze = function () {
        var li, ri,
            array = [],
            func = arguments[2],
            source = Object._toStr(this),
            is = Object._isFunction(func),
            l_str = Object._toStr(arguments[0]),
            r_str = Object._toStr(arguments[1]),
            lstr_rstr = { lStr: lstr, rStr: rstr },
            lreg = /^[\<\{\[\(][~\!@#\$%\^&\*\(\-\+=\{\[\|\/\?]+$/,
            rreg = /^[~\!@#\$%\^&\*\)\-\+=\}\]\|\/\?]+[\>\}\]\)]$/,
            lstr = lreg.test(l_str) ? l_str : '<#', rstr = rreg.test(r_str) ? r_str : '#>',
            msg = 'Template string syntax error !';

        while (source.length > 0) {
            li = source.indexOf(lstr); ri = source.indexOf(rstr);
            if (li < 0 && ri < 0) {
                Array.prototype.push.call(array, { isLogic: false, string: source });
                if (is) { Function.prototype.call.apply(func, [array._last(), lstr_rstr]); } break;
            } else if (li >= 0 && ri >= 0 && ri - li >= lstr.length) {
                var logic = source.substring(li + lstr.length, ri);
                if (logic.indexOf(lstr) >= 0) { throw new Error(msg); }
                Array.prototype.push.call(array, { isLogic: false, string: source.substring(0, li) });
                if (is) { Function.prototype.call.apply(func, [array._last(), lstr_rstr]); }
                Array.prototype.push.call(array, { isLogic: true, string: logic });
                if (is) { Function.prototype.call.apply(func, [array._last(), lstr_rstr]); }
                source = source.substring(ri + rstr.length);
            } else { throw new Error(msg); }
        }
        return array;
    };

    /*字符串格式化*/
    String.prototype.___format = function () {
        var li, ri,
            lc = '{', rc = '}',
            source = Object._toStr(this),
            array = new Array(), func = arguments[0],
            e = arguments.callee.syntaxError = new Error('Format string syntax error !');

        if (Object._isFunction(func)) {
            while (source.length > 0) {
                li = source.indexOf(lc); ri = source.indexOf(rc);
                if (li < 0 && ri < 0) {
                    Array.prototype.push.call(array, source); break;
                }
                if (li >= 0 &&
                    (li < ri || ri < 0) && source.charAt(li + 1) === lc) {
                    Array.prototype.push.call(array, source.substring(0, li + 1));
                    source = source.substring(li + 2); continue;
                }
                if (ri >= 0 &&
                    (ri < li || li < 0) && source.charAt(ri + 1) === rc) {
                    Array.prototype.push.call(array, source.substring(0, ri + 1));
                    source = source.substring(ri + 2); continue;
                }
                if (li >= 0 && ri >= 0 && ri > li) {
                    Array.prototype.push.apply(
                        array, [source.substring(0, li),
                        Object._toStr(Function.prototype.call.apply(
                            func, [Object._toArray(arguments)._shift(), source.substring(li + 1, ri)._trim()]))]);
                    source = source.substring(ri + 1); continue;
                } throw e;
            }
            return array.join('');
        }
        return this;
    };

    /*字符串格式化*/
    String.prototype._format = function () {
        var re = /^\d+$/;
        return String.prototype.___format.apply(this,
            Object._toArray(arguments)._unshift(function (k) {
                if (re.test(k)) { return this[parseInt(k)]; } throw String.prototype.___format.syntaxError;
            }));
    };

    /*字符串格式化*/
    String.prototype._format_map = function () {
        var re = /^\d+$/;
        return String.prototype.___format.apply(this,
            Object._toArray(arguments)._unshift(function (k) {
                var i = 0, v = this[0] || {};
                if (re.test(k)) { i = parseInt(k); return v[i] || this.slice(1)[i]; }
                return Function._get(k, v);
            }));
    };

    /*************************************************** String ***************************************************/




    /*************************************************** Array ***************************************************/

    /*封装 Array.prototype.push */
    Array.prototype._push = function () { Array.prototype.push.apply(this, arguments); return this; };

    /*封装 Array.prototype.pop */
    Array.prototype._pop = function () { Array.prototype.pop.apply(this, arguments); return this; };

    /*封装 Array.prototype.shift */
    Array.prototype._shift = function () { Array.prototype.shift.apply(this, arguments); return this; };

    /*封装 Array.prototype.unshift */
    Array.prototype._unshift = function () { Array.prototype.unshift.apply(this, arguments); return this; };

    /*封装 Array.prototype.splice */
    Array.prototype._splice = function () { Array.prototype.splice.apply(this, arguments); return this; };

    /*取数组某个元素*/
    Array.prototype._item = function () { return this[arguments[0]]; };

    /*随机取数组中的元素*/
    Array.prototype._random = function () { return this[Number._randomInt(this.length)]; };

    /*第一个满足条件的元素*/
    Array.prototype._first = function () {
        if (Object._isFunction(arguments[0])) {
            for (var i = 0, len = this.length; i < len; i++) {
                if (!!Function.prototype.call.apply(arguments[0], [this[i], i, this[i]])) return this[i];
            } return null;
        }
        return Array.prototype._item.call(this, 0);
    };

    /*最后一个满足条件的元素*/
    Array.prototype._last = function () {
        if (Object._isFunction(arguments[0])) {
            for (var i = this.length - 1; i >= 0; i--) {
                if (!!Function.prototype.call.apply(arguments[0], [this[i], i, this[i]])) return this[i];
            } return null;
        }
        return Array.prototype._item.call(this, this.length - 1);
    };

    /*数组转换成 sys.Range*/
    Array.prototype._toRange = function () {
        var me = this;
        return new sys.Range(0, me.length, function (i) { return me[i]; });
    };

    /*为数组中的每一个元素依次执行回调函数*/
    Array.prototype._reduceAll = function () {
        return sys.Range.prototype.reduceAll.apply(Array.prototype._toRange.call(this), arguments);
    };

    /*为数组中的每一个元素依次执行回调函数*/
    Array.prototype._reduce = function () {
        return Array.prototype._reduceAll.apply(this, arguments)._last();
    };

    /*遍历数组*/
    Array.prototype._each = function () {
        if (Object._isFunction(arguments[0])) {
            for (var i = 0, len = this.length; i < len; i++) {
                Function.prototype.call.apply(arguments[0], [this[i], i, this[i]]);
            }
        } return this;
    };

    /*循环遍历树结构数据*/
    Array.prototype._treeEach = function () {
        var item = null,
            array = Array.apply(null, this),
            is = Object._isFunction(arguments[0]),
            func = is ? arguments[0] : arguments[1],
            key = is ? 'children' : Object._toStr(arguments[0] || 'children'),
            is_ = Object._isFunction(func);

        for (var i = 0, len = array.length; i < len; i++) {
            item = array[i];
            if (is_) { Function.prototype.call.apply(func, [item, i, item]); }
            if (!Object._isNullOrUndefined(item) && Object._isArray(item[key])) { Array.prototype.push.apply(array, item[key]); }
        }
        return array;
    };

    /*递归遍历树结构数据*/
    Array.prototype._treeEach_Recursion = function () {
        var j = 0,
            item = null,
            is = Object._isFunction(arguments[0]),
            func = is ? arguments[0] : arguments[1],
            key = is ? 'children' : Object._toStr(arguments[0] || 'children'),
            is_ = Object._isFunction(func);

        return (function (array_) {
            var array = [], me = arguments.callee;
            if (Object._isArray(array_)) {
                Array.prototype.push.apply(array, array_);
                for (var i = 0, len = array_.length; i < len; i++) {
                    j++; item = array_[i];
                    if (is_) { Function.prototype.call.apply(func, [item, j, item]); }
                    Array.prototype.push.apply(array, Function.prototype.call.apply(me, [this, (item || {})[key]]));
                }
            }
            return array;
        }).call(this, this);
    };

    /*将现有数组中的所有元素替换成新的元素*/
    Array.prototype._replaceAll = function () {
        if (Object._isFunction(arguments[0])) {
            for (var i = 0, len = this.length; i < len; i++) {
                this[i] = Function.prototype.call.apply(arguments[0], [this[i], i, this[i]]);
            }
        } return this;
    };

    /*将现有数组转换成新的数组*/
    Array.prototype._convertAll = function () {
        var me = this, func = arguments[0];
        if (!Object._isFunction(func)) return this;
        return Array._new(this.length, function (i) { return Function.prototype.call.apply(func, [me[i], i, me[i]]); });
    };

    /*将现有数组转换成新的数组*/
    Array.prototype._convertAll2 = function () {
        if (!Object._isFunction(arguments[0])) return this;
        return Array.prototype.concat.apply([], Array.prototype._convertAll.call(this, arguments[0]));
    };

    /*将现有数组转换成新的数组*/
    Array.prototype._convertAll3 = function () {
        if (!Object._isFunction(arguments[0])) return this;
        return Array.prototype.concat.apply([], Array._zip.apply(null, Array.prototype._convertAll.call(this, arguments[0])));
    };

    /*合并多个数组*/
    Array.prototype._zip = function () {
        var args_arr = Object._toArray(arguments)._unshift(this),
            args_array = args_arr._convertAll(function (i, v) { return Object._toArray(v); });
        return Array._new(args_array._max(function (i, v) { return v.length; }).length,
            function (i) { return Array.prototype._convertAll.call(args_array, function (j, arg) { return arg[i]; }); });
    };

    /*判断数组中是否有满足给定条件的元素*/
    Array.prototype._any = function () { return Array.prototype._index.apply(this, arguments) >= 0; };

    /*对Array.join方法的扩展，用于内部元素比较复杂的数组*/
    Array.prototype._join = function (tag, func) {
        if (Object._isFunction(tag)) { func = tag; tag = undefined; }
        if (!Object._isFunction(func)) return Array.prototype.join.call(this, tag);
        return Array.prototype._convertAll.call(this, function (i, v) { return Function.prototype.call.apply(func, [v, i, v]) || ''; }).join(tag);
    };

    /*将给定的一串字符串 Path 连接成一个完整的 URL*/
    Array.prototype._joinPaths = function () {
        var pathname = '',
            re = /[^\/]*\/\.\.\//g,
            url = Array.prototype._join.call(this, '/')._toUrl();
        if (!!url) {
            pathname = url.pathname.replace(/\\/g, '/').replace(/^\.\//g, '').replace(/\/\.\//g, '/').replace(/\/{2,}/g, '/');
            while (re.test(pathname)) { pathname = pathname.replace(re, function (m, i, v) { return ''; }); }
            return url.set('pathname', '/' + pathname._trimChar('/')).toString();
        }
        return this;
    };

    /*筛选数组*/
    Array.prototype._filter = function (func) {
        var array = [], count = parseInt(arguments[1]);
        if (!Object._isFunction(func)) { return this; }
        for (var i = 0, len = this.length; i < len; i++) {
            if (!isNaN(count) && array.length >= count) break;
            if (!!Function.prototype.call.apply(func, [this[i], i, this[i]])) { array.push(this[i]); }
        }
        return array;
    };

    /*在数组中找到某元素第一次出现的位置*/
    Array.prototype._index = function (v) {
        if (Object._isFunction(v)) {
            for (var i = 0, len = this.length; i < len; i++)
                if (!!Function.prototype.call.apply(v, [this[i], i, this[i]])) return i; return -1;
        } return arguments.callee.call(this, function (i, o) { return v == o; });
    };

    /*在数组中找到某元素最后一次出现的位置*/
    Array.prototype._lastIndex = function (v) {
        if (Object._isFunction(v)) {
            for (var i = this.length - 1; i >= 0; i--)
                if (!!Function.prototype.call.apply(v, [this[i], i, this[i]])) return i; return -1;
        } return arguments.callee.call(this, function (i, o) { return v == o; });
    };

    /*取数组中最小*/
    Array.prototype._min = function () {
        var min = Infinity,
            min_o = undefined, func = arguments[0];
        if (Object._isFunction(func)) {
            Array.prototype._each.call(this, function (i, o) {
                var v = Function.prototype.call.apply(func, [o, i, o]); if (v < min) { min = v; min_o = o; }
            }); return min_o;
        } return Math.min.apply(null, this);
    };

    /*取数组中最大*/
    Array.prototype._max = function () {
        var max = -Infinity,
            max_o = undefined, func = arguments[0];
        if (Object._isFunction(func)) {
            Array.prototype._each.call(this, function (i, o) {
                var v = Function.prototype.call.apply(func, [o, i, o]); if (v > max) { max = v; max_o = o; }
            }); return max_o;
        } return Math.max.apply(null, this);
    };

    /*去掉数组中重复的项*/
    Array.prototype._uniq = function () {
        var obj = {},
            func = arguments[0],
            array = [], is = Object._isFunction(func);
        Object._each(this, function (i, o) {
            var v = Function.prototype.call.apply(is ? func : function (i, o) { return o; }, [o, i, o]);
            if (!(v in obj)) { obj[v] = o; Array.prototype.push.call(array, o); }
        });
        return array;
    };

    /*数组求和*/
    Array.prototype._sum = function () {
        var func = arguments[0], is = Object._isFunction(func);
        return Array.prototype._reduce.call(this,
            function (pv, v, i) { return pv + Object._toNumber(is ? Function.prototype.call.apply(func, [v, i, v]) : v); }, 0);
    };

    /*数组求平均值*/
    Array.prototype._avg = function () { return Array.prototype._sum.apply(this, arguments) / this.length; };

    /*数组求数量*/
    Array.prototype._count = function () {
        var count = 0, func = arguments[0];
        if (!Object._isFunction(func)) return this.length;
        Array.prototype._each.call(this, function (i, v) { if (!!Function.prototype.call.apply(func, [v, i, v])) { count++; } });
        return count;
    };

    /*************************************************** Array ***************************************************/

    /*是否是 null 或 undefined*/
    Object._isNullOrUndefined = function () { return arguments[0] === null || arguments[0] === undefined; };

    /*转换成字符串*/
    Object._toStr = function (v) {
        if (Object._isNullOrUndefined(v)) return '';
        return Object._isFunction(v._toStr) ? Function.prototype.call.apply(v._toStr, arguments) : String(v);
    };

    /*取第几个元素*/
    Object._item = function (v, i) {
        if (!Object._isNullOrUndefined(v)) {
            return Object._isFunction(v._item) ? Function.prototype.call.apply(v._item, arguments) : v[i];
        }
    };

    /*转换成查询字符串*/
    Object._toQueryString = function (o) {
        if (Object._isPlainObject(o)) {
            return Object._toArray(o)._filter(function (i, kv) {
                return !!kv && !Object._isPlainObject(kv[1]) && !Object._isArray(kv[1]) && !Object._isFunction(kv[1]);
            })._join('&', function (i, kv) {
                return String._encodeURI(kv[0] || '') + '=' + String._encodeURI(kv[1] || '');
            });
        }
        return '';
    };

    /*判断一个变量是否能被转换成Number*/
    Object._can2Number = function () { return !isNaN(Number(arguments[0])); };

    /*转换为数字，第二个参数为转换失败的默认值（默认为 0）*/
    Object._toNumber = function (o) {
        return Object._can2Number(o) ? Number(o) : (Object._isNumber(arguments[1]) ? arguments[1] : 0);
    };

    /*转换成整数*/
    Object._toInt = function () { return parseInt(Object._toNumber.apply(null, arguments)); };

    /*获取类型*/
    Object._type = function () { return Object.prototype.toString.call(arguments[0]).split(/[\[ \]]/)[2]; };

    /*判断是否是正则表达式*/
    Object._isRegexp = function () { return Object.prototype.toString.call(arguments[0]) === '[object RegExp]'; };

    /*判断是否是日期*/
    Object._isDate = function () { return Object.prototype.toString.call(arguments[0]) === '[object Date]'; };

    /*判断是否是字符串*/
    Object._isString = function () { return Object.prototype.toString.call(arguments[0]) === '[object String]'; };

    /*判断是否是数组*/
    Object._isArray = function () { return Object.prototype.toString.call(arguments[0]) === '[object Array]'; };

    /*测试对象是否是纯粹的对象（通过 "{}" 或者 "new Object" 创建的）*/
    Object._isPlainObject = function () { return Object.prototype.toString.call(arguments[0]) === '[object Object]'; };

    /*判断是否是函数*/
    Object._isFunction = function () { return Object.prototype.toString.call(arguments[0]) === '[object Function]'; };

    /*判断是否是数字*/
    Object._isNumber = function () { return Object.prototype.toString.call(arguments[0]) === '[object Number]'; };

    /*判断是否是整形数字*/
    Object._isInt = function () { return Object._isNumber(arguments[0]) && arguments[0] % 1 === 0; };

    /*遍历对象*/
    Object._each = function () { return Object._toArray.apply(null, arguments); };

    /*浅拷贝*/
    Object._copy = function () {
        var dest = arguments[arguments.length - 1] || {};
        Object._toArray(arguments)._pop()._each(
            function (i, o) { if (!!o) Object._each(o, function (k, v) { dest[k] = v; }); });
        return dest;
    };

    /*设置对象的属性*/
    Object._setProperty = function () {
        var args_arr = Object._toArray(arguments);
        return Object._copy.apply(null, args_arr._push(args_arr[0])._shift());
    };

    /*删除对象的属性*/
    Object._delProperty = function () {
        var args_arr = Object._toArray(arguments), dest = args_arr[0] || {};
        args_arr._each(function (i, v) { delete dest[v]; });
        return dest;
    };

    /*深拷贝*/
    Object._deepCopy = function () { return String._jsonParse(String._jsonStringify(arguments[0])); };

    /*转换成数组*/
    Object._toArray = function () {
        var array = [],
            args_one = arguments[0],
            func = arguments[1], is = Object._isFunction(func);
        if (!Object._isNullOrUndefined(args_one)) {
            if (Object._isArray(args_one)) { if (is) Array.prototype._each.call(args_one, func); return args_one; }
            if (Object._isInt(args_one.length) && !Object._isFunction(args_one)) {
                array = Array.prototype.slice.call(args_one); if (is) Array.prototype._each.call(array, func); return array;
            }
            for (var k in args_one) {
                if (args_one.hasOwnProperty(k)) {
                    if (is) Function.prototype.call.apply(func, [args_one[k], k, args_one[k]]);
                    Array.prototype.push.call(array, [k, args_one[k]]);
                }
            }
        }
        return array;
    };

    /*字符串格式化（静态方式调用）*/
    String.___format = String.prototype.___format._uncurry('');

    /*字符串格式化（静态方式调用）*/
    String._format = String.prototype._format._uncurry('');

    /*字符串格式化（静态方式调用）*/
    String._format_map = String.prototype._format_map._uncurry('');

    /*字符串模版编译（静态方式调用）*/
    String.___templateCompile = String.prototype.___templateCompile._uncurry('');

    /*字符串模版编译（静态方式调用）*/
    String._templateCompile = String.prototype._templateCompile._uncurry('');

    /*将 JSON 字符串转换成对象（静态方式调用）*/
    String._jsonParse = String.prototype._jsonParse._uncurry('');

    /*URL编码字符串（静态方式调用）*/
    String._encodeURI = String.prototype._encodeURI._uncurry('');

    /*URL解码字符串（静态方式调用）*/
    String._decodeURI = String.prototype._decodeURI._uncurry('');

    /*查询字符串转换成 Object（静态方式调用）*/
    String._toQuery = String.prototype._toQuery._uncurry('');

    /*将 object 转换成 JSON 字符串*/
    String._jsonStringify = function () {
        var args = arguments, value = args[0];
        if (typeof JSON === 'undefined') {
            switch (typeof value) {
                case 'boolean':
                case 'undefined':
                    return String(value);
                case 'number':
                    return isFinite(value) ? String(value) : 'null';
                case 'string':
                    return String._format('"{0}"', String.prototype._repl.call(value));
                case 'object':
                    if (!value) return 'null';
                    if (Object._isArray(value)) {
                        return String._format('[{0}]', Array.prototype._join.apply(value, [',', function (i, v) { return args.callee(v); } ]));
                    } else if (Object._isPlainObject(value)) {
                        return String._format('{{{0}}}', Array.prototype._join.apply(
                            Object._toArray(value), [',', function (i, v) { return String._format('"{0}":{1}', String.prototype._repl.call(v[0]), args.callee(v[1])); } ]));
                    }
                    return String._format('"{0}"', String.prototype._repl.call(value.toString()));
            }
            return '';
        }
        return JSON.stringify(value);
    };

    /*随机字符串*/
    String._randomStr = function () {
        var func = arguments[2],
            is = Object._isFunction(func),
            len = Object._toNumber(arguments[0]),
            default_from = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_',
            str = Object._toStr(arguments[1]) || default_from, L = str.length;
        return Array._new(len, function (i) {
            var ch = str.charAt(parseInt(Math.random() * L));
            return is ? (Function.prototype.call.apply(func, [ch, i, ch]) || '') : ch;
        }).join('');
    };

    /*GUID*/
    String._guid = function () {
        return String._randomStr(36, '0123456789ABCDEF',
            function (i, c) { return i == 8 || i == 13 || i == 18 || i == 23 ? '-' : c; });
    };

    /*随机数字字符串*/
    String._randomNumStr = function () { return String._randomStr(arguments[0], '0123456789', arguments[1]); };

    /*随机颜色字符串*/
    String._randomHtmlColor = function () { return sys.Color.random().toHtml(); };

    /*随机颜色 RGB*/
    String._randomRGB = function () { return sys.Color.random().toRGB(); };

    /*随机颜色 RGBA*/
    String._randomRGBA = function () { return sys.Color.random().toRGBA(); };

    /*Array.isArray*/
    Array.isArray = Array.isArray || Object._isArray;

    /*合并多个数组（静态方式调用）*/
    Array._zip = Array.prototype._zip._uncurry([]);

    /*多个数组合并成一个新数组（静态方式调用）*/
    Array._merge = Array._concat = Array.prototype.concat._this([]);

    /*将给定的一串字符串 Path 连接成一个完整的 URL（静态方式调用）*/
    Array._joinPaths = Array.prototype._joinPaths._uncurry([]);

    /*创建新的数组*/
    Array._new = function () {
        if (arguments.length === 2) {
            if (Object._isInt(arguments[0]) &&
                Object._isFunction(arguments[1])) return sys.Range.apply(null, arguments).toArray();
        } return Array.apply(null, arguments);
    };

    /*防止异常*/
    Function._tryCall = function () {
        if (Object._isFunction(arguments[0])) {
            try { return Function.prototype.call.apply(arguments[0], Object._toArray(arguments)._shift()); }
            catch (e) { }
        }
    };

    /*防止异常*/
    Function._try = function () {
        return Function._tryCall.apply(null, Object._toArray(arguments)._splice(1, 0, null));
    };

    /*如果第一个参数为 true, 则返回第二个参数（如果是 function 则返回该 function 执行的结果）*/
    Function._if_true = function () {
        if (!!arguments[0]) {
            return !Object._isFunction(arguments[1]) ? arguments[1] :
                Function.prototype.call.apply(arguments[1], Object._toArray(arguments)._splice(0, 2)._unshift(null));
        }
    };

    /*如果第一个参数为 false, 则返回第二个参数（如果是 function 则返回该 function 执行的结果）*/
    Function._if_false = function () {
        return Function._if_true.apply(null, Object._toArray(arguments)._splice(0, 1, !arguments[0]));
    };

    /*计算某个方法执行的时间（毫秒）*/
    Function._exe_time = function () {
        var args = arguments,
            is_0 = Object._isFunction(args[0]),
            is_1 = Object._isFunction(args[1]), d = new Date();
        if (is_0) {
            if (!is_1) return {
                argv: Function.prototype.call.apply(args[0], [{}]), time: (new Date()).getTime() - d.getTime()
            };
            Function.___exeAsync([args[0],
                function () {
                    Function.prototype.apply.apply(args[1], [
                        { time: this.times[0], argv: this.argvs[0] }, Object._toArray(arguments)]);
                }
            ]);
        }
        return {};
    };

    /*按顺序执行一组异步的方法*/
    Function.___exeAsync = function () {
        var i = 0,
            args_arr = Object._toArray(arguments);

        if (Object._isArray(args_arr[0]) && args_arr[0].length > 0) {
            var fun_arr = Array.prototype._filter.call(args_arr[0], function (i, v) {
                return Object._isFunction(v);
            }), len = fun_arr.length;

            if (len > 0) {
                (function () {
                    var me = arguments.callee,
                        times = this.times || [],
                        argvs = this.argvs || [], d = new Date();
                    Function.prototype.apply.apply(fun_arr[i++],
                        [{ index: i, callback: _callback, times: times, argvs: argvs }, arguments]);
                    function _callback() {
                        if (i < len) {
                            Function.prototype.apply.apply(me, [{
                                times: times._push((new Date()).getTime() - d.getTime()),
                                argvs: argvs._push(Object._toArray(arguments))
                            }, arguments]);
                        }
                    }
                }).apply({}, args_arr._shift());
            }
        }
    };

    /*按顺序执行一组异步的方法*/
    Function._exeAsync = function () {
        var args_arr = Object._toArray(arguments),
            index = args_arr._lastIndex(function (i, v) { return Object._isFunction(v); }) + 1;
        Function.___exeAsync.apply(null, Array.prototype._push.apply([args_arr.slice(0, index)], args_arr.slice(index)));
    };

    /*将 function 中的 this 指向一个固定的对象（静态方式调用）*/
    Function._this = Function.prototype._this._uncurry();

    /*获取对象*/
    Function._get = function () {
        var name = Object._toStr(arguments[0])._trim(),
            ns = arguments[1] || global, array = name.split(/\s*\.\s*/), len = array.length;
        for (var v, i = 0; i < len; i++) {
            v = array[i]; if (Object._isNullOrUndefined(ns[v])) return ns[v]; ns = ns[v];
        }
        return ns;
    };

    /*设置对象*/
    Function._set = function () {
        var name = Object._toStr(arguments[0])._trim(),
            ns = global, array = name.split(/\s*\.\s*/), len = array.length;
        for (var v, i = 0; i < len; i++) {
            v = array[i]; ns[v] = i === len - 1 ? arguments[1] : (ns[v] || {}); ns = ns[v];
        }
        return ns;
    };

    /*创建一个类*/
    Function._class = function () {
        var is_super,
            is_ctor, ctor, args = arguments,
            name = args[0], superClass = args[1] || Object,
            prototype = args[2] || {}, staticPrototype = args[3] || {};

        if (Object._isPlainObject(name)) {
            staticPrototype = superClass;
            prototype = name; superClass = Object; name = '';
        } else if (Object._isFunction(name)) {
            staticPrototype = prototype;
            prototype = superClass; superClass = name; name = '';
        } else if (!Object._isFunction(superClass)) {
            staticPrototype = prototype; prototype = superClass; superClass = Object;
        }

        F__.prototype = {};
        F__.__CLASSNAME__ = name; ctor = prototype.constructor;
        is_ctor = Object._isFunction(ctor); is_super = Object._isFunction(superClass);

        Object._each(staticPrototype, function (k, v) {
            F__[k] = Object._isFunction(v) ? Function._this(v, F__) : v;
        });

        if (!!is_super) {
            F__.__SUPERCLASS__ = superClass;
            superClass.__SUBCLASSES__ = (superClass.__SUBCLASSES__ || [])._push(F__);
            F__.prototype = new F__.__SUPERCLASS__();
        }

        Object._each(prototype, function (k, v) {
            F__.prototype[k] = !Object._isFunction(v) ? v : function () {
                if (this instanceof F__) { return Function.prototype.apply.apply(v, [this, arguments]); }
            };
        });

        return F__.prototype.constructor = Function._set(name, F__);

        function F__() {
            if (!(this instanceof F__)) return F__.apply(new F__(), arguments);
            if (!!is_super) { Function.prototype.apply.apply(superClass, [this, arguments]); }
            if (!!is_ctor) { Function.prototype.apply.apply(ctor, [this, arguments]); }
            return this;
        }
    };

    /*随机数*/
    Number._randomInt = function () {
        var can = Object._can2Number(arguments[1]),
            min = can ? Object._toInt(arguments[0]) : 0,
            max = can ? Object._toInt(arguments[1]) : Object._toInt(arguments[0]);
        return max === 0 && min === 0 ? 0 : parseInt(Math.random() * (max - min) + min);
    };

    /*返回当前日期*/
    Date._now = function () { return new Date(); };

    /*字符串转日期*/
    Date._parse = function () {
        var n = NaN, array = null,
            str = Object._toStr(arguments[0]),
            re = /^[^\d\n]*\d{4}([^\d\n]+\d{1,2}){2}(([^\d\n]+\d{1,2}){2}|([^\d\n]+\d{1,2}){3}([^\d\n]+\d{1,3})?)?[^\d\n]*$/;
        if (re.test(str)) {
            array = str.replace(/^[^\d\n]+|[^\d\n]+$/g, '').split(/[^\d\n]+/);
            str = String._format('{0} {1}', array.slice(0, 3).join('/'), array.slice(3).join(':'));
        }
        return isNaN(n = Date.parse(str)) ? null : new Date(n);
    };

    /*用于组装正则表达式时替换其中的特殊字符*/
    RegExp._repl = function () {
        return String.prototype.replace.call(
            Object._toStr(arguments[0]),
            /[\\\/\|\^\$\*\+\-\.\?\<\>\(\)\[\]\{\}]/g, function (m) { return '\\' + m; });
    };

    /*用于替换正则表达式中特殊字符*/
    RegExp._or_Repl = function () {
        return Object._toArray(arguments)._join('|', function (i, v) { return RegExp._repl(Object._toStr(v)); });
    };

    /*************************************************** 类 ***************************************************/

    /* Eval */
    Function._set('Eval', function () { return eval(arguments[0]); });

    /* sys.Range */
    Function._class('sys.Range', {
        constructor: function () {
            this.from = arguments[0]; this.to = arguments[1];
            this.step = arguments[2]; this.ctor = arguments[3];
            if (arguments.length === 1) {
                this.step = 1; this.to = this.from; this.from = 0;
            } else if (arguments.length === 2) {
                if (Object._isFunction(this.to)) { this.ctor = this.to; this.to = this.from; this.from = 0; }
            } else if (arguments.length === 3) {
                if (Object._isFunction(this.step)) { this.ctor = this.step; this.step = 1; }
            }
            this.from = Object._toInt(this.from); this.to = Object._toInt(this.to);
            this.step = Math.abs(Object._toInt(this.step, 1)); this.step = Math.max(1, this.step);
            this.ctor = Object._isFunction(this.ctor) ? this.ctor : undefined;
        },
        each: function () {
            var j = 0, i = this.from,
                func = arguments[0], is = Object._isFunction(this.ctor);
            if (!Object._isFunction(func)) return;
            if (this.from < this.to) { for (; i < this.to; i += this.step, j++) _each.apply(this, [i, j]); }
            else { for (; i > this.to; i -= this.step, j++) _each.apply(this, [i, j]); }
            function _each(i, j) {
                Function.prototype.call.apply(
                    func, [this, j, is ? Function.prototype.call.apply(this.ctor, [this, i]) : i]);
            }
            return this;
        },
        reduceAll: function () {
            var array = [],
                func = arguments[0],
                value = arguments[1], is = arguments.length > 1;
            if (Object._isFunction(func)) {
                this.each(function (i, v) {
                    value = is ? Function.prototype.call.apply(func, [null, value, v, i, array]) : v;
                    Array.prototype.push.call(array, value);
                    is = true;
                });
            }
            return array;
        },
        reduce: function () {
            return this.reduceAll.apply(this, arguments)._last();
        },
        toArray: function () {
            var array = []; this.each(function (i, v) { array.push(v); }); return array;
        },
        toString: function () {
            return Array.prototype._join.apply(this.toArray(), arguments);
        }
    });

    /* sys.Date */
    Function._class('sys.Date', {
        constructor: function () {
            var arg_date = arguments[0],
                is = Object._isDate(arg_date),
                now = is ? arg_date : (Date._parse(arg_date) || new Date());
            this.yyyy = now.getFullYear();
            this.MM = now.getMonth() + 1;
            this.dd = now.getDate();
            this.HH = now.getHours();
            this.hh = this.HH > 12 ? this.HH - 12 : this.HH;
            this.mm = now.getMinutes();
            this.ss = now.getSeconds();
            this.MS = now.getMilliseconds();
            this.tick = now.getTime();
            this.day = now.getDay();
        },
        regex_ms: /MS/g,
        regex_year: /y{1,4}/g,
        regex: /M{1,2}|d{1,2}|H{1,2}|h{1,2}|m{1,2}|s{1,2}/g,
        toString: function () {
            var date = this,
                format = Object._toStr(arguments[0]) || 'yyyy-MM-dd HH:mm:ss';
            format = format.replace(
                this.regex_ms, Number.prototype._lpad.call(date.MS, 3));
            format = format.replace(this.regex_year,
                function (m) { return m.length > 2 ? date.yyyy : date.yyyy.toString().substring(2); });
            return format.replace(this.regex,
                function (m) { return m.length === 1 ? date[m + m] : Number.prototype._lpad.call(date[m], 2); });
        }
    });

    /* sys.Url */
    Function._class('sys.Url', {
        regex: /^((\w+?:)\/\/(([^\/\?#:]+)(:(\d+))?))?([^\?#]*)(\?[^#]*)?(#(.*))?/i,
        constructor: function () {
            this.href = Object._toStr(arguments[0]).replace(/\\/g, '/');
            var array = this.regex.exec(this.href);
            this.protocol_host = !!array ? array[1] || '' : '';
            this.protocol = !!array ? array[2] || '' : '';
            this.host = !!array ? array[3] || '' : '';
            this.hostname = !!array ? array[4] || '' : '';
            this.port = !!array ? array[6] || '' : '';
            this.pathname = !!array ? array[7] || '' : '';
            this.search = !!array ? array[8] || '' : '';
            this.hash = !!array ? array[9] || '' : '';
        },
        set: function () {
            this[arguments[0]] = arguments[1]; return this;
        },
        toString: function () {
            return String._format_map('{protocol_host}{pathname}{search}{hash}', this);
        }
    });

    /* sys.Color */
    Function._class('sys.Color', {
        constructor: function () {
            this.R = Math.min(255, Math.abs(Object._toInt(arguments[0])));
            this.G = Math.min(255, Math.abs(Object._toInt(arguments[1])));
            this.B = Math.min(255, Math.abs(Object._toInt(arguments[2])));
            this.A = Math.min(255, Math.abs(Object._toInt(arguments[3])));
        },
        toRGB: function () {
            return String._format_map('RGB({R}, {G}, {B})', this);
        },
        toRGBA: function () {
            return String._format_map('RGBA({R}, {G}, {B}, {A})', this);
        },
        toHtml: function () {
            return String._format('#{0}{1}{2}',
                this.R._hex(2), this.G._hex(2), this.B._hex(2)).toUpperCase();
        },
        toString: function () { return this.toHtml(); }
    }, {
        regex_html: /^\s*#([0-9A-F])([0-9A-F])([0-9A-F])\s*$/i,
        regex_html2: /^\s*#([0-9A-F]{2})([0-9A-F]{2})([0-9A-F]{2})\s*$/i,
        regex_rgb: /^\s*RGB\s*\(\s*(\d{1,3})\s*,\s*(\d{1,3})\s*,\s*(\d{1,3})\s*\)\s*$/i,
        regex_rgba: /^\s*RGBA\s*\(\s*(\d{1,3})\s*,\s*(\d{1,3})\s*,\s*(\d{1,3})\s*\,\s*(\d{1,3})\s*\)\s*$/i,
        parse: function () {
            var str = Object._toStr(arguments[0]),
                array = this.regex_html.exec(str) || this.regex_html2.exec(str),
                array2 = !!array ? array : this.regex_rgb.exec(str) || this.regex_rgba.exec(str);
            return !array2 ? null : this.apply(null,
                !!array ? array2._shift()._convertAll(function (i, v) { return parseInt(v, 16); }) : array2._shift());
        },
        random: function () {
            return this.apply(null,
                Array._new(4, function () { return Number._randomInt(0, 256); }));
        }
    });

    /*************************************************** 类 ***************************************************/

})();


/*************************************************** 浏览器环境专用 ***************************************************/

(function () {
    var global = this;
    if (typeof document === 'undefined' ||
        typeof document.getElementById === 'undefined') return;

    /* sys.Ajax */
    Function._set('sys.Ajax', {
        getXHR: function () {
            if (typeof XMLHttpRequest != 'undefined') return new XMLHttpRequest();
            if (typeof ActiveXObject != 'undefined') {
                var array = ['Msxml2.XMLHTTP.6.0', 'Msxml2.XMLHTTP.3.0', 'Msxml2.XMLHTTP', 'Microsoft.XMLHTTP'];
                for (var i = 0, len = array.length; i < len; i++) { try { return new ActiveXObject(array[i]); } catch (e) { } }
            }
        },
        request: function () {
            var xhr = this.getXHR(),
                opts = Object._setProperty({
                    url: '?', type: 'GET',
                    data: null, async: true, dataType: 'text', headers: {},
                    contentType: 'application/x-www-form-urlencoded', success: null, error: null
                }, arguments[0]),
                type = Object._toStr(opts.type).toLowerCase(),
                url = type === 'get' ? String.prototype._setUrlQuery.call(opts.url, opts.data) : url;
            if (!!xhr) {
                xhr.onreadystatechange = function () {
                    if (xhr.readyState === 4) {
                        if (xhr.status != 200) {
                            return Object._isFunction(opts.error) ?
                                Function.prototype.call.apply(opts.error, [xhr, xhr, xhr.responseText]) : undefined;
                        }
                        if (Object._isFunction(opts.success)) {
                            switch ((opts.dataType || 'text').toLowerCase()) {
                                case 'script': return eval(xhr.responseText);
                                case 'xml': return Function.prototype.call.apply(opts.success, [xhr, xhr.responseXML, xhr.statusText, xhr]);
                                case 'text': return Function.prototype.call.apply(opts.success, [xhr, xhr.responseText, xhr.statusText, xhr]);
                                case 'json': return Function.prototype.call.apply(opts.success, [xhr, String._jsonParse(xhr.responseText), xhr.statusText, xhr]);
                            }
                        }
                    }
                };
                xhr.open(opts.type, url, opts.async);
                Object._each(opts.headers, function (k, v) { xhr.setRequestHeader(k, v); });
                xhr.setRequestHeader('Content-Type', opts.contentType || 'application/x-www-form-urlencoded');
                xhr.send(type === 'get' ? undefined : Object._toQueryString(opts.data));
            }
        }
    });

    /* sys.Jsonp */
    Function._set('sys.Jsonp', {
        jsonp_Callbacks: {},
        request: function () {
            var me = this,
                url = arguments[0],
                is = Object._isFunction(arguments[1]),
                data = is ? null : arguments[1], success = is ? arguments[1] : arguments[2],
                script = document.createElement('script'), name = 'jsonp_Callback_' + String._randomNumStr(15),
                new_url = String.prototype._setUrlQuery.call(url, Object._copy({ jsonp_callback: 'sys.Jsonp.jsonp_Callbacks.' + name }, data));
            if (Object._isFunction(success)) {
                this.jsonp_Callbacks[name] = function () { delete me.jsonp_Callbacks[name]; document.body.removeChild(script); return Function.prototype.apply.apply(success, [null, arguments]); };
                Object._each({ src: new_url, defer: 'defer', type: 'text/javascript' }, function (k, v) { script.setAttribute(k, v); });
                document.body.appendChild(script);
            }
        }
    });

})();

/*************************************************** 浏览器环境专用 ***************************************************/