package com.reservation.controller;

import com.reservation.dto.board.CommentCreateRequest;
import com.reservation.jwt.dto.TokenDto;
import com.reservation.service.BoardService;
import com.reservation.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;
  private final BoardService boardService;

  @PostMapping("/{boardId}")
  public String addComments(@PathVariable Long boardId, @ModelAttribute CommentCreateRequest req,
      @AuthenticationPrincipal TokenDto dto, Model model) {
    commentService.writeComment(boardId, req, dto.getId());

    model.addAttribute("message", "댓글이 추가되었습니다.");
    model.addAttribute("nextUrl", "/boards/" + boardService.getCategory(boardId) + "/" + boardId);
    return "printMessage";
  }

  @PostMapping("/{commentId}/edit")
  public String editComment(@PathVariable Long commentId, @ModelAttribute CommentCreateRequest req,
      @AuthenticationPrincipal TokenDto dto, Model model) {
    Long boardId = commentService.editComment(commentId, req.getBody(),dto.getId());
    model.addAttribute("message", boardId == null? "잘못된 요청입니다." : "댓글이 수정 되었습니다.");
    model.addAttribute("nextUrl", "/boards/" + boardService.getCategory(boardId) + "/" + boardId);
    return "printMessage";
  }

  @GetMapping("/{commentId}/delete")
  public String deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal TokenDto dto, Model model) {
    Long boardId = commentService.deleteComment(commentId, dto.getId());
    model.addAttribute("message", boardId == null? "작성자만 삭제 가능합니다." : "댓글이 삭제 되었습니다.");
    model.addAttribute("nextUrl", "/boards/" + boardService.getCategory(boardId) + "/" + boardId);
    return "printMessage";
  }
}
