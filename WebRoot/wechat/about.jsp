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
    <title>关于我们</title>
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
    <link href="/assets/css/app/about.css" rel="stylesheet">

    <!-- <script src="//cdnjs.cloudflare.com/ajax/libs/three.js/r63/three.min.js"></script> -->
  </head>

  <body>
    <div class="page">
      <div class="content">
        <div class="about-container">
          <div class="title">
            <img alt="" width="35%" src="/assets/img/app/about/title.png" />
          </div>
          <div class="img">
            <img alt="" width="20%" src="/assets/img/app/logo_wf.png" />
          </div>
          <div class="info">
            <p>“知了云盒”是由上海翼得营销策划有限公司以及上海郞客信息技术有限公司共同开发，“知了云盒”以网络平台为主，搭建中国医学领域多媒体教育学习互动平台，“知了云盒”涵盖电视盒子，手机，ipad等多媒体数字终端，构建网络一体化的学术交流、沟通平台。</p>
            <p>“知了云盒”内容丰富，涵盖麻醉、内科、外科等各大领域。旨在普及学术知识，促进学术沟通，通过学术知识推送、核心知识的讲授、临床应用技能的培训，全面提升医生临床思维能力和诊断技能。</p>
          </div>
          <div class="company">
            <p>合作伙伴</p>
            <p><img alt="" width="16%" src="/assets/img/app/icon_bkx.png" /></p>
            <p class="copyright">Copyright 2015-2016</p>
          </div>
        </div>
      </div>
    </div>

    <!-- <canvas id="myCanvas" height="200">
      您的浏览器不支持canvas！
    </canvas> -->

    <!-- <script>
      var grayscale = function (pixels) {
        var d = pixels.data;
        for (var i = 0; i < d.length; i += 4) {
          var r = d[i];
          var g = d[i + 1];
          var b = d[i + 2];
          d[i] = d[i + 1] = d[i + 2] = (r+g+b)/3;
        }
        return pixels;
      };

      var image = new Image();
      image.onload = function() {
        var canvas = document.createElement('canvas');
        canvas.width = image.width;
        canvas.height = image.height;
        canvas.getContext('2d').drawImage(image, 0, 0);
        // 插入页面底部
        document.body.appendChild(image);
        return canvas;
      }
      image.src = '/assets/img/app/about/logo.png';

      var body_width = document.body.clientWidth;
      var canvas = document.getElementById('myCanvas');
      canvas.width = body_width;
      if (canvas.getContext) {
        var context = canvas.getContext('2d');
      }

      if (canvas.width > 0 && canvas.height > 0) {
        var imageData = context.getImageData(0, 0, canvas.width, canvas.height);
          grayscale(imageData);
          context.putImageData(imageData, 0, 0);
      }

      // ctx.beginPath(); // 开始路径绘制
      // var posX = 20,
      //     posY = 100,
      //     vx = 10,
      //     vy = -10,
      //     gravity = 1;

      // setInterval(function() {
      //   posX += vx;
      //   posY += vy;
      //   if (posY > canvas.height * 0.75) {
      //     vy *= -0.6;
      //     vx *= 0.75;
      //     posY = canvas.height * 0.75;
      //   }
      //   vy += gravity;
      //   context.fillStyle = "black";
      //   context.fillRect(0,0,canvas.width, canvas.height);

      //   posX += 1;
      //   posY += 0.25;

      //   context.beginPath();
      //   context.fillStyle = "white";

      //   context.arc(posX, posY, 10, 0, Math.PI*2, true);
      //   context.closePath();
      //   context.fill();
      // }, 30);
    </script> -->

    <!-- <div id="container"></div>

    <script>
      var WIDTH = window.innerWidth - 30,
          HEIGHT = window.innerHeight - 30;

      var angle = 45,
          aspect = WIDTH / HEIGHT,
          near = 0.1,
          far = 3000;

      var container = document.getElementById('container');

      var camera = new THREE.PerspectiveCamera(angle, aspect, near, far);
      camera.position.set(0, 0, 0);
      var scene = new THREE.Scene();

      var light = new THREE.SpotLight(0xFFFFFF, 1, 0, Math.PI / 2, 1);
      light.position.set(4000, 4000, 1500);
      light.target.position.set (1000, 3800, 1000);

      scene.add(light);

      var earthGeo = new THREE.SphereGeometry (30, 40, 400),
          earthMat = new THREE.MeshPhongMaterial();

      // diffuse map
      earthMat.map = THREE.ImageUtils.loadTexture('https://s3-us-west-2.amazonaws.com/s.cdpn.io/123941/earthmap.jpg');

      // bump map
      earthMat.bumpMap = THREE.ImageUtils.loadTexture('https://s3-us-west-2.amazonaws.com/s.cdpn.io/123941/bump-map.jpg');
      earthMat.bumpScale = 8;

      var earthMesh = new THREE.Mesh(earthGeo, earthMat);
      earthMesh.position.set(-100, 0, 0);
      earthMesh.rotation.y=5;

      scene.add(earthMesh);

      camera.lookAt( earthMesh.position );

      //renderer
      var renderer = new THREE.WebGLRenderer({antialiasing : true});
      renderer.setSize(WIDTH, HEIGHT);
      renderer.domElement.style.position = 'relative';

      container.appendChild(renderer.domElement);
      renderer.autoClear = false;
      renderer.shadowMapEnabled = true;

      function animate() {
         requestAnimationFrame(animate);
         render();
      }

      function render() {
         var clock = new THREE.Clock();
         var delta = clock.getDelta();

         earthMesh.rotation.y += 0.2 * delta;
         renderer.render(scene, camera);
      }

      animate();

    </script> -->
  </body>
</html>
