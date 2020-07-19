var nanShopSwiper = new Swiper('.swiper-container', {
    //设置自动播放
    autoplay: {
        delay: 2000,
        disableOnInteraction: false
    },
    //设置无限循环播放
    loop: true,
    //设置圆点指示器
    pagination: {
        el: '.swiper-pagination',
    },
    //设置上下页按钮
    navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev',
    }
})

$('.all-sort-list > .item').hover(function () {
    var eq = $('.all-sort-list > .item').index(this),				//获取当前滑过是第几个元素
        h = $('.all-sort-list').offset().top,						//获取当前下拉菜单距离窗口多少像素
        s = $(window).scrollTop(),									//获取游览器滚动了多少高度
        i = $(this).offset().top,									//当前元素滑过距离窗口多少像素
        item = $(this).children('.item-list').height(),				//下拉菜单子类内容容器的高度
        sort = $('.all-sort-list').height();						//父类分类列表容器的高度

    if (item < sort) {												//如果子类的高度小于父类的高度
        if (eq == 0) {
            $(this).children('.item-list').css('top', (i - h));
        } else {
            $(this).children('.item-list').css('top', (i - h) + 1);
        }
    } else {
        if (s > h) {												//判断子类的显示位置，如果滚动的高度大于所有分类列表容器的高度
            if (i - s > 0) {											//则 继续判断当前滑过容器的位置 是否有一半超出窗口一半在窗口内显示的Bug,
                $(this).children('.item-list').css('top', (s - h) + 2);
            } else {
                $(this).children('.item-list').css('top', (s - h) - (-(i - s)) + 2);
            }
        } else {
            $(this).children('.item-list').css('top', 3);
        }
    }

    $(this).addClass('hover');
    $(this).children('.item-list').css('display', 'block');
}, function () {
    $(this).removeClass('hover');
    $(this).children('.item-list').css('display', 'none');
});
(function($, window, undefined) {

    var ui = {
        $search_text: $('#search-text'),
        $search_keywords: $('#search-keywords'),
        $ui_pager: $('#ui-pager'),
        $btn_prev: $('#btn-prev'),
        $btn_next: $('#btn-next'),
        $banner_item: $('#banner-wrap li'),
        $tab_list: $('#tab-list')
    };

    //轮播组件
    var swiper_plugin = {
        curIndex: 0,
        nlength: ui.$banner_item.length,
        time: 3000,
        init: function() {
            this.view();
            this.listen();
        },
        view: function() {
            var self = this;
        },
        listen: function() {
            var self = this;
            ui.$btn_prev.on('click', function() {
                self.fPrev();
            });
            ui.$btn_next.on('click', function() {
                self.fNext();
            });
            ui.$ui_pager.on('click', 'li', function() {
                self.curIndex = $(this).index();
                self.fToggleActiveByIndex(self.curIndex);
            });
        },
        fPrev: function() { //轮播向前按钮
            var self = this;
            if (0 == self.curIndex) {
                self.curIndex = self.nlength - 1;
            } else {
                self.curIndex -= 1;
            }
            self.fToggleActiveByIndex(self.curIndex);
        },
        fNext: function() { //轮播向后按钮
            var self = this;
            if (self.nlength - 1 == self.curIndex) {
                self.curIndex = 0;
            } else {
                self.curIndex += 1;
            }
            self.fToggleActiveByIndex(self.curIndex);
        },
        fToggleActiveByIndex: function(index) {
            var self = this;
            ui.$banner_item.fadeOut().removeClass('slide-active').eq(index).fadeIn().addClass('slide-active');
            ui.$ui_pager.find('li').removeClass('active').eq(index).addClass('active');
        }
    }
    //初始化轮播插件
    swiper_plugin.init();

    ui.$search_text.focus(function() {
        $(this).closest('form').toggleClass('search-form-active');
        ui.$search_keywords.fadeOut();
    }).blur(function() {
        $(this).closest('form').toggleClass('search-form-active');
        ui.$search_keywords.fadeIn();
    });

})(jQuery, window);