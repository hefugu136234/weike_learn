<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="x-ua-compatible" content="IE=edge,chrome=1">
    <meta http-equiv="X-UA-Compatible" content="IE=9">
    <meta name="renderer" content="webkit">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>知了微课 知我所学，了我所需</title>
    <link rel="icon" href="/assets/favicon.ico" type="image/x-ico">
    <link rel="stylesheet" media="all" href="/assets/css/app/font.css" />
    <link rel="stylesheet" href="http://cdn.bootcss.com/Swiper/3.1.7/css/swiper.min.css">
    <link rel="stylesheet" media="all" href="/assets/css/web/subscribe.css" />

    <!--[if lt IE 9]>
    <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
    <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

<body>
	<div class="clearfix" id="top">
		<h1 class="bfL" id="logo">
			<img alt="" src="/assets/img/web/subscribe/logo.png" />
		</h1>
		<ul class="nav bfL" id="nav">
			<li class="active"><a href="#index">首页</a></li>
			<!-- <li><a href="#activity">知了活动</a></li> -->
			<li><a href="#product">产品介绍</a></li>
			<!-- <li><a class="nav_subBtn external" href="javascript:;">预约申请</a></li> -->
			<li><a href="javascript:;" class="external">最新消息</a></li>
			<li><a class="aboutBtn external" href="javascript:;">关于我们</a></li>
		</ul>
	</div>
	<div class="content indexBanner" id="index">
		<div class="tag1">
			<img alt="" src="/assets/img/web/subscribe/banner_info1.png" />
		</div>
		<div class="tag2">
			<img alt="" src="/assets/img/web/subscribe/banner_info2.png" />
		</div>
		<!-- <div class="subscribeBtn">公测申请</div> -->
		<div id="tvBox">
			<div id="id_video_container"></div>
		</div>
	</div>
	<!-- <div class="content indexActivity" id="activity">
		<div class="container">
			<div class="tt">
				<img alt="" src="/assets/img/web/subscribe/index_act_tt.png" />
			</div>
			<div id="actShow" class="swiper-container activity_list">
        <ul class="swiper-wrapper list">
          <li class="swiper-slide" data-target="activity1"><img src="/assets/img/web/subscribe/_temp/i_act_img1.jpg"></li>
          <li class="swiper-slide" data-target="activity2"><img src="/assets/img/web/subscribe/_temp/i_act_img2.jpg"></li>
          <li class="swiper-slide" data-target="activity3"><img src="/assets/img/web/subscribe/_temp/i_act_img3.jpg"></li>
          <li class="swiper-slide" data-target="activity4"><img src="/assets/img/web/subscribe/_temp/i_act_img4.jpg"></li>
          <li class="swiper-slide" data-target="activity5"><img src="/assets/img/web/subscribe/_temp/i_act_img5.jpg"></li>
        </ul>
	    </div>
		</div>
	</div> -->
	<div class="content indexProduct" id="product">
		<div class="container">
			<div class="tt">
				<img alt="" src="/assets/img/web/subscribe/index_product_img1.png" />
			</div>
		</div>
	</div>
	<div class="content indexProduct2">
		<div class="container">
			<div class="tt">
				<img alt="" src="/assets/img/web/subscribe/index_product_img2.png" />
			</div>
		</div>
	</div>
	<div class="content indexProduct3">
		<div class="container">
			<div class="tt">
				<img alt="" src="/assets/img/web/subscribe/index_product_img3.png" />
			</div>
		</div>
	</div>
	<div class="content indexProduct4">
		<div class="container">
			<div class="tt">
				<img alt="" src="/assets/img/web/subscribe/index_product_img4.png" />
			</div>
		</div>
	</div>
	<div class="content indexQrCode" id="share">
		<div class="container">
			<div class="qrTT">扫一扫 参与互动</div>
			<div class="qrImg">
				<img alt="" width="100%" src="/assets/img/qr.jpg" />
			</div>
		</div>
	</div>
	<div class="content copyright">
