(function () {
    var g = document.documentElement;
    var the = document.currentScript;
    var coMoMan = !!the && the.getAttribute('data-mo-man') === 'co';

    g.className += ' co-js';

    setTimeout(function () {
        if (g.className.indexOf('hien-san-sang') < 0) {
            g.className += ' hien-het';
        }
    }, 5000);

    var bo = false;
    try {
        bo = window.matchMedia &&
             window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    } catch (e) { }
    if (bo) return;

    if (coMoMan) {
        g.className += ' dang-mo-man khoa-cuon';
        return;
    }

    g.className += ' dang-luoi';

    setTimeout(function () {
        g.className = g.className.replace(/ *\bdang-luoi\b/g, '');
    }, 2200);
})();
