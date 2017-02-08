$(document).ready(function() {
  // $.getJSON('/f/web/banner/index/top/', function(data){
  //   if(isSuccess(data)){
  //     var item_list = data.banners;
  //     var $top_banner = $('#top_banner');
  //     var $carousel = $top_banner.flickity({
  //                       wrapAround: true,
  //                       prevNextButtons: false,
  //                       imagesLoaded: true,
  //                       autoPlay: 3000
  //                     });

  //     if (isItemList(item_list)){
  //       $.each(item_list, function(index, item){
  //         var $cellElems = $(buildTopBanner(item));
  //         console.log($cellElems);
  //         $carousel.flickity('append', $cellElems);
  //       });
  //     };
  //   }
  // });

  $('#top_banner').flickity({
    wrapAround: true,
    prevNextButtons: false,
    imagesLoaded: true,
    autoPlay: 3000
  });
});

// function buildTopBanner(item){
//   var item_url = item.imageUrl;
//   var item_href;
//   var img_src = coverShow(item_url);

//   if(!!item_href){
//     item_href = item.refUrl;
//   } else {
//     item_href = 'javascript:;';
//   }

//   var banner_item = '<div class="carousel-cell">'
//              + '<a href='+item_href+'>'
//              + '<img src='+img_src+' />'
//              + '<div class="cover"></div>'
//              + '</a></div>';
//   return banner_item;
// }