<!-- 		<div class="container">
			Powered by <a href="http://www.lankr.cn/" target="_blank">Lankr</a>
		</div> -->

		 <footer class="footer pt-80">

        <!-- Copyright Bar -->
        <section class="copyright pb-60">
            <div class="container">
                <p class="">
                    &copy; 2016 <a><b>Lankr</b></a>. All Rights Reserved. <a href="http://www.miitbeian.gov.cn">沪ICP备14039441号-10</a>&nbsp;&nbsp;&nbsp; 联系电话：021-62580020</p> <p>
                </p>
            </div>
        </section>
        <!-- End Copyright Bar -->

    </footer>



	</div>

	<div class="sidePBtn">
		<a href="#share" class="btn">
			<div class="icon">
				<img alt="" src="/assets/img/web/subscribe/icon_code.png" />
			</div>
			<div class="tt">扫一扫</div>
		</a>
		<!-- <a href="javascript:;" class="btn subSideBtn">
			<div class="icon">
				<img alt="" src="/assets/img/web/subscribe/icon_subs.png" />
			</div>
			<div class="tt">预 约</div>
		</a> -->
	</div>

	<div id="aboutBg" class="floatParent">
		<div class="aboutBox floatFormBox clearfix">
			<div class="formBanner bfL">
				<img alt="banner1" width="100%"
					src="/assets/img/web/subscribe/about_img.jpg" />
			</div>
			<div class="formMain bfR">
				<div class="lineTitle">关于我们</div>
				<div class="form-group aboutInfo">
					<p>“知了云盒”是由上海翼得营销策划有限公司以及上海郞客信息技术有限公司共同开发，“知了云盒”以网络平台为主，搭建中国医学领域多媒体教育学习互动平台，“知了云盒”涵盖电视盒子，手机，ipad等多媒体数字终端，构建网络一体化的学术交流、沟通平台。</p>
					<p>“知了云盒”内容丰富，涵盖麻醉、内科、外科等各大领域。旨在普及学术知识，促进学术沟通，通过学术知识推送、核心知识的讲授、临床应用技能的培训，全面提升医生临床思维能力和诊断技能。</p>
					<h5 class="tt">联系我们</h5>
					<p>
						<strong>电子信箱：</strong><a href="mailto:bd@z-box.com.cn">bd@z-box.com.cn</a>
					</p>
					<p><strong>服务热线：</strong>400 806 2533</p>
				</div>
			</div>
			<div class="close"></div>
		</div>
	</div>

	<div id="alertBox" class="floatParent">
		<div class="alertBox floatFormBox clearfix">
			<div class="txtC">
				<p>公测申请将于9月23日12点正式开始，</p>
				<p>请您耐心等待。</p>
			</div>
			<div class="close"></div>
		</div>
	</div>

	<div id="comingBox" class="floatParent">
		<div class="alertBox floatFormBox clearfix">
			<div class="txtC">
				<p>第一批公测申请已结束</p>
				<p>请继续关注"知了云盒"微信平台，"知了云盒"即将正式发布</p>
				<p>Coming Soon …</p>
			</div>
			<div class="close"></div>
		</div>
	</div>

	<!-- <div id="activityBg" class="floatParent">
		<div class="activityBox" id="activity1" style="background-image:url('/assets/img/web/subscribe/_temp/act_bg_1.jpg')">
			<div class="actTag">
				<h5 class="tt">菁英汇</h5>
				<div class="desc">
					<p>汇聚小亮点，融合大智慧。分享典型病例手术操作，更有机会获得领域顶尖专家解析。活动火热进行中，等你来加入！</p>
					<p>活动最终解释权归“知了云盒”所有</p>
				</div>
			</div>
			<div class="actTips clearfix">
				<div class="info">
					<h5 class="tt">参赛须知</h5>
					<p>知了云盒是由上海翼得营销策划有限公司以及上海郞客信息技术有限公司共同开发，知了云盒以网络平台为主，搭建中国医学领域多媒体教育学习互动平台，知了云盒涵盖电视盒子，手机，ipad等多媒体数字终端，构建网络一体化的学术交流、沟通平台。</p>
					<p>知了云盒内容丰富，涵盖麻醉、内科、外科等各大领域。旨在普及学术知识，促进学术沟通，通过学术知识推送、核心知识的讲授、临床应用技能的培训，全面提升医生临床思维能力和诊断技能。</p>
				</div>
				<div class="code">
					<div class="img"><img src="/assets/img/web/subscribe/_temp/code_wechat.jpg"></div>
					<div class="tt">手机扫描二维码报名参赛</div>
				</div>
			</div>
			<div class="close">✕</div>
		</div>

		<div class="activityBox" id="activity2" style="background-image:url('/assets/img/web/subscribe/_temp/act_bg_2.jpg')">
			<div class="actTag">
				<h5 class="tt">镜秀前程</h5>
				<div class="desc">
					<p>汇聚小亮点，融合大智慧。分享典型病例手术操作，更有机会获得领域顶尖专家解析。活动火热进行中，等你来加入！</p>
					<p>活动最终解释权归“知了云盒”所有</p>
				</div>
			</div>
			<div class="actTips clearfix">
				<div class="info">
					<h5 class="tt">参赛须知</h5>
					<p>知了云盒是由上海翼得营销策划有限公司以及上海郞客信息技术有限公司共同开发，知了云盒以网络平台为主，搭建中国医学领域多媒体教育学习互动平台，知了云盒涵盖电视盒子，手机，ipad等多媒体数字终端，构建网络一体化的学术交流、沟通平台。</p>
					<p>知了云盒内容丰富，涵盖麻醉、内科、外科等各大领域。旨在普及学术知识，促进学术沟通，通过学术知识推送、核心知识的讲授、临床应用技能的培训，全面提升医生临床思维能力和诊断技能。</p>
				</div>
				<div class="code">
					<div class="img"><img src="/assets/img/web/subscribe/_temp/code_wechat.jpg"></div>
					<div class="tt">手机扫描二维码报名参赛</div>
				</div>
			</div>
			<div class="close">✕</div>
		</div>

		<div class="activityBox" id="activity3" style="background-image:url('/assets/img/web/subscribe/_temp/act_bg_3.jpg')">
			<div class="actTag">
				<h5 class="tt">3D大师秀</h5>
				<div class="desc">
					<p>汇聚小亮点，融合大智慧。分享典型病例手术操作，更有机会获得领域顶尖专家解析。活动火热进行中，等你来加入！</p>
					<p>活动最终解释权归“知了云盒”所有</p>
				</div>
			</div>
			<div class="actTips clearfix">
				<div class="info">
					<h5 class="tt">参赛须知</h5>
					<p>知了云盒是由上海翼得营销策划有限公司以及上海郞客信息技术有限公司共同开发，知了云盒以网络平台为主，搭建中国医学领域多媒体教育学习互动平台，知了云盒涵盖电视盒子，手机，ipad等多媒体数字终端，构建网络一体化的学术交流、沟通平台。</p>
					<p>知了云盒内容丰富，涵盖麻醉、内科、外科等各大领域。旨在普及学术知识，促进学术沟通，通过学术知识推送、核心知识的讲授、临床应用技能的培训，全面提升医生临床思维能力和诊断技能。</p>
				</div>
				<div class="code">
					<div class="img"><img src="/assets/img/web/subscribe/_temp/code_wechat.jpg"></div>
					<div class="tt">手机扫描二维码报名参赛</div>
				</div>
			</div>
			<div class="close">✕</div>
		</div>

		<div class="activityBox" id="activity4" style="background-image:url('/assets/img/web/subscribe/_temp/act_bg_4.jpg')">
			<div class="actTag">
				<h5 class="tt">知了大咖秀</h5>
				<div class="desc">
					<p>汇聚小亮点，融合大智慧。分享典型病例手术操作，更有机会获得领域顶尖专家解析。活动火热进行中，等你来加入！</p>
					<p>活动最终解释权归“知了云盒”所有</p>
				</div>
			</div>
			<div class="actTips clearfix">
				<div class="info">
					<h5 class="tt">参赛须知</h5>
					<p>知了云盒是由上海翼得营销策划有限公司以及上海郞客信息技术有限公司共同开发，知了云盒以网络平台为主，搭建中国医学领域多媒体教育学习互动平台，知了云盒涵盖电视盒子，手机，ipad等多媒体数字终端，构建网络一体化的学术交流、沟通平台。</p>
					<p>知了云盒内容丰富，涵盖麻醉、内科、外科等各大领域。旨在普及学术知识，促进学术沟通，通过学术知识推送、核心知识的讲授、临床应用技能的培训，全面提升医生临床思维能力和诊断技能。</p>
				</div>
				<div class="code">
					<div class="img"><img src="/assets/img/web/subscribe/_temp/code_wechat.jpg"></div>
					<div class="tt">手机扫描二维码报名参赛</div>
				</div>
			</div>
			<div class="close">✕</div>
		</div>

		<div class="activityBox" id="activity5" style="background-image:url('/assets/img/web/subscribe/_temp/act_bg_5.jpg')">
			<div class="actTag">
				<h5 class="tt">镇静随手拍</h5>
				<div class="desc">
					<p>汇聚小亮点，融合大智慧。分享典型病例手术操作，更有机会获得领域顶尖专家解析。活动火热进行中，等你来加入！</p>
					<p>活动最终解释权归“知了云盒”所有</p>
				</div>
			</div>
			<div class="actTips clearfix">
				<div class="info">
					<h5 class="tt">参赛须知</h5>
					<p>知了云盒是由上海翼得营销策划有限公司以及上海郞客信息技术有限公司共同开发，知了云盒以网络平台为主，搭建中国医学领域多媒体教育学习互动平台，知了云盒涵盖电视盒子，手机，ipad等多媒体数字终端，构建网络一体化的学术交流、沟通平台。</p>
					<p>知了云盒内容丰富，涵盖麻醉、内科、外科等各大领域。旨在普及学术知识，促进学术沟通，通过学术知识推送、核心知识的讲授、临床应用技能的培训，全面提升医生临床思维能力和诊断技能。</p>
				</div>
				<div class="code">
					<div class="img"><img src="/assets/img/web/subscribe/_temp/code_wechat.jpg"></div>
					<div class="tt">手机扫描二维码报名参赛</div>
				</div>
			</div>
			<div class="close">✕</div>
		</div>
	</div> -->

	<div id="floatForm" class="floatParent">
		<div class="floatFormBox clearfix" id="step1">
			<div class="slideBox formBanner bfL" id="slideBox">
				<div class="bd">
					<ul>
						<li><img alt="banner1" src="/assets/img/web/subscribe/ad.jpg" /></li>
					</ul>
				</div>
			</div>
			<div class="formMain bfR">
				<div class="lineTitle">预约申请</div>
				<div class="tips">请填写您的手机号以及验证码用来预约申请</div>
				<div class="form-group">
					<div class="label_tag">手机号</div>
					<div class="input_box">
						<div class="input_code">
							<input type="text" name="mobile" id="userMobile"
								class="form-control" placeholder="请输入手机号" required="required" />
						</div>
						<button name="button" type="submit" id="codeBtn">获取验证码</button>
					</div>
				</div>
				<div class="form-group">
					<div class="label_tag">验证码</div>
					<div class="input_box">
						<input type="text" name="validate_code" id="validCode"
							class="form-control" placeholder="请输入验证码" require="true" /> <input
							type="hidden" name="R_verify_code" id="R_verify_code" value="" />
					</div>
				</div>
				<button name="button" type="button" id="nextBtn">
					<span>下一步</span>
				</button>
			</div>
			<div class="close"></div>
		</div>
		<div class="floatFormBox hide" id="step2">
			<div class="formMain">
				<div class="lineTitle">填写用户信息</div>
				<ul class="selectTag">
					<li class="active"><a href="javascript:;" class="btn"><span
							class="icon icon-brand"></span>品牌用户</a></li>
					<li><a href="javascript:;" class="btn"><span
							class="icon icon-user"></span>普通用户</a></li>
				</ul>

				<div class="sub_form">
					<div class="tips txtC">请务必填写您的真实信息，这些信息会影响到您的申请结果</div>
					<div class="form-group line">
						<div class="label_tag">姓 名</div>
						<div class="input_box">
							<input type="text" name="name" id="userName_b"
								class="form-control" placeholder="必填" required="required" />
						</div>
						<div class="label_tag">座 机</div>
						<div class="input_box">
							<input type="tel" name="tel" id="userTel" class="form-control"
								placeholder="格式：021-51111111" required="required" />
						</div>
					</div>
					<div class="form-group">
						<div class="label_tag">企业名称</div>
						<div class="input_box">
							<input type="text" name="companyName" id="companyName"
								class="form-control" placeholder="必填" required="required" />
						</div>
						<div class="label_tag">品牌名称</div>
						<div class="input_box">
							<input type="text" name="brandName" id="brandName"
								class="form-control" placeholder="必填" required="required" />
						</div>
					</div>
					<div class="form-group">
						<div class="label_tag">职 位</div>
						<div class="input_box">
							<input type="text" name="position" id="position"
								class="form-control" placeholder="必填" required="required" />
						</div>
						<div class="label_tag">企业邮箱</div>
						<div class="input_box">
							<input type="email" name="companyEmail" id="companyEmail"
								class="form-control" placeholder="必填，知了云盒激活码将通过邮件或短信方式发给您"
								required="required" />
						</div>
					</div>
					<div class="form-group">
						<div class="label_tag">办公地址</div>
						<div class="input_box long">
							<input type="text" name="companyAddress" id="companyAddress"
								class="form-control" placeholder="必填，我们会将知了云盒快递给您"
								required="required" />
						</div>
					</div>
					<div class="form-group protocol">
						<input type="checkbox" name="accept" id="accept_b" /> <a
							href="/front/protocol.jsp" target="_blank">同意用户协议</a>
					</div>
					<div class="form-group txtC">
						<input type="button" value="提交申请" class="submitBtn"
							id="submitBrand" />
					</div>
				</div>

				<!-- <div class="sub_form hide">
            <div class="form-group line">
              <div class="label_tag">姓　名</div>
              <div class="input_box">
                <input type="text" name="name" id="userName" class="form-control" placeholder="必填" required="required" />
              </div>
            </div>
            <div class="form-group">
              <div class="label_tag">所在省</div>
              <div class="input_box">
                <select name="province" id="province" class="form-control" onchange="changeProvince(this.value);">

                  <option value="">请先选择省份</option>
                </select>
              </div>
              <div class="label_tag">所在市</div>
              <div class="input_box">
                <select name="city" id="city" class="form-control" onchange="changeCity(this.value);" require="true">

                  <option value="">请选择城市</option>
                </select>
              </div>
            </div>
            <div class="form-group">
              <div class="label_tag">医　院</div>
              <div class="input_box">
                <select name="hospital" id="hosipital" class="form-control" require="true">

                  <option value="">请选择城市</option>
                </select>
              </div>
              <div class="label_tag">科　室</div>
              <div class="input_box">
                <select name="department" id="userDepartment" class="form-control" require="true">
                  <option value="">请选择科室</option>
                </select>
              </div>
            </div>
            <div class="form-group">
              <div class="label_tag">职　称</div>
              <div class="input_box">
                <select name="job_title" id="userJobTitle" class="form-control" require="true">
                  <option value="">请选择职称</option>
                  <option value="医师">医师</option>
                  <option value="主治医师">主治医师</option>
                  <option value="副主任医师">副主任医师</option>
                  <option value="主任医师">主任医师</option>
                  <option value="护师">护师</option>
                  <option value="药师">药师</option>
                  <option value="技师">技师</option>
                </select>
              </div>
            </div>
            <div class="form-group protocol">
              <input type="checkbox" name="accept" id="accept" />
              <a href="/front/protocol.jsp" target="_blank">同意用户协议</a>
            </div>
            <div class="form-group txtC">
              <button  class="submitBtn" id="submitNormal" >提交申请</button>
            </div>
          </div> -->
				<div class="sub_form hide">
					<div class="blank">您好，公测申请目前只针对品牌用户，请继续关注"知了云盒"微信平台。</div>
				</div>

			</div>
			<div class="close"></div>
		</div>
		<div class="floatFormBox hide" id="step3">
			<div class="formMain">
				<div class="lineTitle">申请成功</div>
				<div class="successInfo">
					<p>
						<img alt="" src="/assets/img/web/subscribe/feedback_success.png" />
					</p>
					<p>您的预约已记录，</p>
					<p>请耐心等待审核结果。</p>
					<p>
						<a id="finishBtn" href="/">我知道了</a>
					</p>
				</div>
			</div>
			<div class="close"></div>
		</div>
	</div>
	<input id="valid_code" type="hidden" value="" />
	<script type="text/javascript" src="/assets/js/jquery.js"></script>
	<script type="text/javascript" src="/assets/js/web/home.js"></script>
	<script type="text/javascript"
		src="/assets/js/web/jquery.SuperSlide.2.1.1.js"></script>
	<script type="text/javascript" src="/assets/js/web/jquery.nav.js"></script>
	<!-- <script
		src="http://qzonestyle.gtimg.cn/open/qcloud/video/h5/h5connect.js"></script> -->
		<script type="text/javascript" src="/assets/js/qq/h5connect_dev.js"></script>
	<script type="text/javascript" src="/assets/js/wechat/region.js"></script>
	<script type="text/javascript" src="/assets/js/common.js"></script>
	<script src="http://cdn.bootcss.com/Swiper/3.1.7/js/swiper.min.js"></script>
	<script>
		$(document)
				.ready(
						function() {
							browserRedirect();

							var option = {
								"auto_play" : "0",
								"file_id" : "16092504232103694599",
								"app_id" : "1251442335",
								"width" : 517,
								"height" : 300
							};
						var	p = new qcVideo.Player("id_video_container", option,function(e){
								console.log(e)
							});

							$(window).scroll(function() {
								if ($(this).scrollTop() > 0) {
									$('#top').addClass('current');
								} else {
									$('#top').removeClass('current');
								}
							});

							$('#nav').onePageNav({
								currentClass : 'active',
								filter : ':not(.external)'
							});

							// var acvShow = new Swiper('#actShow.swiper-container', {
					  //     slidesPerView: 'auto',
					  //     paginationClickable: true,
					  //     loop: true,
					  //     autoplay: 5000,
				   //      autoplayDisableOnInteraction: false
						 //  });

						 //  $('#actShow .list').find('li').click(function(){
						 //  	var targetLink = $(this).data('target');
						 //  	$('#activityBg').show();
						 //  	$('#' + targetLink).show();
						 //  });

							$('.aboutBtn').click(function() {
								$('#aboutBg').show();
							});

							$('.nav_subBtn, .subscribeBtn, .subSideBtn')
									.click(
											function() {
												var starttime = new Date(
														'2015/09/22 11:30');
												// console.log(starttime);
												var endtime = new Date(
														'2015/09/25 11:30');
												// console.log(endtime);
												var nowtime = new Date();
												// console.log(nowtime);
												var leftsecond = parseInt((starttime
														.getTime() - nowtime
														.getTime()) / 1000);
												var endsecond = parseInt((endtime
														.getTime() - nowtime
														.getTime()) / 1000);
												// console.log(leftsecond);
												// console.log(endsecond);
												if (leftsecond <= 0) {
													$('#floatForm').show();
												} else if (endsecond <= 0) {
													$('#comingBox').show();
												} else {
													$('#alertBox').show();
												}

											});

							$('.floatFormBox').find('.close').each(function() {
								$(this).click(function() {
									$(this).parents('.floatParent').hide();
								});
							});

							// $('.activityBox').find('.close').each(function() {
							// 	$(this).click(function() {
							// 		$(this).parents('.floatParent').hide();
							// 		$(this).parents('.activityBox').hide();
							// 	});
							// });

							setTimeout(function() {
								$('.indexBanner').find('.tag1').fadeIn();
							}, 1000);

							setTimeout(function() {
								$('.indexBanner').find('.tag2').fadeIn();
							}, 3000);

							setTimeout(function() {
								$('.indexBanner').find('.tag2').fadeOut();
								$('#tvBox').fadeIn();
							}, 5000);

							// 预约bannerSlide
							$(".slideBox").slide({
								mainCell : ".bd ul",
								effect : "fold",
								autoPlay : true
							});

							$('.selectTag').find('li').click(
									function() {
										var thisIndex = $(this).index();
										$('.selectTag').find('li').removeClass(
												'active');
										$(this).addClass('active');
										$('.sub_form').hide();
										$('.sub_form').eq(thisIndex).show();
									});

							$('#submitBrand').unbind('click');
							$('#submitBrand')
									.click(
											function() {
												var regTel = /^0\d{2,3}-?\d{7,8}$/; //验证座机号码
												var regEmail = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/; //验证邮箱地址

												if ($('#userName_b').val() == '') {
													alert('请填写您的姓名！');
													return false;
												}
												if ($('#userTel').val() == '') {
													alert('请输入正确的区号加座机号！');
													return false;
												} else if ($('#userTel').val() != ''
														&& !regTel.test($(
																'#userTel')
																.val())) {
													alert('请输入正确的区号加座机号！');
													$('#userTel').val('');
													$('#userTel').focus();
													return false;
												}
												if ($('#brandName').val() == '') {
													alert('请填写您的品牌名称！');
													return false;
												}
												if ($('#companyName').val() == '') {
													alert('请填写您的企业名称！');
													return false;
												}
												if ($('#position').val() == '') {
													alert('请填写您的职位！');
													return false;
												}
												if ($('#companyEmail').val() == '') {
													alert('请填写您的企业邮箱！');
													return false;
												} else if ($('#companyEmail')
														.val() != ''
														&& !regEmail
																.test($(
																		'#companyEmail')
																		.val())) {
													alert('请填写正确的邮箱格式！');
													$('#companyEmail').val('');
													$('#companyEmail').focus();
													return false;
												}
												if ($('#companyAddress').val() == '') {
													alert('请填写您的办公地址！');
													return false;
												}
												if (!($('#accept_b')
														.is(':checked'))) {
													alert('请同意用户协议！');
													return false;
												}

												$
														.post(
																'/f/subscribe/save',
																{
																	valid_code : $(
																			'#valid_code')
																			.val(),
																	userType : '品牌用户',
																	name : $(
																			'#userName_b')
																			.val(),
																	phone : $(
																			'#userTel')
																			.val(),
																	company : $(
																			'#companyName')
																			.val(),
																	group : $(
																			'#brandName')
																			.val(),
																	position : $(
																			'#position')
																			.val(),
																	mail : $(
																			'#companyEmail')
																			.val(),
																	mark : $(
																			'#companyAddress')
																			.val()
																},
																function(data) {
																	if (data.status == 'success') {
																		$(
																				'#step2')
																				.hide();
																		$(
																				'#step3')
																				.show();
																	} else {
																		if (!!data.message) {
																			alert(data.message);
																		} else {
																			alert("保存失败");
																		}
																	}
																});
											});

							$('#submitNormal')
									.bind(
											'click',
											function(event) {
												event.preventDefault();
												if ($('#userName').val() == '') {
													alert('请填写您的姓名！');
													return false;
												}
												if ($('#province').val() == '') {
													alert('请选择省份！');
													return false;
												}
												if ($('#city').val() == '') {
													alert('请选择城市！');
													return false;
												}
												if ($('#hosipital').val() == '') {
													alert('请选择医院！');
													return false;
												}
												if ($('#userDepartment').val() == '') {
													alert('请选择科室！');
													return false;
												}
												if ($('#userJobTitle').val() == '') {
													alert('请选择职位！');
													return false;
												}
												if (!($('#accept')
														.is(':checked'))) {
													alert('请同意用户协议！');
													return false;
												}

												$
														.post(
																'/f/subscribe/save',
																{
																	valid_code : $(
																			'#valid_code')
																			.val(),
																	userType : '普通用户',
																	name : $(
																			'#userName')
																			.val(),
																	hosipitalUuid : $(
																			'#hosipital')
																			.val(),
																	departmentUuid : $(
																			'#userDepartment')
																			.val(),
																	position : $(
																			'#userJobTitle')
																			.val(),
																},
																function(data) {
																	if (data.status == 'success') {
																		$(
																				'#step2')
																				.hide();
																		$(
																				'#step3')
																				.show();
																	} else {
																		if (!!data.message) {
																			alert(data.message);
																		} else {
																			alert("保存失败");
																		}
																	}
																});
											});

							function browserRedirect() {
								var sUserAgent = navigator.userAgent
										.toLowerCase();
								var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
								var bIsIphoneOs = sUserAgent
										.match(/iphone os/i) == "iphone os";
								var bIsMidp = sUserAgent.match(/midp/i) == "midp";
								var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
								var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
								var bIsAndroid = sUserAgent.match(/android/i) == "android";
								var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
								var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
								if (bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7
										|| bIsUc || bIsAndroid || bIsCE
										|| bIsWM) {
									window.location.href = '/wechat/subscribe.jsp';
								}
							}
						});
	</script>
</body>
</html>

