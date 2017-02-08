function jifenTypeClass(action){
	var result_Class='icon';
	if(!action){
		return result_Class;
	}
	action=parseInt(action);
	var typeClass='';
	switch(action){
	//积分兑换
	case -1:
		typeClass='icon-scores';//样式
		break;
	//注册成功
	case 1:
		typeClass='icon-reg';//样式
		break;
	//实名认证成功
	case 2:
		typeClass='icon-crown';//样式
		break;
		//通过分享有礼，邀请好友并注册成功
	case 3:
		typeClass='icon-is-users';//样式
		break;
		//三分屏资源拥有者
	case 4:
		typeClass='icon-three';//样式
		break;
		//视频拥有者
	case 5:
		typeClass='icon-player-o';//样式
		break;
		//PDF拥有者
	case 6:
		typeClass='icon-pdf';//样式
		break;
		//资源被播放，资源拥有者加分
	case 7:
		typeClass='icon-play';//样式
		break;
		//资源被点赞，资源拥有者加分
	case 8:
		typeClass='icon-is-zan';//样式
		break;
		//资源被分享，资源拥有者加分
	case 9:
		typeClass='icon-share';//样式
		break;
		//用户播放资源
	case 10:
		typeClass='icon-video';//样式
		break;
		//用户参与投票
	case 11:
		typeClass='icon-statistics';//样式
		break;
		//用户点赞
	case 12:
		typeClass='icon-like';//样式
		break;
		//用户分享资源被点击
	case 13:
		typeClass='icon-is-share';//样式
		break;
		// 用户评论
	case 15:
		typeClass='icon-message';//样式
		break;
	}
	if(!!typeClass){
		result_Class=result_Class+' '+typeClass;
	}
	return result_Class;
}
