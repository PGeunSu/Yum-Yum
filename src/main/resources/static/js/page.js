window.onload = function () {
      let nowPage = [[${boards.getNumber()}]] + 1;    // 현재 페이지
      let totalPage = [[${boards.getTotalPages()}]];  // 전체 페이지 수

      let firstPage;  // 화면에 출력될 첫 페이지
      for (let i = nowPage ; i >= 1 ; i --) {
          if(i % 5 == 1) {
              firstPage = i;
              break;
          }
      }

      let lastPage;   // 화면에 출력될 마지막 페이지
      let nextButton; // 다음 버튼 출력 여부
      if (firstPage + 4 >= totalPage) {
          lastPage = totalPage;
          nextButton = false;
      } else {
          lastPage = firstPage + 4;
          nextButton = true;
      }

      // HTML 생성
      let pageHtml = "";
      pageHtml += "<li><a class='page-link' href='" + makeUrl(1) +  "'>&laquo;</a></li>";
      if (firstPage != 1) {
          pageHtml += "<li><a class='page-link' href='" + makeUrl(firstPage - 1) +  "'>&lsaquo;</a></li>";
      }

      for (let i = firstPage; i <= lastPage; i++) {
          if (i == nowPage) {
              pageHtml += "<li class='page-item active'><a class= 'page-link'>" + i + "</a></li>";
          } else {
              pageHtml += "<li class='page-item'><a class= 'page-link' href='" + makeUrl(i) + "'>" + i + "</a></li>";
          }
      }

      if (nextButton) {
          pageHtml += "<li><a class='page-link' href='" + makeUrl(lastPage + 1) +  "'>&rsaquo;</a></li>";
      }
      pageHtml += "<li><a class='page-link' href='" + makeUrl(totalPage) +  "'>&raquo;</a></li>";

      $("#paging-ul").html(pageHtml);
  }

//  function makeUrl(page) {
//      let category = [[${category}]];
//      let url = "/boards/" + category + "?page=" + page;
//
//      // 검색 했으면 다음 URL에도 추가
//      let sortType = [[${boardSearchRequest.sortType}]];
//      let searchType = [[${boardSearchRequest.searchType}]];
//      let keyword = [[${boardSearchRequest.keyword}]];
//
//      if (sortType != null) {
//          url += "&sortType=" + sortType;
//      }
//      if (searchType != null) {
//          url += "&searchType=" + searchType + "&keyword=" + keyword;
//      }
//
//      return url;
//  }