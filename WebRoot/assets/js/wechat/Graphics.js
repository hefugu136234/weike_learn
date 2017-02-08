(function (lib, img, cjs, ss) {

var p; // shortcut to reference prototypes

// library properties:
lib.properties = {
  width: 375,
  height: 547,
  fps: 24,
  color: "#FFFFFF",
  manifest: []
};



// symbols:



(lib.元件1 = function() {
  this.initialize();

  // 图层 1
  this.text = new cjs.Text("生成签名", "14px 'Microsoft YaHei'", "#FFFFFF");
  this.text.lineHeight = 22;
  this.text.setTransform(-26,-8.1);
  // this.text.textAlign = 'center';

  this.shape = new cjs.Shape();
  this.shape.graphics.f("#2866b9").s().p("AmMCkQhSAAAAhTIAAihQAAhTBSAAIMZAAQBSAAAABTIAAChQAABThSAAg");

  this.addChild(this.shape,this.text);
}).prototype = p = new cjs.Container();
p.nominalBounds = new cjs.Rectangle(-48,-16.4,96,32.9);


// stage content:
(lib.Graphics = function(mode,startPosition,loop) {
  this.initialize(mode,startPosition,loop,{});

  // timeline functions:
  this.frame_0 = function() {
    createjs.Touch.enable(stage);
    var _self = this;

    stage.addEventListener("mousedown", mouseDown);
    var shape = new createjs.Shape();
    _self.addChild(shape);

    function mouseDown(e) {
      //console.log(e);

      shape.graphics.beginStroke("#2866b9");
      shape.graphics.moveTo(e.stageX, e.stageY);

      stage.addEventListener("pressmove", mouseMove);
      stage.addEventListener("pressup", mouseUp);
    }

    function mouseMove(e) {
      //console.log(e);
      shape.graphics.lineTo(e.stageX, e.stageY);
    }
    function mouseUp(e) {
      //console.log(e);
      stage.removeEventListener("pressmove", mouseMove);
      stage.removeEventListener("pressup", mouseUp);
    }

    function clearGraphics() {
      var divbox = document.getElementById("imgPrev");
      var divdom = document.getElementById("imageitem");
      var signImg = document.getElementById("signImg");
      if(!divdom)divdom = document.body;
      console.log(getCanvasImage().src);
      // divdom.appendChild(getCanvasImage());
      signImg.src = getCanvasImage().src;
      divbox.style.display = "block";
      shape.graphics.clear();
    }

    _self.savebtn.on("click",clearGraphics);


    function getCanvasImage() {
      shape.cache(0, 0, lib.properties.width, lib.properties.height);
      var image = new Image();
      image.src = shape.getCacheDataURL(); //canvas.toDataURL("image/png");
      shape.uncache();
      return image;
    }
  }

  // actions tween:
  this.timeline.addTween(cjs.Tween.get(this).call(this.frame_0).wait(1));

  // 图层 3
  this.savebtn = new lib.元件1();
  this.savebtn.setTransform(63,30);
  new cjs.ButtonHelper(this.savebtn, 0, 1, 1);

  this.text = new cjs.Text("清除画线，并生成签名", "12px 'Microsoft YaHei'", "#666666");
  this.text.lineHeight = 14;
  this.text.lineWidth = 158;
  this.text.setTransform(121,21.1);

  this.timeline.addTween(cjs.Tween.get({}).to({state:[{t:this.text},{t:this.savebtn}]}).wait(1));

  // 图层 2
  this.shape = new cjs.Shape();
  this.shape.graphics.f("#FFFFFF").s().p("A6jfPMAAAg+eMA1HAAAMAAAA+eg");
  this.shape.setTransform(170,200);

  this.timeline.addTween(cjs.Tween.get(this.shape).wait(1));

}).prototype = p = new cjs.MovieClip();
p.nominalBounds = new cjs.Rectangle(160,200,340,400);

})(lib = lib||{}, images = images||{}, createjs = createjs||{}, ss = ss||{});
var lib, images, createjs, ss;
