$(function(){
    $(".menuList li").bind("click",function(){
        $(this).addClass("active").siblings().removeClass("active");
        var url=$(this).find("a").attr("src");
        $("#mainFrame").attr("src",url);
    })
});