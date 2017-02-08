<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta content="template language" name="keywords" />
    <meta content="author" name="author" />
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no" name="viewport" />
    <meta content="yes" name="mobile-web-app-capable" />
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <meta content="black-translucent" name="apple-mobile-web-app-status-bar-style" />
    <title>注册新用户</title>
    <link rel="stylesheet" href="http://cdn.bootcss.com/Swiper/3.1.7/css/swiper.min.css">
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
    <link href="/assets/css/app/reg.css" rel="stylesheet">
    <script type="text/javascript" src="/assets/js/jquery.js"></script>
    <script type="text/javascript" src="http://cdn.bootcss.com/Swiper/3.1.7/js/swiper.min.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/reg.js"></script>
  </head>

  <body>
    <div class="page form-bg">
      <div class="content">
        <div class="form-top-logo">
          <span class="icon icon-logo"></span>
        </div>

        <form name="regForm" id="regForm">
          <div id="step1">
            <div class="form-title">注册信息</div>
            <div class="reg-process mt20 clearfix">
              <div class="box current">
                <div class="icon icon-phone"></div>
                <div class="tt">手机验证</div>
              </div>
              <div class="box">
                <div class="icon icon-edit"></div>
                <div class="tt">完善信息</div>
              </div>
            </div>

            <div class="default-form transparent p10 mt10">
              <label class="form-group with-btn">
                <div class="form-input">
                  <input type="tel" id="mobile" name="mobile" class="form-control" placeholder="请输入手机号码">
                  <button type="button" name="code_btn" class="button btn-blue" id="valid_code_but">获取验证码</button>
                </div>
              </label>
              <label class="form-group">
                <div class="form-input">
                  <input type="text" name="valid_code" id="valid_code" class="form-control" placeholder="请输入验证码">
                </div>
              </label>

              <button type="button" id="next_step1" class="button btn-full btn-cyan btn-radius mt20">下一步</button>
            </div>
          </div>

          <div id="step2" class="hide">
            <div class="form-title">注册信息</div>
            <div class="reg-process mt20 clearfix">
              <div class="box">
                <div class="icon icon-phone"></div>
                <div class="tt">手机验证</div>
              </div>
              <div class="box current">
                <div class="icon icon-edit"></div>
                <div class="tt">完善信息</div>
              </div>
            </div>

            <div class="default-form transparent p10 mt10">
              <label class="form-group">
                <div class="form-label">用户姓名</div>
                <div class="form-input">
                  <input type="text" name="nickname" id="nickname" class="form-control" placeholder="用户姓名不得为空" maxlength="25"/>
                </div>
              </label>
              <label class="form-group">
                <div class="form-label">账户密码</div>
                <div class="form-input">
                  <input type="password" name="password" id="password" class="form-control" placeholder="密码不得为空" maxlength="20"/>
                </div>
              </label>
              <label class="form-group">
                <div class="form-label">确认密码</div>
                <div class="form-input">
                  <input type="password" name="re_password" id="re_password" class="form-control" placeholder="请再次输入密码" maxlength="20"/>
                </div>
              </label>
            </div>

            <div id="normalForm">
              <div class="default-form transparent p10">
                <label id="city" class="form-group form-select">
                  <div class="form-label">所在城市：</div>
                  <div class="form-input">
                    <div class="form-control select-text"></div>
                  </div>
                </label>
                <label id="hosipital" class="form-group form-select">
                  <div class="form-label">医　　院：</div>
                  <div class="form-input">
                    <div class="form-control select-text"></div>
                  </div>
                </label>
                <label id="department" class="form-group form-select">
                  <div class="form-label">科　　室：</div>
                  <div class="form-input">
                    <div class="form-control select-text"></div>
                  </div>
                </label>
                <label class="form-group">
                  <div class="form-label">职位名称：</div>
                  <div class="form-input">
                    <input type="text" name="professor" id="professor" class="form-control" maxlength="80"/>
                  </div>
                </label>

                <div class="form-group protocol">
                  <input type="checkbox" checked="checked" name="protocol" id="protocol_doc" required>
                  <a href="javascript:;" class="link protocol-show-btn">我同意《用户协议》</a>
                  <a href="javascript:;" class="link pull-right regChange_c">我是企业用户</a>
                  <!-- <a href="tel:02131319310" class="link pull-right">客服电话：021-31319310</a> -->
                </div>
              </div>
              <div class="p10">
                <button type="button" id="sub_doc" class="button btn-full btn-cyan btn-radius">完成注册</button>
              </div>
            </div>

            <div id="companyForm" class="hide">
              <div class="default-form transparent p10">
                <label id="company" class="form-group form-select">
                  <div class="form-label">企业名称：</div>
                  <div class="form-input">
                    <div class="form-control select-text"></div>
                  </div>
                </label>
                <label class="form-group">
                  <div class="form-label">职位名称：</div>
                  <div class="form-input">
                    <input type="text" name="professor" id="professor_com" class="form-control" maxlength="25"/>
                  </div>
                </label>

                <div class="form-group protocol">
                  <input type="checkbox" checked="checked" name="protocol" id="protocol_com" required>
                  <a href="javascript:;" class="link protocol-show-btn">我同意《用户协议》</a>
                  <a href="javascript:;" class="link pull-right regChange_n">我是医生用户</a>
                </div>
              </div>
              <div class="p10">
                <button type="button" id="sub_Company" class="button btn-full btn-cyan btn-radius">完成注册</button>
              </div>
            </div>
          </div>

          <input type="hidden" id="hidden_vaild_code" value=""/>
        </form>
      </div>
    </div>

    <div class="page white-bg" id="protocol_modal">
      <div class="content">
        <div class="protocol-modal">
          <h5 class="tt">用户协议</h5>
          <p>上海翼得营销策划有限公司（下称“本公司”）在此特别提醒您（用户）在注册成为用户之前，请认真阅读本《用户协议》（以下简称“本协议”），确保您充分理解本协议中各条款。请您审慎阅读并选择接受或不接受本协议。除非您接受本协议所有条款，否则您无权注册、登录或使用本协议所涉服务。</p>
          <p>本协议适用于注册或使用“知了云盒”平台(包含微信公众号，网站，TV终端等)的一切用户，“知了云盒”平台为本公司运维。您的注册、登录、使用等行为将视为对本协议的接受，并同意接受本协议各项条款的约束。</p>
          <p>知了云盒平台提供医学专业相关学术资料，仅供医疗相关从业人员参考，资料中涉及的药品、医疗器械使用以及相关理念，作为作者个人观点，不代表本公司立场或建议，仅供医疗相关从业人员参考，不能作为诊断及医疗的依据。具体使用请查询相关药品及医疗器械使用说明书。本公司对平台提供所有的医学信息不承担任何责任。</p>
          <h6 class="tag">1、注册及使用</h6>
          <p>1.1　知了云盒平台提供医学专业资讯，仅供医疗相关从业人员参考。您承诺您是医疗相关从业人员，并提交真实有效信息。否则本公司有权拒绝提供服务，并删除该账号。</p>
          <p>1.2　知了云盒平台提供用户注册。知了云盒平台帐号的所有权归本公司所有，用户完成申请注册手续后，获得知了云盒平台帐号的使用权，该使用权仅属于初始申请注册人，禁止赠与、借用、租用、转让或售卖。本公司因经营需要，有权回收用户的知了云盒平台帐号。您的帐号和密码由您自行保管；您应当对以您的帐号进行的所有活动负法律责任。</p>
          <p>1.3　您注册时，在账号名称、头像和简介等注册信息中不得出现违法和不良信息，否则本公司有权拒绝提供服务，并删除该账号。</p>
          <p>1.4　如有必要,本公司有权采取合理方式对您的身份进行核实，并仅对通过认证的用户提供相应特别服务。您未能通过身份认证的，本公司保留停止服务(或部分服务)并删除您相关认证信息的权利。</p>
          <p>1.5　您注册本平台所提供的信息应当真实有效，如提供虚假信息，本公司保留权屏蔽或注销该用户的权利。</p>
          <p>1.6　因您注册或使用本平台而提供或产生的个人信息，将为本公司所知晓，用户完成注册即表明您同意授权本公司因服务于您的目的而使用这些个人信息，包括但不限于向您发出服务请求、服务信息等。本公司搜集的信息包括但不限于用户的姓名、性别、年龄、出生日期、地址、医院和科室情况、所属行业、个人说明等。本公司同意对这些信息的使用将受限于第5条隐私声明中对于个人隐私保护的约束。</p>
          <h6 class="tag">2、用户的权利和责任</h6>
          <p>2.1　用户有权利使用自己的用户名及密码登陆知了云盒平台并使用相关服务。</p>
          <p>2.2　用户有责任妥善保管注册帐号信息及帐号密码的安全，因用户保管不善可能导致遭受盗号或密码失窃，责任由用户自行承担。用户不得盗用他人帐号或转让自己的账号，由以上行为造成的后果由用户自行承担。</p>
          <p>2.3　您必须遵守国家关于互联网信息发布的相关法律法规，您对自己在知了云盒平台上发布的信息承担责任，您不得发布违法信息。您承诺自己在使用知了云盒平台时实施的所有行为均遵守国家法律、法规和知了云盒平台管理规定以及社会公共利益或公共道德。如您违反上述任一规则，导致相应法律后果的发生，您将以自己的名义独立承担所有法律责任。</p>
          <p>2.4　用户应遵守本协议的各项条款，正确、适当地使用知了云盒平台相关服务，如因用户违反本协议中的任何条款，本公司在通知用户后有权依据协议中断或终止对违约用户知了云盒平台帐号提供服务。同时，本公司保留在任何时候收回知了云盒平台帐号、用户名的权利。</p>
          <p>2.5　您不得将涉及医疗纠纷的问题或其它有责任争议的问题在知了云盒平台发布，关于医疗纠纷的问题，请另行咨询律师或相关主管部门寻求援助，本公司有权将此类信息即刻删除，而无需通知。</p>
          <p>2.6　您发现其他用户有违法或违反知了云盒平台管理规定的行为，可以向知了云盒平台进行反应要求处理。</p>
          <p>2.7　您对利用知了云盒平台帐号或本服务传送的病情信息、病例、CT片等所有信息、资料、视频等的真实性、合法性、无害性、准确性、有效性等全权负责与用户所传播的信息相关的任何法律责任由用户自行承担，与本公司无关。如因此给本公司或第三方造成损害的，用户应当依法予以赔偿。</p>
          <p>2.8　知了云盒平台内下设的在线教育平台所有视频的版权属于作者本人，您可以通过平台内分享转发给其他知了云盒平台注册用户。但您不得将平台内作品转载其他平台、出版物或其他用途，否则本公司将保留通过包括法律手段在内的方式进行维权的权利。</p>
          <p>2.9　许可使用权：用户注册即授予本公司通过知了云盒平台独家的、永久的、全世界范围的、免费的许可使用权利，使知了云盒平台有权(全部或部分) 使用、复制、修订、编辑、发布、翻译、分发、执行和展示用户在知了云盒平台的各类信息或制作其衍生作品，和以现在已知或日后开发的任何形式、媒体或技术，将上述信息纳入其它作品内。您注册使用知了云盒平台账号，默认表示您同意，并且也得到患者本人的许可。</p>
          <h6 class="tag">3、本公司的权利和责任</h6>
          <p>3.1　知了云盒平台服务的具体内容由用户提供，本公司进行合理范围之内的管理和维护，通过知了云盒平台协助医生之间的沟通，并提供必要的网络技术帮助；本公司保留随时变更、中断或终止部分或全部网络服务的权利。</p>
          <p>3.2　知了云盒平台作为在线教育的平台，仅仅是提供在线交流的机会，本公司不对所发布的信息、视频、资料等的真实性、实用性、科学性、严肃性做任何形式保证。</p>
          <p>3.3　因系统维护或升级、不可抗力、网络状况、通讯线路、计算机病毒或黑客攻击、系统不稳定、用户自身过错等原因，或其他不可控原因导致服务中断或您不能正常使用知了云盒平台服务，本公司不承担相应责任。</p>
          <p>3.4　对于您在知了云盒平台上的不当行为或其它任何知了云盒平台认为应当终止服务的情况，本公司有权随时作出删除相关信息、终止服务提供等处理，而无需征得您的同意；</p>
          <p>3.5　知了云盒平台没有义务对所有用户的注册数据、所有的活动行为以及与之有关的其它事项进行审查，但知了云盒平台有权根据不同情况选择保留或删除相关信息或继续、停止对该用户提供服务，并追究相关法律责任。</p>
          <p>3.6　知了云盒平台有权对用户的注册数据及活动行为进行查阅，发现注册数据或活动行为中存在任何问题或怀疑，均有权向用户发出询问及要求改正的通知或者直接删除等处理；</p>
          <p>3.7　经任何生效法律文书确认或怀疑用户存在违法行为，或者知了云盒平台有足够事实依据可以认定用户存在违法或违反知了云盒平台管理规定的行为的，知了云盒平台有权以合理方式公布用户的违法行为，并暂停或终止向该用户提供服务；</p>
          <p>3.8　用户理解并同意，因业务发展需要，本公司保留单方面对本服务的全部或部分服务内容变更、暂停、终止或撤销的权利，用户需承担此风险。</p>
          <h6 class="tag">4、服务变更、中断或终止</h6>
          <p>4.1　因系统维护或升级的需要而需暂停网络服务，本公司将尽可能事先进行通告。</p>
          <p>4.2　如发生下列任何一种情形，本公司有权解除本协议，并终止您的全部服务：</p>
          <p class="indent_2">4.2.1　您违反国家有关法律法规或知了云盒平台管理规定，侵害他人合法权益的；</p>
          <p class="indent_2">4.2.2　您因在知了云盒平台上的不当行为被行政或司法机构采取强制措施或起诉；</p>
          <p class="indent_2">4.2.3　您提供的个人资料不真实；</p>
          <p class="indent_2">4.2.4　您盗用他人账户、发布违禁信息、骗取他人财物的；</p>
          <p class="indent_2">4.2.5　您上传虚假信息，经查证属实的；</p>
          <p class="indent_2">4.2.6　其它本公司认为需要终止服务的情况。</p>
          <p>4.3　除前款所述情形外，本公司保留在不事先通知您的情况下随时中断或终止部分或全部网络服务的权利，对于服务的中断或终止而造成您的损失的，知了云盒平台不承担任何责任。</p>
          <p>4.4　服务发生变更、中断、终止后，本公司仍有以下权利：</p>
          <p>4.5　本公司有权保留您的注册数据及以前的行为记录；</p>
          <p>4.6　如您在服务变更、中断、终止前，在知了云盒平台上存在违法行为或违反条款的行为，本公司仍可行使本服务条款所规定的权利</p>
          <h6 class="tag">5、隐私声明</h6>
          <p>5.1　适用范围：您注册知了云盒平台时，根据要求提供的个人信息，以及在使用知了云盒平台服务所产生的所有信息；</p>
          <p>5.2　信息保密：</p>
          <p class="indent_2">5.2.1　保护您的隐私是知了云盒平台的一项基本政策，知了云盒平台保证不对外公开或向第三方提供您的注册资料及您在使用网络服务时存储的非公开内容，但下列情况除外：事先获得您的明确授权；根据有关的法律法规要求；按照相关政府主管部门的要求；为维护社会公众的利益；为维护本公司的合法权益。</p>
          <p class="indent_2">5.2.2　本公司可能会与第三方合作向用户提供相关的网络服务，在此情况下，如该第三方同意承担与知了云盒平台同等的保护用户隐私的责任，则知了云盒平台有权将您的注册资料或者其他信息等提供给该第三方。</p>
          <p>5.3　信息使用：</p>
          <p class="indent_2">5.3.1　在不透露您的保密信息的前提下，本公司有权对整个用户数据库进行分析并对用户数据库进行商业上的利用。</p>
          <p class="indent_2">5.3.2　为服务用户的目的，本公司可能通过使用您的个人信息向您提供服务，包括但不限于向您发出活动和服务信息等。</p>
          <h6 class="tag">6、违约赔偿</h6>
          <p>6.1　您同意保障和维护知了云盒平台及其他用户的利益，如因您违反有关法律、法规或知了云盒平台管理规定而给本公司或任何其他第三人造成损失，您同意承担由此造成的损害赔偿责任。</p>
          <h6 class="tag">7、服务条款修改</h6>
          <p>7.1　本公司有权根据服务情况随时变更、终止知了云盒平台相关的各项服务条款，更新后的协议条款一旦公布即代替原来的协议条款，恕不再另行通知，用户可在知了云盒平台查阅最新版协议条款。</p>
          <p>7.2　如果您不同意知了云盒平台管理规定所做的修改，有权停止使用知了云盒平台服务。如果您继续使用知了云盒平台服务，则视为您接受修改后的知了云盒平台服务条款。</p>
          <h6 class="tag">8、免责声明</h6>
          <p><strong>8.1　您使用知了云盒平台服务所存在的风险将完全由您自己承担；因其使用知了云盒平台服务而产生的一切后果也由您自己承担，本公司对您不承担任何责任。</strong></p>
          <p><strong>8.2　知了云盒平台不承诺知了云盒平台服务一定能满足您的要求，也不承诺知了云盒平台服务不会中断，对知了云盒平台服务的及时性、安全性、准确性也不作承诺。</strong></p>
          <p><strong>8.3　本公司不承诺知了云盒平台网页上设置的外部链接的准确性和完整性，同时，对于该等外部链接指向的不由本公司实际控制的任何网页上的内容，知了云盒平台不承担任何责任。</strong></p>
          <p>8.4　本公司不对用户在本服务中相关数据的删除或储存失败负责。</p>
          <p>8.5　本公司可以根据实际情况自行决定用户在本服务中数据的最长储存期限，并在服务器上为其分配数据最大存储空间等。用户可根据自己的需要自行备份本服务中的相关数据。</p>
          <p>8.6　如用户停止使用本服务或本服务终止，本公司可以从服务器上永久地删除用户的数据。本服务停止、终止后，本公司没有义务向用户返还任何数据。</p>
          <h6 class="tag">9、其他</h6>
          <p>9.1　本公司郑重提醒用户注意本协议中免除本公司责任和限制用户权利的条款，请用户仔细阅读，自主考虑风险。</p>
          <p>9.2　本协议的效力、解释及纠纷的解决，适用于中华人民共和国法律。若用户和本公司之间发生任何纠纷或争议，首先应友好协商解决，协商不成的，用户同意将纠纷或争议提交本公司住所地有管辖权的人民法院通过诉讼解决。</p>
          <p>9.3　本协议的任何条款无论因何种原因无效或不具可执行性，不影响其他条款的效力，对双方具有约束力。无效或不具可执行性的条款应在法律允许的范围内自动进行修改以反映双方的真实意图。</p>
        </div>

        <div class="p10">
          <a href="javascript:;" class="button btn-full btn-cyan btn-radius closePageBtn">已阅读并同意协议</a>
        </div>
      </div>
    </div>

    <div class="page" id="reg_banner_page">
      <div class="content">
        <div class="swiper-container reg-banner" id="reg_banner">
          <ul class="swiper-wrapper">
            <li class="swiper-slide bg1">
              <div class="tt">
                <h5>/ 方便快捷 /</h5>
                <p>移动医学教育平台</p>
              </div>
              <div class="bottom-area">
                <div class="info">
                  <p><span class="num">7</span>大学科；</p>
                  <p>超过<span class="num">500</span>个医学课件</p>
                </div>
              </div>
            </li>
            <li class="swiper-slide bg2">
              <div class="tt">
                <h5>/ 内容丰富 /</h5>
                <p>知了微课等你来加入</p>
              </div>
              <div class="bottom-area">
                <div class="btn" id="close_reg_banner">立即体验</div>
              </div>
            </li>
          </ul>
          <div class="swiper-pagination" id="reg_banner_pagination"></div>
        </div>
      </div>
    </div>
  </body>
</html>
