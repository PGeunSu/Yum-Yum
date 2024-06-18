package com.reservation.dto.board;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class BoardCntDto {


  private Long totalNoticeCnt;
  private Long totalBoardCnt;

}
