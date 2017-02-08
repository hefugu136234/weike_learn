$(function(){
	addActiveClass('zlxy');

  var category_plate = $('.category-plate .list');
  if (isItemList(category_plate)) {
    category_plate.each(function(index, item) {
      var $item = $(item);
      var icons = $item.find('.icon');

      icons.each(function(i, icon){
        var $icon = $(icon);
        var name = $icon.data('name');
        $icon.attr('class', menuImgType(name));
      });
    });
  }
});

// 筛选板块图标
function menuImgType(name) {
  var resultType = 'icon';
  if (!name) {
    return resultType;
  }
  var typeClass = '';
  switch (name) {
  case '外科':
    typeClass = 'category-wk';
    break;
  case '内科':
    typeClass = 'category-nk';
    break;
  case '重症医学':
    typeClass = 'category-zz';
    break;
  case '皮肤':
    typeClass = 'category-pf';
    break;
  case '消化内科':
    typeClass = 'category-xh';
    break;
  case '麻醉':
    typeClass = 'category-mz';
    break;
  case '儿科':
    typeClass = 'category-ek';
    break;
  }
  if (!!typeClass) {
    resultType = resultType + ' ' + typeClass;
  }
  return resultType;
}
