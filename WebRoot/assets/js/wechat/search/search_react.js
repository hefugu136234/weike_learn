var ResourceItem = React.createClass({
  render: function() {
    var item = this.props.itemData,
        type = item.type,
        uuid = item.uuid,
        name = item.name,
        cover = item.cover,
        speaker_name = item.speakerName,
        hospital_name = item.hospitalName,
        catgory_name = item.catgoryName,
        view_count = item.viewCount,
        url = '/api/webchat/resource/first/view/' + uuid,
        fileClass = 'img-tag tr',
        filename = '';
    name = dealSpanStr(name, 34);
    if (type == 'VIDEO') {
      fileClass += ' video-type';
      filename = '视频';
    } else if (type == 'PDF') {
      fileClass += ' pdf-type';
      filename = 'PDF';
    } else if (type == 'THREESCREEN') {
      fileClass += ' ppt-type';
      filename = '课件';
    } else if (type == 'VR') {
      fileClass += ' vr-type';
      filename = 'VR';
    }

    return (
      <a href={url} className="box">
        <div className="img">
          <img src={cover} />
          <div className={fileClass}>{filename}</div>
          <div className="img-tag br view-num">
            <span className="icon icon-eye"></span>{view_count}
          </div>
        </div>
        <div className="info">
          <h5 className="tt">{name}</h5>
          <div className="desc">
            <p><span>{catgory_name}</span></p>
            <p><span>{speaker_name}</span> | <span>{hospital_name}</span></p>
          </div>
        </div>
        <div className="icon icon-arr-r"></div>
      </a>
    );
  }
});

var SearchResult = React.createClass({
  render: function() {
    var result_content;
    var items = this.props.listData.items;
    if (!!items && items.length > 0) {
      var view_counts = this.props.listData.hits;
      var items_list = items.map(function(item, i){
        return (<ResourceItem itemData={item} key={i} />);
      });
      result_content = (
        <div>
          <div className="search-status-tag">共有<span className="num">{view_counts}</span>条记录</div>
          <div className="list-with-img with-arr p10">{items_list}</div>
        </div>
      );
    } else if (typeof(items) == "undefined") {
      result_content = (<SearchNothing />);
    }

    return (
      <div>
        {result_content}
      </div>
    );
  }
});

var SearchNothing = React.createClass({
  render: function() {
    return (
      <div className="search-nothing">
        <div className="info">
          <p>对不起</p>
          <p>没有搜索到相对应的信息</p>
        </div>
      </div>
    );
  }
});

var SearchHot = React.createClass({
  handleClick: function(key){
    this.props.onSearchRun({
      'search': key
    });
  },

  render: function() {
    return (
      <div>
        <div className="search-status-tag">热门搜索</div>
        <div className="search-hot clearfix">
          <div className="item" onClick={this.handleClick.bind(this, "MDT")}>1. MDT</div>
          <div className="item" onClick={this.handleClick.bind(this, "腹腔镜")}>2. 腹腔镜</div>
          <div className="item" onClick={this.handleClick.bind(this, "肿瘤")}>3. 肿瘤</div>
          <div className="item" onClick={this.handleClick.bind(this, "胃癌")}>4. 胃癌</div>
        </div>
      </div>
    );
  }
});

var SearchOthers = React.createClass({
  handleClick: function(key){
    this.props.onSearchRun({
      'search': key
    });
  },

  render: function() {
    return (
      <div className="search-others">
        <h5 className="tt">别的同道也在搜：</h5>
        <div className="list clearfix">
          <div className="item" onClick={this.handleClick.bind(this, "指南")}>指南</div>
          <div className="item" onClick={this.handleClick.bind(this, "胰腺炎")}>胰腺炎</div>
          <div className="item" onClick={this.handleClick.bind(this, "直播")}>直播</div>
        </div>
      </div>
    );
  }
});

var SearchBar = React.createClass({
  handleSubmit: function(e) {
    e.preventDefault();
    var searchInput = this.refs.searchInput;
    var searchValue = searchInput.value.trim();
    if (!searchValue) {
      alert("请输入要搜索的关键字！");
      searchInput.focus();
      return ;
    } else {
      this.props.onSearchRun({
        "search": searchValue
      });
    }
  },

  handleChange: function(e) {
    var searchInput = this.refs.searchInput;
    var searchValue = searchInput.value.trim();
    // if (searchValue == "") {
    //   return ;
    // }
    this.props.onSearchRun({
      "search": searchValue
    });
  },

  render: function() {
    var value_key = this.props.searchKey.query;
    return (
      <div className="search-bar">
        <a href="/api/webchat/index" className="cancel-btn">取消</a>
        <form onSubmit={this.handleSubmit}>
          <div className="input-box">
            <input
              type="search"
              className="input"
              maxLength="50"
              placeholder="多个关键字空格隔开查询"
              ref="searchInput"
              // value={value_key}
              // onChange={this.handleChange}
            />
          </div>
          <button type="submit" className="sub-btn icon-search"></button>
        </form>
      </div>
    );
  }
});

var SearchPage = React.createClass({
  getInitialState: function() {
    var query = '',
        view_counts = 0,
        items = [],
        searchData = {
          query: query,
          hits: view_counts,
          items: items
        };
    return {
      searchData: searchData
    };
  },

  handleSearchRun: function(searchData) {
    $.getJSON(
      this.props.api,
      searchData,
      function(data) {
        if (data.status == "success") {
          this.setState({
            searchData: data
          });
          console.log(searchData);
        } else {
          console.log(data);
        }
      }.bind(this)
    );
  },

  render: function() {
    return (
      <div className="page search-page">
        <SearchBar onSearchRun={this.handleSearchRun} searchKey={this.state.searchData} />
        <div className="content">
          <SearchResult listData={this.state.searchData} />
          <SearchHot onSearchRun={this.handleSearchRun} />
          <SearchOthers onSearchRun={this.handleSearchRun} />
        </div>
      </div>
    );
  }
});

ReactDOM.render(
  <SearchPage api="/api/webchat/search/action/result" />,
  document.getElementById('search_container')
);
