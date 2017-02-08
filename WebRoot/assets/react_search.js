//先写出来，再完善拆分
var SearchFromTotal=React.createClass({
	handleUp:function(searchData){
		this.props.searchTop(searchData);
	},
	render:function(){
		return  (
			<div className="search-bar">
			<a href="/api/webchat/index" className="cancel-btn">取消</a>
			<SearchFrom searchUp={this.props.searchTop}/>
			</div>
		);
	}

});

var SearchFrom=React.createClass({
	handleSubmit:function(e){
		e.preventDefault();
		var search_value=this.refs.search.value.trim();
		if(!search_value){
			return ;
		}
		this.props.searchUp({'search':search_value});
		// this.props.searchTop({'search':search_value});
		return ;
	},
	render:function(){
		return (
			<form onSubmit={this.handleSubmit}>
				<div className="input-box">
					<input type="search" className="input" ref="search"/>
				</div>
				<button type="submit" className="sub-btn icon-search"></button>
			</form>
		);
	}

});

var SearchListItem=React.createClass({
	render:function(){
		var item=this.props.itemData;
		var type = item.type;
		var uuid = item.uuid;
		var name = item.name;
		var cover = item.cover;
		var speakerName = item.speakerName;
		var hospitalName = item.hospitalName;
		var catgoryName = item.catgoryName;
		var viewCount = item.viewCount;
		name = dealSpanStr(name, 34);
		var url = '/api/webchat/resource/first/view/' + uuid;
		var fileClass = 'img-tag tr', filename = '';
		if (type == 'VIDEO') {
			fileClass += ' video-type';
			filename = '视频';
		} else if (type == 'PDF') {
			fileClass += ' pdf-type';
			filename = 'PDF';
		} else if (type == 'THREESCREEN') {
			fileClass += ' ppt-type';
			filename = '课件';
		} 
		return (
			<a href={url} className="box">
			<div className="img"><img src={cover}/>
				<div className={fileClass}>{filename}</div>
				<div className="img-tag br view-num"><span className="icon icon-eye"></span>{viewCount}</div>
				</div>
				<div className="info">
				<h5 className="tt">{name}</h5>
				<div className="desc">
				<p><span>{catgoryName}</span></p>
				<p><span>{speakerName}</span> | <span>{hospitalName}</span></p>
				</div></div><div className="icon icon-arr-r"></div>
				</a>
			);
	}

});


var SearchListBox=React.createClass({
	isItemList:function(){
		var list=this.props.listData.items;
		if (!!list && list.length > 0) {
		return true
		}
		return false;
	},
	render:function(){
		var listItem;
		if(this.isItemList()){
			var hits=this.props.listData.hits;
			var items=this.props.listData.items;
			var listNodes=items.map(function(item){
				return (<SearchListItem itemData={item}/>);
			});
			listItem=(
				<div>
				<div className="search-status-tag">共有<span className="num">{hits}</span>条记录</div>
				<div className="list-with-img with-arr p10">
				{listNodes}
				</div>
				</div>
				);
		}else{
			listItem=(<SearchNoList />);
		}
		return (
				<div>
				{listItem}
				</div>
		);

	}

});

var SearchNoList=React.createClass({
	render:function(){
		return (
				<div className="search-nothing">
				<div className="icon">
					<img src="/assets/img/app/search_noting.png" width="11%"/>
				</div>
				<div className="info">
					<p>对不起</p>
					<p>没有搜索到相对应的信息</p>
				</div>
			</div>
		);
	}

});


var SearchHotWord=React.createClass({
	handleClick:function(param,event){
		event.preventDefault();
		this.props.searchTop({'search':param});
	},
	render:function(){
		return (
			<div>
			<div className="search-status-tag">热门关键字</div>
		   <div className="search-hot clearfix">
			<a href="javascript:void(0);" data-search="MDT" onClick={this.handleClick.bind(this,'MDT')}>1. MDT</a>
			<a href="javascript:void(0);" data-search="腹腔镜" onClick={this.handleClick.bind(this,'腹腔镜')}>2. 腹腔镜</a>
			<a href="javascript:void(0);" data-search="肿瘤" onClick={this.handleClick.bind(this,'肿瘤')}>3. 肿瘤</a>
			<a href="javascript:void(0);" data-search="胃癌" onClick={this.handleClick.bind(this,'胃癌')}>4. 胃癌</a>
		</div>
		</div>
			);
	}

});

var SearchOtherWord=React.createClass({
	handleClick:function(param,event){
		event.preventDefault();
		this.props.searchTop({'search':param});
	},
	render:function(){
		return (
		<div className="search-others">
			<h5 className="tt">别的同道也在搜：</h5>
			<div className="list clearfix">
				<a href="javascript:void(0);" onClick={this.handleClick.bind(this,'指南')} className="items">指南</a>
				<a href="javascript:void(0);" data-search="胰腺炎" onClick={this.handleClick.bind(this,'胰腺炎')} className="items">胰腺炎</a>
				<a href="javascript:void(0);" data-search="直播" onClick={this.handleClick.bind(this,'直播')} className="items">直播</a>
			</div>
		</div>
		);
	}

});

var SearchPage=React.createClass({
	getInitialState:function(){
		var query='';
		var hits=0;
		var items=[];
		var searchData={query:query,hits:hits,items:items};
		return {searchData:searchData};
	},
	isSuccess:function(data){
		if(data.status == 'success'){
			return true;
		}
		return false;

	},
	handleTop:function(searchData){
		var dataTime = new Date().getTime();
		searchData.dataTime=dataTime;
		$.getJSON(this.props.url,searchData,function(data){
			if(this.isSuccess(data)){
				this.setState({searchData:data});
			}else{
				console.log(data);
			}

		}.bind(this));

	},
	render:function(){
		return (
			<div>
			<SearchFromTotal searchTop={this.handleTop}/>
			<div className="content">
			<SearchListBox listData={this.state.searchData}/>
			<SearchHotWord searchTop={this.handleTop}/>
			<SearchOtherWord searchTop={this.handleTop}/>
			</div>
			</div>
			);
	}
});

ReactDOM.render(
  <SearchPage url="/api/webchat/search/action/result"/>,
  document.getElementById('page_body')
);

